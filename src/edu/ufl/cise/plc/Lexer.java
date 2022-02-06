package edu.ufl.cise.plc;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
//Main is at the bottom.

public class Lexer implements ILexer {
    private String source;
    private final List<Token> tokens = new ArrayList<>();
    int tokenPos =0;
    private int start = 0;
    private int current = 0;
    private Token currentToken;
    private int line = 0;
    private int counter = 0;
    private int column = 0;
    //private int column = 0;
    private static final Map<String, IToken.Kind> keywords;

    public void setCounter(int foo) {
        counter = foo;
    }
    static {
        keywords = new HashMap<>();
        keywords.put("BLACK", IToken.Kind.COLOR_CONST);
        keywords.put("BLUE", IToken.Kind.COLOR_CONST);
        keywords.put("CYAN", IToken.Kind.COLOR_CONST);
        keywords.put("DARK_GRAY", IToken.Kind.COLOR_CONST);
        keywords.put("GRAY", IToken.Kind.COLOR_CONST);
        keywords.put("GREEN", IToken.Kind.COLOR_CONST);
        keywords.put("LIGHT_GRAY", IToken.Kind.COLOR_CONST);
        keywords.put("MAGENTA", IToken.Kind.COLOR_CONST);
        keywords.put("ORANGE", IToken.Kind.COLOR_CONST);
        keywords.put("PINK", IToken.Kind.COLOR_CONST);
        keywords.put("RED", IToken.Kind.COLOR_CONST);
        keywords.put("WHITE", IToken.Kind.COLOR_CONST);
        keywords.put("YELLOW", IToken.Kind.COLOR_CONST);
       // System.out.println("Hash map");
        keywords.put("getWidth", IToken.Kind.IMAGE_OP);
        keywords.put("getHeight", IToken.Kind.IMAGE_OP);

        keywords.put("getRed", IToken.Kind.COLOR_OP);
        keywords.put("getGreen", IToken.Kind.COLOR_OP);
        keywords.put("getBlue", IToken.Kind.COLOR_OP);

        keywords.put("string", IToken.Kind.TYPE);
        keywords.put("int", IToken.Kind.TYPE);
        keywords.put("float", IToken.Kind.TYPE);
        keywords.put("boolean", IToken.Kind.TYPE);
        keywords.put("color", IToken.Kind.TYPE);
        keywords.put("image", IToken.Kind.TYPE);
        keywords.put("void", IToken.Kind.KW_VOID);

        keywords.put("if", IToken.Kind.KW_IF);
        keywords.put("else", IToken.Kind.KW_ELSE);
        keywords.put("fi", IToken.Kind.KW_FI);
        keywords.put("write", IToken.Kind.KW_WRITE);
        keywords.put("console", IToken.Kind.KW_CONSOLE);

        keywords.put("true", IToken.Kind.BOOLEAN_LIT);
        keywords.put("false", IToken.Kind.BOOLEAN_LIT);


        //keywords.put("BLACK", new Token(IToken.Kind.COLOR_CONST), "BLACK", "");
    }

    Lexer(String source) {
        this.source = source;
    }

    //read in the raw source code
    List<Token> scanTokens() throws LexicalException {
        while (!AtEnd()) {
            //beginning of a new lexeme. not at end.
            start = current;
            determineTokenKind();

        }
       // System.out.println("token added from scan tokens");
        tokens.add(new Token(IToken.Kind.EOF, "", null, line, current));
        return tokens;
    }

    //method for categorizing each token kind and adding to tokens ArrayList
    private void determineTokenKind() throws LexicalException {
        this.source = source;
        char c = advanceToken();
        //switch statements for determining Kind
        //this first set of statements do not need a peek function to check
        //for additional elements of the lexeme
        switch (c) {
            //case '#' : while(pe)
            //single char lexemes
            case '(':
                addToken(IToken.Kind.LPAREN);
                break;
            case ')':
                addToken(IToken.Kind.RPAREN);
                break;
            case '[':
                addToken(IToken.Kind.LSQUARE);
                break;
            case ']':
                addToken(IToken.Kind.RSQUARE);
                break;
            case '+':
               // System.out.println("plus token added");
                addToken(IToken.Kind.PLUS);
                break;
            case '-':
                if (secondChecker('>')) {
                    addToken(IToken.Kind.RARROW);
                } else {
                    addToken(IToken.Kind.MINUS);
                }
                break;
            case '*':
                addToken(IToken.Kind.TIMES);
                break;
            case '%':
                addToken(IToken.Kind.MOD);
                break;
            case '&':
                addToken(IToken.Kind.AND);
                break;
            case '|':
                addToken(IToken.Kind.OR);
                break;
            case ';':
                addToken(IToken.Kind.SEMI);
                break;
            case ',':
                addToken(IToken.Kind.COMMA);
                break;
            case '^':
                addToken(IToken.Kind.RETURN);
                break;
            case '/':
                addToken(IToken.Kind.DIV);
                break;

            //OPERATORS - checks for a secondary character and classifies accordingly

            case '<':
                if (secondChecker('<')) {
                    addToken(IToken.Kind.LANGLE);
                } else if (secondChecker('=')) {
                    addToken(IToken.Kind.LE);
                } else if (secondChecker('-')) {
                    addToken(IToken.Kind.LARROW);
                } else addToken(IToken.Kind.LT);
                break;
            case '>':
                if (secondChecker('>')) {
                    addToken(IToken.Kind.RANGLE);
                } else if (secondChecker('=')) {
                    addToken(IToken.Kind.GE);
                } else addToken(IToken.Kind.GT);
                break;
            case '=':
                addToken(secondChecker('=') ? IToken.Kind.EQUALS : IToken.Kind.ASSIGN);
                break;
            case '!':
                addToken(secondChecker('=') ? IToken.Kind.NOT_EQUALS : IToken.Kind.BANG);
                break;
            //making sure to ignore comments.
            case '#':
                //advances token past all characters until newline reached.
                if (charPeek() == '\r' && peekOver() == '\n')
                    break;
                while (charPeek() != '\n' && !AtEnd()) advanceToken();
                break;
            //skippers
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            //STRING LITERALS
            case '"':
                stringLit();
                break;

            default:
                if (isDigit(c)) {
                    intLit();
                } else if (isChar(c)) {
                    ident();
                } else {
                    throw new LexicalException("Unexpected Character", line, 1);
                }
        }
    }

