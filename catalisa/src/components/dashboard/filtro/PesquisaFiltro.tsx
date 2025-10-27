import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

export default function PesquisaFiltro() {
  return (
    <Select defaultValue="fidelidade">

      <SelectTrigger className="w-55 py-8 text-left border border-black cursor-pointer">

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
          <SelectItem value="fidelidade">Fidelidade</SelectItem>
          <SelectItem value="preco">Preço</SelectItem>
          <SelectItem value="popularidade">Popularidade</SelectItem>
          <SelectItem value="data">Data</SelectItem>
        </SelectGroup>
      </SelectContent>
    </Select>
  );
}