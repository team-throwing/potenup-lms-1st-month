package com.lms.domain.asset;

/**
 * Asset 업로드 처리 상태를 나타내는 Enum.
 * 
 * <p>Asset의 업로드 생명주기를 5단계로 관리합니다.
 * 각 상태는 DB에 저장될 때 간소화된 3가지 값(ACCEPTED, SUCCESS, FAILED)으로 매핑됩니다.</p>
 * 
 * <h3>상태 전이 흐름:</h3>
 * <pre>
 * PENDING → UPLOADING → SUCCESS (정상 완료)
 *            ↓
 *          FAILED → RETRY → UPLOADING (재시도)
 *            ↓
 *          FAILED (최종 실패, 3회 초과)
 * </pre>
 * DB에 저장시 {@link #toDb()} 호출
 * 
 * @see Asset
 * @see AssetService
 */
public enum UploadStatus {
    /**
     * 업로드 대기 중 상태.
     * Asset이 생성되었으나 아직 큐에 추가되지 않은 상태.
     * DB 저장값: "ACCEPTED"
     */
    PENDING("ACCEPTED"), 
    
    /**
     * 업로드 진행 중 상태.
     * 워커 스레드가 큐에서 꺼내어 실제 업로드를 처리 중인 상태.
     * DB 저장값: "ACCEPTED"
     */
    UPLOADING("ACCEPTED"), 
    
    /**
     * 업로드 성공 상태.
     * 파일이 정상적으로 업로드되어 처리가 완료된 상태.
     * DB 저장값: "SUCCESS"
     */
    SUCCESS("SUCCESS"), 
    
    /**
     * 업로드 실패 상태.
     * 업로드 중 오류가 발생하여 실패한 상태.
     * 재시도 가능 여부는 {@link Asset#retryAble()}로 확인.
     * DB 저장값: "FAILED"
     */
    FAILED("FAILED"), 
    
    /**
     * 재시도 대기 상태.
     * 실패 후 재시도가 가능한 경우, 다시 큐에 추가되기 전의 상태.
     * DB 저장값: "ACCEPTED"
     */
    RETRY("ACCEPTED");

    private final String value;

    UploadStatus(String value) {
        this.value = value;
    }

    /**
     * DB 저장용 상태 값을 반환합니다.
     * 
     * <p>애플리케이션 내부에서는 5가지 상태로 관리하지만,
     * DB에는 3가지 상태(ACCEPTED, SUCCESS, FAILED)로 저장됩니다:</p>
     * <ul>
     *   <li>PENDING, UPLOADING, RETRY → "ACCEPTED"</li>
     *   <li>SUCCESS → "SUCCESS"</li>
     *   <li>FAILED → "FAILED"</li>
     * </ul>
     * 
     * @return DB 저장용 상태 문자열 ("ACCEPTED", "SUCCESS", "FAILED" 중 하나)
     */
    public String toDb() {
        return this.value;
    }
}
