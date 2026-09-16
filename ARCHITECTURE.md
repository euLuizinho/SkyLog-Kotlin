# 🏗️ Documento de Arquitetura — SkyLog Motorista (Android)

> **Projeto:** SkyLog Motorista | **Tipo:** MVP Acadêmico (Global Solution FIAP 2026.1)
> **Stack:** Kotlin · Jetpack Compose · Clean Architecture · MVVM

Este documento descreve as decisões arquiteturais e o design técnico do aplicativo SkyLog Motorista. O objetivo é registrar formalmente o *porquê* de cada escolha tecnológica, servindo como referência de onboarding e avaliação técnica do projeto.

---

## 1. Visão Geral da Arquitetura

O projeto adota **Clean Architecture** em conjunto com o padrão **MVVM (Model-View-ViewModel)**, organizando o código em camadas de responsabilidade isoladas e unidirecionais. A regra fundamental é: **as camadas internas não conhecem as externas**.

```
┌────────────────────────────────────────────┐
│            PRESENTATION (UI)               │
│  Screens (Compose) · ViewModels · UiState  │
├────────────────────────────────────────────┤
│               DOMAIN (Negócio)             │
│  UseCases · Models · Repository Contracts  │
├────────────────────────────────────────────┤
│                DATA (Dados)                │
│  Repository Impls · Retrofit · DTOs        │
├────────────────────────────────────────────┤
│           DI (Injeção de Dependência)      │
│              Koin · appModule              │
└────────────────────────────────────────────┘
```

---

## 2. Diagrama de Camadas e Componentes

O diagrama abaixo mostra todas as classes do projeto e suas respectivas camadas:

```mermaid
graph TD
    subgraph DI["⚙️ DI Layer"]
        AppModule["appModule (Koin)"]
    end

    subgraph DATA["💾 Data Layer"]
        NasaEonetApi["NasaEonetApi (Retrofit)"]
        EonetResponse["EonetResponse (DTO)"]
        EventoDto["EventoDto (DTO)"]
        AlertaRepositoryImpl["AlertaRepositoryImpl"]
        PreferencesRepositoryImpl["PreferencesRepositoryImpl"]
    end

    subgraph DOMAIN["🧠 Domain Layer"]
        Alerta["Alerta (Model)"]
        NivelRisco["NivelRisco (Enum)"]
        AlertaRepository["AlertaRepository (Interface)"]
        PreferencesRepository["PreferencesRepository (Interface)"]
        GetAlertasUseCase["GetAlertasUseCase"]
        FiltrarAlertasUseCase["FiltrarAlertasUseCase"]
    end

    subgraph PRESENTATION["🖥️ Presentation Layer"]
        HomeScreen["HomeScreen"]
        HomeViewModel["HomeViewModel"]
        AlertasScreen["AlertasScreen"]
        AlertasViewModel["AlertasViewModel"]
        DetalheScreen["DetalheAlertaScreen"]
        DetalheViewModel["DetalheAlertaViewModel"]
        PerfilScreen["PerfilScreen"]
        PerfilViewModel["PerfilViewModel"]
        RotaScreen["RotaAlternativaScreen"]
        SplashScreen["SplashScreen"]
        OnboardingScreen["OnboardingScreen"]
    end

    AppModule --> AlertaRepositoryImpl
    AppModule --> PreferencesRepositoryImpl
    AppModule --> GetAlertasUseCase
    AppModule --> FiltrarAlertasUseCase
    AppModule --> HomeViewModel
    AppModule --> AlertasViewModel
    AppModule --> DetalheViewModel
    AppModule --> PerfilViewModel

    AlertaRepositoryImpl --> NasaEonetApi
    AlertaRepositoryImpl --> AlertaRepository
    AlertaRepositoryImpl --> Alerta
    NasaEonetApi --> EonetResponse
    EonetResponse --> EventoDto

    GetAlertasUseCase --> AlertaRepository
    FiltrarAlertasUseCase --> Alerta

    HomeViewModel --> GetAlertasUseCase
    HomeViewModel --> FiltrarAlertasUseCase
    AlertasViewModel --> GetAlertasUseCase
    AlertasViewModel --> FiltrarAlertasUseCase
    DetalheViewModel --> AlertaRepository
    DetalheViewModel --> PreferencesRepository
    PerfilViewModel --> PreferencesRepository

    HomeScreen --> HomeViewModel
    AlertasScreen --> AlertasViewModel
    DetalheScreen --> DetalheViewModel
    PerfilScreen --> PerfilViewModel
```

---

## 3. Fluxo de Dados (Da NASA até a Tela)

O diagrama abaixo representa o caminho percorrido por um dado desde a requisição na API da NASA até ser exibido na interface do usuário:

