import { useAuth } from "@/store/useAuth";
import { useState } from "react";
import { FaEye } from "react-icons/fa";
import { FaEyeSlash } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export default function Login (){
    const navigate = useNavigate();
    const {login} = useAuth();

    const [email , setEmail] = useState('');
    const [password , setPassword] = useState('');
    const [showUser , setShowuUser] = useState(false);
    const [showPassword , setShowPassword] = useState(false);


    function handleShow (){
        setShowPassword(!showPassword);
    }

    function handleShowUser (){
        setShowuUser(!showUser);
    }


    async function handleSubmit (e: React.FormEvent){
        e.preventDefault();

        try {
            const response = await fetch ("http://localhost:8080/api/users" , {
                method: 'POST',
                headers: {
                    'Content-type': 'aplication/json',
                },
                body: JSON.stringify ({email , password}),
            })

            const data = await response.json();


            if (!response.ok){
                throw new Error ("Erro ao capturar dados da api.");
            }

            if (data.email && data.token) {
                login (data.user , data.token);
                navigate ('/dashboard');
            }
            else {
                throw new Error ("Resposta inválida de api");
            }  
        }

        catch (error){
            console.log (error);
        }
        
    }

    

    return (
        <section className="w-full h-[79vh] flex justify-center items-center">
            <form className="w-full max-w-md flex flex-col gap-5 px-5" onSubmit={handleSubmit}>
                <section className="flex flex-col justify-start gap-3 ">
                    <label className="text-sm" htmlFor="user">Usuário</label>
                    <section className="flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ">
                        <input className="w-full outline-none" type={showUser ? "password" : "text"} name="user" placeholder="Seu ID" onChange={(e) => setEmail(e.target.value)} value={email} required/>
                        {!showUser && <FaEye className="cursor-pointer" size={18} onClick={handleShowUser}/>}
                        {showUser && <FaEyeSlash className="cursor-pointer" size={18} onClick={handleShowUser}/>}
                    </section>
                </section>

                <section className="flex flex-col justify-start gap-3">
                    <label className="text-sm" htmlFor="senha">Senha</label>
                    <section className="flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ">
                        <input className="w-full outline-none" type={showPassword ? "text" : "password"} name="senha" placeholder="Sua senha" onChange={(e) => setPassword(e.target.value)} value={password} required/>
                        {!showPassword && <FaEye className="cursor-pointer" size={18} onClick={handleShow}/>}
                        {showPassword && <FaEyeSlash className="cursor-pointer" size={18} onClick={handleShow}/>}
                        
                    </section>
                </section>

                <p className="underline font-semibold text-center cursor-pointer">Esqueci a senha</p>

                <button className="flex items-center justify-center w-full h-12 bg-[#2B1CD3] text-white rounded-xl font-semibold cursor-pointer">Entrar</button>
                
            </form>
        </section>
    )
}