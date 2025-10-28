import { Outlet } from "react-router-dom";
import Configuracoes from "./configuracoes";

export default function SidebarConfiguracoes (){
    return (
        <section className="flex flex-row h-screen w-55 bg-gray-200">
            <Configuracoes/>
            <Outlet/>
        </section>
    )
}