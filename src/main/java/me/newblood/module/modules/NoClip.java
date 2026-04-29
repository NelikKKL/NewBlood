package me.newblood.module.modules;

import me.newblood.module.Module;

public class NoClip extends Module {
    private boolean jumpPressed = false;
    private boolean sneakPressed = false;

    public NoClip() {
        super("NoClip", "Walk through blocks horizontally. Use Space/Shift inside blocks to go Up/Down.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // Проверяем, находится ли игрок внутри блоков (горизонтальная коллизия или внутри блока)
        boolean inBlock = mc.world.getBlockState(mc.player.getBlockPos()).isFullCube(mc.world, mc.player.getBlockPos()) 
                        || mc.world.getBlockState(mc.player.getBlockPos().up()).isFullCube(mc.world, mc.player.getBlockPos().up());

        if (inBlock) {
            // Логика перемещения вверх на Пробел
            if (mc.options.jumpKey.isPressed()) {
                if (!jumpPressed) {
                    mc.player.setPosition(mc.player.getX(), mc.player.getY() + 1.0, mc.player.getZ());
                    jumpPressed = true;
                }
            } else {
                jumpPressed = false;
            }

            // Логика перемещения вниз на Shift
            if (mc.options.sneakKey.isPressed()) {
                if (!sneakPressed) {
                    mc.player.setPosition(mc.player.getX(), mc.player.getY() - 1.0, mc.player.getZ());
                    sneakPressed = true;
                }
            } else {
                sneakPressed = false;
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.noClip = false;
        }
    }
}
