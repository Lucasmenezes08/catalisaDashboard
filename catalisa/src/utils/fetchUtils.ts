async function handleResponse(res: Response) {
    if (res.status === 204) {
        return null; // Sem conteúdo, sucesso
    }

    const data = await res.json();

    if (!res.ok) {
        // Se a API enviar uma mensagem de erro estruturada, usamos isso
        const message = data?.message || `Erro HTTP: ${res.status} ${res.statusText}`;
        throw new Error(message);
    }

    return data;
}

/**
 * Wrapper de Fetch para simplificar as chamadas GET
 */
export const fetchApi = {
    get: async (url: string) => {
        const res = await fetch(url);
        return handleResponse(res);
    },

    /**
     * Wrapper de Fetch para POST, PUT, PATCH
     */
    post: async (url: string, body: any, method: 'POST' | 'PUT' | 'PATCH' = 'POST') => {
        const res = await fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        return handleResponse(res);
    },

    /**
     * Wrapper de Fetch para DELETE
     */
    del: async (url: string) => {
        const res = await fetch(url, {
            method: 'DELETE',
        });
        return handleResponse(res);
    },
};