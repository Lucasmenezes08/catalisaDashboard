from playwright.sync_api import sync_playwright
from urllib.parse import urlparse, urljoin, unquote
import os
import requests
from bs4 import BeautifulSoup
import re

URL = "https://sebrae.com.br/sites/PortalSebrae/cursosonline/gestao-financeira,e059d743ad3a8910VgnVCM1000001b00320aRCRD"
OUTPUT_DIR = "cursoFinancas"
os.makedirs(OUTPUT_DIR, exist_ok=True)

downloaded = set()

# ------------------- Funções auxiliares -------------------


def nome_arquivo(url):
    """Gera nome seguro, único e decodificado."""
    parsed = urlparse(url)
    nome = os.path.basename(parsed.path)
    nome = nome.split("?")[0]  # remove query string
    nome = unquote(nome)  # decodifica %20, %C3%BA, etc.

    if not nome:
        nome = parsed.path.strip("/").replace("/", "_") or "arquivo"

    if nome in downloaded:
        base, ext = os.path.splitext(nome)
        i = 1
        while f"{base}_{i}{ext}" in downloaded:
            i += 1
        nome = f"{base}_{i}{ext}"

    return nome


def salvar_arquivo(url, pasta):
    """Baixa e salva o arquivo no diretório local."""
    try:
        nome = nome_arquivo(url)
        caminho = os.path.join(pasta, nome)
        if nome in downloaded:
            return nome
        r = requests.get(url, timeout=20)
        if r.status_code == 200 and r.content:
            with open(caminho, "wb") as f:
                f.write(r.content)
            downloaded.add(nome)
            print("✅", nome)
            return nome
        else:
            print("⚠️ Erro HTTP", r.status_code, "→", url)
    except Exception as e:
        print("⚠️ Falha em", url, "→", e)
    return None


def processar_css(caminho_css):
    """Baixa imagens e fontes referenciadas dentro do CSS e ajusta URLs."""
    with open(caminho_css, "r", encoding="utf-8") as f:
        css_text = f.read()

    urls = re.findall(r"url\(['\"]?(.*?)['\"]?\)", css_text)
    for u in urls:
        if u.startswith("data:"):
            continue
        full_url = urljoin(URL, u)
        nome = salvar_arquivo(full_url, OUTPUT_DIR)
        if nome:
            css_text = css_text.replace(u, nome)

    # Processa @import
    imports = re.findall(r'@import\s+[\'"](.*?)[\'"]', css_text)
    for imp in imports:
        full_url = urljoin(URL, imp)
        nome = salvar_arquivo(full_url, OUTPUT_DIR)
        if nome:
            css_text = css_text.replace(imp, nome)

    with open(caminho_css, "w", encoding="utf-8") as f:
        f.write(css_text)


# ------------------- Renderiza a página -------------------

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    page = browser.new_page()

    recursos = set()

    def on_response(response):
        url = response.url
        if any(
            ext in url
            for ext in [
                ".css",
                ".js",
                ".png",
                ".jpg",
                ".jpeg",
                ".svg",
                ".gif",
                ".webp",
                ".woff",
                ".woff2",
                ".ttf",
                ".otf",
            ]
        ):
            if url.startswith("http"):
                recursos.add(url)

    page.on("response", on_response)

    print("🌐 Carregando página...")
    page.goto(URL, wait_until="networkidle")

    html = page.content()
    html_path = os.path.join(OUTPUT_DIR, "index.html")
    with open(html_path, "w", encoding="utf-8") as f:
        f.write(html)
    print("💾 HTML renderizado salvo.")

    browser.close()

# ------------------- Analisa HTML para recursos faltantes -------------------

print("🔍 Coletando recursos do HTML...")
soup = BeautifulSoup(html, "html.parser")

for tag in soup.find_all(["link", "script", "img"]):
    attr = "href" if tag.name == "link" else "src"
    if tag.has_attr(attr):
        link = tag[attr]
        if any(
            ext in link
            for ext in [
                ".css",
                ".js",
                ".png",
                ".jpg",
                ".jpeg",
                ".svg",
                ".gif",
                ".webp",
                ".woff",
                ".woff2",
                ".ttf",
                ".otf",
            ]
        ):
            full_url = urljoin(URL, link)
            recursos.add(full_url)

# ------------------- Baixa todos os arquivos -------------------

mapeamento = {}
print("⬇️ Baixando recursos...")
for url in sorted(recursos):
    nome = salvar_arquivo(url, OUTPUT_DIR)
    if nome:
        mapeamento[url] = nome

print("🎉 Tudo pronto! Verifique a pasta:", OUTPUT_DIR)
print("💡 Abra via servidor local: python -m http.server 8080")
