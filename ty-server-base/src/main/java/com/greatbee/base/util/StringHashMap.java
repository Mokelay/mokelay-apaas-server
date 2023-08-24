package com.greatbee.base.util;

import com.greatbee.base.util.io.CodecUtil;
import com.greatbee.base.util.io.Marshallable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class StringHashMap<V>
    extends AbstractStringMap<V>
    implements Marshallable, Cloneable
{
    /**
     * The default initial capacity - MUST be a power of two.
     */
    public static final int INITIAL_SIZE = 16;

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    transient Entry<V>[] table;

    /**
     * The number of key-value mappings contained in this identity hash map.
     */
    transient int size;

    /**
     * The next size value at which to resize (capacity * load factor).
     *
     * @serial
     */
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    float loadFactor;

    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     */
    transient volatile int modCount;


    public StringHashMap(int initialCapacity, float loadFactor)
    {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Initial Capacity: " +
                                               initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal Load factor: " +
                                               loadFactor);
        }
        // Find a power of 2 >= initialCapacity
        this.loadFactor = loadFactor;
        init(initialCapacity);
    }

    private void init(int initialCapacity)
    {
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        if (loadFactor == 0) {
            loadFactor = DEFAULT_LOAD_FACTOR;
        }
        threshold = (int)(capacity * loadFactor);
        table = (Entry<V>[])new Entry[capacity];
    }

    public StringHashMap(int initialCapacity)
    {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public StringHashMap()
    {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int)(INITIAL_SIZE * loadFactor);
        table = (Entry<V>[])new Entry[INITIAL_SIZE];
    }

    /**
     * Constructs a new map with the same mappings as the given map.  The
     * map is created with a capacity of twice the number of mappings in
     * the given map or 11 (whichever is greater), and a default load factor,
     * which is <tt>0.75</tt>.
     *
     * @param m the map whose mappings are to be placed in this map.
     */
    public StringHashMap(StringMap<V> m)
    {
        this(Math.max((int)(m.size() / DEFAULT_LOAD_FACTOR) + 1,
            INITIAL_SIZE), DEFAULT_LOAD_FACTOR);
        putAllForCreate(m);
    }

    /**
     * Returns index for hash code h.
     */
    static int indexFor(int h, int length)
    {
        return h & (length - 1);
    }

    /**
     * This method is used instead of put by constructors and
     * pseudoconstructors (clone, readObject).  It does not resize the table,
     * check for comodification, etc.  It calls createEntry rather than
     * addEntry.
     */
    private void putForCreate(String key, V value)
    {
        String k = maskNull(key);
        int hash = hashCode(k);
        int i = indexFor(hash, table.length);

        /**
         * Look for preexisting entry for key.  This will never happen for
         * clone or deserialize.  It will only happen for construction if the
         * input Map is a sorted map whose ordering is inconsistent w/ equals.
         */
        for (Entry<V> e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && equals(k, e.key)) {
                e.value = value;
                return;
            }
        }

        createEntry(hash, k, value, i);
    }

    /**
     * Like addEntry except that this version is used when creating entries
     * as part of Map construction or "pseudo-construction" (cloning,
     * deserialization).  This version needn't worry about resizing the table.
     *
     * Subclass overrides this to alter the behavior of HashMap(Map),
     * clone, and readObject.
     */
    void createEntry(int hash, String key, V value, int bucketIndex)
    {
        table[bucketIndex] = new Entry<V>(hash, key, value, table[bucketIndex]);
//        table[bucketIndex] = createEntry(hash, key, value, table[bucketIndex]);
        size++;
    }

    void putAllForCreate(StringMap<V> m)
    {
        for (StringMap.Entry<V> e : m.entrySet()) {
            putForCreate(e.getKey(), e.getValue());
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map.
     */
    public int size()
    {
        return size;
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings.
     */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.
     *
     * @param value value whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map maps one or more keys to the
     *         specified value.
     */
    public boolean containsValue(Object value)
    {
        if (value == null) {
            return containsNullValue();
        }

        Entry tab[] = table;
        for (int i = 0; i < tab.length; i++) {
            for (Entry e = tab[i]; e != null; e = e.next) {
                if (value.equals(e.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Special-case code for containsValue with null argument
     */
    private boolean containsNullValue()
    {
        Entry tab[] = table;
        for (int i = 0; i < tab.length; i++) {
            for (Entry e = tab[i]; e != null; e = e.next) {
                if (e.value == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsKey(String key)
    {
        String k = maskNull(key);
        int hash = hashCode(k);
        int i = indexFor(hash, table.length);
        Entry e = table[i];
        while (e != null) {
            if (e.hash == hash && equals(k, e.key)) {
                return true;
            }
            e = e.next;
        }
        return false;
    }

    public V get(String key)
    {
        String k = maskNull(key);
        int hash = hashCode(k);
        int i = indexFor(hash, table.length);
        Entry<V> e = table[i];
        while (true) {
            if (e == null) {
                return null;
            }
            if (e.hash == hash && equals(k, e.key)) {
                return e.value;
            }
            e = e.next;
        }
    }

    public String getString(String name)
    {
        return StringUtil.getString(get(name));
    }

    public String getString(String name, String defValue)
    {
        return StringUtil.getString(get(name), defValue);
    }

    /**
     * Add a new entry with the specified key, value and hash code to
     * the specified bucket.  It is the responsibility of this
     * method to resize the table if appropriate.
     *
     * Subclass overrides this to alter the behavior of put method.
     */
    void addEntry(int hash, String key, V value, int bucketIndex)
    {
//        table[bucketIndex] = createEntry(hash, key, value, table[bucketIndex]);
        table[bucketIndex] = new Entry<V>(hash, key, value, table[bucketIndex]);
        if (size++ >= threshold) {
            resize(2 * table.length);
        }
    }

    /**
     * Rehashes the contents of this map into a new array with a
     * larger capacity.  This method is called automatically when the
     * number of keys in this map reaches its threshold.
     *
     * If current capacity is MAXIMUM_CAPACITY, this method does not
     * resize the map, but but sets threshold to Integer.MAX_VALUE.
     * This has the effect of preventing future calls.
     *
     * @param newCapacity the new capacity, MUST be a power of two;
     *                    must be greater than current capacity unless current
     *                    capacity is MAXIMUM_CAPACITY (in which case value
     *                    is irrelevant).
     */
    void resize(int newCapacity)
    {
        Entry<V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry<V>[] newTable = (Entry<V>[])new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }

    /**
     * Transfer all entries from current table to newTable.
     */
    void transfer(Entry[] newTable)
    {
        Entry[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                }
                while (e != null);
            }
        }
    }

    public V put(String key, V value)
    {
        String k = maskNull(key);
        int hash = hashCode(k);
        int i = indexFor(hash, table.length);

        for (Entry<V> e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && equals(k, e.key)) {
                V oldValue = e.value;
                e.value = value;
//                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, k, value, i);
        return null;
    }

    /**
     * Value representing null keys inside tables.
     */
    protected static final String NULL_KEY = new StringBuilder("__NULL_").append("_KEY__").toString();

    /**
     * Returns internal representation for key. Use NULL_KEY if key is null.
     */
    protected static String maskNull(String key)
    {
        return (key == null ? NULL_KEY : key);
    }

    /**
     * Returns key represented by specified internal representation.
     */
    protected static String unmaskNull(String key)
    {
        return (key == NULL_KEY ? null : key);
    }

    /**
     * ��ϣ
     */
    protected int hashCode(String key)
    {
        int h = key.hashCode();
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        h ^= (h >>> 10);
        return h;
    }

    /**
     * �Ƿ����
     */
    protected boolean equals(String x, String y)
    {
        return (x == y || (x.equals(y) && (x != NULL_KEY && y != NULL_KEY)));
    }

    public V remove(String key)
    {
        Entry<V> e = removeEntryForKey(key);
        return (e == null ? null : e.value);
    }

    /**
     * Removes and returns the entry associated with the specified key
     * in the HashMap.  Returns null if the HashMap contains no mapping
     * for this key.
     */
    Entry<V> removeEntryForKey(String key)
    {
        String k = maskNull(key);
        int hash = hashCode(k);
        int i = indexFor(hash, table.length);
        Entry<V> prev = table[i];
        Entry<V> e = prev;

        while (e != null) {
            Entry<V> next = e.next;
            if (e.hash == hash && equals(k, e.key)) {
                modCount++;
                size--;
                if (prev == e) {
                    table[i] = next;
                }
                else {
                    prev.next = next;
                }
//                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }

    /**
     * Copies all of the mappings from the specified map to this one.
     *
     * These mappings replace any mappings that this map had for any of the
     * keys currently in the specified Map.
     *
     * @param map Mappings to be stored in this map.
     */
    public void putAll(StringMap<? extends V> map)
    {
        int numKeysToBeAdded = map.size();
        if (numKeysToBeAdded == 0) {
            return;
        }

        /*
         * Expand the map if the map if the number of mappings to be added
         * is greater than or equal to threshold.  This is conservative; the
         * obvious condition is (m.size() + size) >= threshold, but this
         * condition could result in a map with twice the appropriate capacity,
         * if the keys to be added overlap with the keys already in this map.
         * By using the conservative calculation, we subject ourself
         * to at most one extra resize.
         */
        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int)(numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > MAXIMUM_CAPACITY) {
                targetCapacity = MAXIMUM_CAPACITY;
            }
            int newCapacity = table.length;
            while (newCapacity < targetCapacity) {
                newCapacity <<= 1;
            }
            if (newCapacity > table.length) {
                resize(newCapacity);
            }
        }

        for (StringMap.Entry<? extends V> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * ����<code>StringMap</code>�����еĲ���
     *
     * @param map [StringMap]
     */
    public void putAll(Map<String, ? extends V> map)
    {
        int numKeysToBeAdded = map.size();
        if (numKeysToBeAdded == 0) {
            return;
        }

        /*
         * Expand the map if the map if the number of mappings to be added
         * is greater than or equal to threshold.  This is conservative; the
         * obvious condition is (m.size() + size) >= threshold, but this
         * condition could result in a map with twice the appropriate capacity,
         * if the keys to be added overlap with the keys already in this map.
         * By using the conservative calculation, we subject ourself
         * to at most one extra resize.
         */
        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int)(numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > MAXIMUM_CAPACITY) {
                targetCapacity = MAXIMUM_CAPACITY;
            }
            int newCapacity = table.length;
            while (newCapacity < targetCapacity) {
                newCapacity <<= 1;
            }
            if (newCapacity > table.length) {
                resize(newCapacity);
            }
        }

        for (Map.Entry<String, ? extends V> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * Removes all mappings from this map.
     */
    public void clear()
    {
        modCount++;
        Entry tab[] = table;
        for (int i = 0; i < tab.length; i++) {
            tab[i] = null;
        }
        size = 0;
    }

    /**
     * Returns a shallow copy of this <tt>StringHashMap</tt> instance: the keys and
     * values themselves are not cloned.
     *
     * @return a shallow copy of this map.
     */
    public Object clone() throws CloneNotSupportedException
    {
        StringHashMap<V> result = (StringHashMap<V>)super.clone();
        result.table = (Entry<V>[])new Entry[table.length];
        result.entrySet = null;
        result.modCount = 0;
        result.size = 0;
        result.putAllForCreate(this);
        return result;
    }

//    /**
//     * Returns a set view of the keys contained in this map.  The set is
//     * backed by the map, so changes to the map are reflected in the set, and
//     * vice-versa.  The set supports element removal, which removes the
//     * corresponding mapping from this map, via the <tt>Iterator.remove</tt>,
//     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
//     * <tt>clear</tt> operations.  It does not support the <tt>add</tt> or
//     * <tt>addAll</tt> operations.
//     *
//     * @return a set view of the keys contained in this map.
//     */
//    public Iterator<String> keys()
//    {
//        return getKeys();
//    }

    /**
     * �������м�
     */
    public Iterator<String> getKeys()
    {
        return newKeyIterator();
    }

//    public Iterator<V> values()
//    {
//        return getValues();
//    }

    /**
     * ��������ֵ
     */
    public Iterator<V> getValues()
    {
        return newValueIterator();
    }

//    public Iterator<StringMap.Entry<V>> entries()
//    {
//        return getEntries();
//    }

    /**
     */
    public Iterator<StringMap.Entry<V>> getEntries()
    {
        return newEntryIterator();
    }

    // Views

    private transient Set<String> keySet = null;
    private transient Set<StringMap.Entry<V>> entrySet = null;
    private transient Collection<V> valueSet = null;

    /**
     * Returns a set view of the keys contained in this map.  The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa.  The set supports element removal, which removes the
     * corresponding mapping from this map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
     * <tt>clear</tt> operations.  It does not support the <tt>add</tt> or
     * <tt>addAll</tt> operations.
     *
     * @return a set view of the keys contained in this map.
     */
    public Set<String> keySet()
    {
        Set<String> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }

    private class KeySet extends AbstractSet
    {
        public Iterator iterator()
        {
            return newKeyIterator();
        }

        public int size()
        {
            return size;
        }

        public boolean contains(Object o)
        {
            return containsKey((String)o);
        }

        public boolean remove(Object o)
        {
            return StringHashMap.this.removeEntryForKey((String)o) != null;
        }

        public void clear()
        {
            StringHashMap.this.clear();
        }
    }

    /**
     * Returns a collection view of the values contained in this map.  The
     * collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from this map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map.
     */
    public Collection<V> valueSet()
    {
        Collection<V> vs = valueSet;
        return (vs != null ? vs : (valueSet = new Values()));
    }

    private class Values extends AbstractCollection
    {
        public Iterator iterator()
        {
            return newValueIterator();
        }

        public int size()
        {
            return size;
        }

        public boolean contains(Object o)
        {
            return containsValue(o);
        }

        public void clear()
        {
            StringHashMap.this.clear();
        }
    }

    /**
     * Returns a collection view of the mappings contained in this map.  Each
     * element in the returned collection is a <tt>Map.Entry</tt>.  The
     * collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the mappings contained in this map.
     * @see StringMap.Entry
     */
    public Set<StringMap.Entry<V>> entrySet()
    {
        Set<StringMap.Entry<V>> es = entrySet;
        return (es != null ? es : (entrySet = new EntrySet()));
    }

    /**
     * Returns the entry associated with the specified key in the
     * HashMap.  Returns null if the HashMap contains no mapping
     * for this key.
     */
    Entry getEntry(String key)
    {
        String k = maskNull(key);
        int hash = hashCode(k);
        int i = indexFor(hash, table.length);
        Entry e = table[i];
        while (e != null && !(e.hash == hash && equals(k, e.key))) {
            e = e.next;
        }
        return e;
    }

    private class EntrySet extends AbstractSet
    {
        public Iterator iterator()
        {
            return newEntryIterator();
        }

        public boolean contains(Object o)
        {
            if (!(o instanceof StringMap.Entry)) {
                return false;
            }
            StringMap.Entry e = (StringMap.Entry)o;
            Entry candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        public boolean remove(Object o)
        {
            return removeMapping(o) != null;
        }

        public int size()
        {
            return size;
        }

        public void clear()
        {
            StringHashMap.this.clear();
        }
    }

    /**
     * Special version of remove for EntrySet.
     */
    Entry removeMapping(Object o)
    {
        if (!(o instanceof StringMap.Entry)) {
            return null;
        }

        StringMap.Entry entry = (StringMap.Entry)o;
        String k = maskNull(entry.getKey());
        int hash = hashCode(k);
        int i = indexFor(hash, table.length);
        Entry<V> prev = table[i];
        Entry<V> e = prev;

        while (e != null) {
            Entry<V> next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                modCount++;
                size--;
                if (prev == e) {
                    table[i] = next;
                }
                else {
                    prev.next = next;
                }
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }

    /**
     * StringHashMap collision list entry.
     */
    private static class Entry<V>
        implements StringMap.Entry<V>, Cloneable
    {
        int hash;
        String key;
        V value;
        Entry<V> next;

        Entry(int hash, String key,
              V value, Entry<V> next)
        {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Object clone()
        {
            return new Entry<V>(hash, key, value,
                (next == null ? null : (Entry<V>)next.clone()));
        }

        public void recycle()
        {
            this.hash = 0;
            this.key = null;
            this.value = null;
            this.next = null;
        }

        // Map.Entry Ops

        public String getKey()
        {
            return unmaskNull(key);
        }

        public V getValue()
        {
            return value;
        }

        public V setValue(V value)
        {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o)
        {
            if (!(o instanceof StringMap.Entry)) {
                return false;
            }
            StringMap.Entry<V> e = (StringMap.Entry<V>)o;
            String k1 = getKey();
            String k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                V v1 = getValue();
                V v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2))) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode()
        {
            return hash ^ (value == null ? 0 : value.hashCode());
        }

        public String toString()
        {
            Object value = getValue();
            StringBuilder sb = new StringBuilder(16);
            sb.append(key);
            sb.append('=');
            if (value != null && value instanceof Object[]) {
                String className = value.getClass().getName();
                sb.append(className).append("[").append(((Object[])value).length).append("]{");
                sb.append(StringUtil.toString(((Object[])value), ','));
                sb.append('}');
            }
            else {
                sb.append(value);
            }

            return sb.toString();
        }
    }


    private abstract class HashIterator implements Iterator
    {
        Entry next;                  // next entry to return
        int expectedModCount;        // For fast-fail
        int index;                   // current slot
        Entry current;               // current entry

        HashIterator()
        {
            expectedModCount = modCount;
            Entry[] t = table;
            int i = t.length;
            Entry n = null;
            if (size != 0) { // advance to first entry
                while (i > 0 && (n = t[--i]) == null) {
                }
            }
            next = n;
            index = i;
        }

        public boolean hasNext()
        {
            return next != null;
        }

        Entry nextEntry()
        {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            Entry e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }

            Entry n = e.next;
            Entry[] t = table;
            int i = index;
            while (n == null && i > 0) {
                n = t[--i];
            }
            index = i;
            next = n;
            return current = e;
        }

        public void remove()
        {
            if (current == null) {
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            String k = current.key;
            current = null;
            StringHashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }

    }

    private class ValueIterator extends HashIterator
    {
        public Object next()
        {
            return nextEntry().value;
        }
    }

    private class KeyIterator extends HashIterator
    {
        public Object next()
        {
            return nextEntry().getKey();
        }
    }

    private class EntryIterator extends HashIterator
    {
        public Object next()
        {
            return nextEntry();
        }
    }

    // Subclass overrides these to alter behavior of views' iterator() method
    Iterator<String> newKeyIterator()
    {
        return new KeyIterator();
    }

    Iterator<V> newValueIterator()
    {
        return new ValueIterator();
    }

    Iterator<StringMap.Entry<V>> newEntryIterator()
    {
        return new EntryIterator();
    }

    private void writeObject(ObjectOutputStream oos)
        throws IOException
    {
        oos.defaultWriteObject();
        // Write out number of buckets
        oos.writeInt(table.length);
        marshal(oos);
    }

    /**
     * Reconstitute the <tt>StringHashMap</tt> instance from a stream (i.e.,
     * deserialize it).
     */
    private void readObject(ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        ois.defaultReadObject();
        // Read in number of buckets and allocate the bucket array;
        int numBuckets = ois.readInt();
        table = new Entry[numBuckets];
        demarshal(ois);
    }

    int capacity()
    {
        return table.length;
    }

    float loadFactor()
    {
        return loadFactor;
    }

    public boolean equals(Object o)
    {
        if (o == this) {
            return true;
        }

        if (!(o instanceof StringMap)) {
            return false;
        }
        StringMap t = (StringMap)o;
        if (t.size() != size()) {
            return false;
        }

        Iterator i = getEntries();
        while (i.hasNext()) {
            Entry e = (Entry)i.next();
            String key = e.getKey();
            Object value = e.getValue();
            if (value == null) {
                if (!(t.get(key) == null && t.containsKey(key))) {
                    return false;
                }
            }
            else {
                if (!value.equals(t.get(key))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode()
    {
        int h = 0;
        Iterator i = getEntries();
        while (i.hasNext()) {
            h += i.next().hashCode();
        }
        return h;
    }

    public String toString()
    {
        int max = size() - 1;
        StringBuilder buf = new StringBuilder();
        Iterator i = getEntries();
        buf.append("{");
        for (int j = 0; j <= max; j++) {
            Entry e = (Entry)(i.next());
            buf.append(e.toString());
            if (j < max) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    // ʵ��Marshallable

    /**
     * ����һ���µķ�������ʶ�������κβ���
     *
     * @return StringHashMap
     * @deprecated Using com.ceno.util.ObjectUtil#getStringHashMap()
     */
    public static StringHashMap allocate()
    {
        return ObjectUtil.getStringHashMap();
    }

//    public StringHashMap(Empty empty)
//    {
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//    }

    /**
     * ���л���д����Ա
     *
     * @param oos ���������
     * @throws IOException ������I/O�쳣ʱ
     */
    public void marshal(ObjectOutputStream oos)
        throws IOException
    {
        oos.writeInt(size);
        String key;
        for (int index = table.length - 1; index >= 0; index--) {
            Entry entry = table[index];
            while (entry != null) {
                key = entry.getKey();
                CodecUtil.writeString(oos, key);
                CodecUtil.writeObject(oos, entry.value);
                entry = entry.next;
            }
        }
    }

    /**
     * �����л�����ȡ��Ա
     *
     * @param ois ����������
     * @throws IOException ������I/O�쳣ʱ
     */
    public void demarshal(ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        int size = ois.readInt();
        if (size > 64 * 1024) {
            throw new IOException("The map size is too big:" + size);
        }

        int initialCapacity = Math.max((int)(size / DEFAULT_LOAD_FACTOR) + 1, INITIAL_SIZE);
        if (table == null || table.length < initialCapacity) {
            init(initialCapacity);
        }

        for (int i = 0; i < size; i++) {
            String key = CodecUtil.readString(ois);
            V value = (V)CodecUtil.readObject(ois);
            putForCreate(key, value);
        }
    }

    /**
     * �����޸�����
     *
     * @return �޸�����
     */
    public int getModificationCount()
    {
        return modCount;
    }
}
