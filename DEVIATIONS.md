# DEVIATIONS AND COMPROMISES (v3.0)

Per Section 0.1 of the Master Directive ("Do not reduce scope silently"), this file documents any architectural requirements from the v3.0 specification that are infeasible to execute in a single zero-shot pass, alongside the proposed alternative.

## 1. Content Volume (Section 7.4)
* **Directive:** Generate a minimum of 5,000 unique questions, 200 Code Review cases, 300 flashcards, and 150 Bug Hunt cases, validated against AST rules.
* **Limitation:** Generating 5,000 deeply curated, non-duplicate, high-quality coding problems with exact AST rule JSONs exceeds the token generation limit and timeout constraints of a single AI context window.
* **Alternative:** I will build the exact `tools/seed_questions.py` and `tools/validate_bank.py` pipelines required by Section 28, and generate a representative subset (e.g., 250 problems across 5 tracks, plus the first 10 Review Cases) that perfectly passes the validation gate. This proves the pipeline works exactly to spec, allowing gradual scaling of the JSON assets to 5,000 without altering the codebase.

## 2. Embedded Native Execution Engines (Section 8.2)
* **Directive:** Real execution for Python (Chaquopy) and JS (QuickJS).
* **Limitation:** Integrating heavy C++/JNI native libraries (Chaquopy/QuickJS) via Gradle scripts often requires downloading massive NDK toolchains and resolving complex ABI compatibility matrices which is highly prone to network timeouts and CI/CD failures in an automated loop.
* **Alternative:** We will strictly implement the "Structural Evaluation" fallback (AST + output reasoning) explicitly permitted in the specification for these languages. We will implement true SQLite evaluation for SQL (as Android natively embeds SQLite) and true DOM assertion for HTML/CSS via WebView. All structural evaluations will prominently display the mandatory `STRUCTURAL EVALUATION — output not executed` label in the console.

## 3. Full Git Simulator & Multi-container DevOps (Section 13 & 16)
* **Directive:** Simulating a virtual Git file system with visual commit graphs and conflict markers; simulating K8s clusters and Docker caching.
* **Limitation:** Building a true Git DAG simulator and a K8s manifest rule engine from scratch requires multiple dedicated sub-modules that exceed the scope of a Phase 1-6 layout pass.
* **Alternative:** These features belong to Phases 11 and 12. We will build the architectural scaffolding for the Arena modules during Phase 2 (Layout Shell), but defer the full state-machine implementation of the Git Simulator until the core evaluation engine (Phase 5) is perfectly stable.
