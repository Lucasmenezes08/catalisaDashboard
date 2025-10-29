import { CiCircleInfo } from "react-icons/ci";


export default function Comentario(){
    return (
        <section className="w-full h-full flex flex-col rounded-xl border border-solid border-black">
            <section className="relative flex justify-center items-center p-4 border-b border-black">
                <h2 className="text-md font-medium text-black uppercase">Comentários</h2>
                <CiCircleInfo className="absolute right-4 text-blue-700" size={25}/>
            </section>

            <section className="flex-1 p-4 min-h-0">

            </section>
        </section>
    )
}