package com.wistron.occ.protocol.packet;

import com.wistron.occ.protocol.state.EmptyPacketState;
import com.wistron.occ.protocol.state.PacketState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PacketFactory {

    private List<Packet> packets = new ArrayList<>();

    private PacketState packetState = new EmptyPacketState();

    public void setPacketState(PacketState packetState) {
        this.packetState = packetState;
    }

    public void addPacket(Packet packet) {
        packets.add(packet);
    }

    public void sink(byte b) {
        packetState.feed(this, b);
    }

}
