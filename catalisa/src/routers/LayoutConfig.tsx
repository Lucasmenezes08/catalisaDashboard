import Sidebar from "@/components/configuracoes/Sidebar";
import { Outlet } from "react-router-dom";

export default function LayoutConfig (){
    return (
        <section className="flex flex-row">
            <Sidebar/>
            <Outlet/>
        </section>
    )
}