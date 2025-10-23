import { Link } from 'react-router-dom'
import catalizaLogo from '../assets/catalizaLogo.png'


export default function Homepage (){
    return (
        <section className='w-full h-[70vh] flex flex-col justify-center items-center gap-25'>
            <section className='h-40 flex flex-col'>
                <img className='w-105 h-22' src={catalizaLogo}/>
                <p className='text-xs text-right pt-2 font-bold'>Consultoria e soluções em tecnologia e análise de dados</p>
            </section>

            <section className='flex flex-col justify-center items-center'>
                <p className='text-sm font-semibold'>Já é cliente?</p>
                <Link to={"/Login"} className='text-[#0F0FA6] text-sm'>Login</Link>
            </section>
        </section>
    )
}