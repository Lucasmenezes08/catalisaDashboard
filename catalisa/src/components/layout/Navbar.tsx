import { Link, useLocation } from "react-router-dom";
import icon from "../../assets/icon.svg";
import catalisa from "../../assets/catalisa.svg";
import person from "../../assets/person.svg";
import { useAuth } from "@/store/useAuth.tsx";

export default function NavBar() {
    const { user } = useAuth();
    const location = useLocation();
    const caminho = location.pathname;

    return (
        <section className="fixed bg-[#2020AF] w-full py-2.5 px-8 mb-5 z-50">
            <section className="w-full flex justify-between items-center px-10">
                <div className="flex gap-4 items-center">
                    <Link to="/" className="cursor-pointer">
                        <img src={icon} alt="Icone Home" />
                    </Link>

                    <Link to="/" className="cursor-pointer">
                        <img src={catalisa} alt="Logo Catalisa" />
                    </Link>
                </div>

                {user ? (
                    <Link to="/dashboard" className="cursor-pointer">
                        <img src={person} alt="Perfil" />
                    </Link>
                ) : (

                    caminho === '/login' ? (
                        <Link to="/cadastro" className="text-white border border-white py-1 px-2 rounded-lg transition-colors">
                            Cadastro
                        </Link>
                    ) : (
                        <Link to="/login" className="text-white border border-white py-1 px-2 rounded-lg transition-colors">
                            Login
                        </Link>
                    )
                )}
            </section>
        </section>
    );
}