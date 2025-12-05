import { useEffect } from "react";

function SebraeHome() {
  useEffect(() => {
    window.location.href = "/sebrae/home/index.html"; // Caminho do HTML em public/
  }, []);

  return null; // Não renderiza nada
}

export default SebraeHome;
