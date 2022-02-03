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
        //checks if the kind is an integer, returns value. If not, exception is thrown.
        if(this.kind == Kind.INT_LIT) {
            return Integer.parseInt(String.valueOf(literal));
        }
        else{
            throw new UnsupportedOperationException("Not an Integer.");
        }

    }
    @Override
    public float getFloatValue(){
        if(this.kind == Kind.FLOAT_LIT) {
            return Float.parseFloat(String.valueOf(literal));
        }
        else {
            throw new UnsupportedOperationException("Not a Float.");
        }
    }

    @Override
    public boolean getBooleanValue() {
        return false;
    }

    @Override
    public String getStringValue() {
        if(this.kind == Kind.STRING_LIT) {
            return (String)literal;
        }
        else{
            throw new UnsupportedOperationException("Not a string.");
        }
    }
}
