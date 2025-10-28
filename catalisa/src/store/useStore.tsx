import { create } from 'zustand'



type SelectorState = {
    selector : 'dashboard' | 'insight';
    setSelector : (newSelector:  'dashboard' | 'insight') => void;
}


export const useSelector = create<SelectorState>()(set => ({
    selector: 'dashboard',
    setSelector: (newSelector : 'dashboard' | 'insight') => set({selector : newSelector}),
}))


