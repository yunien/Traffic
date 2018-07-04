package com.wistron.occ.protocol.state;

import com.wistron.occ.protocol.packet.PacketFactory;

public interface PacketState {

    void feed(PacketFactory packetFactory, byte b);
}
