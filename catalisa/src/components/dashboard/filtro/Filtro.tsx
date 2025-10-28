import DataFiltro from "./DataFiltro";
import FiltroSeletor from "./FiltroSeletor";
import PesquisaFiltro from "./PesquisaFiltro";

export default function Filtro (){
    return (
        <section className="flex flex-row justify-between p-2 h-18 bg-gray-200 rounded-lg">
           <section className="h-full flex flex-row items-center gap-2">
            <DataFiltro/>
            <PesquisaFiltro/>
           </section>

           <FiltroSeletor/>
        </section>
    )
}