package cn.disy920.chatbridge.mixin;

import cn.disy920.chatbridge.listener.Listeners;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onChatMessage")
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        String chatMessage = packet.chatMessage();
        if (chatMessage.startsWith("/")) return;

        Listeners.Companion.onChat(this.player.getName().getString(), chatMessage);
    }
}
