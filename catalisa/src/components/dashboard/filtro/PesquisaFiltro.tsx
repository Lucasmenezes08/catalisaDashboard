import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {useSelector} from "@/store/useStore.tsx";


export default function PesquisaFiltro() {

    const { tipoPesquisa, setTipoPesquisa } = useSelector();

    return (
    <Select defaultValue="CSAT"  value={tipoPesquisa} onValueChange={setTipoPesquisa}>

      <SelectTrigger className="w-55 py-7 text-left border border-black cursor-pointer">

        <div className="flex flex-col">
          <span className="text-xs font-bold                                                                                                                                                                                                                                                                 uppercase text-black mb-0.5">
            PESQUISA
          </span>
          
          <SelectValue className="text-base font-medium text-slate-800" />
        
        </div>
      </SelectTrigger>
      
      <SelectContent>
        <SelectGroup>
          <SelectLabel>Pesquisar por</SelectLabel>
          <SelectItem value="CSAT">Satisfação</SelectItem>
          <SelectItem value={'NPS'}>Fidelidade</SelectItem>
        </SelectGroup>
      </SelectContent>
    </Select>
  );
}