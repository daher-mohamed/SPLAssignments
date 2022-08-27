package bgu.spl.net.api.bidi;

public class Pair<T, T1> {
    private T Key;
    private T1 value;

    public Pair(T key, T1 value) {
        Key = key;
        this.value = value;
    }

    public T getKey() {
        return Key;
    }

    public T1 getValue() {
        return value;
    }

    public void setKey(T key) {
        Key = key;
    }

    public void setValue(T1 value) {
        this.value = value;
    }
}
