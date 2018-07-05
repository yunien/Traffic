package com.wistron.occ.protocol.packet;

import com.wistron.occ.enums.ControlCode;

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

    @Override
    public byte[] getNative() {
        int length = info.length;
        byte[] re = new byte[10 + length];
        re[0] = ControlCode.DLE.getHex();
        re[1] = ControlCode.STX.getHex();
        re[2] = seq;
        re[3] = addr[0];
        re[4] = addr[1];
        re[5] = len[0];
        re[6] = len[1];
        for (int i = 1; i <= length; i++) {
            re[6+i] = info[i-1];
        }
        re[7 + length] = dle;
        re[8 + length] = etx;
        re[9 + length] = cks;
        return re;
    }
}
