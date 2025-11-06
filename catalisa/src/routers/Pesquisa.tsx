// ----- Pesquisa.tsx -----
// (A página /pesquisa que "captura" o consumo pendente)

import { useEffect, useState } from "react";
import PesquisaBox from "@/components/sebrae/PesquisaBox.tsx";
import { Spinner } from "@/components/common/animation-spinner.tsx";
import backgroundSebrae from "../assets/backgroundSebrae.png";
import { useAuth } from "@/store/useAuth.tsx"; // (Dependência: store de autenticação)

interface ConsumoData {
    id: number; // ID do Consumo
    pesquisaRespondida: boolean;
    pesquisa: number | null; // ID da Pesquisa (se houver)
}

export default function Pesquisa() {
    const [isLoading, setIsLoading] = useState(true);
    const [isBoxOpen, setIsBoxOpen] = useState(false);
    const [consumoId, setConsumoId] = useState<number | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const user = useAuth((state) => state.user);

    function handleCloseModal() {
        setIsBoxOpen(false);
        // TODO: Adicionar um navigate('/') para sair da página após fechar
    }

    // Busca o próximo consumo pendente
    useEffect(() => {
        const fetchNextConsumo = async (idDoUsuario: number) => {
            setIsLoading(true);
            try {
                // 1. Busca todos os consumos do usuário
                const response = await fetch(`http://localhost:8080/api/v1/users/${idDoUsuario}/consumos`);
                if (!response.ok) {
                    throw new Error("Falha ao buscar consumos do utilizador");
                }
                const consumos: ConsumoData[] = await response.json();

                // 2. FILTRA: Encontra o primeiro consumo que...
                const proximoConsumo = consumos.find(
                    c => c.pesquisaRespondida === false && c.pesquisa === null
                );

                if (proximoConsumo) {
                    // 3. SUCESSO! Encontrou um consumo pendente (ex: ID 5)
                    console.log("Pesquisa.tsx (Pai): Encontrou consumo pendente:", proximoConsumo);
                    setConsumoId(proximoConsumo.id); // Guarda o ID do CONSUMO
                    setIsBoxOpen(true); // <-- Abre o modal
                } else {
                    // 4. Falha (Nenhum consumo pendente)
                    console.log("Pesquisa.tsx (Pai): Nenhum consumo pendente encontrado.");
                    setErrorMessage("Você não tem nenhuma pesquisa pendente. Volte mais tarde!");
                }
            } catch (error: any) {
                console.error("Erro em Pesquisa.tsx:", error);
                setErrorMessage(error.message || "Erro ao carregar a sua pesquisa.");
            } finally {
                setIsLoading(false);
            }
        };

        if (user && user.id) {
            fetchNextConsumo(user.id);
        } else if (!user) {
            setIsLoading(false);
            setErrorMessage("Você precisa estar logado para ver as suas pesquisas.");
        }
    }, [user]);

    return (
        <section
            className={"relative w-full h-screen bg-cover"}
            style={{ backgroundImage: `url(${backgroundSebrae})` }}
        >
            {isLoading && <Spinner />}

            {/* Renderiza o Box SOMENTE se o consumoId existir */}
            {!isLoading && isBoxOpen && consumoId && (
                <PesquisaBox
                    isActive={isBoxOpen}
                    consumoId={consumoId}    // Passa o ID do Consumo (ex: 5)
                    onClose={handleCloseModal}
                />
            )}

            {/* Mostra mensagens de erro ou "nada pendente" */}
            {!isLoading && errorMessage && (
                <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-white p-8 rounded-lg shadow-xl text-center">
                    <h2 className="text-xl font-bold mb-4">Aviso</h2>
                    <p>{errorMessage}</p>
                </div>
            )}
        </section>
    );
}