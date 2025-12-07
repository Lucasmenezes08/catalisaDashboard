import { useEffect } from "react";

function SebraeCadastro() {
  useEffect(() => {
    window.location.href = "/sebrae/cadastro/index.html"; // Caminho do HTML em public/
  }, []);

  return null; // Não renderiza nada
}

export default SebraeCadastro;
