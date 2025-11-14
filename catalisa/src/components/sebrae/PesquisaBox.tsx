import React, { useState, useRef, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import personagem from "../../assets/personagem.png"; // Verifique se este caminho está correto
import { useAuth } from "@/store/useAuth.tsx";
import { overlayVariants, modalVariants, newMessageVariants } from "@/utils/motionFunctions.ts";
// Importando os tipos necessários
import type { ConsumoResponseDTO, PesquisaBoxProps } from "@/types/types.ts";

type MessageType = 'system' | 'user';

interface Message {
    id: number;
    type: MessageType;
    content: React.ReactNode;
}

const ratingOptions = [0, 1, 2, 3, 4, 5];

export default function PesquisaBox({ consumo }: PesquisaBoxProps) {
    const messagesEndRef = useRef<HTMLDivElement>(null);
    const [inputValue, setInputValue] = useState("");
    const [isVisible, setIsVisible] = useState(true);
    const [step, setStep] = useState(0);
    const [messages, setMessages] = useState<Message[]>([]);
    const { user } = useAuth();
    const [nota, setNota] = useState<number | null>(null);
    const [nomeProduto, setNomeProduto] = useState("este serviço");
    const isLoaded = useRef(false);

    const scrollToBottom = () => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
        }
    };

    // 1. Busca o nome do produto assim que o componente recebe o 'consumo'
    useEffect(() => {
        if (!consumo.productId) {
            setNomeProduto("o serviço consumido");
            return;
        }

        const fetchProductName = async () => {
            try {
                // Usando caminho relativo para a API
                const res = await fetch(`/api/v2/products/${consumo.productId}`);
                if (!res.ok) throw new Error('Produto não encontrado');

                const produto = await res.json();
                if (produto.name) {
                    setNomeProduto(produto.name); // Atualiza o estado
                }
            } catch (e) {
                console.error("Erro ao buscar nome do produto:", e);
                setNomeProduto("o serviço consumido"); // fallback em caso de erro
            }
        };

        fetchProductName();
    }, [consumo.productId]); // Dependência correta


    useEffect(() => {
        scrollToBottom();
    }, [messages, step]);

    // 2. Carrega as mensagens iniciais APÓS o nome do produto ser definido
    useEffect(() => {
        // Proteção para não rodar antes do fetch ou se já tiver carregado
        if (isLoaded.current || nomeProduto === "este serviço") return;

        isLoaded.current = true;

        const loadInitialMessages = async () => {
            await new Promise(r => setTimeout(r, 500));
            setMessages(prev => [...prev, {
                id: 1,
                type: 'system',
                // Usa a variável nomeProduto
                content: <p className="text-sm">lembra do <span className="font-bold">{nomeProduto}?</span> 🧠</p>
            }]);

            await new Promise(r => setTimeout(r, 1200));
            setMessages(prev => [...prev, {
                id: 2,
                type: 'system',
                content: <p className="text-sm font-bold">Deu certo? 😉</p>
            }]);

            await new Promise(r => setTimeout(r, 1000));
            setMessages(prev => [...prev, {
                id: 3,
                type: 'system',
                content: <p className="text-sm font-bold">Fala pra gente!</p>
            }]);
            setStep(1);
        };

        loadInitialMessages();
    }, [nomeProduto]); // Depende do 'nomeProduto'

    // 3. Resposta inicial do usuário (Sim/Não)
    const handleInteraction = (responseType: 'negative' | 'positive') => {
        if (step !== 1) return;

        const userText = responseType === 'positive' ? "Deu tudo certo!" : "Não tenho certeza...";
        setMessages(prev => [...prev, { id: Date.now(), type: 'user', content: <p className="text-sm font-medium">{userText}</p> }]);

        setStep(2); // Vai para "digitando..."

        setTimeout(() => {
            const feedbackText = responseType === 'positive' ? "Que notícia boa! 🎉" : "Poxa, entendemos...";
            setMessages(prev => [...prev, { id: Date.now(), type: 'system', content: <p className="text-sm">{feedbackText}</p> }]);

            setTimeout(() => {
                setMessages(prev => [...prev, {
                    id: Date.now(),
                    type: 'system',
                    content: <p className="text-sm font-bold">De 0 a 5, como você avaliaria esse serviço?</p>
                }]);
                setStep(3); // Mostra as opções de nota
            }, 1000);
        }, 1000);
    };

    // 4. Usuário seleciona a nota
    const handleRating = (rating: number) => {
        if (step !== 3) return;

        setNota(rating); // Salva a nota no estado

        setMessages(prev => [...prev, { id: Date.now(), type: 'user', content: <p className="text-sm font-bold">{rating}</p> }]);
        setStep(2); // "Digitando..."

        setTimeout(() => {
            setMessages(prev => [...prev, { id: Date.now(), type: 'system', content: <p className="text-sm">Obrigado pela avaliação!</p> }]);

            setTimeout(() => {
                setMessages(prev => [...prev, {
                    id: Date.now(),
                    type: 'system',
                    content: <p className="text-sm">Gostaria de deixar algum comentário por escrito?</p>
                }]);
                setStep(4); // Mostra o input de texto
            }, 800);
        }, 1000);
    };

    // 5. Usuário envia o comentário e finaliza
    const handleSendText = async () => {
        // Proteção para não enviar sem comentário ou sem nota
        if (!inputValue.trim() || nota === null) return;

        const comentarioFinal = inputValue.trim();

        setMessages(prev => [...prev, { id: Date.now(), type: 'user', content: <p className="text-sm">{comentarioFinal}</p> }]);
        setInputValue("");
        setStep(2); // "Digitando..."

        const payload = {
            consumoId: consumo.id, // O ID vindo das props!
            nota: nota,            // A nota salva no estado
            dataPesquisa: new Date().toISOString().split('T')[0], // Data de hoje (yyyy-MM-dd)
            tipoPesquisa: "NPS",   // (Ajuste se for outro tipo, ex: CSAT)
            resposta: comentarioFinal, // O comentário escrito
        };

        try {
            // Envia para a API de Pesquisas
            const res = await fetch('/api/v2/pesquisas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                // Tratar erro de envio
                throw new Error("Falha ao enviar pesquisa");
            }

            // Sucesso!
            setMessages(prev => [...prev, {
                id: Date.now(),
                type: 'system',
                content: <p className="text-sm">Perfeito! Recebemos tudo. 🚀</p>
            }]);
            setStep(5); // "Enviado!"

            setTimeout(() => {
                setIsVisible(false); // Fecha o modal
            }, 2500);

        } catch (error) {
            console.error("Erro no envio:", error);
            // Mostrar mensagem de erro ao usuário se falhar
            setMessages(prev => [...prev, {
                id: Date.now(),
                type: 'system',
                content: <p className="text-sm text-red-300">Ops! Tivemos um problema. Tente mais tarde.</p>
            }]);
            setStep(4); // Volta para a caixa de texto
        }
    };
    // (O bloco setTimeout solto foi removido daqui)

    // 6. Função para lidar com o "Enter" no input
    const handleKeyDown = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter') {
            e.preventDefault(); // Evita quebra de linha em textareas (se usar)
            handleSendText();
        }
    };


    return (
        <AnimatePresence>
            {isVisible && (
                <motion.section
                    className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-[2px] p-4"
                    variants={overlayVariants}
                    initial="hidden"
                    animate="visible"
                    exit="exit"
                >
                    {/* Estilos do Scrollbar */}
                    <style>{`
                        .custom-scrollbar::-webkit-scrollbar { width: 6px; }
                        .custom-scrollbar::-webkit-scrollbar-track { background: transparent; }
                        .custom-scrollbar::-webkit-scrollbar-thumb { background: rgba(255, 255, 255, 0.2); border-radius: 10px; }
                        .custom-scrollbar::-webkit-scrollbar-thumb:hover { background: rgba(255, 255, 255, 0.3); }
                    `}</style>

                    {/* Corpo do Modal */}
                    <motion.section
                        className="bg-[#3730a3] w-full max-w-sm h-[600px] max-h-[90vh] rounded-[30px] p-6 shadow-2xl relative flex flex-col"
                        variants={modalVariants}
                    >

                        {/* Header */}
                        <section className="flex justify-center mb-4 mt-4 shrink-0">
                            <section className="p-4 rounded-lg flex items-center gap-4 bg-[#3730a3] w-full max-w-[90%] relative z-10">
                                <img src={personagem} alt="Personagem" className="w-16 h-16 object-cover rounded-full border-transparent" />
                                <section className="text-white leading-tight">
                                    <h2 className="text-xl font-bold flex items-center gap-2">
                                        Olá, {user?.username}!
                                        <motion.span
                                            initial={{ rotate: 0 }}
                                            animate={{ rotate: [0, 15, -15, 0] }}
                                            transition={{ duration: 0.8, repeat: Infinity, repeatDelay: 3 }}
                                        >👋</motion.span>
                                    </h2>
                                    <p className="text-sm font-light text-gray-200">tudo bem?</p>
                                </section>
                            </section>
                        </section>

                        {/* Área de Mensagens */}
                        <section className="flex-1 overflow-y-auto px-2 py-2 flex flex-col gap-3 custom-scrollbar scroll-smooth">
                            <AnimatePresence mode='popLayout'>
                                {messages.map((msg) => (
                                    <motion.section
                                        key={msg.id}
                                        layout
                                        variants={newMessageVariants}
                                        initial="hidden"
                                        animate="visible"
                                        className={`
                                            py-3 px-5 shadow-md max-w-[85%] w-fit text-sm
                                            ${msg.type === 'system'
                                            ? 'bg-white text-black rounded-2xl rounded-tl-sm self-start'
                                            : 'bg-[#08005E] text-white rounded-2xl rounded-tr-sm self-end break-words'} 
                                        `}
                                    >
                                        {msg.content}
                                    </motion.section>
                                ))}
                            </AnimatePresence>
                            <div ref={messagesEndRef} className="h-1" />
                        </section>

                        {/* Barra de Ações (Input/Botões) */}
                        <section className="mt-4 bg-[#1a1a40] rounded-full p-1 flex items-center justify-between h-16 border border-blue-900/30 shadow-lg shrink-0 overflow-hidden relative">
                            <AnimatePresence mode="wait">

                                {/* Step 0 e 2: "Digitando..." */}
                                {(step === 0 || step === 2) && (
                                    <motion.div key="loading" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} className="w-full h-full flex items-center justify-center">
                                        <span className="text-white/50 text-sm animate-pulse">...</span>
                                    </motion.div>
                                )}

                                {/* Step 1: Botões "Sim/Não" */}
                                {step === 1 && (
                                    <motion.div
                                        key="step1"
                                        initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }}
                                        className="flex w-full h-full"
                                    >
                                        <button onClick={() => handleInteraction('negative')} className="flex-1 text-white font-medium text-sm hover:bg-white/5 transition-colors">Não tenho certeza...</button>
                                        <div className="w-[1px] h-6 bg-gray-500/50 self-center mx-1"></div>
                                        <button onClick={() => handleInteraction('positive')} className="flex-1 text-white font-medium text-sm hover:bg-white/5 transition-colors">Deu tudo certo!</button>
                                    </motion.div>
                                )}

                                {/* Step 3: Botões de Nota */}
                                {step === 3 && (
                                    <motion.div
                                        key="step3"
                                        initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }}
                                        className="flex w-full h-full justify-between items-center px-4"
                                    >
                                        {ratingOptions.map((rating, index) => (
                                            <React.Fragment key={rating}>
                                                <button onClick={() => handleRating(rating)} className="text-white font-bold text-md hover:text-blue-300 transition-colors focus:outline-none">{rating}</button>
                                                {index < ratingOptions.length - 1 && <div className="w-[1px] h-4 bg-white/70"></div>}
                                            </React.Fragment>
                                        ))}
                                    </motion.div>
                                )}

                                {/* Step 4: Input de Texto */}
                                {step === 4 && (
                                    <motion.div
                                        key="step4"
                                        initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: -20 }}
                                        className="flex w-full h-full items-center px-2 gap-2"
                                    >
                                        <input
                                            type="text"
                                            value={inputValue}
                                            onChange={(e) => setInputValue(e.target.value)}
                                            onKeyDown={handleKeyDown} // Função 'Enter'
                                            placeholder="Digite sua resposta..."
                                            className="flex-1 bg-transparent text-white text-sm px-3 py-2 focus:outline-none placeholder-gray-400"
                                            autoFocus
                                        />
                                        <button
                                            onClick={handleSendText}
                                            className="bg-[#0099ff] hover:bg-[#007acc] text-white p-2 rounded-full transition-colors flex items-center justify-center w-10 h-10 shrink-0"
                                        >
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-4 h-4">
                                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5" />
                                            </svg>
                                        </button>
                                    </motion.div>
                                )}

                                {/* Step 5: Enviado! */}
                                {step === 5 && (
                                    <motion.div key="step5" initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="w-full h-full flex items-center justify-center">
                                        <span className="text-green-400 text-sm font-bold animate-pulse">Enviado!</span>
                                    </motion.div>
                                )}

                            </AnimatePresence>
                        </section>
                    </motion.section>
                </motion.section>
            )}
        </AnimatePresence>
    );
}