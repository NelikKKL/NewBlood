package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.stream.StreamSupport;

public class ESP extends Module {
    public static boolean enabled = false;

    public ESP() {
        super("ESP", "Highlights players and shows their stats", Category.RENDER);
    }

    @Override
    public void onTick() {
        if (mc.world == null) return;
        enabled = isEnabled();

        StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> entity != mc.player)
                .forEach(entity -> {
                    entity.setGlowing(isEnabled());
                });
    }

    @Override
    public void onDisable() {
        enabled = false;
        if (mc.world == null) return;
        StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .forEach(entity -> entity.setGlowing(false));
    }
}
