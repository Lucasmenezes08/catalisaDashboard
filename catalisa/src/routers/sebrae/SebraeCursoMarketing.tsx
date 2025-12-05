import { useEffect } from "react";

function SebraeCursosMarketing() {
  useEffect(() => {
    window.location.href = "/sebrae/cursoMarketing/index.html"; // Caminho do HTML em public/
  }, []);

  return null; // Não renderiza nada
}

export default SebraeCursosMarketing;
