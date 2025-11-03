import { useState } from "react";
import { CiCircleInfo } from "react-icons/ci";
import ExplicacaoModal from "./ExplicacaoModal";

export default function PropocaoDeRespostas(){

    const [isCLicked , setIsCLicked] = useState(false);

    function handleCLick (){
        setIsCLicked(!isCLicked);
    }


    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black">
            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">Proporção de Respostas</h2>
                <CiCircleInfo className="absolute right-4 text-blue-700 cursor-pointer" size={30} onClick={handleCLick}/>
                {isCLicked &&
                    <section className="absolute top-10">
                        <ExplicacaoModal mensagem={"o cálculo do NPS é feito a partir da porcentagem (%) dos promotores menos(-) a porcentagem (%) dos detratores"}/>
                    </section>
                }
            </section>

            <section className="flex-1 p-4 min-h-0">

            </section>
        </section>
    )
}