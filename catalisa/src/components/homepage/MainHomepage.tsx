import catalisaHomepage from "../../assets/catalisaHomepage.svg"
import iconHomepage from "../../assets/iconHomepage.svg"
import {Link} from "react-router-dom";
import setaDiagonal from "@/assets/seta_diagonal.svg";

export default function MainHomepage(){
    return (
        <section className={"min-h-screen w-full flex items-center justify-center bg-white"}>
            <section className={"relative flex items-center justify-center flex-row w-full px-8 gap-15"}>
                <img src={iconHomepage}/>
                <section className={"flex flex-col items-end gap-1"}>
                    <img src={catalisaHomepage}/>
                    <p className={"font-bold"}>Consultoria e soluções em tecnologia e análise de dados</p>
                </section>

                <section className={"flex flex-row justify-center items-center border border-black rounded-lg gap-1 cursor-pointer"}>
                    <Link to={"/teste"} className={"pl-5 font-medium"}>Cadastre-se</Link>
                    <img className={"w-12"} src={setaDiagonal}/>

                </section>


            </section>
        </section>
    )
}