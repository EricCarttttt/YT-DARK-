# YTDARK | BY SORENUS15K

```
 ██╗   ██╗████████╗██████╗  █████╗ ██████╗ ██╗  ██╗
 ╚██╗ ██╔╝╚══██╔══╝██╔══██╗██╔══██╗██╔══██╗██║ ██╔╝
  ╚████╔╝    ██║   ██║  ██║███████║██████╔╝█████╔╝ 
   ╚██╔╝     ██║   ██║  ██║██╔══██║██╔══██╗██╔═██╗ 
    ██║       ██║   ██████╔╝██║  ██║██║  ██║██║  ██╗
    ╚═╝       ╚═╝   ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
              B Y   S O R E N U S 1 5 K   v2.0
```

**Ferramenta OSINT local para análise profunda de canais YouTube via YouTube Data API v3.**  
Executa via CLI ou TUI interativa, salva dados em SQLite, exporta para CSV/JSON/XLSX/ODS/Markdown.

---

## Quick Start

```bash
# 1. Build
./gradlew shadowJar
# 2. Configurar API key (NUNCA no código)
export YOUTUBE_API_KEY="AIzaSy..."

# 3. Primeiro scan
java -jar build/libs/ytdark-all.jar scan --target @canal

# 4. TUI interativa
java -jar build/libs/ytdark-all.jar tui
```

---

## Pré-requisitos

| Requisito | Versão | Como obter |
|-----------|--------|------------|
| Java JDK | 17+ | https://adoptium.net |
| YouTube Data API v3 key | - | https://console.cloud.google.com/apis/credentials |
| Gradle (via wrapper) | 8.7 | Incluído no projeto |

---

## Comandos CLI

### `scan` — Analisar canal

```bash
java -jar build/libs/ytdark-all.jar scan \
  --target @canal \                  # ou UCid ou URL completa
  --comments sample \                # off | sample | top_only | sample+top_only
  --max-comments 100 \               # máximo de comentários
  --since 2024-01-01 \               # data inicial YYYY-MM-DD
  --until 2024-12-31 \               # data final YYYY-MM-DD
  --quota-budget 5000 \              # orçamento de cota
  --format csv \                     # formato de export (repetível)
  --format xlsx \
  --output ./reports/ \              # diretório de saída
  --refresh \                        # ignorar cache
  --api-key AIzaSy...                # key (ou use YOUTUBE_API_KEY)
```

**Formatos de --target suportados:**

| Formato | Exemplo |
|---------|---------|
| Handle | `@canal` |
| Channel ID | `UCxxxxxxxxxxxxxxxxxxxxxx` |
| URL com ID | `https://youtube.com/channel/UCxxx` |
| URL com handle | `https://youtube.com/@canal` |
| Username legado | `https://youtube.com/user/nome` |
| ❌ Vanity URL | `/c/nome` — não suportado pela API |

### `tui` — Interface interativa

```bash
java -jar build/libs/ytdark-all.jar tui [--api-key AIzaSy...]
```

### `export` — Exportar dados existentes

```bash
java -jar build/libs/ytdark-all.jar export \
  --format xlsx \
  --output ./relatorio/ \
  --channel-id UCxxxxxx
```

### `status` — Status do sistema

```bash
java -jar build/libs/ytdark-all.jar status
```

### `config` — Gerenciar configuração

```bash
# Criar arquivo de configuração
java -jar build/libs/ytdark-all.jar config init

# Listar configurações
java -jar build/libs/ytdark-all.jar config list

# Definir valor
java -jar build/libs/ytdark-all.jar config set --key defaults.quota_budget --value 10000

# Obter valor
java -jar build/libs/ytdark-all.jar config get --key defaults.quota_budget

# Purgar dados
java -jar build/libs/ytdark-all.jar config purge --channel-id UCxxxxxx
java -jar build/libs/ytdark-all.jar config purge --all
```

---

## Navegação TUI

| Tecla | Ação |
|-------|------|
| `1` | Tela Overview |
| `2` | Tela Vídeos |
| `3` | Tela Breakouts |
| `4` | Tela Monetização |
| `5` | Tela Temas |
| `6` | Tela Comentários |
| `N` | Nova Coleta (instruções) |
| `Q` / `ESC` | Sair |
| `↑↓` | Navegar lista de vídeos |
| `PgUp/PgDn` | Saltar 10 vídeos |
| `S` | Ordenar por coluna |
| `/` | Filtrar por título |
| `Enter` | Detalhe do vídeo |

---

## Formatos de Export

| Formato | Extensão | Notas |
|---------|----------|-------|
| CSV | `.csv` | UTF-8 com BOM (LibreOffice) |
| JSON | `.json` | Hierárquico canal→vídeos→métricas |
| XLSX | `.xlsx` | 5 abas (Apache POI) |
| ODS | `.ods` | 5 abas (OdfToolkit) |
| Markdown | `.md` | Relatório formatado |

