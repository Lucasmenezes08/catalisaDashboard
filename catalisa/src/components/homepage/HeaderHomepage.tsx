import setaDiagonal from "../../assets/seta_diagonal.svg";
import setCima from "../../assets/seta_cima.svg";
import { useState } from "react";
import { Link } from 'react-router-dom';
import { GoArrowUpRight } from "react-icons/go";


export default function HeaderHomepage (){

    const [isClicked, setIsClicked] = useState(false);

    function handleClick (){
        setIsClicked(!isClicked);
    }

    return (
        <header className={"h-screen relative w-full flex justify-start pl-55 items-center"}>
            <section className={"flex flex-col gap-3"}>
                <p className={"text-5xl font-extralight text-black"}><span className={"font-bold"}>Dê luz</span> aos seus dados <br/></p>
                <p className={"text-5xl font-extralight text-black"}><span className={"font-bold"}>Catalise</span> a melhor estratégia <br/></p>
                <section
                    className={"relative flex flex-row items-end gap-2"}>
                    <p className={"text-5xl font-extralight text-black"}><span className={"font-bold"}>e Aplique</span> no seu negócio</p>
                    <section className={`absolute inline-flex justify-center items-center top-10 -translate-y-1 left-full bg-[#0000FF] 
                    w-4 h-4 rounded-full transition-all duration-300 ease-in-out
                    cursor-pointer transform
                    ${isClicked ? 'w-25 h-25 px-4 text-white rounded-full' : 'w-4 h-4 ml-4'}
                    ${isClicked ? 'text-lg' : 'text-base'}
                    `}
                             onClick={handleClick}
                    >
                        {
                            isClicked &&
                            <section className={"flex flex-row justify-center items-center text-white transition-all duration-300 ease-in-out px-5"}>
                                <Link className={"text-sm cursor-pointer font-light"} to={"/login"}>Entre</Link>
                                <GoArrowUpRight size={30} className={"font-bold"} />
                            </section>
                        }



                    </section>
                </section>

                <section className={"flex flex-row gap-7 mt-5"}>
                    <section className={"flex flex-row w-42 justify-center items-center border border-black rounded-lg gap-1 cursor-pointer"}>
                        <Link to={"/teste"} className={"pl-5"}>Cadastre-se</Link>
                        <img className={"w-12"} src={setaDiagonal}/>
                    </section>

                    <a href={"#content"} className={"flex flex-row w-42 justify-center items-center border border-black rounded-lg gap-3 cursor-pointer"}>
                        <p>Quem somos</p>
                        <img className={"w-4"} src={setCima} />
                    </a>
                </section>
            </section>

        </header>
    )
}