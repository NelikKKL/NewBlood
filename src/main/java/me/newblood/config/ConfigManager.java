package me.newblood.config;

import com.google.gson.*;
import me.newblood.NewBloodClient;
import me.newblood.module.Module;
import me.newblood.module.settings.BooleanSetting;
import me.newblood.module.settings.Setting;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private final File configFolder;
    private final File defaultConfigFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager() {
        // Minecraft game directory is where mods and resourcepacks are
        File gameDir = MinecraftClient.getInstance().runDirectory;
        this.configFolder = new File(gameDir, "newblood_configs");
        this.defaultConfigFile = new File(configFolder, "default.json");

        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
    }

    public void save() {
        save("default");
    }

    public void save(String name) {
        try {
            if (!configFolder.exists()) configFolder.mkdirs();
            File file = new File(configFolder, name + ".json");
            
            JsonObject json = new JsonObject();
            JsonArray modulesArray = new JsonArray();

            for (Module m : NewBloodClient.INSTANCE.getModuleManager().getModules()) {
                JsonObject moduleJson = new JsonObject();
                moduleJson.addProperty("name", m.getName());
                moduleJson.addProperty("enabled", m.isEnabled());
                moduleJson.addProperty("key", m.getKey());

                JsonObject settingsJson = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    if (s instanceof BooleanSetting) {
                        settingsJson.addProperty(s.getName(), ((BooleanSetting) s).getValue());
                    }
                }
                moduleJson.add("settings", settingsJson);
                modulesArray.add(moduleJson);
            }

            json.add("modules", modulesArray);

            try (Writer writer = new FileWriter(file)) {
                gson.toJson(json, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        load("default");
    }

    public void load(String name) {
        File file = new File(configFolder, name + ".json");
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            if (json == null || !json.has("modules")) return;

            JsonArray modulesArray = json.getAsJsonArray("modules");
            if (modulesArray == null) return;

            for (JsonElement element : modulesArray) {
                if (element == null || !element.isJsonObject()) continue;
                JsonObject moduleJson = element.getAsJsonObject();
                
                if (!moduleJson.has("name")) continue;
                String moduleName = moduleJson.get("name").getAsString();
                Module m = NewBloodClient.INSTANCE.getModuleManager().getModuleByName(moduleName);

                if (m != null) {
                    if (moduleJson.has("enabled") && !moduleJson.get("enabled").isJsonNull()) {
                        boolean enabled = moduleJson.get("enabled").getAsBoolean();
                        if (enabled != m.isEnabled()) m.toggle();
                    }
                    if (moduleJson.has("key") && !moduleJson.get("key").isJsonNull()) {
                        m.setKey(moduleJson.get("key").getAsInt());
                    }

                    if (moduleJson.has("settings") && !moduleJson.get("settings").isJsonNull()) {
                        JsonObject settingsJson = moduleJson.getAsJsonObject("settings");
                        for (Setting<?> s : m.getSettings()) {
                            if (s instanceof BooleanSetting && settingsJson.has(s.getName()) && !settingsJson.get(s.getName()).isJsonNull()) {
                                ((BooleanSetting) s).setValue(settingsJson.get(s.getName()).getAsBoolean());
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public java.util.List<String> getConfigs() {
        java.util.List<String> configs = new java.util.ArrayList<>();
        if (configFolder.exists() && configFolder.isDirectory()) {
            File[] files = configFolder.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File f : files) {
                    configs.add(f.getName().replace(".json", ""));
                }
            }
        }
        return configs;
    }

    public File getConfigFolder() {
        return configFolder;
    }
}
