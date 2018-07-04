package com.wistron.occ.protocol.packet;

public class NackPacket extends Packet {

    private byte err;

    public NackPacket(byte seq, byte[] addr, byte[] len, byte err, byte cks) {
        super(seq, addr, len);
        this.err = err;
        validateCks(cks);
    }
}
