package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;

public class Main {
	
	private void start() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		LinkedList<Integer> test = new LinkedList<Integer>();
		Integer a, b, c, d, e;
		a = 5;
		b = 6;
		c = 7;
		d = 8;
		test.insert(a);
		test.insert(b);
		test.insert(c);
		test.insert(d);
		boolean x = test.goToNext();
		LinkedList copy = (LinkedList) test.copy();
		copy.goToFirst();
//		Integer x = (Integer) test.retrieve();
//		test.goToFirst();
//		test.goToNext();
//		test.goToNext();
//		test.remove();
//		Integer y = (Integer) test.retrieve();
//		test.goToPrevious();
//		Integer z = (Integer) test.retrieve();
		int kaaas = 3;

	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
