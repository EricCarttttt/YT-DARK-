# YTDARK BY SORENUS15K

> **Ferramenta OSINT YouTube — Análise de Canais, Métricas e Ideação de Conteúdo**

```
██╗   ██╗████████╗██████╗  █████╗ ██████╗ ██╗  ██╗
╚██╗ ██╔╝╚══██╔══╝██╔══██╗██╔══██╗██╔══██╗██║ ██╔╝
 ╚████╔╝    ██║   ██║  ██║███████║██████╔╝█████╔╝ 
  ╚██╔╝     ██║   ██║  ██║██╔══██║██╔══██╗██╔═██╗ 
   ██║      ██║   ██████╔╝██║  ██║██║  ██║██║  ██╗
   ╚═╝      ╚═╝   ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
                                       BY SORENUS15K
```

[![Python 3.10+](https://img.shields.io/badge/Python-3.10%2B-blue?style=flat-square&logo=python)](https://www.python.org/)
[![YouTube Data API v3](https://img.shields.io/badge/YouTube%20Data%20API-v3-red?style=flat-square&logo=youtube)](https://developers.google.com/youtube/v3)
[![SQLite](https://img.shields.io/badge/Storage-SQLite-lightblue?style=flat-square&logo=sqlite)](https://sqlite.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)
[![Compliance: YouTube ToS](https://img.shields.io/badge/Compliance-YouTube%20ToS-orange?style=flat-square)](https://developers.google.com/youtube/terms/api-services-terms-of-service)

---

## O que é o YTDARK?

YTDARK é uma ferramenta **OSINT focada em YouTube**, executada **100% localmente no terminal** (CLI e TUI interativo), que analisa canais públicos usando exclusivamente a **YouTube Data API v3 oficial**.

Ela foi projetada para **pesquisadores de nicho, criadores de conteúdo e analistas** que precisam entender o que está performando em um canal — sem depender de scraping, sem violar políticas da plataforma e sem comprometer a privacidade de ninguém.

> **"Dark channel"** é definido exclusivamente como estilo de produção: vídeos sem rosto do criador (narrados, animados, compilados, screen recordings). A ferramenta não incentiva, facilita ou sugere qualquer prática ilegal.

---

## Funcionalidades

### 📊 Análise de Performance
- Velocidade de vídeos em **views por dia** com baseline do canal (mediana)
- **Delta vs. baseline**: saiba quais vídeos estão acima ou abaixo do típico
- **Detecção de outliers** por IQR ou Z-Score (configurável)
- **Crescimento tardio** (late bloomers) — detectado via histórico de runs

### 🔗 Radar de Monetização
- Extração e classificação de links de descrição: afiliado, hub, produto próprio, social, comunidade
- **CTA Score** (0–100) com scoring explicável por sinal
- Inferência do modelo de monetização do canal por heurísticas

### 💡 Ideação de Conteúdo
- Detecção de **padrões de título** por regex (Como X em Y, Erro que Z comete, etc.)
- **Clustering semântico** de temas (heurística no MVP; embeddings locais na v1)
- 10 sugestões de vídeo baseadas em dados — sem copiar conteúdo de ninguém

### 💬 Análise de Comentários (Opcional)
- Amostragem de texto bruto — **zero PII coletada**
- Perguntas recorrentes, dores, pedidos de tema e sentimento agregado
- Modos: `off`, `sample`, `top_only`, `sample+top_only`

### 🖥️ Interface
- **TUI interativo** com 6 telas navegáveis (Overview, Vídeos, Breakouts, Monetização, Temas, Comentários)
- **CLI headless** para automação e scripts
- Exports: Markdown, HTML, CSV, SQLite, JSON

---

## Instalação

```bash
# Clone o repositório
git clone https://github.com/sorenus15k/ytdark.git
cd ytdark

# Crie um ambiente virtual (recomendado)
python -m venv .venv
source .venv/bin/activate  # Linux/macOS
# .venv\Scripts\activate   # Windows

# Instale as dependências
pip install -r requirements.txt
```

### Requisitos
- Python 3.10+
- Linux, macOS ou Windows (WSL2 recomendado; PowerShell suportado)
- Chave de acesso à **YouTube Data API v3** (gratuita — Google Cloud Console)

---

## Configuração da API Key

**Nunca** inclua sua API key no código ou em arquivos commitados.

```bash
# Recomendado: variável de ambiente
export YOUTUBE_API_KEY="sua_chave_aqui"

# Ou configure no arquivo (não commitar):
cp config.example.yaml ~/.sorenus15k/config.yaml
# Edite e adicione sua key no campo api.key
```

> Cada usuário utiliza sua própria chave de API e é responsável pelo uso dentro das cotas e políticas do YouTube Developer.

---

## Uso Rápido

```bash
# Análise básica de um canal
ytdark analyze https://www.youtube.com/@handle \
  --since 2024-01-01 \
  --until 2024-12-31

# Com coleta de comentários e export completo
ytdark analyze https://www.youtube.com/@handle \
  --since 2024-06-01 --until 2024-12-31 \
  --comments sample --max-comments 100 \
  --export md,html,csv,sqlite \
  --quota-budget 5000

# Comparar até 5 canais (v1)
ytdark compare @canal1 @canal2 @canal3 \
  --since 2024-01-01 --until 2024-12-31

# Modo headless (sem TUI, útil para automação)
ytdark analyze @handle --since 2024-01-01 --until 2024-12-31 --no-tui --yes
```

---

## Flags Principais

| Flag | Descrição | Padrão |
|---|---|---|
| `--since DATE` | Data de início (YYYY-MM-DD) | Obrigatório |
| `--until DATE` | Data de fim (YYYY-MM-DD) | Obrigatório |
| `--comments MODE` | `off`, `sample`, `top_only`, `sample+top_only` | `off` |
| `--max-comments N` | Máximo de comentários por vídeo | `50` |
| `--quota-budget N` | Orçamento de cota em unidades | `5000` |
| `--export FORMATS` | `md,html,csv,sqlite,json` | `md,sqlite` |
| `--resolver MODE` | `auto`, `handle_only`, `strict_id` | `auto` |
| `--language LANG` | `pt`, `en`, `any` | `any` |
| `--outlier-method` | `iqr` ou `zscore` | `iqr` |
| `--refresh` | Ignorar cache e re-coletar tudo | — |
| `--purge-cache` | Apagar cache do canal informado | — |
| `--no-tui` | Modo headless (CLI puro) | — |
| `--yes` | Confirmar estimativa de cota automaticamente | — |

Ver `ytdark --help` para a lista completa de flags.

---

## Arquivo de Configuração

```yaml
# ~/.sorenus15k/config.yaml

defaults:
  quota_budget: 5000
  comments_mode: "off"
  max_comments: 50
  ttl_days: 30
  export_formats: ["md", "sqlite"]
  output_dir: "./reports/"
  language: "any"
  resolver_mode: "auto"
  log_level: "info"

api:
  key: ""  # Use YOUTUBE_API_KEY env var (nunca commitar a key aqui)
```

---

## Custo de Cota da API

A ferramenta é **quota-aware** e evita chamadas caras por padrão:

| Operação | Endpoint | Custo |
|---|---|---|
| Resolver canal | `channels.list` | 1 unidade |
| Listar vídeos (por página) | `playlistItems.list` | 1 unidade |
| Enriquecer vídeos (lote de 50) | `videos.list` | 1 unidade |
| Coletar comentários (por página) | `commentThreads.list` | 1 unidade |
| Busca geral (**evitar**) | `search.list` | **100 unidades** |

**Exemplos de custo real:**
- Canal com 50 vídeos, sem comentários: ~3 unidades
- Canal com 200 vídeos, sem comentários: ~9 unidades
- Canal com 200 vídeos + comentários (50/vídeo): ~209 unidades

O limite padrão da Google é **10.000 unidades/dia** por projeto. A ferramenta nunca ultrapassa o `--quota-budget` sem aviso prévio.

---

## Painel TUI — 6 Telas

```
╔══════════════════════════════════════════════════════════════════════╗
║  YTDARK BY SORENUS15K  │  Canal: @handle  │  Cota: 312/5000        ║
║  Intervalo: 2024-01-01 → 2024-12-31                                 ║
╠══════════════════════════════════════════════════════════════════════╣
║  [A]Overview [B]Vídeos [C]Breakouts [D]Monetiz. [E]Temas [F]Coment ║
╠══════════╦══════════════════════════╦═════╦════════╦═══════╦════════╣
║ Data     ║ Título                   ║Tipo ║ Views  ║ V/Dia ║ CTA    ║
╠══════════╬══════════════════════════╬═════╬════════╬═══════╬════════╣
║2024-10-15║ Como Ganhar X em Y dias  ║LONG ║284.350 ║ 3.124 ║  88    ║
║2024-10-08║ Antes de Comprar Qualq...║LONG ║198.420 ║ 1.984 ║  72    ║
║2024-10-01║ Erro #1 que Iniciantes...║LONG ║102.100 ║   890 ║  45    ║
╠══════════╩══════════════════════════╩═════╩════════╩═══════╩════════╣
║  3 de 47 vídeos │ Baseline: 710 V/Dia │ [P]Anterior [N]Próxima     ║
╚══════════════════════════════════════════════════════════════════════╝
```

| Tela | Conteúdo |
|---|---|
| **A** Overview | Métricas do canal, baseline, top 5, distribuição de tipos |
| **B** Vídeos | Tabela ordenável/filtrável com todas as métricas |
| **C** Breakouts | Outliers e vídeos com crescimento tardio |
| **D** Monetização | Mapa de domínios, tipos de link, CTA Score, modelo inferido |
| **E** Temas | Clusters semânticos, padrões de título, velocidade por padrão |
| **F** Comentários | Perguntas, dores, pedidos — anonimizado, sem PII |

---

## Compliance e Privacidade

YTDARK foi desenhado com **compliance como requisito não-negociável**:

| ✅ O que fazemos | ❌ O que NÃO fazemos |
|---|---|
| Usar exclusivamente a YouTube Data API v3 oficial | Scraping de HTML ou endpoints não-oficiais |
| Coletar apenas metadados públicos | Armazenar PII de comentaristas (username, foto, ID) |
| Cache local com TTL de 30 dias (metadados) | Bypass de cotas com múltiplas API keys |
| Deletar textos de comentários em 7 dias | Acessar vídeos de membros ou dados privados |
| Exibir aviso de privacidade antes de qualquer coleta | Executar ações no YouTube em nome do usuário |
| Processar NLP 100% localmente | Enviar dados para APIs externas de IA/NLP |

**Fundamentos legais:** YouTube ToS §4 e §5B, YouTube Developer Policies §II e §III, LGPD (Art. 5), GDPR (Art. 4).

### Gerenciar seus dados locais

```bash
# Ver onde os dados estão armazenados
# Padrão: ~/.sorenus15k/data.db

# Apagar cache de um canal específico
ytdark --purge-cache https://www.youtube.com/@handle

# Apagar TUDO (banco completo)
ytdark --purge-cache --all
```

---

## Limitações Técnicas Conhecidas

A ferramenta declara explicitamente o que **não é possível** via API oficial:

- ❌ **Comentário fixado (pinned)** — não identificável via API v3
- ❌ **Métricas históricas por dia** — não disponível; usa snapshots comparados entre runs
- ❌ **Receita / monetização real** — inferida por heurísticas de CTA e links
- ❌ **Impressões e CTR da thumbnail** — disponível apenas no YouTube Studio (privado)
- ❌ **Watch time e taxa de retenção** — disponível apenas no YouTube Studio (privado)
- ⚠️ **Classificação de Shorts** — heurística por duração (≤ 60s); sem campo direto na API v3
- ⚠️ **Vanity URLs (/c/nome)** — solicitar channelId ou handle equivalente ao usuário

---

## Roadmap

| Fase | Status | Principais entregas |
|---|---|---|
| **MVP** | 🔨 Em desenvolvimento | Pipeline completo, TUI Telas A+B, exports MD/CSV/SQLite, compliance |
| **v1** | 📋 Planejado | Módulo comentários completo, Telas C–F, compare de canais, padrões de título, HTML+JSON |
| **v2** | 💡 Futuro | Embeddings locais (sentence-transformers), agendamento cron, plugin system, dashboard HTML standalone |

---

## Estrutura do Projeto

```
ytdark/
├── ytdark/
│   ├── api/          # Camada de abstração da YouTube Data API v3
│   ├── pipeline/     # Resolução, listagem, enriquecimento, comentários
│   ├── metrics/      # Views/dia, engajamento, baseline, outliers
│   ├── cta/          # Extração de links, CTA Score, monetização
│   ├── themes/       # Padrões de título, clustering semântico
│   ├── db/           # Schema SQLite, cache, TTL
│   ├── tui/          # Painel TUI (6 telas)
│   ├── cli/          # Comandos e flags
│   └── exports/      # MD, HTML, CSV, SQLite, JSON
├── tests/
├── config.example.yaml
├── requirements.txt
└── README.md
```

---

## Contribuindo

1. Fork o repositório
2. Crie uma branch: `git checkout -b feature/minha-feature`
3. Commit suas alterações: `git commit -m 'feat: descrição da feature'`
4. Push: `git push origin feature/minha-feature`
5. Abra um Pull Request

**Antes de contribuir, leia:**
- As regras de compliance desta ferramenta são inegociáveis
- Nenhum PR que adicione scraping, coleta de PII ou bypass de API será aceito
- Mantenha a assinatura `YTDARK BY SORENUS15K` em todos os outputs e mocks

---

## Licença

MIT License — veja [LICENSE](LICENSE) para detalhes.

---

## Aviso Legal

Esta ferramenta é fornecida para fins de pesquisa e análise de dados públicos. O uso é de responsabilidade exclusiva do usuário, que deve respeitar os [Termos de Serviço do YouTube](https://www.youtube.com/static?template=terms), a [Política de Desenvolvedores da YouTube Data API](https://developers.google.com/youtube/terms/api-services-terms-of-service), a LGPD e o GDPR.

---

<div align="center">

**YTDARK BY SORENUS15K**

*Dados coletados exclusivamente via YouTube Data API v3 (oficial)*

</div>
