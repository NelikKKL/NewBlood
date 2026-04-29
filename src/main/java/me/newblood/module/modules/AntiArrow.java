package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;

public class AntiArrow extends Module {
    public AntiArrow() {
        super("AntiArrow", "Automatically dodges incoming projectiles at any distance", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ProjectileEntity projectile) {
                // Ignore projectiles not moving
                if (projectile.getVelocity().length() < 0.1) continue;
                
                // Don't dodge our own projectiles
                if (projectile.getOwner() == mc.player) continue;

                // Check distance - work up to 40 blocks away
                double distance = projectile.distanceTo(mc.player);
                if (distance > 40) continue;

                Vec3d projPos = projectile.getPos();
                Vec3d projVel = projectile.getVelocity().normalize();
                
                // Check multiple points on the player (head, body, feet)
                Vec3d[] playerPoints = {
                    mc.player.getPos().add(0, 0.5, 0),
                    mc.player.getPos().add(0, 1.2, 0),
                    mc.player.getPos().add(0, 1.8, 0)
                };

                for (Vec3d playerPos : playerPoints) {
                    Vec3d toPlayer = playerPos.subtract(projPos);
                    double dot = toPlayer.dotProduct(projVel);

                    if (dot > 0) { // Projectile is moving towards player
                        Vec3d projection = projVel.multiply(dot);
                        Vec3d closestPoint = projPos.add(projection);
                        double distanceToPath = closestPoint.distanceTo(playerPos);
                        
                        // Dodge if it's going to pass within 2 blocks
                        if (distanceToPath < 2.0) {
                            // Calculate dodge direction (perpendicular to projectile)
                            Vec3d dodgeDir = new Vec3d(-projVel.z, 0, projVel.x).normalize();
                            
                            // Always dodge away from the closest point on the path
                            Vec3d side1 = mc.player.getPos().add(dodgeDir);
                            Vec3d side2 = mc.player.getPos().subtract(dodgeDir);
                            
                            if (side1.distanceTo(closestPoint) < side2.distanceTo(closestPoint)) {
                                dodgeDir = dodgeDir.multiply(-1);
                            }

                            // Dynamic strength: more power if the projectile is close
                            double strength = 0.4;
                            if (distance < 5) strength = 0.6; // Stronger dodge for close shots
                            
                            // Apply velocity directly for instant reaction
                            mc.player.addVelocity(dodgeDir.x * strength, 0, dodgeDir.z * strength);
                            break; // Dodge once per tick for one projectile
                        }
                    }
                }
            }
        }
    }
}
