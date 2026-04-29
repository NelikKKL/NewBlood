package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BunnyHop extends Module {
    private double currentSpeed = 0.2873;
    private final double maxSpeed = 0.45; // Reduced max speed for better control

    public BunnyHop() {
        super("BunnyHop", "High-speed movement using momentum", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        boolean isMoving = mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0;

        if (isMoving) {
            if (mc.player.isOnGround()) {
                mc.player.jump();
                // Initial jump speed
                if (currentSpeed < 0.35) {
                    currentSpeed = 0.35;
                } else {
                    currentSpeed *= 1.015; // Very small boost per jump
                }
            } else {
                currentSpeed *= 0.996; // Slight air friction
                if (currentSpeed < 0.2873) currentSpeed = 0.2873;
            }

            if (currentSpeed > maxSpeed) {
                currentSpeed = maxSpeed;
            }

            // Calculate movement direction based on inputs
            float yaw = mc.player.getYaw();
            float forward = mc.player.forwardSpeed;
            float side = mc.player.sidewaysSpeed;

            if (forward != 0) {
                if (side > 0) yaw -= (forward > 0 ? 45 : -45);
                else if (side < 0) yaw += (forward > 0 ? 45 : -45);
                side = 0;
                if (forward > 0) forward = 1;
                else if (forward < 0) forward = -1;
            }

            double mx = Math.cos(Math.toRadians(yaw + 90.0F));
            double mz = Math.sin(Math.toRadians(yaw + 90.0F));

            mc.player.setVelocity(forward * currentSpeed * mx + side * currentSpeed * mz, 
                                mc.player.getVelocity().y, 
                                forward * currentSpeed * mz - side * currentSpeed * mx);
        } else {
            currentSpeed = 0.2873;
        }
    }
}