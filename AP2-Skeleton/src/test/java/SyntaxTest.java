import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

import nl.vu.labs.phoenix.ap.InterpreterInterface;
import nl.vu.labs.phoenix.ap.Set;
import nl.vu.labs.phoenix.ap.SetInterface;
import nl.vu.labs.phoenix.ap.Interpreter;


public class SyntaxTest {

	@Test
	public void identifierTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// spacing inside the identifier test
		interpreter.eval("M        ies = {123, 456, 789");
		SetInterface<BigInteger> actual = interpreter.getMemory("M");
		assertNull("spacing in the identifier is not allowed", actual);
		actual = interpreter.getMemory("ies");
		assertNull("spacing in the identifier is not allowed", actual);
		actual = interpreter.getMemory("Mies");
		assertNull("spacing in the identifier is not allowed", actual);
		
		// case sensitivity tests
		interpreter.eval("DonaldDuck = {1337}");
		actual = interpreter.getMemory("DonaldDuck");
		assertNotNull("upper case characters should be allowed", actual);
		
		interpreter.eval("MickeyMouse = donaldduck");
		actual = interpreter.getMemory("MickeyMouse");
		assertNull("identifiers should be case sensitive", actual);
		
		// numericals tests
		interpreter.eval("G00fy = {42}");
		actual = interpreter.getMemory("G00fy");
		assertNotNull("numbers should be allowed in identifiers", actual);
		
		interpreter.eval("1Goofy = {42}");
		actual = interpreter.getMemory("1Goofy");
		assertNull("identifiers are not allowed to start with a number", actual);
		
