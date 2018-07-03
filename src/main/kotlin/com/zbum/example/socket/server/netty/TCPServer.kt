/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zbum.example.socket.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;


@Component
public class TCPServer (
        val serverBootstrap: ServerBootstrap,
        val tcpPort: InetSocketAddress
) {
    var serverChannel: Channel? = null

    fun start() {
        serverChannel =  serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync().channel();

    }

    @PreDestroy
    fun stop() {
        if ( serverChannel != null ) {
            serverChannel!!.close();
            serverChannel!!.parent().close();
        }
    }
}

