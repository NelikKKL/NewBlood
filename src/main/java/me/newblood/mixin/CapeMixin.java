package me.newblood.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class CapeMixin {
    private static final Identifier CAPE_TEXTURE = new Identifier("newblood", "textures/cape.png");

    @Inject(method = "getCapeTexture", at = @At("HEAD"), cancellable = true)
    private void onGetCapeTexture(CallbackInfoReturnable<Identifier> info) {
        // Устанавливаем плащ только для игрока (себя)
        info.setReturnValue(CAPE_TEXTURE);
    }

    @Inject(method = "canRenderCapeTexture", at = @At("HEAD"), cancellable = true)
    private void onCanRenderCapeTexture(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }
}
