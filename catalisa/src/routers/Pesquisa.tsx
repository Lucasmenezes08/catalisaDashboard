import { useState, useEffect } from 'react';
import { AnimatePresence } from 'framer-motion';
import backgroundSebrae from "../assets/backgroundSebrae.png";
import { Spinner } from "@/components/common/animation-spinner.tsx";
import PesquisaBox from "@/components/sebrae/PesquisaBox.tsx";

export default function Pesquisa() {
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsLoading(false);
        }, 3000);

        return () => clearTimeout(timer);
    }, []);

    return (
        <section
            className="relative w-full h-screen bg-cover bg-center overflow-hidden"
            style={{ backgroundImage: `url(${backgroundSebrae})` }}
        >
            <AnimatePresence mode="wait">
                {isLoading ? (
                    <Spinner key="loading" />
                ) : (

                    <PesquisaBox key="modal" />
                )}
            </AnimatePresence>
        </section>
    );
}