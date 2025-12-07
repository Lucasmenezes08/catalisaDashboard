import pessoaFalando from "../../assets/pessoaFalando.svg"
import nossosPilares from "../../assets/Group 59.svg"
import lapis from "../../assets/lapis.svg"
import comeia from "../../assets/comeia.svg"

export default function PilaresHomepage() {
    return (
        <section className="min-h-screen w-full flex flex-col items-center justify-center gap-y-16 bg-[#EAEAEC]/56" id="pilares">
            <img src={nossosPilares} alt="nossos pilares" />
            <section className="flex flex-row justify-center gap-x-6">
                <section className="flex flex-col border-2 border-black px-6 py-8 items-start gap-y-6">
                    <img src={pessoaFalando} alt="pessoaFalando" />
                    <p className="font-bold text-3xl">Comunicação <br />Estrategica</p>
                    <p className="text-base">Transformamos pesquisas e <br/>
                    relatórios técnicos complexos em<br/>
                    narrativas e mensagens claras.<br/>
                    Garantimos que os insights de<br/>
                    pesquisa cheguem à liderança no <br />
                    formato e no momento certo para <br />
                    a tomada de decisão.</p>
                </section>
                <section className="flex flex-col border-2 border-black px-6 py-8 items-start gap-y-6">
                    <img src={lapis} alt="lapis" />
                    <p className="font-bold text-3xl">Design de <br />Experiências</p>
                    <p className="text-base">Com o propósito comunicativo <br />
                    em mente, projetamos a <br />
                    experiência e usabilidade de <br />
                    pesquisas e dashboards. Criamos <br />
                    interfaces intuitivas e fluxos de <br />
                    informação que garantem a <br />
                    máxima adoção e aplicabilidade.</p>
                </section>
                <section className="flex flex-col border-2 border-black px-6 py-8 items-start gap-y-6">
                    <img src={comeia} alt="comeia" />
                    <p className="font-bold text-3xl">Tecnologia e <br />Governança</p>
                    <p className="text-base">Construímos a infraestrutura <br />
                    (ResearchOps) para centralização <br />
                    e automação de dados. <br />
                    Implementamos sistemas que <br />
                    garantem a governança, a <br />
                    segurança e a escalabilidade do <br />
                    seu acervo de pesquisa.</p>
                </section>
            </section>
        </section>
    )
}