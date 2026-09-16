# 🚛 SkyLog Motorista — App Android (MVP Acadêmico)

⚠️ **Status do Projeto: Prova de Conceito (PoC)**
O **SkyLog Motorista** é um aplicativo Android desenvolvido em Kotlin e Jetpack Compose. Construído originalmente como o Produto Mínimo Viável (MVP) para o **Global Solution 2026.1 (FIAP)**, o objetivo deste projeto é validar a viabilidade técnica de uma central preventiva de alertas rodoviários. 

Apesar de sua origem acadêmica, a base de código foi estruturada com rigor técnico para demonstrar domínio prático sobre Clean Architecture, consumo de APIs reais e boas práticas de mercado, não se tratando (ainda) de um produto comercial, mas sim de uma demonstração de arquitetura escalável.

---

## 🌍 O Desafio (Tema da Global Solution)
**Prevenção de Desastres Naturais e Resiliência Climática na Logística Terrestre.**
Com o avanço das mudanças climáticas, eventos extremos nas rodovias brasileiras e globais são cada vez mais frequentes. O SkyLog foi idealizado para mitigar prejuízos de frotas e proteger vidas de motoristas, processando dados espaciais abertos (satélites da NASA) e indicando desvios e rotas alternativas seguras antes que o caminhoneiro chegue à área de perigo.

---

## 🛠️ Tecnologias Utilizadas
* **Kotlin** & **Jetpack Compose** (Layouts modernos e declarativos)
* **Koin** (Injeção de dependências leve e nativa para Kotlin)
* **Retrofit & OkHttp** (Consumo de APIs REST com interceptador de logs)
* **Kotlinx Serialization** (Desserialização JSON de alto desempenho)
* **StateFlow & Coroutines** (Arquitetura reativa e assíncrona)
* **SharedPreferences** (Persistência local de preferências e favoritos)
* **Material 3** (Design System premium e responsivo com suporte a Dark Mode)

---

## 🏗️ Explicação da Arquitetura (Clean Architecture)
O projeto segue rigorosamente os conceitos de **Clean Architecture**, dividindo responsabilidades em camadas desacopladas e testáveis:

1. **Camada `domain` (Domínio):** Contém os modelos puros de negócio (`Alerta`, `NivelRisco`), os contratos dos repositórios (`AlertaRepository`, `PreferencesRepository`) e os Casos de Uso (`GetAlertasUseCase`, `FiltrarAlertasUseCase`). Esta camada não depende de nenhuma biblioteca Android ou de terceiros.
2. **Camada `data` (Dados):** Implementa os contratos de domínio. Contém as chamadas de rede da API Retrofit (`NasaEonetApi`), os modelos de transferência de dados (`EventoDto`, `EonetResponse`) e as implementações de repositórios.
3. **Camada `presentation` (Apresentação):** Contém os componentes visuais reutilizáveis (`AlertCard`, `RiskBadge`, `MetricCard`), a máquina de rotas de navegação (`AppNavigation`), os ViewModels para cada tela contendo a lógica de estado reativo (`UiState` com `StateFlow`) e as Telas Compose.
4. **Camada `di` (Injeção de Dependência):** Módulos Koin (`appModule`) injetando as classes da API, Repositórios, UseCases e ViewModels para desacoplamento completo.

---

## 📱 Fluxo de Telas
1. **Splash Screen:** Carrega a logo animada. Verifica se o motorista já visualizou a introdução.
2. **Onboarding Screen:** Slides instrutivos (O Problema, A Solução e Como Usar). Fluxo fluido que elimina barreiras de login para imediata visualização de valor (foco do MVP).
3. **Home Screen (Dashboard):** Cards de métricas rápidas (alertas ativos, alertas críticos, rotas seguras) e destaca o alerta mais urgente da API na região.
4. **Lista de Alertas:** Listagem geral de todos os alertas ativos da NASA. Contém busca em tempo real e filtros rápidos por tipo (Queimada, Tempestade, Enchente, Deslizamento).
5. **Detalhe do Alerta:** Detalha o evento selecionado, exibindo a fonte, data de detecção, e coordenadas geográficas da NASA. Habilita cálculo de rota alternativa em caso de perigo.
6. **Rota Alternativa:** Tela ilustrativa que apresenta um comparativo em tempo real de quilometragem e tempo entre a rota original bloqueada e a nova rota sugerida livre de perigos climáticos.
7. **Perfil do Motorista:** Permite configurar o limiar mínimo de alerta (BAIXO, MÉDIO, ALTO, CRÍTICO) que filtra a API globalmente, e alternar o Modo Escuro.

---

## 📡 API Científica Utilizada (Sem Custos / Open Data)
Diferente de sistemas amarrados a APIs pagas, este MVP consome diretamente a **NASA EONET API** (Earth Observatory Natural Event Tracker), provando a capacidade de usar dados governamentais abertos para o setor privado.
* **Base URL:** `https://eonet.gsfc.nasa.gov/api/v3`
* **Endpoint:** `GET /events?status=open&days=30&limit=50`
* O app realiza o mapeamento dinâmico de categorias da NASA para termos amigáveis de rodovias (`wildfires` -> Queimada, `severeStorms` -> Tempestade) e calcula os níveis de criticidade dos alertas baseados na proximidade/severidade.

---

## 👥 Integrantes do Grupo (FIAP - 3SIZ)
* **Luiz Fabiano Nascimento Vale da Silva** - RM 553529 
* **Lucas Fontes Peruzin** - RM 552877
* **Fernando Youngbin Kang** - RM 553499

---
