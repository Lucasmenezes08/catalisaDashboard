import { useEffect } from "react";

function SebraeLogin() {
  useEffect(() => {
    window.location.href = "/sebrae/login/index.html"; // Caminho do HTML em public/
  }, []);

  return null; // Não renderiza nada
}

export default SebraeLogin;
