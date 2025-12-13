package net.okocraft.suffix.core.api.config;

import java.util.Collections;
import java.util.List;

public final class SuffixConfig {

    public static final String YAML_FILENAME = "config.yml";

    public static final String SUFFIX_MAX_LENGTH_KEY = "suffix-length";
    public static final String SUFFIX_PRIORITY_KEY = "suffix-priority";
    public static final String BLACKLIST_PATTERN_KEY = "blacklist-pattern";

    public int suffixMaxLength = 0;
    public int suffixPriority = 0;
    public List<String> blacklistPatterns = Collections.emptyList();

}
