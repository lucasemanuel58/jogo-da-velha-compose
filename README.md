# Jogo da Velha — versão Kotlin/Compose

Refação do meu primeiro app ([jogoDavelha](https://github.com/lucasemanuel58/jogoDavelha), de 2018) com tecnologia atual: **Kotlin 2.0**, **Jetpack Compose**, **Material 3**, **MVVM** e testes de unidade.

> Motivação: mostrar a evolução entre o código que eu escrevia em 2018 (JavaScript/Cordova) e a forma como escrevo Android hoje.

## Features

- Jogo contra o computador ou **2 jogadores locais**.
- Dificuldade **Fácil** (primeira casa livre) e **Difícil** (minimax — nunca perde).
- Placar de vitórias de X, de O e empates.
- Tema claro/escuro com **dynamic color** (Android 12+).
- Reiniciar a qualquer momento.

## Arquitetura

```
app/src/main/java/com/lucasemanuel/jogodavelha/
├── MainActivity.kt          # Activity + setContent
├── core/
│   ├── Board.kt             # regras puras: vitória, empate, casas livres
│   ├── Ai.kt                # minimax e modo fácil
│   └── Game.kt              # estado da partida (turno, resultado)
└── ui/
    ├── GameUiState.kt       # estado imutável da tela + placar
    ├── GameViewModel.kt     # orquestra jogador/IA e publica StateFlow
    ├── GameScreen.kt        # Compose UI
    └── theme/Theme.kt       # Material 3 + dynamic color
```

A regra de negócio **não depende do Android**: `core/` é Kotlin puro e por isso pode ser testado com JUnit, sem emulator. A UI apenas observa um `StateFlow<GameUiState>`.

## Como rodar

1. Abra a pasta no Android Studio e configure **Gradle JDK = 17** (`File > Project Structure > SDK Location`).
2. Sincronize o Gradle e rode no emulator ou dispositivo.

Pela linha de comando (com `JAVA_HOME` apontando para um JDK 17):

```bash
./gradlew test          # testes de unidade
./gradlew assembleDebug # APK em app/build/outputs/apk/debug/
```

Não quer buildar? Baixe o APK da [release v1.0.0](https://github.com/lucasemanuel58/jogo-da-velha-compose/releases/tag/v1.0.0).

## Testes

`GameTest` cobre vitória, empate, jogada inválida, bloqueio e aproveitamento de vitória pela IA, escolha do modo fácil e reset. O workflow em `.github/workflows/ci.yml` roda `./gradlew test` a cada push/PR e anexa o relatório.

Também há um teste de UI com Compose Testing em `androidTest` (roda em dispositivo/emulador).

## Decisões

- **Minimax puro, sem poda alpha-beta**: o tabuleiro tem só 9 casas, então a simplicidade facilita a leitura do código e os testes.
- **Estado imutável na UI**: cada jogada gera um novo snapshot do tabuleiro; evita bugs de referência mutável no Compose.
- **Atraso de 250 ms antes da jogada da IA**: dá tempo para o usuário perceber que o computador "pensou".

## Licença

[Unlicense](LICENSE) — mesmo espírito do projeto original.
