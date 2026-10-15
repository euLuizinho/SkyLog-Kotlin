# 🚛 SkyLog Motorista — App Android Nativo

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![NASA EONET](https://img.shields.io/badge/NASA%20EONET-E03C31?style=for-the-badge&logo=nasa&logoColor=white)](https://eonet.gsfc.nasa.gov/)
[![Material 3](https://img.shields.io/badge/Material%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-FF6B4A?style=for-the-badge)](./ARCHITECTURE.md)

> **Módulo Mobile do ecossistema SkyLog.** Aplicativo Android nativo projetado para motoristas de transporte rodoviário de cargas receberem alertas geoespaciais em tempo real e desviarem de desastres naturais nas rodovias.  
> Desenvolvido e validado na **Global Solution (FIAP 2026.1 — Tema: Space Connect)**, alcançando **notas 80+ em todas as disciplinas avaliadas**.

---

## 🧭 Visão de Produto & O Desafio

No setor de transporte rodoviário de cargas, desastres naturais e eventos climáticos extremos (queimadas, enchentes, deslizamentos e tempestades severas) geram prejuízos milionários e colocam em risco a vida dos caminhoneiros.

O ecossistema **SkyLog** resolve esse problema unindo dados orbitais de satélite à operação rodoviária através de duas frentes complementares:

1. **SkyLog Motorista (Android / Kotlin Nativo — Este Repositório):** Desenvolvido com foco na usabilidade em cabine sob condições adversas. Consome dados geoespaciais abertos em tempo real diretamente da API EONET da NASA, categoriza riscos na pista e sugere rotas de escape antes que o veículo chegue ao ponto de bloqueio.
2. **SkyLog Gestor (Web / Flutter):** Painel central voltado para operadores de tráfego monitorarem toda a malha rodoviária e gerenciarem limiares de criticidade.  
   🔗 **Repositório do Gestor:** [https://github.com/euLuizinho/SkyLog-Flutter](https://github.com/euLuizinho/SkyLog-Flutter)

---

## 🛠️ Tecnologias Utilizadas

* **Kotlin** & **Jetpack Compose** (Interface moderna, 100% declarativa e reativa)
* **Koin** (Injeção de dependências nativa e leve)
* **Retrofit & OkHttp** (Comunicação com API REST e interceptadores de rede)
* **Kotlinx Serialization** (Desserialização tipada de alto desempenho)
* **StateFlow & Coroutines** (Gerenciamento de estado reativo e assíncrono em background)
* **SharedPreferences** (Persistência local de preferências e limiar de alertas)
* **Material 3** (Design System responsivo com suporte completo a Dark Mode)

---

## 🏗️ Explicação da Arquitetura (Clean Architecture)

O projeto adota rigorosamente os conceitos de **Clean Architecture**, com separação estrita de camadas e inversão de dependência:

1. **Camada `domain` (Domínio):** Contém os modelos puros de negócio (`Alerta`, `NivelRisco`), os contratos dos repositórios (`AlertaRepository`, `PreferencesRepository`) e os Casos de Uso (`GetAlertasUseCase`, `FiltrarAlertasUseCase`). Esta camada não depende de bibliotecas Android externas.
2. **Camada `data` (Dados):** Implementa os contratos de domínio. Gerencia as chamadas à API Retrofit (`NasaEonetApi`), os DTOs (`EventoDto`, `EonetResponse`) e os repositórios com mapeamento de dados.
3. **Camada `presentation` (Apresentação):** Contém os componentes visuais reutilizáveis (`AlertCard`, `RiskBadge`, `MetricCard`), a navegação declarativa (`AppNavigation`), os ViewModels com gerenciamento de estado via `StateFlow` e as telas Compose.
4. **Camada `di` (Injeção de Dependência):** Módulos Koin (`appModule`) orquestrando a injeção desacoplada de rede, repositórios, use cases e viewmodels.

Para diagramas detalhados de sequência e registros de decisões técnicas (ADRs), consulte o [ARCHITECTURE.md](./ARCHITECTURE.md).

---

## 📱 Fluxo de Telas

1. **Splash Screen:** Inicialização rápida com carregamento da logo e verificação de primeiro acesso.
2. **Onboarding Screen:** Introdução visual explicando a proposta de valor, sem barreiras de login para acesso imediato.
3. **Home Screen (Dashboard):** KPIs de rota (alertas ativos, críticos, rotas seguras) e destaque do alerta mais severo no trajeto.
4. **Lista de Alertas:** Listagem completa com busca em tempo real e filtros rápidos por tipo de desastre (Queimada, Tempestade, Enchente, Deslizamento).
5. **Detalhe do Alerta:** Visualização completa da ocorrência, coordenadas de satélite, fonte oficial e botão de cálculo de desvio quando o risco é Crítico ou Alto.
6. **Rota Alternativa:** Comparativo visual de quilometragem e tempo estimado entre a rota bloqueada e a nova rota recomendada.
7. **Perfil do Motorista:** Configuração de limiar de risco (Baixo, Médio, Alto, Crítico) e alternância instantânea entre Tema Claro e Modo Escuro.

---

## 📡 API Científica Utilizada (Open Data / Sem Custos)

O aplicativo consome dados em tempo real da **NASA EONET API** (Earth Observatory Natural Event Tracker), provando a viabilidade de utilizar dados espaciais abertos no setor logístico privado:
* **Base URL:** `https://eonet.gsfc.nasa.gov/api/v3`
* **Endpoint:** `GET /events?status=open&days=30&limit=50`
* **Mapeamento:** `wildfires` → Queimada · `severeStorms` → Tempestade · `floods` → Enchente · `landslides` → Deslizamento.

---

## 👥 Equipe e Reconhecimento Acadêmico

Projeto desenvolvido para a **Global Solution 2026.1** da **FIAP** (Faculdade de Informática e Administração Paulista — Tema: Space Connect):
* **Luiz Fabiano** — Product Management, Arquitetura de Software & Desenvolvimento Kotlin/Flutter
* **Lucas Fontes** — Engenharia de Software & Integração de Dados
* **Fernando Kang** — Negócios, Pesquisa & Apresentação

🏆 **Resultados Obtidos:**
* **Notas 80+** em todas as disciplinas avaliadas (Engenharia de Software, Mobile, Cloud e Gestão de TI)
