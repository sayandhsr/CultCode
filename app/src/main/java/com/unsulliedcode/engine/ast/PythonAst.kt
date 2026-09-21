package com.unsulliedcode.engine.ast

object PythonAst {
    fun hasHardcodedPrint(code: String, expected: String): Boolean {
        val noComments = code.lines().map { it.substringBefore("#") }.joinToString("\n")
        val tokens = tokenize(noComments)
        
        for (i in 0 until tokens.size - 3) {
            if (tokens[i].type == TokenType.IDENTIFIER && tokens[i].value == "print") {
                if (tokens[i+1].type == TokenType.PAREN_OPEN) {
                    val arg = tokens[i+2]
                    if (arg.type == TokenType.STRING || arg.type == TokenType.NUMBER) {
                        val close = tokens[i+3]
                        if (close.type == TokenType.PAREN_CLOSE) {
                            val printedVal = arg.value
                            if (printedVal == expected || printedVal.replace("\\s".toRegex(), "") == expected.replace("\\s".toRegex(), "")) {
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }
    
    fun hasForbiddenConstruct(code: String, construct: String): Boolean {
        val noComments = code.lines().map { it.substringBefore("#") }.joinToString("\n")
        val tokens = tokenize(noComments)
        
        for (i in tokens.indices) {
            if (tokens[i].type == TokenType.IDENTIFIER && tokens[i].value == "import") {
                if (i + 1 < tokens.size && tokens[i+1].type == TokenType.IDENTIFIER && tokens[i+1].value == construct) {
                    return true
                }
            }
        }
        return false
    }

    enum class TokenType {
        IDENTIFIER, NUMBER, STRING, PAREN_OPEN, PAREN_CLOSE, OTHER
    }

    data class Token(val type: TokenType, val value: String)

    private fun tokenize(code: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        while (i < code.length) {
            val c = code[i]
            when {
                c.isWhitespace() -> i++
                c == '(' -> { tokens.add(Token(TokenType.PAREN_OPEN, "(")); i++ }
                c == ')' -> { tokens.add(Token(TokenType.PAREN_CLOSE, ")")); i++ }
                c == '"' || c == '\'' -> {
                    val quote = c
                    var str = ""
                    i++
                    while (i < code.length && code[i] != quote) {
                        str += code[i]
                        i++
                    }
                    if (i < code.length) i++
                    tokens.add(Token(TokenType.STRING, str))
                }
                c.isDigit() -> {
                    var num = ""
                    while (i < code.length && (code[i].isDigit() || code[i] == '.')) {
                        num += code[i]
                        i++
                    }
                    tokens.add(Token(TokenType.NUMBER, num))
                }
                c.isLetter() || c == '_' -> {
                    var id = ""
                    while (i < code.length && (code[i].isLetterOrDigit() || code[i] == '_')) {
                        id += code[i]
                        i++
                    }
                    tokens.add(Token(TokenType.IDENTIFIER, id))
                }
                else -> {
                    tokens.add(Token(TokenType.OTHER, c.toString()))
                    i++
                }
            }
        }
        return tokens
    }
}
