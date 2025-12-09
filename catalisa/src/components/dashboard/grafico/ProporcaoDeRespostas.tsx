/*
import { useState, useEffect } from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "../grafico/ExplicacaoModal";
import { Spinner } from "@/components/common/animation-spinner.tsx";
import nivelSatisfacao from '../../../assets/nivelsatisfacao.png';
import setaSatisfacao from '../../../assets/setaSatisfacao.png';

interface MediaData {
    media: number;
}


export default function NivelSatisfacao() {
    const [isClicked, setIsClicked] = useState(false);
    const [mediaData, setMediaData] = useState<MediaData | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        async function fetchMedia() {
            try {
                const response = await fetch('http://localhost:8080/api/v2/dashboard/csat/media');
                if (response.ok) {
                    const data = await response.json();
                    setMediaData(data);
                }
            } catch (error) {
                console.error("Erro ao buscar média:", error);
            } finally {
                setIsLoading(false);
            }
        }
        fetchMedia();
    }, []);

    function handleClick() {
        setIsClicked(!isClicked);
    }

    const formatarMedia = (valor?: number) => {
        if (valor === undefined || valor === null) return "0,00";
        return valor.toFixed(2).replace('.', ',');
    };

    const calcularRotacao = (nota: number) => {
        const maxScore = 6.3;
        const porcentagem = nota / maxScore;
        return (180 * porcentagem) - 90;
    };

    const anguloRotacao = mediaData ? calcularRotacao(mediaData.media) : -90;

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black bg-gray-100">

            <section className="relative flex justify-center items-center p-4 border-b border-black rounded-t-xl">
                <h2 className="text-md font-medium text-black uppercase">Nível de Satisfação</h2>
                <CiCircleInfo
                    className="absolute right-4 text-blue-700 cursor-pointer hover:text-blue-900"
                    size={30}
                    onClick={handleClick}
                />
                {isClicked && (
                    <section className="absolute top-10 right-4 z-50 w-64">
                        <ExplicacaoModal mensagem={"Média ponderada das notas de satisfação (escala 0 a 5). A seta indica a pontuação atual."} />
                    </section>
                )}
            </section>

            <section className="flex-1 p-2 flex flex-col items-center justify-center relative w-full">
                {isLoading ? (
                    <Spinner />
                ) : (
                    <>
                        <div className="relative w-full h-full mt-4 mb-[-4rem]">
                            <img
                                src={nivelSatisfacao}
                                alt="Gráfico de nível de satisfação"
                                className="w-full h-full object-contain absolute top-0 left-0 z-0"
                            />

                            <div
                                className="absolute bottom-24 left-[51%] w-15 h-30 origin-bottom z-10 transition-transform duration-1000 ease-out"
                                style={{
                                    transform: `translateX(-50%) rotate(${anguloRotacao}deg)`
                                }}
                            >
                                <div className={'relative w-full h-full'}>
                                    <img
                                        src={setaSatisfacao}
                                        alt="Seta"
                                        className="w-full h-full object-contain"
                                    />
                                    <span className="absolute top-[50%] left-[15%] text-lg font-bold text-gray-700">
                                        {formatarMedia(mediaData?.media)}
                                    </span>
                                </div>

                            </div>

                        </div>

                        <p className="text-center text-sm text-gray-700">
                            A média de satisfação dos usuários é de <span className="font-bold">{formatarMedia(mediaData?.media)}</span>
                        </p>
                    </>
                )}
            </section>
        </section>
    );
}
 */


import { useState, useEffect } from "react";
import { Info } from "lucide-react";

interface MediaData {
    media: number;
}

interface ExplicacaoModalProps {
    mensagem: string;
}

function ExplicacaoModal({ mensagem }: ExplicacaoModalProps) {
    return (
        <div className="bg-white border border-gray-300 rounded-lg shadow-lg p-4">
            <p className="text-sm text-gray-700">{mensagem}</p>
        </div>
    );
}

function Spinner() {
    return (
        <div className="flex justify-center items-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-700"></div>
        </div>
    );
}

