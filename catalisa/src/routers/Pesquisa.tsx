import { useState, useEffect } from 'react';
import { AnimatePresence } from 'framer-motion';
import backgroundSebrae from "../assets/backgroundSebrae.png";
import { Spinner } from "@/components/common/animation-spinner.tsx";
import PesquisaBox from "@/components/sebrae/PesquisaBox.tsx";
import { useAuth } from "@/store/useAuth.tsx";
import type { ConsumoResponseDTO } from "@/types/types.ts";

export default function Pesquisa() {
    const [isLoading, setIsLoading] = useState(true);
    const { user } = useAuth();
    const [consumoPendente, setConsumoPendente] = useState<ConsumoResponseDTO | null>(null);

    useEffect(() => {
        if (!user) {
            setIsLoading(false);
            return;
        }

        const checkPendingSurveys = async () => {
            try {
                const res = await fetch(`http://localhost:8080/api/v2/users/${user.id}/consumos`);
                if (!res.ok) throw new Error("Falha ao buscar consumos");

                const consumos: ConsumoResponseDTO[] = await res.json();
                const pendente = consumos.find(c => !c.pesquisaRespondida);

                if (pendente) {
                    setConsumoPendente(pendente);
                }

            } catch (error) {
                console.error("Erro ao verificar pesquisas pendentes:", error);
            } finally {
                setIsLoading(false);
            }
        };

        checkPendingSurveys();

    }, [user]);

    return (
        <section
            className="relative w-full h-screen bg-cover bg-center overflow-hidden"
            style={{ backgroundImage: `url(${backgroundSebrae})` }}
        >
            <AnimatePresence mode="wait">
                {isLoading && (
                    <Spinner key="loading" />
                )}

                {!isLoading && consumoPendente && (
                    <PesquisaBox
                        key="modal"
                        consumo={consumoPendente}
                    />
                )}

            </AnimatePresence>
        </section>
    );
}