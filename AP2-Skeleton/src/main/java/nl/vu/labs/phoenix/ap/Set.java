package nl.vu.labs.phoenix.ap;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	private LinkedList<T> setList;
	private int size;

	private Set() {
		setList = new LinkedList<>();
		init();
	}

	private Set(ListInterface<T> l) {
		setList = (LinkedList<T>) l.copy();
		size = l.size();
	}

	@Override
	public void init() {
		setList.init();
		size = 0;
	}

	@Override
	public boolean add(T t) {
		if (contains(t)) return false;
		setList.insert(t);
		size++;
		return true;
	}

	@Override
	public T get() {
		T element =  setList.retrieve();
		setList.remove();
		size--;
		return element;
	}

	@Override
	public boolean remove(T t) {
		if (!setList.find(t)) return false;
		else if (t.equals(setList.retrieve())) {
			setList.remove();
			size--;
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(T t) {
		if (setList.find(t)) return true;
		return false;
	}

	@Override
	public SetInterface<T> copy() {
		Set s = new Set(setList);
		return s;
		}


	@Override
	public SetInterface union(SetInterface s) {
		SetInterface<T> union = copy();
		SetInterface<T> subject = s.copy();
		return union;
	}

	@Override
	public SetInterface difference(SetInterface s) {
		Set differences = new Set();
		Set subject = new Set(this);
		for (int i = 0; i < size; i++) {
			Identifier idt = subject.get();
			if (!s.contains(idt)) {
				differences.add(idt);
			}
		}
		return differences;
	}

	@Override
	public SetInterface intersection(SetInterface s) {
		Set intersect = new Set();
		Set subject = new Set(this);
		for (int i = 0; i < size; i++) {
			Identifier idt = subject.get();
			if (s.contains(idt)) {
				intersect.add(idt);
			}
		}
		return intersect;
	}

	@Override
	public SetInterface symDifference(SetInterface s) {
		Set symDif = new Set();
		Set subject = new Set(this);
		Set comparand = new Set(s);
		for (int i = 0; i < size; i++) {
			Identifier subIdt = subject.get();
			Identifier comIdt = comparand.get();
			if (!s.contains(subIdt)) {
				symDif.add(subIdt);
			}
			if (!contains(comIdt)) {
				symDif.add(comIdt);
			}
		}
		return symDif;
	}

}
