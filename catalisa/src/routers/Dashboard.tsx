import Filtro from "@/components/dashboard/filtro/Filtro";
import Grafico from "@/components/dashboard/grafico/Grafico";
import { Outlet } from "react-router-dom";

export default function Dashboard (){
    return (
        <section className="px-4 mt-3">
            <Filtro/>
            <Grafico/>
            <Outlet/>
        </section>
    )
}