package com.unigal.lexer;

import java.io.*;
import java.util.*;

public class Lexer {
    private final String input;
    private int pos = 0;
    private int line = 1;
    private int col = 1;
    private final int length;
    private final Set<String> keywords = new HashSet<>(Arrays.asList(
            "if","while","for","int","float","return","else","do","break","continue"
    ));

    public static class LexResult {
        public final List<Token> tokens = new ArrayList<>();
        public final List<String> errors = new ArrayList<>();
        public final Map<Token.Type, Integer> counts = new EnumMap<>(Token.Type.class);
    }

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
    }

    private char peek() {
        if (pos >= length) return (char)-1;
        return input.charAt(pos);
    }

    private char next() {
        if (pos >= length) return (char)-1;
        char c = input.charAt(pos++);
        if (c == '\n') {
            line++; col = 1;
        } else {
            col++;
        }
        return c;
    }

    private void addCount(LexResult res, Token.Type t) {
        res.counts.put(t, res.counts.getOrDefault(t, 0) + 1);
    }

    public LexResult tokenize() {
        LexResult res = new LexResult();
        while (true) {
            char c = peek();
            if (c == (char)-1) {
                res.tokens.add(new Token(Token.Type.EOF, "<EOF>", line, col));
                break;
            }
            // whitespace
            if (Character.isWhitespace(c)) {
                next();
                continue;
            }
            int tokLine = line;
            int tokCol = col;
            // comments line //
            if (c == '/' && pos+1 < length && input.charAt(pos+1) == '/') {
                // consume to end of line
                while (peek() != (char)-1 && peek() != '\n') next();
                continue;
            }
            // strings
            if (c == '"') {
                StringBuilder sb = new StringBuilder();
                sb.append(next()); // consume "
                boolean closed = false;
                while (peek() != (char)-1) {
                    char nc = next();
                    sb.append(nc);
                    if (nc == '"') { closed = true; break; }
                    if (nc == '\n') break;
                }
                String lex = sb.toString();
                if (!closed) {
                    res.errors.add(String.format("Unterminated string at %d:%d", tokLine, tokCol));
                    res.tokens.add(new Token(Token.Type.UNKNOWN, lex, tokLine, tokCol));
                    addCount(res, Token.Type.UNKNOWN);
                } else {
                    res.tokens.add(new Token(Token.Type.STRING, lex, tokLine, tokCol));
                    addCount(res, Token.Type.STRING);
                }
                continue;
            }
            // braces and punctuation
            if (c == '{') { next(); res.tokens.add(new Token(Token.Type.LBRACE, "{", tokLine, tokCol)); addCount(res, Token.Type.LBRACE); continue; }
            if (c == '}') { next(); res.tokens.add(new Token(Token.Type.RBRACE, "}", tokLine, tokCol)); addCount(res, Token.Type.RBRACE); continue; }
            if (c == '(') { next(); res.tokens.add(new Token(Token.Type.LPAREN, "(", tokLine, tokCol)); addCount(res, Token.Type.LPAREN); continue; }
            if (c == ')') { next(); res.tokens.add(new Token(Token.Type.RPAREN, ")", tokLine, tokCol)); addCount(res, Token.Type.RPAREN); continue; }
            if (c == ';') { next(); res.tokens.add(new Token(Token.Type.SEMICOLON, ";", tokLine, tokCol)); addCount(res, Token.Type.SEMICOLON); continue; }
            if (c == '.') { next(); res.tokens.add(new Token(Token.Type.DOT, ".", tokLine, tokCol)); addCount(res, Token.Type.DOT); continue; }

            // numbers
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append(next());
                boolean dotSeen = false;
                while (Character.isDigit(peek()) || (!dotSeen && peek() == '.')) {
                    if (peek() == '.') dotSeen = true;
                    sb.append(next());
                }
                String lex = sb.toString();
                res.tokens.add(new Token(Token.Type.NUMBER, lex, tokLine, tokCol));
                addCount(res, Token.Type.NUMBER);
                continue;
            }

            // identifiers / keywords
            if (Character.isLetter(c) || c == '_') {
                StringBuilder sb = new StringBuilder();
                sb.append(next());
                while (Character.isLetterOrDigit(peek()) || peek() == '_') sb.append(next());
                String lex = sb.toString();
                if (keywords.contains(lex)) {
                    res.tokens.add(new Token(Token.Type.KEYWORD, lex, tokLine, tokCol));
                    addCount(res, Token.Type.KEYWORD);
                } else {
                    res.tokens.add(new Token(Token.Type.IDENTIFIER, lex, tokLine, tokCol));
                    addCount(res, Token.Type.IDENTIFIER);
                }
                continue;
            }

            // operators (simple)
            if ("+-*/=%<>!&|".indexOf(c) >= 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(next());
                // try two-char operators
                if ((sb.charAt(0)=='=' || sb.charAt(0)=='!' || sb.charAt(0)=='<' || sb.charAt(0)=='>') && peek()=='=') {
                    sb.append(next());
                } else if ((sb.charAt(0)=='&' || sb.charAt(0)=='|') && peek()==sb.charAt(0)) {
                    sb.append(next());
                }
                String lex = sb.toString();
                res.tokens.add(new Token(Token.Type.OPERATOR, lex, tokLine, tokCol));
                addCount(res, Token.Type.OPERATOR);
                continue;
            }

            // unknown single char
            String unk = Character.toString(next());
            res.errors.add(String.format("Unrecognized token '%s' at %d:%d", unk, tokLine, tokCol));
            res.tokens.add(new Token(Token.Type.UNKNOWN, unk, tokLine, tokCol));
            addCount(res, Token.Type.UNKNOWN);
        }
        return res;
    }
}
