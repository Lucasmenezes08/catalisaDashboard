// src/components/admin/CadastroProduto.tsx (Exemplo)
import React, { useState } from 'react';

export default function CadastroProduto() {
    const [name, setName] = useState('');
    const [type, setType] = useState('');
    const [description, setDescription] = useState('');
    const [message, setMessage] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setMessage('');

        // 1. Monta o payload para POST /api/v2/products
        const payload = {
            name,
            type: type.toUpperCase(),
            description };

        try {
            const res = await fetch(' http://localhost:8080/api/v2/products', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.status === 201) {
                const newProduct = await res.json();
                setMessage(`Produto '${newProduct.name}' (ID: ${newProduct.id}) criado com sucesso!`);
                // Limpa o formulário
                setName('');
                setType('');
                setDescription('');
            } else {
                // Trata erros comuns, como duplicidade (409)
                const err = await res.json();
                if (res.status === 409) {
                    setMessage(`Erro: ${err.message || 'Já existe um produto com esse nome.'}`);
                } else {
                    throw new Error(err.message || "Falha ao criar produto");
                }
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
            <h2 className="text-xl font-bold mb-4">Cadastrar Novo Produto</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                        Nome do Produto
                    </label>
                    <input
                        id="name"
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="w-full p-2 border rounded"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="type" className="block text-sm font-medium text-gray-700 mb-1">
                        Tipo (ex: Curso, Bebida)
                    </label>
                    <input
                        id="type"
                        type="text"
                        value={type}
                        onChange={(e) => setType(e.target.value)}
                        className="w-full p-2 border rounded"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                        Descrição
                    </label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        className="w-full p-2 border rounded"
                        rows={3}
                    />
                </div>

                <button
                    type="submit"
                    disabled={isLoading}
                    className="w-full bg-green-600 text-white p-2 rounded hover:bg-green-700 disabled:bg-gray-400"
                >
                    {isLoading ? "Salvando..." : "Cadastrar Produto"}
                </button>
            </form>
            {message && <p className="mt-4 text-sm text-center">{message}</p>}
        </div>
    );
}