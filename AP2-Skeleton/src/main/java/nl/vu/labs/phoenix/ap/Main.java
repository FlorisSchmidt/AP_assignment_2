package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;

public class Main {
	
	private void start() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		//1. Create a scanner on System.in
		//2. call interpreter.eval() on each line
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
