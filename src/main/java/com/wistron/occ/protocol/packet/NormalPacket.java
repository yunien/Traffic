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

    public NormalPacket(
            byte[] codes, // seq(1), addr(2), len(2), dle(1), etx(1), cks(1)
            byte[] info)
    {
        super(codes[0], new byte[] {codes[1], codes[2]}, new byte[] {codes[3], codes[4]});
        this.info = info;
        this.dle = codes[5];
        this.etx = codes[6];
        validateCks(codes[7]);
    }
}
