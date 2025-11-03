import Filtro from "@/components/dashboard/filtro/Filtro";
import Grafico from "@/components/dashboard/grafico/Grafico";
import Insight from "@/components/dashboard/insight/Insight";
import { useSelector } from "@/store/useStore";
import { Outlet } from "react-router-dom";


export default function Dashboard (){

    const {selector} = useSelector();
    return (
        <section className="max-h-screen px-4 mt-3">
            <Filtro/>
            {selector === 'dashboard' && <Grafico/>}
            {selector === 'insight' && <Insight/>}
            <Outlet/>
        </section>
    )
}