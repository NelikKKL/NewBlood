package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", "Автоматически нажимает кнопку возрождения", Category.MISC);
    }

    @Override
    public void onTick() {
        if (mc.currentScreen instanceof DeathScreen) {
            mc.player.requestRespawn();
            mc.setScreen(null);
        }
    }
}
