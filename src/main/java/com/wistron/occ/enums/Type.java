package com.wistron.occ.enums;

public enum Type {
    NULL(""),
    ACK("正認知碼框"),
    NACK("負認知碼框"),
    MSG("訊息碼框");

    private String type;

    Type (final String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
