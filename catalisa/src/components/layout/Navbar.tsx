import { IoPersonCircleOutline } from "react-icons/io5";
import { NavLink } from "react-router-dom";
import catalisaLogo from '../../assets/catalizaLogo.png'
import { useNavigate } from "react-router-dom";


export default function NavBar (){

    const navigate = useNavigate();
    function navegarInicio (){
        navigate('/');
    }

    return (
        <section className="bg-gray-200 w-full py-2 px-6 mb-5">
            <section className="flex flex-row justify-between items-center">
                <img onClick={navegarInicio} className='w-20 h-5 cursor-pointer' src={catalisaLogo}/>
                <section className="flex flex-row justify-center items-center gap-2 ">
                    <NavLink to={"/Login"} className="text-[#0F0FA6]">Login</NavLink>
                    <IoPersonCircleOutline size={35}/>
                </section>
            </section>
        </section>
    )
}