		// alphanumericals test
		interpreter.eval("Scrooge_mcDuck = {999999}");
		actual = interpreter.getMemory("Scrooge_mcDuck");
		assertNull("only alphanumerical characcters should be allowed in the identifier", actual);
	}
	
	@Test
	public void spacesTest() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// ignore prefix spacing tests
		SetInterface<BigInteger> actual = interpreter.eval("         ? {1,2}");
		assertNotNull("spacing in front of the command symbols should be ignored", actual);
		
		interpreter.eval("        Aap = {1,2}");
		actual = interpreter.getMemory("Aap");
		assertNotNull("spacing in front of the assignment identifier should be ignored", actual);
		
		// ignore suffix spacing test
		interpreter.eval("Aap = {3,4}             ");
		actual = interpreter.getMemory("Aap");
		ArrayList<BigInteger> expected = convertExpectedList("3 4");
		assertTrue("spacing behind the statement should be ignored", compareSets(actual, expected));
		
		// ignore spacing around the equals sign tests
		interpreter.eval("Aap          = {5,6}");
		actual = interpreter.getMemory("Aap");
		expected = convertExpectedList("5 6");
		assertTrue("spacing in front of the equals sign should be ignored", compareSets(actual, expected));
		
		interpreter.eval("Aap =          {7,8}");
		actual = interpreter.getMemory("Aap");
		expected = convertExpectedList("7 8");
		assertTrue("spacing behind the equals sign should be ignored", compareSets(actual, expected));
		
		// ignore spacing behind print command sign
		actual = interpreter.eval("?          Aap");
		assertTrue("spacing behind the print command sign should be ignored", compareSets(actual, expected));
		
		// ignore spacing inside the sets tests
		interpreter.eval("Mies = {       1,     2     ,3       }");
		actual = interpreter.getMemory("Mies");
		expected = convertExpectedList("1 2 3");
		assertTrue("spacing inside the set between the elements should be ignored", compareSets(actual, expected));
		
		interpreter.eval("Mies = {123, 4   5   6, 789");
		actual = interpreter.getMemory("Mies");
		assertTrue("spacing between the digits of the numbers is NOT allowed", compareSets(actual, expected));
		
		// ignore spacing between operators
		actual = interpreter.eval("? {1}         +   {2}+{3}+        {4}");
		expected = convertExpectedList("1 2 3 4");
		assertTrue("spacing between operators should be ignored", compareSets(actual, expected));
		
		actual = interpreter.eval("? {1}         *   {2}*{3}*        {4}");
		expected = convertExpectedList("");
		assertTrue("spacing between operators should be ignored", compareSets(actual, expected));
	}

	@Test
	public void emptyLineTest() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		SetInterface<BigInteger> actual = interpreter.eval("");
		assertNull("an empty line should result in an exception and thus return Null", actual);
	}

	@Test
	public void priorityTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();

		// same priority test			
		SetInterface<BigInteger> actual = interpreter.eval("? {1, 2, 3} + {4, 5} | {2, 3, 4} - {5}");
		ArrayList<BigInteger> expected = convertExpectedList("1");
		assertTrue("the operators '+', '|' and '-' should have the same priority", compareSets(actual, expected));
		
		// higher priority test
		actual = interpreter.eval("? {1, 2, 3} + {4, 5, 6} * {3, 4}");
		expected = convertExpectedList("1 2 3 4");
		assertTrue("the operator '*' should have a higher priority than the other operators", compareSets(actual, expected));	
	}
	
	@Test
	public void parenthesisBalanceTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		interpreter.eval("Ape = {1, 2, 3, 4, 5, 6, 7, 8, 9}");
		
		// missing closing parenthesis test
		interpreter.eval("Ape = (({1, 2, 3})");
		SetInterface<BigInteger> actual = interpreter.getMemory("Ape");
		ArrayList<BigInteger> expected = convertExpectedList("1 2 3 4 5 6 7 8 9");
		assertTrue("missing closing parenthesis' should result in an exception", compareSets(actual, expected));
		
		// missing opening parenthesis test
		interpreter.eval("Ape = ({1, 2, 3}))");
		actual = interpreter.getMemory("Ape");
		assertTrue("missing opening parenthesis' should result in an exception", compareSets(actual, expected));
	}
	
	@Test
	public void breakLineTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		interpreter.eval("Mies = {1,2,3} / ");
		interpreter.eval("+ {4,5}");
		SetInterface<BigInteger> actual = interpreter.getMemory("Mies");
		assertNull("the two lines above should not be interpreted as Mies = {1,2,3} + {4,5} and instead should result in an exception", actual);
		
		interpreter.eval("Mies = {19, 20,");
		interpreter.eval("21}");
		actual = interpreter.getMemory("Mies");
		assertNull("the two lines above should not be interpreted as Mies = {19, 20, 21} and instead should result in an exception", actual);
	}
	
	@Test
	public void doubleAssignmentTest() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		interpreter.eval("Ape = {1, 10, 100, 1000}          Wim = {3, 4}");
		SetInterface<BigInteger> actual = interpreter.getMemory("Ape");
		assertNull("only 1 assignment per command is allowed, thus an exception should occur and Ape should not exist", actual);
		actual = interpreter.getMemory("Wim");
		assertNull("only 1 assignment per command is allowed, thus an exception should occur and Wim should not exist", actual);
	}
	
	@Test
	public void referencingTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		interpreter.eval("Ape = {1,2,3,4,5,6,7,8,9}");
		interpreter.eval("Nut = Ape");
		interpreter.eval("Ape = {1,2,5}");
		SetInterface<BigInteger> actual = interpreter.getMemory("Nut");
		ArrayList<BigInteger> expected = convertExpectedList("1 2 3 4 5 6 7 8 9");
		assertTrue("Nut should not change when Ape changes, meaning that they should refer to the same object in the memory", compareSets(actual, expected));
		
		interpreter.eval("Wim = Ape        + Ape + {     }*Nut*(Ape + Nut)");
		actual = interpreter.getMemory("Wim");
		expected = convertExpectedList("1 2 5");
		assertTrue("Wim should be equal to Ape now", compareSets(actual, expected));
		
		// retrieving again
		actual = interpreter.getMemory("Wim");
		assertTrue("retrieving Wim a second time should leave it unchanged", compareSets(actual, expected));
		
		// self-assignment
		interpreter.eval("Wim = Wim");
		actual = interpreter.getMemory("Wim");
		assertTrue("a self-assignment should leave Wim unchanged", compareSets(actual, expected));
	}
	
	@Test
	public void emptySetOperationsTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// empty union
		SetInterface<BigInteger> actual = interpreter.eval("? {} + {}");
		ArrayList<BigInteger> expected = convertExpectedList("");
		assertNotNull("an empty union operation should not cause an exception to occur", actual);
		assertTrue("an empty union operation should result in an empty set", compareSets(actual, expected));
		
		// empty intersection
		actual = interpreter.eval("? {} * {}");
		assertNotNull("an empty intersection operation should not cause an exception to occur", actual);
		assertTrue("an empty intersection operation should result in an empty set", compareSets(actual, expected));	
		
		// empty complement
		actual = interpreter.eval("? {} - {}");
		assertNotNull("an empty complement operation should not cause an exception to occur", actual);
		assertTrue("an empty complement operation should result in an empty set", compareSets(actual, expected));	
		
		// empty symmetric difference
		actual = interpreter.eval("? {} | {}");
		assertNotNull("an empty symmetric difference operation should not cause an exception to occur", actual);
		assertTrue("an empty symmetric difference operation should result in an empty set", compareSets(actual, expected));	
	}
	
	@Test
	public void naturalNumbersTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// 0 digit tests
		SetInterface<BigInteger> actual = interpreter.eval("? {02}");
		assertNull("natural numbers cannot start with 0", actual);
		
		actual = interpreter.eval("? {0}");
		ArrayList<BigInteger> expected = convertExpectedList("0");
		assertTrue("0 is a natural number and should thus be accepted", compareSets(actual, expected));
		
		// signed numbers tests
		actual = interpreter.eval("? {-1}");
		assertNull("natural numbers cannot be negative", actual);
		
		actual = interpreter.eval("? {+2}");
		assertNull("natural numbers cannot be signed", actual);
		
		// arbitrarily large number test
		actual = interpreter.eval("?{99999999999999999999999999999999999999999999999999999999999999999999999999}");
		expected = convertExpectedList("99999999999999999999999999999999999999999999999999999999999999999999999999");
		assertTrue("the interpreter should accept any arbritarily large number", compareSets(actual, expected));
	}
	
	@Test
	public void duplicateElementsTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		SetInterface<BigInteger> actual = interpreter.eval("? {1, 2, 3, 2, 1}");
		ArrayList<BigInteger> expected = convertExpectedList("1 2 3");
		assertTrue("a set cannot contain duplicate elements, these need to be removed", compareSets(actual, expected));
		
		actual = interpreter.eval("? {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}");
		expected = convertExpectedList("1");
		assertTrue("a set cannot contain duplicate elements, these need to be removed", compareSets(actual, expected));
	}
	
	@Test
	public void setSyntaxTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// checking whether only natural numbers are accepted
		SetInterface<BigInteger> actual = interpreter.eval("? {a,b,c,d}");
		assertNull("only natural numbers should be accepted in the set", actual);
		
		// missing element test
		actual = interpreter.eval("? { , 2, 3}");
		assertNull("sets with missing elements should be rejected", actual);
		
		// element delimiter tests
		actual = interpreter.eval("? {1.2.3.4}");
		assertNull("only comma's should be accepted as delimiter for the elements in a set", actual);
		
		actual = interpreter.eval("? {1 2 3 4}");
		assertNull("only comma's should be accepted as delimiter for the elements in a set", actual);
		
		// brackets tests
		actual = interpreter.eval("? 1,2,3}");
		assertNull("sets missing an opening bracket should be rejected", actual);
		
		actual = interpreter.eval("? {1,2,3");
		assertNull("sets missing a closing bracket should be rejected", actual);
		
	}

	@Test
	public void operatorsTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// missing operators tests
		SetInterface<BigInteger> actual = interpreter.eval("? ({}({1}))");
		assertNull("statements with operators missing between sets should be rejected", actual);
		
		actual = interpreter.eval("? ( {1} {2} )");
		assertNull("statements with operators missing between sets should be rejected", actual);
		
		// unknown operator test
		actual = interpreter.eval("? {1,2} > {3,4}");
		assertNull("statements with unknown operators between sets should be rejected", actual);
		
	}

	@Test
	public void endOfLineTest() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// Some parsers end an expression when a ')' is seen which should not happen.
		SetInterface<BigInteger> actual = interpreter.eval("? {1,2})");
		assertNull("an expression should not end when the ')' character has been found", actual);
	}
	
	@Test
	public void statementTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// Does the program detect that '=' is missing?
		SetInterface<BigInteger> actual = interpreter.eval("Aap {1,2}");
		assertNull("the program should reject assignments where the '=' sign is missing", actual);
		
		// Will the program detect that the '=' has become a '>'?
		actual = interpreter.eval("Aap > {1,2}");
		assertNull("the program should reject unknown symbols", actual);
		
		// What happens when an expression is missing after the '=' sign?
		interpreter.eval("Aap = ");
		actual = interpreter.getMemory("Aap");
		assertNull("the program should reject assignments that miss an expression", actual);
		
		// What happens when an expression is missing during a print statement?
		actual = interpreter.eval("? ");
		assertNull("the program should reject print statements that miss an expression", actual);
		
	}

	@Test
	public void otherTests() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		// Test of term(): correct factor followed by a wrong factor
		SetInterface<BigInteger> actual = interpreter.eval("?{1}*({1, 2, 3}");
		assertNull("a correct factor followed by a wrong factor should raise an exception", actual);
		
		// Test of expression(): correct term followed by a wrong term
		actual = interpreter.eval("?{1}+({1, 2, 3}");
		assertNull("a correct term followed by a wrong term should raise an exception", actual);
		
		// Does the program see that two different identifiers that start with the same letter are different?
		interpreter.eval("AB = {1,2}");
		interpreter.eval("AC = {3,4}");
		actual = interpreter.getMemory("AB");
		ArrayList<BigInteger> expected = convertExpectedList("1 2");
		assertTrue("the program should be able to see the difference between two identifiers that start with the same letter", compareSets(actual, expected));
	}
	
	public static ArrayList<BigInteger> convertExpectedList(String s) {
		ArrayList<BigInteger> expectedSet = new ArrayList<BigInteger>();

		Scanner numberScanner = new Scanner(s);
		while (numberScanner.hasNextBigInteger()) {
			expectedSet.add(numberScanner.nextBigInteger());
		}

		return expectedSet;
	}
	
	public static boolean compareSets(SetInterface<BigInteger> actual, ArrayList<BigInteger> expected) {
		if (actual == null) {
			return false;
		}
		
		SetInterface<BigInteger> actualCopy = actual.copy();
		ArrayList<BigInteger> expectedClone = (ArrayList<BigInteger>) expected.clone();
				
		while (actualCopy.size() > 0) {
			BigInteger v = actualCopy.get();
			if (!expectedClone.contains(v)) {
				return false;
			}
			actualCopy.remove(v);
			expectedClone.remove(v);
		}
		
		if (!expectedClone.isEmpty()) {
			return false;
		}
		
		return true;
	}
}
