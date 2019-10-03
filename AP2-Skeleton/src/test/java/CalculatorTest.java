import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

import nl.vu.labs.phoenix.ap.InterpreterInterface;
import nl.vu.labs.phoenix.ap.SetInterface;
import nl.vu.labs.phoenix.ap.Interpreter;


public class CalculatorTest {
	
	@Test
	public void assignmentTests() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
	
		// empty set
		SetInterface<BigInteger> actual = interpreter.eval("Aap = { }");
		assertNull("eval() should return Null after assignment", actual);
		actual = interpreter.getMemory("Aap");
		assertTrue("Aap = { } should return an empty set", actual.size() == 0);
		
		// normal set
		interpreter.eval("Dirk = {470423, 376329282, 235188141, 9447047, 94,  0, 141, 188  }");
		actual = interpreter.getMemory("Dirk");
		ArrayList<BigInteger> expected = convertExpectedList("0 94 141 188 470423 9447047 235188141 376329282");
		assertTrue("Dirk did not equal the expected set", compareSets(actual, expected));
		
		// referencing test
		interpreter.eval("Beer = Dirk");
		actual = interpreter.getMemory("Beer");
		assertTrue("Beer should equal Dirk", compareSets(actual, expected));
		actual = interpreter.getMemory("Dirk");
		assertTrue("Dirk should equal Beer", compareSets(actual, expected));
		interpreter.eval("Dirk = { }");
		actual = interpreter.getMemory("Beer");
		assertTrue("Beer should not have been emptied like Dirk", compareSets(actual, expected));
		
