import { useEffect } from "react";

function SebraeCursosOnline() {
  useEffect(() => {
    window.location.href = "/sebrae/cursos/index.html"; // Caminho do HTML em public/
  }, []);

  return null; // Não renderiza nada
}

export default SebraeCursosOnline;
