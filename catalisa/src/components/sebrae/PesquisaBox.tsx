import personagem from "../../assets/personagem.png";

export default function PesquisaBox(){
    return (
        <section className={"absolute flex flex-col items-center top-[13%] left-[50%] translate-x-[-50%]  min-w-sm h-[100vh] bg-[#2E23AA] rounded-t-3xl py-5 px-5 transition easy-in-out delay-500 "}>
            <section className={"flex flex-row items-start justify-center gap-1"}>
                <img className={"w-25 h-25"} src={personagem}/>
                <section className={"flex flex-col pt-3 text-white"}>
                    <p className={"font-bold text-lg"}>Eai Rafael!</p>
                    <p className={"text-sm"}>Tudo bem?</p>
                </section>
            </section>
        </section>
    )
}