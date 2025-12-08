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
    const [openModal ,setOpenModal] = useState(false);

    function handleModal () {
        setOpenModal(!openModal);
    }


    return (
        <section className="bg-[#2020AF] w-full py-2.5 px-8 mb-5 z-50">
            <section className={"w-full flex justify-between items-center px-10"}>
                <Link to={"/dashboard"} className={"cursor-pointer"}>
                    <img src={icon} />
                </Link>


                <Link to={"/dashboard"} className={"cursor-pointer"}>
                    <img src={catalisa}/>
                </Link>

                <img className={"cursor-pointer"} src={person} onClick={handleModal}/>
            </section>
            {openModal && <ModalPerfil nome={user?.username} onclick={handleModal}/>}
        </section>
    )
}