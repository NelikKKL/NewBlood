package me.newblood.module.modules;

import me.newblood.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.HashSet;
import java.util.Set;

public class XRay extends Module {
    public static final Set<Block> ORES = new HashSet<>();
    public static boolean enabled = false;

    public XRay() {
        super("XRay", "Allows you to see ores through walls", Category.RENDER);
        ORES.add(Blocks.DIAMOND_ORE);
        ORES.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        ORES.add(Blocks.GOLD_ORE);
        ORES.add(Blocks.DEEPSLATE_GOLD_ORE);
        ORES.add(Blocks.IRON_ORE);
        ORES.add(Blocks.DEEPSLATE_IRON_ORE);
        ORES.add(Blocks.COAL_ORE);
        ORES.add(Blocks.DEEPSLATE_COAL_ORE);
        ORES.add(Blocks.NETHER_QUARTZ_ORE);
        ORES.add(Blocks.NETHER_GOLD_ORE);
        ORES.add(Blocks.ANCIENT_DEBRIS);
        ORES.add(Blocks.EMERALD_ORE);
        ORES.add(Blocks.DEEPSLATE_EMERALD_ORE);
        ORES.add(Blocks.LAPIS_ORE);
        ORES.add(Blocks.DEEPSLATE_LAPIS_ORE);
        ORES.add(Blocks.REDSTONE_ORE);
        ORES.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        ORES.add(Blocks.COPPER_ORE);
        ORES.add(Blocks.DEEPSLATE_COPPER_ORE);
        ORES.add(Blocks.RAW_IRON_BLOCK);
        ORES.add(Blocks.RAW_GOLD_BLOCK);
        ORES.add(Blocks.RAW_COPPER_BLOCK);
        ORES.add(Blocks.CHEST);
        ORES.add(Blocks.TRAPPED_CHEST);
        ORES.add(Blocks.ENDER_CHEST);
        ORES.add(Blocks.SPAWNER);
    }

    @Override
    public void onEnable() {
        enabled = true;
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        enabled = false;
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
}
