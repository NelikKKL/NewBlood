package me.newblood.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.newblood.NewBloodClient;
import me.newblood.module.modules.ClickTP;
import me.newblood.module.modules.ESP;
import me.newblood.module.modules.JumpCircle;
import me.newblood.module.modules.Optimizer;
import me.newblood.module.modules.Tracers;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void onRenderWeather(LightmapTextureManager manager, float tickDelta, double x, double y, double z, CallbackInfo info) {
        if (Optimizer.isEnabledStatic) {
            info.cancel();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, net.minecraft.client.render.Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo info) {
        Tracers tracers = (Tracers) NewBloodClient.INSTANCE.getModuleManager().getModuleByName("Tracers");
        if (tracers != null && tracers.isEnabled() && NewBloodClient.INSTANCE.mc.world != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.disableDepthTest();

            Vec3d cameraPos = camera.getPos();
            
            matrices.push();
            // Move to camera position
            matrices.translate(0, 0, 0); // Already there relative to camera in some contexts, but let's be explicit
            
            // Vector looking forward from camera (0.1 blocks forward to be near crosshair)
            // In camera space, forward is (0, 0, -1)
            Vec3d forward = new Vec3d(0, 0, -0.1).rotateX(-(float)Math.toRadians(camera.getPitch())).rotateY(-(float)Math.toRadians(camera.getYaw()));
            
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            Matrix4f matrix = matrices.peek().getPositionMatrix();

            for (Entity entity : NewBloodClient.INSTANCE.mc.world.getEntities()) {
                if (tracers.isValidTarget(entity)) {
                    double x = entity.prevX + (entity.getX() - entity.prevX) * tickDelta - cameraPos.x;
                    double y = entity.prevY + (entity.getY() - entity.prevY) * tickDelta - cameraPos.y;
                    double z = entity.prevZ + (entity.getZ() - entity.prevZ) * tickDelta - cameraPos.z;

                    // Tracer line from crosshair (forward vector in camera-relative space)
                    bufferBuilder.vertex(matrix, (float)forward.x, (float)forward.y, (float)forward.z).color(255, 0, 0, 255).next();
                    bufferBuilder.vertex(matrix, (float)x, (float)(y + entity.getEyeHeight(entity.getPose())), (float)z).color(255, 0, 0, 255).next();

                    // Hitbox
                    Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ()).offset(x, y, z);
                    drawBox(bufferBuilder, matrix, box, 255, 0, 0, 255);
                }
            }

            tessellator.draw();
            matrices.pop();
            
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }

        if (ESP.enabled && NewBloodClient.INSTANCE.mc.world != null) {
            Vec3d cameraPos = camera.getPos();
            TextRenderer textRenderer = NewBloodClient.INSTANCE.mc.textRenderer;

            for (Entity entity : NewBloodClient.INSTANCE.mc.world.getEntities()) {
                if (entity instanceof LivingEntity living && entity != NewBloodClient.INSTANCE.mc.player && entity.isAlive()) {
                    double x = entity.prevX + (entity.getX() - entity.prevX) * tickDelta - cameraPos.x;
                    double y = entity.prevY + (entity.getY() - entity.prevY) * tickDelta - cameraPos.y;
                    double z = entity.prevZ + (entity.getZ() - entity.prevZ) * tickDelta - cameraPos.z;

                    matrices.push();
                    matrices.translate(x, y + entity.getHeight() + 0.5, z);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                    matrices.scale(-0.025f, -0.025f, 0.025f);

                    String text = String.format("%s §a%dHP", entity.getName().getString(), (int) living.getHealth());
                    if (entity instanceof PlayerEntity player) {
                        text = String.format("§6[P] §f%s §a%dHP", player.getName().getString(), (int) player.getHealth());
                    }
                    
                    float width = textRenderer.getWidth(text) / 2f;
                    textRenderer.draw(text, -width, 0, 0xFFFFFFFF, true, matrices.peek().getPositionMatrix(), NewBloodClient.INSTANCE.mc.getBufferBuilders().getEntityVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
                    
                    matrices.pop();
                }
            }
        }

        if (ClickTP.targetPos != null && NewBloodClient.INSTANCE.mc.world != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.disableDepthTest();

            Vec3d cameraPos = camera.getPos();
            matrices.push();
            matrices.translate(ClickTP.targetPos.x - cameraPos.x, ClickTP.targetPos.y - cameraPos.y, ClickTP.targetPos.z - cameraPos.z);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            Box box = new Box(-0.2, -0.2, -0.2, 0.2, 0.2, 0.2);
            drawBox(bufferBuilder, matrix, box, 255, 0, 0, 150);

            tessellator.draw();
            matrices.pop();
            
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }

        if (JumpCircle.enabled && NewBloodClient.INSTANCE.mc.player != null && !NewBloodClient.INSTANCE.mc.player.isOnGround()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.disableDepthTest();

            Vec3d cameraPos = camera.getPos();
            
            matrices.push();
            
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            Matrix4f matrix = matrices.peek().getPositionMatrix();

            double playerX = NewBloodClient.INSTANCE.mc.player.prevX + (NewBloodClient.INSTANCE.mc.player.getX() - NewBloodClient.INSTANCE.mc.player.prevX) * tickDelta - cameraPos.x;
            double playerY = NewBloodClient.INSTANCE.mc.player.prevY + (NewBloodClient.INSTANCE.mc.player.getY() - NewBloodClient.INSTANCE.mc.player.prevY) * tickDelta - cameraPos.y;
            double playerZ = NewBloodClient.INSTANCE.mc.player.prevZ + (NewBloodClient.INSTANCE.mc.player.getZ() - NewBloodClient.INSTANCE.mc.player.prevZ) * tickDelta - cameraPos.z;

            double radius = 1.0;
            int segments = 36;
            for (int i = 0; i < segments; i++) {
                double angle1 = Math.toRadians((i * 360.0) / segments);
                double angle2 = Math.toRadians(((i + 1) * 360.0) / segments);
                
                float x1 = (float)(playerX + Math.cos(angle1) * radius);
                float z1 = (float)(playerZ + Math.sin(angle1) * radius);
                float x2 = (float)(playerX + Math.cos(angle2) * radius);
                float z2 = (float)(playerZ + Math.sin(angle2) * radius);
                
                bufferBuilder.vertex(matrix, x1, (float)playerY, z1).color(255, 0, 0, 255).next();
                bufferBuilder.vertex(matrix, x2, (float)playerY, z2).color(255, 0, 0, 255).next();
            }
            
            tessellator.draw();
            matrices.pop();
            
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private void drawBox(BufferBuilder bufferBuilder, Matrix4f matrix, Box box, int r, int g, int b, int a) {
        // Bottom
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(r, g, b, a).next();

        // Top
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).next();

        // Sides
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).next();
    }
}
