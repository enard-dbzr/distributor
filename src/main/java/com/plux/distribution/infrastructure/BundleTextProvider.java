package com.plux.distribution.infrastructure;

import com.plux.distribution.core.workflow.domain.frame.TextProvider;
import java.util.Locale;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NotNull;

public class BundleTextProvider implements TextProvider {

    private final ResourceBundle bundle;

    public BundleTextProvider(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    @Override
    public @NotNull String getString(@NotNull String key) {
        return bundle.getString(key);
    }
}
