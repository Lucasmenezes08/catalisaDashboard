import DistribuicaoResposta from "./DistribuicaoResposta";
import PropocaoDeRespostas from "./ProporcaoDeRespostas";
import TaxaDeResposta from "./TaxaDeResposta";

export default function Grafico() {
  return (
    <section className="">
        <section>
           <DistribuicaoResposta/> 
           <PropocaoDeRespostas/>
           <TaxaDeResposta/>
        </section>
        <section>

        </section>
    </section>
  );
}
