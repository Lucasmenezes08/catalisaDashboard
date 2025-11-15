export interface User {
    id: number;
    username: string;
    email: string;
}

// --- Product ---
export interface ProductResponseDTO {
    id: number;
    name: string;
    type: string;
    description: string;
}

// --- Consumo ---
export interface ConsumoResponseDTO {
    id: number;
    userId: number | null;
    productId: number | null;
    dataConsumo: string; // yyyy-MM-dd
    pesquisaRespondida: boolean;
    pesquisaId: number | null;
}

// DTO para CRIAR um consumo (Baseado na documentação e uso)
export interface ConsumoRequestDTO {
    user: { id: number };
    product: { id: number };
    consumiuPesquisa: boolean;
    pesquisa: { id: number } | null;
    dataConsumo: string; // yyyy-MM-dd
}

// --- Pesquisa ---
export interface PesquisaRequestDTO {
    consumoId: number;
    nota: number;
    dataPesquisa: string; // yyyy-MM-dd
    tipoPesquisa: string; // "NPS", "CSAT", etc.
    resposta: string | null;
}


export interface PesquisaBoxProps {
    consumo: ConsumoResponseDTO;
}