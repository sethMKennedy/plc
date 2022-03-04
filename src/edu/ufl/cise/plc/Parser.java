package edu.ufl.cise.plc;
import edu.ufl.cise.plc.Lexer;

import java.util.List;

public class Parser implements IParser {
    private int current = 0;
    //List<Token> iTokens
    Parser(List<Token> tokens) {

    }
    public static void main(String[] args) throws LexicalException {
        Lexer lex = new Lexer("99999999999999999999999999999999999999999999999999999999999999999999999");

        List<Token> tokens = lex.runLex();
        System.out.println("foo");
    }

}
