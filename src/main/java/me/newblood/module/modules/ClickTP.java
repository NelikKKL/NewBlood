package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class ClickTP extends Module {
    public static Vec3d targetPos = null;
    private final double maxRange = 100.0;

    public ClickTP() {
        super("ClickTP", "Teleports you to the block you click", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!isEnabled() || mc.player == null || mc.world == null) {
            targetPos = null;
            return;
        }

        // Custom long-range raycast to find target position for rendering
        Vec3d eyePos = mc.player.getEyePos();
        Vec3d lookVec = mc.player.getRotationVec(1.0F);
        Vec3d endPos = eyePos.add(lookVec.multiply(maxRange));

        HitResult hit = mc.world.raycast(new RaycastContext(
                eyePos,
                endPos,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                mc.player
        ));

        if (hit.getType() != HitResult.Type.MISS) {
            targetPos = hit.getPos();
        } else {
            targetPos = endPos;
        }
    }

    public void onMouseClick(int button) {
        if (!isEnabled()) return;
        if (button == 0 && mc.player != null && targetPos != null) { // Left Click
            mc.player.setPosition(targetPos.x, targetPos.y, targetPos.z);
        }
    }

    @Override
    public void onDisable() {
        targetPos = null;
        super.onDisable();
    }
}
