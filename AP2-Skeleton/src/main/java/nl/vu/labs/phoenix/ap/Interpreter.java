package nl.vu.labs.phoenix.ap;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.PrintStream;
import java.math.BigInteger;
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
		try{
            s = removeUnnecessarySpace(s);
            Scanner input = new Scanner(s);
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
		String statement = input.nextLine();

		if(statement.startsWith("?")){
			out.println("Printing");
			parsePrintStatement(statement);
		} else if(statement.startsWith("/")){
			out.println("comment");
		} else {
			parseIdentifierStatement(new Scanner(statement));
		}
	}

	private void parsePrintStatement(String input) throws NoSuchElementException{

		if(input.length()!=1){
			input = input.substring(1);
		}
		Scanner line = new Scanner(input);
		while(line.hasNext()){
			String token = line.next();
			if(hasAndRemoveMemory(token, false)){
				T set = getMemory(token);
				printSet(set);
			} else {
				throw new NoSuchElementException(token);
			}
		}

	}

	private void printSet(T set){
		int size = set.size();
		SetInterface setCopy = set.copy();
		out.print("{ ");
		for(int i = 0;i<size;i++){
			out.print(setCopy.get().toString()+" ");
			setCopy.remove(setCopy.get());
		}
		out.println("}");
	}

	private void parseIdentifierStatement(Scanner input) throws APException {
		input.useDelimiter("=");
		String identifierInput = input.next();

		if(identifierInput.contains(" ")){
			throw new IdentifierException("no space between identifiers allowed");
		}
		Identifier identifier = makeIdentifier(identifierInput);

		if(hasAndRemoveMemory(identifier.value(), true)){
			memory.remove(identifier);
			out.print("overwriting\n");
		}
		//TODO check if statement is filled
		if(!input.hasNext()){
			throw new StatementException("assignment expected");
		}
		parseExpression(input, identifier);
	}

	private Identifier makeIdentifier(String name) throws IdentifierException {
		Identifier id = new Identifier();
		for(int i = 0;i<name.length();i++){
			id.add(name.charAt(i));
		}
		return id;
	}

	private void parseExpression(Scanner input, Identifier identifier) throws APException {
		String expression = input.next().trim();

		if(expression.startsWith("(")){
			parseComplexFactor(input, identifier);
		} else if (expression.startsWith("{")){
			Set set = parseNaturalNumbers(new Scanner(expression));
			memory.put(identifier, (T) set);
		} else if (expression.startsWith("[")){
			parseSet(input);
		} else if (expression.contains("*")||expression.contains("+")){
			parseOperator(new Scanner(expression));
		} else {
			throw new ExpressionException("");
		}

	}

	private void parseComplexFactor(Scanner input, Identifier identifier){

	}

	private Set parseNaturalNumbers(Scanner input) throws NumberException {
		Set set = new Set<BigInteger>();

		String line = input.nextLine().trim().substring(1);
		if(line.startsWith(",")){
			throw new NumberException("no element before first ,");
		}
		input = new Scanner(line);
		input.useDelimiter(",");

		while(input.hasNext()){
			String token = input.next().trim();
			if(token.equals("")){
				throw new NumberException("no element after ,");
			}
			if(token.contains(" ")){
				throw new NumberException("no space between numbers allowed");
			}

			if(token.endsWith("}")){
				if(input.hasNext()){
					throw new NumberException("No element allowed after }");
				} else if(token.length()==1) {
					throw new NumberException("No element before }");
				} else {
					token = token.substring(0,token.length()-1);
					set = addToSet(set,token);
					continue;
				}
			}
			set = addToSet(set,token);
		}
		return set;
	}

	private Set addToSet(Set set,String string) throws NumberException{
		if(string.matches("[0-9]*")){
			set.add(string);
		} else {
			throw new NumberException(string);
		}
		return set;
	}

	private void parseSet(Scanner input){

	}

	private void parseOperator(String expression, Scanner input) throws NoSuchElementException, IdentifierException {
		String line = input.nextLine().trim();
//		if(!memory.containsKey(expression)){
//			throw new NoSuchElementException(expression);
//		} else {
//			T id = memory.get(expression);
//
//			memory.put();
//		}
	}

	private void parseOperations(){
	}

	private String parseMultiplying(String line){
		if(line.contains("*")){
			addParentheses(line);
		}
		return line;
	}

	private String addParentheses(String line){
		int i = 0;
		int adjacendLength = 0;
		boolean opened = false;
		boolean closed = false;
		StringBuffer result = new StringBuffer(line);

		for(;i<line.length();i++){
			char c = line.charAt(i);

			if(!opened){
				if(c == '*'){
					result.insert(i-adjacendLength,'(');
					adjacendLength=0;
					opened = true;
				} else if (c == '+' || c=='-' || c=='|'){
					adjacendLength=0;
				} else {
					adjacendLength++;
				}
			}
			else {
				if(c == '+' || c=='-' || c=='|'){
					result.insert(i+adjacendLength,')');
					closed=true;
					String remainingString = result.substring(i+adjacendLength);
				} else {
					adjacendLength++;
				}
			}

		}
		if(!closed){
			result.append(')');
		}

		return result.toString();
	}

	private void parseOperator(Scanner input){
		String line = input.nextLine();
		if (line.contains("*")){
			out.print("Detected a *");
			line = parseMultiplying(line);
		}
	}

	private String removeUnnecessarySpace(String input) throws IdentifierException, NumberException {
		input.trim();
		boolean insideIdentifier = false;
		boolean insideNumber = false;
		boolean hasSpace = false;

		for (int i = 0; i < input.length(); i++) {
			String c = String.valueOf(input.charAt(i));
			if(c.matches(" " ) && (insideIdentifier || insideNumber)){
				hasSpace = true;
			} else if(c.matches("[a-zA-z]")){
				insideIdentifier = true;
			} else if(c.matches("[0-9]")){
				insideNumber = true;
			} else {
                insideIdentifier = false;
                insideNumber = false;
                hasSpace = false;
            }

			if(hasSpace && insideIdentifier && c.matches("[a-zA-z]")){
				throw new IdentifierException(" no space between identifiers allowed");
			}
			if (hasSpace && insideNumber && c.matches("[0-9]")){
				throw new NumberException("no space between numbers allowed");
			}
		}
		return input.replaceAll("\\s+","");
	}
}