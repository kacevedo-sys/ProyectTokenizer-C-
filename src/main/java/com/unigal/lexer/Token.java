package com.unigal.lexer;

public class Token {
    public enum Type {
        KEYWORD,
        IDENTIFIER,
        NUMBER,
        STRING,
        LBRACE,
        RBRACE,
        LPAREN,
        RPAREN,
        SEMICOLON,
        OPERATOR,
        DOT,
        UNKNOWN,
        EOF
    }

    public final Type type;
    public final String lexeme;
    public final int line;
    public final int column;

    public Token(Type type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("(%d:%d) %-10s '%s'", line, column, type.name(), lexeme);
    }
}
