package edu.ufl.cise.plc;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




public class Lexer implements ILexer{
    private String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Lexer(String source){
        this.source = source;
    }

    //read in the raw source code
     List<Token> scanTokens() throws LexicalException {
        while(!AtEnd()){
            //beginning of a new lexeme. not at end.
            start = current;
            determineTokenKind();

        }
        tokens.add(new Token(IToken.Kind.EOF, "", null, line));
        return tokens;
    }

    //method for categorizing each token kind and adding to tokens ArrayList
    private void determineTokenKind() throws LexicalException {
        this.source = source;
        char c = advanceToken();
        //switch statements for determining Kind
        //this first set of statements don't need a peek function to check
        //for additional elements of the token
        switch (c) {
            //single char lexemes
            case '(' : addToken(IToken.Kind.LPAREN); break;
            case ')' : addToken(IToken.Kind.RPAREN); break;
            case '[' : addToken(IToken.Kind.LSQUARE); break;
            case ']' : addToken(IToken.Kind.RSQUARE); break;
            case '+' : addToken(IToken.Kind.PLUS); break;
            case '-' : addToken(IToken.Kind.MINUS); break;
            case '*' : addToken(IToken.Kind.TIMES); break;
            case '/' : addToken(IToken.Kind.DIV); break;
            case '%' : addToken(IToken.Kind.MOD); break;
            case '&' : addToken(IToken.Kind.AND); break;
            case '|' : addToken(IToken.Kind.OR); break;
            case ';' : addToken(IToken.Kind.SEMI); break;
            case ',' : addToken(IToken.Kind.COMMA); break;
            case '^' : addToken(IToken.Kind.RETURN); break;
            //operators
            //case '!': addToken(match('='))

            default:
                throw new LexicalException("Unexpected Character",line,1);

        }
    }

    //method for checking if at the end of reading in current lexeme.
    private boolean AtEnd(){
        return current >= source.length();
    }

    //advanced the current counter reading in the token
    private char advanceToken() {
        return source.charAt(current++);
    }

    private void addToken(IToken.Kind type) {
        addToken(type, null);
    }

    //adds the current token to the Token ArrayList
    private void addToken(IToken.Kind type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    //main method
    public static void main(String[] args) throws LexicalException {
        Lexer lex = new Lexer("* * + / *");
        lex.runLex();

    }


    @Override
    public IToken next() throws LexicalException {
        return null;
    }

    @Override
    public IToken peek() throws LexicalException {
        return null;
    }

    private void runLex() throws LexicalException {
        Scanner scan = new Scanner(source);
        //runs the lexer, scanning in the tokens.
        List<Token> tokens = scanTokens();
        //printing the tokens
        for(Token token:tokens) {
            System.out.println(token.getKind()+ " " + token.getText());
        }



    }

}
