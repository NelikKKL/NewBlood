package me.newblood.module.modules;

import me.newblood.module.Module;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "Reduces knockback", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player.hurtTime > 0) {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
        }
    }
}
