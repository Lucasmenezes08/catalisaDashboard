function generateCPF(formatted = true) {
    const digits = Array.from({ length: 9 }, () => Math.floor(Math.random() * 10));

    function calcCheckDigit(baseDigits, weightStart) {
        let sum = 0;
        for (let i = 0; i < baseDigits.length; i++) {
            sum += baseDigits[i] * (weightStart - i);
        }
        const remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    const d1 = calcCheckDigit(digits, 10);
    const d2 = calcCheckDigit([...digits, d1], 11);

    const all = [...digits, d1, d2].join('');
    if (!formatted) return all;

    return `${all.slice(0, 3)}.${all.slice(3, 6)}.${all.slice(6, 9)}-${all.slice(9)}`;
}

async function createAccount() {
    const res = await fetch('https://randomuser.me/api/');
    const data = await res.json();
    const user = data.results[0];

    const firstName = user.name.first;
    const lastName = user.name.last;

    document.getElementById("cpf").value = generateCPF(false);
    document.getElementById("firstName").value = firstName;
    document.getElementById("email").value = `${firstName}.${lastName}@gmail.com`.toLowerCase();
    document.getElementById("password").value = "123";

    const button = document.getElementById("continueButton")
    button.click()
}

createAccount();

// ================================================

async function getData(page = 0) {
    const URL = "http://104.248.217.165:8080/api/v2/pesquisas";
    
    let size = 10
    let sort = "id,DESC"
    let tipoPesquisa = "CSAT"

    const params = new URLSearchParams();
    params.append("page", String(page));
    params.append("size", String(size));
    params.append("sort", sort);
    params.append("tipoPesquisa", tipoPesquisa);

    const response = await fetch(`${URL}?${params.toString()}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })

    if (!response.ok) {
        throw new Error(`Erro na requisição: ${response.status} ${response.statusText}`);
    }

    const data = await response.json()

    const pesquisas = await Promise.all(
        data.content.map(async (pesquisa) => {
            try {
                const userResponse = await fetch(`${URL}/${pesquisa.id}/user`);

                if (userResponse.ok) {
                    const userData = await userResponse.json();
                    return { ...pesquisa, nomeUsuario: userData.username };
                }
            } catch (error) {
                console.warn(`Erro ao buscar user para pesquisa ${pesquisa.id}`, error);
            }
            return { ...pesquisa, nomeUsuario: "Usuário Desconhecido" };
        })
    )
    console.log(data, pesquisas)
}

async function test(){
    for (let i = 0; i < 5; i++) {
        await getData(i)
    }
}

test()