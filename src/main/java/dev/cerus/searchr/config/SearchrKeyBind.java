package dev.cerus.searchr.config;

import dev.cerus.searchr.interfaces.KeyData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Custom key bind class
 */
public class SearchrKeyBind extends KeyBinding {

    private boolean requiresCtrl;

    public SearchrKeyBind(final String translationKey, final int code) {
        super("key.searchr." + translationKey, code,  Category.create(Identifier.of("searchr", "searchr")));
    }

    public SearchrKeyBind requiresCtrl(final boolean b) {
        this.requiresCtrl = b;
        return this;
    }

    @Override
    public boolean isPressed() {
        if (!this.requiresCtrl) {
            return super.isPressed();
        }
        return super.isPressed() && hasControlDown();
    }

    /**
     * Check if this key bind was pressed.
     * Must be called from keyPressed() method of screens / widgets.
     *
     * @param keyInput The key input
     *
     * @return if the pressed keycode matches this key
     */
    public boolean wasPressed(final KeyInput keyInput) {
        final boolean pressed = ((KeyData) this).getBoundKey().getCode() == keyInput.key();
        return pressed && (!this.requiresCtrl || keyInput.hasCtrl());
    }

    @Override
    public boolean wasPressed() {
        if (!this.requiresCtrl) {
            return super.wasPressed();
        }
        return super.wasPressed() && hasControlDown();
    }

    private static boolean hasControlDown() {
        return InputUtil.isKeyPressed(
                MinecraftClient.getInstance().getWindow(),
                GLFW.GLFW_KEY_LEFT_CONTROL
        ) || InputUtil.isKeyPressed(
                MinecraftClient.getInstance().getWindow(),
                GLFW.GLFW_KEY_RIGHT_CONTROL
        );
    }
}
