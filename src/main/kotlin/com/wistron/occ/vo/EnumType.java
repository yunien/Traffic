package com.wistron.occ.vo;
import org.apache.commons.lang3.StringUtils;

public class EnumType{
	
	public enum ControllerCode {
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
			ControllerCode returnControllerCode = null;
			final ControllerCode[] codes = ControllerCode.values();
            for (ControllerCode code : codes) {
                if (code.getHex() == hex) {
                	returnControllerCode = code;
                    break;
                }
            }
            return returnControllerCode;
		}
		
	}
	
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
			ParamCode returnParamCode = null;
			final ParamCode[] params = ParamCode.values();
            for (ParamCode value : params) {
                if (value.getParam() == param) {
                	returnParamCode = value;
                    break;
                }
            }
            return returnParamCode;
		}
	}
	
}

