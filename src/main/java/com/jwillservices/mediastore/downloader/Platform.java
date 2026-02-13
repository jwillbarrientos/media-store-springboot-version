package com.jwillservices.mediastore.downloader;

// todo borrar
public enum Platform {
    YOU_TUBE, INSTAGRAM, TIKTOK, FACEBOOK, INVALID_PLATFORM;

    public static Platform fromUrl(String url) {
        if (url.contains("facebook.com") || url.contains("fb.watch")) {
            return FACEBOOK;
        } else if (url.contains("youtube.com") || url.contains("youtu.be")) {
            return YOU_TUBE;
        } else if (url.contains("instagram.com") || url.contains("instagr.am")) {
            return INSTAGRAM;
        } else if (url.contains("tiktok.com")) {
            return TIKTOK;
        }
        return INVALID_PLATFORM;
    }
}
