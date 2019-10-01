package nl.vu.labs.phoenix.ap;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	private LinkedList<T> setList;
	private int size;

	Set() {
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
		return setList.find(t);
	}

	@Override
	public SetInterface<T> copy() {
		return new Set<T>(setList);
		}


	@Override
	public SetInterface<T> union(SetInterface<T> s) {
		SetInterface<T> union = copy();
		SetInterface<T> subject = s.copy();
		for (int i = 0; i < s.size(); i++) {
			T type = subject.get();
			union.add(type);
		}
		return union;
	}

	@Override
	public SetInterface<T> difference(SetInterface<T> s) {
		Set<T> differences = new Set<>();
		Set<T> subject = new Set<>(setList);
		for (int i = 0; i < size; i++) {
			T element = subject.get();
			if (!s.contains(element)) {
				differences.add(element);
			}
		}
		return differences;
	}

	@Override
	public SetInterface<T> intersection(SetInterface<T> s) {
		Set<T> intersect = new Set<>();
		Set<T> subject = new Set<>(setList);
		for (int i = 0; i < size; i++) {
			T element = subject.get();
			if (s.contains(element)) {
				intersect.add(element);
			}
		}
		return intersect;
	}

	@Override
	public SetInterface<T> symDifference(SetInterface<T> s) {
		Set<T> symDif = new Set<>();
		Set<T> subject = new Set<>(setList);
		Set<T> comparand = (Set<T>) s.copy();
		for (int i = 0; i < size; i++) {
			T subElement = subject.get();
			if (!s.contains(subElement)) {
				symDif.add(subElement);
			}
		}
		for (int j = 0; j< s.size(); j++) {
			T compElement = comparand.get();
			if (!contains(compElement)) {
				symDif.add(compElement);
			}
		}
		return symDif;
	}

}
