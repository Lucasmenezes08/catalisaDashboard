import { useState, useEffect } from 'react';
import { AnimatePresence } from 'framer-motion';
import backgroundSebrae from "../assets/backgroundSebrae.png";
import { Spinner } from "@/components/common/animation-spinner.tsx";
import PesquisaBox from "@/components/sebrae/PesquisaBox.tsx";

export default function Pesquisa() {
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        // Reduzi um pouco o tempo para teste, ajuste conforme necessário
        const timer = setTimeout(() => {
            setIsLoading(false);
        }, 3000);

        return () => clearTimeout(timer);
    }, []);

    return (
        <section
            className="relative w-full h-screen bg-cover bg-center overflow-hidden" // overflow-hidden evita scrollbar indesejada
            style={{ backgroundImage: `url(${backgroundSebrae})` }}
        >
            <AnimatePresence mode="wait">
                {isLoading ? (
                    <Spinner key="loading" />
                ) : (
                    // O PesquisaBox agora cuida do overlay escuro sozinho
                    <PesquisaBox key="modal" />
                )}
            </AnimatePresence>
        </section>
    );
}