    //method for checking if it is going to be an ident.
    private boolean isChar(char c) {
        if ((c >= 'A' && c <= 'Z') ||
                (c >= 'a' && c <= 'z')) {
            return true;
        } else {
            return false;
        }
    }

    //helper method for ident. can be a combo of alphabet chars and numbers.
    private boolean identHelper(char c) {
        if (isChar(c) || isDigit(c)) {
            return true;
        } else {
            return false;
        }
    }

    //method for classifying if lexeme is an ident
    private void ident() {
        //while the ident is alpha numeric, keep reading in tokens.
        while (identHelper(charPeek())) {
            advanceToken();
        }
        String text = source.substring(start, current);
        IToken.Kind kwType = keywords.get(text);
        if (kwType == null)
            kwType = IToken.Kind.IDENT;
        addToken(kwType);
    }

    //this is like the stringLit() method but for numbers
    private void intLit() throws LexicalException {

        boolean isFloat = false;
        while (isDigit(charPeek())) {
            advanceToken();
        }
        //this checks for decimals, advances over decimal
        if (isDigit(peekOver()) && (charPeek() == '.')) {
            isFloat = true;
            advanceToken();
            while (isDigit(charPeek())) {
                advanceToken();
            }
        }
        if (isFloat) {
            addToken(IToken.Kind.FLOAT_LIT, Float.parseFloat(source.substring(start, current)));

        } else {
            addToken(IToken.Kind.INT_LIT, Integer.parseInt(source.substring(start, current)));
        }
    }

    //peek but one more char over
    private char peekOver() {
        if (1 + current >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    //checks if the character is an integer 0-9.
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    //method for handling string literals
    private void stringLit() throws LexicalException {
        //String block functionality here?

        //while not at end of string and not on a closing quote
        while (!AtEnd() && charPeek() != '"') {
            //if the token is a newline, advance line counter.
            if (charPeek() == '\n') {
                line++;
            }
            advanceToken();
        }
        //when the token reaches the end with no quote, string is incomplete
        if (AtEnd()) {
            throw new LexicalException("Unterminated String", line, 1);

        }
        advanceToken();
        //captures the string without quotes
        String stringLit = source.substring(start + 1, current - 1);
        column++;
        addToken(IToken.Kind.STRING_LIT, stringLit);

    }

    //this method only advances the scanner after checking for a secondary
    //character in longer lexemes like operators
    private boolean secondChecker(char secondary) {
        //if the scanner is at the end of a token, there is no additional check
        if (AtEnd()) return false;
        //if the secondary character isnt what is expected, return false.
        if (source.charAt(current) != secondary) {
            return false;
        }
        current++;
        return true;
    }

    //method for checking if at the end of reading in current lexeme.
    private boolean AtEnd() {
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
        counter++;
        String text = source.substring(start, current);
        //tracking what the current token is.
        currentToken = new Token(type, text, literal, line, column);
       // System.out.println(type + " token added");
        tokens.add(currentToken);
    }


    @Override
    public IToken next() throws LexicalException {
        if (tokens.size() != 0 && tokens.get(counter).getKind() != IToken.Kind.EOF) {
            //System.out.println(tokens.get(counter));
            return tokens.get(counter++);
        }
        //is empty
        else{
            return new Token(IToken.Kind.EOF,"",null, 0, 0);
        }

    }



    @Override
    public IToken peek() throws LexicalException {

        return  (IToken)tokens.get(tokenPos);
       // return null;
    }

    //peek method for characters
    public char charPeek(){
        if(AtEnd()) return '\0';
        return source.charAt(current);
    }

    public void runLex() throws LexicalException {
        Scanner scan = new Scanner(source);
        //runs the lexer, scanning in the tokens.
        List<Token> tokens = scanTokens();
        //printing the tokens
        for(Token token:tokens) {
            System.out.println(token.getKind()+ " " + token.getText());
        }

    }
    //*************************MAIN**************************************
    public static void main(String[] args) throws LexicalException {
        Lexer lex = new Lexer("");

        lex.runLex();
    }

}
