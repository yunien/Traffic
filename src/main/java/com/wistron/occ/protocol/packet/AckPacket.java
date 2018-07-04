package com.wistron.occ.protocol.packet;

public class AckPacket extends Packet {
    public AckPacket(byte seq, byte[] addr, byte[] len, byte cks) {
        super(seq, addr, len);
        validateCks(cks);
    }
}
