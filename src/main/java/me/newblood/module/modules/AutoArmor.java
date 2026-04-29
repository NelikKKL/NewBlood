package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {
    public AutoArmor() {
        super("AutoArmor", "Automatically puts on the best armor", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem)) continue;

            ArmorItem armorItem = (ArmorItem) stack.getItem();
            int armorSlot = -1;
            switch (armorItem.getSlotType()) {
                case FEET: armorSlot = 0; break;
                case LEGS: armorSlot = 1; break;
                case CHEST: armorSlot = 2; break;
                case HEAD: armorSlot = 3; break;
                default: continue;
            }

            if (armorSlot == -1) continue;

            ItemStack currentArmor = mc.player.getInventory().getArmorStack(armorSlot);
            if (currentArmor.isEmpty()) {
                int invSlot = i < 9 ? i + 36 : i;
                mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, invSlot, 0, SlotActionType.QUICK_MOVE, mc.player);
            }
        }
    }
}
