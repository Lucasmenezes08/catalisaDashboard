import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';

interface ModalDesistenciaProps {
    onCancel: () => void;
}

const modalVariants = {
    hidden: { opacity: 0, scale: 0.8 },
    visible: { opacity: 1, scale: 1, transition: { type: "spring", duration: 0.5 } },
    exit: { opacity: 0, scale: 0.8 }
};

export default function ModalDesistencia({ onCancel }: ModalDesistenciaProps) {
    const navigate = useNavigate();

    const handleConfirmExit = () => {
        navigate('/sebrae');
    };

    return (
        <motion.div
            className="fixed inset-0 z-[60] flex items-center justify-center bg-black/60 backdrop-blur-sm"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
        >
            <motion.div
                className="bg-white w-[320px] rounded-[30px] p-6 flex flex-col items-center shadow-2xl relative"
                variants={modalVariants}
                initial="hidden"
                animate="visible"
                exit="exit"
                onClick={(e) => e.stopPropagation()} // Impede que clicar neste modal feche ele mesmo
            >
                <div className="w-full flex items-center justify-between mb-4">
                    <h2 className="text-2xl font-bold text-black flex items-center gap-2">
                        Já vai? 🥺
                    </h2>
                </div>

                <p className="text-black font-bold text-lg leading-tight text-left w-full mb-8">
                    Sua opinião é muito importante, caso queira responder a nossa pesquisa <br/>
                    isso irá influênciar as próximas experiencias conosco.
                </p>
                <div className="flex gap-4 w-full justify-between">
                    <button
                        onClick={handleConfirmExit}
                        className="flex-1 bg-[#362F9D] hover:bg-[#2c2685] text-white font-bold py-3 px-4 rounded-full flex items-center justify-center gap-2 transition-transform active:scale-95"
                    >
                        Sim! 😣
                    </button>

                    <button
                        onClick={onCancel}
                        className="flex-1 bg-[#2D2A55] hover:bg-[#232142] text-white font-bold py-3 px-4 rounded-full flex items-center justify-center gap-2 transition-transform active:scale-95"
                    >
                        Não! 😊
                    </button>
                </div>
            </motion.div>
        </motion.div>
    );
}