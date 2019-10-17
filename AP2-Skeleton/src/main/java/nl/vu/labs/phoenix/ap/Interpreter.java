package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {

	private PrintStream out = new PrintStream(System.out);
	private HashMap<Identifier, T> memory;

	public Interpreter() {
		memory = new HashMap<>();
	}

	@Override
	public T getMemory(String v) {
		for (Identifier id : memory.keySet()) {
			if (id.value().equals(v)) {
				return memory.get(id);
			}
		}
		return null;
	}

	private void removeMemory(String v){
		for(Identifier id : memory.keySet()){
			if(id.value().equals(v)){
				memory.remove(id);
				return;
			}
		}
	}

	@Override
	public T eval(String s) {
		try {
			s = s.trim();
			Scanner input = new Scanner(s);
			input.useDelimiter("");
			if(nextCharIs(input, '?')){
				return parsePrintStatement(input);
			} else if (nextCharIs(input, '/')){
				return parseComment(input);
			} else if (nextCharIsLetter(input)){
				return parseAssignment(input);
			} else {
				throw new SyntaxException("Invalid starting character");
			}
		} catch (APException e) {
			out.print(e.getMessage());
		}
		return null;
	}

	private T parseAssignment(Scanner s) throws APException {
		Identifier id = parseIdentifier(s);
		skipSpaces(s);
		if(nextCharIs(s,'=')){
			nextChar(s);
			skipSpaces(s);

			T expression = parseExpression(s);
			if(s.hasNext()){
				throw new AssignmentException("End of line expected");
			}
			removeMemory(id.value());
			memory.put(id,expression);

			if(expression.size()==0){
				return null;
			}

			return expression;
		} else {
			throw new IdentifierException("equals sign expected");
		}
	}


	private T parsePrintStatement(Scanner s) throws APException {
		nextChar(s);
		skipSpaces(s);
		T expression = parseExpression(s);
		printSet(expression);
		if(s.hasNext()){
			throw new SyntaxException("End of line expected");
		}
		return expression;
	}

	private void printSet(SetInterface s){
		StringBuffer sb = new StringBuffer("{ ");
		SetInterface setCopy = s.copy();
		for(int i = 0;i<s.size();i++){
			sb.append(setCopy.get().toString()).append(" ");
			setCopy.remove(setCopy.get());
		}
		sb.append("}");
		System.out.println(sb.toString());
	}

	private T parseComment(Scanner s) {
		System.out.println(s.nextLine());
		return null;
	}

	private Identifier parseIdentifier(Scanner s){
		Identifier id = new Identifier();
		while (nextCharIsLetter(s) || nextCharIsDigit(s)){
			id.add(nextChar(s));
		}
		return id;
	}

	private T parseExpression(Scanner s) throws APException {
		T term = parseTerm(s);
		T term2;
		char operator;
		while(true) {
			skipSpaces(s);

			if(!s.hasNext() || nextCharIs(s,')')){
				return term;
			}

			operator = nextChar(s);
			skipSpaces(s);

			if(isAdditiveOperator(operator)) {
				term2 = parseTerm(s);
				switch(operator){
					case '+':
						term = (T) term.union(term2);
						break;
					case '-':
						term = (T) term.difference(term2);
						break;
					case '|':
						term = (T) term.symDifference(term2);
						break;
				}
			} else {
				throw new ExpressionException("Unknown ending");
			}
		}
	}

	private T parseTerm(Scanner s) throws APException {
		T factor = parseFactor(s);
		T factor2;
		while(true) {
			skipSpaces(s);
			if(!s.hasNext()){
				return factor;
			}

			if(nextCharIs(s,'*')){
				nextChar(s);
				skipSpaces(s);
				factor2 = parseFactor(s);
				factor = (T) factor.intersection(factor2);
			} else {
				break;
			}
		}
		return factor;
	}

	private T parseFactor(Scanner s) throws APException {
		T set;
		if(nextCharIsLetter(s)) {
			Identifier id = parseIdentifier(s);
			set = getMemory(id.value());
			if(set==null) {
				throw new NoSuchElementException(id.value());
			}
			return set;
		} else if (nextCharIs(s,'{')){
			return parseSet(s);
		} else if (nextCharIs(s, '(')){
			return parseComplexFactor(s);
		} else {
			throw new FactorException("Factor not well formulated");
		}
	}

	private T parseComplexFactor(Scanner s) throws APException {
		nextChar(s);
		skipSpaces(s);
		T set = parseExpression(s);
		if(!nextCharIs(s,')')){
			throw new ComplexFactorException("No closing bracket found");
		}
		nextChar(s);
		return set;
	}

	private BigInteger parseNaturalNumber(Scanner s) throws APException {
		skipSpaces(s);
		StringBuffer sb = new StringBuffer();
		while(true){
			if(nextCharIsDigit(s)){
				sb.append(nextChar(s));
			} else if (nextCharIs(s,',') || nextCharIs(s,'}')){
				break;
			} else if (nextCharIs(s,' ')) {
				skipSpaces(s);
				if (nextCharIs(s, ',') || nextCharIs(s, '}')) {
					break;
				} else {
					throw new NumberException("Space after number");
				}
			} else {
				if(s.hasNext()){
					throw new NumberException(nextChar(s)+" is not supported");
				} else {
					throw new SyntaxException("Unexpected end of line");
				}

			}
		}
		if(sb.length()==0){
			throw new NumberException("No element found");
		}
		if (sb.charAt(0) == '0' && sb.length() > 1) throw new NumberException("Number cannot start with 0");
		return new BigInteger(sb.toString());
	}

	private T parseSet(Scanner s) throws APException {
		Set set = new Set<BigInteger>();
		nextChar(s);
		skipSpaces(s);
		while(!nextCharIs(s,'}')){
			set.add(parseNaturalNumber(s));
			if(nextCharIs(s,'}')) {
				break;
			}
			nextChar(s);
		}
		nextChar(s);
		return (T) set;
	}

	private boolean isAdditiveOperator(char c){
		return c == '+' || c == '|' || c == '-';
	}

	private void skipSpaces(Scanner s){
		while(nextCharIs(s, ' ')) {
			nextChar(s);
		}
	}

	private char nextChar(Scanner in) {
		return in.next().charAt(0);
	}

	private boolean nextCharIs(Scanner in,char c) {
		return in.hasNext(Pattern.quote(c+""));
	}

	private boolean nextCharIsDigit(Scanner in) {
		return in.hasNext("[0-9]");
	}

	private boolean nextCharIsLetter(Scanner in) {
		return in.hasNext("[a-zA-Z]");
	}
}
