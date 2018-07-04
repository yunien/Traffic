package com.wistron.occ.serial;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class MainSerialProcess {

	public static MainSerialProcess serialProcess = new MainSerialProcess();

	public MainSerialProcess() {
		super();
	}

//	public static void main(String[] args) {
//		try {
//			serialProcess.connect("/dev/tty.usbserial", 9600);
//		} catch (Exception e) {
//			System.out.println("main error:" + e);
//			e.printStackTrace();
//		}
//	}

	public void connect(String portName, Integer baudrate) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {

			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();


//				(new Thread(new SerialReader(in, serialPort))).start();

//				(new Thread(new SerialWriter(out))).start();

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

}