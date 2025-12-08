// ----- ConsumoButton.tsx -----
// (O botão que cria o 'Consumo' pendente)

import { useState } from 'react';
import { useAuth } from '@/store/useAuth'; // (Dependência: store de autenticação)
import { useNavigate } from 'react-router-dom';

interface CriarConsumoProps {
    productId: number;
    children: React.ReactNode;
    className?: string;
}

export function ConsumoButton({
                                  productId,
                                  children,
                                  className
                              }: CriarConsumoProps) {

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const { user } = useAuth(); // Pega o usuário logado
    const navigate = useNavigate();

    const handleCreateConsumo = async () => {
        if (!user || !user.id) {
            setError("Você precisa estar logado para concluir esta ação.");
            return;
        }

        setIsLoading(true);
        setError(null);

        const dataConsumo = new Date().toISOString().split('T')[0];


        const consumoRequest = {
            userId: user.id,
            productId: productId,
            dataConsumo: dataConsumo,
            avaliacao: 1,
            pesquisaRespondida: false,
            pesquisaId: null
        };

        try {
            const response = await fetch('http://localhost:8080/api/v1/consumos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(consumoRequest)
            });

            if (!response.ok) {
                const errorData = await response.json();
                console.error("Erro do Backend (ConsumoButton):", errorData);
                throw new Error(errorData.message || "Falha ao salvar seu progresso");
            }

            console.log("Consumo criado com sucesso (pendente)!", await response.json());
            setIsLoading(false);

            // Leva o utilizador direto para a página que irá "capturar" este consumo
            navigate('/pesquisa');

        } catch (err: any) {
            console.error("Erro ao criar consumo:", err);
            setError(err.message);
            setIsLoading(false);
        }
    };

    return (
        <div className="flex flex-col items-center">
            <button
                onClick={handleCreateConsumo}
                disabled={isLoading}
                className={`bg-blue-600 text-white font-bold py-3 px-6 rounded-lg shadow-md transition-opacity hover:opacity-90 disabled:opacity-50 disabled:cursor-not-allowed ${className}`}
            >
                {isLoading ? 'Salvando...' : children}
            </button>
            {error && (
                <p className="text-red-500 text-sm mt-2">{error}</p>
            )}
        </div>
    );
}