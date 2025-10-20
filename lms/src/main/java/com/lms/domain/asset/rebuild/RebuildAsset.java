package com.lms.domain.asset.rebuild;

import com.lms.domain.asset.UploadStatus;

public record RebuildAsset(
    Long id,
    String mimeType,
    String path,
    String originalFilename,
    String convertedFilename,
    Long contentId,
    UploadStatus status
) {}
