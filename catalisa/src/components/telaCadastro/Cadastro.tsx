import {useState} from "react";
import {useNavigate} from "react-router-dom";

export default function Cadastro (){

    const [email , setEmail] = useState('');
    const [password , setPassword] = useState('');
    const [username , setUsername] = useState('');
    const [cpfCnpj , setCpfCnpj] = useState("");
    const [errorMessage , setErrorMessage] = useState('');
    const navigate = useNavigate();

    async function handleSubmit (e:React.FormEvent){
        e.preventDefault();

        const url = "http://localhost:8080/api/v2/users";

        try {
            const response = await fetch(url , {
                method : 'POST',
                headers : {
                    "content-type" : 'application/json',
                },
                body : JSON.stringify({email , cpfCnpj , username , password })
            })

            if (!response.ok){
                throw new Error ('Erro ao enviar dados.');
            }

            const data = await response.json();
            console.log (data);

            setEmail('');
            setPassword('');
            setUsername('');
            setCpfCnpj('');
            navigate('/login');
        }

        catch (error){
            setErrorMessage((error as Error).message);
        }
    }

    return (
        <section className="w-full min-h-screen flex justify-center items-center pt-15">
            <form onSubmit={handleSubmit} className="w-full max-w-md flex flex-col gap-5 px-5">
                <section className="flex flex-col justify-start gap-3 ">
                    <label className="text-sm" htmlFor="email">Email</label>
                    <section className={`flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ${errorMessage ? "border border-red-500" : ''}`}>
                        <input className="flex-1 outline-none" type="text" name={"email"} placeholder="email" value={email} onChange={(e) => setEmail(e.target.value)} required/>
                    </section>
                    {errorMessage && (<p className={"text-red-800 text-sm"}>{errorMessage}</p>)}

                </section>

                <section className="flex flex-col justify-start gap-3 ">
                    <label className="text-sm" htmlFor="cpfcnpj">Cpf/Cnpj</label>
                    <section className={`flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ${errorMessage ? "border border-red-500" : ''}`}>
                        <input className="flex-1 outline-none" type="text" name={"cpfcnpj"} placeholder="Cpf/Cnpj" value={cpfCnpj} onChange={(e) => setCpfCnpj(e.target.value)} required/>
                    </section>

                </section>

                <section className="flex flex-col justify-start gap-3 ">
                    <label className="text-sm" htmlFor="user">Usuário</label>
                    <section className={`flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ${errorMessage ? "border border-red-500" : ''}`}>
                        <input className="flex-1 outline-none" type="text" name={"user"} placeholder="Nome de usuário" value={username} onChange={(e) => setUsername(e.target.value)} required/>
                    </section>
                </section>

                <section className="flex flex-col justify-start gap-3 ">
                    <label className="text-sm" htmlFor="senha">Senha</label>
                    <section className={`flex flex-row justify-between items-center h-12 w-full text-sm px-4 text-gray-400 font-light border border-solid border-gray-400 rounded-lg ${errorMessage ? "border border-red-500" : ''}`}>
                        <input className="flex-1 outline-none" type="text" name={"senha"} placeholder="Senha" value={password} onChange={(e) => setPassword(e.target.value)} required/>
                    </section>
                </section>

                <button className="flex items-center justify-center w-full h-12 bg-[#2B1CD3] text-white rounded-xl font-semibold cursor-pointer" type={"submit"}>submit</button>
            </form>
        </section>



    )
}