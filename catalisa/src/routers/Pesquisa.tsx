import backgroundSebrae from "../assets/backgroundSebrae.png";
import PesquisaBox from "@/components/sebrae/PesquisaBox.tsx";
import {useEffect, useState} from "react";
import {Spinner} from "@/components/common/animation-spinner.tsx";

export default function Pesquisa(){
    const [isLoading , setIsLoading] = useState(true);
    const [isBoxOpen , setIsBoxOpen] = useState(false);


    function handleCloseModal (){
        setIsBoxOpen(false);
    }

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsLoading(false);
            setIsBoxOpen(true);
        }, 5000);

        return () => clearTimeout(timer);
    }, []);

    return (
        <section
            className={"relative w-full h-screen bg-cover"}
            style={{ backgroundImage: `url(${backgroundSebrae})`}}
        >

            {isLoading && <Spinner />}
            {!isLoading && isBoxOpen && (
                <PesquisaBox/>
            )}

        </section>
    )
}