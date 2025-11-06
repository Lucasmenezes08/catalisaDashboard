// ----- TestConsumo.tsx -----
// (Página que simula um curso ou artigo)

import { ConsumoButton } from "@/components/sebrae/ConsumoButton.tsx";

export default function TestConsumo() {

    // Esta página "sabe" apenas qual é o produto
    // (Ex: ID 1 = "Curso de Gestão Financeira")
    const ID_DO_PRODUTO_ATUAL = 1;

    return (
        <div className="p-10 text-center">
            <h1 className="text-3xl font-bold">Página do Curso/Artigo</h1>
            <p className="mt-4">...conteúdo do curso...</p>

            <div className="mt-10 border-t pt-8">
                <p className="mb-4">Clique abaixo para marcar como concluído.</p>

                {/* Este botão vai criar o 'Consumo'
                  com 'pesquisaRespondida: false' e 'pesquisaId: null'.
                  O ID do usuário virá do 'useAuth'.
                */}
                <ConsumoButton
                    productId={ID_DO_PRODUTO_ATUAL}
                >
                    Concluir Atividade
                </ConsumoButton>
            </div>
        </div>
    );
}