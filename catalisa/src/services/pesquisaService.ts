import type { Pesquisa, Page } from "@/types/types.ts";

const URL = "http://localhost:8080/api/v2/pesquisas";

export async function fetchPesquisas(
    page = 0,
    size = 10,
    sort = "dataPesquisa,DESC",
    tipoPesquisa = "NPS"
): Promise<Page<Pesquisa>> {

    const params = new URLSearchParams();
    params.append("page", String(page));
    params.append("size", String(size));
    params.append("sort", sort);
    params.append("tipoPesquisa", tipoPesquisa);

    const response = await fetch(`${URL}?${params.toString()}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error(`Erro na requisição: ${response.status} ${response.statusText}`);
    }

    return await response.json();
}