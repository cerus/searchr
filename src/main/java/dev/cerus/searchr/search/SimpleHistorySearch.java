package dev.cerus.searchr.search;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.util.collection.ArrayListDeque;
import org.jetbrains.annotations.Nullable;

/**
 * Basic search implementation
 */
public class SimpleHistorySearch implements HistorySearch {

    private final MinecraftClient client;

    public SimpleHistorySearch(final MinecraftClient client) {
        this.client = client;
    }

    @Override
    public @Nullable String searchHistory(final String input, final int offset) {
        final ChatHud chatHud = this.client.inGameHud.getChatHud();
        if (chatHud == null || input == null || input.isBlank()) {
            return null;
        }

        final String inputLowerCase = input.toLowerCase();
        final ArrayListDeque<String> history = chatHud.getMessageHistory();
        final List<String> matching = history.stream()
                .filter(s -> s.toLowerCase().contains(inputLowerCase))
                .distinct()
                .toList();
        if (matching.isEmpty() || offset % matching.size() >= matching.size()) {
            return null;
        }
        return matching.get(offset % matching.size());
    }

    private <T> void distinct(final List<T> list) {
        final List<T> copy = List.copyOf(list);
        for (final T element : copy) {
            final int lastIdx = list.lastIndexOf(element);
            int idx;
            while ((idx = list.indexOf(element)) != lastIdx) {
                list.remove(idx);
            }
        }
    }

}
