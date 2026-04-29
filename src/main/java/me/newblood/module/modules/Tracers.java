package me.newblood.module.modules;

import me.newblood.module.Module;
import me.newblood.module.settings.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

public class Tracers extends Module {
    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting animals = new BooleanSetting("Animals", false);
    public final BooleanSetting monsters = new BooleanSetting("Monsters", false);
    public final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);

    public Tracers() {
        super("Tracers", "Draws lines to nearby entities and boxes", Category.RENDER);
        addSetting(players);
        addSetting(animals);
        addSetting(monsters);
        addSetting(invisibles);
    }

    public boolean isValidTarget(Entity entity) {
        if (entity == mc.player) return false;
        if (!entity.isAlive()) return false;
        if (!invisibles.getValue() && entity.isInvisible()) return false;
        if (players.getValue() && entity instanceof PlayerEntity) return true;
        if (animals.getValue() && entity instanceof AnimalEntity) return true;
        if (monsters.getValue() && entity instanceof HostileEntity) return true;
        return false;
    }
}
