import { useState, useEffect } from "react";
import { usePesquisas } from "@/hooks/usePesquisas";
import {useSelector} from "@/store/useStore.tsx";

const StarRating = ({ nota }: { nota: number }) => (
    <section className="flex gap-1 bg-[#E3C152CC]/80 px-2 rounded-md border border-yellow-100">
        {Array.from({ length: 5 }).map((_, i) => {
            const active = i < (nota > 5 ? nota / 2 : nota);
            return (
                <span key={i} className={`text-lg ${active ? "text-white" : "text-[#A89863]"}`}>
                    ★
                </span>
            );
        })}
    </section>
);

const dataString = (str: String)=> {
    const f = str.split("-");
    const temp = f[2];
    f[2] = f[0];
    f[0] = temp;
    return f.join("/")
}

export default function Insight() {
    const [page, setPage] = useState(0);
    const { tipoPesquisa } = useSelector();

    useEffect(() => {
        setPage(0);
    }, [tipoPesquisa]);

    const { data, isLoading, isError, error, isPlaceholderData, refetch } = usePesquisas({
        page: page,
        size: 10,
        sort: "id,DESC",
        tipoPesquisa: tipoPesquisa,
    });

    return (
        <section className="flex flex-col w-full min-h-screen bg-gray-200 border border-solid border-black rounded-xl p-2 mt-2 overflow-hidden">
            {isLoading && (
                <section className="flex-1 flex justify-center items-center text-gray-500 gap-3 bg-white">
                    <section className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin"></section>
                    <span>Carregando dados de {tipoPesquisa}...</span>
                </section>
            )}

            {isError && (
                <section className="flex-1 flex flex-col items-center justify-center p-6 text-red-600">
                    <p className="font-bold text-lg mb-2">Erro ao carregar</p>
                    <p className="text-sm text-gray-600 mb-4">{error instanceof Error ? error.message : "Erro desconhecido"}</p>
                    <button onClick={() => refetch()} className="px-4 py-2 bg-white border border-red-200 text-red-600 rounded-lg hover:bg-red-50 transition-colors">
                        Tentar Novamente
                    </button>
                </section>
            )}

            {!isLoading && !isError && (
                (!data?.content || data.content.length === 0) ? (

                    <section className="flex-1 flex flex-col items-center justify-center p-8 text-gray-400">
                        <section className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mb-4 text-3xl grayscale opacity-50">📂</section>
                        <p className="font-medium">Nenhum feedback encontrado para <span className="font-bold text-gray-600">{tipoPesquisa}</span>.</p>
                    </section>
                ) : (

                    <section className="flex-1 overflow-y-auto p-4 bg-[#F7F7F7] rounded-xl">
                        <section className="space-y-4">
                            {data.content.map((pesquisa) => (
                                <section key={pesquisa.id} className="p-4 rounded-xl bg-gray-50/30 hover:bg-gray-50  transition-colors flex flex-col gap-3">
                                    <section className="flex flex-wrap items-center justify-between gap-2">
                                        <section className="flex items-center gap-5 flex-wrap">
                                            <h3 className="font-bold text-gray-800 text-sm uppercase tracking-wide">
                                                {pesquisa.nomeUsuario || `CLIENTE #${pesquisa.consumoId}`}
                                            </h3>

                                            <span className="bg-red-100 text-red-800 text-xs font-semibold px-3 py-1 rounded-md">
                                                {pesquisa.dataPesquisa ? dataString(pesquisa.dataPesquisa) : "--/--/----"}
                                            </span>

                                            <span className={`text-xs font-semibold px-3 py-1 rounded-md flex items-center gap-1 ${
                                                tipoPesquisa === "NPS" ? "bg-blue-100 text-blue-800" : "bg-green-100 text-green-800"
                                            }`}>
                                                {pesquisa.tagAtendimento || (tipoPesquisa === "NPS" ? "Atendimento presencial" : "Satisfação")}
                                            </span>

                                            <span className="text-xs text-gray-500 font-medium flex items-center gap-1">
                                                {pesquisa.nomeProduto}
                                            </span>
                                            <StarRating nota={pesquisa.nota} />
                                        </section>


                                    </section>

                                    <section className={`p-8 rounded-md text-md leading-relaxed border ${
                                        pesquisa.resposta
                                            ? "bg-white border-gray-200 text-gray-700" 
                                            : "bg-transparent border-transparent text-gray-400 italic"
                                    }`}>
                                        {pesquisa.resposta || "Sem comentário."}
                                    </section>
                                    <section className={"w-full border-1 border-gray-300"}></section>
                                </section>
                            ))}
                        </section>
                    </section>
                )
            )}

            {data?.content && data.content.length > 0 && (
                <section className="p-4 bg-gray-50 border-t border-gray-200 flex justify-between items-center shrink-0 z-10">
                    <button
                        onClick={() => setPage((old) => Math.max(old - 1, 0))}
                        disabled={page === 0}
                        className="px-4 py-2 text-sm font-medium text-gray-600 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed shadow-sm transition-all"
                    >
                        Anterior
                    </button>

                    <span className="text-sm text-gray-500 font-medium">
                        Página <span className="text-gray-900 font-bold">{data.number + 1}</span> de {data.totalPages}
                    </span>

                    <button
                        onClick={() => {
                            if (!isPlaceholderData && !data.last) setPage((old) => old + 1);
                        }}
                        disabled={isPlaceholderData || data.last}
                        className="px-4 py-2 text-sm font-medium text-gray-600 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed shadow-sm transition-all"
                    >
                        Próxima
                    </button>
                </section>
            )}
        </section>
    );
}