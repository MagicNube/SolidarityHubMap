package org.pinguweb.frontend.services;

import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

@Component
public class TranslationProvider implements I18NProvider {
    public static final String BUNDLE_PREFIX = "i18n/messages";
    private final List<Locale> locales = List.of(new Locale("es"), new Locale("en"), new Locale("va"));

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            return "";
        }
        locale = locale == null ? locales.get(0) : locale;
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);
        String value = bundle.containsKey(key) ? bundle.getString(key) : key;
        return MessageFormat.format(value, params);
    }
}
