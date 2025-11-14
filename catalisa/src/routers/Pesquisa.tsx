// src/pages/Pesquisa.tsx (Exemplo de caminho)
import { useState, useEffect } from 'react';
import { AnimatePresence } from 'framer-motion';
import backgroundSebrae from "../assets/backgroundSebrae.png";
import { Spinner } from "@/components/common/animation-spinner.tsx";
import PesquisaBox from "@/components/sebrae/PesquisaBox.tsx";
import { useAuth } from "@/store/useAuth.tsx";
import type { ConsumoResponseDTO } from "@/types/types.ts";

export default function Pesquisa() {
    const [isLoading, setIsLoading] = useState(true);
    const { user } = useAuth(); // Pega o usuário logado

    // Guarda o consumo que precisa ser respondido
    const [consumoPendente, setConsumoPendente] = useState<ConsumoResponseDTO | null>(null);

    useEffect(() => {
        // Se não tem usuário, não há o que checar
        if (!user) {
            setIsLoading(false);
            return;
        }

        const checkPendingSurveys = async () => {
            try {
                // 1. Busca todos os consumos do usuário logado
                const res = await fetch(`/api/v2/users/${user.id}/consumos`);
                if (!res.ok) throw new Error("Falha ao buscar consumos");

                const consumos: ConsumoResponseDTO[] = await res.json();

                // 2. Encontra o *primeiro* consumo que ainda não foi respondido
                const pendente = consumos.find(c => !c.pesquisaRespondida);

                if (pendente) {
                    // 3. Se achou, guarda no estado
                    setConsumoPendente(pendente);
                }

            } catch (error) {
                console.error("Erro ao verificar pesquisas pendentes:", error);
            } finally {
                // 4. Para o loading, tendo achado ou não
                setIsLoading(false);
            }
        };

        checkPendingSurveys();

    }, [user]); // Roda sempre que o 'user' for carregado

    return (
        <section
            className="relative w-full h-screen bg-cover bg-center overflow-hidden"
            style={{ backgroundImage: `url(${backgroundSebrae})` }}
        >
            <AnimatePresence mode="wait">
                {/* Mostra o spinner enquanto verifica */}
                {isLoading && (
                    <Spinner key="loading" />
                )}

                {/* * Se NÃO está carregando E existe um consumo pendente,
                  * renderiza o PesquisaBox e passa o consumo para ele.
                */}
                {!isLoading && consumoPendente && (
                    <PesquisaBox
                        key="modal"
                        consumo={consumoPendente} // Passa a prop!
                    />
                )}

                {/* * Opcional: Se NÃO está carregando E NÃO há pesquisas,
                  * pode mostrar uma mensagem ou deixar em branco.
                */}
                {!isLoading && !consumoPendente && (
                    <div key="nada" className="flex items-center justify-center h-full">
                        <h1 className="text-white text-2xl bg-black/30 p-4 rounded-lg">
                            Você não possui pesquisas pendentes.
                        </h1>
                    </div>
                )}
            </AnimatePresence>
        </section>
    );
}