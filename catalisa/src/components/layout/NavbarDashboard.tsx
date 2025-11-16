import { useNavigate } from 'react-router-dom';
import catalizaLogo from '../../assets/catalizaLogo.png'
import { IoPersonCircleOutline } from "react-icons/io5";
import { Link } from 'react-router-dom';
import {useState} from "react";
import ModalPerfil from "@/components/common/modalPerfil.tsx";
import {useAuth} from "@/store/useAuth.tsx";
import icon from "@/assets/icon.svg";
import catalisa from "@/assets/catalisa.svg";
import person from "@/assets/person.svg";

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
        <section className="bg-[#2020AF] w-full py-2.5 px-8 mb-5 z-50">
            <section className={"w-full flex justify-between items-center px-10"}>
                <img src={icon} />
                <img src={catalisa}/>
                <img src={person}/>
            </section>
        </section>
    )
}