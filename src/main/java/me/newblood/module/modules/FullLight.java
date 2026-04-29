package me.newblood.module.modules;

import me.newblood.module.Module;

public class FullLight extends Module {
    public FullLight() {
        super("FullLight", "Makes everything bright as day (Night Vision effect)", Category.RENDER);
    }

    @Override
    public void onEnable() {
        // Now handled by GameRendererMixin
    }

    @Override
    public void onTick() {
        // Now handled by GameRendererMixin
    }

    @Override
    public void onDisable() {
        // Now handled by GameRendererMixin
    }
}
