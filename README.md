# SkyLog — App Motorista Preventivo

O **SkyLog** é um aplicativo Android desenvolvido em Kotlin e Jetpack Compose com foco em logística e segurança nas estradas. Ele consome dados climáticos e meteorológicos em tempo real a partir de dados espaciais fornecidos pela NASA, servindo como uma central preventiva de alertas de riscos naturais (como queimadas, inundações, deslizamentos de terra e tempestades) para motoristas de frotas logísticas.

---

## 🌍 Tema da Global Solution
**Prevenção de Desastres Naturais e Resiliência Climática na Logística Terrestre.**
Com o avanço das mudanças climáticas, eventos extremos nas rodovias brasileiras e globais são cada vez mais frequentes. O SkyLog mitiga prejuízos de frotas e protege vidas de motoristas, processando dados espaciais (satélites da NASA) e indicando desvios e rotas alternativas seguras antes que o caminhoneiro chegue à área de perigo.

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

## 📐 Explicação da Arquitetura
O projeto segue rigorosamente os conceitos de **Clean Architecture**, dividindo responsabilidades em camadas desacopladas e testáveis:

1. **Camada `domain` (Domínio):** Contém os modelos puros de negócio (`Alerta`, `NivelRisco`), os contratos dos repositórios (`AlertaRepository`, `PreferencesRepository`) e os Casos de Uso (`GetAlertasUseCase` para buscar e filtrar por criticidade de perfil, e `FiltrarAlertasUseCase` para buscas e filtros por tags). Esta camada não depende de nenhuma biblioteca Android ou de terceiros.
2. **Camada `data` (Dados):** Implementa os contratos de domínio. Contém as chamadas de rede da API Retrofit (`NasaEonetApi`), os modelos de transferência de dados (`EventoDto`, `EonetResponse`) e as implementações de repositórios (`AlertaRepositoryImpl` e `PreferencesRepositoryImpl` para persistência em SharedPreferences).
3. **Camada `presentation` (Apresentação):** Contém os componentes visuais reutilizáveis (`AlertCard`, `RiskBadge`, `MetricCard`, `TopBar`), a máquina de rotas de navegação (`AppNavigation`, `NavRoutes`), os ViewModels para cada tela contendo a lógica de estado usando `StateFlow` e `UiState` (Initial, Loading, Success, Error) e as Telas Compose (`SplashScreen`, `OnboardingScreen`, `HomeScreen`, `AlertasScreen`, `DetalheAlertaScreen`, `RotaAlternativaScreen`, `PerfilScreen`).
4. **Camada `di` (Injeção de Dependência):** Módulos Koin (`appModule`) injetando as classes da API, Repositórios, UseCases e ViewModels para desacoplamento completo.

---

## 📱 Fluxo de Telas
1. **Splash Screen:** Carrega a logo animada por 2 segundos. Verifica via `SharedPreferences` se o motorista já visualizou a introdução.
   * Onboarding não concluído ➔ Navega para **Onboarding**.
   * Onboarding concluído ➔ Navega para **Home**.
2. **Onboarding Screen:** Pager com 3 slides instrutivos (O Problema, A Solução e Como Usar). Botões de avançar, voltar e pular. Ao finalizar, grava a conclusão no dispositivo.
3. **Home Screen (Dashboard):** Apresenta o nome do motorista, a empresa dele, cards de métricas rápidas (alertas ativos, alertas críticos, rotas seguras) e destaca o alerta mais urgente da API na região, permitindo clicar para traçar rota. Lista também as 5 ocorrências mais recentes.
4. **Lista de Alertas (Alertas):** Listagem geral de todos os alertas ativos da NASA. Contém uma barra de busca reativa em tempo real e chips seletores de filtro rápido por tipo (Queimada, Tempestade, Enchente, Deslizamento, Vulcão) e uma aba especial de Favoritados.
5. **Detalhe do Alerta:** Detalha o evento selecionado, exibindo a fonte, data de detecção, coordenadas geográficas, descrição técnica e radar térmico da NASA. Se o alerta for de nível **ALTO** ou **CRÍTICO**, habilita o botão para calcular rota alternativa.
6. **Rota Alternativa:** Tela ilustrativa que apresenta um comparativo em tempo real de quilometragem e tempo entre a rota original bloqueada e a nova rota sugerida livre de perigos climáticos, emitindo uma notificação visual ao confirmar o desvio.
7. **Perfil do Motorista:** Permite configurar o nome do motorista, a empresa de logística, o limiar mínimo de alerta (BAIXO, MÉDIO, ALTO, CRÍTICO) que filtra a API globalmente, alternar o **Modo Escuro (Dark Mode)** persistido localmente e botão de logout para reiniciar o app.

---

## 🔌 API Utilizada
Consome a **NASA EONET API** (Earth Observatory Natural Event Tracker):
* **Base URL:** `https://eonet.gsfc.nasa.gov/api/v3`
* **Endpoint:** `GET /events?status=open&days=30&limit=50`
* O app realiza o mapeamento dinâmico de categorias da NASA para termos amigáveis de rodovias (`wildfires` ➔ Queimada, `severeStorms` ➔ Tempestade, etc.) e calcula os níveis de criticidade dos alertas.

---

## 👥 Integrantes do Grupo
* **Luiz Fabiano Nascimento Vale da Silva** - RM 553529 (FIAP - 3SIZ)
* **Lucas Fontes Peruzin** - RM 552877
* **Fernando Youngbin Kang** - RM 553499

---

## 🎥 Link do Pitch de Apresentação
* 🔗 **[Assista ao Pitch do Projeto SkyLog no YouTube](https://youtu.be/oWIz45x7whg)**
