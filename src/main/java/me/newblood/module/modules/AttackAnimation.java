package me.newblood.module.modules;

import me.newblood.module.Module;

public class AttackAnimation extends Module {
    public static boolean isEnabledStatic = false;

    public AttackAnimation() {
        super("AttackAnimation", "Changes attack animation", Category.RENDER);
    }

    @Override
    public void onEnable() {
        isEnabledStatic = true;
    }

    @Override
    public void onDisable() {
        isEnabledStatic = false;
    }
}
