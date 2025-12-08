export async function distribuicaoPesquisaFetch (){
    const URL = 'http://localhost:8080/api/v2/dashboard/csat/distribuicao';
    try{
        const response = await fetch(URL);
        if (!response.ok){
            throw new Error('Erro ao capturar dados');
        }
        const resData = response.json();
        return resData;
    }

    catch (err){
        throw err;
    }

}