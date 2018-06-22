package com.wistron.occ.utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import com.wistron.occ.vo.EnumType;

import gnu.io.SerialPort;

public class SerialReader implements Runnable {
	InputStream in;
	SerialPort serialPort;

	public SerialReader(InputStream in, SerialPort serialPort) {
		this.in = in;
		this.serialPort = serialPort;
	}

	public void run() {
		byte[] buffer = new byte[1];
		int len = -1;
		
		try {
			
			List<String> perMsgList = new ArrayList<String>();
			int msgListIndex = 0;
			EnumType.Type msgTitle = EnumType.Type.NULL;
			int totalLength = 0;
			List<String> infoList = new ArrayList<String>();
			Map<EnumType.InfoCode, List<String>> infoMap = new HashMap<EnumType.InfoCode, List<String>>();
			int i=0;
			while ((len = this.in.read(buffer)) > -1) {
//				System.out.println( i+"：InputStream:"+Hex.encodeHexString(buffer) );
				i++;
				
//				composePerMsgList(perMsgList, msgListIndex, msgTitle, totalLength, Hex.encodeHexString(buffer));
				
				perMsgList.add(Hex.encodeHexString(buffer));
				
				System.out.println(perMsgList.size() + ". perMsgList:" + perMsgList.get(msgListIndex));
				
				//	第二個byte可決定傳過來的訊息類型 msgTitle
				if(perMsgList.size() > 1 && msgTitle == EnumType.Type.NULL){
//					System.out.println("改變msgTitle "+ perMsgList.get(msgListIndex-1) + " " + perMsgList.get(msgListIndex) );
					if(StringUtils.equals(perMsgList.get(msgListIndex-1), "aa") && StringUtils.equals(perMsgList.get(msgListIndex), "dd")){
						System.out.println(EnumType.Type.ACK);
						msgTitle = EnumType.Type.ACK;
					} else if( StringUtils.equals(perMsgList.get(msgListIndex-1), "aa") && StringUtils.equals(perMsgList.get(msgListIndex), "ee") ){
						System.out.println(EnumType.Type.NACK);
						msgTitle = EnumType.Type.NACK;
					} else if( StringUtils.equals(perMsgList.get(msgListIndex-1), "aa") && StringUtils.equals(perMsgList.get(msgListIndex), "bb") ){
						System.out.println(EnumType.Type.MSG);
						msgTitle = EnumType.Type.MSG;
					} else {
						perMsgList.clear();
						msgListIndex = 0;
						msgTitle = EnumType.Type.NULL;
						totalLength = 0;
						infoMap.clear();
						infoList.clear();
					}
				}
				
				//	第六、七個byte會知道總長度
				if(perMsgList.size() == 7){
					String length = perMsgList.get(5) + perMsgList.get(6);
					totalLength = Integer.parseInt(length, 16);
				}
				System.out.println(msgTitle+"=> perMsgList.size():"+perMsgList.size()+". totalLength:"+totalLength);
				
				//	擷取完整byte字串後，解析字串
				if(perMsgList.size() == totalLength) {
					switch (msgTitle) {
					case MSG:	//	取INFO 訊息欄位，從第8位 取到 倒數第4位
						System.out.println("MSG");
						composeInfoMap(perMsgList, totalLength, infoList, infoMap);
						converterInfoMsg(infoMap);
						break;
					case ACK:	//	寫入訊息成功
						System.out.println("ACK");
						break;
					case NACK:	//	寫入訊息失敗
						System.out.println("NACK");
						break;
					default:
						break;
					}
					
					System.out.println("list2 clear");
					// save db
					perMsgList.clear();
					msgListIndex = 0;
					msgTitle = EnumType.Type.NULL;
					totalLength = 0;
					infoMap.clear();
					infoList.clear();
				}else{
					msgListIndex++;
				}
				
			}
			this.in.close();
			if (serialPort != null) {
				serialPort.close();
			}
			this.serialPort = null;
		} catch (IOException e) {
			if (serialPort != null) {
				serialPort.close();
			}
			this.serialPort = null;
			e.printStackTrace();
		} finally {
			System.out.println("SerialReader Finally");
			if (serialPort != null) {
				serialPort.close();
			}
			this.serialPort = null;
		}
	}
	
