package com.jwillservices.mediastore.downloader;

public enum VideoTag {
    ALL("all"),
    LESS_THAN_A_MINUTE("lte60"),
    MORE_THAN_A_MINUTE("bt60"),
    WITH_TAGS("with"),
    WITHOUT_TAGS("without"),
    CUSTOM("custom");

    public final String tagName;
    VideoTag(String tagName) {
        this.tagName = tagName;
    }

    public static VideoTag fromName(String tagName) {
        for (VideoTag defaultTag : VideoTag.values()) {
            if (defaultTag.tagName.equalsIgnoreCase(tagName)) {
                return defaultTag;
            }
        }
        return CUSTOM;
    }
}
