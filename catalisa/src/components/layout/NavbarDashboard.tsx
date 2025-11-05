import { useNavigate } from 'react-router-dom';
import catalizaLogo from '../../assets/catalizaLogo.png'
import { IoPersonCircleOutline } from "react-icons/io5";
import { Link } from 'react-router-dom';
import {useState} from "react";
import ModalPerfil from "@/components/common/modalPerfil.tsx";
import {useAuth} from "@/store/useAuth.tsx";

export default function NavbarDashboard (){

    const {user} = useAuth()
    const navigate = useNavigate();
    const [openModal ,setOpenModal] = useState(false);

    function handleModal () {
        setOpenModal(!openModal);
    }

    function navegarInicio (){
        navigate('/');
    }

    return (
        <nav className="flex items-center justify-between bg-gray-200 w-full py-2 px-6">
            <img onClick={navegarInicio} className='w-20 h-5 cursor-pointer' src={catalizaLogo}/>
            <section className='flex flex-row items-center gap-5'>
                <Link to={'/dashboard'}>Meu quadro</Link>
                <section className="relative">
                    <button className="cursor-pointer" onClick={handleModal}>
                        <IoPersonCircleOutline size={35}/>
                    </button>
                    {openModal && <ModalPerfil nome={user?.username} onclick={handleModal} /> }
                </section>

            </section>
        </nav>
    )
}