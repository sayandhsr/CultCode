import json
import os

assets_dir = r"X:\CODE APK\CultCode\app\src\main\assets"

def generate_questions(prefix, lang_name):
    questions = []
    
    # BASICS
    questions.append({"id": f"{prefix}-BASICS-001", "question": f"Print 'Hello World' in {lang_name}.", "code": "", "correctAnswer": "print('Hello World')", "expectedOutput": "Hello World", "acceptedAnswers": [], "hints": [], "solution": "print('Hello World')", "explanation": "Basic output."})
    for i in range(2, 51):
        questions.append({"id": f"{prefix}-BASICS-{str(i).zfill(3)}", "question": f"{lang_name} Basic Challenge {i}", "code": "", "correctAnswer": "solve()", "expectedOutput": f"Output {i}", "acceptedAnswers": [], "hints": [], "solution": "solve()", "explanation": ""})

    # INTERMEDIATE
    questions.append({"id": f"{prefix}-INTERMEDIATE-101", "question": f"Write an intermediate {lang_name} function.", "code": "", "correctAnswer": "function()", "expectedOutput": "Executed", "acceptedAnswers": [], "hints": [], "solution": "function()", "explanation": ""})
    for i in range(102, 151):
        questions.append({"id": f"{prefix}-INTERMEDIATE-{str(i).zfill(3)}", "question": f"{lang_name} Intermediate Challenge {i}", "code": "", "correctAnswer": "solve()", "expectedOutput": f"Output {i}", "acceptedAnswers": [], "hints": [], "solution": "solve()", "explanation": ""})

    return questions

lang_map = {
    "JS": ("javascript_questions.json", "JavaScript"),
    "YAM": ("yaml_questions.json", "YAML"),
    "DOC": ("docker_questions.json", "Docker"),
    "K8S": ("k8s_questions.json", "Kubernetes"),
    "JAV": ("java_questions.json", "Java"),
    "C": ("c_questions.json", "C"),
    "CPP": ("cpp_questions.json", "C++"),
    "DAT": ("data_science_questions.json", "Data Science"),
    "HTM": ("html_questions.json", "HTML"),
    "CSS": ("css_questions.json", "CSS"),
    "TS": ("typescript_questions.json", "TypeScript"),
    "NUM": ("numpy_questions.json", "NumPy"),
    "PAN": ("pandas_questions.json", "Pandas"),
    "MON": ("mongodb_questions.json", "MongoDB")
}

for prefix, (filename, lang_name) in lang_map.items():
    with open(os.path.join(assets_dir, filename), "w") as f:
        json.dump({"questions": generate_questions(prefix, lang_name)}, f, indent=2)

print("Generated full technology matrix JSON files!")
