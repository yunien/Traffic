package com.wistron.occ

import com.wistron.occ.socket.config.NettyProperties
import com.wistron.occ.socket.netty.ChannelRepository
import com.wistron.occ.socket.netty.TCPServer
import com.wistron.occ.socket.netty.handler.ByteChannelInitializer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import java.net.InetSocketAddress
import java.util.HashMap

@SpringBootApplication
@EnableConfigurationProperties(NettyProperties::class)
class Application {

    @Autowired
    lateinit var nettyProperties: NettyProperties

    @Autowired
    lateinit var byteChannelInitializer: ByteChannelInitializer

    @Bean(name = arrayOf("serverBootstrap"))
    fun bootstrap2(): ServerBootstrap {
        val b = ServerBootstrap()
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.DEBUG))
                .childHandler(byteChannelInitializer)

        val tcpChannelOptions: Map<ChannelOption<Int>, Int> = tcpChannelOptions()
        for ((option, value) in tcpChannelOptions) {
            b.option<Int>(option, value)
        }
        return b
    }

    @Bean
    fun tcpChannelOptions(): Map<ChannelOption<Int>, Int> {
        val options = HashMap<ChannelOption<Int>, Int>()
        options[ChannelOption.SO_BACKLOG] = nettyProperties.backlog.toInt()
        return options
    }

    @Bean(destroyMethod = "shutdownGracefully")
    fun bossGroup(): NioEventLoopGroup {
        return NioEventLoopGroup(nettyProperties.bossCount.toInt())
    }

    @Bean(destroyMethod = "shutdownGracefully")
    fun workerGroup(): NioEventLoopGroup {
        return NioEventLoopGroup(nettyProperties.workerCount.toInt())
    }

    @Bean
    fun tcpSocketAddress(): InetSocketAddress {
        return InetSocketAddress(nettyProperties.tcpPort.toInt())
    }

    @Bean
    fun channelRepository(): ChannelRepository {
        return ChannelRepository()
    }


}

fun main(args: Array<String>) {
    val context = SpringApplication.run(Application::class.java, *args)
    val tcpServer = context.getBean(TCPServer::class.java)
    tcpServer.start()
}
