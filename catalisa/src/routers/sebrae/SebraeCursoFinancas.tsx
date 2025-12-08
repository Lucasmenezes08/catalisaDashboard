import { useEffect } from "react";

function SebraeCursoFinancas() {
  useEffect(() => {
    window.location.href = "/sebrae/cursoFinancas/index.html"; // Caminho do HTML em public/
  }, []);

  return null; // Não renderiza nada
}

export default SebraeCursoFinancas;