package me.newblood.mixin;

import me.newblood.NewBloodClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionStrength(net.minecraft.entity.LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> info) {
        if (NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.getModuleManager() != null) {
            if (NewBloodClient.INSTANCE.getModuleManager().getModuleByName("FullLight").isEnabled()) {
                info.setReturnValue(1.0f);
            }
        }
    }
}
