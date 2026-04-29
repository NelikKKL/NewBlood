package me.newblood.module.modules;

import me.newblood.module.Module;

public class Spider extends Module {
    public Spider() {
        super("Spider", "Allows you to climb walls like a spider", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player.horizontalCollision) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0.2, mc.player.getVelocity().z);
        }
    }
}
