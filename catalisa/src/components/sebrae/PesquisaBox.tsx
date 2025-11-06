import { motion } from "framer-motion";
import { useState, useEffect, Fragment } from "react";
import personagem from "../../assets/personagem.png";

// --- DEFINIÇÃO DAS MENSAGENS E IDs DE PERGUNTAS ---
// (Estes IDs precisam existir na sua tabela 'PERGUNTAS' no banco)

// TODO: Confirme que o ID '4' é a sua pergunta "de 0 a 5..."
const RATING_PERGUNTA_ID = 4;

// TODO: Confirme que estes IDs (21, 22, 23, 24) são os IDs REAIS da sua tabela 'PERGUNTAS'
const feedbackQuestionsMap = {
    '0-2': { id: 21, text: "que pena! 😢 o que aconteceu?" },
    '3':   { id: 22, text: "horm... 🤔 o que poderia ter sido diferente?" },
    '4':   { id: 23, text: "que bom! 😊 e o que dá pra melhorar?" },
    '5':   { id: 24, text: "que massa! 🤩 o que você mais gostou?" }
};
// --- FIM DAS DEFINIÇÕES ---


// (Interfaces, ícones e funções helper)
interface ChatMessage { id: number; text: string; sender: 'bot' | 'user'; }
const initialBotMessages: ChatMessage[] = [
    { id: 1, text: "lembra do <strong>curso de Gestão Financeira?</strong> 🧠", sender: 'bot' },
    { id: 2, text: "Deu certo? 😃", sender: 'bot' },
    { id: 3, text: "Fala pra gente!", sender: 'bot' }
];
const ratingBotMessage: ChatMessage = { id: RATING_PERGUNTA_ID, text: "de 0 a 5, <strong>como você avaliaria esse serviço?</strong>", sender: 'bot' };
const getFeedbackQuestion = (rating: number): { id: number, text: string } => {
    if (rating <= 2) return feedbackQuestionsMap['0-2'];
    if (rating === 3) return feedbackQuestionsMap['3'];
    if (rating === 4) return feedbackQuestionsMap['4'];
    return feedbackQuestionsMap['5'];
};
const finalThanksMessages: ChatMessage[] = [
    { id: 100, text: "Obrigado pelo feedback, Rafael!", sender: 'bot' },
    { id: 101, text: "Sua opinião é essencial para continuarmos melhorando! 🚀", sender: 'bot' }
];
const SendArrowIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-5 h-5">
        <path d="M3.478 2.405a.75.75 0 00-.926.94l2.432 7.905H13.5a.75.75 0 010 1.5H4.984l-2.432 7.905a.75.75 0 00.926.94 60.519 60.519 0 0018.445-8.986.75.75 0 000-1.218A60.517 60.517 0 003.478 2.405z" />
    </svg>
);
const variants = { hidden: { y: "100%" }, visible: { y: 0 } };
const mensagensVariants = {
    hidden: { opacity: 0, y: 15 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.4 } },
};

// --- DEFINIÇÃO DAS PROPS ---
interface PesquisaBoxProps {
    isActive: boolean;
    consumoId: number;  // Recebe SÓ o ID do Consumo (ex: 5)
    onClose: () => void;
}

