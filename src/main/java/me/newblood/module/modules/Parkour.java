package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Parkour extends Module {
    public Parkour() {
        super("Parkour", "Smart and legit parkour. Automatically jumps and sprints for gaps up to 5 blocks.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        
        // Модуль не мешает, если вы крадетесь
        if (mc.player.isSneaking()) return;
        
        // Работаем только на земле
        if (!mc.player.isOnGround() || mc.player.isFallFlying()) return;

        // Если игрок не пытается идти вперед, ничего не делаем
        if (mc.player.forwardSpeed <= 0) return;

        // Получаем направление взгляда
        double yaw = Math.toRadians(mc.player.getYaw());
        double dirX = -Math.sin(yaw);
        double dirZ = Math.cos(yaw);

        // 1. Умное сканирование: определяем ширину пропасти впереди
        int gapWidth = 0;
        boolean foundLanding = false;
        
        for (int i = 1; i <= 6; i++) {
            BlockPos checkPos = new BlockPos(
                (int)Math.floor(mc.player.getX() + dirX * i),
                (int)Math.floor(mc.player.getY() - 0.5),
                (int)Math.floor(mc.player.getZ() + dirZ * i)
            );
            
            if (mc.world.getBlockState(checkPos).isAir()) {
                gapWidth++;
            } else {
                foundLanding = true;
                break;
            }
        }

        // Если впереди есть пропасть и мы нашли куда прыгать
        if (gapWidth > 0 && foundLanding) {
            
            // 2. Легитное ускорение: если пропасть большая (4-5 блоков), включаем спринт заранее
            if (gapWidth >= 3) {
                mc.player.setSprinting(true);
            }

            // 3. Точный момент прыжка: 
            // Проверяем блок прямо под передней частью игрока
            double edgeCheckX = mc.player.getX() + dirX * 0.1;
            double edgeCheckZ = mc.player.getZ() + dirZ * 0.1;
            BlockPos edgePos = new BlockPos((int)Math.floor(edgeCheckX), (int)Math.floor(mc.player.getY() - 0.5), (int)Math.floor(edgeCheckZ));

            // Если мы уже почти на самом краю (под нами все еще блок, но впереди 0.1 - пустота)
            if (mc.world.getBlockState(edgePos.add((int)Math.signum(dirX), 0, (int)Math.signum(dirZ))).isAir()) {
                // Совершаем обычный прыжок. Без искусственных ускорений (boost).
                // Прыжок будет длинным за счет того, что мы уже в спринте и прыгаем в идеальный момент.
                mc.player.jump();
                
                // Чтобы прыгнуть на 5 блоков легитно, мы просто поддерживаем спринт в воздухе
                if (gapWidth >= 4) {
                    mc.player.setSprinting(true);
                }
            }
        }
    }
}
