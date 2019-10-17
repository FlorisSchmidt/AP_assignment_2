package nl.vu.labs.phoenix.ap;

import java.lang.StringBuffer;

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
	public boolean add(char c){
		if(addWasCalled) {
			sb.append(c);
		} else {
			sb.delete(0, 1);
			sb.append(c);
			addWasCalled = true;
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
