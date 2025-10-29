import {Routes , Route} from 'react-router-dom';
import NavbarRoot from './routers/NavbarRoot';
import Homepage from './routers/Homepage';
import Login from './routers/Login';
import Dashboard from './routers/Dashboard';
import NavbarRootDashboard from './routers/NavbarRootDashboard';
import LayoutConfig from './routers/LayoutConfig';
import PerfilConfig from './components/configuracoes/PerfilConfig';
import EmpresaConfig from './components/configuracoes/EmpresaConfig';


function App() {
  return (
    <Routes>

      <Route path='/' element={<NavbarRoot/>}>
        <Route index element={<Homepage/>} />
        <Route path='/Login' element={<Login/>}/>
      </Route>

        <Route path='/dashboard' element={<NavbarRootDashboard/>}>
          <Route index element={<Dashboard/>}></Route>
          <Route path='/dashboard/configuracoes' element={<LayoutConfig/>}>
            <Route index element={<PerfilConfig/>}/>
            <Route path='/dashboard/configuracoes/empresa' element={<EmpresaConfig/>} /> 
          </Route>
        </Route>
      
      

    </Routes>
  )
 
}

export default App
