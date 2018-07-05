package com.wistron.occ.protocol.packet;

import com.wistron.occ.enums.ControlCode;

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

    @Override
    public byte[] getNative() {
        byte[] re = new byte[9];
        re[0] = ControlCode.DLE.getHex();
        re[1] = ControlCode.NAK.getHex();
        re[2] = seq;
        re[3] = addr[0];
        re[4] = addr[1];
        re[5] = len[0];
        re[6] = len[1];
        re[7] = err;
        re[8] = cks;
        return re;
    }
}
