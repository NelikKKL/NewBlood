package me.newblood.module;

import me.newblood.module.settings.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int key;
    private final List<Setting<?>> settings = new ArrayList<>();
    protected final MinecraftClient mc = MinecraftClient.getInstance();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
        this.key = 0; // 0 means no keybound
    }

    public void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
        if (me.newblood.NewBloodClient.INSTANCE != null && me.newblood.NewBloodClient.INSTANCE.getConfigManager() != null) {
            me.newblood.NewBloodClient.INSTANCE.getConfigManager().save();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }

    public enum Category {
        COMBAT("Бой"), 
        MOVEMENT("Движение"), 
        RENDER("Визуальные"), 
        MISC("Прочее");

        private final String name;
        Category(String name) { this.name = name; }
        public String getName() { return name; }
    }
}
