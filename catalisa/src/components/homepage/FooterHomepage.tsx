import iconeFooter from "../../assets/iconeFooter.svg"
import iconeLinkedin from "../../assets/div.svg"

export default function FooterHomepage() {
    return (
        <section className="w-full flex flex-col items-center justify-center bg-black text-white py-12 gap-y-8">
            <section className="w-full max-w-7xl flex flex-1 flex-row items-start justify-between px-12">
                <img src={iconeFooter} alt="Icone do footer" className="w-24" />
                
                <section className="flex flex-row items-start justify-center gap-x-32">
                    <section className="flex flex-col items-start gap-y-4">
                        <p className="text-gray-400 text-sm mb-2">Navegação</p>
                        <a href="#inicio">Inicio</a>
                        <a href="#content">Sobre nós</a>
                        <a href="#pilares">Pilares</a>
                    </section>
                    
                    <section className="flex flex-col items-start gap-y-4">
                        <p className="text-gray-400 text-sm mb-2">Projeto</p>
                        <p className="text-white">Sobre nós</p>
                        <p className="text-white">Fale conosco</p>
                    </section>
                </section>
            </section>
            
            <section className="w-full max-w-7xl flex flex-1 flex-row items-center justify-between px-12 border-t border-gray-800 pt-8">
                <section className="flex flex-row items-center gap-x-8 text-sm text-gray-400">
                    <p>© 2025 Bem vindos.</p>
                    <p>Política de Privacidade</p>
                    <p>Termos de Serviço</p>
                </section>
                
                <img src={iconeLinkedin} alt="LinkedIn" className="w-5 h-5" />
            </section>
        </section>
    )
}