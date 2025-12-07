import { useState, useEffect, useMemo } from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "./ExplicacaoModal";
import {
    Chart as ChartJS,
    ArcElement,
    Tooltip,
    Legend,
} from 'chart.js';
import { Doughnut } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

export default function TaxaDeResposta() {
    const [isClicked, setIsClicked] = useState(false);
    const [percentage, setPercentage] = useState(0);
    const [isLoading, setIsLoading] = useState(true);

    function handleClick() {
        setIsClicked(!isClicked);
    }

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await fetch('http://localhost:8080/api/v2/dashboard/csat/respostas');
                if (response.ok) {
                    const data = await response.json();
                    setPercentage(data.porcentagemRespostas || 0);
                }
            } catch (error) {
                console.error("Erro ao buscar taxa de resposta:", error);
            } finally {
                setIsLoading(false);
            }
        }
        fetchData();
    }, []);

    const chartData = useMemo(() => {
        const remaining = 100 - percentage;
        return {
            labels: ['Responderam', 'Não Responderam'],
            datasets: [
                {
                    data: [percentage, remaining],
                    backgroundColor: [
                        '#304FFE',
                        '#E0E0E0',
                    ],
                    borderWidth: 0,
                    hoverOffset: 4,
                },
            ],
        };
    }, [percentage]);

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '75%',
        plugins: {
            legend: { display: false },
            tooltip: { enabled: true },
        },
    };

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black bg-gray-100">

            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">Taxa de Resposta</h2>
                <CiCircleInfo
                    className="absolute right-4 text-blue-700 cursor-pointer hover:text-blue-900"
                    size={30}
                    onClick={handleClick}
                />
                {isClicked && (
                    <section className="absolute top-10 right-4 z-50 w-64">
                        <ExplicacaoModal mensagem={"Porcentagem de clientes que responderam à pesquisa."} />
                    </section>
                )}
            </section>
            <section className="flex-1 p-4 flex flex-col min-h-0 w-full relative justify-center items-center">
                {isLoading ? (
                    <div className="flex-1 flex items-center justify-center">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-700"></div>
                    </div>
                ) : (
                    <>

                        <div className="relative w-48 h-48">
                            <Doughnut data={chartData} options={options} />

                            <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
                                <span className="text-3xl font-bold text-black">
                                    {percentage}%
                                </span>
                            </div>
                        </div>
                        <div className="mt-6 text-center">
                            <p className="text-sm text-gray-700 px-4">
                                <strong className="text-black">{percentage}%</strong> dos consumidores de <strong>todos os serviços</strong> responderam à pesquisa.
                            </p>
                        </div>
                    </>
                )}
            </section>
        </section>
    );
}