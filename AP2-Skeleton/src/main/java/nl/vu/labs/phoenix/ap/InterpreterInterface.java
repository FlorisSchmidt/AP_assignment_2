package nl.vu.labs.phoenix.ap;

/**
 * Do not modify this interface
 */
public interface InterpreterInterface<T> {
	
	/**
	 * Retrieve the value of an identifier from the memory
	 * @param var 		value of an Identifier
	 * @return 
	 * 	the set corresponding to var or null
	 */
	T getMemory(String var);
	
	/**
	 * Evaluate a line of input
	 * @param s 		an expression
	 * @return
	 * 	if the statement is a print return the corresponding set
	 * 	otherwise return null. also return null when an exception occurs (after printing it out!)
	 */
	T eval(String s);
}
