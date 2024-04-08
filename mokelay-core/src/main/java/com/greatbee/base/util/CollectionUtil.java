package com.greatbee.base.util;


import java.io.Serializable;
import java.util.*;

/**
 * ��Collection��صķ���
 *
 * @author system
 * @version 1.00 2005-2-7 15:06:51
 */
public class CollectionUtil
{
    /**
     * Copies all of the elements from one list into another.  After the
     * operation, the index of each copied element in the destination list
     * will be identical to its index in the source list.  The destination
     * list must be at least as long as the source list.  If it is longer, the
     * remaining elements in the destination list are unaffected. <p>
     *
     * This method runs in linear time.
     *
     * @param dest The destination list.
     * @param src  The source list.
     * @throws IndexOutOfBoundsException     if the destination list is too small
     *                                       to contain the entire source List.
     * @throws UnsupportedOperationException if the destination list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void copy(List<V> dest, V[] src)
    {
        ArrayUtil.addAll(dest, src);
    }

    /**
     * Copies all of the elements from one list into another.  After the
     * operation, the index of each copied element in the destination list
     * will be identical to its index in the source list.  The destination
     * list must be at least as long as the source list.  If it is longer, the
     * remaining elements in the destination list are unaffected. <p>
     *
     * This method runs in linear time.
     *
     * @param dest The destination list.
     * @param src  The source list.
     * @throws IndexOutOfBoundsException     if the destination list is too small
     *                                       to contain the entire source List.
     * @throws UnsupportedOperationException if the destination list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void copy(Collection<V> dest, V[] src)
    {
        ArrayUtil.addAll(dest, src);
    }

    /**
     * Copies all of the elements from one list into another.  After the
     * operation, the index of each copied element in the destination list
     * will be identical to its index in the source list.  The destination
     * list must be at least as long as the source list.  If it is longer, the
     * remaining elements in the destination list are unaffected. <p>
     *
     * This method runs in linear time.
     *
     * @param dest The destination list.
     * @param src  The source Iterator
     * @throws IndexOutOfBoundsException     if the destination list is too small
     *                                       to contain the entire source List.
     * @throws UnsupportedOperationException if the destination list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void copy(List<V> dest, Iterator<V> src)
    {
        while (src.hasNext()) {
            dest.add(src.next());
        }
    }

    /**
     * Copies all of the elements from one list into another.  After the
     * operation, the index of each copied element in the destination list
     * will be identical to its index in the source list.  The destination
     * list must be at least as long as the source list.  If it is longer, the
     * remaining elements in the destination list are unaffected. <p>
     *
     * This method runs in linear time.
     *
     * @param dest The destination list.
     * @param src  The source Iterator
     * @throws IndexOutOfBoundsException     if the destination list is too small
     *                                       to contain the entire source List.
     * @throws UnsupportedOperationException if the destination list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void copy(Collection<V> dest, Iterator<V> src)
    {
        while (src.hasNext()) {
            dest.add(src.next());
        }
    }

    /**
     * Copies all of the elements from one list into another.  After the
     * operation, the index of each copied element in the destination list
     * will be identical to its index in the source list.  The destination
     * list must be at least as long as the source list.  If it is longer, the
     * remaining elements in the destination list are unaffected. <p>
     *
     * This method runs in linear time.
     *
     * @param dest The destination list.
     * @param src  The source Enumeration
     * @throws IndexOutOfBoundsException     if the destination list is too small
     *                                       to contain the entire source List.
     * @throws UnsupportedOperationException if the destination list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void copy(List<V> dest, Enumeration<V> src)
    {
        while (src.hasMoreElements()) {
            dest.add(src.nextElement());
        }
    }

    /**
     * Copies all of the elements from one list into another.  After the
     * operation, the index of each copied element in the destination list
     * will be identical to its index in the source list.  The destination
     * list must be at least as long as the source list.  If it is longer, the
     * remaining elements in the destination list are unaffected. <p>
     *
     * This method runs in linear time.
     *
     * @param dest The destination list.
     * @param src  The source Enumeration
     * @throws IndexOutOfBoundsException     if the destination list is too small
     *                                       to contain the entire source List.
     * @throws UnsupportedOperationException if the destination list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void copy(Collection<V> dest, Enumeration<V> src)
    {
        while (src.hasMoreElements()) {
            dest.add(src.nextElement());
        }
    }

    /**
     * �ж�����ʱ����Ч
     *
     * @param coll
     */
    public static final boolean isValid(Collection coll)
    {
        return coll != null && !coll.isEmpty();
    }

