package com.lms.domain.asset.service;

import com.lms.domain.asset.UploadStatus;
import com.lms.domain.asset.rebuild.RebuildAsset;

/**
 * DB에서 호출시 사용할 DTO객체?
 * 
 * {@link #toRebuildAsset()} 의 사용을 통해 AssetService에서 사용
 */
public record AssetServiceRebuild(
    Long id,
    String mimeType,
    String path,
    String originalFilename,
    String convertedFilename,
    Long contentId,
    UploadStatus status
) {
    public RebuildAsset toRebuildAsset() {
        return new RebuildAsset(
            this.id,
            this.mimeType,
            this.path,
            this.originalFilename,
            this.convertedFilename,
            this.contentId,
            this.status
        );
    }
}
