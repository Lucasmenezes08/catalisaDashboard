import { useState, useEffect } from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "../grafico/ExplicacaoModal";
import { Spinner } from "@/components/common/animation-spinner.tsx";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

interface RespostasData {
    percComComentario?: number;
    percSemComentario?: number;
}

interface SentimentoData {
    percNegativos: number;
    percPositivos: number;
    negativos: number;
    positivos: number;
    totalComentarios: number;
}

export default function Comentario() {
    const [isClicked, setIsClicked] = useState(false);
    const [respostasData, setRespostasData] = useState<RespostasData | null>(null);
    const [sentimentoData, setSentimentoData] = useState<SentimentoData | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        async function fetchData() {
            try {
                const [respostasResponse, sentimentoResponse] = await Promise.all([
                    fetch('http://localhost:8080/api/v2/dashboard/csat/respostas'),
                    fetch('http://localhost:8080/api/v2/dashboard/csat/sentimento')
                ]);

                if (respostasResponse.ok) {
                    const data = await respostasResponse.json();
                    setRespostasData(data);
                    console.log(data);
                }

                if (sentimentoResponse.ok) {
                    const data = await sentimentoResponse.json();
                    setSentimentoData(data);
                    console.log(data)
                }
            } catch (error) {
                console.error("Erro na requisição:", error);
            } finally {
                setIsLoading(false);
            }
        }
        fetchData();
    }, []);

    function handleClick() {
        setIsClicked(!isClicked);
    }

    const chartOptionsComComentario = {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '70%',
        plugins: {
            legend: { display: false },
            tooltip: { enabled: false }
        }
    };

    const chartOptionsNegativos = {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '75%',
        plugins: {
            legend: { display: false },
            tooltip: { enabled: false }
        }
    };

    const dataComComentario = {
        datasets: [{
            data: [
                Math.round(respostasData?.percComComentario ?? 0),
                Math.round(respostasData?.percSemComentario ?? 100),
            ],
            backgroundColor: ['#22c55e', '#e5e7eb'],
            borderWidth: 0,
        }]
    };

    const dataNegativos = {
        datasets: [{
            data: [
                sentimentoData?.percNegativos || 0,
                sentimentoData?.percPositivos || 0
            ],
            backgroundColor: ['#dc2626', '#e5e7eb'],
            borderWidth: 0,
        }]
    };

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black bg-gray-100">
            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">Comentários</h2>
                <CiCircleInfo
                    className="absolute right-4 text-blue-700 cursor-pointer hover:text-blue-900"
                    size={30}
                    onClick={handleClick}
                />
                {isClicked && (
                    <section className="absolute top-10 right-4 z-50 w-64">
                        <ExplicacaoModal mensagem={"Dados quantitativos sobre a realização dos comentários facultativos da pesquisa"} />
                    </section>
                )}
            </section>

            <section className="flex-1 p-4 min-h-0">
                {isLoading ? (
                    <div className="flex h-full items-center justify-center">
                        <Spinner />
                    </div>
                ) : (respostasData || sentimentoData) ? (
                    <div className="h-full flex items-center justify-around gap-8">
                        {/* Gráfico: Dos que responderam */}
                        <div className="flex flex-col items-center justify-center flex-1">
                            <div className="relative w-48 h-48">
                                <Doughnut data={dataComComentario} options={chartOptionsComComentario} />
                                <div className="absolute inset-0 flex items-center justify-center">
                                    <span className="text-3xl font-bold text-black">
                                        {(respostasData?.percComComentario || 0).toFixed(1)}%
                                    </span>
                                </div>
                            </div>
                            <p className="text-center text-sm text-gray-700 mt-4 max-w-xs">
                                Dos que responderam a pesquisa,{" "}
                                <span className="font-bold">{(respostasData?.percComComentario || 0).toFixed(1)}%</span>{" "}
                                deixaram algum comentário
                            </p>
                        </div>

                        {/* Gráfico: Negativos vs Positivos */}
                        <div className="flex flex-col items-center justify-center flex-1">
                            <div className="relative w-52 h-52">
                                <Doughnut data={dataNegativos} options={chartOptionsNegativos} />
                                <div className="absolute inset-0 flex items-center justify-center">
                                    <span className="text-3xl font-bold text-black">
                                        {(sentimentoData?.percNegativos || 0).toFixed(1)}%
                                    </span>
                                </div>
                            </div>
                            <div className="text-center text-sm text-gray-700 mt-4">
                                <p>
                                    <span className="font-bold">{(sentimentoData?.percNegativos || 0).toFixed(1)}%</span>{" "}
                                    dos comentários são <span className="font-bold">negativos</span>
                                </p>
                                <p className="mt-1">
                                    <span className="font-bold">{(sentimentoData?.percPositivos || 0).toFixed(1)}%</span>{" "}
                                    dos comentários são <span className="font-bold">positivos</span>
                                </p>
                            </div>
                        </div>
                    </div>
                ) : (
                    <div className="flex h-full items-center justify-center">Sem dados.</div>
                )}
            </section>
        </section>
    );
}