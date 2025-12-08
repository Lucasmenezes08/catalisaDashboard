import {useEffect, useState} from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "./ExplicacaoModal";

import carinha100 from '../../../assets/carinha100.png';
import carinha80 from '../../../assets/carinha80.png';
import carinha60 from '../../../assets/carinha60.png';
import carinha40 from '../../../assets/carinha40.png';
import carinha20 from '../../../assets/carinha20.png';

export default function CsatCard() {
    const [isClicked, setIsClicked] = useState(false);
    const [percentualSatisfacao, setPercentualSatisfacao] = useState(0);
    const [isLoading, setIsLoading] = useState(true);

    function handleClick() {
        setIsClicked(!isClicked);
    }

    useEffect(() => {
        async function fetchData() {
            try {
                // ✅ Endpoint correto da documentação
                const response = await fetch('http://localhost:8080/api/v2/dashboard/csat/distribuicao');

                if (response.ok) {
                    const data = await response.json();

                    console.log("Distribuição CSAT:", data); // Debug

                    // Extrai as notas
                    const nota1 = data.nota1 || 0;
                    const nota2 = data.nota2 || 0;
                    const nota3 = data.nota3 || 0;
                    const nota4 = data.nota4 || 0;
                    const nota5 = data.nota5 || 0;

                    // Total de avaliações
                    const total = nota1 + nota2 + nota3 + nota4 + nota5;

                    if (total > 0) {
                        // ✅ CSAT = apenas notas 4 e 5 são consideradas "satisfeitos"
                        const satisfeitos = nota4 + nota5;
                        const porcentagem = Math.round((satisfeitos / total) * 100);

                        console.log(`Total: ${total}, Satisfeitos (4+5): ${satisfeitos}, CSAT: ${porcentagem}%`);

                        setPercentualSatisfacao(porcentagem);
                    } else {
                        setPercentualSatisfacao(0);
                    }
                }
            } catch (error) {
                console.error("Erro ao buscar distribuição CSAT:", error);
            } finally {
                setIsLoading(false);
            }
        }
        fetchData();
    }, []);

    function getCarinha(valor: number) {
        if (valor >= 80) return carinha100; // muito satisfeito 80-100%
        if (valor >= 60) return carinha80;  // satisfeito 60-80%
        if (valor >= 40) return carinha60;  // neutro 40-60%
        if (valor >= 20) return carinha40;  // insatisfeito 20-40%
        return carinha20;                   // muito insatisfeito 0-20%
    }

    function getTextoSatisfacao(valor: number) {
        if (valor >= 80) return "muito satisfeito";
        if (valor >= 60) return "satisfeito";
        if (valor >= 40) return "neutro";
        if (valor >= 20) return "insatisfeito";
        return "muito insatisfeito";
    }

    function getCorPorcentagem(valor: number) {
        if (valor >= 80) return "text-green-600";
        if (valor >= 60) return "text-green-500";
        if (valor >= 40) return "text-yellow-500";
        if (valor >= 20) return "text-red-400";
        return "text-red-600";
    }

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black bg-gray-100">

            {/* HEADER */}
            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">CSAT</h2>

                <CiCircleInfo
                    className="absolute right-4 text-blue-700 cursor-pointer"
                    size={28}
                    onClick={handleClick}
                />

                {isClicked && (
                    <section className="absolute top-10 right-4 z-[100]">
                        <ExplicacaoModal
                            mensagem="O CSAT representa a % de clientes satisfeitos (notas 4 e 5) dividido pelo total de clientes avaliadores."
                        />
                    </section>
                )}
            </section>

            {/* CONTENT */}
            <section className="flex flex-col justify-center items-center flex-1 p-4 gap-3">

                {isLoading ? (
                    <div className="text-gray-400">Carregando...</div>
                ) : (
                    <>
                        {/* Carinha */}
                        <img
                            src={getCarinha(percentualSatisfacao)}
                            alt="Indicador de satisfação"
                            className="w-24 h-24 object-contain"
                        />

                        {/* Porcentagem com cor dinâmica */}
                        <h3 className={`text-4xl font-bold ${getCorPorcentagem(percentualSatisfacao)}`}>
                            {percentualSatisfacao}%
                        </h3>

                        {/* Texto de satisfação */}
                        <p className="text-base text-gray-700 font-medium">
                            {getTextoSatisfacao(percentualSatisfacao)}
                        </p>
                    </>
                )}
            </section>
        </section>
    );
}
