import { Link } from "react-router-dom";

export default function NotFound (){
    return (
        <section className="relative w-full h-screen bg-sky-700">
            <Link to={"/"} className="absolute px-5 py-4">Pagina inicial</Link>
            <h1 className="absolute top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%] text-4xl text-white/90">Pagina não encontrada - 404</h1>
        </section>
    )
}