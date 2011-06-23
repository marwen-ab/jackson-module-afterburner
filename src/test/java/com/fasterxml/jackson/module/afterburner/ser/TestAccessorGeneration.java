package com.fasterxml.jackson.module.afterburner.ser;

import java.lang.reflect.Method;

import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;

import com.fasterxml.jackson.module.afterburner.AfterburnerTestBase;

public class TestAccessorGeneration extends AfterburnerTestBase
{
    /*
    /**********************************************************************
    /* Helper types
    /**********************************************************************
     */

    public static class Bean {
        public int getX() { return 13; }
        public int getY() { return 27; }

        public int get3() { return 3; }
        public int get4() { return 4; }
        public int get5() { return 5; }
        public int get6() { return 6; }
        public int get7() { return 7; }
    }
    
    /*
    /**********************************************************************
    /* Test methods
    /**********************************************************************
     */
    
    public void testSingleIntAccessorGeneration() throws Exception
    {
        Method method = Bean.class.getDeclaredMethod("getX");
        AnnotatedMethod annMethod = new AnnotatedMethod(method, null, null);
        PropertyCollector coll = new PropertyCollector();
        BeanPropertyWriter bpw = new BeanPropertyWriter(annMethod, null,
                new SerializedString("x"), null,
                null, null, null,
                method, null, false, null);
        coll.addIntGetter(bpw);
        BeanPropertyAccessor acc = coll.findAccessor(Bean.class);
        Bean bean = new Bean();
        int value = acc.intGetter(bean, 0);
        assertEquals(bean.getX(), value);
    }

    public void testDualIntAccessorGeneration() throws Exception
    {
        PropertyCollector coll = new PropertyCollector();

        String[] methodNames = new String[] {
                "getX", "getY", "get3"
        };
        for (String methodName : methodNames) {
            Method method = Bean.class.getDeclaredMethod(methodName);
            AnnotatedMethod annMethod = new AnnotatedMethod(method, null, null);
            coll.addIntGetter(new BeanPropertyWriter(annMethod, null,
                    new SerializedString(methodName), null,
                    null, null, null,
                    method, null, false, null));
        }

        BeanPropertyAccessor acc = coll.findAccessor(Bean.class);
        Bean bean = new Bean();

        assertEquals(bean.getX(), acc.intGetter(bean, 0));
        assertEquals(bean.getY(), acc.intGetter(bean, 1));
        assertEquals(bean.get3(), acc.intGetter(bean, 2));
    }

    // And then test to ensure Switch-table construction also works...
    public void testLotsaIntAccessorGeneration() throws Exception
    {
        PropertyCollector coll = new PropertyCollector();
        String[] methodNames = new String[] {
                "getX", "getY", "get3", "get4", "get5", "get6", "get7"
        };
        for (String methodName : methodNames) {
            Method method = Bean.class.getDeclaredMethod(methodName);
            AnnotatedMethod annMethod = new AnnotatedMethod(method, null, null);
            coll.addIntGetter(new BeanPropertyWriter(annMethod, null,
                    new SerializedString(methodName), null,
                    null, null, null,
                    method, null, false, null));
        }

        BeanPropertyAccessor acc = coll.findAccessor(Bean.class);
        Bean bean = new Bean();

        assertEquals(bean.getX(), acc.intGetter(bean, 0));
        assertEquals(bean.getY(), acc.intGetter(bean, 1));

        assertEquals(bean.get3(), acc.intGetter(bean, 2));
        assertEquals(bean.get4(), acc.intGetter(bean, 3));
        assertEquals(bean.get5(), acc.intGetter(bean, 4));
        assertEquals(bean.get6(), acc.intGetter(bean, 5));
        assertEquals(bean.get7(), acc.intGetter(bean, 6));
    }
}