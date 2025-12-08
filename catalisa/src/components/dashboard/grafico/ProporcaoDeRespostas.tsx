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