package edu.ufl.cise.plc;

import edu.ufl.cise.plc.*;
import edu.ufl.cise.plc.IToken.Kind;

class Token implements IToken {

    private enum Text {IDENT, RESERVE, INT_LIT, FLOAT_LIT, STRING_LIT, BOOL_LIT}

    final Kind kind;
    final String input;
    final int posCol;
    final int posLine;
    final int length;
    IToken.SourceLocation sl;

    Token(Kind kind, String input, int posLine, int posCol, int length){

        this.kind = kind;
        this.input = input;
        this.posCol = posCol;
        this.posLine = posLine;
        this.length = length;
        sl = new IToken.SourceLocation(posLine, posCol);

    }

    public record SourceLocation(int line, int column){

    }

    @Override
    public Kind getKind() {

        return kind;

    }

    @Override
    public String getText() {

        return input;

    }

    @Override
    public IToken.SourceLocation getSourceLocation() {
        return sl;
    }

    @Override
    public int getIntValue(){

        if(kind == Kind.INT_LIT){

            int retVal = Integer.parseInt(input);
            return retVal;

        } else {

            throw new IllegalArgumentException("not an int");

        }

    }

    @Override
    public float getFloatValue() {

        if(kind == Kind.FLOAT_LIT){

            float retVal = Float.parseFloat(input);
            return retVal;

        } else {

            throw new IllegalArgumentException("not a float"); //not sure how to handle if wrong get is called.

        }

    }

    @Override
    public boolean getBooleanValue() {

        if(kind == Kind.BOOLEAN_LIT){

            boolean retVal = Boolean.parseBoolean(input);
            return retVal;

        } else {

            throw new IllegalArgumentException("not a bool");

        }

    }


    @Override
    public String getStringValue(){

        String retVal = input;
        if(retVal.contains("\\" + "t")){

            retVal = input.replaceAll("\\" + "t", "\t");

        }
        if(retVal.contains("\\" + "n")){

            retVal = input.replaceAll("\\" + "n", "\n");

        }
        if(retVal.contains("\\" + "b")){

            retVal = input.replaceAll("\\" + "b", "\b");

        }
        if(retVal.contains("\\" + "f")){

            retVal = input.replaceAll("\\" + "f", "\f");

        }
        if(retVal.contains("\\" + "r")){

            retVal = input.replaceAll("\\" + "r", "\r");

        }
        if(retVal.contains("\\" + "'")){

            retVal = input.replaceAll("\\" + "'", "\'");

        }
        if(retVal.contains("\\" + "\"")){

            retVal = input.replaceAll("\\" + "\"", "\"");

        }
        if(retVal.contains("\\" + "\\")){

            retVal = input.replaceAll("\\" + "\\", "\\\\");

        }

        return retVal;
    }

}
