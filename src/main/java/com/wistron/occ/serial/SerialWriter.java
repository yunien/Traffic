package com.wistron.occ.serial;
import java.io.IOException;
import java.io.OutputStream;

import com.wistron.occ.enums.ControlCode;

public class SerialWriter implements Runnable {
	OutputStream out;

	public SerialWriter(OutputStream out) {
		this.out = out;
	}

	public void run() {
		try {

			byte[] RESULT = this.makeData();
			System.out.println(":this.out.write："+RESULT);
			this.out.write(RESULT);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("SerialWriter Finally");
		}
	}

	public byte[] makeData() {

		byte[] MSG = new byte[] { ControlCode.DLE.getHex(), ControlCode.STX.getHex(),
				(byte) 0x33 , ControlCode.ADDR1.getHex() , ControlCode.ADDR2.getHex(),
				(byte) 0x00, (byte) 0x0c, (byte) 0x0F, (byte) 0x40,
				ControlCode.DLE.getHex(), ControlCode.ETX.getHex() };

		byte CKS = converterCKS(MSG);
		System.out.println( "CKS:"+Integer.toHexString(CKS & (byte)0xff) );

		byte[] RESULT = new byte[MSG.length+1];
		for(int i=0 ; i < MSG.length+1 ; i++) {
			if(i<MSG.length) {
				RESULT[i] = MSG[i];
			} else {
				RESULT[i] = CKS;
			}
			System.out.println( i+":"+Integer.toHexString(RESULT[i] & (byte)0xff) );
		}

		return RESULT;
	}

	public static byte converterCKS(byte[] bytes){
		byte xor = 0;
		for(int i=0 ; i<bytes.length ; i++){
			if (i == 0) {
				xor = bytes[i];
			} else {
				xor = (byte)(0xff & ( (int)xor ) ^ ( (int)bytes[i] ));
			}
		}
		return xor;
	}


}
