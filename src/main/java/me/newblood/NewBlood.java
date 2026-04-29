package me.newblood;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewBlood implements ModInitializer {
    public static final String MOD_ID = "newblood";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("NewBlood Cheat Mod Initialized!");
    }
}
