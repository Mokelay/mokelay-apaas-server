package com.mokelay.base.util.lang.reflect;

import com.mokelay.base.util.io.ByteArrayOutputStream;
import com.mokelay.base.util.io.StreamUtil;
import com.mokelay.base.util.ArrayUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;


/**
 * �����乤��
 *
 * @author system
 * @version 1.00 2005-2-8 14:15:19
 */
public class Reflection
{
    /**
     * �յ�������
     */
    public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

    /**
     * ����ĺ�׺
     */
    public static final String ARRAY_SUFFIX = "[]";

    /**
     * Primitive classes for reflection of constructors.
     */
    private static HashMap<String, Class> primitiveClasses;

    static {
        primitiveClasses = new HashMap<String, Class>(8);
        primitiveClasses.put(Boolean.TYPE.toString(), Boolean.TYPE);
        primitiveClasses.put(Character.TYPE.toString(), Character.TYPE);
        primitiveClasses.put(Byte.TYPE.toString(), Byte.TYPE);
        primitiveClasses.put(Short.TYPE.toString(), Short.TYPE);
        primitiveClasses.put(Integer.TYPE.toString(), Integer.TYPE);
        primitiveClasses.put(Long.TYPE.toString(), Long.TYPE);
        primitiveClasses.put(Float.TYPE.toString(), Float.TYPE);
        primitiveClasses.put(Double.TYPE.toString(), Double.TYPE);

        primitiveClasses.put("[Z", boolean[].class);
        primitiveClasses.put("boolean[]", boolean[].class);
        primitiveClasses.put("[B", byte[].class);
        primitiveClasses.put("byte[]", byte[].class);
        primitiveClasses.put("[C", char[].class);
        primitiveClasses.put("char[]", char[].class);
        primitiveClasses.put("[S", short[].class);
        primitiveClasses.put("short[]", short[].class);
        primitiveClasses.put("[I", int[].class);
        primitiveClasses.put("int[]", int[].class);
        primitiveClasses.put("[J", long[].class);
        primitiveClasses.put("long[]", long[].class);
        primitiveClasses.put("[F", float[].class);
        primitiveClasses.put("float[]", float[].class);
        primitiveClasses.put("[D", double[].class);
        primitiveClasses.put("double[]", double[].class);

        primitiveClasses.put("Object", Object.class);
        primitiveClasses.put("Object[]", Object[].class);
        primitiveClasses.put("String", String.class);
        primitiveClasses.put("String[]", String[].class);
        primitiveClasses.put("void", Void.TYPE);
    }

    /**
     * �ж��Ƿ������ֵ�����
     *
     * @param className
     */
    public static final boolean isArrayClass(String className)
    {
        if (className == null) {
            throw new IllegalArgumentException("Null class name");
        }
        return className.endsWith(ARRAY_SUFFIX)
               || className.startsWith("[L")
               || className.endsWith("[]");
    }

    /**
     * �жϸ�����Ƿ�ʵ���˵ڶ�����
     *
     * @param clazz      �����
     * @param superClass ����
     *                   �Ƿ��Ѿ�ʵ��
     */
    public static final boolean isAssignableFrom(Class clazz, Class superClass)
    {
        return superClass.isAssignableFrom(clazz);
    }

    /**
     * ��ϵͳĬ�ϵ���װ����ȥװ����
     *
     * @param className ���ض�Ӧ����
     * @throws ClassNotFoundException �����಻������ϵͳ����װ������
     */
    public static Class<?> loadClass(String className)
        throws ClassNotFoundException
    {
        ClassLoader loader = getClassLoader();
        return loader != null ? loadClass(loader, className) : Class.forName(className);
    }

    /**
     * ��ϵͳĬ�ϵ���װ����ȥװ����
     *
     * @param className ����
     * @param primitive �Ƿ����ڲ��Ķ���
     *                  ���ض�Ӧ����
     * @throws ClassNotFoundException �����಻������ϵͳ����װ������
     */
    public static final Class loadClass(String className, boolean primitive)
        throws ClassNotFoundException
    {
        Class clazz = null;
        if (primitive) {
            clazz = getPrimitiveClass(className);
        }
        if (clazz == null) {
            clazz = loadClass(className);
        }
        return clazz;
    }

