import HeaderHomepage from "@/components/homepage/HeaderHomepage.tsx";
import ContentHomepage from "@/components/homepage/ContentHomepage.tsx";

export default function Homepage (){
    return (
        <section className={"w-full flex flex-col bg-white scroll-smooth"}>
            <HeaderHomepage/>
            <ContentHomepage/>
        </section>
    )
}