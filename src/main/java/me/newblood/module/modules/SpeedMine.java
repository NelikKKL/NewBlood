package me.newblood.module.modules;

import me.newblood.module.Module;

public class SpeedMine extends Module {
    public static boolean enabledStatic = false;

    public SpeedMine() {
        super("SpeedMine", "Accelerates block breaking correctly", Category.MISC);
    }

    @Override
    public void onTick() {
        enabledStatic = isEnabled();
    }

    @Override
    public void onEnable() {
        enabledStatic = true;
    }

    @Override
    public void onDisable() {
        enabledStatic = false;
    }
}
