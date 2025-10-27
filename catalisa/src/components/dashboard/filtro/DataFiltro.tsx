import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { useState } from "react"
import { ChevronDownIcon } from "lucide-react"

import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"



export default function DataFiltro (){

    const [open, setOpen] = useState(false)
    const [date, setDate] = useState<Date | undefined>(undefined)

    return (
      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger className="w-55 py-8 text-left border border-black cursor-pointer bg-gray-200 hover:bg-gray-200" asChild>  
          <Button
            variant="outline"
            id="date"
            className="w-48 justify-between font-normal"
          >
            <div className="flex flex-col">
              <span className="text-xs font-bold uppercase text-black mb-0.5">
                DATA
              </span>
              <span className="text-sm text-slate-800">
                {date ? date.toLocaleDateString() : "Selecione uma data"}
              </span>
            </div>

            <ChevronDownIcon />
          </Button>

        </PopoverTrigger>
        <PopoverContent className="w-auto overflow-hidden p-0" align="start">
          <Calendar
            mode="single"
            selected={date}
            captionLayout="dropdown"
            onSelect={(date) => {
              setDate(date)
              setOpen(false)
            }}
          />
        </PopoverContent>
      </Popover>
    )
}