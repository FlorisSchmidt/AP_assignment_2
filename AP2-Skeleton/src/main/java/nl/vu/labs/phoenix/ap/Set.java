package nl.vu.labs.phoenix.ap;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	private ListInterface<T> setList;
	private int size;

	Set() {
		setList = new LinkedList<>();
		init();
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
		return setList.retrieve();
	}

	@Override
	public boolean remove(T t) {
		if (!setList.find(t)) return false;
		if (t.equals(setList.retrieve())) {
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
		Set setCopy = new Set<T>();
		setCopy.setList = this.setList.copy();
		setCopy.size = this.size;
		return setCopy;
		}


	@Override
	public SetInterface<T> union(SetInterface<T> s) {
		SetInterface<T> union = copy();
		SetInterface<T> subject = s.copy();
		for (int i = 0; i < s.size(); i++) {
			T element = subject.get();
			subject.remove(element);
			union.add(element);
		}
		return union;
	}

	@Override
	public SetInterface<T> difference(SetInterface<T> s) {
		Set<T> differences = new Set<>();
		SetInterface<T> subject = this.copy();
		for (int i = 0; i < size; i++) {
			T element = subject.get();
			subject.remove(element);
			if (!s.contains(element)) {
				differences.add(element);
			}
		}
		return differences;
	}

	@Override
	public SetInterface<T> intersection(SetInterface<T> s) {
		Set<T> intersect = new Set<>();
		SetInterface<T> subject = this.copy();
		for (int i = 0; i < size; i++) {
			T element = subject.get();
			subject.remove(element);
			if (s.contains(element)) {
				intersect.add(element);
			}
		}
		return intersect;
	}

	@Override
	public SetInterface<T> symDifference(SetInterface<T> s) {
		Set<T> symDif = new Set<>();
		SetInterface<T> subject = this.copy();
		Set<T> comparand = (Set<T>) s.copy();
		for (int i = 0; i < size; i++) {
			T subjectElement = subject.get();
			subject.remove(subjectElement);
			if (!s.contains(subjectElement)) {
				symDif.add(subjectElement);
			}
		}
		for (int j = 0; j< s.size(); j++) {
			T comparandElement = comparand.get();
			comparand.remove(comparandElement);
			if (!contains(comparandElement)) {
				symDif.add(comparandElement);
			}
		}
		return symDif;
	}

}
