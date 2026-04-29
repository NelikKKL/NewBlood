package me.newblood.module.modules;

import me.newblood.module.Module;

public class ItemPhysics extends Module {
    public static boolean enabledStatic = false;

    public ItemPhysics() {
        super("ItemPhysics", "Реалистичная физика выброшенных предметов", Category.RENDER);
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
