package me.newblood.mixin;

import me.newblood.NewBloodClient;
import me.newblood.module.Module;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ChatMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo info) {
        if (NewBloodClient.INSTANCE == null || NewBloodClient.INSTANCE.getModuleManager() == null) return;
        if (message.startsWith(".")) {
            if (handleCommand(message.substring(1))) {
                info.cancel();
            }
        }
    }

    private boolean handleCommand(String message) {
        String[] args = message.split(" ");
        if (args.length > 0) {
            Module m = NewBloodClient.INSTANCE.getModuleManager().getModuleByName(args[0]);
            if (m != null) {
                m.toggle();
                NewBloodClient.INSTANCE.mc.player.sendMessage(Text.literal("§b[NewBlood] §f" + m.getName() + " is now " + (m.isEnabled() ? "§aENABLED" : "§cDISABLED")), false);
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                NewBloodClient.INSTANCE.mc.player.sendMessage(Text.literal("§b[NewBlood] §fAvailable modules:"), false);
                for (Module mod : NewBloodClient.INSTANCE.getModuleManager().getModules()) {
                    NewBloodClient.INSTANCE.mc.player.sendMessage(Text.literal("§7- §f" + mod.getName() + (mod.isEnabled() ? " §a(ON)" : " §c(OFF)")), false);
                }
                return true;
            }
        }
        return false;
    }
}
