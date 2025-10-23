import { Outlet } from "react-router-dom"
import NavBar from "../components/layout/Navbar"

export default function NavbarRoot (){
    return (
        <section className="font-sans antialiased">
            <NavBar/>
            <Outlet/>
        </section>
    )
}