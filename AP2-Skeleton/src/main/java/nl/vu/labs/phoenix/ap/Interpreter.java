package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {

	PrintStream out = new PrintStream(System.out);

	@Override
	public T getMemory(String v) {
		// TODO Implement me
		return null;
	}

	@Override
	public T eval(String s) {
		Scanner input = new Scanner(s);
		try{
			parseStatement(input);
		} catch (APException e){
			out.print(e.getMessage());
		}
		return null;
	}

	private void parseStatement(Scanner input) throws APException {
		String statement = input.next();
		if(statement.equals("?")){
			out.println("print statement");
			parsePrintStatement(input);
		} else if (statement.equals("\\")){
			out.println("comment statement");
			parseCommentStatement(input);
		} else if (statement.matches("[a-zA-Z0-9]")){
			out.println("identifier statement");
			parseIdentifierStatement(statement);
		} else {
			throw new StatementException('"'+statement+'"' + " not supported");
		}
	}

	private void parsePrintStatement(Scanner input){
		String line = input.nextLine();
		line = line.replaceAll("\\s","");
		out.println(line);
	}

	private void parseCommentStatement(Scanner comment) {
		out.println(comment.nextLine());
	}

	private void parseIdentifierStatement(String input) throws IdentifierException {
		Identifier identifier = new Identifier();
		for(int i = 0;i<input.length();i++){
			identifier.add(input.charAt(i));
		}
	}



}
