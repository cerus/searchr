package dev.cerus.searchr.search;

import org.jetbrains.annotations.Nullable;

/**
 * Base class for history search algorithms
 */
public interface HistorySearch {

    @Nullable String searchHistory(@Nullable String input, int offset);

}
