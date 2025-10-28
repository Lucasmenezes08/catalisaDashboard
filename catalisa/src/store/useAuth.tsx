import Login from "@/routers/Login";
import { create} from "zustand";
import { persist} from 'zustand/middleware'



interface User {
    id: string;
    name: string;
    email: string;
}


interface AuthState {
    isAuthenticated: boolean;
    user: User | null;
    token: string | null;
    login: (userData: User , token: string) => void;
    logout: () => void;
}


export const useAuth = create<AuthState>()(
    persist(
        (set) => ({
            isAuthenticated: false,
            user: null,
            token: null,    


            login: (userData , token) => {
                set({
                    isAuthenticated: true,
                    user: userData,
                    token: token,
                })
            },

            logout: () => {
                set({
                    isAuthenticated: false,
                    user: null,
                })
            }
        }),

        {
            name : 'auth-storage',
        }
    )
)