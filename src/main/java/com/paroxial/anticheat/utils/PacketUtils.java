package com.paroxial.anticheat.utils;

import com.paroxial.anticheat.packet.WrappedPacket;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class PacketUtils {
    public boolean isFlyingPacket(WrappedPacket packet) {
        return packet.hasName("PacketPlayInPosition", "PacketPlayInPositionLook", "PacketPlayInFlying",
                "PacketPlayInLook");
    }

    public boolean flyingHasPosition(WrappedPacket wrapper) {
        Object packet = wrapper.getPacket();
        Class<?> reflector;

        if (wrapper.hasName("PacketPlayInFlying")) {
            reflector = packet.getClass();
        } else {
            reflector = packet.getClass().getSuperclass();
        }

        return wrapper.getField(reflector, "hasPos");
    }

    public float getFlyingPitch(WrappedPacket wrapper) {
        Object packet = wrapper.getPacket();
        Class<?> reflector;

        if (wrapper.hasName("PacketPlayInFlying")) {
            reflector = packet.getClass();
        } else {
            reflector = packet.getClass().getSuperclass();
        }

        if (wrapper.getField(reflector, "hasLook")) {
            return wrapper.getField(reflector, "pitch");
        }

        return 0;
    }

    public Location getLocationFromFlyingPacket(WrappedPacket wrapper, World world) {
        Object packet = wrapper.getPacket();
        Class<?> reflector;

        if (wrapper.hasName("PacketPlayInFlying")) {
            reflector = packet.getClass();
        } else {
            reflector = packet.getClass().getSuperclass();
        }

        boolean hasPos = wrapper.getField(reflector, "hasPos");

        if (!hasPos) {
            return null;
        }

        double x = wrapper.getField(reflector, "x");
        double y = wrapper.getField(reflector, "y");
        double z = wrapper.getField(reflector, "z");

        Location location = new Location(world, x, y, z);

        boolean hasLook = wrapper.getField(reflector, "hasLook");

        if (hasLook) {
            float yaw = wrapper.getField(reflector, "yaw");
            float pitch = wrapper.getField(reflector, "pitch");

            location.setYaw(yaw);
            location.setPitch(pitch);
        }

        return location;
    }
}
