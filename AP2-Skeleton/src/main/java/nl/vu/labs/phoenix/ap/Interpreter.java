package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {

	PrintStream out = new PrintStream(System.out);
	HashMap<Identifier, T> memory;


	public Interpreter(){
		memory = new HashMap<>();
	}


	@Override
	public T getMemory(String v) {
		for(Identifier id : memory.keySet()){
			if(id.value().equals(v)){
				return memory.get(id);
			}
		}
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

	// TODO 2 functions in one?
	public boolean hasAndRemoveMemory(String v, boolean remove){
		for(Identifier id : memory.keySet()){
			if(id.value().equals(v)){
				if(remove){
					memory.remove(id);
				}
				return true;
			}
		}
		return false;
	}

	private void parseStatement(Scanner input) throws APException {
		String statement = input.next();
		if(statement.equals("?")){
			out.println("print statement");
			parsePrintStatement(input);
		} else if (statement.equals("\\")){
			out.println("comment statement");
		} else if (statement.matches("[a-zA-Z0-9]*")){
			out.println("identifier statement");
			parseIdentifierStatement(statement,input);
		} else {
			throw new StatementException('"'+statement+'"' + " not supported");
		}
	}

	private void parsePrintStatement(Scanner input){
//		String line = input.nextLine();
//		parseOperator(line);

		while(input.hasNext()){
			String token = input.next();
			if(hasAndRemoveMemory(token, false)){
				T set = getMemory(token);
				out.print(set.get().toString());
			}
		}

	}

	private void parseIdentifierStatement(String statement, Scanner input) throws APException {
		Identifier identifier = new Identifier();

		for(int i = 0;i<statement.length();i++){
			identifier.add(statement.charAt(i));
		}

		if(hasAndRemoveMemory(identifier.value(), true)){
			memory.remove(identifier);
			out.print("overwriting");
		}
		//TODO check if statement is filled
		if(!input.hasNext()){
			throw new StatementException("assignment expected");
		}
		if(!input.next().equals("=")){
			throw new StatementException("spacing between identifiers is not allowed");
		}

		if(!input.hasNext()){
			throw new StatementException("nothing to assign to");
		}

		parseExpression(input, identifier);
	}

	private void parseExpression(Scanner input, Identifier identifier) throws APException {
		String expression = input.next();

		if(expression.equals("(")){
			parseComplexFactor(input, identifier);
		} else if (expression.equals("{")){
			parseNaturalNumbers(input, identifier);
		} else if (expression.equals("[")){
			parseSet(input);
		} else if (expression.matches("[a-zA-Z0-9]")){
			parseOperator(expression, input);
		} else {
			throw new ExpressionException("");
		}

	}

	void parseComplexFactor(Scanner input, Identifier identifier){

	}

	void parseNaturalNumbers(Scanner input, Identifier identifier) throws NumberException {
		Set set = new Set<BigInteger>();
		while(input.hasNext()){
			String token = input.next();
			if(token.equals("}")){
				break;
			} else if (token.matches("[1-9]*")){
				set.add(new BigInteger(token));
			} else {
				throw new NumberException(token);
			}
		}
		memory.put(identifier, (T) set);
	}

	void parseSet(Scanner input){

	}
	private void parseOperator(String expression, Scanner input) throws NoSuchElementException {
		if(!memory.containsKey(expression)){
			throw new NoSuchElementException(expression);
		}
	}

	private void parseOperator(String line){
		if (line.contains("*")){
			out.print("Detected a *");
			//TODO add multiplying funcionality
		}

	}


}
