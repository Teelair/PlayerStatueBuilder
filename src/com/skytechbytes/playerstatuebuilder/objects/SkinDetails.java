package com.skytechbytes.playerstatuebuilder.objects;

import java.util.Map;

public record SkinDetails(long timestamp, String profileId, String profileName, Map<String, Texture> textures) {}
