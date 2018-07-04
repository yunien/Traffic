package com.wistron.occ.protocol.state;

import com.wistron.occ.enums.ControlCode;
import com.wistron.occ.protocol.packet.PacketFactory;

public class EmptyPacketState implements PacketState {


    @Override
    public void feed(PacketFactory packetFactory, byte b) {
        if (ControlCode.DLE.equals(b)) {
            packetFactory.setPacketState(new StartPacketState());
        }
    }
}
