import { Outlet } from "react-router-dom"
import NavBar from "../components/layout/Navbar"

export default function NavbarRoot (){
    return (
        <section>
            <NavBar/>
            <Outlet/>
        </section>
    )
}