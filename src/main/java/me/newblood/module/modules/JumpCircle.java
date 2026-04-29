package me.newblood.module.modules;

import me.newblood.module.Module;

public class JumpCircle extends Module {
    public static boolean enabled = false;

    public JumpCircle() {
        super("JumpCircle", "Renders a red circle under you when you jump", Category.RENDER);
    }

    @Override
    public void onEnable() {
        enabled = true;
    }

    @Override
    public void onDisable() {
        enabled = false;
    }
}
