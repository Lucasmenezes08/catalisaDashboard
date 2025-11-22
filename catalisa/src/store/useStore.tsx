import { create } from 'zustand';

type SelectorState = {
    selector: 'dashboard' | 'insight';
    setSelector: (newSelector: 'dashboard' | 'insight') => void;

    tipoPesquisa: string;
    setTipoPesquisa: (newTipo: string) => void;
}

export const useSelector = create<SelectorState>()((set) => ({
    selector: 'dashboard',
    setSelector: (newSelector) => set({ selector: newSelector }),

    tipoPesquisa: 'CSAT',
    setTipoPesquisa: (newTipo) => set({ tipoPesquisa: newTipo }),
}));