import { Outlet } from "react-router-dom";
import NavbarDashboard from "../components/layout/NavbarDashboard";


export default function NavbarRootDashboard (){
    return (
        <nav>
            <NavbarDashboard/>
            <Outlet/>
        </nav>
    )
}