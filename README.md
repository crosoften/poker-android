# Poker Android

Aplicativo Android para plataforma de poker online.

## Requisitos

- Android Studio Arctic Fox ou superior
- Gradle 8.0+
- JDK 8
- Android SDK 26+ (mínimo)
- Android SDK 34 (target)

## Configuração

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Aguarde o sync do Gradle
4. Configure o arquivo `google-services.json` para Firebase
5. Build e execute

## Arquitetura

O projeto utiliza MVVM com as seguintes camadas:

- **ui/** - Activities e Fragments
- **viewModel/** - ViewModels e lógica de apresentação
- **repository/** - Camada de dados
- **data/** - Models e DTOs
- **api/** - Serviços REST e Socket.io
- **di/** - Injeção de dependências (Koin)
- **firebase/** - Integração com Firebase Cloud Messaging

## Principais dependências

- Kotlin
- Navigation Component
- Koin (injeção de dependências)
- Retrofit (chamadas HTTP)
- Socket.io (comunicação em tempo real)
- Firebase Messaging (notificações push)
- Google Maps
- Glide (carregamento de imagens)
- Coroutines

## Build

```bash
./gradlew assembleDebug
```

Para build de release:

```bash
./gradlew assembleRelease
```

## Versão atual

**1.0.0.14** (versionCode 14)
