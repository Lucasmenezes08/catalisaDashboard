import { create } from "zustand";
import { persist } from 'zustand/middleware'

interface UserData {
    id: number;
    email: string;
    username: string | null;
}

interface AuthState {
    isAuthenticated: boolean;
    user: UserData | null;
    login: (userData: UserData) => void;
    logout: () => void;
}

export const useAuth = create<AuthState>()(
    persist(
        (set) => ({
            isAuthenticated: false,
            user: null,
            login: (userData) => {
                set({
                    isAuthenticated: true,
                    user: userData,
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
            name: 'auth-storage',
        }
    )
)