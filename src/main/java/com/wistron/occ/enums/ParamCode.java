package com.wistron.occ.enums;

public enum ParamCode {
    SIGNAL_MAP("SignalMap", "表示燈之方向"),
    GREEN_SIGNAL_MAP("GreenSignalMap", "綠燈之方向"),
    YELLOW_SIGNAL_MAP("YellowSignalMap", "黃燈之方向"),
    RED_SIGNAL_MAP("RedSignalMap", "紅燈之方向");

    private String param;
    private String des;

    ParamCode (final String param, final String des){
        this.param = param;
        this.des = des;
    }

    public String getParam() {
        return param;
    }


    public void setParam(String param) {
        this.param = param;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static ParamCode lookup(final String param) {
        for (ParamCode c : values()) {
            if (c.getParam().equals(param)) {
                return c;
            }
        }
        return null;
    }
}
