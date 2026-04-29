package me.newblood.module.modules;

import me.newblood.module.Module;

public class AntiAim extends Module {
    public static boolean enabled = false;
    private float yaw = 0;

    public AntiAim() {
        super("AntiAim", "Rotates your character for others while keeping your camera still", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        yaw += 45; // Fast rotation
        if (yaw >= 360) yaw -= 360;

        // Visual rotation for the player (F5 mode)
        mc.player.bodyYaw = yaw;
        mc.player.headYaw = yaw;
        mc.player.prevBodyYaw = yaw;
        mc.player.prevHeadYaw = yaw;

        // Force a packet to be sent even if the player isn't moving
        mc.player.networkHandler.sendPacket(new net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround(yaw, mc.player.getPitch(), mc.player.isOnGround()));
    }

    public float getYaw() {
        return yaw;
    }

    @Override
    public void onEnable() {
        enabled = true;
        if (mc.player != null) yaw = mc.player.getYaw();
    }

    @Override
    public void onDisable() {
        enabled = false;
    }
}
