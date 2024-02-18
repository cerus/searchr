package dev.cerus.searchr.mixin;

import dev.cerus.searchr.Searchr;
import dev.cerus.searchr.config.KeyBinds;
import dev.cerus.searchr.config.ModConfig;
import dev.cerus.searchr.interfaces.Configurable;
import dev.cerus.searchr.interfaces.Searchable;
import dev.cerus.searchr.search.HistorySearch;
import dev.cerus.searchr.search.SimpleHistorySearch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Implements search functionality
 */
@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen implements Searchable {

    @Shadow private TextFieldWidget chatField;
    @Shadow private ChatInputSuggestor chatInputSuggestor;

    private ModConfig config;
    private HistorySearch historySearch;
    private boolean searchEnabled;
    private String latestSuggestion;
    private int offset;

    protected ChatScreenMixin(final Text title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void injectInit(final CallbackInfo ci) {
        this.config = Searchr.getConfig();
        this.historySearch = new SimpleHistorySearch(MinecraftClient.getInstance()); // TODO: Make configurable
        ((Configurable) this.chatField).setConfig(this.config);
    }

    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    public void injectOnKeyPress(final int keyCode, final int scanCode, final int modifiers, final CallbackInfoReturnable<Boolean> ci) {
        if (KeyBinds.ACTIVATE.wasPressed(keyCode)) {
            if (!this.isSearchEnabled()) {
                this.enableSearch();
            } else {
                // Activation key has been pressed while already enabled, cycle to the next result
                this.offset += 1;
                this.updateSuggestion(this.chatField.getText());
            }
            ci.setReturnValue(true);
        } else if (this.shouldExit(keyCode) && this.isSearchEnabled()) {
            // Exit search without keeping any text
            this.disableSearch();
            this.chatField.setText("");
            ci.setReturnValue(true);
        } else if ((keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_ENTER) && this.isSearchEnabled()) {
            // Exist search and keep suggestion or entered search
            this.disableSearch();
            if (this.latestSuggestion != null) {
                this.chatField.setText(this.latestSuggestion);
            }
            this.chatField.setCursorToEnd(false);
            ci.setReturnValue(true);
        }
    }

    private boolean shouldExit(final int keyCode) {
        final boolean ctrlC = this.config.keys.allowCtrlC && keyCode == GLFW.GLFW_KEY_C && Screen.hasControlDown();
        final boolean normalExit = keyCode == GLFW.GLFW_KEY_ESCAPE;
        return normalExit || ctrlC;
    }

    @Inject(method = "removed()V", at = @At("HEAD"))
    public void injectOnRemove(final CallbackInfo ci) {
        this.disableSearch();
    }

    @Inject(method = "onChatFieldUpdate(Ljava/lang/String;)V", at = @At("HEAD"))
    private void injectOnChatFieldUpdate(final String chatText, final CallbackInfo ci) {
        this.offset = 0;
        this.updateSuggestion(chatText);
    }

    private void updateSuggestion(final String txt) {
        if (this.isSearchEnabled() && this.historySearch != null) {
            this.latestSuggestion = this.historySearch.searchHistory(txt, this.offset);
            this.setSuggestion(this.latestSuggestion);
        }
    }

    @Override
    public void setSuggestion(final String suggestion) {
        ((Searchable) this.chatField).setSuggestion(suggestion);
    }

    @Override
    public void enableSearch() {
        this.searchEnabled = true;
        this.chatField.setText("");
        this.offset = 0;
        ((Searchable) this.chatField).enableSearch();
        ((Searchable) this.chatInputSuggestor).enableSearch();
        this.latestSuggestion = null;
    }

    @Override
    public void disableSearch() {
        this.searchEnabled = false;
        ((Searchable) this.chatField).disableSearch();
        ((Searchable) this.chatInputSuggestor).disableSearch();
    }

    @Override
    public boolean isSearchEnabled() {
        return this.searchEnabled;
    }

}
