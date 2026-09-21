# CultCode / UnsulliedCode

An elite, offline-first mobile coding ecosystem built with Android, Kotlin, and Jetpack Compose. CultCode brings complex software engineering challenges, Git simulation, and algorithmic battles directly to your mobile device without requiring an active internet connection.

## Features (v6.2 Stable Release)
- **Offline-First Curriculum**: Complete tracks for Python, JavaScript, Java, C++, Docker, Kubernetes, SQL, Rust, Go, HTML, and more.
- **True AST & Evaluation Engine**:
  - **PythonAST**: Custom offline tokenizer and parse-tree evaluator that blocks hardcoded cheats (e.g., `print("answer")`) while permitting semantic algorithmic logic. Blocks forbidden constructs like `import os`.
  - **SqlEvaluator**: Structural database verifier running an embedded in-memory SQLite instance, comparing results with Row-Order and Column-Order insensitivity.
  - **Output Normalization**: Automatically calculates float tolerances (e.g., `3.1415` == `3.142`), numeric equivalences (`2.0` == `2`), and unordered array structural matching.
- **Code Review Arena**: Fully interactive pull-request battleground using a custom `DiffView` component to review added/removed lines of code and "Approve" or "Reject" them.
- **Motion & 3D Layer**: Fluid interactive components featuring algorithmic `pseudo3DTilt` effects, `animateFloatAsState` expansions, and responsive hover/press scaling (`Interaction.kt`).
- **Strict Design System**: Brutalist "Obsidian" and "Porcelain" themes utilizing strict mathematical design tokens (`Space.kt`, `Radius.kt`, `AppTypography.kt`).
- **Custom Syntax Engine**: Line-numbered IDE (`CodeEditor`) utilizing custom tokenizers for real-time syntax highlighting on-device.

## Architecture
- **Language**: Kotlin 2.x
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Navigation**: Typed routing via `androidx.navigation3`
- **Testing**: Robolectric + JUnit4 Golden Test Suite ensuring semantic correctness and blocking logic bypasses.
- **Storage**: Highly optimized JSON-based local asset repository.

## Releases
The latest compiled APK can be found in the `apk/` directory.

[![Download Latest APK](https://img.shields.io/badge/Download-CultCode_v6.2_APK-blue?style=for-the-badge&logo=android)](https://github.com/sayandhsr/CultCode/raw/main/apk/CultCode-v6.2.apk)

## License & Fonts
- **Fonts**: JetBrains Mono & Inter (SIL Open Font License 1.1)

---
*Built under the strict architectural directives of the UnsulliedCode project.*