// --- COMPONENTE PRINCIPAL ---
export default function PesquisaBox({ isActive, consumoId, onClose }: PesquisaBoxProps) {

    console.log("PesquisaBox (Filho): RECEBEU AS PROPS:", { consumoId });

    const [visibleMessages, setVisibleMessages] = useState<ChatMessage[]>([]);
    const [feedbackText, setFeedbackText] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Estados para guardar as respostas
    const [currentRating, setCurrentRating] = useState<number | null>(null);
    const [currentFeedbackQuestionId, setCurrentFeedbackQuestionId] = useState<number | null>(null);

    type ChatStep = 'loading' | 'initial_choice' | 'rating' | 'feedback_input' | 'finishing' | 'finished';
    const [chatStep, setChatStep] = useState<ChatStep>('loading');

    // useEffect: Inicia o chat
    useEffect(() => {
        if (isActive) {
            setVisibleMessages([]); setFeedbackText(""); setIsSubmitting(false);
            setCurrentRating(null); setCurrentFeedbackQuestionId(null); setChatStep('loading');

            if (!consumoId) {
                console.error("PesquisaBox: consumoId é nulo!", { consumoId });
                onClose(); return;
            }

            const messageDelay = 1500;
            const timers: NodeJS.Timeout[] = [];
            initialBotMessages.forEach((msg, index) => {
                const timerId = setTimeout(() => { setVisibleMessages((prev) => [...prev, msg]); }, (index + 1) * messageDelay);
                timers.push(timerId);
            });
            const buttonTimerId = setTimeout(() => { setChatStep('initial_choice'); }, (initialBotMessages.length + 1) * messageDelay);
            timers.push(buttonTimerId);

            return () => { timers.forEach((timerId) => clearTimeout(timerId)); };
        }
    }, [isActive, consumoId, onClose]);

    // (Funções de UI do chat)
    const handleInitialChoice = (text: string) => {
        if (chatStep !== 'initial_choice') return;
        setChatStep('rating');
        const userMessage: ChatMessage = { id: Date.now(), text, sender: 'user' };
        setVisibleMessages((prev) => [...prev, userMessage]);
        setTimeout(() => { setVisibleMessages((prev) => [...prev, ratingBotMessage]); }, 1200);
    };

    const handleRating = (rating: number) => {
        if (chatStep !== 'rating') return;
        setChatStep('feedback_input');
        setCurrentRating(rating); // Guarda a nota
        const feedbackQuestion = getFeedbackQuestion(rating);
        setCurrentFeedbackQuestionId(feedbackQuestion.id); // Guarda o ID da pergunta de feedback
        const userMessage: ChatMessage = { id: Date.now(), text: rating.toString(), sender: 'user' };
        const feedbackQuestionMessage: ChatMessage = { id: Date.now() + 1, text: feedbackQuestion.text, sender: 'bot' };
        setVisibleMessages((prev) => [...prev, userMessage]);
        setTimeout(() => { setVisibleMessages((prev) => [...prev, feedbackQuestionMessage]); }, 1200);
    };

    // --- LÓGICA DE SUBMISSÃO (O FLUXO DE 3 ETAPAS) ---
    const handleFeedbackSubmit = async () => {
        if (chatStep !== 'feedback_input') return;
        setIsSubmitting(true);
        setChatStep('finishing');

        const userFeedbackText = feedbackText.trim() === "" ? "enviado!" : feedbackText;
        const feedbackMessage: ChatMessage = { id: Date.now(), text: userFeedbackText, sender: 'user' };
        setVisibleMessages((prev) => [...prev, feedbackMessage]);
        setFeedbackText("");

        try {
            // --- ETAPA 1: CRIAR A PESQUISA ---
            // (Vincula o Consumo e as Perguntas que serão respondidas)

            const perguntaIds = [
                { id: RATING_PERGUNTA_ID },
                { id: currentFeedbackQuestionId }
            ].filter(p => p.id !== null);

            const pesquisaResponse = await fetch("http://localhost:8080/api/v1/pesquisas", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    consumo: { id: consumoId }, // Vincula ao Consumo (ex: 5)
                    perguntas: perguntaIds,     // Vincula às Perguntas (ex: 4 e 24)
                    respostas: []               // Respostas virão na Etapa 2
                })
            });

            if (!pesquisaResponse.ok) {
                const err = await pesquisaResponse.json();
                throw new Error(`Falha ao criar pesquisa: ${err.message}`);
            }

            const novaPesquisa = await pesquisaResponse.json();
            const newPesquisaId = novaPesquisa.id; // (ex: 8)

            if (!newPesquisaId) {
                throw new Error("Falha ao obter o ID da nova pesquisa criada.");
            }
            console.log("Etapa 1: Pesquisa criada com ID:", newPesquisaId);

            // --- ETAPA 2: CRIAR AS RESPOSTAS ---
            // (Agora que temos um newPesquisaId)

            // a. Resposta da Nota (Rating)
            await fetch("http://localhost:8080/api/v1/respostas", {
                method: "POST", headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    pesquisa: { id: newPesquisaId },
                    pergunta: { id: RATING_PERGUNTA_ID },
                    resposta: currentRating?.toString() || "0"
                })
            });

            // b. Resposta do Texto (Feedback)
            if (feedbackText.trim() !== "") {
                await fetch("http://localhost:8080/api/v1/respostas", {
                    method: "POST", headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        pesquisa: { id: newPesquisaId },
                        pergunta: { id: currentFeedbackQuestionId },
                        resposta: feedbackText
                    })
                });
            }
            console.log("Etapa 2: Respostas criadas e ligadas à Pesquisa", newPesquisaId);

            // --- ETAPA 3: ATUALIZAR O CONSUMO ---
            // (Marcar como "concluído" e vincular a Pesquisa)

            const consumoResponse = await fetch(`http://localhost:8080/api/v1/consumos/${consumoId}`, {
                method: "PATCH", headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    avaliacao: currentRating,
                    pesquisaRespondida: true, // <-- MARCA COMO CONCLUÍDO
                    pesquisaId: newPesquisaId   // <-- LIGA A PESQUISA AO CONSUMO
                })
            });
            if (!consumoResponse.ok) throw new Error('Falha ao atualizar consumo');

            console.log("Etapa 3: Consumo atualizado e marcado como respondido.");

            // --- SUCESSO: Mostrar agradecimento e botão de fechar ---
            const messageDelay = 1200;
            setTimeout(() => { setVisibleMessages((prev) => [...prev, finalThanksMessages[0]]); }, messageDelay);
            setTimeout(() => {
                setVisibleMessages((prev) => [...prev, finalThanksMessages[1]]);
                setChatStep('finished');
            }, messageDelay * 2);

        } catch (error) {
            console.error("Erro no fluxo de 3 etapas:", error);
            // TODO: Mostrar mensagem de erro para o utilizador
            setIsSubmitting(false);
            setChatStep('feedback_input');
        }
    };


    // --- RETURN (JSX) ---
    return (
        <motion.section
            initial={'hidden'}
            variants={variants}
            animate={isActive ? 'visible' : 'hidden'}
            transition={{ ease: "easeInOut", duration: 1 }}
            className={"absolute flex flex-col top-[13%] bottom-0 left-[50%] translate-x-[-50%] w-full max-w-md bg-[#2E23AA] rounded-t-3xl py-5 px-5 overflow-hidden"}
        >
            {/* Header */}
            <section className={"flex flex-row items-start justify-center gap-1"}>
                <img className={"w-24 h-24"} src={personagem} alt="Personagem"/>
                <section className={"flex flex-col pt-3 text-white"}>
                    <p className={"font-bold text-lg"}>Eai Rafael!</p>
                    <p className={"text-sm"}>Tudo bem?</p>
                </section>
            </section>

            {/* Lista de Mensagens */}
            <section className="flex flex-1 overflow-y-auto min-h-0 flex-col w-full mt-8 space-y-3 pr-2">
                {visibleMessages.map((msg) => (
                    <motion.div
                        key={msg.id}
                        className={`flex w-full ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}
                        variants={mensagensVariants}
                        initial="hidden"
                        animate="visible"
                    >
                        <div
                            className={`py-3 px-4 max-w-[85%] rounded-xl shadow-md ${
                                msg.sender === 'bot'
                                    ? 'bg-white text-[#2E23AA] rounded-bl-none'
                                    : 'bg-[#4A3DBC] text-white rounded-br-none'
                            }`}
                        >
                            <p className="text-base font-medium" dangerouslySetInnerHTML={{ __html: msg.text }} />
                        </div>
                    </motion.div>
                ))}
            </section>

            {/* Rodapé Condicional */}
            <section className="w-full mt-auto pt-8">
                {/* ETAPA 1: ESCOLHA INICIAL */}
                {chatStep === 'initial_choice' && (
                    <motion.section variants={mensagensVariants} initial="hidden" animate="visible">
                        <section className="flex bg-[#4A3DBC] rounded-full text-white font-medium text-sm shadow-lg">
                            <button onClick={() => handleInitialChoice('Não tenho certeza...')} className="flex-1 py-4 px-3 text-center hover:bg-white/10 rounded-l-full cursor-pointer">
                                Não tenho certeza...
                            </button>
                            <section className="w-px bg-white/40 my-3"></section>
                            <button onClick={() => handleInitialChoice('Deu tudo certo!')} className="flex-1 py-4 px-3 text-center hover:bg-white/10 rounded-r-full cursor-pointer">
                                Deu tudo certo!
                            </button>
                        </section>
                    </motion.section>
                )}

                {/* ETAPA 2: AVALIAÇÃO (NOTAS 0-5) */}
                {chatStep === 'rating' && (
                    <motion.section variants={mensagensVariants} initial="hidden" animate="visible">
                        <section className="flex bg-[#4A3DBC] rounded-full text-white font-medium text-sm shadow-lg">
                            {[0, 1, 2, 3, 4, 5].map((num, index) => (
                                <Fragment key={num}>
                                    <button onClick={() => handleRating(num)} className="flex-1 py-4 px-3 text-center hover:bg-white/10 rounded-full">
                                        {num}
                                    </button>
                                    {index < 5 && <section className="w-px bg-white/40 my-3"></section>}
                                </Fragment>
                            ))}
                        </section>
                    </motion.section>
                )}

                {/* ETAPA 3: INPUT DE FEEDBACK */}
                {chatStep === 'feedback_input' && (
                    <motion.section variants={mensagensVariants} initial="hidden" animate="visible">
                        <section className="flex items-center bg-[#4A3DBC] rounded-full text-white font-medium text-sm shadow-lg pr-3">
                            <input
                                type="text"
                                value={feedbackText}
                                onChange={(e) => setFeedbackText(e.target.value)}
                                placeholder="escreva aqui (opcional)"
                                className="flex-1 bg-transparent py-4 px-6 text-white placeholder:text-white/60 focus:outline-none"
                                disabled={isSubmitting}
                            />
                            <button
                                onClick={handleFeedbackSubmit}
                                className="p-2 rounded-full text-white hover:bg-white/20 transition-colors disabled:opacity-50"
                                aria-label="Enviar feedback"
                                disabled={isSubmitting}
                            >
                                <SendArrowIcon />
                            </button>
                        </section>
                    </motion.section>
                )}

                {/* ETAPA 4: CHAT FINALIZADO (Botão de Fechar) */}
                {chatStep === 'finished' && (
                    <motion.section variants={mensagensVariants} initial="hidden" animate="visible">
                        <button
                            onClick={onClose} // Chama a função para fechar o modal
                            className="w-full bg-[#4A3DBC] rounded-full text-white font-medium text-sm shadow-lg py-4 px-3 text-center hover:bg-white/10 cursor-pointer transition-colors"
                        >
                            Encerrar Chat
                        </button>
                    </motion.section>
                )}
            </section>
        </motion.section>
    );
}