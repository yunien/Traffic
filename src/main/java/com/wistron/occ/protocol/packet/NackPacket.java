package com.wistron.occ.protocol.packet;

public class NackPacket extends Packet {

    private byte err;

    public NackPacket(byte seq, byte[] addr, byte[] len, byte err, byte cks) {
        super(seq, addr, len);
        this.err = err;
        validateCks(cks);
    }

    public NackPacket(byte[] codes) {
        super(codes[0], new byte[] {codes[1], codes[2]}, new byte[] {codes[3], codes[4]});
        err = codes[5];
        validateCks(codes[6]);
    }
}
