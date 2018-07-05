package com.wistron.occ.protocol.packet;

import com.wistron.occ.enums.ControlCode;

public class AckPacket extends Packet {

    public AckPacket(byte seq, byte[] addr, byte[] len, byte cks) {
        super(seq, addr, len);
        validateCks(cks);
    }

    public AckPacket(byte[] codes) {
        super(codes[0], new byte[] {codes[1], codes[2]}, new byte[] {codes[3], codes[4]});
        validateCks(codes[5]);
    }

    @Override
    public byte[] getNative() {
        byte[] re = new byte[8];
        re[0] = ControlCode.DLE.getHex();
        re[1] = ControlCode.ACK.getHex();
        re[2] = seq;
        re[3] = addr[0];
        re[4] = addr[1];
        re[5] = len[0];
        re[6] = len[1];
        re[7] = cks;
        return re;
    }
}
