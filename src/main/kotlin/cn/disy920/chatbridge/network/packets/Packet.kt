package cn.disy920.chatbridge.network.packets

import com.google.gson.Gson
import com.google.gson.GsonBuilder

interface Packet {
    companion object {
        val GSON: Gson = GsonBuilder().setLenient().create()
    }
}