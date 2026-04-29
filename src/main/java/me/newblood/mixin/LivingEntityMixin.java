package me.newblood.mixin;

import me.newblood.module.modules.FreeCam;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (FreeCam.enabled) {
            // Cancel damage effects client-side for the player
            // This prevents the camera from shaking or showing red when in "camera mode"
            if ((Object) this == net.minecraft.client.MinecraftClient.getInstance().player) {
                info.setReturnValue(false);
                info.cancel();
            }
        }
    }
}
