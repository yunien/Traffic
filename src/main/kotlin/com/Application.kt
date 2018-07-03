package com

import com.zbum.example.socket.server.config.NettyProperties
import com.zbum.example.socket.server.netty.ChannelRepository
import com.zbum.example.socket.server.netty.TCPServer
import com.zbum.example.socket.server.netty.handler.SomethingChannelInitializer
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
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.net.InetSocketAddress
import java.util.HashMap

@SpringBootApplication
@EnableConfigurationProperties(NettyProperties::class)
class Application {

    @Autowired
    lateinit var nettyProperties: NettyProperties

    @Autowired
    lateinit var somethingChannelInitializer: SomethingChannelInitializer

    @Bean(name = arrayOf("serverBootstrap"))
    fun bootstrap(): ServerBootstrap {
        val b = ServerBootstrap()
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.DEBUG))
                .childHandler(somethingChannelInitializer)

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
    //runApplication<Application>(*args)

    val context = SpringApplication.run(Application::class.java, *args)
    val tcpServer = context.getBean(TCPServer::class.java)
    tcpServer.start()
}
