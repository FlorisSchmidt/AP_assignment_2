package nl.vu.labs.phoenix.ap;

public interface IdentifierInterface {

	/** @elements
	 *    Characters of type char
	 *  @structure
	 *    linear
	 *  @domain
	 *    Only alphanumeric characters. Identifiers begins with a letter.
	 *    Identifier has a length at least 1 character
	 *  @constructor
	 *    There is a default constructor that creates an identifier containing one character.
	 *  @precondition
	 *    --
	 *  @postcondition
	 *    The new Identifier-object and contains the character 'c'.
	 *
	 **/


	/** @precondition
	 *    --
	 *  @postcondition
	 *    Identifier-POST contains only one temporary character. This character will be substituted
	 *    with the first character added after usage of this method.
	 **/
	void init();


	/** @precondition
	 *   --
	 *  @postcondition
	 *    If char coheres to domain requirements, it is present in Identifier-POST
	 *    @return 	true: element has been added.
	 *    			false: element has not been added.
	 *    			failure: IdentifierException
	 *
	 **/
	boolean add(char c) throws IdentifierException;


	/** @precondition
	 *  --
	 *  @postcondition
	 *  @return a String-object with the concatenation of the added chars.
	 **/
	String value();


	/** @precondition
	 *    --
	 *  @postcondition
	 *  @return An integer representing the number of characters in the identifier.
	 **/
	int size();


	/** @precondition
	 *    --
	 *  @postcondition
	 *  @return true: 	The identifier contains the same chars in the same order as the identifier
	 *  			  	passed as argument.
	 *    		false: 	The identifier contains the same chars in the same order as the identifier
	 * 					passed as argument.
	 **/
	boolean equals(Identifier comparand);


}
