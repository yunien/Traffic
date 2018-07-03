package com.wistron.occ.vo;

import org.apache.commons.lang3.StringUtils;

public enum InfoCode {
    CODE_NULL("null", 0, "NULL"),
    CODE_5F0F("5f0f", 4, "燈態步階傳輸週期管理"),
    CODE_0f81("0f81", 4, "燈態步階傳輸週期管理");

    private String infoCode;
    private int params;
    private String des;

    InfoCode (final String infoCode, final int params, final String des){
        this.infoCode = infoCode;
        this.params = params;
        this.des = des;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }

    public int getParams() {
        return params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static InfoCode lookup(final String infoCode) {
        InfoCode returnInfoCode = null;
        final InfoCode[] infoCodes = InfoCode.values();
        for (InfoCode code : infoCodes) {
            if (StringUtils.equals(code.getInfoCode(), infoCode)) {
                returnInfoCode = code;
                break;
            } else {
                returnInfoCode = InfoCode.CODE_NULL;
            }
        }
        return returnInfoCode;
    }
}
