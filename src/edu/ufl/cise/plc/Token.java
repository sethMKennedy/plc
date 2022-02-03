package edu.ufl.cise.plc;

public class Token implements IToken{
    //class variables for Token
    SourceLocation source;
    Kind kind;
    String lexeme;
    Object literal;
    int line;
    //Token constructor
    Token(Kind kind, String lexeme, Object literal, int line ){
        this.kind = kind;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    //returns the token kind
    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public String getText() {
        return lexeme;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return source;
    }

    @Override
    public int getIntValue() {
        return 0;
    }

    @Override
    public float getFloatValue() {
        return 0;
    }

    @Override
    public boolean getBooleanValue() {
        return false;
    }

    @Override
    public String getStringValue() {
        return null;
    }
}
