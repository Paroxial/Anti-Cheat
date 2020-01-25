package com.paroxial.anticheat.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class ReflectionUtils {
    public Object getFieldValue(Class<?> clazz, Object instance, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setFieldValue(Class<?> clazz, Object instance, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPing(Player player) {
        try {
            Method getHandleMethod = player.getClass().getDeclaredMethod("getHandle");
            getHandleMethod.setAccessible(true);

            Object entityPlayer = getHandleMethod.invoke(player);

            Field pingField = entityPlayer.getClass().getDeclaredField("ping");
            pingField.setAccessible(true);

            int ping = pingField.getInt(entityPlayer);

            return Math.max(ping, 0);
        } catch (Exception e) {
            return 0;
        }
    }
}
