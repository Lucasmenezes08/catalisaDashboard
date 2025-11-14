// src/components/admin/RegistroConsumo.tsx (Exemplo)
import React, { useState, useEffect } from 'react';
import { useAuth } from '@/store/useAuth';
import type { ProductResponseDTO } from '@/types/types.ts'; // Importe seus tipos

export default function RegistroConsumo() {
    const { user } = useAuth();
    const [products, setProducts] = useState<ProductResponseDTO[]>([]);
    const [selectedProductId, setSelectedProductId] = useState<string>('');
    const [message, setMessage] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);

    // 1. Carrega todos os produtos disponíveis
    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const res = await fetch('http://localhost:8080/api/v2/products');
                const data = await res.json();
                setProducts(data);
            } catch (error) {
                console.error("Erro ao buscar produtos:", error);
            }
        };
        fetchProducts();
    }, []);

    const handleConsumir = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!user || !selectedProductId) {
            setMessage("Erro: Selecione um usuário e um produto.");
            return;
        }

        setIsLoading(true);
        setMessage('');

        const payload = {
            userId: user.id,
            productId: parseInt(selectedProductId),
            dataConsumo: new Date().toISOString().split('T')[0],
            consumiuPesquisa: false,
            pesquisa: null
        }
        try {
            const res = await fetch('http://localhost:8080/api/v2/consumos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.status === 201) {
                setMessage("Consumo registrado com sucesso! O usuário agora tem uma pesquisa pendente.");
            } else {
                const err = await res.json();
                throw new Error(err.message || "Falha ao registrar consumo");
            }
        } catch (error: any) {
            console.error(error);
            setMessage(`Erro: ${error.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="p-4 max-w-md mx-auto bg-white shadow-md rounded-lg mt-10">
            <h2 className="text-xl font-bold mb-4">Registrar Consumo de Produto</h2>
            <form onSubmit={handleConsumir}>
                <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Usuário Logado
                    </label>
                    <input
                        type="text"
                        value={user ? user.username : 'Nenhum usuário logado'}
                        disabled
                        className="w-full p-2 border rounded bg-gray-100"
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="product" className="block text-sm font-medium text-gray-700 mb-1">
                        Selecione o Produto
                    </label>
                    <select
                        id="product"
                        value={selectedProductId}
                        onChange={(e) => setSelectedProductId(e.target.value)}
                        className="w-full p-2 border rounded"
                        required
                    >
                        <option value="" disabled>Selecione...</option>
                        {products.map((prod) => (
                            <option key={prod.id} value={prod.id}>
                                {prod.name} (ID: {prod.id})
                            </option>
                        ))}
                    </select>
                </div>

                <button
                    type="submit"
                    disabled={isLoading || !user}
                    className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700 disabled:bg-gray-400"
                >
                    {isLoading ? "Registrando..." : "Registrar Consumo"}
                </button>
            </form>
            {message && <p className="mt-4 text-sm text-center">{message}</p>}
        </div>
    );
}