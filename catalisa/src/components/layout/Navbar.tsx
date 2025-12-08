import {Link} from "react-router-dom";
import icon from "../../assets/icon.svg"
import catalisa from "../../assets/catalisa.svg"
import person from "../../assets/person.svg"
import {useAuth} from "@/store/useAuth.tsx";

export default function NavBar (){
    const {user} = useAuth();

    return (
        <section className="fixed bg-[#2020AF] w-full py-2.5 px-8 mb-5 z-50">
            <section className={"w-full flex justify-between items-center px-10"}>
                <Link to={"/"} className={"cursor-pointer"}>
                    <img src={icon} />
                </Link>

                <Link to={"/"} className={"cursor-pointer"}>
                    <img src={catalisa}/>
                </Link>

                {
                    user ?
                    <Link to={"/dashboard"} className={"cursor-pointer"}>
                        <img src={person}/>
                    </Link>
                    :
                        <Link to={"/login"} className={"text-white border-1 border-white py-1 px-2 rounded-lg"}>
                            Entrar
                        </Link>
                }


            </section>
        </section>
    )
}