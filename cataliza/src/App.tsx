import {Routes , Route} from 'react-router-dom';
import NavbarRoot from './routers/NavbarRoot';
import Homepage from './routers/Homepage';
import Login from './routers/Login';

function App() {
  return (
    <Routes>
      <Route path='/' element={<NavbarRoot/>}>
        <Route index element={<Homepage/>} />
        <Route path='/Login' element={<Login/>}/>
      </Route>
    </Routes>
  )
 
}

export default App
