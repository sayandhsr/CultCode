# UNSULLIED CODE

<div align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="UC Logo" width="200"/>
</div>

## THE ULTIMATE CODING ARCHITECTURE

**Unsullied Code** is a massive, fully-featured, strictly optimized mobile learning platform. It seamlessly blends an aggressively minimalist UI with deeply personalized, dynamic curriculum tracking. 

### 🚀 EXECUTE PROTOCOL
Test the latest production build on your Android device:
**[⬇️ Download UnsulliedCode-latest.apk](https://github.com/sayandhsr/CultCode/raw/main/apk/UnsulliedCode-latest.apk)**

## 🌟 MASTER FEATURES & EXPANSION
* **700+ QUESTION LIBRARY:** A massive local repository containing progressively scaled questions for Python, SQL, JavaScript, HTML, CSS, TypeScript, Docker, Kubernetes, and YAML.
* **UNIVERSAL SANDBOX IDE:** A dedicated code playground accessible directly from the dashboard. Toggles between VS Code mode and Jupyter Notebook cell mode, with full execution simulation and an explicit local auto-save workspace.
* **DATA SCIENCE / OOP INTEGRATION:** Features an embedded `sales_data.csv` sandbox with NumPy and Pandas execution mocking, allowing users to practice real exploratory data analysis and feature engineering on device.
* **UC SCORE & BADGE ENGINE:** A deep gamification system. Gain UC points for correctly solving code logic, and earn dynamic visual badges for milestones (e.g., *First Login*, *Python Advanced*, *101 Streak*, and the prime *Elite* badge).
* **DYNAMIC CURRICULUM PACING:** The app dynamically scales the user's study roadmap using a 15-day, 30-day, or 60-day algorithm, instantly adjusting the curriculum based on user experience levels (Beginner, Intermediate, Advanced).
* **THEME & FONT TOGGLES:** While built on a Stark Dark-Mode Brutalist core, the Settings page now allows users to cleanly toggle a High-Contrast White Theme and switch between standard Sans-Serif and Monospace fonts for ultimate readability.
* **LOCAL FILE PERSISTENCE:** Every single keystroke is autonomously buffered and saved using local `SharedPreferences`. The app never forgets your progress, your streak, or your code.

## 🛠 TECHNICAL SPECIFICATIONS
* **LANGUAGE:** Kotlin (100% Jetpack Compose)
* **EXECUTION ENGINE:** Android SQLite Engine (True Execution) / Semantic Validator Pipeline (Python, Data Science, JS, OOP)
* **ROUTING:** Compose Navigation 3 with explicit typed graphs.
* **STATE MANAGEMENT:** Persistent local storage engine.

## 📂 DIRECTORY STRUCTURE
* `/app/src/main/java/com/cultcode/engine/` - Local Execution Engines & Curriculum Roadmap Generators
* `/app/src/main/assets/` - 700+ Local JSON Question DBs (Python, SQL, JS, etc.)
* `/app/src/main/java/com/cultcode/ui/` - Compose UI Architecture (Dashboard, Sandbox, Settings, Practice)
* `/apk/` - Compiled binaries ready for deployment
