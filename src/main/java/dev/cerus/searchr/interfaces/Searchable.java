package dev.cerus.searchr.interfaces;

import org.jetbrains.annotations.Nullable;

/**
 * Represents components that implement search functionality
 */
public interface Searchable {

    /**
     * Set a suggestion (search result)
     *
     * @param suggestion The suggestion
     */
    void setSuggestion(@Nullable String suggestion);

    /**
     * Enable search
     */
    void enableSearch();

    /**
     * Disable search
     */
    void disableSearch();

    boolean isSearchEnabled();

}
