import { IoPersonCircleOutline } from "react-icons/io5";
import { NavLink } from "react-router-dom";


export default function NavBar (){
    return (
        <section className="bg-gray-200 w-full py-4 px-4 mb-5">
            <section className="flex flex-row justify-between items-center">
                <NavLink to={"/"}>Use nossos serviços</NavLink>
                <NavLink to={"/"}>Inicio</NavLink>
                <section className="flex flex-row justify-center items-center gap-2 ">
                    <NavLink to={"/Login"} className="text-[#0F0FA6]">Login</NavLink>
                    <IoPersonCircleOutline size={35}/>
                </section>
            </section>
        </section>
    )
}