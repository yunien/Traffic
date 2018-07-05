package com.wistron.occ.socket.netty.handler

import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.bytes.ByteArrayDecoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ByteChannelInitializer : ChannelInitializer<SocketChannel>()  {

    @Autowired
    private lateinit var byteServerHandler: ChannelInboundHandlerAdapter

    override fun initChannel(socketChannel: SocketChannel) {
        val pipeline = socketChannel.pipeline()

        pipeline.addLast(ByteArrayDecoder())

        pipeline.addLast(byteServerHandler)
    }
}
