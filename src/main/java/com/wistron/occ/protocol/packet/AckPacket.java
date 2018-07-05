package com.wistron.occ.protocol.packet;

public class AckPacket extends Packet {
    public AckPacket(byte seq, byte[] addr, byte[] len, byte cks) {
        super(seq, addr, len);
        validateCks(cks);
    }

    public AckPacket(byte[] codes) {
        super(codes[0], new byte[] {codes[1], codes[2]}, new byte[] {codes[3], codes[4]});
        validateCks(codes[5]);
    }
}
