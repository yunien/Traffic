package com.wistron.occ.protocol.packet;

public class NormalPacket extends Packet {

    private byte[] info;
    private byte dle;
    private byte etx;


    public NormalPacket(byte seq, byte[] addr, byte[] len, byte[] info, byte dle, byte etx, byte cks) {
        super(seq, addr, len);
        this.info = info;
        this.dle = dle;
        this.etx = etx;
        validateCks(cks);
    }
}
