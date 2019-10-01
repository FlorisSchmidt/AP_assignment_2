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

	Identifier(Identifier src){
		sb = new StringBuffer();
		addWasCalled = src.addWasCalled;
		sb.append(src.value());

	}

	@Override
	public void init() {
		sb.delete(0,sb.length());
		sb.append('c');
		addWasCalled = false;
	}

	@Override
	public boolean add(char c) {
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

	private boolean testDomain(char c) {
		if(!addWasCalled && Character.isDigit(c)) {
			return false;
		}
		return Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c);
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
