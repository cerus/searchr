package dev.cerus.searchr.config;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;

/**
 * Key bind utility
 */
public final class KeyBinds {

    public static final SearchrKeyBind ACTIVATE = new SearchrKeyBind("activate", GLFW.GLFW_KEY_R)
            .requiresCtrl(true);

    private KeyBinds() {
    }

    public static void update(final ModConfig config) {
        ACTIVATE.requiresCtrl(config.keys.activationRequiresCtrl);
    }

    public static void register() {
        KeyBindingHelper.registerKeyBinding(ACTIVATE);
    }

}
