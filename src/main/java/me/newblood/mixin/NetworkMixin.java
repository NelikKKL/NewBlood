package me.newblood.mixin;

import me.newblood.NewBloodClient;
import me.newblood.module.modules.AntiAim;
import me.newblood.module.modules.FreeCam;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class NetworkMixin {
    private static boolean isSelfSending = false;

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo info) {
        if (FreeCam.enabled) {
            if (packet instanceof PlayerMoveC2SPacket || 
                packet instanceof PlayerInputC2SPacket || 
                packet instanceof ClientCommandC2SPacket) {
                info.cancel();
                return;
            }
        }

        if (AntiAim.enabled && packet instanceof PlayerMoveC2SPacket && !isSelfSending) {
            PlayerMoveC2SPacket p = (PlayerMoveC2SPacket) packet;
            AntiAim antiAim = (AntiAim) NewBloodClient.INSTANCE.getModuleManager().getModuleByName("AntiAim");
            net.minecraft.client.MinecraftClient mc = net.minecraft.client.MinecraftClient.getInstance();
            
            if (antiAim != null && mc.player != null && mc.getNetworkHandler() != null) {
                info.cancel();
                isSelfSending = true;
                
                float yaw = antiAim.getYaw();
                float pitch = p.getPitch(mc.player.getPitch());
                boolean onGround = p.isOnGround();
                
                // Construct a new packet with our modified yaw
                if (p instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(
                        p.getX(mc.player.getX()), 
                        p.getY(mc.player.getY()), 
                        p.getZ(mc.player.getZ()), 
                        yaw, pitch, onGround));
                } else if (p instanceof PlayerMoveC2SPacket.Full) {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(
                        p.getX(mc.player.getX()), 
                        p.getY(mc.player.getY()), 
                        p.getZ(mc.player.getZ()), 
                        yaw, pitch, onGround));
                } else {
                    // For LookAndOnGround or others, just send LookAndOnGround with our yaw
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, onGround));
                }
                
                isSelfSending = false;
            }
        }
    }
}
