package physics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PhysicsEngineFactory {
    private static HashMap<String, Class<?>> engines = new HashMap();

    public PhysicsEngineFactory() {
    }

    public static void registerPhysicsEngine(String engineName, Class<?> engineClass) {
        engines.put(engineName, engineClass);
    }

    public static PhysicsEngine createPhysicsEngine(String engineClassName) {
        String postFix;
        String key;
        try {
            Class.forName(engineClassName);
        } catch (ClassNotFoundException var15) {
            postFix = "PhysicsEngine";
            key = "";
            if (engineClassName.contains(postFix)) {
                key = key + engineClassName.substring(0, engineClassName.length() - postFix.length()) + "." + engineClassName;
            } else {
                key = key + engineClassName + "." + engineClassName + "PhysicsEngine";
            }

            try {
                Class.forName("physics." + key);
            } catch (ClassNotFoundException var13) {
            } catch (NoClassDefFoundError var14) {
                findClassByErrorMessage(var14.getMessage());
            }
        } catch (NoClassDefFoundError var16) {
            findClassByErrorMessage(var16.getMessage());
        }

        Class<PhysicsEngine> engineClass = (Class)engines.get(engineClassName);
        if (engineClass == null) {
            Set<String> keys = engines.keySet();
            key = null;
            String temp = null;
            Iterator var6 = keys.iterator();

            while(var6.hasNext()) {
                String s = (String)var6.next();
                temp = s.toLowerCase();
                if (temp.contains(engineClassName.toLowerCase())) {
                    key = s;
                    break;
                }
            }

            if (key == null) {
                throw new IllegalArgumentException("Could not find Physics Engine given name: " + engineClassName + ". This engine may exist but is not properly" + " registered with this factory, likely indicating it should not be used.");
            }

            engineClass = (Class)engines.get(key);
            System.err.println("NOTE: " + engineClassName + " is not recognized as" + " a full class name. " + "Attempted to find it which resulted in using engine: " + key + ".");
        }

        postFix = null;

        Constructor engineConstructor;
        try {
            engineConstructor = engineClass.getDeclaredConstructor();
        } catch (SecurityException var11) {
            throw new RuntimeException("PhysicsEngineFactory.createPhysicsEngine(): SecurityException getting engine constructor:  \n" + var11);
        } catch (NoSuchMethodException var12) {
            throw new RuntimeException("PhysicsEngineFactory.createPhysicsEngine(): NoSuchMethodException getting engine constructor: \n" + var12);
        }

        key = null;

        try {
            PhysicsEngine engine = (PhysicsEngine)engineConstructor.newInstance();
            return engine;
        } catch (IllegalArgumentException var7) {
            throw new RuntimeException("PhysicsEngineFactory.createPhysicsEngine(): IllegalArgumentException instantiating engine: \n" + var7);
        } catch (InstantiationException var8) {
            throw new RuntimeException("PhysicsEngineFactory.createPhysicsEngine(): InstantiationException instantiating engine: \n" + var8);
        } catch (IllegalAccessException var9) {
            throw new RuntimeException("PhysicsEngineFactory.createPhysicsEngine(): IllegalAccessException instantiating engine: \n" + var9);
        } catch (InvocationTargetException var10) {
            System.out.println("PhysicsEngineFactory: Exception constructing engine '" + engineClassName + "': " + var10.getCause());
            throw new RuntimeException("PhysicsEngineFactory.createPhysicsEngine(): InvocationTargetException instantiating engine: \n" + var10);
        }
    }

    private static void findClassByErrorMessage(String message) {
        int i0 = message.indexOf("wrong name:");
        int i1 = -1;
        int i2 = -1;
        if (i0 != -1) {
            i1 = message.indexOf("sage", i0);
            i2 = message.indexOf(")", i1);
        }

        if (i1 != -1 && i2 != -1) {
            message = message.substring(i1, i2);
            message = message.replace('/', '.');

            try {
                Class.forName(message);
            } catch (ClassNotFoundException var5) {
            }
        }

    }
}
