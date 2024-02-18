package dev.cerus.searchr.mixin;

import dev.cerus.searchr.config.ModConfig;
import dev.cerus.searchr.interfaces.Configurable;
import dev.cerus.searchr.interfaces.Searchable;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin extends ClickableWidget implements Searchable, Configurable {

    // Used to add an alpha value of 255 to colors
    private static final int MASK = 0xFF000000;

    @Shadow private TextRenderer textRenderer;
    @Shadow private String text;

    private ModConfig config;
    private boolean searchEnabled;
    private String textLower;
    private String suggestion;
    private String suggestionLower;

    public TextFieldWidgetMixin(final int x, final int y, final int width, final int height, final Text message) {
        super(x, y, width, height, message);
    }

    @Shadow
    public abstract void setCursorToStart(boolean shiftKeyPressed);

    @Shadow
    public abstract void setCursorToEnd(boolean shiftKeyPressed);

    @Inject(
            method = "renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void injectOnRenderWidget(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo ci,
                                      final int $$5, final int $$6, final String $$7, final boolean $$8, final boolean $$9, final int $$10, final int $$11) {
        if (this.searchEnabled) {
            final int k = $$10; // See local k of TextFieldWidget#render()
            final int l = $$11; // See local l of TextFieldWidget#render()

            if (this.suggestion == null) {
                if (this.text.isEmpty()) {
                    // Show placeholder
                    context.drawTextWithShadow(this.textRenderer, Text.translatable("text.searchr.typeToSearch"), k, l, this.config.colors.colorPlaceholder | MASK);
                } else {
                    // Show not found
                    final Text infoText = Text.empty().append(Text.translatable("text.searchr.notFound")).append(" ");
                    context.drawTextWithShadow(this.textRenderer, infoText, k, l, this.config.colors.colorNotFoundPrefix | MASK);
                    context.drawTextWithShadow(this.textRenderer, this.text, k + 1 + this.textRenderer.getWidth(infoText), l, this.config.colors.colorNotFoundSuffix | MASK);
                }
            } else {
                // Draw search result
                context.drawTextWithShadow(this.textRenderer, this.suggestion, k, l, this.config.colors.colorSearch | MASK);

                if (this.text != null && !this.text.isBlank()) {
                    final int start = this.suggestionLower.indexOf(this.textLower);
                    if (start < 0) {
                        return;
                    }

                    // Draw highlighter
                    final int startOffset = this.textRenderer.getWidth(this.suggestion.substring(0, start)) + k;
                    final int lineWidth = this.textRenderer.getWidth(this.text);
                    context.drawHorizontalLine(startOffset, startOffset + lineWidth, l + this.textRenderer.fontHeight, this.config.colors.colorSearchHighlight | MASK);
                }
            }
        }
    }

    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    public void injectOnKeyPressed(final int keyCode, final int scanCode, final int modifiers, final CallbackInfoReturnable<Boolean> ci) {
        // Block a bunch of special keys while search is enabled
        if (this.isSearchEnabled() && (keyCode == GLFW.GLFW_KEY_LEFT
                                       || keyCode == GLFW.GLFW_KEY_RIGHT
                                       || keyCode == GLFW.GLFW_KEY_HOME
                                       || keyCode == GLFW.GLFW_KEY_END
                                       || Screen.isSelectAll(keyCode)
                                       || Screen.isPaste(keyCode))) {
            ci.setReturnValue(true);
        }
    }

    @Inject(method = "onChanged(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void injectOnChanged(final String text, final CallbackInfo ci) {
        this.textLower = text.toLowerCase();
    }

    @Redirect(
            method = "renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;isEmpty()Z")
    )
    private boolean proxyStringEmpty(final String obj) {
        if (this.isSearchEnabled()) {
            // Prevents the widget from rendering text while search is enabled
            return true;
        }
        return obj.isEmpty();
    }

    @Redirect(
            method = "renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;isFocused()Z", ordinal = 1)
    )
    private boolean proxyIsFocused(final TextFieldWidget obj) {
        // Prevents the cursor from rendering while search is enabled
        return !this.isSearchEnabled() && obj.isFocused();
    }

    @Override
    public void setSuggestion(final String suggestion) {
        this.suggestion = suggestion;
        this.suggestionLower = suggestion == null ? null : suggestion.toLowerCase();
    }

    @Override
    public void enableSearch() {
        this.searchEnabled = true;
        this.textLower = null;
        this.setCursorToEnd(false);
    }

    @Override
    public void disableSearch() {
        this.searchEnabled = false;
        this.setSuggestion(null);
        this.setCursorToStart(false);
    }

    @Override
    public boolean isSearchEnabled() {
        return this.searchEnabled;
    }

    @Override
    public void setConfig(final ModConfig config) {
        this.config = config;
    }

}