```mermaid
sequenceDiagram
    actor Tela as 🖥️ Compose UI
    participant VM as ViewModel
    participant UC as UseCase
    participant Repo as Repository (Interface)
    participant Impl as RepositoryImpl
    participant API as NASA EONET API

    Tela->>VM: Usuário abre a tela
    VM->>UC: loadAlertas()
    UC->>Repo: getAlertas()
    Repo->>Impl: (via injeção Koin)
    Impl->>API: GET /events?status=open&days=30
    API-->>Impl: JSON (EonetResponse)
    Impl-->>Repo: List<Alerta> (mapeado e filtrado)
    Repo-->>UC: List<Alerta>
    UC-->>VM: List<Alerta> (aplicando regra de NivelRisco)
    VM-->>Tela: StateFlow emite UiState.Success(alertas)
    Tela->>Tela: Recomposição automática do Compose
```

---

## 4. Fluxo de Navegação (Entre Telas)

O diagrama abaixo mapeia todos os fluxos de navegação possíveis dentro do app:

```mermaid
flowchart TD
    A([▶️ App Iniciado]) --> B[SplashScreen]
    B -->|onboarding completo| D[HomeScreen]
    B -->|primeira vez| C[OnboardingScreen]
    C -->|Avançar até o fim ou Pular| D

    D -->|Tab: Alertas| E[AlertasScreen]
    D -->|Tab: Perfil| G[PerfilScreen]
    D -->|Clica num alerta| F[DetalheAlertaScreen]
    D -->|Ver todos| E

    E -->|Tab: Home| D
    E -->|Tab: Perfil| G
    E -->|Clica num alerta| F

    F -->|← Voltar| E
    F -->|Risco ALTO ou CRÍTICO| H[RotaAlternativaScreen]

    H -->|Confirmar Desvio| D

    G -->|Tab: Home| D
    G -->|Tab: Alertas| E
    G -->|Limpar Dados e Sair| B
```

---

## 5. ADRs — Architecture Decision Records

Cada decisão abaixo foi tomada com base nas necessidades do MVP e nos padrões do mercado Android atual.

### ADR-001: Jetpack Compose vs. XML Views
- **Decisão:** Jetpack Compose
- **Justificativa:** O Compose é o padrão oficial e moderno do Android. Sua natureza declarativa alinha perfeitamente com o MVVM (a UI é uma função do estado), elimina a necessidade de `ViewBinding` e reduz drasticamente o código boilerplate de UI.
- **Trade-off:** Curva de aprendizado inicial maior para quem vem do XML tradicional.

### ADR-002: Koin vs. Hilt (Dagger)
- **Decisão:** Koin
- **Justificativa:** Como projeto MVP, a velocidade de desenvolvimento foi priorizada. O Koin é uma biblioteca Kotlin-first de DI que não depende de `annotation processing` (kapt/ksp), acelerando o build e simplificando a configuração em comparação com o Hilt.
- **Trade-off:** O Hilt oferece verificação em tempo de compilação das dependências, o que o Koin não faz. Em um projeto de produção em escala, Hilt seria avaliado.

### ADR-003: StateFlow vs. LiveData
- **Decisão:** StateFlow (Kotlin Coroutines)
- **Justificativa:** O `StateFlow` é agnóstico ao ciclo de vida do Android (não precisa de `LifecycleOwner`), o que facilita o teste unitário dos ViewModels. Combinado com `collectAsState()` do Compose, o binding de dados é natural e reativo.
- **Trade-off:** Requer atenção ao escopo de coleta para evitar vazamentos de memória em apps mais complexos.

### ADR-004: NASA EONET API vs. Firebase/Backend próprio
- **Decisão:** NASA EONET API
- **Justificativa:** A API EONET (Earth Observatory Natural Event Tracker) é pública, sem autenticação, e fornece dados reais de desastres naturais do mundo. Essa escolha eliminou a necessidade de gerenciar um backend, um banco de dados e chaves de API secretas, tornando o repositório 100% autossuficiente e auditável.
- **Trade-off:** Os dados são globais (não filtrados para o Brasil especificamente) e podem ter latência variável dependendo da carga nos servidores da NASA.

### ADR-005: UiState Sealed Class vs. Boolean flags
- **Decisão:** `UiState` como sealed class (`Initial`, `Loading`, `Success<T>`, `Error`)
- **Justificativa:** Modelar o estado da UI como um tipo fechado (sealed class) torna impossível o app estar em um estado inválido (ex: `isLoading = true` e `data != null` ao mesmo tempo). O compilador Kotlin garante que todos os estados sejam tratados no `when`.

---

## 6. Contrato da API Externa

| Propriedade | Valor |
|---|---|
| **Provider** | NASA — Earth Observatory Natural Event Tracker (EONET) |
| **Base URL** | `https://eonet.gsfc.nasa.gov/api/v3` |
| **Autenticação** | Nenhuma (Open Data) |
| **Endpoint principal** | `GET /events` |
| **Parâmetros utilizados** | `status=open`, `days=30`, `limit=50` |
| **Formato de resposta** | `application/json` |
| **Mapeamento de categorias** | `wildfires` → Queimada · `severeStorms` → Tempestade · `floods` → Enchente · `landslides` → Deslizamento |
