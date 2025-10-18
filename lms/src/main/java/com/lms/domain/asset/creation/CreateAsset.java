package com.lms.domain.asset.creation;

public record CreateAsset(
    String mimeType,
    String path,
    String originalFilename,
    Integer contentId
) {}
