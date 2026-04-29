package me.newblood.mixin;

import me.newblood.NewBloodClient;
import me.newblood.module.modules.FreeCam;
import me.newblood.module.modules.IceWalk;
import me.newblood.ui.clickgui.ClickGuiScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if (IceWalk.enabledStatic) {
            ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
            if (player.getHungerManager().getFoodLevel() > 0) {
                // Periodically add a bit of exhaustion back to reduce total hunger loss
                // This is a hacky way to "slow down" hunger since we can't easily cancel exhaustion ticks
                player.getHungerManager().addExhaustion(-0.01f);
            }
        }
        if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
            if (!(MinecraftClient.getInstance().currentScreen instanceof ClickGuiScreen)) {
                MinecraftClient.getInstance().setScreen(new ClickGuiScreen());
            }
        }
    }

    @Inject(method = "move", at = @At("HEAD"))
    private void onMove(MovementType type, Vec3d movement, CallbackInfo info) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.getModuleManager() != null) {
            if (FreeCam.enabled) {
                player.noClip = true;
            }
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void onPushOutOfBlocks(double x, double z, CallbackInfo info) {
        if (NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.getModuleManager() != null) {
            if (NewBloodClient.INSTANCE.getModuleManager().getModuleByName("NoClip").isEnabled() || FreeCam.enabled) {
                info.cancel();
            }
        }
    }
}
