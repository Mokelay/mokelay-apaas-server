package com.greatbee.base.util.lang.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * ָ����ͬ��װ������������
 *
 * @author system
 * @version Dec 24, 2002 5:15:44 PM
 */
public class ReflectionInputStream extends ObjectInputStream
{
    /**
     * The class loader of the context.
     */
    private ClassLoader loader;

    /**
     * Contructs a new object stream for a context.
     *
     * @param in     the serialized input stream.
     * @param loader the class loader of the context.
     * @throws IOException on errors.
     */
    public ReflectionInputStream(InputStream in, ClassLoader loader)
        throws IOException
    {
        super(in);
        this.loader = loader;
    }

    protected Class resolveClass(ObjectStreamClass v)
        throws IOException,
        ClassNotFoundException
    {
        return loader == null ? super.resolveClass(v) :
               loader.loadClass(v.getName());
    }
}
