import {useEffect, useState} from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "./ExplicacaoModal";

import carinha100 from '../../../assets/carinha100.png';
import carinha80 from '../../../assets/carinha80.png';
import carinha60 from '../../../assets/carinha60.png';
import carinha40 from '../../../assets/carinha40.png';
import carinha20 from '../../../assets/carinha20.png';



export default function CsatCard({ percent }:any) {
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
    }, [percentage]);




    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black">

            {/* HEADER */}
            <section className="relative flex justify-center items-center p-4 border-b border-black bg-gray-50">
                <h2 className="text-md font-medium text-black uppercase">CSAT</h2>

                <CiCircleInfo
                    className="absolute right-4 text-blue-700 cursor-pointer"
                    size={28}
                    onClick={handleClick}
                />

                {isClicked && (
                    <section className="absolute top-10 right-4 z-[100]">
                        <ExplicacaoModal
                            mensagem="O CSAT representa a % de clientes satisfeitos dividido pelo total de clientes avaliadores."
                        />
                    </section>
                )}
            </section>

            {/* CONTENT */}
            <section className="flex flex-col justify-center items-center flex-1 p-4">

                <h3 className="text-3xl font-semibold text-black">
                    {percent}%
                </h3>

                <p className="text-lg text-black mt-1">
                    {percent >= 75 ? "satisfeito" : percent >= 50 ? "neutro" : "insatisfeito"}
                </p>
            </section>
        </section>
    );
}