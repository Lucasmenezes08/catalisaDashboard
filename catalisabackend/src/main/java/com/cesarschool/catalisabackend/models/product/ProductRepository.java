package com.cesarschool.catalisabackend.models.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByNameIgnoreCase(String name);

    // use este padrão para delete:
    long deleteByNameIgnoreCase(String name); // << troque o método

    boolean existsByNameIgnoreCase(String name);

    List<Product> findAllByTypeOrderByNameAsc(ProductType type);

    // Se quiser manter busca por texto, foque em name/description.
    // Para filtrar por type, passe como parâmetro separado (p.type = :type).
    @Query("""
       select p from Product p
       where (:q is null or :q = ''
          or lower(p.name) like lower(concat('%', :q, '%'))
          or lower(coalesce(p.description, '')) like lower(concat('%', :q, '%'))
       )
    """)
    List<Product> search(@Param("q") String q);

    // Ex.: outra assinatura caso queira filtrar também por type
    @Query("""
       select p from Product p
       where (:q is null or :q = ''
          or lower(p.name) like lower(concat('%', :q, '%'))
          or lower(coalesce(p.description, '')) like lower(concat('%', :q, '%'))
       )
       and (:type is null or p.type = :type)
    """)
    List<Product> search(@Param("q") String q, @Param("type") ProductType type);
}
