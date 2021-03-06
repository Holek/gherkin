package gherkin.lexer;

import gherkin.I18n;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18nLexer implements Lexer {
    private static final Pattern COMMENT_OR_EMPTY_LINE_PATTERN = Pattern.compile("^\\s*#|^\\s*$");
    private static final Pattern LANGUAGE_PATTERN = Pattern.compile("^\\s*#\\s*language\\s*:\\s*([a-zA-Z\\-]+)");
    private final Listener listener;
    private I18n i18n;

    public I18nLexer(Listener listener) {
        this(listener, false);
    }

    public I18nLexer(Listener listener, boolean forceRubyDummy) {
        this.listener = listener;
    }

    /**
     * @return the i18n language from the previous scanned source.
     */
    public I18n getI18nLanguage() {
        return i18n;
    }

    public void scan(String source) {
        createDelegate(source).scan(source);
    }

    private Lexer createDelegate(String source) {
        i18n = i18nLanguage(source);
        return i18n.lexer(listener);
    }

    private I18n i18nLanguage(String source) {
        String key = "en";
        for (String line : source.split("\\n")) {
            if (!COMMENT_OR_EMPTY_LINE_PATTERN.matcher(line).find()) {
                break;
            }
            Matcher matcher = LANGUAGE_PATTERN.matcher(line);
            if (matcher.find()) {
                key = matcher.group(1);
                break;
            }
        }
        return new I18n(key);
    }
}