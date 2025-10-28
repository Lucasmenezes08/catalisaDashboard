import { fetchApiUser } from "@/routers/services/fetchApiUser";
import { useQuery } from "@tanstack/react-query";

export default function useFormLogin (){
    const {data , isLoading , error} = useQuery ({
        queryKey: ['user'],
        queryFn: fetchApiUser,
        staleTime: 1000 * 60 * 60,
    })

    return {data , isLoading , error};
}