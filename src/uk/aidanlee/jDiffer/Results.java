package uk.aidanlee.jDiffer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Results<T> implements Iterable {
    private List<T> items;
    private int count;

    public Results()
    {
        items = new LinkedList<>();
        count = 0;
    }

    //

    public void push(T _value) {
        items.add(_value);
        count++;
    }

    public T get(int _index) {
        if (_index < 0 || _index > count - 1) return null;

        return items.get(_index);
    }

    //

    @Override
    public Iterator<T> iterator() {
        return new ResultsIterator<T>(this);
    }

    public int getLength() {
        return count;
    }
    public int getTotal() {
        return items.size();
    }

    private class ResultsIterator<T> implements Iterator {
        private int index = 0;
        private Results<T> results;

        ResultsIterator(Results<T> _results)
        {
            index   = 0;
            results = _results;
        }

        @Override
        public boolean hasNext() {
            return index < results.getLength();
        }

        @Override
        public Object next() {
            return null;
        }
    }
}
