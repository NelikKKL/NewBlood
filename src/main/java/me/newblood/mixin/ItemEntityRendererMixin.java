package me.newblood.mixin;

import me.newblood.module.modules.ItemPhysics;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {

    @Redirect(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", 
              at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private void onTranslate(MatrixStack stack, float x, float y, float z, ItemEntity entity, float f, float g) {
        if (ItemPhysics.enabledStatic && entity.isOnGround()) {
            stack.translate(x, 0.05f, z);
        } else {
            stack.translate(x, y, z);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", 
              at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"))
    private void onMultiply(MatrixStack stack, Quaternionf quaternion, ItemEntity entity, float f, float g) {
        if (ItemPhysics.enabledStatic) {
            if (entity.isOnGround()) {
                stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            } else {
                float rotation = (entity.age + g) * 5.0f;
                stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
                stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
                stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
            }
        } else {
            stack.multiply(quaternion);
        }
    }
}
