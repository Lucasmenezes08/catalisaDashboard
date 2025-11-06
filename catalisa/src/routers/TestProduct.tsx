import React, { useState, useEffect } from 'react';
import type { FormEvent } from "react";

// --- Interfaces (Baseadas na sua API) ---

// DTO para criar ou atualizar um produto
interface ProductRequestDTO {
    name: string;
    type: "CURSO" | "VIDEO_AULA" | "ARTIGO" | ""; // "" para o estado inicial
    description?: string;
}

// DTO que recebemos do backend
interface ProductResponseDTO {
    id: number;
    name: string;
    type: "CURSO" | "VIDEO_AULA" | "ARTIGO";
    description: string | null;
}

// Estado inicial vazio para o formulário
const initialState: ProductRequestDTO = {
    name: "",
    type: "",
    description: ""
};

/**
 * Página de CRUD (Create, Read, Update, Delete) para
 * "alimentar" a tabela de Produtos.
 */
export default function TestProduct() {
    const [products, setProducts] = useState<ProductResponseDTO[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // Estado para o formulário
    const [formData, setFormData] = useState<ProductRequestDTO>(initialState);
    const [editingId, setEditingId] = useState<number | null>(null);

    // --- 1. BUSCAR PRODUTOS (READ) ---
    const fetchProducts = async () => {
        setIsLoading(true);
        setError(null);
        try {
            // Usamos a URL relativa, pois o vite.config.ts faz o proxy
            const response = await fetch('http://localhost:8080/api/v1/products');
            if (!response.ok) {
                throw new Error("Falha ao buscar produtos");
            }
            const data: ProductResponseDTO[] = await response.json();
            setProducts(data);
        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    // Efeito para buscar os produtos ao carregar a página
    useEffect(() => {
        fetchProducts();
    }, []);

    // --- 2. CRIAR / ATUALIZAR PRODUTOS (CREATE / UPDATE) ---
    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (!formData.name || !formData.type) {
            setError("Nome e Tipo são obrigatórios.");
            return;
        }

        setIsLoading(true);
        setError(null);

        const url = editingId
            ? `http://localhost:8080/api/v1/products/${editingId}` // URL de Update
            : 'http://localhost:8080/api/v1/products';             // URL de Create

        const method = editingId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData)
            });

            if (!response.ok) {
                // 409 Conflict é um erro comum (ex: nome duplicado)
                if (response.status === 409) {
                    const errData = await response.json();
                    throw new Error(errData.message || "Conflito: Produto já existe.");
                }
                throw new Error(`Falha ao ${editingId ? 'atualizar' : 'criar'} produto`);
            }

            // Sucesso
            resetForm();
            await fetchProducts(); // Re-buscar a lista atualizada

        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    // --- 3. DELETAR PRODUTO (DELETE) ---
    const handleDelete = async (id: number) => {
        // NOTA: Em um app real, usaríamos um modal de confirmação.
        // Como 'alert' e 'confirm' são proibidos, deletamos direto.

        setIsLoading(true);
        setError(null);
        try {
            const response = await fetch(`http://localhost:8080/api/v1/products/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok && response.status !== 204) { // 204 No Content é sucesso
                throw new Error("Falha ao deletar produto");
            }

            // Sucesso: re-buscar a lista
            await fetchProducts();

        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    // --- Funções Auxiliares do Formulário ---

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleEditClick = (product: ProductResponseDTO) => {
        setEditingId(product.id);
        setFormData({
            name: product.name,
            type: product.type,
            description: product.description || ""
        });
        window.scrollTo(0, 0); // Rolar para o topo (onde está o form)
    };

    const resetForm = () => {
        setFormData(initialState);
        setEditingId(null);
        setError(null);
    };


    // --- RENDERIZAÇÃO ---
    return (
        <div className="max-w-4xl mx-auto p-6 md:p-10 bg-gray-50 min-h-screen">
            <h1 className="text-3xl font-bold text-gray-800 mb-8">
                Gerenciador de Produtos
            </h1>

            {/* --- Formulário de Criação / Edição --- */}
            <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-lg mb-10">
                <h2 className="text-2xl font-semibold mb-6 text-gray-700">
                    {editingId ? "Editar Produto" : "Adicionar Novo Produto"}
                </h2>

                {/* Linha 1: Nome */}
                <div className="mb-4">
                    <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                        Nome do Produto (obrigatório)
                    </label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Linha 2: Tipo */}
                <div className="mb-4">
                    <label htmlFor="type" className="block text-sm font-medium text-gray-700 mb-1">
                        Tipo (obrigatório)
                    </label>
                    <select
                        id="type"
                        name="type"
                        value={formData.type}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    >
                        <option value="" disabled>Selecione um tipo...</option>
                        <option value="CURSO">Curso</option>
                        <option value="VIDEO_AULA">Vídeo Aula</option>
                        <option value="ARTIGO">Artigo</option>
                    </select>
                </div>

                {/* Linha 3: Descrição */}
                <div className="mb-6">
                    <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                        Descrição (opcional)
                    </label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleInputChange}
                        rows={3}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                {/* Mensagem de Erro */}
                {error && (
                    <div className="my-4 p-3 bg-red-100 text-red-700 rounded-lg">
                        {error}
                    </div>
                )}

                {/* Botões */}
                <div className="flex items-center gap-4">
                    <button
                        type="submit"
                        disabled={isLoading}
                        className="bg-blue-600 text-white font-bold py-2 px-6 rounded-lg shadow-md hover:bg-blue-700 transition-colors disabled:opacity-50"
                    >
                        {isLoading ? "Salvando..." : (editingId ? "Atualizar" : "Adicionar")}
                    </button>
                    {editingId && (
                        <button
                            type="button"
                            onClick={resetForm}
                            disabled={isLoading}
                            className="bg-gray-500 text-white font-bold py-2 px-6 rounded-lg shadow-md hover:bg-gray-600 transition-colors"
                        >
                            Cancelar Edição
                        </button>
                    )}
                </div>
            </form>

            {/* --- Lista de Produtos Existentes --- */}
            <section>
                <h2 className="text-2xl font-semibold mb-6 text-gray-700">
                    Produtos Existentes
                </h2>
                {isLoading && products.length === 0 && <p>Carregando produtos...</p>}

                <div className="space-y-4">
                    {products.length === 0 && !isLoading && <p>Nenhum produto encontrado.</p>}

                    {products.map(product => (
                        <div key={product.id} className="bg-white p-5 rounded-lg shadow-lg flex flex-col sm:flex-row sm:items-center sm:justify-between">
                            {/* Info */}
                            <div className="mb-4 sm:mb-0">
                                <div className="flex items-center gap-3">
                                    <span className="font-mono text-xs text-gray-500">(ID: {product.id})</span>
                                    <strong className="text-lg text-gray-800">{product.name}</strong>
                                </div>
                                <span className="text-sm text-white bg-indigo-500 px-3 py-0.5 rounded-full ml-10">
                                    {product.type}
                                </span>
                                <p className="text-gray-600 mt-2 ml-10">
                                    {product.description || <i>Sem descrição</i>}
                                </p>
                            </div>

                            {/* Ações */}
                            <div className="flex-shrink-0 flex gap-3">
                                <button
                                    onClick={() => handleEditClick(product)}
                                    disabled={isLoading}
                                    className="bg-yellow-500 text-white text-sm font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-yellow-600 transition-colors"
                                >
                                    Editar
                                </button>
                                <button
                                    onClick={() => handleDelete(product.id)}
                                    disabled={isLoading}
                                    className="bg-red-600 text-white text-sm font-semibold py-2 px-4 rounded-lg shadow-md hover:bg-red-700 transition-colors"
                                >
                                    Deletar
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </section>
        </div>
    );
}