Todos os exporters têm **5 seções/abas:** Resumo, Vídeos, Comentários, Links_CTA, Métricas.

---

## Métricas Calculadas

| Métrica | Fórmula |
|---------|---------|
| Views/Dia | `viewCount / max(1, diasDesdePublicação)` |
| Baseline | Mediana de views/dia de todos vídeos elegíveis |
| Delta vs Baseline | `video.viewsPerDay / baseline` |
| Engajamento | `(likes + comentários) / viewCount` |
| Outlier IQR | `viewsPerDay >= P75 + 1.5 × IQR` |
| Outlier Z-Score | `z = (vpd - média) / stdDev >= threshold` |
| CTA Score | Soma ponderada de sinais detectados (0–100) |

**Vídeos excluídos das métricas:** membros-only, indisponíveis, lives ativas.

---

## Arquitetura

```
ytdark/
├── gradlew / gradlew.bat
├── build.gradle.kts
├── src/main/kotlin/com/ytdark/
│   ├── Main.kt                      ← Entry point
│   ├── api/
│   │   ├── ApiKeyManager.kt         ← Gestão segura da API key
│   │   ├── YouTubeApiClient.kt      ← Cliente HTTP YouTube API v3
│   │   ├── RateLimiter.kt           ← Rate limiting + retry + quota
│   │   └── Models.kt                ← DTOs de resposta da API
│   ├── domain/Entities.kt           ← Entidades de domínio
│   ├── persistence/DatabaseManager.kt ← SQLite via Exposed ORM
│   ├── service/
│   │   ├── ScanService.kt           ← Orquestração do pipeline
│   │   ├── MetricsService.kt        ← Cálculo de métricas
│   │   └── LinkExtractor.kt         ← Extração e classificação de links
│   ├── cli/Commands.kt              ← Comandos Clikt
│   ├── tui/                         ← Interface TUI Lanterna
│   ├── export/                      ← CSV, JSON, XLSX, ODS, Markdown
│   └── config/ConfigManager.kt      ← Gestão de configuração YAML
└── src/main/resources/
    ├── logback.xml
    └── db/migration/V1__init.sql    ← Schema via Flyway
```

---

## Segurança da API Key

1. **Nunca hardcodada** — carregada de flag CLI, ENV ou config file
2. **Precedência:** `--api-key` > `YOUTUBE_API_KEY` > `~/.ytdark/config.yaml`
3. **Armazenada como `CharArray`** — nunca como `String`
4. **Zeroizada** após uso com `Arrays.fill(arr, '\u0000')`
5. **Mascarada** em logs: `AIza...XXXX`
6. **Config file** criado com permissões `chmod 600`
7. **URLs de request nunca logadas** (contêm a key no query param)
8. **Erros de API** loggados com hash SHA-256 dos params (sem expor key)

---

## Limitações Técnicas

| Limitação | Descrição |
|-----------|-----------|
| Vanity URLs `/c/` | Não suportadas pela YouTube Data API v3 |
| Subscriber count oculto | Registrado como NULL, nunca estimado |
| Likes desativados | Calculado como NULL, engajamento marcado aproximado |
| `search.list` desabilitado | Custam 100 unidades/request |
| Comentários PII | Apenas texto coletado, sem autor/ID/foto |
| TTL comentários | 7 dias (mais restritivo que metadados) |
| Cota diária | 10.000 unidades padrão (pode ser ampliado no GCP) |

---

## Troubleshooting

### `ApiKeyNotFoundException: YouTube API key not found`

```bash
# Solução:
export YOUTUBE_API_KEY="AIzaSy..."
# ou
java -jar ytdark-all.jar scan --target @canal --api-key AIzaSy...
```

### `VanityUrlNotSupported: URLs /c/ não são suportadas`

```
A YouTube Data API v3 não suporta vanity URLs (/c/).
Solução:
1. Abra o canal no navegador
2. Ctrl+U para ver o código-fonte
3. Busque por 'externalId' — você verá UCxxxxxxxxxx
4. Execute: java -jar ytdark-all.jar scan --target UCxxxxxxxxxx
```

### `quotaExceeded: Cota da API esgotada`

```
A cota de 10.000 unidades diárias foi atingida.
Reset automático às 00:00 Pacific Standard Time (PST).
Dados parciais foram salvos em ~/.ytdark/data.db.
Para verificar: java -jar ytdark-all.jar status
```

---

## Declaração de Compliance

- Todos os dados coletados via **YouTube Data API v3 oficial** (TOS compliant)
- **Sem web scraping** — apenas endpoints oficiais da API
- **Sem coleta de PII:** nome/ID/foto de comentaristas nunca coletados
- Textos de comentários retidos por **máximo 7 dias**
- Metadados de vídeo/canal retidos por **máximo 30 dias**
- Usuário pode purgar todos os dados com `ytdark config purge --all`

---

*YTDARK | BY SORENUS15K v2.0 — Dados via YouTube Data API v3 (oficial)*
