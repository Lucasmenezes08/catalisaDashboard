import { IoIosCloseCircleOutline } from "react-icons/io";
import { IoPersonCircleOutline } from "react-icons/io5";
import {useAuth} from "@/store/useAuth.tsx";
import { Link } from 'react-router-dom';


export default function ModalPerfil({nome , onclick} : any){
    const {logout} = useAuth();
    return (
        <section className={"absolute flex flex-col items-center top-5 right-0 w-48 z-10 max-h-md gap-3 p-4 bg-white rounded-2xl border border-black"}>
            <IoIosCloseCircleOutline onClick={onclick} className={"absolute top-2 right-2 cursor-pointer"} size={25}/>
                <IoPersonCircleOutline size={40}/>
                <p className={"text-md"}>{nome}</p>
                {/* <Link className={"text-lg font-semibold"} to={"configuracoes"} onClick={onclick}> Meu perfil </Link> */}
                <button className={"text-blue-700 cursor-pointer"} onClick={logout}>Log out</button>
        </section>
    )
}