    /**
     * ������װ����
     */
    public static final ClassLoader getClassLoader()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader;
    }

    /**
     * ����ThreadContextClassLoader
     *
     * @param classLoader ��װ����
     *                    ԭ�ȵ�ClassLoader
     */
    public static final ClassLoader switchClassLoader(ClassLoader classLoader)
    {
        Thread thread = Thread.currentThread();
        ClassLoader oldLoader = thread.getContextClassLoader();
        thread.setContextClassLoader(classLoader);
        return oldLoader;
    }

    /**
     * ����෵��Ĭ�ϵĹ�����
     *
     * @param clazz ��
     *              ������
     * @throws ReflectionException ������Ĭ�Ϲ�����
     */
    public static final Constructor getConstructor(Class clazz)
        throws ReflectionException
    {
        return getConstructor(clazz, (Class[])null);
    }

    /**
     * ����෵��Ĭ�ϵĹ�����
     *
     * @param clazz     ��
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ������Ĭ�Ϲ�����
     */
    public static final Constructor getConstructor(Class clazz, Class[] signature)
        throws ReflectionException
    {
        try {
            return clazz.getConstructor(signature);
        }
        catch (NoSuchMethodException ne) {
            throw new ReflectionException("No default constructor", ne);
        }
    }


    /**
     * ����෵��Ĭ�ϵĹ�����
     *
     * @param clazz     ��
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ������Ĭ�Ϲ�����
     */
    public static final Constructor getConstructor(Class clazz, String[] signature)
        throws ReflectionException
    {
        Class[] classes = getSignature(clazz, signature);
        return getConstructor(clazz, classes);
    }

    /**
     * ����෵��Ĭ�ϵĹ�����
     *
     * @param clazz     ��
     * @param signature �����б�
     * @param params    ����ʵ��
     *                  ������
     * @throws ReflectionException ������Ĭ�Ϲ�����
     */
    public static final Constructor getConstructor(Class clazz,
                                                   String[] signature,
                                                   Object[] params)
        throws ReflectionException
    {
        try {
            Class[] classes = getSignature(clazz, params, signature);
            return getConstructor(clazz, classes);
        }
        catch (ClassNotFoundException cnfe) {
            throw new ReflectionException("Signature class not found", cnfe);
        }
    }

    /**
     * ����෵��Ĭ�ϵĹ�����
     *
     * @param clazz     ��
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ������Ĭ�Ϲ�����
     */
    public static final Constructor getDeclaredConstructor(Class clazz, Class[] signature)
        throws ReflectionException
    {
        try {
            return clazz.getDeclaredConstructor(signature);
        }
        catch (NoSuchMethodException ne) {
            throw new ReflectionException("No default constructor", ne);
        }
    }


    /**
     * ����෵��Ĭ�ϵĹ�����
     *
     * @param clazz     ��
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ������Ĭ�Ϲ�����
     */
    public static final Constructor getDeclaredConstructor(Class clazz, String[] signature)
        throws ReflectionException
    {
        Class[] classes = getSignature(clazz, signature);
        return getDeclaredConstructor(clazz, classes);
    }

    /**
     * ����෵��Ĭ�ϵĹ�����
     *
     * @param clazz     ��
     * @param signature �����б�
     * @param params    ����ʵ��
     *                  ������
     * @throws ReflectionException ������Ĭ�Ϲ�����
     */
    public static final Constructor getDeclaredConstructor(Class clazz,
                                                           String[] signature,
                                                           Object[] params)
        throws ReflectionException
    {
        try {
            Class[] classes = getSignature(clazz, params, signature);
            return getDeclaredConstructor(clazz, classes);
        }
        catch (ClassNotFoundException cnfe) {
            throw new ReflectionException("Signature class not found", cnfe);
        }
    }

    /**
     * ��������Ĭ�ϵĹ�����
     *
     * @param className ����
     *                  ������
     * @throws ReflectionException ����಻���ڻ��߲�����Ĭ�ϵĹ�����
     */
    public static final Constructor getConstructor(String className)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return getConstructor(clazz);
    }

    /**
     * ��������Ĭ�ϵĹ�����
     *
     * @param className ����
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ����಻���ڻ��߲�����Ĭ�ϵĹ�����
     */
    public static final Constructor getConstructor(String className, Class[] signature)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return getConstructor(clazz, signature);
    }

    /**
     * ��������Ĭ�ϵĹ�����
     *
     * @param className ����
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ����಻���ڻ��߲�����Ĭ�ϵĹ�����
     */
    public static final Constructor getConstructor(String className, String[] signature)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return getConstructor(clazz, signature);
    }

    /**
     * ��������Ĭ�ϵĹ�����
     *
     * @param className ����
     * @param signature �����б�
     * @param params    ����ʵ��
     *                  ������
     * @throws ReflectionException ����಻���ڻ��߲�����Ĭ�ϵĹ�����
     */
    public static final Constructor getConstructor(String className,
                                                   String[] signature,
                                                   Object[] params)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return getConstructor(clazz, signature, params);
    }

    /**
     * ��������Ĭ�ϵĹ�����
     *
     * @param className ����
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ����಻���ڻ��߲�����Ĭ�ϵĹ�����
     */
    public static final Constructor getDeclaredConstructor(String className, Class[] signature)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return getDeclaredConstructor(clazz, signature);
    }

    /**
     * ��������Ĭ�ϵĹ�����
     *
     * @param className ����
     * @param signature �����б�
     *                  ������
     * @throws ReflectionException ����಻���ڻ��߲�����Ĭ�ϵĹ�����
     */
    public static final Constructor getDeclaredConstructor(String className, String[] signature)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return getDeclaredConstructor(clazz, signature);
    }

    /**
     * ��������Ĭ�ϵĹ�����
     *
     * @param className ����
     * @param signature �����б�
     * @param params    ����ʵ��
     *                  ������
     * @throws ReflectionException ����಻���ڻ��߲�����Ĭ�ϵĹ�����
     */
    public static final Constructor getDeclaredConstructor(String className,
                                                           String[] signature,
                                                           Object[] params)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return getDeclaredConstructor(clazz, signature, params);
    }

    /**
     * �ø����װ����ȥװ����
     *
     * @param className ���ض�Ӧ����
     * @throws ClassNotFoundException �����಻�����ڸ��װ������
     */
    public static final Class loadClass(ClassLoader loader,
                                        String className)
        throws ClassNotFoundException
    {
        return loader != null ? loader.loadClass(className) : loadClass(className);
    }

    public static final URL getResource(String resourceName)
    {
        return getResource(null, resourceName);
    }

    public static final URL getResource(ClassLoader loader,
                                        String resourceName)
    {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        if (loader == null) {
            loader = getClassLoader();
        }
        return loader.getResource(resourceName);
    }

    public static final Enumeration<URL> getResources(String resourceName)
        throws IOException
    {
        return getResources(null, resourceName);
    }

    public static final Enumeration<URL> getResources(ClassLoader loader, String resourceName)
        throws IOException
    {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        if (loader == null) {
            loader = getClassLoader();
        }
        return loader.getResources(resourceName);
    }

    public static final InputStream getResourceAsStream(String resourceName)
    {
        return getResourceAsStream(null, resourceName);
    }

    public static final InputStream getResourceAsStream(ClassLoader loader,
                                                        String resourceName)
    {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }

        if (loader == null) {
            loader = getClassLoader();
        }

        return loader.getResourceAsStream(resourceName);
    }

    /**
     * Gets an instance of a specified class.
     *
     * @param className the name of class.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static final Object newInstance(String className)
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return newInstance(clazz);
    }

    /**
     * Gets an instance of a specified class.
     *
     * @param className the name of class.
     * @param singleton �ǲ��ǵ�������ǵ������Ȼ�����Ӧ��"getInstance"������
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static <T> T newInstance(String className, boolean singleton)
        throws ReflectionException
    {
        Class<T> clazz = (Class<T>)internalLoadClass(className);
        return newInstance(clazz, singleton);
    }

    /**
     * Gets an instance of a specified class.
     *
     * @param clazz the class.
     *              the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static <T> T newInstance(Class<T> clazz)
        throws ReflectionException
    {
        try {
            return clazz.newInstance();
        }
        catch (Exception x) {
            throw new ReflectionException("Instantiation failed for " + clazz.getName(), x);
        }
    }

    /**
     * Gets an instance of a specified class.
     *
     * @param clazz the class.
     *              the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static <T> T newInstance(Class<T> clazz, boolean singleton)
        throws ReflectionException
    {
        if (singleton) {
            //����getInstance�����������ڵ���֮
            try {
                //ֻ�����Լ������� getInstance���ŷ�������ʳ����getInstance
                Method method = clazz.getDeclaredMethod("getInstance");
                if (Modifier.isStatic(method.getModifiers())) {
                    return (T)Reflection.invoke(null, method);
                }
            }
            catch (NoSuchMethodException ex) {
            }
            catch (ReflectionException ex) {
            }
        }
        try {
            return clazz.newInstance();
        }
        catch (Exception x) {
            throw new ReflectionException("Instantiation failed for " + clazz.getName(), x);
        }
    }

    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param clazz   the class.
     * @param params  an array containing the parameters of the constructor.
     * @param classes an array containing the parameters class of the constructor.
     *                the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static final Object newInstance(Class clazz,
                                           Object params[],
                                           Class classes[])
        throws ReflectionException
    {
        try {
            return clazz.getConstructor(classes).newInstance(params);
        }
        catch (Exception x) {
            throw new ReflectionException("Instantiation failed for " + clazz.getName(), x);
        }
    }


    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param className the name of class.
     * @param params    an array containing the parameters of the constructor.
     * @param classes   an array containing the parameters class of the constructor.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static final Object newInstance(String className,
                                           Object params[],
                                           Class classes[])
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return newInstance(clazz, params, classes);
    }

    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param clazz     the class.
     * @param params    an array containing the parameters of the constructor.
     * @param signature an array containing the signature of the constructor.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static final Object newInstance(Class clazz,
                                           Object params[],
                                           String signature[])
        throws ReflectionException
    {
        /* Try to construct. */
        try {
            Class[] sign = getSignature(clazz, params, signature);
            return newInstance(clazz, params, sign);
        }
        catch (ReflectionException re) {
            throw re;
        }
        catch (Exception x) {
            throw new ReflectionException("Instantiation failed for " + clazz.getName(), x);
        }
    }

    /**
     * Gets an instance of a specified class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param className the name of class.
     * @param params    an array containing the parameters of the constructor.
     * @param signature an array containing the signature of the constructor.
     *                  the instance.
     * @throws ReflectionException if instantiation fails.
     */
    public static final Object newInstance(String className,
                                           Object params[],
                                           String signature[])
        throws ReflectionException
    {
        Class clazz = internalLoadClass(className);
        return newInstance(clazz, params, signature);
    }

    /**
     * �ڲ�װ����
     *
     * @param className
     * @throws ReflectionException
     */
    static final Class<?> internalLoadClass(String className)
        throws ReflectionException
    {
        try {
            return loadClass(className);
        }
        catch (Exception ex) {
            throw new ReflectionException("Loading class error:" + className, ex);
        }
    }

    /**
     * Gets the class of a primitive type.
     *
     * @param type a primitive type.
     *             the corresponding class, or null.
     */
    public static final Class getPrimitiveClass(String type)
    {
        return (Class)primitiveClasses.get(type);
    }

    /**
     * �ж��Ƿ����ڻ��������
     *
     * @param className
     */
    public static final boolean isPrimitiveClass(String className)
    {
        return primitiveClasses.containsKey(className);
    }

    /**
     * �ж��Ƿ����ڻ��������
     *
     * @param clazz
     */
    public static final boolean isPrimitiveClass(Class clazz)
    {
        return primitiveClasses.containsValue(clazz);
    }

    /**
     * check for primitive and widening.  Take from the 1.4 code
     */
    public static final boolean checkPrimitive(Class formal, Class arg)
    {

        if (formal.isPrimitive()) {
            if (formal == Boolean.TYPE && arg == Boolean.class) {
                return true;
            }

            if (formal == Character.TYPE && arg == Character.class) {
                return true;
            }

            if (formal == Byte.TYPE && arg == Byte.class) {
                return true;
            }

            if (formal == Short.TYPE &&
                (arg == Short.class || arg == Byte.class)) {
                return true;
            }

            if (formal == Integer.TYPE &&
                (arg == Integer.class || arg == Short.class ||
                 arg == Byte.class)) {
                return true;
            }

            if (formal == Long.TYPE &&
                (arg == Long.class || arg == Integer.class ||
                 arg == Short.class || arg == Byte.class)) {
                return true;
            }

            if (formal == Float.TYPE &&
                (arg == Float.class || arg == Long.class ||
                 arg == Integer.class || arg == Short.class ||
                 arg == Byte.class)) {
                return true;
            }

            if (formal == Double.TYPE &&
                (arg == Double.class || arg == Float.class ||
                 arg == Long.class || arg == Integer.class ||
                 arg == Short.class || arg == Byte.class)) {
                return true;
            }
        }

        return false;
    }


    /**
     * �������װ����
     *
     * @param className ��������ǻ����͵�����int byte�ȣ�
     *                  ��Ӧ����
     */
    public static final Class getClass(String className)
        throws ClassNotFoundException
    {
        return getClass(className, getClassLoader());
    }

    /**
     * �������װ����
     *
     * @param className ��������ǻ����͵�����int byte�ȣ�
     *                  ��Ӧ����
     */
    public static final Class getClass(String className, ClassLoader loader)
        throws ClassNotFoundException
    {
        if (className == null) {
            throw new IllegalArgumentException("Null class name");
        }
        if (loader == null) {
            loader = getClassLoader();
        }
        Class clazz = getPrimitiveClass(className);
        if (clazz != null) {
            return clazz;
        }
        else if (className.endsWith(ARRAY_SUFFIX)) {
            // special handling for array class names
            int len = className.length() + 1;
            StringBuilder sb = new StringBuilder(len + 1);
            sb.append("[L").append(className);
            sb.setCharAt(sb.length() - 2, ';');
            sb.setLength(len);
            return Class.forName(sb.toString(), true, loader);
        }
        else {
            clazz = Class.forName(className, true, loader);
        }
        return clazz;
    }

    /**
     * @param params �����б�
     */
    public static final Class[] getSignature(Object[] params)
    {
        if (params == null) {
            return null;
        }
        else if (params.length == 0) {
            return NO_SUCH_CLASS;
        }
        Class[] classes = new Class[params.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = params[i].getClass();
        }
        return classes;
    }

    /**
     * @param clazz
     * @param signature
     * @throws ReflectionException
     */
    public static final Class[] getSignature(Class clazz, String[] signature)
        throws ReflectionException
    {
        Class[] classes = null;
        try {
            classes = getSignature(clazz, null, signature);
        }
        catch (ClassNotFoundException e) {
            throw new ReflectionException("Signature class not found", e);
        }
        return classes;
    }

    /**
     * Gets the signature classes for parameters of a method of a class.
     *
     * @param clazz     the class.
     * @param params    an array containing the parameters of the method.
     * @param signature an array containing the signature of the method.
     *                  an array of signature classes. Note that in some cases
     *                  objects in the parameter array can be switched to the context
     *                  of a different class loader.
     * @throws ClassNotFoundException if any of the classes is not found.
     */
    public static final Class[] getSignature(Class clazz,
                                             Object[] params,
                                             String[] signature)
        throws ClassNotFoundException
    {
        if (signature != null) {
            ClassLoader tempLoader;
            ClassLoader loader = clazz.getClassLoader();
            Class[] sign = new Class[signature.length];
            for (int i = 0; i < signature.length; i++) {
                /* Check primitive types. */
                sign[i] = getPrimitiveClass(signature[i]);
                if (sign[i] == null) {
                    /* Not a primitive one, continue building. */
                    if (loader != null) {
                        /* Use the class loader of the target object. */
                        sign[i] = loader.loadClass(signature[i]);
                        tempLoader = sign[i].getClassLoader();
                        if (params != null && params[i] != null && (tempLoader != null)
                            && !tempLoader.equals(params[i].getClass().getClassLoader())) {
                            params[i] = switchObject(params[i], loader);
                        }
                    }
                    else {
                        /* Use the default class loader. */
                        sign[i] = loadClass(signature[i]);
                    }
                }
            }
            return sign;
        }
        else {
            return null;
        }
    }

    /**
     * Switches an object into the context of a different class loader.
     *
     * @param object an object to switch.
     * @param loader the loader of the new context.
     */
    public static final Object switchObject(Object object, ClassLoader loader)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(object);
            out.flush();
        }
        catch (Exception x) {
            StreamUtil.close(baos);
            return object;
        }

        try {
            ReflectionInputStream in = new ReflectionInputStream(baos.getInputStream(), loader);
            return in.readObject();
        }
        catch (Exception x) {
            return object;
        }
        finally {
            StreamUtil.close(baos);
        }
    }

    /**
     * �����ͷ�����ط���
     *
     * @param clazz      ��
     * @param methodName ������
     * @throws ReflectionException
     */
    public static final Method getMethod(Class<?> clazz, String methodName)
        throws ReflectionException
    {
        try {
            return clazz.getMethod(methodName);
        }
        catch (Exception e) {
            throw new ReflectionException("Get method failed for:" + clazz.getName()
                                            + "#" + methodName, e);
        }
    }

    /**
     * �����ͷ������������ͬ��һ�鷽��
     *
     * @param clazz      ��
     * @param methodName ������
     */
    public static final Method[] getMethods(Class clazz, String methodName)
    {
        Method[] methods = clazz.getMethods();
        List<Method> list = new ArrayList<Method>();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                list.add(methods[i]);
            }
        }

        if (list.size() > 0) {
            methods = (Method[])list.toArray(new Method[list.size()]);
        }
        else {
            methods = NO_SUCH_METHOD;
        }
        return methods;
    }

    public static final Method[] NO_SUCH_METHOD = new Method[0];
    public static final Class[] NO_SUCH_CLASS = EMPTY_CLASS_ARRAY;

    /**
     * �����ͷ�����ط���
     *
     * @param clazz      ��
     * @param methodName ������
     * @param signature  �����б�
     * @throws ReflectionException
     */
    public static final Method getMethod(Class clazz,
                                         String methodName,
                                         Class[] signature)
        throws ReflectionException
    {
        try {
            return clazz.getMethod(methodName, signature);
        }
        catch (Exception e) {
            throw new ReflectionException("Get method failed for:" + clazz.getName()
                                            + "#" + methodName, e);
        }
    }

    /**
     * �����ͷ�����ط���
     *
     * @param clazz      ��
     * @param methodName ������
     * @param signature  �����б�
     * @throws ReflectionException
     */
    public static final Method getMethod(Class clazz,
                                         String methodName,
                                         String[] signature)
        throws ReflectionException
    {
        return getMethod(clazz, methodName, signature, null);
    }

    /**
     * �����ͷ�����ط���
     *
     * @param clazz      ��
     * @param methodName ������
     * @param signature  �����б�
     * @throws ReflectionException
     */
    private static Method getMethod(Class clazz,
                                    String methodName,
                                    String[] signature,
                                    Object[] params)
        throws ReflectionException
    {
        Class[] classes = null;
        try {
            classes = getSignature(clazz, params, signature);
        }
        catch (Exception e) {
            throw new ReflectionException("Get method failed for:" + clazz.getName()
                                           + "#" + methodName, e);
        }
        return getMethod(clazz, methodName, classes);
    }

    /**
     * �����ͷ�����ط���
     *
     * @param clazz      ��
     * @param methodName ������
     * @param signature  �����б�
     * @throws ReflectionException
     */
    public static final Method getDeclaredMethod(Class clazz,
                                                 String methodName,
                                                 Class[] signature)
        throws ReflectionException
    {
        try {
            return clazz.getDeclaredMethod(methodName, signature);
        }
        catch (Exception e) {
            throw new ReflectionException("Get declared method failed for:" + clazz.getName()
                                            + "#" + methodName, e);
        }
    }

    /**
     * �����ͷ�����ط���
     *
     * @param clazz      ��
     * @param methodName ������
     * @param signature  �����б�
     * @throws ReflectionException
     */
    public static final Method getDeclaredMethod(Class clazz,
                                                 String methodName,
                                                 String[] signature)
        throws ReflectionException
    {
        return getDeclaredMethod(clazz, methodName, signature, null);
    }

    /**
     * �����ͷ�����ط���
     *
     * @param clazz      ��
     * @param methodName ������
     * @param signature  �����б�
     * @throws ReflectionException
     */
    private static Method getDeclaredMethod(Class clazz,
                                            String methodName,
                                            String[] signature,
                                            Object[] params)
        throws ReflectionException
    {
        Class[] classes = null;
        try {
            classes = getSignature(clazz, params, signature);
        }
        catch (Exception e) {
            throw new ReflectionException("Get method failed for:" + clazz.getName()
                                           + "#" + methodName, e);
        }
        return getDeclaredMethod(clazz, methodName, classes);
    }

    /**
     * ִ�з���
     *
     * @param obj    ����
     * @param method ������
     * @throws ReflectionException
     */
    public static final Object invoke(Object obj, Method method)
        throws ReflectionException
    {
        return invoke(obj, method, null);
    }


    /**
     * ��ȡObjʵ�����
     *
     * @param obj
     * @param methodNames �����ǹ淶��ʵ�巽�����
     * @return
     */
    public static final Object beanMethodsInvoke(Object obj, String[] methodNames)
    {
        if (ArrayUtil.isValid(methodNames)) {
            int len = methodNames.length;
            if (len == 1) {
                return invoke(obj, methodNames[0]);
            }
            else {
                Object target = obj;
                for (int i = 0; i < len; i++) {
                    target = invoke(target, methodNames[i]);
                    if (target == null) {
                        return target;
                    }
                }
                return target;
            }
        }
        return null;
    }

    /**
     * ִ�з���
     *
     * @param obj    ����
     * @param method ������
     * @throws ReflectionException
     */
    public static final Object invoke(Object obj, Method method, Object[] params)
        throws ReflectionException
    {
        try {
            return method.invoke(obj, params);
        }
        catch (Exception e) {
            throw new ReflectionException("Invoke method failed for:" + obj.getClass().getName()
                                                + "#" + method.getName(), e);
        }
    }

    /**
     * ִ�з���
     *
     * @param obj        ����
     * @param methodName ������
     * @throws ReflectionException
     */
    public static final Object invoke(Object obj, String methodName)
        throws ReflectionException
    {
        Class clazz = obj.getClass();
        Method method = getMethod(clazz, methodName);
        return invoke(obj, method);
    }

    /**
     * ִ�з���
     *
     * @param obj        ����
     * @param methodName ������
     * @throws ReflectionException
     */
    public static final Object invoke(Object obj, String methodName,
                                      Class[] signature, Object[] params)
        throws ReflectionException
    {
        Class clazz = obj.getClass();
        Method method = getMethod(clazz, methodName, signature);
        return invoke(obj, method, params);
    }

    /**
     * ִ�з���
     *
     * @param obj        ����
     * @param methodName ������
     * @throws ReflectionException
     */
    public static final Object invoke(Object obj, String methodName,
                                      String[] signature, Object[] params)
        throws ReflectionException
    {
        Class clazz = obj.getClass();
        Method method = getMethod(clazz, methodName, signature, params);
        return invoke(obj, method, params);
    }
}
