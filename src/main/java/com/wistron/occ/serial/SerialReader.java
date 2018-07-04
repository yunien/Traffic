package com.wistron.occ.serial;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wistron.occ.enums.InfoCode;
import com.wistron.occ.enums.Type;
import com.wistron.occ.parser.Parser;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import gnu.io.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SerialReader implements Runnable {

    @Autowired
    private Parser parser;

    private InputStream in;
    private SerialPort serialPort;

//    public SerialReader(InputStream in, SerialPort serialPort) {
//        this.in = in;
//        this.serialPort = serialPort;
//    }

    public void run() {

        try {

            parser.process(in);


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
}