		// BigIntegers test
		interpreter.eval("Cola = {8939409871034, 0, 10811128, 117529612914, 2867, 2820, 27732726, 2679    }");
		actual = interpreter.getMemory("Cola");
		expected = convertExpectedList("0 2679 2820 2867 10811128 27732726 117529612914 8939409871034");
		assertNotNull("use the BigInteger class instead of int", actual);
		assertTrue("Cola did not equal the expected set", compareSets(actual, expected));	
	}
	
	@Test
	public void printTests() {
		// simple set
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		SetInterface<BigInteger> actual = interpreter.eval("? {1, 2}");
		ArrayList<BigInteger> expected = convertExpectedList("1 2");
		assertNotNull("print statements must force the eval() function to return a set", actual);
		assertTrue("printed set does not equal expected set.", compareSets(actual, expected));
		
		// empty set
		actual = interpreter.eval("? {}");
		assertNotNull("the empty set must still be printed and returned.", actual);
		assertTrue("? {} should return an empty set", actual.size() == 0);
	}
	
	@Test
	public void commentTest() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		SetInterface<BigInteger> actual = interpreter.eval("/ Aap = {1,2}");
		assertNull("a comment should always return null", actual);
		actual = interpreter.getMemory("Aap");
		assertNull("a comment should force the interpreter to ignore the statement", actual);
	}
	
	@Test
	public void intersectionTests() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		interpreter.eval("Aap = {}");
		interpreter.eval("Beer = {470423, 376329282, 235188141, 9447047, 94,  0, 141, 188}");
		interpreter.eval("Cola = {8939409871034, 0, 10811128, 117529612914, 2867, 2820, 27732726, 2679}");
		interpreter.eval("Dirk = {12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42}");
		
		SetInterface<BigInteger> actual = interpreter.eval("? Cola * Dirk");
		ArrayList<BigInteger> expected = convertExpectedList("");
		assertTrue("Cola * Dirk did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Dirk * Cola");
		assertTrue("Dirk * Cola did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Aap * Dirk");
		assertTrue("Aap * Dirk did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Dirk * Aap");
		assertTrue("Dirk * Aap did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Beer * Cola");
		expected = convertExpectedList("0");
		assertTrue("Beer * Cola did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Cola * Beer");
		assertTrue("Cola * Beer did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Dirk * Dirk * Dirk");
		expected = convertExpectedList("12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42");
		assertTrue("Dirk * Dirk * Dirk did not equal Dirk", compareSets(actual, expected));
	}
	
	@Test
	public void unionTests() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		interpreter.eval("Aap = {}");
		interpreter.eval("Beer = {470423, 376329282, 235188141, 9447047, 94,  0, 141, 188}");
		interpreter.eval("Cola = {8939409871034, 0, 10811128, 117529612914, 2867, 2820, 27732726, 2679}");
		interpreter.eval("Dirk = {12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42}");
		
		SetInterface<BigInteger> actual = interpreter.eval("? Beer + Cola");
		ArrayList<BigInteger> expected = convertExpectedList("0 94 141 188 2679 2820 2867 470423 9447047 10811128 27732726 235188141 376329282 117529612914 8939409871034");
		assertTrue("Beer + Cola did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Cola + Beer");
		assertTrue("Cola + Beer did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Dirk + Aap");
		expected = convertExpectedList("12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42");
		assertTrue("Dirk + Aap did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Aap + Dirk");
		assertTrue("Aap + Dirk did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Dirk + Beer");
		expected = convertExpectedList("0 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42 94 141 188 470423 9447047 235188141 376329282");
		assertTrue("Dirk + Beer did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Beer + Dirk");
		assertTrue("Beer + Dirk did not equal the expected set", compareSets(actual, expected));
	}
		
	@Test
	public void complementTests() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		interpreter.eval("Aap = {}");
		interpreter.eval("Beer = {470423, 376329282, 235188141, 9447047, 94,  0, 141, 188}");
		interpreter.eval("Cola = {8939409871034, 0, 10811128, 117529612914, 2867, 2820, 27732726, 2679}");
		interpreter.eval("Dirk = {12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42}");
		
		SetInterface<BigInteger> actual = interpreter.eval("? Cola - Aap");
		ArrayList<BigInteger> expected = convertExpectedList("0 2679 2820 2867 10811128 27732726 117529612914 8939409871034");
		assertTrue("Cola - Aap did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Aap - Cola");
		expected = convertExpectedList("");
		assertTrue("Aap - Cola did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Cola - Beer");
		expected = convertExpectedList("2679 2820 2867 10811128 27732726 117529612914 8939409871034");
		assertTrue("Cola - Beer did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Beer - Cola");
		expected = convertExpectedList("94 141 188 470423 9447047 235188141 376329282");
		assertTrue("Beer - Cola did not equal the expectd set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Cola - Dirk");
		expected = convertExpectedList("0 2679 2820 2867 10811128 27732726 117529612914 8939409871034");
		assertTrue("Cola - Dirk did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Dirk - Cola");
		expected = convertExpectedList("12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42");
		assertTrue("Dirk - Cola did not equal the expceted set", compareSets(actual, expected));
	}
		
	@Test
	public void symmetricDifferenceTests() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		interpreter.eval("Aap = {}");
		interpreter.eval("Beer = {470423, 376329282, 235188141, 9447047, 94,  0, 141, 188}");
		interpreter.eval("Cola = {8939409871034, 0, 10811128, 117529612914, 2867, 2820, 27732726, 2679}");
		interpreter.eval("Dirk = {12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42}");
	
		SetInterface<BigInteger> actual = interpreter.eval("? Aap | Cola");
		ArrayList<BigInteger> expected = convertExpectedList("0 2679 2820 2867 10811128 27732726 117529612914 8939409871034");
		assertTrue("Aap | Cola did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Cola | Aap");
		assertTrue("Cola | Aap did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Dirk | Cola");
		expected = convertExpectedList("0 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42 2679 2820 2867 10811128 27732726 117529612914 8939409871034");
		assertTrue("Dirk | Cola did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Cola | Dirk");
		assertTrue("Cola | Dirk did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Beer | Cola");
		expected = convertExpectedList("94 141 188 2679 2820 2867 470423 9447047 10811128 27732726 235188141 376329282 117529612914 8939409871034");
		assertTrue("Beer | Cola did not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Cola | Beer");
		assertTrue("Cola | Beer did not equal the expected set", compareSets(actual, expected));
	}
	
	@Test
	public void expressionEvaluationTests() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		interpreter.eval("Erik = {3948, 3901, 3854, 3807, 3760, 3713, 3666, 3619, 3572, 3525}");
		interpreter.eval("Beer = {470423, 376329282, 235188141, 9447047, 94,  0, 141, 188}");
		interpreter.eval("Cola = {8939409871034, 0, 10811128, 117529612914, 2867, 2820, 27732726, 2679}");
		interpreter.eval("Zeven = {63, 70, 77, 84, 91, 98, 105, 112, 119, 126, 133, 140, 147, 154, 161, 168, 175, 182, 189, 196, 203, 210, 217, 224, 231, 238, 245, 252, 259, 266, 273, 280, 287, 294, 301, 308, 315, 322, 329, 336, 343  }");
		interpreter.eval("Yeti = {45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, 155, 160, 165, 170, 175, 180, 185, 190, 195, 200, 205, 210, 215, 220, 225, 230, 235, 240, 245  }");
		interpreter.eval("Xorn = {27, 30, 33, 36, 39, 42, 45, 48, 51, 54, 57, 60, 63, 66, 69, 72, 75, 78, 81, 84, 87, 90, 93, 96, 99, 102, 105, 108, 111, 114, 117, 120, 123, 126, 129, 132, 135, 138, 141, 144, 147  }");
		interpreter.eval("Worm = {18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98  }");
		
		// parenthesis handling test
		SetInterface<BigInteger> actual = interpreter.eval("? (((((((( { 20 } ))))))))");
		ArrayList<BigInteger> expected = convertExpectedList("20");
		assertTrue("parenthesis aren't handled correctly", compareSets(actual, expected));
		
		// left associativity tests
		actual = interpreter.eval("? Cola - Beer - Erik");
		expected = convertExpectedList("2679 2820 2867 10811128 27732726 117529612914 8939409871034");
		assertTrue("Cola - Beer - Erik does not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Worm + Xorn + Yeti + Zeven");
		expected = convertExpectedList("18 20 22 24 26 27 28 30 32 33 34 36 38 39 40 42 44 45 46 48 50 51 52 54 55 56 57 58 60 62 63 64 65 66 68 69 70 72 74 75 76 77 78 80 81 82 84 85 86 87 88 90 91 92 93 94 95 96 98 99 100 102 105 108 110 111 112 114 115 117 119 120 123 125 126 129 130 132 133 135 138 140 141 144 145 147 150 154 155 160 161 165 168 170 175 180 182 185 189 190 195 196 200 203 205 210 215 217 220 224 225 230 231 235 238 240 245 252 259 266 273 280 287 294 301 308 315 322 329 336 343");
		assertTrue("Worm + Xorn + Yeti + Zeven does not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Worm * Xorn * Yeti * Zeven");
		expected = convertExpectedList("");
		assertTrue("Worm * Xorn * Yeti * Zeven does not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Worm | Xorn | Yeti | Zeven");
		expected = convertExpectedList("18 20 22 24 26 27 28 32 33 34 38 39 40 44 46 51 52 55 56 57 58 60 62 64 65 68 69 70 74 76 77 81 82 84 85 86 87 88 90 91 92 93 94 95 99 100 102 105 108 110 111 112 114 115 117 119 123 125 129 130 132 133 138 141 144 145 150 154 155 160 161 165 168 170 180 182 185 189 190 195 196 200 203 205 215 217 220 224 225 230 231 235 238 240 252 259 266 273 280 287 294 301 308 315 322 329 336 343");
		assertTrue("Worm | Xorn | Yeti | Zeven does not equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? Worm - Xorn - Yeti - Zeven");
		expected = convertExpectedList("18 20 22 24 26 28 32 34 38 40 44 46 52 56 58 62 64 68 74 76 82 86 88 92 94");
		assertTrue("Worm - Xorn - Yeti - Zeven does not equal the expected set", compareSets(actual, expected));
		
		interpreter.eval("Vat = Worm + Xorn + Yeti + Zeven");
		interpreter.eval("Vat = Vat * Worm * Yeti * Xorn * Zeven");
		actual = interpreter.getMemory("Vat");
		expected = convertExpectedList("");
		assertTrue("Vat does not equal the expected set", compareSets(actual, expected));
		
		// priority test
		actual = interpreter.eval("? Worm + Xorn | Yeti * Zeven");
		expected = convertExpectedList("18 20 22 24 26 27 28 30 32 33 34 36 38 39 40 42 44 45 46 48 50 51 52 54 56 57 58 60 62 63 64 66 68 69 72 74 75 76 78 80 81 82 84 86 87 88 90 92 93 94 96 98 99 102 108 111 114 117 120 123 126 129 132 135 138 140 141 144 147 175 210 245");
		assertTrue("Worm + Xorn | Yeti * Zeven does not equal the expected set", compareSets(actual, expected));
	}
		
	@Test
	public void complicatedExpressionsTests() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		SetInterface<BigInteger> actual = interpreter.eval("? { 12, 18, 13, 1, 14 } + { 100, 400, 200 } * { 300, 100, 200 }");
		ArrayList<BigInteger> expected = convertExpectedList("1 12 13 14 18 100 200");
		assertTrue("printed statement doesn't equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? ( { 12, 18, 13, 1, 14 } + { 100, 400, 200 } ) * { 300, 100, 200 }");
		expected = convertExpectedList("100 200");
		assertTrue("printed statement doesn't equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? ((( { 300 } + { 200 } ) * { 300 } ) - { 300 } )");
		expected = convertExpectedList("");
		assertTrue("printed statement doesn't equal the expected set", compareSets(actual, expected));
		
		actual = interpreter.eval("? (((( { 1 } ))) + (((( { 0 } )))))");
		expected = convertExpectedList("0 1");
		assertTrue("printed statement doesn't equal the expected set", compareSets(actual, expected));
	}
	
	@Test
	public void ReinoutVanSchouwenTest() {
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		interpreter.eval("rein = {1, 2, 5}");
		interpreter.eval("rein = {1, 2, 5, 16} - {2, 3, 4, 5}");
		SetInterface<BigInteger> actual = interpreter.getMemory("rein");
		ArrayList<BigInteger> expected = convertExpectedList("1 16");
		assertTrue("rein did not equal the expected set", compareSets(actual, expected));
		
		interpreter.eval("out = rein");
		
		// now performing the same operations might go wrong.
		interpreter.eval("rein = {1, 2, 5}");
		interpreter.eval("rein = {1, 2, 5, 16} - {2, 3, 4, 5}");
		actual = interpreter.getMemory("rein");
		assertTrue("rein did not equal the expected set", compareSets(actual, expected));
	}
	
	@Test
	public void ElineVanMantgemTest() {
		// the following test will fail if compareTo() had been
		// used incorrectly.
		
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		interpreter.eval("A={1234567890, 2625242322}");
		interpreter.eval("B={2}");
		interpreter.eval("C=(A|B)+(A|B)");
		
		SetInterface<BigInteger> actual = interpreter.getMemory("C");
		ArrayList<BigInteger> expected = convertExpectedList("2 1234567890 2625242322");
		assertTrue("C did not equal the expected set", compareSets(actual, expected));
		
	}
	
	@Test
	public void MaartenVanDerMeulenTest() {
		// now let the program really work!
		
		InterpreterInterface<SetInterface<BigInteger>> interpreter = new Interpreter<SetInterface<BigInteger>>();
		
		interpreter.eval("Aap={10099, 9897, 9695, 9493, 9291, 9089, 8887, 8685, 8483, 8281, 8079, 7877, 7675, 7473, 7271, 7069, 6867, 6665, 6463, 6261, 6059, 5857, 5655, 5453, 5251, 5049, 4847, 4645, 444342, 41439, 38373635, 3433321, 30292827, 2625242322, 212019181716, 1514, 1312, 1110987654, 321, 0,     5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100}");
		interpreter.eval("Beer={99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250}");
		interpreter.eval("Cola=((Aap+Beer)-Aap)+((Aap+Beer)-Aap)+((Aap+Beer)-Aap)+((Aap+Beer)-Aap)+(Aap*Beer)+(Aap|Beer)+(Aap*Beer)+(Aap|Beer)+(Aap*Beer)+(Aap|Beer)");
		
		SetInterface<BigInteger> actual = interpreter.getMemory("Cola");
		ArrayList<BigInteger> expected = convertExpectedList("0 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100 101 102 103 104 105 106 107 108 109 110 111 112 113 114 115 116 117 118 119 120 121 122 123 124 125 126 127 128 129 130 131 132 133 134 135 136 137 138 139 140 141 142 143 144 145 146 147 148 149 150 151 152 153 154 155 156 157 158 159 160 161 162 163 164 165 166 167 168 169 170 171 172 173 174 175 176 177 178 179 180 181 182 183 184 185 186 187 188 189 190 191 192 193 194 195 196 197 198 199 200 201 202 203 204 205 206 207 208 209 210 211 212 213 214 215 216 217 218 219 220 221 222 223 224 225 226 227 228 229 230 231 232 233 234 235 236 237 238 239 240 241 242 243 244 245 246 247 248 249 250 321 1312 1514 4645 4847 5049 5251 5453 5655 5857 6059 6261 6463 6665 6867 7069 7271 7473 7675 7877 8079 8281 8483 8685 8887 9089 9291 9493 9695 9897 10099 41439 444342 3433321 30292827 38373635 1110987654 2625242322 212019181716");
		assertTrue("Cola did not equal the expected set", compareSets(actual, expected));
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
