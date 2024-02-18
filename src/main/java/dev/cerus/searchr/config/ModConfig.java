package dev.cerus.searchr.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

/**
 * Searchr config
 */
@Config(name = "searchr")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public Colors colors = new Colors();
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public Keys keys = new Keys();
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public History history = new History();

    public static class Colors {

        @ConfigEntry.ColorPicker
        @ConfigEntry.Gui.Tooltip
        public int colorPlaceholder = net.minecraft.util.Colors.GRAY & 0xFFFFFF;
        @ConfigEntry.ColorPicker
        @ConfigEntry.Gui.Tooltip
        public int colorNotFoundPrefix = net.minecraft.util.Colors.LIGHT_GRAY & 0xFFFFFF;
        @ConfigEntry.ColorPicker
        @ConfigEntry.Gui.Tooltip
        public int colorNotFoundSuffix = net.minecraft.util.Colors.RED & 0xFFFFFF;
        @ConfigEntry.ColorPicker
        @ConfigEntry.Gui.Tooltip
        public int colorSearch = 0xEDDA5C;
        @ConfigEntry.ColorPicker
        @ConfigEntry.Gui.Tooltip
        public int colorSearchHighlight = 0xE8DE9B;

    }

    public static class Keys {

        @ConfigEntry.Gui.Tooltip
        public boolean activationRequiresCtrl = true;
        @ConfigEntry.Gui.Tooltip
        public boolean allowCtrlC = true;

    }

    public static class History {

        @ConfigEntry.Gui.Tooltip
        public boolean extendMaxHistory = true;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 50, max = 10_000) public int maxHistory = 250;

    }

}
