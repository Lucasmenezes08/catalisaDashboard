import Filtro from "@/components/dashboard/filtro/Filtro";
import Grafico from "@/components/dashboard/grafico/Grafico";
import Insight from "@/components/dashboard/insight/Insight";
import { useSelector } from "@/store/useStore";
import { Outlet } from "react-router-dom";


export default function Dashboard (){

    const {selector} = useSelector();


    async function getId (){
        const url = 'http://localhost:8080/api/v1/products';

        try{
            const response = await fetch(url);

            const data = await response.json();

            if (!response.ok){
                throw new Error ("Erro ao capturar dados da api");
            }
            console.log(response.ok)
            return data.id;
        }

        catch (error){
            throw error;
        }
    }

    

    console.log(getId());
    return (
        <section className="max-h-screen px-4 mt-3">
            <Filtro/>
            {selector === 'dashboard' && <Grafico/>}
            {selector === 'insight' && <Insight/>}
            <Outlet/>
        </section>
    )
}