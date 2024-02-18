package dev.cerus.searchr.config;

import dev.cerus.searchr.interfaces.KeyData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;

/**
 * Custom key bind class
 */
public class SearchrKeyBind extends KeyBinding {

    private boolean requiresCtrl;

    public SearchrKeyBind(final String translationKey, final int code) {
        super("key.searchr." + translationKey, code, "category.searchr");
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
        return super.isPressed() && Screen.hasControlDown();
    }

    /**
     * Check if this key bind was pressed.
     * Must be called from keyPressed() method of screens / widgets.
     *
     * @param keyCode The pressed keycode
     *
     * @return if the pressed keycode matches this key
     */
    public boolean wasPressed(final int keyCode) {
        final boolean pressed = ((KeyData) this).getBoundKey().getCode() == keyCode;
        return pressed && (!this.requiresCtrl || Screen.hasControlDown());
    }

    @Override
    public boolean wasPressed() {
        if (!this.requiresCtrl) {
            return super.wasPressed();
        }
        return super.wasPressed() && Screen.hasControlDown();
    }

}
