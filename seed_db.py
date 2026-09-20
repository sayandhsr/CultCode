import json
import os

assets_dir = r"X:\CODE APK\CultCode\app\src\main\assets"

def generate_python_questions():
    questions = []
    # Add the specific ones referenced in the app to prevent dead routing
    questions.append({
        "id": "PY-BASICS-001",
        "question": "Variables are containers for storing data values. Create a variable named 'score' and assign it 10.",
        "code": "# Your code here",
        "correctAnswer": "score = 10",
        "acceptedAnswers": ["score=10"],
        "hints": ["Use the equals sign for assignment"],
        "solution": "score = 10",
        "explanation": "Python does not require keyword like var or let."
    })
    questions.append({
        "id": "DSA-ARRAYS-001",
        "question": "Create a list named 'arr' with the numbers 1, 2, 3.",
        "code": "# Your code here",
        "correctAnswer": "arr = [1, 2, 3]",
        "acceptedAnswers": ["arr=[1,2,3]"],
        "hints": ["Use square brackets []"],
        "solution": "arr = [1, 2, 3]",
        "explanation": "Python lists are equivalent to arrays in DSA."
    })
    
    # Generate remaining 48 questions programmatically
    for i in range(2, 51):
        questions.append({
            "id": f"PY-AUTO-{str(i).zfill(3)}",
            "question": f"Python Challenge #{i}: Print the number {i}",
            "code": "# Your code here",
            "correctAnswer": f"print({i})",
            "acceptedAnswers": [f"print( {i} )"],
            "hints": ["Use the print function."],
            "solution": f"print({i})",
            "explanation": "The print function outputs text to the console."
        })
        
    with open(os.path.join(assets_dir, "python_questions.json"), "w") as f:
        json.dump({"questions": questions}, f, indent=2)

def generate_sql_questions():
    questions = []
    questions.append({
        "id": "SQL-MEDIUM-JOINS-001",
        "question": "Write a query to SELECT all columns from Orders joined with Customers on CustomerID.",
        "code": "SELECT * FROM Orders\n-- Your JOIN here",
        "correctAnswer": "SELECT * FROM Orders JOIN Customers ON Orders.CustomerID = Customers.CustomerID",
        "acceptedAnswers": ["SELECT * FROM Orders INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID"],
        "hints": ["Use JOIN ... ON ..."],
        "solution": "SELECT * FROM Orders JOIN Customers ON Orders.CustomerID = Customers.CustomerID",
        "explanation": "JOIN combines rows based on a related column."
    })
    
    for i in range(2, 51):
        questions.append({
            "id": f"SQL-AUTO-{str(i).zfill(3)}",
            "question": f"SQL Challenge #{i}: Select everything from table 'table{i}'",
            "code": "-- Your code here",
            "correctAnswer": f"SELECT * FROM table{i}",
            "acceptedAnswers": [],
            "hints": ["Use SELECT *"],
            "solution": f"SELECT * FROM table{i}",
            "explanation": "SELECT * grabs all columns."
        })

    with open(os.path.join(assets_dir, "sql_questions.json"), "w") as f:
        json.dump({"questions": questions}, f, indent=2)
        
def generate_js_questions():
    questions = []
    questions.append({
        "id": "JS-BASICS-001",
        "question": "Declare a constant named 'pi' and set it to 3.14.",
        "code": "// Your code here",
        "correctAnswer": "const pi = 3.14;",
        "acceptedAnswers": ["const pi=3.14;"],
        "hints": ["Use the const keyword."],
        "solution": "const pi = 3.14;",
        "explanation": "const declares block-scoped, immutable variables."
    })
    
    for i in range(2, 51):
        questions.append({
            "id": f"JS-AUTO-{str(i).zfill(3)}",
            "question": f"JS Challenge #{i}: Return the number {i}",
            "code": "// Your code here",
            "correctAnswer": f"return {i};",
            "acceptedAnswers": [f"return {i}"],
            "hints": ["Use the return keyword."],
            "solution": f"return {i};",
            "explanation": "return exits a function and returns a value."
        })

    with open(os.path.join(assets_dir, "javascript_questions.json"), "w") as f:
        json.dump({"questions": questions}, f, indent=2)

generate_python_questions()
generate_sql_questions()
generate_js_questions()
print("Successfully generated 150+ questions!")
