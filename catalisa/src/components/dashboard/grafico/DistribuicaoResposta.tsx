import { useState } from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "./explicacaoModal";

export default function DistribuicaoResposta(){

    const [isHovered , setIsHovered] = useState(false);

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black">
            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">Distribuição de Respostas</h2>
                <CiCircleInfo className="absolute right-4 text-blue-700" size={30} onMouseEnter={() => setIsHovered(true)} onMouseLeave={() => setIsHovered(false)}/>
                     {isHovered && 
                        <section className="absolute pr-15 pt-15">
                            <ExplicacaoModal mensagem={"representação visual da quantidade e classificação das avaliações do período de tempo filtrado"}/>
                        </section>
                    }
            </section>

           

            <section className="flex-1 p-4 min-h-0">

            </section>
        </section>
    )

}


