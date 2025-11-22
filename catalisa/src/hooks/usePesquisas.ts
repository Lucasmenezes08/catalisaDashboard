import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { fetchPesquisas } from "@/services/pesquisaService.ts";

interface UsePesquisasParams {
    page?: number;
    size?: number;
    sort?: string;
    tipoPesquisa?: string;
}

export const usePesquisas = ({
    page = 0,
    size = 10,
    sort = "dataPesquisa,DESC",
    tipoPesquisa = "NPS"
    }: UsePesquisasParams = {}) => {
    return useQuery({
        queryKey: ["pesquisas", page, size, sort, tipoPesquisa],
        queryFn: () => fetchPesquisas(page, size, sort, tipoPesquisa),
        placeholderData: keepPreviousData,
        staleTime: 1000 * 60 * 5,
    })
}