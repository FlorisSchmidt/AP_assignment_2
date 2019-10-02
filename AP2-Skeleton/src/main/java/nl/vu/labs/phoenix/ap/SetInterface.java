package nl.vu.labs.phoenix.ap;

/*
 * Elements: Objects of type T.
 * Structure: none
 * Domain: All elements must be unique.
 *
 * constructors
 *
 * Set();
 *   PRE  -
 *   POST - A new Set-object has been made and contains the empty set.
 *
 */
public interface SetInterface<T extends Comparable<T>> {

	/*  PRE -
       POST - The set is empty
    */
	void init();

	/*  PRE - The size of the set is less than 20
        POST - The element has been added to the set
               @return  True: if element has been added
                        False: if element has not been added
     */
	boolean add(T t);


	/*  PRE - The set is not empty
        POST - Returns and removes an element from the set
     */
	T get();

	/*  PRE - The set is not empty
        POST - Returns and removes specified element from the set
     */
	boolean remove(T t);

	/*  PRE -
       POST - The amount of elements in the set has been returned
    */
	int size();

	boolean contains(T t);

	SetInterface<T> copy();

	SetInterface<T> union(SetInterface<T> s);
    /*  PRE -
        POST - A set containing the union of the argument set and this.
     */

	SetInterface<T> difference(SetInterface<T> s);
    /*  PRE -
        POST - A set containing the difference of the argument and this.
     */

	SetInterface<T> intersection(SetInterface<T> s);
    /*  PRE -
        POST - A set containing the intersection of the argument and this.
     */

	SetInterface<T> symDifference(SetInterface<T> s);
    /*  PRE -
        POST - A set containing the symmetric difference between the argument set
     */
}

