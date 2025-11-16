export default function ExplicacaoModal ({mensagem}:any){
    return (
        <section className="w-full flex items-center justify-center bg-white text-sm rounded-2xl border border-black px-1 py-3 transition ease-in-out delay-200 z-100">
            <h1 className="text-justify break-after-all w-[80%]">{mensagem}</h1>
        </section>
    )
}