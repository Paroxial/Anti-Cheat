package com.paroxial.anticheat.packet.handler;

import com.paroxial.anticheat.packet.WrappedPacket;

public interface PacketHandler {
    void handlePacket(WrappedPacket packet);
}
