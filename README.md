# UNSULLIED CODE

<div align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="UC Logo" width="200"/>
</div>

## THE ULTIMATE CODING ARCHITECTURE

**Unsullied Code** is a massive, fully-featured, strictly optimized mobile learning platform. It seamlessly blends an aggressively minimalist UI with deeply personalized, dynamic curriculum tracking and real offline execution.

### 🚀 EXECUTE PROTOCOL
Test the latest production build on your Android device:
**[⬇️ Download UnsulliedCode-latest.apk](https://github.com/sayandhsr/CultCode/raw/main/apk/UnsulliedCode-latest.apk)**

## 🌟 MASTER FEATURES & EXPANSION
* **EXHAUSTIVE OFFLINE QUESTION BANK:** A massive local repository containing progressively scaled questions for Python, SQL, JavaScript, HTML, CSS, TypeScript, Docker, Kubernetes, YAML, C, C++, Java, NumPy, Pandas, and MongoDB.
* **UNIVERSAL SANDBOX IDE:** A dedicated code playground accessible directly from the dashboard. Toggles between VS Code mode and Jupyter Notebook cell mode, with full execution simulation and an explicit local auto-save workspace.
* **PHYSICAL FILESYSTEM PERSISTENCE:** Your workspaces don't just save to shared memory. Code written in the IDE explicitly saves directly to physical files (`.py`, `.js`, `.sql`) inside your device's local internal storage (`/UnsulliedCode_Data/saved_code/`).
* **DARK-THEMED ANALYTICS DASHBOARD:** Displays graphical mastery breakdown bars, total XP tracking, UC Score, and consecutive login streaks.
* **SMART OUTPUT EVALUATION:** The execution engine runs code semantically. Successful execution triggers a green success overlay unlocking the solution. Failing yields detailed errors and hints.
* **UC SCORE & BADGE ENGINE:** A deep gamification system. Gain UC points for correctly solving code logic, and earn dynamic visual badges for milestones (e.g., *First Login*, *Python Advanced*, *101 Streak*, and the prime *Elite* badge) formatted in clean UI circles.

## 🛠 TECHNICAL SPECIFICATIONS
* **LANGUAGE:** Kotlin (100% Jetpack Compose)
* **EXECUTION ENGINE:** Android SQLite Engine (True Execution) / Semantic Validator Pipeline (Python, Data Science, JS, OOP)
* **ROUTING:** Compose Navigation 3 with explicit typed graphs.
* **STATE MANAGEMENT:** Persistent physical storage engine (JSON file I/O).

## 📂 DIRECTORY STRUCTURE
* `/app/src/main/java/com/cultcode/engine/` - Local Execution Engines & Curriculum Roadmap Generators
* `/app/src/main/assets/` - Massive array of Offline JSON Question DBs
* `/app/src/main/java/com/cultcode/ui/` - Compose UI Architecture (Dashboard, Sandbox, Settings, Practice)
* `/apk/` - Compiled binaries ready for deployment
