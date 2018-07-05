package com.wistron.occ.protocol.state;

import com.wistron.occ.protocol.packet.NackPacket;
import com.wistron.occ.protocol.packet.PacketFactory;

public class NackPacketState implements PacketState {
    private int index = 0;
    private byte[] codes = new byte[7];


    @Override
    public void feed(PacketFactory packetFactory, byte b) {
        codes[index] = b;

        if (index == 6) {
            packetFactory.addPacket(new NackPacket(codes));
            packetFactory.setPacketState(new EmptyPacketState());
        } else {
            index++;
        }
    }
}
