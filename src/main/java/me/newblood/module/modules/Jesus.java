package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", "Allows you to walk on water", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        // Check if player is actually touching water or is just above it
        boolean touchingWater = mc.player.isTouchingWater();
        // Use exact Y position to check for water below feet
        BlockPos belowPos = new BlockPos((int)Math.floor(mc.player.getX()), (int)Math.floor(mc.player.getY() - 0.01), (int)Math.floor(mc.player.getZ()));
        boolean isAboveWater = mc.world.getBlockState(belowPos).getBlock() == Blocks.WATER;

        if ((touchingWater || isAboveWater) && !mc.player.isSneaking() && !mc.player.isFallFlying()) {
            // Get the water level Y coordinate
            double waterLevel = Math.floor(mc.player.getY());
            if (isAboveWater && !touchingWater) {
                waterLevel = Math.floor(mc.player.getY() - 0.01);
            }
            
            // If we are below or at the water surface, keep us there
            if (mc.player.getY() < waterLevel + 1.0) {
                Vec3d vel = mc.player.getVelocity();
                
                // Set Y to just above water surface without flying up
                // If moving, we stay at surface. If not moving, we stay at surface.
                double targetY = waterLevel + 0.0;
                
                if (mc.player.getY() < targetY) {
                    mc.player.setPosition(mc.player.getX(), targetY, mc.player.getZ());
                }

                mc.player.setVelocity(vel.x, 0.0, vel.z);
                mc.player.setOnGround(true);

                if (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0) {
                    double speed = 0.28;
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

                    mc.player.setVelocity(forward * speed * mx + side * speed * mz, 
                                        0.0, 
                                        forward * speed * mz - side * speed * mx);
                }
            }
        }
    }
}