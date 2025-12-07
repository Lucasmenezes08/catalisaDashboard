import { useAuth } from "@/store/useAuth";
import { useState } from "react";
import { FaEye } from "react-icons/fa";
import { FaEyeSlash } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export default function Sebraelogin (){
    const navigate = useNavigate();
    const { login } = useAuth();

    const [email , setEmail] = useState('');
    const [password , setPassword] = useState('');
    const [showUser , setShowUser] = useState(true);
    const [showPassword , setShowPassword] = useState(false);
    const [errorMessage , setErrorMessage] = useState('');


    function handleShow (){
        setShowPassword(!showPassword);
    }

    function handleShowUser (){
        setShowUser(!showUser);
    }

    async function handleSubmit (e: React.FormEvent){
        e.preventDefault();

        try {
            const loginResponse = await fetch ("http://localhost:8080/api/v2/users/login" , {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                },
                body: JSON.stringify ({email, password}),
            });

            const loginData = await loginResponse.json();

            if (!loginResponse.ok){
                throw new Error (loginData.message || "Credenciais inválidas ou usuário não encontrado.");
            }

          
            if (loginData.authenticated) {
                const userResponse = await fetch(`http://localhost:8080/api/v2/users?email=${encodeURIComponent(email)}`);

                if (!userResponse.ok) {
                    throw new Error("Erro ao buscar dados do usuário após o login.");
                }

                const userDataPage = await userResponse.json(); 
                if (userDataPage.content && userDataPage.content.length > 0) {
                    const user = userDataPage.content[0];
                    login(user);
                    navigate('/dashboard');

                } else {
                    throw new Error("Usuário autenticado, mas não encontrado na base de dados.");
                }

            } else {
                throw new Error ("Falha na autenticação.");
            }
        }
        catch (error){
            console.log (error);
            setErrorMessage((error as Error).message);
        }
    }

    return (
        <section className="w-full h-[95vh] flex justify-center items-center">
            <form className="w-full max-w-md flex flex-col gap-5 px-5" onSubmit={handleSubmit}>
                <section className="flex flex-col justify-start gap-3 ">
                    <label className="text-sm" htmlFor="user">Email</label>
                    <section className={`flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ${errorMessage ? "border border-red-500" : ''}`}>
                        <input className="w-full outline-none" type={showUser ? "text" : "password"} name="user" placeholder="Seu Email" onChange={(e) => setEmail(e.target.value)} value={email} required/>
                        {!showUser && <FaEye className="cursor-pointer" size={18} onClick={handleShowUser}/>}
                        {showUser && <FaEyeSlash className="cursor-pointer" size={18} onClick={handleShowUser}/>}
                    </section>
                    {errorMessage && (<p className={"text-red-800 text-sm"}>{errorMessage}</p>)}
                </section>

                <section className="flex flex-col justify-start gap-3">
                    <label className="text-sm" htmlFor="senha">Senha</label>
                    <section className={`flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ${errorMessage ? "border border-red-500" : ''}`}>
                        <input className={"w-full outline-none"} type={showPassword ? "text" : "password"} name="senha" placeholder="Sua senha" onChange={(e) => setPassword(e.target.value)} value={password} required/>
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