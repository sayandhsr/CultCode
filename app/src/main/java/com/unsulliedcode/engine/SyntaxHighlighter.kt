package com.unsulliedcode.engine

import androidx.compose.ui.graphics.Color
import com.unsulliedcode.ui.theme.AppColors

data class SyntaxToken(val text: String, val type: TokenType)

enum class TokenType {
    KEYWORD, STRING, NUMBER, COMMENT, FUNCTION, TYPE, VARIABLE, OPERATOR, PUNCTUATION, PLAIN
}

class SyntaxHighlighter(private val colors: AppColors) {

    private val keywordSets = mapOf(
        "python" to setOf("def", "class", "if", "elif", "else", "for", "while", "return", "import", "from", "as", "try", "except", "finally", "with", "yield", "lambda", "pass", "break", "continue", "and", "or", "not", "in", "is", "True", "False", "None", "raise", "global", "nonlocal", "assert", "del", "async", "await"),
        "javascript" to setOf("function", "const", "let", "var", "if", "else", "for", "while", "return", "import", "export", "from", "class", "extends", "new", "this", "super", "try", "catch", "finally", "throw", "async", "await", "yield", "of", "in", "typeof", "instanceof", "void", "delete", "true", "false", "null", "undefined", "switch", "case", "default", "break", "continue"),
        "java" to setOf("public", "private", "protected", "static", "final", "class", "interface", "extends", "implements", "new", "this", "super", "return", "if", "else", "for", "while", "do", "switch", "case", "default", "break", "continue", "try", "catch", "finally", "throw", "throws", "void", "int", "long", "double", "float", "boolean", "char", "byte", "short", "String", "true", "false", "null", "import", "package", "abstract", "synchronized"),
        "c" to setOf("auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "include", "define", "NULL"),
        "cpp" to setOf("auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "class", "public", "private", "protected", "virtual", "override", "new", "delete", "this", "template", "typename", "namespace", "using", "try", "catch", "throw", "nullptr", "true", "false", "include", "define", "string", "vector", "map", "cout", "cin", "endl"),
        "sql" to setOf("SELECT", "FROM", "WHERE", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "TABLE", "INDEX", "VIEW", "JOIN", "INNER", "LEFT", "RIGHT", "OUTER", "ON", "AND", "OR", "NOT", "IN", "BETWEEN", "LIKE", "ORDER", "BY", "GROUP", "HAVING", "LIMIT", "OFFSET", "AS", "INTO", "VALUES", "SET", "NULL", "IS", "EXISTS", "DISTINCT", "UNION", "ALL", "COUNT", "SUM", "AVG", "MIN", "MAX", "CASE", "WHEN", "THEN", "ELSE", "END", "PRIMARY", "KEY", "FOREIGN", "REFERENCES", "CONSTRAINT", "CHECK", "DEFAULT", "UNIQUE", "CASCADE"),
        "go" to setOf("break", "case", "chan", "const", "continue", "default", "defer", "else", "fallthrough", "for", "func", "go", "goto", "if", "import", "interface", "map", "package", "range", "return", "select", "struct", "switch", "type", "var", "true", "false", "nil", "iota", "append", "cap", "close", "copy", "delete", "len", "make", "new", "panic", "print", "println", "recover"),
        "rust" to setOf("as", "break", "const", "continue", "crate", "else", "enum", "extern", "false", "fn", "for", "if", "impl", "in", "let", "loop", "match", "mod", "move", "mut", "pub", "ref", "return", "self", "Self", "static", "struct", "super", "trait", "true", "type", "unsafe", "use", "where", "while", "async", "await", "dyn")
    )

    fun tokenize(code: String, language: String): List<SyntaxToken> {
        val keywords = keywordSets[language.lowercase()] ?: emptySet()
        val tokens = mutableListOf<SyntaxToken>()
        var i = 0

        while (i < code.length) {
            when {
                // Comments
                code.startsWith("//", i) || code.startsWith("#", i) -> {
                    val end = code.indexOf('\n', i).let { if (it == -1) code.length else it }
                    tokens.add(SyntaxToken(code.substring(i, end), TokenType.COMMENT))
                    i = end
                }
                code.startsWith("/*", i) -> {
                    val end = code.indexOf("*/", i + 2).let { if (it == -1) code.length else it + 2 }
                    tokens.add(SyntaxToken(code.substring(i, end), TokenType.COMMENT))
                    i = end
                }
                // Strings
                code[i] == '"' || code[i] == '\'' -> {
                    val quote = code[i]
                    var j = i + 1
                    while (j < code.length && code[j] != quote) {
                        if (code[j] == '\\') j++ // skip escape
                        j++
                    }
                    if (j < code.length) j++ // include closing quote
                    tokens.add(SyntaxToken(code.substring(i, j), TokenType.STRING))
                    i = j
                }
                // Numbers
                code[i].isDigit() -> {
                    var j = i
                    while (j < code.length && (code[j].isDigit() || code[j] == '.' || code[j] == 'x' || code[j] == 'f')) j++
                    tokens.add(SyntaxToken(code.substring(i, j), TokenType.NUMBER))
                    i = j
                }
                // Words (keywords, functions, types, variables)
                code[i].isLetter() || code[i] == '_' -> {
                    var j = i
                    while (j < code.length && (code[j].isLetterOrDigit() || code[j] == '_')) j++
                    val word = code.substring(i, j)
                    val type = when {
                        word in keywords -> TokenType.KEYWORD
                        j < code.length && code[j] == '(' -> TokenType.FUNCTION
                        word[0].isUpperCase() -> TokenType.TYPE
                        else -> TokenType.VARIABLE
                    }
                    tokens.add(SyntaxToken(word, type))
                    i = j
                }
                // Operators
                code[i] in "+-*/%=<>!&|^~?" -> {
                    tokens.add(SyntaxToken(code[i].toString(), TokenType.OPERATOR))
                    i++
                }
                // Punctuation
                code[i] in "(){}[];:,." -> {
                    tokens.add(SyntaxToken(code[i].toString(), TokenType.PUNCTUATION))
                    i++
                }
                else -> {
                    tokens.add(SyntaxToken(code[i].toString(), TokenType.PLAIN))
                    i++
                }
            }
        }
        return tokens
    }

    fun colorForToken(type: TokenType): Color = when (type) {
        TokenType.KEYWORD -> colors.syntaxKeyword
        TokenType.STRING -> colors.syntaxString
        TokenType.NUMBER -> colors.syntaxNumber
        TokenType.COMMENT -> colors.syntaxComment
        TokenType.FUNCTION -> colors.syntaxFunction
        TokenType.TYPE -> colors.syntaxType
        TokenType.VARIABLE -> colors.syntaxVariable
        TokenType.OPERATOR -> colors.syntaxOperator
        TokenType.PUNCTUATION -> colors.syntaxPunct
        TokenType.PLAIN -> colors.textPrimary
    }
}
