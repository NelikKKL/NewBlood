package me.newblood.mixin;

import me.newblood.NewBloodClient;
import me.newblood.module.Module;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onHandleInputEvents(CallbackInfo info) {
        MinecraftClient mc = (MinecraftClient) (Object) this;
        if (mc.player == null || mc.currentScreen != null) return;

        // Check for mouse clicks for ClickTP
        if (mc.options.attackKey.wasPressed()) {
            me.newblood.module.modules.ClickTP clickTP = (me.newblood.module.modules.ClickTP) NewBloodClient.INSTANCE.getModuleManager().getModuleByName("ClickTP");
            if (clickTP != null) {
                clickTP.onMouseClick(0); // 0 is left click
            }
        }
    }
}
