# ChatBridge
适用于Minecraft Bukkit/Fabric端的一个兼容SLS-Bot的跨服聊天Mod，通过Websocket与SLS-Bot主插件通讯，以Apache 2.0协议开源

# 配置文件
不论是Bukkit还是Fabric端，配置文件格式均为JSON。由于原生JSON不支持注释，所以当您复制下方内容作为您的配置文件时，可能需要手动去掉注释内容。
```json5
{
  "host": "127.0.0.1",  // SLS-Bot监听的地址，可在SLS-Bot的配置文件中设置
  "port": 16123,  // SLS-Bot监听的端口，可在SLS-Bot的配置文件中设置
  "serverName": "unknown-server",  // 服务器名，会显示给其他服务器
  "retryInterval": 3000,  // 与服务器重连尝试的间隔时间，单位为毫秒（ms）
  messageConfig: {
    transferChat: true,  // 是否传递聊天信息
    transferMCDRCommand: false,  // 是否传递可能的MCDR命令（以!!开头的消息）
    transferPlayerChange: true,  // 是否传递玩家变动信息（玩家加入/退出）
    transferServerLifecycle: true  // 是否传递服务器生命变动事件（开/关服）
  }
}
```

# 给开发者的一些说明
事实上，你可以无需安装SLS-Bot而同样实现跨服聊天功能，只需要部署一个Websocket服务器，将收到的包直接转发给其他客户端即可。当然，若您有将客户端部署到其他地方的需求，您可以依照以下包规范实现API

所有跨服聊天的包均为一个纯JSON文本，其格式如下：

```json5
{
  "type": "chatBridge",
  "args": {
    "identity": "server-name",  // 服务器名称
    "sender": "player-name",  // 消息发送人名称
    "text": "message"  // 消息本体内容
  }
}
```
