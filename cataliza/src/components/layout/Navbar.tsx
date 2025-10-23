import { IoPersonCircleOutline } from "react-icons/io5";
import { NavLink } from "react-router-dom";


export default function NavBar (){
    return (
        <section className="flex flex-row justify-center items-center">
            <section className="justify-around items-center">
                <NavLink to={"/"}>Use nossos serviços</NavLink>
                <NavLink to={"/"}>Inicio</NavLink>
                <section className="flex flex-row ">
                    <NavLink to={"/Login"}>Login</NavLink>
                    <IoPersonCircleOutline size={25}/>
                </section>
            </section>
        </section>
    )
}