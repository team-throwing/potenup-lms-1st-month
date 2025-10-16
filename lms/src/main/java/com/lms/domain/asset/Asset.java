package com.lms.domain.asset;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.lms.domain.asset.creation.CreateAsset;
import com.lms.domain.asset.rebuild.RebuildAsset;

import lombok.Getter;


// Asset is asynchronous upload to other place.
// but our case is async upload to our local file system.

@Getter
public class Asset {
    private Integer id;
    private final String mimeType;
    private final String path;
    private final String originalFilename;
    private final String convertedFilename;
    private final Integer contentId;
    private UploadStatus status; // 비동기 처리 상태 추적적

    private Asset(
        Integer id, String mimeType, String path,
        String originalFilename, String convertedFilename, 
        Integer contentId, UploadStatus status
    ) throws IllegalArgumentException {
        
        this.id = id;
        this.mimeType = mimeType;
        this.path = path;
        this.originalFilename = originalFilename;
        this.convertedFilename = convertedFilename;
        this.contentId = contentId;
        this.status = status;
    }
    
    // Asset 생성 요청시, id 존재 
    public static Asset create(CreateAsset createAsset) {
        String originalFilename = createAsset.originalFilename();
        UUID uuid = UUID.nameUUIDFromBytes(originalFilename.getBytes(StandardCharsets.UTF_8));
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

    // message Queue에 있을 때, 작업이 상황에 따라 해당 상태를 변경
    // 조회로 상태 점검
    
    public void statusUpdateUploading() {
        if (this.status != UploadStatus.PENDING && this.status != UploadStatus.RETRY) {
            throw new IllegalStateException("Cannot start uploading from status: " + this.status);
        }
        this.status = UploadStatus.UPLOADING;
    }
    
    public void statusUpdateUploaded() {
        if (this.status != UploadStatus.UPLOADING) {
            throw new IllegalStateException("Cannot mark as uploaded from status: " + this.status);
        }
        this.status = UploadStatus.UPLOADED;
    }

    public void statusUpdateFailed() {
        if (this.status != UploadStatus.UPLOADING) {
            throw new IllegalStateException("Cannot mark as failed from status: " + this.status);
        }
        this.status = UploadStatus.FAILED;
    }

    public void statusUpdateRetry() {
        if (this.status != UploadStatus.FAILED) {
            throw new IllegalStateException("Cannot retry from status: " + this.status);
        }
        this.status = UploadStatus.RETRY;
    }

}
