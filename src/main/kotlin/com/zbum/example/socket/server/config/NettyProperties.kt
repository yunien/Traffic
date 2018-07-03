package com.zbum.example.socket.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "netty")
class NettyProperties {
    lateinit var tcpPort: String
    lateinit var bossCount: String
    lateinit var workerCount: String
    lateinit  var backlog: String
}