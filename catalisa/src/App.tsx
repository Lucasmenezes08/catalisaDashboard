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
import NotFound from '../src/components/common/NotFound.tsx';
import Pesquisa from "@/routers/Pesquisa.tsx";
import TestConsumo from "@/routers/TestConsumo.tsx";
import CadastroProduto from "@/routers/TestProduct.tsx";
import Cadastro from "@/components/telaCadastro/Cadastro.tsx";
import SebraeHome from './routers/sebrae/SebraeHome.tsx';
import Sebraelogin from './routers/sebrae/SebraeLogin.tsx';
import SebraeCursos from './routers/sebrae/SebraeCursos.tsx';
import SebraeCursoMarketing from './routers/sebrae/SebraeCursoMarketing.tsx';
import SebraeCursoFinancas from './routers/sebrae/SebraeCursoFinancas.tsx';


function App() {
  return (
    <Routes>

      <Route path='/' element={<NavbarRoot/>}>
          <Route index element={<Homepage/>} />
          <Route path='/Login' element={<Login/>}/>
          <Route path={'/cadastro'} element={<Cadastro/>}/>
      </Route>

        <Route element={<ProtectedRoute/>}>
            <Route path='/dashboard' element={<NavbarRootDashboard/>}>
              <Route index element={<Dashboard/>}></Route>
              <Route path='configuracoes' element={<LayoutConfig/>}>
                <Route index element={<PerfilConfig/>}/>
                <Route path='empresa' element={<EmpresaConfig/>} />
              </Route>
            </Route>

          
          <Route path={"/sebrae/cursos"} element={<SebraeCursos/>}/>
          <Route path={"/sebrae/cursos/marketing"} element={<SebraeCursoMarketing/>}/>
          <Route path={"/sebrae/cursos/financas"} element={<SebraeCursoFinancas/>}/>

        </Route>

        <Route path={"/sebrae"} element={<SebraeHome/>}/>
        <Route path={"/sebrae/login"} element={<Sebraelogin/>}/>

        <Route path='*' element={<NotFound/>}/>
        <Route path={"/pesquisa"} element={<Pesquisa/>}/>
        <Route path={"/testeconsumo"} element={<TestConsumo/>}/>
        <Route path={"/testeproduto"} element={<CadastroProduto/>}/>

    </Routes>
  )
 
}

export default App
