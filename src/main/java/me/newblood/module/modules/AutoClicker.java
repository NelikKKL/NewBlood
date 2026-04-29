package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoClicker extends Module {
    private int timer = 0;
    private final int delay = 2; // ~10 CPS

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;

        // Auto attack when holding attack key
        if (mc.options.attackKey.isPressed()) {
            timer++;
            if (timer >= delay) {
                mc.player.swingHand(Hand.MAIN_HAND);
                
                if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHit = (EntityHitResult) mc.crosshairTarget;
                    mc.interactionManager.attackEntity(mc.player, entityHit.getEntity());
                } else if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                    // This triggers block breaking if holding attack
                    // No need to manually call breakBlock as Minecraft handles this while attackKey is pressed
                }
                
                timer = 0;
            }
        } else {
            timer = 0;
        }
    }
}
