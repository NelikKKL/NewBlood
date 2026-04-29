package me.newblood.module.modules;

import me.newblood.module.Module;

public class Optimizer extends Module {
    public static boolean isEnabledStatic = false;

    public Optimizer() {
        super("Optimizer", "Optimizes the game by disabling heavy visuals", Category.MISC);
    }

    @Override
    public void onEnable() {
        isEnabledStatic = true;
        if (mc.world != null) {
            // Force a reload of chunks to apply some optimizations if needed
            // mc.worldRenderer.reload(); // This might be too heavy, let's just use flags
        }
    }

    @Override
    public void onDisable() {
        isEnabledStatic = false;
    }
}
