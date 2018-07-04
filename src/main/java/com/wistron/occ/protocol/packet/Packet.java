package com.wistron.occ.protocol.packet;

public abstract class Packet {

    private byte seq;
    private byte[] addr;
    private byte[] len;

    private byte cks;

    public Packet(byte seq, byte[] addr, byte[] len) {
        this.seq = seq;
        this.addr = addr;
        this.len = len;
    }

    protected boolean validateCks(byte cks) {
        this.cks = cks;

        // validate logic
        return true;
    }
}
