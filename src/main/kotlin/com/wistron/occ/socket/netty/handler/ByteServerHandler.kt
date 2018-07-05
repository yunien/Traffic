package com.wistron.occ.socket.netty.handler

import com.wistron.occ.parser.Parser
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@ChannelHandler.Sharable
class ByteServerHandler(): ChannelInboundHandlerAdapter() {

    @Autowired
    private lateinit var parser: Parser

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val bytes = msg as ByteArray
        bytes.forEach {
            parser.process(it)
        }

    }
}