package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoGapple extends Module {
    private int oldSlot = -1;
    private boolean eating = false;

    public AutoGapple() {
        super("AutoGapple", "Automatically eats Enchanted Golden Apples", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // Condition to eat: health < 16 or absorption is 0
        boolean shouldEat = mc.player.getHealth() < 16 || mc.player.getAbsorptionAmount() <= 0;

        if (shouldEat) {
            int gappleSlot = findGapple();
            if (gappleSlot != -1) {
                if (!eating) {
                    oldSlot = mc.player.getInventory().selectedSlot;
                    mc.player.getInventory().selectedSlot = gappleSlot;
                    eating = true;
                }
                mc.options.useKey.setPressed(true);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            } else {
                stopEating();
            }
        } else {
            stopEating();
        }
    }

    private void stopEating() {
        if (eating) {
            mc.options.useKey.setPressed(false);
            if (oldSlot != -1) {
                mc.player.getInventory().selectedSlot = oldSlot;
            }
            eating = false;
            oldSlot = -1;
        }
    }

    private int findGapple() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE || stack.getItem() == Items.GOLDEN_APPLE) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDisable() {
        stopEating();
    }
}
