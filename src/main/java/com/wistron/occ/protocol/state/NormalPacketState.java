package com.wistron.occ.protocol.state;

import com.wistron.occ.protocol.packet.NormalPacket;
import com.wistron.occ.protocol.packet.PacketFactory;

public class NormalPacketState implements PacketState {

    private int index = 0;

    // seq(1), addr(2), len(2), dle(1), etx(1), cks(1)
    private byte[] codes = new byte[8];

    private int len, remain;
    private byte[] info;

    @Override
    public void feed(PacketFactory packetFactory, byte b) {
        switch (index) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
                codes[index] = b;
                index++;
                break;
            case 4:
                if (len == 0) {
                    codes[index] = b;
                    len = Byte.toUnsignedInt(codes[3]) * 256 + Byte.toUnsignedInt(codes[4]) -10;
                    remain = len;
                    info = new byte[len];
                } else {
                    if (remain == 1) {
                        index++;
                    }
                    info[len - remain] = b;
                    remain--;
                }
                break;
            case 7:
                codes[index] = b;
                packetFactory.addPacket(new NormalPacket(codes, info));
                packetFactory.setPacketState(new EmptyPacketState());
                break;
        }
    }
}
