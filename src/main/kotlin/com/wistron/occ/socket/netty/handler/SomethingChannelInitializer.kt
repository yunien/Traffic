package com.wistron.occ.socket.netty.handler

import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("somethingChannelInitializer")
class SomethingChannelInitializer : ChannelInitializer<SocketChannel>() {

    @Autowired
    @Qualifier("somethingServerHandler")
    private lateinit var somethingServerHandler: ChannelInboundHandlerAdapter

    override fun initChannel(socketChannel: SocketChannel) {
        val pipeline = socketChannel.pipeline()

        // Add the text line codec combination first,
        pipeline.addLast(DelimiterBasedFrameDecoder(1024 * 1024, *Delimiters.lineDelimiter()))
        // the encoder and decoder are static as these are sharable
        pipeline.addLast(DECODER)
        pipeline.addLast(ENCODER)

        pipeline.addLast(somethingServerHandler)
    }

    companion object {

        private val DECODER = StringDecoder()
        private val ENCODER = StringEncoder()
    }
}