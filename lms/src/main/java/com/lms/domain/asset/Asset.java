package com.lms.domain.asset;

import java.util.UUID;

import com.lms.domain.asset.creation.CreateAsset;
import com.lms.domain.asset.rebuild.RebuildAsset;

import lombok.Getter;


/**
 * Content에 첨부되는 파일(Asset)의 도메인 객체.
 * 
 * <p>Asset은 비동기로 외부 스토리지에 업로드되며, 업로드 상태를 추적합니다.
 * Content 하나에 여러 Asset이 연결 가능</p>
 * 
 * <h3>주요 특징:</h3>
 * <ul>
 *   <li>원본 파일명을 UUID로 변환하여 저장</li>
 *   <li>업로드 상태 추적 (PENDING → UPLOADING → SUCCESS/FAILED)</li>
 *   <li>자동 재시도 메커니즘 (최대 3회)</li>
 *   <li>Content에 종속적 (contentId 필수)</li>
 * </ul>
 * 
 * <h3>생성 패턴:</h3>
 * <ul>
 *   <li>{@link #create(CreateAsset)} - 새로운 Asset 생성 (업로드 요청 시)</li>
 *   <li>{@link #rebuild(RebuildAsset)} - DB에서 조회한 Asset 재구성</li>
 * </ul>
 * 
 * @see UploadStatus
 * @see AssetService
 */
@Getter
public class Asset {
    private Long id;
    private final String mimeType;
    private final String path;
    private final String originalFilename;
    private final String convertedFilename;
    private final Long contentId;
    private UploadStatus status; // 비동기 처리 상태 추적
    private int retryCount = 0;

    private static final int MAX_RETRY_COUNT = 5;

    private Asset(
        Long id, String mimeType, String path,
        String originalFilename, String convertedFilename, 
        Long contentId, UploadStatus status
    ) {
        
        this.id = id;
        this.mimeType = mimeType;
        this.path = path;
        this.originalFilename = originalFilename;
        this.convertedFilename = convertedFilename;
        this.contentId = contentId;
        this.status = status;
    }
    
    /**
     * 새로운 Asset을 생성합니다. (상태: PENDING, UUID 자동 생성)
     * @param createAsset Asset 생성 정보
     * @return 생성된 Asset 객체
     */
    public static Asset create(CreateAsset createAsset) {
        String originalFilename = createAsset.originalFilename();
        UUID uuid = UUID.randomUUID();
        String convertedFilename = uuid.toString();
        
        return new Asset(
            null,
            createAsset.mimeType(),
            createAsset.path(),
            originalFilename,
            convertedFilename,
            createAsset.contentId(),
            UploadStatus.PENDING
        );
    }

    /**
     * DB에서 조회한 데이터로 Asset을 재구성합니다.
     * @param rebuildAsset 재구성 정보
     * @return 재구성된 Asset 객체
     */
    public static Asset rebuild(RebuildAsset rebuildAsset) {
        return new Asset(
            rebuildAsset.id(),
            rebuildAsset.mimeType(),
            rebuildAsset.path(),
            rebuildAsset.originalFilename(),
            rebuildAsset.convertedFilename(),
            rebuildAsset.contentId(),
            rebuildAsset.status()
        );
    }

    /**
     * Asset의 업로드 재시도 여부 조회
     * @return 가능하면 true, 불가능은 false
     */
    public boolean canRetry() {
        return this.retryCount < MAX_RETRY_COUNT && this.status == UploadStatus.FAILED;
    }

    public void incrementRetryCount() {
        if (!canRetry()) {
            throw new IllegalStateException(
                "Cannot increment retry count. Current: "+ this.retryCount
                + ", Max: " + MAX_RETRY_COUNT 
                + ", Status: " + this.status
            );
        }
        this.retryCount++;
    }
    
    /**
     * 상태를 UPLOADING으로 변경합니다.
     * @throws IllegalStateException 현재 상태가 PENDING 또는 RETRY가 아닌 경우
     */
    public void statusUpdateUploading() {
        if (this.status != UploadStatus.PENDING && this.status != UploadStatus.RETRY) {
            throw new IllegalStateException("Cannot start uploading from status: " + this.status);
        }
        this.status = UploadStatus.UPLOADING;
    }
    
    /**
     * 상태를 SUCCESS로 변경합니다.
     * @throws IllegalStateException 현재 상태가 UPLOADING이 아닌 경우
     */
    public void statusUpdateSuccess() {
        if (this.status != UploadStatus.UPLOADING) {
            throw new IllegalStateException("Cannot mark as uploaded from status: " + this.status);
        }
        this.status = UploadStatus.SUCCESS;
    }

    /**
     * 상태를 FAILED로 변경합니다.
     * @throws IllegalStateException 현재 상태가 UPLOADING이 아닌 경우
     */
    public void statusUpdateFailed() {
        if (this.status != UploadStatus.UPLOADING) {
            throw new IllegalStateException("Cannot mark as failed from status: " + this.status);
        }
        this.status = UploadStatus.FAILED;
    }

    /**
     * 상태를 RETRY로 변경합니다.
     * @throws IllegalStateException 현재 상태가 FAILED가 아닌 경우
     */
    public void statusUpdateRetry() {
        if (this.status != UploadStatus.FAILED) {
            throw new IllegalStateException("Cannot retry from status: " + this.status);
        }
        this.status = UploadStatus.RETRY;
    }

}
