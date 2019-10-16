package nl.vu.labs.phoenix.ap;

/** @elements
 *    objects of type T
 *  @structure
 *    none
 *  @domain
 *    All elements are unique.
 *  @constructor
 *    There is a default constructor that creates an empty set
 *  @precondition
 *    --
 *  @postcondition
 *    The new Set-object is the empty set
 *
 **/
public interface SetInterface<T extends Comparable<T>> {

	/** @precondition
	 *    --
	 *  @postcondition
	 *    set-POST is empty and has been returned.
	 **/
	void init();


	/** @precondition
	 *   --
	 *  @postcondition
	 *    Element t is present in set-POST
	 *    @return 	true: element has been added.
	 *    			false: element has not been added because it was already present.
	 *
	 **/
	boolean add(T t);


	/** @precondition
	 *  --
	 * @postcondition
	 * @return an element in the set.
	 */
	T get();

	/** @precondition
	 *    The set is not empty.
	 *  @postcondition
	 *    Element t is not contained in Set-POST
	 * @return  true: The element passed as argument was contained in Set-PRE and has been removed.
	 * 			false: The element passed as argument was not contained in Set-PRE and has not been removed.
	 *
	 **/
	boolean remove(T t);


	/** @precondition
	 *    --
	 *  @postcondition
	 *    The number of elements in the set has been returned.
	 **/
	int size();

	/** @precondition
	 *    --
	 *  @postcondition
	 *  @return true: The set contains the identifier passed as argument.
	 *    		false: The set does not contain identifier passed as argument.
	 **/
	boolean contains(T t);


	/** @precondition
	 *    --
	 *  @postcondition
	 *    A copy of the set has been returned.
	 */
	SetInterface<T> copy();


	/** @precondition
	 *    --
	 *  @postcondition
	 *  @return a set containing the union of the argument and this.
	 */
	SetInterface<T> union(SetInterface<T> s);


	/** @precondition
	 *    --
	 *  @postcondition
	 *  @return a set containing the difference of the argument and this.
	 */
	SetInterface<T> difference(SetInterface<T> s);


	/** @precondition
	 *    --
	 *  @postcondition
	 *  @return a set containing the intersection of the argument and this.
	 */
	SetInterface<T> intersection(SetInterface<T> s);


	/** @precondition
	 *    --
	 *  @postcondition
	 *  @return a set containing the symmetric difference of the argument and this.
	 */
	SetInterface<T> symDifference(SetInterface<T> s);

}