    /**
     * �ж�����ʱ����Ч
     *
     * @param coll
     */
    public static final boolean isInvalid(Collection coll)
    {
        return coll == null || coll.isEmpty();
    }

    /**
     * �ж�����ʱ����Ч
     *
     * @param map
     */
    public static final boolean isValid(Map map)
    {
        return map != null && !map.isEmpty();
    }

    /**
     * �ж�����ʱ����Ч
     *
     * @param map
     */
    public static final boolean isInvalid(Map map)
    {
        return map == null || map.isEmpty();
    }

    public static <V> Set<V> emptySet()
    {
        return Collections.emptySet();
    }

    public static <V> List<V> emptyList()
    {
        return Collections.emptyList();
    }

    public static <K, V> Map<K, V> emptyMap()
    {
        return Collections.emptyMap();
    }

    /**
     * The empty list (immutable).  This list is serializable.
     */
    public static final List EMPTY_LIST = Collections.EMPTY_LIST;
    public static final Set EMPTY_SET = Collections.EMPTY_SET;
    public static final Map EMPTY_MAP = Collections.EMPTY_MAP;

    public static <V> Iterator<V> emptyIterator()
    {
        return (Iterator<V>)EMPTY_ITERATOR;
    }

    public static final Iterator EMPTY_ITERATOR = new Iterator()
    {

        /**
         * Removes from the underlying collection the last element returned by the
         * iterator (optional operation).  This method can be called only once per
         * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
         * the underlying collection is modified while the iteration is in
         * progress in any way other than by calling this method.
         *
         * @throws UnsupportedOperationException if the <tt>remove</tt>
         *                                       operation is not supported by this Iterator.
         * @throws IllegalStateException         if the <tt>next</tt> method has not
         *                                       yet been called, or the <tt>remove</tt> method has already
         *                                       been called after the last call to the <tt>next</tt>
         *                                       method.
         */
        public void remove()
        {
            throw new NoSuchElementException("No such element");
        }

        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext()
        {
            return false;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration.
         * @throws NoSuchElementException
         *          iteration has no more elements.
         */
        public Object next()
        {
            throw new NoSuchElementException("No such element");
        }
    };

    public static <V> ListIterator<V> emptyListIterator()
    {
        return (ListIterator<V>)emptyListIterator();
    }

    public static final ListIterator EMPTY_LIST_ITERATOR = new ListIterator()
    {

        /**
         * Returns the index of the element that would be returned by a subsequent
         * call to <tt>next</tt>. (Returns list size if the list iterator is at the
         * end of the list.)
         *
         * @return the index of the element that would be returned by a subsequent
         *         call to <tt>next</tt>, or list size if list iterator is at end
         *         of list.
         */
        public int nextIndex()
        {
            return 0;
        }

        /**
         * Returns the index of the element that would be returned by a subsequent
         * call to <tt>previous</tt>. (Returns -1 if the list iterator is at the
         * beginning of the list.)
         *
         * @return the index of the element that would be returned by a subsequent
         *         call to <tt>previous</tt>, or -1 if list iterator is at
         *         beginning of list.
         */
        public int previousIndex()
        {
            return 0;
        }

        /**
         * Removes from the underlying collection the last element returned by the
         * iterator (optional operation).  This method can be called only once per
         * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
         * the underlying collection is modified while the iteration is in
         * progress in any way other than by calling this method.
         *
         * @throws UnsupportedOperationException if the <tt>remove</tt>
         *                                       operation is not supported by this Iterator.
         * @throws IllegalStateException         if the <tt>next</tt> method has not
         *                                       yet been called, or the <tt>remove</tt> method has already
         *                                       been called after the last call to the <tt>next</tt>
         *                                       method.
         */
        public void remove()
        {
            throw new NoSuchElementException("No such element");
        }

        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext()
        {
            return false;
        }

        /**
         * Returns <tt>true</tt> if this list iterator has more elements when
         * traversing the list in the reverse direction.  (In other words, returns
         * <tt>true</tt> if <tt>previous</tt> would return an element rather than
         * throwing an exception.)
         *
         * @return <tt>true</tt> if the list iterator has more elements when
         *         traversing the list in the reverse direction.
         */
        public boolean hasPrevious()
        {
            return false;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration.
         * @throws NoSuchElementException
         *          iteration has no more elements.
         */
        public Object next()
        {
            throw new NoSuchElementException("No such element");
        }

        /**
         * Returns the previous element in the list.  This method may be called
         * repeatedly to iterate through the list backwards, or intermixed with
         * calls to <tt>next</tt> to go back and forth.  (Note that alternating
         * calls to <tt>next</tt> and <tt>previous</tt> will return the same
         * element repeatedly.)
         *
         * @return the previous element in the list.
         * @throws NoSuchElementException
         *          if the iteration has no previous
         *          element.
         */
        public Object previous()
        {
            throw new NoSuchElementException("No such element");
        }

        /**
         * Inserts the specified element into the list (optional operation).  The
         * element is inserted immediately before the next element that would be
         * returned by <tt>next</tt>, if any, and after the next element that
         * would be returned by <tt>previous</tt>, if any.  (If the list contains
         * no elements, the new element becomes the sole element on the list.)
         * The new element is inserted before the implicit cursor: a subsequent
         * call to <tt>next</tt> would be unaffected, and a subsequent call to
         * <tt>previous</tt> would return the new element.  (This call increases
         * by one the value that would be returned by a call to <tt>nextIndex</tt>
         * or <tt>previousIndex</tt>.)
         *
         * @param o the element to insert.
         * @throws UnsupportedOperationException if the <tt>add</tt> method is
         *                                       not supported by this list iterator.
         * @throws ClassCastException            if the class of the specified element
         *                                       prevents it from being added to this list.
         * @throws IllegalArgumentException      if some aspect of this element
         *                                       prevents it from being added to this list.
         */
        public void add(Object o)
        {
            throw new NoSuchElementException("No such element");
        }

        /**
         * Replaces the last element returned by <tt>next</tt> or
         * <tt>previous</tt> with the specified element (optional operation).
         * This call can be made only if neither <tt>ListIterator.remove</tt> nor
         * <tt>ListIterator.add</tt> have been called after the last call to
         * <tt>next</tt> or <tt>previous</tt>.
         *
         * @param o the element with which to replace the last element returned by
         *          <tt>next</tt> or <tt>previous</tt>.
         * @throws UnsupportedOperationException if the <tt>set</tt> operation
         *                                       is not supported by this list iterator.
         * @throws ClassCastException            if the class of the specified element
         *                                       prevents it from being added to this list.
         * @throws IllegalArgumentException      if some aspect of the specified
         *                                       element prevents it from being added to this list.
         * @throws IllegalStateException         if neither <tt>next</tt> nor
         *                                       <tt>previous</tt> have been called, or <tt>remove</tt> or
         *                                       <tt>add</tt> have been called after the last call to
         *                                       <tt>next</tt> or <tt>previous</tt>.
         */
        public void set(Object o)
        {
            throw new NoSuchElementException("No such element");
        }
    };

    public static <V> Enumeration<V> emptyEnum()
    {
        return (Enumeration<V>)EMPTY_ENUMERATION;
    }

    public static final Enumeration EMPTY_ENUMERATION = new Enumeration()
    {

        /**
         * Tests if this enumeration contains more elements.
         *
         * @return <code>true</code> if and only if this enumeration object
         *         contains at least one more element to provide;
         *         <code>false</code> otherwise.
         */
        public boolean hasMoreElements()
        {
            return false;
        }

        /**
         * Returns the next element of this enumeration if this enumeration
         * object has at least one more element to provide.
         *
         * @return the next element of this enumeration.
         * @throws NoSuchElementException
         *          if no more elements exist.
         */
        public Object nextElement()
        {
            throw new NoSuchElementException();
        }
    };

    /**
     * The empty map (immutable).  This map is serializable.
     *
     * @since 1.3
     */
    public static final Properties EMPTY_PROPERTIES = new EmptyProperties();

    public static class EmptyProperties
        extends Properties
    {
        public int size()
        {
            return 0;
        }

        public boolean isEmpty()
        {
            return true;
        }

        public boolean containsKey(String key)
        {
            return false;
        }

        /**
         * �Ƿ�ӵ�и��ֵ
         *
         * @param value [Object]
         */
        public boolean containsValue(Object value)
        {
            return false;
        }

        public Object get(String key)
        {
            return null;
        }

        public Object put(String key, Object value)
        {
            return null;
        }

        public Object remove(String key)
        {
            return null;
        }

        public Enumeration<Object> keys()
        {
            return emptyEnum();
        }

        public Set<Object> keySet()
        {
            return Collections.emptySet();
        }

        public Collection<Object> values()
        {
            return Collections.emptySet();
        }

        public Set<Map.Entry<Object, Object>> entrySet()
        {
            return Collections.emptySet();
        }

        public boolean equals(Object o)
        {
            return (o instanceof Properties) && ((Properties)o).size() == 0;
        }

        public int hashCode()
        {
            return 0;
        }

        public void clear()
        {
        }
    }

    /**
     * ���listΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param coll
     */
    public static <E> Iterator<E> iterator(Collection<E> coll)
    {
        if (isValid(coll)) {
            return coll.iterator();
        }
        else {
            return emptyIterator();
        }
    }

    /**
     * ���listΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param en
     */
    public static <E> Iterator<E> iterator(Enumeration<E> en)
    {
        if (en != null) {
            return new EnumIterator<E>(en);
        }
        else {
            return emptyIterator();
        }
    }

    /**
     * ��Iterator ת����Enumeration
     *
     * @param iterator
     */
    public static <E> Enumeration<E> toEnum(Iterator<E> iterator)
    {
        if (iterator != null) {
            return new EnumIterator<E>(iterator);
        }
        else {
            return emptyEnum();
        }
    }

    /**
     * ���vector<code>null</code>������<code>EMPTY_ENUMERATION</code>
     *
     * @param vector
     */
    public static <E> Enumeration<E> elements(Vector<E> vector)
    {
        if (isValid(vector)) {
            return vector.elements();
        }
        else {
            return emptyEnum();
        }
    }

    /**
     * ���vector<code>null</code>������<code>EMPTY_ENUMERATION</code>
     *
     * @param coll
     */
    public static <E> Enumeration<E> elements(Collection<E> coll)
    {
        if (isValid(coll)) {
            return new EnumIterator<E>(coll.iterator());
        }
        else {
            return emptyEnum();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    public static <K> Iterator<K> keys(Map<K, ?> map)
    {
        if (isValid(map)) {
            return map.keySet().iterator();
        }
        else {
            return emptyIterator();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    public static <K> Set<K> keySet(Map<K, ?> map)
    {
        if (isValid(map)) {
            return map.keySet();
        }
        else {
            return Collections.emptySet();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ENUMERATION</code>
     *
     * @param map
     */
    public static <K> Enumeration<K> keyEnum(Map<K, ?> map)
    {
        if (isValid(map)) {
            return new EnumIterator<K>(map.keySet().iterator());
        }
        else {
            return emptyEnum();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    public static <V> Iterator<V> values(Map<?, V> map)
    {
        if (isValid(map)) {
            return map.values().iterator();
        }
        else {
            return emptyIterator();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    public static <V> Collection<V> valueSet(Map<?, V> map)
    {
        if (isValid(map)) {
            return map.values();
        }
        else {
            return Collections.emptySet();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    public static <K,V> Set<Map.Entry<K, V>> entrySet(Map<K, V> map)
    {
        if (isValid(map)) {
            return map.entrySet();
        }
        else {
            return Collections.emptySet();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ENUMERATION</code>
     *
     * @param map
     */
    public static <V> Enumeration<V> valueEnum(Map<?, V> map)
    {
        if (isValid(map)) {
            return new EnumIterator<V>(map.values().iterator());
        }
        else {
            return emptyEnum();
        }
    }


    // Algorithms

    /**
     * Sorts the specified list into ascending order, according to the
     * <i>natural ordering</i> of its elements.  All elements in the list must
     * implement the <tt>Comparable</tt> interface.  Furthermore, all elements
     * in the list must be <i>mutually comparable</i> (that is,
     * <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt>
     * for any elements <tt>e1</tt> and <tt>e2</tt> in the list).<p>
     *
     * This sort is guaranteed to be <i>stable</i>:  equal elements will
     * not be reordered as a result of the sort.<p>
     *
     * The specified list must be modifiable, but need not be resizable.<p>
     *
     * The sorting algorithm is a modified mergesort (in which the merge is
     * omitted if the highest element in the low sublist is less than the
     * lowest element in the high sublist).  This algorithm offers guaranteed
     * n log(n) performance, and can approach linear performance on nearly
     * sorted lists.<p>
     *
     * This implementation dumps the specified list into an array, sorts
     * the array, and iterates over the list resetting each element
     * from the corresponding position in the array.  This avoids the
     * n<sup>2</sup> log(n) performance that would result from attempting
     * to sort a linked list in place.
     *
     * @param list the list to be sorted.
     * @throws ClassCastException            if the list contains elements that are not
     *                                       <i>mutually comparable</i> (for example, strings and integers).
     * @throws UnsupportedOperationException if the specified list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     * @see Comparable
     */
    public static <T extends Comparable<? super T>> void sort(List<T> list)
    {
        Collections.sort(list);
    }

    /**
     * Sorts the specified list according to the order induced by the
     * specified comparator.  All elements in the list must be <i>mutually
     * comparable</i> using the specified comparator (that is,
     * <tt>c.compare(e1, e2)</tt> must not throw a <tt>ClassCastException</tt>
     * for any elements <tt>e1</tt> and <tt>e2</tt> in the list).<p>
     *
     * This sort is guaranteed to be <i>stable</i>:  equal elements will
     * not be reordered as a result of the sort.<p>
     *
     * The sorting algorithm is a modified mergesort (in which the merge is
     * omitted if the highest element in the low sublist is less than the
     * lowest element in the high sublist).  This algorithm offers guaranteed
     * n log(n) performance, and can approach linear performance on nearly
     * sorted lists.<p>
     *
     * The specified list must be modifiable, but need not be resizable.
     * This implementation dumps the specified list into an array, sorts
     * the array, and iterates over the list resetting each element
     * from the corresponding position in the array.  This avoids the
     * n<sup>2</sup> log(n) performance that would result from attempting
     * to sort a linked list in place.
     *
     * @param list the list to be sorted.
     * @param c    the comparator to determine the order of the list.  A
     *             <tt>null</tt> value indicates that the elements' <i>natural
     *             ordering</i> should be used.
     * @throws ClassCastException            if the list contains elements that are not
     *                                       <i>mutually comparable</i> using the specified comparator.
     * @throws UnsupportedOperationException if the specified list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     * @see Comparator
     */
    public static <T> void sort(List<T> list, Comparator<? super T> c)
    {
        Collections.sort(list, c);
    }


    /**
     * Searches the specified list for the specified object using the binary
     * search algorithm.  The list must be sorted into ascending order
     * according to the <i>natural ordering</i> of its elements (as by the
     * <tt>sort(List)</tt> method, above) prior to making this call.  If it is
     * not sorted, the results are undefined.  If the list contains multiple
     * elements equal to the specified object, there is no guarantee which one
     * will be found.<p>
     *
     * This method runs in log(n) time for a "random access" list (which
     * provides near-constant-time positional access).  It may
     * run in n log(n) time if it is called on a "sequential access" list
     * (which provides linear-time positional access).</p>
     *
     * If the specified list implements the <tt>AbstracSequentialList</tt>
     * interface, this method will do a sequential search instead of a binary
     * search; this offers linear performance instead of n log(n) performance
     * if this method is called on a <tt>LinkedList</tt> object.
     *
     * @param list the list to be searched.
     * @param key  the key to be searched for.
     *             index of the search key, if it is contained in the list;
     *             otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.  The
     *             <i>insertion point</i> is defined as the point at which the
     *             key would be inserted into the list: the index of the first
     *             element greater than the key, or <tt>list.size()</tt>, if all
     *             elements in the list are less than the specified key.  Note
     *             that this guarantees that the return value will be &gt;= 0 if
     *             and only if the key is found.
     * @throws ClassCastException if the list contains elements that are not
     *                            <i>mutually comparable</i> (for example, strings and
     *                            integers), or the search key in not mutually comparable
     *                            with the elements of the list.
     * @see Comparable
     * @see #sort(List)
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key)
    {
        return Collections.binarySearch(list, key);
    }

    /**
     * Searches the specified list for the specified object using the binary
     * search algorithm.  The list must be sorted into ascending order
     * according to the specified comparator (as by the <tt>Sort(List,
     * Comparator)</tt> method, above), prior to making this call.  If it is
     * not sorted, the results are undefined.  If the list contains multiple
     * elements equal to the specified object, there is no guarantee which one
     * will be found.<p>
     *
     * This method runs in log(n) time for a "random access" list (which
     * provides near-constant-time positional access).  It may
     * run in n log(n) time if it is called on a "sequential access" list
     * (which provides linear-time positional access).</p>
     *
     * If the specified list implements the <tt>AbstracSequentialList</tt>
     * interface, this method will do a sequential search instead of a binary
     * search; this offers linear performance instead of n log(n) performance
     * if this method is called on a <tt>LinkedList</tt> object.
     *
     * @param list the list to be searched.
     * @param key  the key to be searched for.
     * @param c    the comparator by which the list is ordered.  A
     *             <tt>null</tt> value indicates that the elements' <i>natural
     *             ordering</i> should be used.
     *             index of the search key, if it is contained in the list;
     *             otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.  The
     *             <i>insertion point</i> is defined as the point at which the
     *             key would be inserted into the list: the index of the first
     *             element greater than the key, or <tt>list.size()</tt>, if all
     *             elements in the list are less than the specified key.  Note
     *             that this guarantees that the return value will be &gt;= 0 if
     *             and only if the key is found.
     * @throws ClassCastException if the list contains elements that are not
     *                            <i>mutually comparable</i> using the specified comparator,
     *                            or the search key in not mutually comparable with the
     *                            elements of the list using this comparator.
     * @see Comparable
     * @see #sort(List, Comparator)
     */
    public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c)
    {
        return Collections.binarySearch(list, key, c);
    }

    /**
     * Reverses the order of the elements in the specified list.<p>
     *
     * This method runs in linear time.
     *
     * @param l the list whose elements are to be reversed.
     * @throws UnsupportedOperationException if the specified list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static final void reverse(List l)
    {
        Collections.reverse(l);
    }

    /**
     * Randomly permutes the specified list using a default source of
     * randomness.  All permutations occur with approximately equal
     * likelihood.<p>
     *
     * The hedge "approximately" is used in the foregoing description because
     * default source of randomenss is only approximately an unbiased source
     * of independently chosen bits. If it were a perfect source of randomly
     * chosen bits, then the algorithm would choose permutations with perfect
     * uniformity.<p>
     *
     * This implementation traverses the list backwards, from the last element
     * up to the second, repeatedly swapping a randomly selected element into
     * the "current position".  Elements are randomly selected from the
     * portion of the list that runs from the first element to the current
     * position, inclusive.<p>
     *
     * This method runs in linear time for a "random access" list (which
     * provides near-constant-time positional access).  It may require
     * quadratic time for a "sequential access" list.
     *
     * @param list the list to be shuffled.
     * @throws UnsupportedOperationException if the specified list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static final void shuffle(List list)
    {
        Collections.shuffle(list);
    }

    /**
     * Randomly permute the specified list using the specified source of
     * randomness.  All permutations occur with equal likelihood
     * assuming that the source of randomness is fair.<p>
     *
     * This implementation traverses the list backwards, from the last element
     * up to the second, repeatedly swapping a randomly selected element into
     * the "current position".  Elements are randomly selected from the
     * portion of the list that runs from the first element to the current
     * position, inclusive.<p>
     *
     * This method runs in linear time for a "random access" list (which
     * provides near-constant-time positional access).  It may require
     * quadratic time for a "sequential access" list.
     *
     * @param list the list to be shuffled.
     * @param rnd  the source of randomness to use to shuffle the list.
     * @throws UnsupportedOperationException if the specified list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static final void shuffle(List list, Random rnd)
    {
        Collections.shuffle(list, rnd);
    }

    /**
     * Replaces all of the elements of the specified list with the specified
     * element. <p>
     *
     * This method runs in linear time.
     *
     * @param list the list to be filled with the specified element.
     * @param o    The element with which to fill the specified list.
     * @throws UnsupportedOperationException if the specified list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void fill(List<V> list, V o)
    {
        Collections.fill(list, o);
    }

    /**
     * Copies all of the elements from one list into another.  After the
     * operation, the index of each copied element in the destination list
     * will be identical to its index in the source list.  The destination
     * list must be at least as long as the source list.  If it is longer, the
     * remaining elements in the destination list are unaffected. <p>
     *
     * This method runs in linear time.
     *
     * @param dest The destination list.
     * @param src  The source list.
     * @throws IndexOutOfBoundsException     if the destination list is too small
     *                                       to contain the entire source List.
     * @throws UnsupportedOperationException if the destination list's
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <V> void copy(List<V> dest, List<V> src)
    {
        Collections.copy(dest, src);
    }

    /**
     * Returns the minimum element of the given collection, according to the
     * <i>natural ordering</i> of its elements.  All elements in the
     * collection must implement the <tt>Comparable</tt> interface.
     * Furthermore, all elements in the collection must be <i>mutually
     * comparable</i> (that is, <tt>e1.compareTo(e2)</tt> must not throw a
     * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and
     * <tt>e2</tt> in the collection).<p>
     *
     * This method iterates over the entire collection, hence it requires
     * time proportional to the size of the collection.
     *
     * @param coll the collection whose minimum element is to be determined.
     *             the minimum element of the given collection, according
     *             to the <i>natural ordering</i> of its elements.
     * @throws ClassCastException if the collection contains elements that are
     *                            not <i>mutually comparable</i> (for example, strings and
     *                            integers).
     * @throws NoSuchElementException
     *                            if the collection is empty.
     * @see Comparable
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll)
    {
        return Collections.min(coll);
    }

    /**
     * Returns the minimum element of the given collection, according to the
     * order induced by the specified comparator.  All elements in the
     * collection must be <i>mutually comparable</i> by the specified
     * comparator (that is, <tt>comp.compare(e1, e2)</tt> must not throw a
     * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and
     * <tt>e2</tt> in the collection).<p>
     *
     * This method iterates over the entire collection, hence it requires
     * time proportional to the size of the collection.
     *
     * @param coll the collection whose minimum element is to be determined.
     * @param comp the comparator with which to determine the minimum element.
     *             A <tt>null</tt> value indicates that the elements' <i>natural
     *             ordering</i> should be used.
     *             the minimum element of the given collection, according
     *             to the specified comparator.
     * @throws ClassCastException if the collection contains elements that are
     *                            not <i>mutually comparable</i> using the specified comparator.
     * @throws NoSuchElementException
     *                            if the collection is empty.
     * @see Comparable
     */
    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp)
    {
        return Collections.min(coll, comp);
    }

    /**
     * Returns the maximum element of the given collection, according to the
     * <i>natural ordering</i> of its elements.  All elements in the
     * collection must implement the <tt>Comparable</tt> interface.
     * Furthermore, all elements in the collection must be <i>mutually
     * comparable</i> (that is, <tt>e1.compareTo(e2)</tt> must not throw a
     * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and
     * <tt>e2</tt> in the collection).<p>
     *
     * This method iterates over the entire collection, hence it requires
     * time proportional to the size of the collection.
     *
     * @param coll the collection whose maximum element is to be determined.
     *             the maximum element of the given collection, according
     *             to the <i>natural ordering</i> of its elements.
     * @throws ClassCastException if the collection contains elements that are
     *                            not <i>mutually comparable</i> (for example, strings and
     *                            integers).
     * @see Comparable
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll)
    {
        return Collections.max(coll);
    }

    /**
     * Returns the maximum element of the given collection, according to the
     * order induced by the specified comparator.  All elements in the
     * collection must be <i>mutually comparable</i> by the specified
     * comparator (that is, <tt>comp.compare(e1, e2)</tt> must not throw a
     * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and
     * <tt>e2</tt> in the collection).<p>
     *
     * This method iterates over the entire collection, hence it requires
     * time proportional to the size of the collection.
     *
     * @param coll the collection whose maximum element is to be determined.
     * @param comp the comparator with which to determine the maximum element.
     *             A <tt>null</tt> value indicates that the elements' <i>natural
     *             ordering</i> should be used.
     *             the maximum element of the given collection, according
     *             to the specified comparator.
     * @throws ClassCastException if the collection contains elements that are
     *                            not <i>mutually comparable</i> using the specified comparator.
     * @throws NoSuchElementException
     *                            if the collection is empty.
     * @see Comparable
     */
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp)
    {
        return Collections.max(coll, comp);
    }

    /**
     * �ж�����ʱ����Ч
     *
     * @param map
     */
    public static final boolean isValid(StringMap map)
    {
        return map != null && !map.isEmpty();
    }

    /**
     * �ж�����ʱ����Ч
     *
     * @param map
     */
    public static final boolean isInvalid(StringMap map)
    {
        return map == null || map.isEmpty();
    }

    /**
     * The empty map (immutable).  This map is serializable.
     *
     * @since 1.3
     */
    public static final StringMap EMPTY_STRING_MAP = new EmptyStringMap();

    public static class EmptyStringMap<V> implements StringMap<V>, Serializable
    {
        public int size()
        {
            return 0;
        }

        public boolean isEmpty()
        {
            return true;
        }

        public boolean containsKey(String key)
        {
            return false;
        }

        /**
         * �Ƿ�ӵ�и��ֵ
         *
         * @param value [Object]
         */
        public boolean containsValue(Object value)
        {
            return false;
        }

        public V get(String key)
        {
            return null;
        }

        public String getString(String key)
        {
            return null;
        }

        public String getString(String key, String defValue)
        {
            return defValue;
        }

        public V put(String key, V value)
        {
            return null;
        }

        /**
         * ����<code>StringMap</code>�����еĲ���
         *
         * @param map [StringMap]
         */
        public void putAll(StringMap<? extends V> map)
        {

        }

        /**
         * ����<code>StringMap</code>�����еĲ���
         *
         * @param map [StringMap]
         */
        public void putAll(Map<String, ? extends V> map)
        {

        }

        public V remove(String key)
        {
            return null;
        }

//        public Iterator<String> keys()
//        {
//            return emptyIterator();
//        }

        /**
         * �������м�
         */
        public Iterator<String> getKeys()
        {
            return emptyIterator();
        }

        public Set<String> keySet()
        {
            return Collections.emptySet();
        }

//        public Iterator<V> values()
//        {
//            return emptyIterator();
//        }

        /**
         * ��������ֵ
         */
        public Iterator<V> getValues()
        {
            return emptyIterator();
        }

//        /**
//         *
//         */
//        public Iterator<StringMap.Entry<V>> entries()
//        {
//            return emptyIterator();
//        }

        /**
         */
        public Iterator<Entry<V>> getEntries()
        {
            return emptyIterator();
        }

        public Collection<V> valueSet()
        {
            return Collections.emptySet();
        }

        public Set<Entry<V>> entrySet()
        {
            return Collections.emptySet();
        }

        public boolean equals(Object o)
        {
            return (o instanceof StringMap) && ((StringMap)o).size() == 0;
        }

        public int hashCode()
        {
            return 0;
        }

        public void clear()
        {

        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    public static final Iterator<String> keys(StringMap map)
    {
        Iterator<String> keys = null;
        if (isValid(map)) {
            keys = map.getKeys();
        }
        else {
            keys = emptyIterator();
        }
        return keys;
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ENUMERATION</code>
     *
     * @param map
     */
    public static final Enumeration<String> keyEnum(StringMap map)
    {
        if (isValid(map)) {
            return new EnumIterator<String>(map.getKeys());
        }
        else {
            return emptyEnum();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ITERATOR</code>
     *
     * @param map
     */
    public static <V> Iterator<V> values(StringMap<V> map)
    {
        if (isValid(map)) {
            return new EnumIterator<V>(map.getValues());
        }
        else {
            return emptyIterator();
        }
    }

    /**
     * ���mapΪ<code>null</code>������<code>EMPTY_ENUMERATION</code>
     *
     * @param map
     */
    public static <V> Enumeration<V> valueEnum(StringMap<V> map)
    {
        if (isValid(map)) {
            return new EnumIterator<V>(map.getValues());
        }
        else {
            return emptyEnum();
        }
    }

    /**
     * get first element
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getFirst(List<T> list) {
        if (isValid(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
