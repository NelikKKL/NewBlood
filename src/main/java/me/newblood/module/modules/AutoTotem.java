package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", "Automatically puts a totem in your offhand", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // If offhand already has totem, do nothing
        if (mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;
        
        // Search for totem in inventory
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                // Move totem to offhand
                int slot = i < 9 ? i + 36 : i; // Hotbar slots are 36-44 in container terms
                mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot, 40, SlotActionType.SWAP, mc.player);
                break;
            }
        }
    }
}
