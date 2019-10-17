package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

public class Main {

	PrintStream out;
	InterpreterInterface<Set<BigInteger>> interpreter;

	private void start() {
		interpreter = new Interpreter<Set<BigInteger>>();
		out = new PrintStream(System.out);

		promptUser();
	}

	private void promptUser(){
		out.print("Enter input:\n");
		Scanner input = new Scanner(System.in);

		while(input.hasNext()) {
			String nextLine = input.nextLine();
			parseInput(nextLine);
		}
	}

	private void parseInput(String input){
		Scanner inputScanner = new Scanner(input);
		interpreter.eval(inputScanner.nextLine());
	}

	public static void main(String[] args) {
		new Main().start();
	}

}