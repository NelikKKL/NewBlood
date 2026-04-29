package me.newblood.ui.clickgui;

import me.newblood.NewBloodClient;
import me.newblood.module.Module;
import me.newblood.utils.SnowEffect;
import me.newblood.module.settings.BooleanSetting;
import me.newblood.module.settings.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private final SnowEffect snowEffect = new SnowEffect(100);
    private final List<CategoryPanel> panels = new ArrayList<>();
    private ConfigPanel configPanel;

    public ClickGuiScreen() {
        super(Text.literal("ClickGUI"));
    }

    private static String getSpecialKeyName(int key) {
        return switch (key) {
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT -> "LShift";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT -> "RShift";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL -> "LCtrl";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCtrl";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT -> "LAlt";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_ALT -> "RAlt";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_CAPS_LOCK -> "Caps";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_TAB -> "Tab";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER -> "Enter";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE -> "Back";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_INSERT -> "Ins";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE -> "Del";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_HOME -> "Home";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_END -> "End";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP -> "PgUp";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN -> "PgDn";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_UP -> "Up";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN -> "Down";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT -> "Left";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT -> "Right";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F1 -> "F1";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F2 -> "F2";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F3 -> "F3";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F4 -> "F4";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F5 -> "F5";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F6 -> "F6";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F7 -> "F7";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F8 -> "F8";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F9 -> "F9";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F10 -> "F10";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F11 -> "F11";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_F12 -> "F12";
            default -> "KEY-" + key;
        };
    }

    @Override
    protected void init() {
        if (!panels.isEmpty()) {
            // Keep existing panels but check if they are off-screen
            for (CategoryPanel panel : panels) {
                if (panel.x + 40 > this.width) panel.x = this.width - 90;
                if (panel.y + 12 > this.height) panel.y = this.height - 18;
                if (panel.x < 0) panel.x = 0;
                if (panel.y < 0) panel.y = 0;
            }
            if (configPanel != null) {
                if (configPanel.x + 40 > this.width) configPanel.x = this.width - 90;
                if (configPanel.y + 12 > this.height) configPanel.y = this.height - 18;
                if (configPanel.x < 0) configPanel.x = 0;
                if (configPanel.y < 0) configPanel.y = 0;
            }
            return;
        }
        
        panels.clear();
        int startX = 10;
        int startY = 30;
        int currentX = startX;
        int currentY = startY;
        
        // Always create panels for each category, buttons will be populated in render if needed
        for (Module.Category category : Module.Category.values()) {
            panels.add(new CategoryPanel(category, currentX, currentY));
            currentX += 95; // Compact horizontal spacing
        }
        configPanel = new ConfigPanel(currentX, currentY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dark gray background
        context.fill(0, 0, this.width, this.height, new Color(20, 20, 20, 230).getRGB());

        // Snow effect
        for (SnowEffect.Snowflake snowflake : snowEffect.getSnowflakes()) {
            snowflake.update(this.width, this.height);
            context.fill((int) snowflake.x, (int) snowflake.y, (int) (snowflake.x + snowflake.size), (int) (snowflake.y + snowflake.size), new Color(255, 255, 255, 150).getRGB());
        }

        // 8-bit Red Logo
        String title = "NEW BLOOD";
        int titleX = 20;
        int titleY = 20;
        
        // Draw 8-bit style logo (shadow + main text)
        context.drawTextWithShadow(this.textRenderer, title, titleX + 2, titleY + 2, new Color(100, 0, 0, 255).getRGB());
        context.drawText(this.textRenderer, title, titleX, titleY, new Color(255, 0, 0, 255).getRGB(), false);
        
        // Draw a small 8-bit blood drop icon
        context.fill(titleX + 85, titleY + 2, titleX + 89, titleY + 10, 0xFFFF0000);
        context.fill(titleX + 83, titleY + 4, titleX + 91, titleY + 8, 0xFFFF0000);

        // Panels
        for (CategoryPanel panel : panels) {
            panel.render(context, mouseX, mouseY);
        }
        if (configPanel != null) configPanel.render(context, mouseX, mouseY);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) return true;
        }
        if (configPanel != null && configPanel.mouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseReleased(mouseX, mouseY, button)) return true;
        }
        if (configPanel != null && configPanel.mouseReleased(mouseX, mouseY, button)) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (CategoryPanel panel : panels) {
            if (mouseX >= panel.x && mouseX <= panel.x + 90 && mouseY >= panel.y && mouseY <= panel.y + 18 + CategoryPanel.MAX_HEIGHT) {
                // Inline scroll for panels
                if (panel.expanded) {
                    int totalContentHeight = 0;
                    for (ModuleButton mb : panel.buttons) totalContentHeight += mb.getHeight();
                    int maxScroll = Math.max(0, totalContentHeight - CategoryPanel.MAX_HEIGHT);
                    panel.scrollOffset -= amount * 12;
                    if (panel.scrollOffset < 0) panel.scrollOffset = 0;
                    if (panel.scrollOffset > maxScroll) panel.scrollOffset = maxScroll;
                    return true;
                }
            }
        }
        if (configPanel != null && mouseX >= configPanel.x && mouseX <= configPanel.x + 90 && mouseY >= configPanel.y && mouseY <= configPanel.y + 18 + ConfigPanel.MAX_HEIGHT) {
            configPanel.scrollOffset -= amount * 12;
            if (configPanel.scrollOffset < 0) configPanel.scrollOffset = 0;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_R && (modifiers & org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL) != 0) {
            panels.clear();
            init();
            return true;
        }
        for (CategoryPanel panel : panels) {
            if (panel.expanded) {
                for (ModuleButton mb : panel.buttons) {
                    if (mb.keyPressed(keyCode, scanCode, modifiers)) return true;
                }
            }
        }
        if (configPanel != null && configPanel.keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (configPanel != null) {
            configPanel.charTyped(chr);
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static class ConfigPanel {
        private int x, y;
        private int dragX, dragY;
        private boolean dragging = false;
        private String currentInput = "";
        private boolean typing = false;
        private int scrollOffset = 0;
        private static final int MAX_HEIGHT = 180;

        public ConfigPanel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private boolean scrolling = false;

        public void render(DrawContext context, int mouseX, int mouseY) {
            if (dragging) {
                x = mouseX - dragX;
                y = mouseY - dragY;
            }

            context.fill(x, y, x + 90, y + 18, new Color(30, 30, 30, 255).getRGB());
            context.fill(x, y + 17, x + 90, y + 18, 0xFFFF0000);
            context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, "Configs", x + 5, y + 5, -1);

            // Input field
            int inputY = y + 20;
            boolean hoveredInput = mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= inputY && mouseY <= inputY + 14;
            context.fill(x + 3, inputY, x + 87, inputY + 14, typing ? new Color(50, 50, 50).getRGB() : new Color(25, 25, 25).getRGB());
            String displayText = currentInput.isEmpty() ? (typing ? "_" : "Name...") : currentInput + (typing ? "_" : "");
            context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, displayText, x + 6, inputY + 3, typing ? -1 : 0xFFAAAAAA);

            // Save button
            int saveY = inputY + 16;
            boolean hoveredSave = mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= saveY && mouseY <= saveY + 14;
            context.fill(x + 3, saveY, x + 87, saveY + 14, hoveredSave ? new Color(180, 0, 0).getRGB() : new Color(140, 0, 0).getRGB());
            context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, "Save", x + 30, saveY + 3, -1);

            // Config list
            int listY = saveY + 16;
            List<String> configs = NewBloodClient.INSTANCE.getConfigManager().getConfigs();
            int totalContentHeight = configs.size() * 16;
            
            context.enableScissor(x, listY, x + 90, listY + MAX_HEIGHT);
            
            int cy = listY - scrollOffset;
            for (String config : configs) {
                if (cy + 14 > listY && cy < listY + MAX_HEIGHT) {
                    boolean hovered = mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= cy && mouseY <= cy + 14;
                    context.fill(x + 3, cy, x + 87, cy + 14, hovered ? new Color(45, 45, 45).getRGB() : new Color(35, 35, 35).getRGB());
                    context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, config, x + 6, cy + 3, -1);
                }
                cy += 16;
            }
            context.disableScissor();

            // Scrollbar for configs
            if (totalContentHeight > MAX_HEIGHT) {
                int barX = x + 87;
                int barY = listY;
                int barWidth = 2;
                int barHeight = MAX_HEIGHT;
                
                double scrollRatio = (double) MAX_HEIGHT / totalContentHeight;
                int thumbHeight = (int) (barHeight * scrollRatio);
                int thumbY = (int) (barY + (scrollOffset * ((double) (barHeight - thumbHeight) / (totalContentHeight - MAX_HEIGHT))));
                
                context.fill(barX, barY, barX + barWidth, barY + barHeight, new Color(40, 40, 40, 150).getRGB());
                context.fill(barX, thumbY, barX + barWidth, thumbY + thumbHeight, new Color(180, 0, 0, 255).getRGB());

                if (scrolling) {
                    double diff = totalContentHeight - MAX_HEIGHT;
                    scrollOffset = (int) ((mouseY - barY - thumbHeight / 2.0) * (diff / (barHeight - thumbHeight)));
                    if (scrollOffset < 0) scrollOffset = 0;
                    if (scrollOffset > diff) scrollOffset = (int) diff;
                }
            }
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 18) {
                if (button == 0) { // Left click to drag
                    dragging = true;
                    dragX = (int) (mouseX - x);
                    dragY = (int) (mouseY - y);
                    return true;
                }
            }

            int inputY = y + 20;
            if (button == 0 && mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= inputY && mouseY <= inputY + 14) {
                typing = !typing;
                return true;
            }
            if (typing && button == 0 && !(mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= inputY && mouseY <= inputY + 14)) {
                typing = false;
            }

            int saveY = inputY + 16;
            if (button == 0 && mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= saveY && mouseY <= saveY + 14) {
                if (!currentInput.isEmpty()) {
                    NewBloodClient.INSTANCE.getConfigManager().save(currentInput);
                    currentInput = "";
                    typing = false;
                    NewBloodClient.INSTANCE.mc.player.playSound(net.minecraft.sound.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.2f);
                }
                return true;
            }

            int listY = saveY + 16;
            List<String> configs = NewBloodClient.INSTANCE.getConfigManager().getConfigs();
            int totalContentHeight = configs.size() * 16;
            
            if (totalContentHeight > MAX_HEIGHT && mouseX >= x + 85 && mouseX <= x + 90 && mouseY >= listY && mouseY <= listY + MAX_HEIGHT) {
                scrolling = true;
                return true;
            }

            int cy = listY - scrollOffset;
            for (String config : configs) {
                if (button == 0 && mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= cy && mouseY <= cy + 14 && cy >= listY && cy <= listY + MAX_HEIGHT) {
                    NewBloodClient.INSTANCE.getConfigManager().load(config);
                    NewBloodClient.INSTANCE.mc.player.playSound(net.minecraft.sound.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.2f);
                    return true;
                }
                cy += 16;
            }

            return false;
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            scrolling = false;
            dragging = false;
            return false;
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (typing) {
                if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE) {
                    if (!currentInput.isEmpty()) currentInput = currentInput.substring(0, currentInput.length() - 1);
                    return true;
                }
                if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER) {
                    if (!currentInput.isEmpty()) {
                        NewBloodClient.INSTANCE.getConfigManager().save(currentInput);
                        currentInput = "";
                        typing = false;
                    }
                    return true;
                }
                return true;
            }
            return false;
        }

        public void charTyped(char chr) {
            if (typing && currentInput.length() < 15) {
                currentInput += chr;
            }
        }
    }

    private static class SettingButton {
        private final BooleanSetting setting;
        private int x;
        public int y;

        public SettingButton(BooleanSetting setting, int x, int y) {
            this.setting = setting;
            this.x = x;
            this.y = y;
        }

        public void render(DrawContext context, int mouseX, int mouseY) {
            boolean hovered = mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= y && mouseY <= y + 14;
            int color = setting.getValue() ? new Color(160, 0, 0, 255).getRGB() : new Color(35, 35, 35, 255).getRGB();
            if (hovered) color = new Color(180, 0, 0, 255).getRGB();

            context.fill(x + 3, y, x + 87, y + 14, color);
            String text = setting.getName() + ": " + (setting.getValue() ? "ON" : "OFF");
            context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, text, x + 6, y + 3, -1);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= y && mouseY <= y + 14) {
                setting.setValue(!setting.getValue());
                NewBloodClient.INSTANCE.mc.player.playSound(net.minecraft.sound.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.1f);
                if (NewBloodClient.INSTANCE.getConfigManager() != null) {
                    NewBloodClient.INSTANCE.getConfigManager().save();
                }
                return true;
            }
            return false;
        }
    }

    private static class CategoryPanel {
        private final Module.Category category;
        private int x, y;
        private int dragX, dragY;
        private boolean dragging = false;
        private final List<ModuleButton> buttons = new ArrayList<>();
        private boolean expanded = true;
        private int scrollOffset = 0;
        private static final int MAX_HEIGHT = 180;

        public CategoryPanel(Module.Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
        }

        private void updateButtons() {
            if (buttons.isEmpty() && NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.getModuleManager() != null) {
                for (Module m : NewBloodClient.INSTANCE.getModuleManager().getModulesByCategory(category)) {
                    buttons.add(new ModuleButton(m, x, 0));
                }
            }
        }

        private boolean scrolling = false;

        public void render(DrawContext context, int mouseX, int mouseY) {
            if (dragging) {
                x = mouseX - dragX;
                y = mouseY - dragY;
            }
            
            updateButtons();
            
            // Panel header
            context.fill(x, y, x + 90, y + 18, new Color(30, 30, 30, 255).getRGB());
            context.fill(x, y + 17, x + 90, y + 18, 0xFFFF0000); // Red line under header
            
            if (NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.mc != null) {
                context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, category.getName(), x + 5, y + 5, -1);
                
                // Indicator (plus/minus)
                String indicator = expanded ? "-" : "+";
                context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, indicator, x + 80, y + 5, -1);
            }

            // Buttons with scrolling support
            if (expanded) {
                int totalContentHeight = 0;
                for (ModuleButton button : buttons) {
                    totalContentHeight += button.getHeight();
                }

                int totalHeight = Math.min(totalContentHeight, MAX_HEIGHT);
                context.fill(x, y + 18, x + 90, y + 18 + totalHeight, new Color(20, 20, 20, 200).getRGB());

                context.enableScissor(x, y + 18, x + 90, y + 18 + totalHeight);
                int btnY = y + 18 - scrollOffset;
                for (ModuleButton button : buttons) {
                    button.x = x;
                    if (btnY + button.getHeight() > y + 18 && btnY < y + 18 + MAX_HEIGHT) {
                        button.y = btnY;
                        button.render(context, mouseX, mouseY);
                    }
                    btnY += button.getHeight();
                }
                context.disableScissor();

                // Scrollbar
                if (totalContentHeight > MAX_HEIGHT) {
                    int barX = x + 87;
                    int barY = y + 18;
                    int barWidth = 2;
                    int barHeight = MAX_HEIGHT;
                    
                    double scrollRatio = (double) MAX_HEIGHT / totalContentHeight;
                    int thumbHeight = (int) (barHeight * scrollRatio);
                    int thumbY = (int) (barY + (scrollOffset * ((double) (barHeight - thumbHeight) / (totalContentHeight - MAX_HEIGHT))));
                    
                    // Track
                    context.fill(barX, barY, barX + barWidth, barY + barHeight, new Color(40, 40, 40, 150).getRGB());
                    // Thumb
                    context.fill(barX, thumbY, barX + barWidth, thumbY + thumbHeight, new Color(180, 0, 0, 255).getRGB());

                    if (scrolling) {
                        double diff = totalContentHeight - MAX_HEIGHT;
                        scrollOffset = (int) ((mouseY - barY - thumbHeight / 2.0) * (diff / (barHeight - thumbHeight)));
                        if (scrollOffset < 0) scrollOffset = 0;
                        if (scrollOffset > diff) scrollOffset = (int) diff;
                    }
                }
            }
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Check if header clicked
            if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 18) {
                if (button == 0) { // Left click to drag
                    dragging = true;
                    dragX = (int) (mouseX - x);
                    dragY = (int) (mouseY - y);
                    return true;
                } else if (button == 1) { // Right click to expand
                    expanded = !expanded;
                    NewBloodClient.INSTANCE.mc.player.playSound(net.minecraft.sound.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
                    return true;
                }
            }

            if (expanded) {
                int listY = y + 18;
                int totalContentHeight = 0;
                for (ModuleButton mb : buttons) totalContentHeight += mb.getHeight();
                
                if (totalContentHeight > MAX_HEIGHT && mouseX >= x + 85 && mouseX <= x + 90 && mouseY >= listY && mouseY <= listY + MAX_HEIGHT) {
                    scrolling = true;
                    return true;
                }

                for (ModuleButton mb : buttons) {
                    if (mb.mouseClicked(mouseX, mouseY, button)) return true;
                }
            }
            return false;
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            scrolling = false;
            dragging = false;
            return false;
        }
    }

    private static class ModuleButton {
        private final Module module;
        private int x;
        public int y;
        private boolean binding = false;
        private boolean expanded = false;
        private final List<SettingButton> settingButtons = new ArrayList<>();

        public ModuleButton(Module module, int x, int y) {
            this.module = module;
            this.x = x;
            this.y = y;
            for (Setting<?> setting : module.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    settingButtons.add(new SettingButton((BooleanSetting) setting, x, 0));
                }
            }
        }

        public void render(DrawContext context, int mouseX, int mouseY) {
            boolean hovered = mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= y && mouseY <= y + 14;
            
            // Background color logic
            int color;
            if (module.isEnabled()) {
                color = hovered ? new Color(200, 0, 0, 255).getRGB() : new Color(160, 0, 0, 255).getRGB();
            } else {
                color = hovered ? new Color(40, 40, 40, 255).getRGB() : new Color(25, 25, 25, 255).getRGB();
            }

            context.fill(x + 3, y, x + 87, y + 14, color);
            
            if (NewBloodClient.INSTANCE != null && NewBloodClient.INSTANCE.mc != null) {
                String text = binding ? "[...]" : module.getName();
                if (!binding && module.getKey() != 0) {
                    String keyName = org.lwjgl.glfw.GLFW.glfwGetKeyName(module.getKey(), 0);
                    if (keyName == null) {
                        // For special keys like Shift, Ctrl, etc.
                        keyName = getSpecialKeyName(module.getKey());
                    }
                    text += " [" + keyName.toUpperCase() + "]";
                }
                
                int textColor = module.isEnabled() ? -1 : new Color(120, 120, 120).getRGB();
                context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, text, x + 6, y + 3, textColor);
                
                if (!settingButtons.isEmpty()) {
                    context.drawTextWithShadow(NewBloodClient.INSTANCE.mc.textRenderer, expanded ? "v" : ">", x + 80, y + 3, textColor);
                }
            }

            if (expanded) {
                int sy = y + 14;
                for (SettingButton sb : settingButtons) {
                    sb.x = x;
                    sb.y = sy;
                    sb.render(context, mouseX, mouseY);
                    sy += 14;
                }
            }
        }

        public int getHeight() {
            return 14 + (expanded ? settingButtons.size() * 14 : 0);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX >= x + 3 && mouseX <= x + 87 && mouseY >= y && mouseY <= y + 14) {
                if (button == 0) { // Left Click - Bind
                    binding = !binding;
                    return true;
                } else if (button == 1) { // Right Click - Toggle
                    module.toggle();
                    NewBloodClient.INSTANCE.mc.player.playSound(net.minecraft.sound.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
                    return true;
                } else if (button == 2) { // Middle Click - Expand Settings
                    expanded = !expanded;
                    return true;
                }
            }
            if (expanded) {
                for (SettingButton sb : settingButtons) {
                    if (sb.mouseClicked(mouseX, mouseY, button)) return true;
                }
            }
            return false;
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (binding) {
                if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE || keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE) {
                    module.setKey(0);
                } else {
                    module.setKey(keyCode);
                }
                binding = false;
                if (NewBloodClient.INSTANCE.getConfigManager() != null) {
                    NewBloodClient.INSTANCE.getConfigManager().save();
                }
                return true;
            }
            return false;
        }
    }
}
