export async function fetchApiUser (){
    try {
        const res = await fetch("http://localhost:8080/api/v2/users");
        const data = await res.json();

        if (!data.ok){
            throw new Error ("Falha ao capturar dados do login");
        }

        return data;
    }
    catch (error){
        throw error;
    }
}
   
