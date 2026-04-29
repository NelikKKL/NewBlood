package me.newblood.mixin;

import me.newblood.module.modules.SpeedMine;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow private float currentBreakingProgress;
    @Shadow private int blockBreakingCooldown;

    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"))
    private void onUpdateBlockBreakingProgressHead(net.minecraft.util.math.BlockPos pos, net.minecraft.util.math.Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (SpeedMine.enabledStatic) {
            currentBreakingProgress += 0.2f; // Force progress increase
        }
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At("RETURN"))
    private void onUpdateBlockBreakingProgress(net.minecraft.util.math.BlockPos pos, net.minecraft.util.math.Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (SpeedMine.enabledStatic) {
            if (currentBreakingProgress >= 0.7f) {
                currentBreakingProgress = 1.0f;
            }
            blockBreakingCooldown = 0;
        }
    }
}