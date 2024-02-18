package dev.cerus.searchr;

import dev.cerus.searchr.config.KeyBinds;
import dev.cerus.searchr.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Searchr implements ModInitializer {

    public static final int MAX_COMMANDS = 250;
    public static final Logger LOGGER = LoggerFactory.getLogger("searchr");
    private static Searchr INSTANCE;

    private ModConfig config;

    public static Searchr getInstance() {
        return INSTANCE;
    }

    public static ModConfig getConfig() {
        return INSTANCE == null ? null : INSTANCE.config();
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;

        // Config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        this.config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        // Keybinds
        KeyBinds.update(this.config);
        AutoConfig.getConfigHolder(ModConfig.class).registerLoadListener((configHolder, modConfig) -> {
            KeyBinds.update(modConfig);
            return ActionResult.PASS;
        });
        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((configHolder, modConfig) -> {
            KeyBinds.update(modConfig);
            return ActionResult.PASS;
        });
        KeyBinds.register();
    }

    public ModConfig config() {
        return this.config;
    }

}