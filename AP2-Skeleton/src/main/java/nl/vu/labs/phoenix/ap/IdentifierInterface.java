package nl.vu.labs.phoenix.ap;

public interface IdentifierInterface {
    /*
    Elements: characters of type char saved in a stingbuffer
    Structure: Linear
    Domain: Only alphanumeric characters, identifier begins with a letter,
            identifier has length of at least 1 character

     constructors

        * Identifier(); *
        PRE  -
        *   POST -A new Identifier has been made and contains the character x *
        *
        Identifier
        PRE  -  *
        POST - A new Identifier-object has been made and contains a copy of src.

    */

	void init();
	/*
	 * PRE -
	 * POST - Identifier has one char
	 */

	boolean add(char c);
	/*
	 * PRE -
	 * POST - true: the char falls inside the domain and is added
	 * 		  false: the char falls outside of the domain and isn't added
	 */

	String value();
	/*
	 * PRE -
	 * POST - The string that makes up the identifier has been returned
	 */

	int size();
	/*
	 * PRE -
	 * POST - the size of the Identifier is returned
	 */

	boolean equals(Identifier comparand);
	/*
	 * PRE - the identifiers need to have the same size
	 * POST - true: The identifier is the same as the identifier passed
	 * 		  false: The identifier is not the same as the identifier passed
	 */

}
