package nl.vu.labs.phoenix.ap;

/* [1] Set Specification
 * 	   -- Complete the specification for a set interface.
 * 		  See the List interface for inspiration
 */
public interface SetInterface<T> {
	
	/* 
	 * [2] Mandatory methods. Make sure you do not modify these!
	 * 	   -- Complete the specifications of these methods
	 */
	
	/**
	 * Hint:
	 * @return
	 * 	true  - element was inserted
	 * 	false - element was already present 
	 */
	boolean add(T t);
	
	T get();
	
	boolean remove(T t);
	
	int size();
	
	SetInterface<T> copy();
	
	/*
	 * [3] Methods for set operations 
	 * 	   -- Add methods to perform the 4 basic set operations 
	 * 		  (union, intersection, difference, symmetric difference)
	 */
	
	
	// your code here
	
	
	/* 
	 * [4] Add anything else you think belongs to this interface 
	 */
	
	// your code here
	
}
