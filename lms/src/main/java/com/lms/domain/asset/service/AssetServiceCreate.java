package com.lms.domain.asset.service;

import com.lms.domain.asset.creation.CreateAsset;

/**
 * Service 에서 호출시 사용할 DTO객체?
 * 
 * {@link #toCreateAsset()} 의 호출을 통해 Service 내부에서 사용
 */
public record AssetServiceCreate(
    String mimeType,
    String path,
    String originalFilename,
    Long contentId
) {
    public CreateAsset toCreateAsset() {
        return new CreateAsset(
            this.mimeType,
            this.path,
            this.originalFilename,
            this.contentId
        );
    }
}
