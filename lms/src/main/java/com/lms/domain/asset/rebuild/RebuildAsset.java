package com.lms.domain.asset.rebuild;

import com.lms.domain.asset.UploadStatus;

public record RebuildAsset(
    Integer id,
    String mimeType,
    String path,
    String originalFilename,
    String convertedFilename,
    Integer contentId,
    UploadStatus status
) {}
