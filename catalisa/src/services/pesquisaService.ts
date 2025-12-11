import type { Pesquisa, Page } from "@/types/types.ts";

const URL = "http://localhost:8080/api/v2/pesquisas";

export async function fetchPesquisas(
    page = 0,
    size = 10,
    sort = "id,DESC",
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

    const data: Page<Pesquisa> = await response.json();
    const pesquisasComNomes = await Promise.all(
        data.content.map(async (pesquisa) => {
            try {
                const userResponse = await fetch(`${URL}/${pesquisa.id}/user`);

                if (userResponse.ok) {
                    const userData = await userResponse.json();
                    return { ...pesquisa, nomeUsuario: userData.username };
                }
            } catch (error) {
                console.warn(`Erro ao buscar user para pesquisa ${pesquisa.id}`, error);
            }
            return { ...pesquisa, nomeUsuario: "Usuário Desconhecido" };
        })
    );

    return {
        ...data,
        content: pesquisasComNomes
    };
}