import { useState } from "react";
import { FaEye } from "react-icons/fa";
import { FaEyeSlash } from "react-icons/fa";

export default function Login (){

    const [showPassword , setShowPassword] = useState(false);


    function handleShow (){
        setShowPassword(!showPassword);
    }


    return (
        <section className="w-full h-[80vh] flex justify-center items-center">
            <form className="flex flex-col gap-5">
                <section className="flex flex-col justify-start gap-3">
                    <label className="text-sm" htmlFor="user">Usuário</label>
                    <section className="flex flex-row justify-between items-center h-12 w-120 px-5 text-sm text-gray-400 font-light border border-solid border-gray-400 rounded-lg ">
                        <input className="w-full outline-none" type="text" name="user" placeholder="Seu ID" required/>
                        <FaEye className="cursor-pointer" size={18} />
                    </section>
                </section>

                <section className="flex flex-col justify-start gap-3">
                    <label className="text-sm" htmlFor="senha">Senha</label>
                    <section className="flex flex-row justify-between items-center h-12 w-120 px-5 text-sm text-gray-400 font-light border border-solid border-gray-400 rounded-lg ">
                        <input className="w-full outline-none" type={showPassword ? "text" : "password"} name="senha" placeholder="Sua senha" required/>
                        {!showPassword && <FaEye className="cursor-pointer" size={18} onClick={handleShow}/>}
                        {showPassword && <FaEyeSlash className="cursor-pointer" size={18} onClick={handleShow}/>}
                        
                    </section>
                </section>

                <p className="underline font-semibold text-center cursor-pointer">Esqueci a senha</p>

                <button className="flex items-center justify-center w-120 h-12 bg-[#2B1CD3] text-white rounded-xl font-semibold cursor-pointer">Entrar</button>
                
            </form>
        </section>
    )
}