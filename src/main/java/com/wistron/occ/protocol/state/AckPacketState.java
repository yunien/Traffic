package com.wistron.occ.protocol.state;

import com.wistron.occ.protocol.packet.AckPacket;
import com.wistron.occ.protocol.packet.PacketFactory;

public class AckPacketState implements PacketState {
    private int index = 0;
    private byte[] codes = new byte[6];

    @Override
    public void feed(PacketFactory packetFactory, byte b) {
        codes[index] = b;

        if (index == 5) {
            packetFactory.addPacket(new AckPacket(codes));
            packetFactory.setPacketState(new EmptyPacketState());
        } else {
            index++;
        }
    }
}
