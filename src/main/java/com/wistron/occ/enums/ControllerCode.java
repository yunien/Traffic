package com.wistron.occ.enums;

public enum  ControllerCode {
    DLE((byte) 0xaa, "Data Link Escape  以控制資料傳輸"),
    STX((byte) 0xbb, "Start of Text 訊息碼框之開始"),
    ETX((byte) 0xcc, "End of Text 訊息碼框之結束"),
    ACK((byte) 0xdd, "Positive Acknowledge 正認知碼框， 表示碼框及CKS正確"),
    NAK((byte) 0xee, "Negative Acknowledge 負認知碼框， 表示碼框及CKS錯誤"),
    ADDR1((byte) 0xff, "設備編號1"),
    ADDR2((byte) 0xff, "設備編號2");

    private byte hex;
    private String des;

    ControllerCode (final byte hex, final String des){
        this.hex = hex;
        this.des = des;
    }

    public byte getHex() {
        return hex;
    }

    public void setHex(byte hex) {
        this.hex = hex;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static ControllerCode lookup(final byte hex) {
        for (ControllerCode code : values()) {
            if (code.getHex() == hex) {
                return code;
            }
        }
        return null;
    }

}
