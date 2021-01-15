package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field fieldTarget = target.getClass().getDeclaredField(fieldName);

            if (!fieldTarget.isAccessible()) {
                fieldTarget.setAccessible(true);
                wasPrivate = true;
            }

            fieldTarget.set(target, toInject);

            if (wasPrivate) {
                fieldTarget.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
