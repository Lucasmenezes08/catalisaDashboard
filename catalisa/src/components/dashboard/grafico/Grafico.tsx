import Comentario from "./Comentarios";
import DistribuicaoResposta from "./DistribuicaoResposta";
import Nps from "../grafico/Nps";
import PropocaoDeRespostas from "./ProporcaoDeRespostas";
import TaxaDeResposta from "./TaxaDeResposta";

export default function Grafico() {
  return (
    <section className="w-full h-full bg-white border border-solid border-black rounded-xl p-2 mt-2">
      <section className="h-full grid grid-cols-1 md:grid-cols-3 grid-rows-2 gap-y-3">
        
        <DistribuicaoResposta />
        <PropocaoDeRespostas />
        <TaxaDeResposta />

        <section className="md:col-span-2">
          <Comentario />
        </section>

        <section>
          <Nps />
        </section>

      </section>
    </section>
  );
}