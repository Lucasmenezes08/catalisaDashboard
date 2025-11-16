import { useState } from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "./ExplicacaoModal";

export default function DistribuicaoResposta(){

    const [isCLicked , setIsCLicked] = useState(false);

    function handleCLick (){
        setIsCLicked(!isCLicked);
    }

    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black">
            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">Distribuição de Respostas</h2>
                <CiCircleInfo className="absolute right-4 text-blue-700 cursor-pointer" size={30} onClick={handleCLick}/>
                     {isCLicked &&
                        <section className="absolute top-10 z-100">
                            <ExplicacaoModal mensagem={"representação visual da <strong> quantidade</strong> e <strong>classificação</strong> das avaliações do período de tempo filtrado"}/>
                        </section>
                    }
            </section>

           

            <section className="flex-1 p-4 min-h-0">

            </section>
        </section>
    )

}


