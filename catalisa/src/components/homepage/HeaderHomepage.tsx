import setaDiagonal from "../../assets/seta_diagonal.svg";
import setCima from "../../assets/seta_cima.svg";


export default function HeaderHomepage (){
    return (
        <header className={"relative flex-1 w-full flex justify-center items-center"}>
            <section className={"absolute top-30 left-50 flex flex-col gap-3"}>
                <p className={"text-5xl font-extralight text-black"}><span className={"font-bold"}>Dê luz</span> aos seus dados <br/></p>
                <p className={"text-5xl font-extralight text-black"}><span className={"font-bold"}>Catalise</span> a melhor estratégia <br/></p>
                <section className={"flex flex-row items-end gap-2"}>
                    <p className={"text-5xl font-extralight text-black"}><span className={"font-bold"}>e Aplique</span> no seu negócio</p>
                    <section className={"bg-[#0000FF] w-4 h-4 rounded-full"}></section>
                </section>

                <section className={"flex flex-row gap-7 mt-5"}>
                    <section className={"flex flex-row w-40 justify-center items-center border-1 border-black rounded-lg gap-2"}>
                        <p>Cadastre-se</p>
                        <img className={"w-12"} src={setaDiagonal}/>
                    </section>

                    <section className={"flex flex-row w-40 justify-center items-center border-1 border-black rounded-lg gap-2"}>
                        <p>Quem somos</p>
                        <img className={"w-4"} src={setCima} />
                    </section>
                </section>
            </section>

        </header>
    )
}