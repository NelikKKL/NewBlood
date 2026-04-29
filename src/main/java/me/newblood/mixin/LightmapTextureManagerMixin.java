package me.newblood.mixin;

import me.newblood.NewBloodClient;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void onGetBrightness(net.minecraft.world.dimension.DimensionType type, int lightLevel, CallbackInfoReturnable<Float> info) {
        if (NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.getModuleManager() != null) {
            if (NewBloodClient.INSTANCE.getModuleManager().getModuleByName("FullLight").isEnabled()) {
                info.setReturnValue(15.0f);
            }
        }
    }
}
