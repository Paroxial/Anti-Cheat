package com.paroxial.anticheat.packet;

import com.paroxial.anticheat.utils.ReflectionUtils;
import lombok.Getter;

@Getter
public class WrappedPacket {
    private final Object packet;
    private final PacketDirection protocol;
    private final String packetName;

    public WrappedPacket(Object packet, PacketDirection protocol) {
        this.packet = packet;
        this.protocol = protocol;
        this.packetName = packet.getClass().getSimpleName();
    }

    public boolean hasName(String name) {
        return packetName.equals(name);
    }

    public boolean hasName(String... names) {
        for (String name : names) {
            if (name.equals(packetName)) {
                return true;
            }
        }

        return false;
    }

    public <T> T getField(String fieldName) {
        return getField(packet.getClass(), fieldName);
    }

    public <T> T getField(String fieldName, boolean useSuper) {
        if (useSuper) {
            return getField(packet.getClass().getSuperclass(), fieldName);
        } else {
            return getField(fieldName);
        }
    }

    public <T> T getField(Class<?> reflector, String fieldName) {
        return (T) ReflectionUtils.getFieldValue(reflector, packet, fieldName);
    }

    public <T> void setField(String fieldName, T value) {
        this.setField(packet.getClass(), fieldName, value);
    }

    public <T> void setField(Class<?> reflector, String fieldName, T value) {
        ReflectionUtils.setFieldValue(packet.getClass(), packet, fieldName, value);
    }
}
