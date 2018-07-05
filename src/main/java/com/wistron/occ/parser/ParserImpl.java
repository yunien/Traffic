package com.wistron.occ.parser;

import com.wistron.occ.protocol.packet.PacketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParserImpl implements Parser {

    @Autowired
    private PacketFactory packetFactory;

    @Override
    public void process(byte b) {
        packetFactory.sink(b);
    }

}
