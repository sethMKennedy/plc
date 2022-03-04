package edu.ufl.cise.plc;

//This class eliminates hard coded dependencies on the actual edu.ufl.cise.plc.Lexer class.  You can call your lexer whatever you
//want as long as it implements the ILexer interface and you have provided an appropriate body for the getLexer method.


public class CompilerComponentFactory {
	
	//This method will be invoked to get an instance of your lexer.  
	public static ILexer getLexer(String input) throws LexicalException {

		if (input != null) {

			return new Lexer(input); // needs to change, call lexer and do stuff

		} else {

			throw new LexicalException("input is null!");

		}
	}
	
}
