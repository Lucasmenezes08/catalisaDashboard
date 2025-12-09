import { GoArrowDown } from "react-icons/go";

export default function ContentHomepage(){
    return (
        <section id={"content"} className={"bg-black min-h-screen w-full flex flex-col justify-center items-center gap-13 pt-10"}>
            <p className={"text-white text-5xl font-semibold "}>Fazemos você<br/> crescer, juntos.</p>
            <p className={"text-gray-300 text-md text-center max-w-[40%] break-after-all"}>
                <span className={"text-white font-semibold"}> Nosso propósito é facilitar</span> a sua vida, <span className={"text-white font-semibold"}>melhorar a saúde</span> do seu negócio e deixar
                seus <span className={"text-white font-semibold"}>clientes satisfeitos</span> através de soluções que unem comunicação, tecnologia e design.
            </p>

            <p className={"text-gray-300 text-md text-center max-w-[38%] break-after-all"}>
                <span className={"text-white font-semibold"}>Assumimos que a sua dor</span>  de formular pesquisas e coletar dados para que você possa focar
                no que realmente importa: sua empresa.
            </p>


            <a  className={"flex flex-row px-5 py-4 justify-center items-center border border-white rounded-lg gap-3 cursor-pointer"} href={"#main"} >
                <p className={"text-gray-300"}>Vamos trabalhar juntos</p>
                <GoArrowDown size={32} className={"font-bold text-white"}/>
            </a>
        </section>
    )
}