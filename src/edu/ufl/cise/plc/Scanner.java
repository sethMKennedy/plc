package edu.ufl.cise.plc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.ufl.cise.plc.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;
    Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }
    private boolean isAtEnd() {
        return current>= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' : addToken(LEFT_PAREN); break;
            case ')' : addToken(RIGHT_PAREN); break;
            case '[' : addToken(LEFT_BRACKET); break;
            case ']' : addToken(RIGHT_BRACKET); break;
            case ',' : addToken(COMMA); break;
            case '.' : addToken(DOT); break;
            case '-' : addToken(MINUS); break;
            case '+' : addToken(PLUS); break;
            case ';' : addToken(SEMICOLON); break;
            case '*' : addToken(STAR); break;
            case '&' : addToken(AMP); break;
            case '!' : addToken(NOT); break;
            case '^' : addToken(CARROT); break;
            case '%' : addToken(MODULO); break;
            case '|' : addToken(PIPE); break;
            default  : Lexer.error(line, "That character isn't recognized!"); break;
        }
    }
}
