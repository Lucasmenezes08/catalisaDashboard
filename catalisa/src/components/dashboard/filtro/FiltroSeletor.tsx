import BtnDashboard from "@/components/common/BtnDashboard";
import BtnInsights from "@/components/common/BtnInsights";
import { useSelector } from "@/store/useStore";

export default function FiltroSeletor (){

    const {selector , setSelector} = useSelector();

    return (
        <section className="flex-flex-row h-full border border-solid border-black rounded-lg">
            <BtnDashboard  isClick={() => setSelector('dashboard')} isActive={selector === 'dashboard'} />
            <BtnInsights isClick={() => setSelector('insight')} isActive={selector === 'insight'} />
        </section>
    )
    
}