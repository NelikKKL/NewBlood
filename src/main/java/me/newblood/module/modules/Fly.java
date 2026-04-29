package me.newblood.module.modules;

import me.newblood.module.Module;

public class Fly extends Module {
    public Fly() {
        super("Fly", "Allows you to fly like in creative mode", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player != null) {
            mc.player.getAbilities().flying = true;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null && !mc.player.isCreative()) {
            mc.player.getAbilities().flying = false;
        }
    }
}
