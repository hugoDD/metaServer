package cn.granitech.web.pojo;

import java.io.Serializable;
import java.util.Map;

public class KeyValueEntry<K, V> implements Map.Entry<K, V>, Serializable {
    private static final long serialVersionUID = -8499721149061103585L;
    private final K key;
    private V value;

    public KeyValueEntry() {
        this.key = null;
        this.value = null;
    }

    public KeyValueEntry(K key2, V value2) {
        this.key = key2;
        this.value = value2;
    }

    public KeyValueEntry(Map.Entry<? extends K, ? extends V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public V setValue(V value2) {
        V oldValue = this.value;
        this.value = value2;
        return oldValue;
    }

    private boolean eq(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> e = (Map.Entry) o;
        return eq(this.key, e.getKey()) && eq(this.value, e.getValue());
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.key == null ? 0 : this.key.hashCode();
        if (this.value != null) {
            i = this.value.hashCode();
        }
        return hashCode ^ i;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }
}
