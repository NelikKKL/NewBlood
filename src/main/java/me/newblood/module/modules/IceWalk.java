package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.util.math.Vec3d;

public class IceWalk extends Module {
    public static boolean enabledStatic = false;

    public IceWalk() {
        super("IceWalk", "Walk like on ice and lose less hunger", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        enabledStatic = isEnabled();
        if (!isEnabled() || mc.player == null) return;

        if (mc.player.isOnGround() && (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0)) {
            Vec3d vel = mc.player.getVelocity();
            // Simulate ice slipperiness by maintaining momentum
            mc.player.setVelocity(vel.x * 1.05, vel.y, vel.z * 1.05);
        }
    }

    @Override
    public void onEnable() {
        enabledStatic = true;
    }

    @Override
    public void onDisable() {
        enabledStatic = false;
    }
}
