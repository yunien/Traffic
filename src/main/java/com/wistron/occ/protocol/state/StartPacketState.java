package com.wistron.occ.protocol.state;

import com.wistron.occ.enums.ControlCode;
import com.wistron.occ.protocol.packet.PacketFactory;

public class StartPacketState implements PacketState {
    @Override
    public void feed(PacketFactory packetFactory, byte b) {
        switch (ControlCode.lookup(b)) {
            case DLE:
                break;
            case STX:
                packetFactory.setPacketState(new NormalPacketState());
                break;
            case ETX:
                break;
            case ACK:
                packetFactory.setPacketState(new AckPacketState());
                break;
            case NAK:
                packetFactory.setPacketState(new NackPacketState());
                break;
            case ADDR1:
                break;
            case ADDR2:
                break;
        }
    }
}
