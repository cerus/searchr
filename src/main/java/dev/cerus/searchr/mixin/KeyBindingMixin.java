package dev.cerus.searchr.mixin;

import dev.cerus.searchr.interfaces.KeyData;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Implements {@link KeyData}
 */
@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyData {

    @Shadow private InputUtil.Key boundKey;

    @Override
    public InputUtil.Key getBoundKey() {
        return this.boundKey;
    }

}