	private void composeInfoMap(List<String> perMsgList, int totalLength, 
			List<String> infoList, Map<EnumType.InfoCode, List<String>> infoMap){
		StringBuffer buf = new StringBuffer();
		for(int j=8 ; j<=totalLength-3 ; j++){
			System.out.print(perMsgList.get(j-1) + " ");
			if(j==8 || j==9){
				buf.append(perMsgList.get(j-1));
			}else{
				infoList.add(perMsgList.get(j-1));
			}
		}
		infoMap.put(EnumType.InfoCode.lookup(buf.toString()), infoList);
	}
	
	private void converterInfoMsg(final Map<EnumType.InfoCode, List<String>> infoMap){
		// 只會有一筆
		for (EnumType.InfoCode infoCode : infoMap.keySet()) {
            System.out.println(infoCode + ":" + infoMap.get(infoCode));
            switch (infoCode) {
    		case CODE_5F0F:
    			converter5F0F(infoMap.get(infoCode));
    			break;

    		default:
    			System.out.println("default");
    			break;
    		}
            
        }
	}
	
	
	/**
	 * 1. 用於控制中心監視號誌控制器之簡單燈態運作狀況用。
	 * 2. 主要給無線通訊環境使用，以顯示紅黃綠燈。
	 *
	 * @param SignalMap
	 * @param GreenSignalMap
	 * @param YellowSignalMap
	 * @param RedSignalMap
	 * Bit 設為 1 表示該方向有燈，Bit 設為 0 表示該方向沒有燈
	 * Bit 0:北向 Bit 1: 東北向 Bit 2: 東向 Bit 3: 東南向
	 * Bit 4:南向 Bit 5: 西南向 Bit 6: 西向 Bit 7: 西北向
	 */
	private void converter5F0F(List<String> paramList) {
		for(String param : paramList){
			byte byteParam = (byte) Integer.parseInt(param.substring(0, 2), 16);
			for(int i=0 ; i<8 ; i++){
				int aa = getBit(byteParam, i);
				System.out.print(aa + ". ");
			}
			System.out.println("");
		}
	}
	
	public static byte[] hex2Byte(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i=0 ; i<bytes.length ; i++){
			bytes[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}
	
	private int getBit(byte id, int position){
		return (id >> position) & 1;
	}
	
	private void composePerMsgList(List<String> perMsgList, int msgListIndex, 
			EnumType.Type msgTitle, int totalLength, final String hexString){
		
		perMsgList.add(hexString);
		
		System.out.println(perMsgList.size() + ". perMsgList:" + perMsgList.get(msgListIndex));
		
		//第二筆會進去改變msgTitle
		if(perMsgList.size() > 1 && msgTitle == EnumType.Type.NULL){
			findMsgTitle(perMsgList, msgListIndex, msgTitle);
		}
		System.out.println("msgTitle:" + msgTitle);
		
		
		if(perMsgList.size() == 7){
			String length = perMsgList.get(5) + perMsgList.get(6);
			totalLength = Integer.parseInt(length, 16);
		}
		
		if(perMsgList.size() == totalLength) {
			// 取INFO 訊息欄位，從第8位 取到 倒數第4位
			if(msgTitle == EnumType.Type.MSG){
				for(int j=8 ; j<=totalLength-3 ; j++){
					System.out.print(perMsgList.get(j-1) + " ");
				}
			}
			
			
			
			System.out.println("list2 clear");
			// save db
			perMsgList.clear();
			msgListIndex = 0;
			msgTitle = EnumType.Type.NULL;
			totalLength = 0;
		}else{
			msgListIndex++;
		}
		
		
	}
	
	private void findMsgTitle(List<String> perMsgList, int msgListIndex, EnumType.Type msgTitle){
		if( perMsgList.get(msgListIndex-1) == "aa" && perMsgList.get(msgListIndex) == "dd" ){
			System.out.println(EnumType.Type.ACK);
			msgTitle = EnumType.Type.ACK;
		} else if( perMsgList.get(msgListIndex-1) == "aa" && perMsgList.get(msgListIndex) == "ee" ){
			System.out.println(EnumType.Type.NACK);
			msgTitle = EnumType.Type.NACK;
		} else if( perMsgList.get(msgListIndex-1) == "aa" && perMsgList.get(msgListIndex) == "bb" ){
			System.out.println(EnumType.Type.MSG);
			msgTitle = EnumType.Type.MSG;
		} else {
			// do nothing.
		}
	}
	
	
}
