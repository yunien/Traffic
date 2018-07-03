
package com.zbum.example.socket.server.netty;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ChannelRepository  {

    private var channelCache: ConcurrentMap<String, Channel>  =  ConcurrentHashMap<String, Channel>();

    fun get(key:String): Channel? = channelCache.get(key)


    fun put(key:String,  value:Channel):ChannelRepository {
        channelCache.put(key, value);
        return this;
    }

    fun remove(key: String) { channelCache.remove(key) }

    fun size(): Int = channelCache.size;
}
