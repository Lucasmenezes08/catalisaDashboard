import {useState} from "react";

export default function Test (){

    const [email , setEmail] = useState('');
    const [password , setPassword] = useState('');
    const [username , setUsername] = useState('');
    const [cpfCnpj , setCpfCnpj] = useState("");

    async function handleSubmit (e:React.FormEvent){
        e.preventDefault();

        const url = "http://localhost:8080/api/v1/users";

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
        }

        catch (error){
            throw  error;
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <input type="text" placeholder="email" value={email} onChange={(e) => setEmail(e.target.value)} required/>
            <input type="text" placeholder="cpf" value={cpfCnpj} onChange={(e) => setCpfCnpj(e.target.value)} required/>
            <input type="text" placeholder="username" value={username} onChange={(e) => setUsername(e.target.value)} required/>
            <input type="text" placeholder="password" value={password} onChange={(e) => setPassword(e.target.value)} required/>

            <button type={"submit"}>submit</button>
        </form>


    )
}