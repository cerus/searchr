package dev.cerus.searchr.mixin;

import dev.cerus.searchr.Searchr;
import dev.cerus.searchr.config.ModConfig;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Modify chat history
 */
@Mixin(ChatHud.class)
public class ChatHudMixin {

    @ModifyConstant(method = "addToMessageHistory(Ljava/lang/String;)V", constant = @Constant(intValue = 100))
    public int maxMessages(final int original) {
        final ModConfig config = Searchr.getConfig();
        return config == null || !config.history.extendMaxHistory
                ? original : Math.max(original, config.history.maxHistory);
    }

}
