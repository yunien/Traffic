package com.wistron.occ.protocol.packet;

import com.wistron.occ.protocol.state.EmptyPacketState;
import com.wistron.occ.protocol.state.PacketState;
import org.springframework.stereotype.Component;

@Component
public class PacketFactory {

    private PacketState packetState = new EmptyPacketState();

    public void setPacketState(PacketState packetState) {
        this.packetState = packetState;
    }

    public void sink(byte b) {
        packetState.feed(this, b);
    }
}
