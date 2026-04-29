package me.newblood.module.modules;

import me.newblood.module.Module;
import me.newblood.module.settings.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Killaura extends Module {
    private final double range = 4.0;
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting animals = new BooleanSetting("Animals", false);
    private final BooleanSetting monsters = new BooleanSetting("Monsters", true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);

    public Killaura() {
        super("Killaura", "Automatically attacks nearby entities", Category.COMBAT);
        addSetting(players);
        addSetting(animals);
        addSetting(monsters);
        addSetting(invisibles);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        Entity target = null;
        double closestDist = range;

        // More efficient entity filtering
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity) || entity == mc.player || !entity.isAlive()) continue;
            if (((LivingEntity) entity).getHealth() <= 0) continue;
            if (!isValidTarget(entity)) continue;

            double dist = mc.player.distanceTo(entity);
            if (dist <= closestDist) {
                closestDist = dist;
                target = entity;
            }
        }

        if (target != null) {
            // Smooth rotations or instant
            float[] rotations = getRotations(target);
            mc.player.setYaw(rotations[0]);
            mc.player.setPitch(rotations[1]);

            // Ensure we are facing the target and cooldown is ready
            if (mc.player.getAttackCooldownProgress(0) >= 1.0f) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private float[] getRotations(Entity entity) {
        double diffX = entity.getX() - mc.player.getX();
        double diffZ = entity.getZ() - mc.player.getZ();
        double diffY = (entity instanceof LivingEntity ? entity.getY() + entity.getEyeHeight(entity.getPose()) - 0.4 : entity.getY()) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));

        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[]{yaw, pitch};
    }

    private boolean isValidTarget(Entity entity) {
        if (!invisibles.getValue() && entity.isInvisible()) return false;
        if (players.getValue() && entity instanceof PlayerEntity) return true;
        if (animals.getValue() && entity instanceof AnimalEntity) return true;
        if (monsters.getValue() && entity instanceof HostileEntity) return true;
        return false;
    }
}
