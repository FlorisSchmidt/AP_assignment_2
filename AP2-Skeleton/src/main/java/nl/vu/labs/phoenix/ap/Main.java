package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;

public class Main {
	
	private void start() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
	Set<Integer> setA = new Set<>();
	Set<Integer> setB = new Set<>();
	Integer a, b, c, d, e, f, g, h;
	a = 1;
	b = 2;
	c = 3;
	d = 4;
	e = 3;
	f = 4;
	g = 5;
	h = 6;
	setA.add(a);
	setA.add(b);
	setA.add(c);
	setA.add(d);
	setB.add(e);
	setB.add(f);
	setB.add(g);
	setB.add(h);
	SetInterface<Integer> setC = setA.union(setB);
	SetInterface<Integer> setD = setA.difference(setB);
	SetInterface<Integer> setE = setA.intersection(setB);
	SetInterface<Integer> setF = setA.symDifference(setB);
	int x = 3;


	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
