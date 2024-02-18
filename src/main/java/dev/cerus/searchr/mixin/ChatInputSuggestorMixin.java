package dev.cerus.searchr.mixin;

import dev.cerus.searchr.interfaces.Searchable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Disable suggestions while searching
 */
@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin implements Searchable {

    private boolean searchEnabled;

    @Inject(method = "refresh()V", at = @At("HEAD"), cancellable = true)
    public void injectRefresh(final CallbackInfo ci) {
        if (this.isSearchEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;II)V", at = @At("HEAD"), cancellable = true)
    public void injectRender(final DrawContext context, final int mouseX, final int mouseY, final CallbackInfo ci) {
        if (this.isSearchEnabled()) {
            ci.cancel();
        }
    }

    @Override
    public void setSuggestion(final String suggestion) {
    }

    @Override
    public void enableSearch() {
        this.searchEnabled = true;
    }

    @Override
    public void disableSearch() {
        this.searchEnabled = false;
    }

    @Override
    public boolean isSearchEnabled() {
        return this.searchEnabled;
    }

}
