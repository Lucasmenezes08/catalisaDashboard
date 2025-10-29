import Sidebar from "@/components/configuracoes/sidebar";
import { Outlet } from "react-router-dom";

export default function LayoutConfig (){
    return (
        <section className="flex flex-row">
            <Sidebar/>
            <Outlet/>
        </section>
    )
}