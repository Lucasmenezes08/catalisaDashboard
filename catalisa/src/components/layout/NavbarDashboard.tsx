import { useNavigate } from 'react-router-dom';
import catalizaLogo from '../../assets/catalizaLogo.png'
import { IoPersonCircleOutline } from "react-icons/io5";
import { Link } from 'react-router-dom';
import { useAuth } from '@/store/useAuth';

export default function NavbarDashboard (){

    const navigate = useNavigate();
    const {logout} = useAuth();

    function navegarInicio (){
        navigate('/');
    }

    return (
        <nav className="flex items-center justify-between bg-gray-200 w-full py-2 px-6">
            <img onClick={navegarInicio} className='w-20 h-5 cursor-pointer' src={catalizaLogo}/>
            <section className='flex flex-row items-center gap-5'>
                <button onClick={logout} className='font-semibold cursor-pointer'>Logout</button>
                <IoPersonCircleOutline size={35}/>
            </section>
        </nav>
    )
}