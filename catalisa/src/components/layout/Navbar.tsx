import {Link} from "react-router-dom";
import icon from "../../assets/icon.svg"
import catalisa from "../../assets/catalisa.svg"
import person from "../../assets/person.svg"

export default function NavBar (){

    return (
        <section className="fixed bg-[#2020AF] w-full py-2.5 px-8 mb-5 z-50">
            <section className={"w-full flex justify-between items-center px-10"}>
                <Link to={"/dashboard"} className={"cursor-pointer"}>
                    <img src={icon} />
                </Link>

                <Link to={"/dashboard"} className={"cursor-pointer"}>
                    <img src={catalisa}/>
                </Link>

                <Link to={"/dashboard"} className={"cursor-pointer"}>
                    <img src={person}/>
                </Link>

            </section>
        </section>
    )
}