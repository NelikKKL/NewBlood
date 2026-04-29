package me.newblood.mixin;

import me.newblood.module.modules.AttackAnimation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void onRenderItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, net.minecraft.item.ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (AttackAnimation.isEnabledStatic && hand == Hand.MAIN_HAND && swingProgress > 0) {
            // Apply custom animation: rotate and scale
            float f = (float) Math.sin(Math.sqrt(swingProgress) * Math.PI);
            matrices.translate(0.15f, 0.15f, 0.15f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f * -30.0f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * -60.0f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * -30.0f));
            matrices.translate(-0.15f, -0.15f, -0.15f);
        }
    }
}
