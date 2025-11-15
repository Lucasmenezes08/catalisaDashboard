import { NavLink } from "react-router-dom";
import icon from "../../assets/icon.svg"
import catalisa from "../../assets/catalisa.svg"
import person from "../../assets/person.svg"

import { useNavigate } from "react-router-dom";


export default function NavBar (){

    const navigate = useNavigate();
    function navegarInicio (){
        navigate('/');
    }

    return (
        <section className="bg-[#2020AF] w-full py-2.5 px-8 mb-5">
            <section className={"w-full flex justify-between items-center px-10"}>
                <img src={icon} />
                <img src={catalisa}/>
                <img src={person}/>
            </section>
        </section>
    )
}