import { useState, useEffect, useMemo } from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "./ExplicacaoModal";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Bar } from 'react-chartjs-2';
import {Spinner} from "@/components/common/animation-spinner.tsx";


ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);


interface CsatDistribuicao {
    nota0?: number;
    nota1: number;
    nota2: number;
    nota3: number;
    nota4: number;
    nota5: number;
    [key: string]: number | undefined;
}

export default function DistribuicaoResposta() {
    const [isClicked, setIsClicked] = useState(false);
    const [csatData, setCsatData] = useState<CsatDistribuicao | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    const chartColors = [
        '#620027',
        '#9E0000',
        '#D96806',
        '#D5DF08',
        '#07CF00',
        '#2B1CD3',
    ];

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await fetch(' http://localhost:8080/api/v2/dashboard/csat/distribuicao');
                if (response.ok) {
                    const data = await response.json();
                    setCsatData(data);
                }
            } catch (error) {
                console.error("Erro na requisição:", error);
            } finally {
                setIsLoading(false);
            }
        }
        fetchData();
    }, []);

    const chartData = useMemo(() => {
        if (!csatData) return null;

        return {
            labels: ['0','1', '2', '3', '4', '5'],
            datasets: [
                {
                    label: 'Respostas',

                    data: [
                        csatData.nota0 || 0,
                        csatData.nota1 || 0,
                        csatData.nota2 || 0,
                        csatData.nota3 || 0,
                        csatData.nota4 || 0,
                        csatData.nota5 || 0,
                    ],
                    backgroundColor: chartColors,
                    borderRadius: 4,
                    barPercentage: 0.6,
                }
            ]
        };
    }, [csatData]);

    console.log(csatData?.nota0);

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false,
            },
            title: {
                display: false,
            },
        },
        scales: {
            y: {
                beginAtZero: true,
                grid: { color: '#f0f0f0', display: true },
                ticks: { padding: 20 }
            },
            x: {
                grid: { display: false },
                ticks: { color: '#000', font: { weight: 'bold' as const } }
            }
        },
    };

    function handleClick() {
        setIsClicked(!isClicked);
    }

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black bg-gray-100">
            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">Distribuição de Respostas</h2>
                <CiCircleInfo
                    className="absolute right-4 text-blue-700 cursor-pointer hover:text-blue-900"
                    size={30}
                    onClick={handleClick}
                />
                {isClicked && (
                    <section className="absolute top-10 right-4 z-50 w-64">
                        <ExplicacaoModal mensagem={"Quantidade de avaliações por nota (0 a 5)."} />
                    </section>
                )}
            </section>

            <section className="flex-1 p-4 flex flex-col min-h-0 w-full relative">
                {isLoading ? (
                    <div className="flex-1 flex items-center justify-center">{<Spinner/>}</div>
                ) : chartData ? (
                    <>
                        <div className="flex-1 w-full min-h-0">
                            <Bar options={options} data={chartData} />
                        </div>
                        <div className="h-12 w-full flex justify-between items-center pl-16 mt-2">
                            {chartData.datasets[0].data.map((value, index) => (
                                <div key={index} className="flex flex-col items-center justify-start w-full">
                                    <span
                                        className="text-white text-xs font-bold py-1 px-3 rounded-md"
                                        style={{ backgroundColor: chartColors[index] }}
                                    >
                                        {value}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <div className="flex-1 flex items-center justify-center">Sem dados.</div>
                )}
            </section>
        </section>
    );
}