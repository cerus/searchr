package dev.cerus.searchr.mixin;

import dev.cerus.searchr.Searchr;
import dev.cerus.searchr.config.ModConfig;
import net.minecraft.client.util.CommandHistoryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Modify command history
 */
@Mixin(CommandHistoryManager.class)
public class CommandHistoryManagerMixin {

    @ModifyConstant(method = "add(Ljava/lang/String;)V", constant = @Constant(intValue = 50))
    public int maxCommands(final int original) {
        final ModConfig config = Searchr.getConfig();
        return config == null || !config.history.extendMaxHistory
                ? original : Math.max(original, config.history.maxHistory);
    }

}
