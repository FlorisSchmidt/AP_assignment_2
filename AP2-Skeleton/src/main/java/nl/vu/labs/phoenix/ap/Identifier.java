package nl.vu.labs.phoenix.ap;

import java.lang.StringBuffer;
import java.lang.Character;

public class Identifier implements IdentifierInterface {

	private StringBuffer sb;
	private boolean addWasCalled;

	Identifier(){
		sb = new StringBuffer();
		init();
	}

	@Override
	public void init() {
		sb.delete(0,sb.length());
		sb.append('c');
		addWasCalled = false;
	}

	@Override
	public boolean add(char c) throws IdentifierException{
		if(testDomain(c) && addWasCalled) {
			sb.append(c);
			return true;
		}
		if(testDomain(c) && !addWasCalled) {
			sb.delete(0, 1);
			sb.append(c);
			addWasCalled = true;
			return true;
		}
		return false;
	}

	private boolean testDomain(char c) throws IdentifierException {
		if(!addWasCalled && Character.isDigit(c)) {
			throw new IdentifierException("first element can't be a digit");
		}
		if(!Character.isDigit(c) && !Character.isLowerCase(c) && !Character.isUpperCase(c)){ //TODO this is never called!
			throw new IdentifierException("unsupported character '" + c + "'");
		}
		return true;
	}

	@Override
	public String value() {
		return sb.toString();
	}

	@Override
	public int size() {
		return sb.length();
	}

	@Override
	public boolean equals(Identifier comparand) {
		return this.value().equals(comparand.value());
	}
}
