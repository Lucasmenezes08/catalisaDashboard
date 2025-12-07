import HeaderHomepage from "@/components/homepage/HeaderHomepage.tsx";
import ContentHomepage from "@/components/homepage/ContentHomepage.tsx";
import MainHomepage from "@/components/homepage/MainHomepage.tsx";
import PilaresHomepage from "@/components/homepage/PilaresHomepage.tsx";
import FooterHomepage from "@/components/homepage/FooterHomepage";

export default function Homepage (){
    return (
        <section className={"w-full flex flex-col bg-white scroll-smooth"}>
            <HeaderHomepage/>
            <ContentHomepage/>
            <MainHomepage/>
            <PilaresHomepage/>
            <FooterHomepage/>
        </section>
    )
}