-- V2__create-tables.sql

-- ==========================================
-- Limpeza (cuidado em produção!)
-- ==========================================
DROP TABLE IF EXISTS pesquisas CASCADE;
DROP TABLE IF EXISTS consumos CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==========================================
-- Tabela users (entidade User)
-- ==========================================
CREATE TABLE users (
                       id          BIGSERIAL PRIMARY KEY,
                       email       VARCHAR(255) NOT NULL UNIQUE,
                       cpfCnpj     VARCHAR(50)  NOT NULL UNIQUE,
                       username    VARCHAR(255),
                       password    VARCHAR(255) NOT NULL
);

-- ==========================================
-- Tabela products (entidade Product)
-- ==========================================
CREATE TABLE products (
                          id          BIGSERIAL PRIMARY KEY,
                          name        VARCHAR(255) NOT NULL,
                          type        VARCHAR(50)  NOT NULL,
                          description TEXT,
                          CONSTRAINT chk_products_type
                              CHECK (type IN ('CURSO', 'VIDEO_AULA', 'ARTIGO'))
);

-- ==========================================
-- Tabela consumos (entidade Consumo)
-- ==========================================
CREATE TABLE consumos (
                          id               BIGSERIAL PRIMARY KEY,
                          user_id          BIGINT       NOT NULL,
                          product_id       BIGINT       NOT NULL,
                          consumiuPesquisa BOOLEAN      NOT NULL,
                          data_consumo     DATE         NOT NULL,

                          CONSTRAINT fk_consumos_user
                              FOREIGN KEY (user_id) REFERENCES users(id),

                          CONSTRAINT fk_consumos_product
                              FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_consumos_user_id    ON consumos(user_id);
CREATE INDEX idx_consumos_product_id ON consumos(product_id);

-- ==========================================
-- Tabela pesquisas (entidade Pesquisa)
-- ==========================================
CREATE TABLE pesquisas (
                           id           BIGSERIAL PRIMARY KEY,
                           consumo_id   BIGINT      NOT NULL,
                           nota         INTEGER     NOT NULL,
                           resposta     TEXT,
                           dataPesquisa DATE        NOT NULL,
                           tipoPesquisa VARCHAR(50) NOT NULL,
                           tipoCliente  VARCHAR(50) NOT NULL,

                           CONSTRAINT fk_pesquisas_consumo
                               FOREIGN KEY (consumo_id) REFERENCES consumos(id),
                           CONSTRAINT uq_pesquisas_consumo
                               UNIQUE (consumo_id),

                           CONSTRAINT chk_pesquisas_nota
                               CHECK (nota >= 0 AND nota <= 10),

                           CONSTRAINT chk_pesquisas_tipoPesquisa
                               CHECK (tipoPesquisa IN ('CSAT', 'NPS')),

                           CONSTRAINT chk_pesquisas_tipoCliente
                               CHECK (tipoCliente IN ('PROMOTOR', 'NEUTRO', 'DETRATOR'))
);

CREATE INDEX idx_pesquisas_consumo_id ON pesquisas(consumo_id);
