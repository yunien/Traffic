package com.wistron.occ.socket.netty.handler

import com.wistron.occ.socket.netty.ChannelRepository
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
@ChannelHandler.Sharable
class SomethingServerHandler(val channelRepository: ChannelRepository) : ChannelInboundHandlerAdapter()
{

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null")

        ctx.fireChannelActive()
        if (log.isDebugEnabled()) {
            log.debug(ctx.channel().remoteAddress().toString() + "")
        }
        val channelKey = ctx.channel().remoteAddress().toString()
        channelRepository.put(channelKey, ctx.channel())

        ctx.writeAndFlush("Your channel key is $channelKey\r\n")

        if (log.isDebugEnabled()) {
            log.debug("Binded Channel Count is {}", this.channelRepository.size())
        }

    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val stringMessage = msg as String
        if (log.isDebugEnabled()) {
            log.debug(stringMessage)
        }

        val splitMessage = stringMessage.split("::".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (splitMessage.size != 2) {
            ctx.channel().writeAndFlush(stringMessage + "\n\r")
            return
        }

        if (channelRepository.get(splitMessage[0]) != null) {
            channelRepository.get(splitMessage[0])!!.writeAndFlush(splitMessage[1] + "\n\r")
        }
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        log.error(cause.message, cause)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null")
        Assert.notNull(ctx, "[Assertion failed] - ChannelHandlerContext is required; it must not be null")

        val channelKey = ctx.channel().remoteAddress().toString()
        this.channelRepository.remove(channelKey)
        if (log.isDebugEnabled()) {
            log.debug("Binded Channel Count is " + this.channelRepository.size())
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(SomethingServerHandler::class.java)
    }
}