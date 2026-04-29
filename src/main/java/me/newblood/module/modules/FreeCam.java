package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends Module {
    private OtherClientPlayerEntity dummy;
    public Vec3d oldPos;
    public float oldYaw, oldPitch;
    private boolean oldFlying;
    public static boolean enabled = false;

    public FreeCam() {
        super("FreeCam", "Free camera movement while leaving your body behind", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) return;
        enabled = true;

        oldPos = mc.player.getPos();
        oldYaw = mc.player.getYaw();
        oldPitch = mc.player.getPitch();
        oldFlying = mc.player.getAbilities().flying;

        dummy = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());
        dummy.copyFrom(mc.player);
        dummy.copyPositionAndRotation(mc.player);
        dummy.headYaw = mc.player.headYaw;
        dummy.bodyYaw = mc.player.bodyYaw;
        dummy.setNoGravity(true);
        mc.world.addEntity(dummy.getId(), dummy);
    }

    @Override
    public void onTick() {
        if (mc.player != null) {
            mc.player.getAbilities().flying = true;
            mc.player.noClip = true;
            mc.player.setOnGround(false);
            
            // Adjust fly speed for smooth but fast movement
            // 0.15f is ~3x normal fly speed, which is fast but controllable
            mc.player.getAbilities().setFlySpeed(0.15f);
        }
    }

    @Override
    public void onDisable() {
        enabled = false;
        if (mc.player == null || mc.world == null) return;

        mc.player.noClip = false;
        mc.player.getAbilities().flying = oldFlying;
        mc.player.getAbilities().setFlySpeed(0.05f);
        mc.player.refreshPositionAndAngles(oldPos.x, oldPos.y, oldPos.z, oldYaw, oldPitch);
        mc.player.setVelocity(Vec3d.ZERO);
        mc.player.setOnGround(true);

        if (dummy != null) {
            mc.world.removeEntity(dummy.getId(), Entity.RemovalReason.DISCARDED);
            dummy = null;
        }
    }
}
