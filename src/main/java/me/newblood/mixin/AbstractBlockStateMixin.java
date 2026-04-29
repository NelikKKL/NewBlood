package me.newblood.mixin;

import me.newblood.NewBloodClient;
import me.newblood.module.modules.XRay;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract net.minecraft.block.Block getBlock();

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void onGetRenderType(CallbackInfoReturnable<BlockRenderType> info) {
        if (XRay.enabled && !XRay.ORES.contains(getBlock())) {
            info.setReturnValue(BlockRenderType.INVISIBLE);
        }
    }

    @Inject(method = "getOpacity", at = @At("HEAD"), cancellable = true)
    private void onGetOpacity(BlockView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
        if (XRay.enabled && !XRay.ORES.contains(getBlock())) {
            info.setReturnValue(0);
        }
    }

    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
    private void onGetAmbientOcclusionLightLevel(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        if (XRay.enabled) {
            info.setReturnValue(1.0f);
        }
    }

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    private void onGetLuminance(CallbackInfoReturnable<Integer> info) {
        if (XRay.enabled && XRay.ORES.contains(getBlock())) {
            info.setReturnValue(15);
        }
    }

    @Inject(method = "isOpaque", at = @At("HEAD"), cancellable = true)
    private void onIsOpaque(CallbackInfoReturnable<Boolean> info) {
        if (XRay.enabled && !XRay.ORES.contains(getBlock())) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "calcBlockBreakingDelta", at = @At("RETURN"), cancellable = true)
    private void onCalcBlockBreakingDelta(net.minecraft.entity.player.PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        if (me.newblood.module.modules.SpeedMine.enabledStatic) {
            info.setReturnValue(info.getReturnValue() * 1.4f);
        }
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(BlockView world, BlockPos pos, net.minecraft.block.ShapeContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.getModuleManager() != null) {
            boolean noClip = NewBloodClient.INSTANCE.getModuleManager().getModuleByName("NoClip").isEnabled();
            boolean freeCam = NewBloodClient.INSTANCE.getModuleManager().getModuleByName("FreeCam").isEnabled();
            
            if (noClip || freeCam) {
                if (freeCam) {
                    info.setReturnValue(VoxelShapes.empty());
                    return;
                }
                
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null) {
                    // Если игрок нажимает Shift или Пробел, мы временно отключаем коллизию пола,
                    // чтобы он мог переместиться по вертикали.
                    boolean movingVertical = MinecraftClient.getInstance().options.jumpKey.isPressed() 
                                           || MinecraftClient.getInstance().options.sneakKey.isPressed();

                    // Игрок не должен проваливаться сквозь пол, если он не хочет переместиться вниз.
                    // Если блок находится ниже уровня ног игрока и игрок не нажимает Shift/Space, сохраняем коллизию.
                    if (pos.getY() < player.getY() - 0.1 && !movingVertical) {
                        return;
                    }
                    
                    // Для NoClip делаем остальные блоки (стены и блоки на уровне игрока) проходимыми.
                    info.setReturnValue(VoxelShapes.empty());
                }
            }
        }
    }
}