export default function NivelSatisfacao() {
    const [isClicked, setIsClicked] = useState(false);
    const [mediaData, setMediaData] = useState<MediaData | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        async function fetchMedia() {
            try {
                const response = await fetch('http://localhost:8080/api/v2/dashboard/csat/media');

                if (response.ok) {
                    const data: MediaData = await response.json();
                    console.log('Média CSAT recebida:', data);
                    setMediaData(data);
                } else {
                    console.error('Erro ao buscar média CSAT:', response.status);
                }
            } catch (error) {
                console.error("Erro ao buscar média:", error);
            } finally {
                setIsLoading(false);
            }
        }

        fetchMedia();
    }, []);

    function handleClick() {
        setIsClicked(!isClicked);
    }

    const formatarMedia = (valor?: number) => {
        if (valor === undefined || valor === null) return "0,0";
        return valor.toFixed(1).replace('.', ',');
    };

    // Determina a cor baseado na nota (escala 0-5)
    const getColor = (nota: number) => {
        if (nota >= 0 && nota <= 1.5) return "#DC2626"; // Vermelho - entre 0 e 1,5
        if (nota > 1.5 && nota <= 2.5) return "#EA580C"; // Laranja - entre 1,6 e 2,5
        if (nota > 2.5 && nota <= 3.5) return "#FACC15"; // Amarelo - entre 2,6 e 3,5
        if (nota > 3.5 && nota <= 4.5) return "#22C55E"; // Verde - entre 3,6 e 4,5
        if (nota > 4.5 && nota <= 5) return "#3B82F6"; // Azul - entre 4,6 e 5
        return "#9CA3AF"; // Cinza para valores inválidos
    };

    const nota = mediaData?.media || 0;
    const cor = getColor(nota);

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black bg-gray-100">
            <section className="relative flex justify-center items-center p-4 border-b border-black rounded-t-xl">
                <h2 className="text-md font-medium text-black uppercase">Nível de Satisfação</h2>
                <Info
                    className="absolute right-4 text-blue-700 cursor-pointer hover:text-blue-900"
                    size={30}
                    onClick={handleClick}
                />
                {isClicked && (
                    <section className="absolute top-14 right-4 z-50 w-64">
                        <ExplicacaoModal mensagem="Média das notas de satisfação (escala 0 a 5). Cores: vermelho (0-1,5), laranja (1,6-2,5), amarelo (2,6-3,5), verde (3,6-4,5), azul (4,6-5)." />
                    </section>
                )}
            </section>

            <section className="flex-1 p-6 flex flex-col items-center justify-center relative w-full">
                {isLoading ? (
                    <Spinner />
                ) : (
                    <>
                        <div className="relative w-full max-w-sm aspect-square flex items-center justify-center mb-6">
                            <svg viewBox="0 0 200 120" className="w-full h-auto">
                                {/* Arco de fundo (cinza) */}
                                <path
                                    d="M 30 100 A 70 70 0 0 1 170 100"
                                    fill="none"
                                    stroke="#E5E7EB"
                                    strokeWidth="20"
                                    strokeLinecap="round"
                                />

                                {/* Arco colorido baseado na nota do banco */}
                                <path
                                    d="M 30 100 A 70 70 0 0 1 170 100"
                                    fill="none"
                                    stroke={cor}
                                    strokeWidth="20"
                                    strokeLinecap="round"
                                    strokeDasharray="220"
                                    strokeDashoffset={220 - (220 * (nota / 5))}
                                    className="transition-all duration-1000 ease-out"
                                />

                                {/* Valor no centro */}
                                <text
                                    x="100"
                                    y="95"
                                    textAnchor="middle"
                                    fontSize="32"
                                    fontWeight="bold"
                                    fill="#1F2937"
                                >
                                    {formatarMedia(nota)}
                                </text>
                            </svg>
                        </div>

                        <p className="text-center text-sm text-gray-700 mt-2">
                            A média de satisfação dos usuários é de <span className="font-bold">{formatarMedia(nota)}</span>
                        </p>
                    </>
                )}
            </section>
        </section>
    );
}