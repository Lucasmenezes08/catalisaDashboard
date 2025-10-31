import {Routes , Route} from 'react-router-dom';
import NavbarRoot from './routers/NavbarRoot';
import Homepage from './routers/Homepage';
import Login from './routers/Login';
import Dashboard from './routers/Dashboard';
import NavbarRootDashboard from './routers/NavbarRootDashboard';
import LayoutConfig from './routers/LayoutConfig';
import PerfilConfig from './components/configuracoes/PerfilConfig';
import EmpresaConfig from './components/configuracoes/EmpresaConfig';
import ProtectedRoute from "@/routers/ProtectedRoute.tsx";
import Test from "@/routers/Test.tsx";


function App() {
  return (
    <Routes>

      <Route path='/' element={<NavbarRoot/>}>
        <Route index element={<Homepage/>} />
        <Route path='/Login' element={<Login/>}/>
      </Route>

        <Route element={<ProtectedRoute/>}>
            <Route path='/dashboard' element={<NavbarRootDashboard/>}>
              <Route index element={<Dashboard/>}></Route>
              <Route path='configuracoes' element={<LayoutConfig/>}>
                <Route index element={<PerfilConfig/>}/>
                <Route path='empresa' element={<EmpresaConfig/>} />
              </Route>
            </Route>
        </Route>

        <Route path={'/teste'} element={<Test/>}/>

      

    </Routes>
  )
 
}

export default App
