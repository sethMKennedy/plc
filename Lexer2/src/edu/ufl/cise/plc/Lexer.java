package edu.ufl.cise.plc;

import edu.ufl.cise.plc.IToken.Kind;
import java.util.*;
import java.lang.*;


public class Lexer implements ILexer{

    private enum State {START, IN_IDENT, HAVE_COMMENT, HAVE_ZERO, HAVE_DOT, IN_FLOAT, IN_NUM, HAVE_EQ,
        HAVE_MINUS , LT, GT, EXC, QUOTE}

    int pos = 0;
    int position;
    int length = 0;
    int startCol = 0;
    int column = 0;
    int line = 0;

    Map<String, Token.Kind> resWord = new HashMap<String, Token.Kind>();

    String input;

    State state = State.START;
    Kind kind;
    ArrayList<Token> tList = new ArrayList<Token>();
    Token t;

    public Lexer(String str) {
        initializeMap();
        input = str;
        state = State.START;
    }

    private Token getT() throws LexicalException {

        String stringVal;
        char[] chars = input.toCharArray();
        while (true) {

            char ch = chars[pos];

            switch (state) {
                case START : {
                    int startPos = pos;

                    switch (ch) {
                        case '#' : {
                            state = State.HAVE_COMMENT;
                            pos++;
                            column++;
                            break;
                        }
                        case '\n': {
                            pos++;
                            line++;
                            column = 0;
                            break;
                        }
                        case ' ':
                        case '\t':
                        case '\r': {
                            pos++;
                            column++;
                            break;
                        }
                        case '\"' : {

                            if (chars.length == 1) {
                                throw new LexicalException("Single Quote");
                            }

                            position = startPos;
                            startCol = column;
                            kind = Kind.STRING_LIT;
                            state = State.QUOTE;
                            length = 1;
                            pos++;
                            break;
                        }
                        case '+' : {
                            kind = Kind.PLUS;
                            position = startPos;
                            startCol = column;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            length = 1;
                            pos++;
                            column++;
                            return t;
                        }
                        case ']' : {
                            kind = Kind.RSQUARE;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case '[' : {
                            kind = Kind.LSQUARE;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case '!' : {
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            state = State.EXC;
                            kind = Kind.BANG;
                            break;
                        }
                        case '>': {
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            state = State.GT;
                            kind = Kind.GT;
                            break;
                        }
                        case '<' : {
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            state = State.LT;
                            kind = Kind.LT;
                            break;
                        }
                        case '(' : {
                            kind = Kind.LPAREN;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case ')' : {

                            kind = Kind.RPAREN;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case ',' : {
                            kind = Kind.COMMA;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case '^' : {
                            kind = Kind.RETURN;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case ';' : {
                            kind = Kind.SEMI;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case '/' : {
                            kind = Kind.DIV;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case '%' : {
                            kind = Kind.MOD;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case '|' : {
                            kind = Kind.OR;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;
                        }
                        case '*' : {

                            kind = Kind.TIMES;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            t = new Token(kind, Character.toString(ch), line, position, length);
                            tList.add(t);
                            pos++;
                            column++;
                            return t;

                        }
                        case '_':
                        case 'M':
                        case 'L':
                        case 'n':
                        case 'd':
                        case 'm':
                        case 'o':
                        case 'v':
                        case 'N':
                        case 'a':
                        case 'Z':
                        case 'z':
                        case 'X':
                        case 'y':
                        case 'Y':
                        case 'q':
                        case 'Q':
                        case 'r':
                        case 'P':
                        case 'R':
                        case 's':
                        case 'S':
                        case 't':
                        case 'T':
                        case 'p':
                        case 'u':
                        case 'U':
                        case 'V':
                        case 'w':
                        case 'W':
                        case 'O':
                        case 'A':
                        case '$':
                        case 'b':
                        case 'B':
                        case 'c':
                        case 'C':
                        case 'D':
                        case 'e':
                        case 'E':
                        case 'f':
                        case 'F':
                        case 'g':
                        case 'G':
                        case 'h':
                        case 'H':
                        case 'i':
                        case 'I':
                        case 'j':
                        case 'x':
                        case 'l':
                        case 'K':
                        case 'J':
                        case 'k': {
                            state = State.IN_IDENT;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            kind = Kind.IDENT;
                            break;
                        }
                        case '0' : {
                            state = State.HAVE_ZERO;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            kind = Kind.INT_LIT;
                            break;
                        }
                        case '1':
                        case '8':
                        case '2':
                        case '3':
                        case '4':
                        case '9':
                        case '7':
                        case '6':
                        case '5': {
                            state = State.IN_NUM;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            kind = Kind.INT_LIT;
                            break;
                        }
                        case '-' : {
                            state = State.HAVE_MINUS;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            kind = Kind.ASSIGN;
                            break;
                        }
                        case '.' : {
                            state = State.HAVE_DOT;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            break;
                        }
                        case '=' : {
                            state = State.HAVE_EQ;
                            position = startPos;
                            startCol = column;
                            length = 1;
                            pos++;
                            kind = Kind.EQUALS;
                            break;
                        }
                        case 0 : {
                            //this is the end of the input, add an EOF token
                            kind = Kind.EOF;
                            t = new Token(kind, null, line, position, 0);
                            tList.add(t);
                            return t;
                        }
                        default : {
                            throw new LexicalException("Cannot Start With " + ch);
                        }
                    }
                    break;
                }
                case IN_IDENT : {

                    if (Character.isDigit(ch) || Character.isAlphabetic(ch) || ch == '_' || ch == '$') {
                        pos++;
                        length++;
                        column++;
                    }
                    else {
                        kind = Kind.IDENT;
                        state = State.START;

                        stringVal = String.copyValueOf(chars, position, length);

                        if (resWord.containsValue(stringVal)) {
                            t = new Token(resWord.get(stringVal), stringVal, line, startCol, length);
                        }
                        else {
                            t = new Token(kind, stringVal, line, startCol, length);
                        }

                        tList.add(t);
                        column++;
                        return t;
                    }
                    break;
                }
                case HAVE_ZERO : {

                    if (ch == '.') {
                        length++;
                        pos++;
                        column++;
                        state = State.IN_FLOAT;
                    }
                    else {
                        kind = Kind.INT_LIT;
                        state = State.START;
                        stringVal = String.copyValueOf(chars, position, length);
                        t = new Token(kind, stringVal, line, startCol, length);
                        tList.add(t);
                        column++;
                        return t;
                    }
                    break;
                }
                case IN_FLOAT : {

                    if (Character.isDigit(ch)) {
                        pos++;
                        length++;
                        column++;
                    }
                    else {
                        kind = Kind.FLOAT_LIT;
                        state = State.START;
                        stringVal = String.copyValueOf(chars, position, length);

                        t = new Token(kind, stringVal, line, startCol, length);
                        tList.add(t);
                        column++;
                        return t;
                    }
                    break;
                }
                case IN_NUM : {

                    if (Character.isDigit(ch)) {
                        pos++;
                        length++;
                        column++;
                    }
                    else if (ch == '.') {
                        pos++;
                        length++;
                        column++;
                        state = State.IN_FLOAT;
                    }
                    else {
                        stringVal = String.copyValueOf(chars, position, length);
                        kind = Kind.INT_LIT;
                        state = State.START;

                        t = new Token(kind, stringVal, line, startCol, length);
                        tList.add(t);
                        column++;
                        return t;
                    }
                    break;
                }
                case HAVE_COMMENT : {

                    switch(chars[pos]){

                        case '\\' : {

                            pos++;
                            if(chars[pos] == 'r' || chars[pos] == 'n'){
                                pos++;
                                line++;
                                column = 0;
                                state = State.START;

                            }
                            else if (pos > chars.length - 1) {
                                pos++;
                                line++;
                                column = 0;
                                state = State.START;
                            }
                            else{
                                state = State.START;
                                throw new LexicalException("bad comment");
                            }
                            break;
                        }
                        default : {
                            pos++;
                            column++;
                            break;
                        }
                    }
                    break;
                }
                case HAVE_EQ : {

                    if (ch == '=') {
                        kind = Kind.EQUALS;
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                    }
                    else {
                        kind = Kind.ASSIGN;
                        stringVal = String.copyValueOf(chars, position, length);
                    }
                    state = State.START;

                    t = new Token(kind, stringVal, line, startCol, length);
                    tList.add(t);
                    column++;
                    return t;
                }
                case HAVE_MINUS : {
                    if (ch == '>') {
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                        kind = Kind.RARROW;
                    }
                    else {
                        kind = Kind.MINUS;
                        stringVal = "=";
                    }

                    state = State.START;

                    t = new Token(kind, stringVal, line, startCol, length);
                    tList.add(t);
                    column++;
                    return t;
                }
                case LT : {
                    if (ch == '=') {
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                        kind = Kind.LE;
                    }
                    else if (ch == '<'){
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                        kind = Kind.LANGLE;
                    }
                    else if (ch == '-') {
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                        kind = Kind.LARROW;
                    }
                    else {
                        stringVal = String.copyValueOf(chars, position, length);
                        kind = Kind.LT;
                    }
                    state = State.START;

                    t = new Token(kind, stringVal, line, startCol, length);
                    tList.add(t);
                    column++;
                    return t;
                }
                case GT : {
                    if (ch == '=') {
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                        kind = Kind.GE;
                    }
                    else if (ch == '>'){
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                        kind = Kind.RANGLE;
                    }
                    else {
                        kind = Kind.GT;
                        stringVal = String.copyValueOf(chars, position, length);
                    }
                    state = State.START;

                    t = new Token(kind, stringVal, line, startCol, length);
                    tList.add(t);
                    column++;
                    return t;
                }
                case QUOTE : {
                    if (ch == '\\') {
                        length++;
                        pos++;
                        column++;
                        ch = chars[pos];

                        if (ch == 'b'|| ch == 't' || ch == 'n' || ch == 'f' || ch == 'r' || ch == '\"' || ch == '\'' || ch == '\\') {
                            length++;
                            pos++;
                            column++;
                            break;
                        }
                        else {
                            state = State.START;
                            throw new LexicalException("Lexer Bug");
                        }
                    }
                    else if (ch == '"') {
                        state = State.START;
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        t = new Token(kind, stringVal, line, startCol, length);
                        tList.add(t);
                        pos++;
                        column++;
                        return t;
                    }
                    length++;
                    pos++;
                    column++;
                    break;
                }
                case EXC : {
                    if (ch == '=') {
                        length++;
                        stringVal = String.copyValueOf(chars, position, length);
                        pos++;
                        column++;
                        kind = Kind.NOT_EQUALS;
                    }
                    else {
                        kind = Kind.BANG;
                        stringVal = String.copyValueOf(chars, position, length);
                    }
                    state = State.START;

                    t = new Token(kind, stringVal, line, startCol, length);
                    tList.add(t);
                    column++;
                    return t;
                }
                default : {
                    state = State.START;
                    throw new LexicalException("lexer bug");
                }
            }

            if (pos > chars.length || chars.length == 0) {

                if (kind == Kind.STRING_LIT) {
                    throw new LexicalException("Single Double Quote");
                }

                kind = Kind.EOF;
                t = new Token(kind, null, line, position, 0);
                tList.add(t);
                return t;
            }
            else if (pos == chars.length) {
                stringVal = String.copyValueOf(chars, position, length);

                if (state == State.START || state == State.HAVE_COMMENT) {
                    kind = Kind.EOF;
                    t = new Token(kind, null, line, position, 0);
                }
                else if (resWord.containsValue(stringVal)) {
                    t = new Token(resWord.get(stringVal), stringVal, line, startCol, length);
                }
                else if (kind == Kind.STRING_LIT) {
                    throw new LexicalException("Single Double Quote");
                }
                else {
                    t = new Token(kind, stringVal, line, startCol, length);
                }

                tList.add(t);
                column++;
                return t;
            }
        }
    }

    @Override
    public IToken next() throws LexicalException {

        if (pos > input.length() - 1) {
            kind = Kind.EOF;
            t = new Token(kind, null, line, position, 0);
            tList.add(t);
            return t;
        }
        t = getT();
        return t;
    }

    @Override
    public IToken peek() throws LexicalException {

        int savePos = pos;
        int savePosition = position;
        int saveLine = line;
        int saveCol = column;
        int saveSCol = startCol;

        if (pos >  input.length() - 1) {
            kind = Kind.EOF;
            t = new Token(kind, null, line, position, 0);
            tList.add(t);
            return t;
        }
        t = getT();

        pos = savePos;
        position = savePosition;
        line = saveLine;
        column = saveCol;
        startCol = saveSCol;
        return t;
    }

    private void initializeMap() {
        resWord.put("if", IToken.Kind.KW_IF);
        resWord.put("else", IToken.Kind.KW_ELSE);
        resWord.put("fi", IToken.Kind.KW_FI);
        resWord.put("write", IToken.Kind.KW_WRITE);
        resWord.put("console", IToken.Kind.KW_CONSOLE);
        resWord.put("void", IToken.Kind.KW_VOID);
        resWord.put("string", IToken.Kind.TYPE);
        resWord.put("int", IToken.Kind.TYPE);
        resWord.put("float", IToken.Kind.TYPE);
        resWord.put("boolean", IToken.Kind.TYPE);
        resWord.put("color", IToken.Kind.TYPE);
        resWord.put("image", IToken.Kind.TYPE);
        resWord.put("BLACK", Token.Kind.COLOR_CONST);
        resWord.put("BLUE", IToken.Kind.COLOR_CONST);
        resWord.put("CYAN", IToken.Kind.COLOR_CONST);
        resWord.put("DARK_GRAY", IToken.Kind.COLOR_CONST);
        resWord.put("GRAY", IToken.Kind.COLOR_CONST);
        resWord.put("GREEN", IToken.Kind.COLOR_CONST);
        resWord.put("LIGHT_GRAY", IToken.Kind.COLOR_CONST);
        resWord.put("MAGENTA", IToken.Kind.COLOR_CONST);
        resWord.put("ORANGE", IToken.Kind.COLOR_CONST);
        resWord.put("PINK", IToken.Kind.COLOR_CONST);
        resWord.put("RED", IToken.Kind.COLOR_CONST);
        resWord.put("WHITE", IToken.Kind.COLOR_CONST);
        resWord.put("YELLOW", IToken.Kind.COLOR_CONST);
        resWord.put("getRed", IToken.Kind.COLOR_OP);
        resWord.put("getGreen", IToken.Kind.COLOR_OP);
        resWord.put("getBlue", IToken.Kind.COLOR_OP);
        resWord.put("true", IToken.Kind.BOOLEAN_LIT);
        resWord.put("false", IToken.Kind.BOOLEAN_LIT);
        resWord.put("getWidth", IToken.Kind.IMAGE_OP);
        resWord.put("getHeight", IToken.Kind.IMAGE_OP);
    }

}
