package com.mokelay.base.util;

import java.util.*;


/**
 * ���󹤾���
 */
public class ObjectUtil {
    /**
     * Empty
     */
    public static final Object[] EMTPY_ARRAY = new Object[0];

    /**
     * Singleton used as a null placeholder where null has another meaning.
     * <p/>
     * For example, in a <code>HashMap</code> the get(key) method returns null
     * if the Map contains null or if there is no matching key. The Null
     * placeholder can be used to distinguish between these two cases.
     * <p/>
     * Another example is <code>HashTable</code>, where <code>null</code>
     * cannot be stored.
     * <p/>
     * This instance is Serializable.
     */
//    public static final Null NULL = new Null();

    /**
     * ObjectUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used as <code>ObjectUtils.defaultIfNull("a","b");</code>.
     * This constructor is public to permit tools that require a JavaBean instance
     * to operate.
     */
    protected ObjectUtil() {
    }

    /**
     * Returns a default value if the object passed is null.
     *
     * @param object       the object to test
     * @param defaultValue the default value to return
     * @return object if it is not null, defaultValue otherwise
     */
    public static final Object defaultIfNull(Object object, Object defaultValue) {
        return (object != null ? object : defaultValue);
    }

    /**
     * Compares two objects for equality, where either one or both
     * objects may be <code>null</code>.
     *
     * @param object1 the first object
     * @param object2 the second object
     * @return <code>true</code> if the values of both objects are the same
     */
    public static final boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    /**
     * ȡ�����hashCode
     *
     * @param obj
     * @return �����hashCode
     */
    public static final int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    /**
     * Gets the toString that would be produced by Object if a class did not
     * override toString itself. Null will return null.
     *
     * @param object the object to create a toString for, may be null
     * @return the default toString text, or null if null passed in
     */
    public static final String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        return new StringBuilder()
                .append(object.getClass().getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(object)))
                .toString();
    }

    /**
     * Class used as a null placeholder where null has another meaning.
     * <p/>
     * For example, in a <code>HashMap</code> the get(key) method returns null
     * if the Map contains null or if there is no matching key. The Null
     * placeholder can be used to distinguish between these two cases.
     * <p/>
     * Another example is <code>HashTable</code>, where <code>null</code>
     * cannot be stored.
     */
//    public static class Null extends com.mokelay.lang.Null
//    {
//        /**
//         * Ensure singleton.
//         *
//         * @return the singleton value
//         */
//        private Object readResolve()
//        {
//            return NULL;
//        }
//    }

    private static ObjectPool<ArrayList> arrayListPool = new ObjectPool<ArrayList>();
    private static ObjectPool<Vector> vectorPool = new ObjectPool<Vector>();
    private static ObjectPool<HashMap> hashMapPool = new ObjectPool<HashMap>();
    private static ObjectPool<Hashtable> hashtablePool = new ObjectPool<Hashtable>();
    private static ObjectPool<StringHashMap> stringMapPool = new ObjectPool<StringHashMap>();

    /**
     * �������ó�������
     */
    public static <V> ArrayList<V> getArrayList() {
        ArrayList<V> list = (ArrayList<V>) arrayListPool.get();
        if (list == null) {
            list = new ArrayList<V>();
        }
        return list;
    }

    /**
     * �������ó�������
     */
    public static <V> Vector<V> getVector() {
        Vector<V> list = (Vector<V>) vectorPool.get();
        if (list == null) {
            list = new Vector<V>();
        }
        return list;
    }

    /**
     * �������ó�������
     */
    public static <K, V> HashMap<K, V> getHashMap() {
        HashMap<K, V> map = (HashMap<K, V>) hashMapPool.get();
        if (map == null) {
            map = new HashMap<K, V>();
        }
        return map;
    }

    /**
     * �������ó�������
     */
    public static <K, V> Hashtable<K, V> getHashtable() {
        Hashtable<K, V> map = (Hashtable<K, V>) hashtablePool.get();
        if (map == null) {
            map = new Hashtable<K, V>();
        }
        return map;
    }

    /**
     * �������õ�<code>StringHashMap</code>
     *
     * @return [StringHashMap]
     */
    public static <V> StringHashMap<V> getStringHashMap() {
        StringHashMap<V> map = (StringHashMap<V>) stringMapPool.get();
        if (map == null) {
            map = new StringHashMap<V>();
        }
        return map;
    }

    /**
     * ����<code>StringMap</code>
     *
     * @param map StringMap
     */
    public static final void recycle(StringMap map) {
        if (map == null) {
            return;
        }

        boolean recycle = map.size() <= 512; //����������С��
        map.clear();
        if (recycle && map.getClass() == StringHashMap.class) {
            stringMapPool.put((StringHashMap) map);
        }
    }

    /**
     * �����б�
     *
     * @param list [java.util.List]
     */
    public static final void recycle(List list) {
        if (list == null) {
            return;
        }

        boolean recycle = list.size() <= 512;
        list.clear();
        if (recycle) {
            Class clazz = list.getClass();
            if (clazz == ArrayList.class) { //��������
                arrayListPool.put((ArrayList) list);
            } else if (clazz == Vector.class) {
                vectorPool.put((Vector) list);
            }
        }
        list = null;
    }

    /**
     * ����<code>Map</code>
     *
     * @param map [java.util.Map]
     */

    public static final void recycle(Map map) {
        if (map == null) {
            return;
        }

        boolean recycle = map.size() <= 512; //����������С��
        map.clear();
        if (recycle) {
            //��������
            Class clazz = map.getClass();
            if (clazz == HashMap.class) {
                hashMapPool.put((HashMap) map);
            } else if (clazz == Hashtable.class) {
                hashtablePool.put((Hashtable) map);
            }
        }
        map = null;
    }

    /**
     * Map to Class
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T mapToClass(Map<String, Object> map, Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        List<java.lang.reflect.Field> fields = getAllFields(clazz);
        for (java.lang.reflect.Field field :fields) {
            field.setAccessible(true); // 允许访问私有字段
            Object value = map.get(field.getName());
            if (value != null) {
                try {
                    if(field.getType().equals(Date.class)){
                        field.set(instance, DataUtil.getDate(value));
                    }else{
                        field.set(instance, value);
                    }
                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

    public static List<java.lang.reflect.Field> getAllFields(Class<?> clazz) {
        List<java.lang.reflect.Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            // 添加当前类声明的所有字段
            java.lang.reflect.Field[] declaredFields = currentClass.getDeclaredFields();
            for (java.lang.reflect.Field field : declaredFields) {
                fields.add(field);
            }
            // 移动到父类
            currentClass = currentClass.getSuperclass();
        }
        return fields;
    }
}
