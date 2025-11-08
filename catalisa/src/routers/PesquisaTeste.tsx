// ----- PesquisaTeste.tsx -----
// (Página para listar e gerenciar pesquisas existentes)

import { useEffect, useState } from "react";
import { Spinner } from "@/components/common/animation-spinner.tsx"; // (Assumindo que você tem este componente)

// --- Tipagem baseada na sua documentação ---

// Response (recebido)
interface PesquisaResponse {
    id: number;
    consumo: {
        id: number;
        // ...outros dados do consumo que a API retornar
    };
    perguntas: { id: number; texto?: string }[];
    respostas: { id: number; resposta?: string }[];
}

// Corpo: Page de PesquisaResponseDTO
interface PageResponse {
    content: PesquisaResponse[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number; // current page
}

// --- Componente de Teste ---

export default function PesquisaTeste() {
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [pesquisas, setPesquisas] = useState<PesquisaResponse[]>([]);

    const API_URL = "http://localhost:8080/api/v1/pesquisas";

    // --- LÓGICA DE BUSCA (GET) ---
    const fetchPesquisas = async () => {
        setIsLoading(true);
        setError(null);
        try {
            // Usando os query params da sua documentação
            const response = await fetch(`${API_URL}?page=0&size=20&sort=id,desc`);

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Falha ao buscar pesquisas");
            }

            const data: PageResponse = await response.json();
            setPesquisas(data.content);

        } catch (err: any) {
            console.error("Erro ao buscar pesquisas:", err);
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    // --- LÓGICA DE DELETAR (DELETE) ---
    const handleDelete = async (id: number) => {
        if (!window.confirm(`Tem certeza que deseja deletar a Pesquisa ID: ${id}?`)) {
            return;
        }

        try {
            const response = await fetch(`${API_URL}/${id}`, {
                method: 'DELETE'
            });

            // Sucesso (204 No Content)
            if (response.status === 204) {
                // Remove da lista local
                setPesquisas(prevPesquisas =>
                    prevPesquisas.filter(p => p.id !== id)
                );
            } else {
                // Trata erros (404, 409)
                const errorData = await response.json();
                console.error("Erro ao deletar:", errorData);
                alert(`Falha ao deletar: ${errorData.message}`);
            }

        } catch (err: any) {
            console.error("Erro na requisição DELETE:", err);
            alert("Um erro ocorreu. Veja o console.");
        }
    };

    // --- EFEITO INICIAL ---
    useEffect(() => {
        fetchPesquisas();
    }, []);


    // --- RENDERIZAÇÃO ---
    return (
        <div className="p-10 max-w-4xl mx-auto">
            <h1 className="text-3xl font-bold mb-6">Painel de Teste de Pesquisas</h1>

            <button
                onClick={fetchPesquisas}
                disabled={isLoading}
                className="mb-6 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
            >
                {isLoading ? "Recarregando..." : "Recarregar Lista"}
            </button>

            {isLoading && <Spinner />}

            {error && (
                <div className="p-4 bg-red-100 text-red-700 rounded-lg">
                    <p className="font-bold">Erro ao carregar:</p>
                    <p>{error}</p>
                </div>
            )}

            {!isLoading && !error && (
                <div className="space-y-4">
                    {pesquisas.length === 0 && (
                        <p className="text-gray-500">Nenhuma pesquisa encontrada.</p>
                    )}

                    {pesquisas.map((pesquisa) => (
                        <div
                            key={pesquisa.id}
                            className="p-5 bg-white shadow-md rounded-lg flex items-center justify-between"
                        >
                            <div>
                                <h2 className="text-xl font-bold">Pesquisa ID: {pesquisa.id}</h2>
                                <p className="text-sm text-gray-700">
                                    Associada ao <strong>Consumo ID: {pesquisa.consumo.id}</strong>
                                </p>
                                <div className="mt-2 flex gap-4 text-sm">
                                    <span className="bg-gray-200 px-2 py-1 rounded-full">
                                        {pesquisa.perguntas.length} Pergunta(s)
                                    </span>
                                    <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full">
                                        {pesquisa.respostas.length} Resposta(s)
                                    </span>
                                </div>
                            </div>
                            <button
                                onClick={() => handleDelete(pesquisa.id)}
                                className="px-3 py-2 bg-red-500 text-white text-sm font-medium rounded-lg hover:bg-red-600"
                            >
                                Deletar
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}