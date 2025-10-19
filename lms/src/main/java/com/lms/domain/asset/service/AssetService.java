package com.lms.domain.asset.service;

import com.lms.domain.asset.Asset;
import com.lms.domain.asset.UploadStatus;
import com.lms.domain.asset.creation.CreateAsset;
import com.lms.domain.asset.rebuild.RebuildAsset;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <br>Asset 파일 비동기 업로드를 관리하는 서비스</br>
 * 
 * <br>내부의 큐와 워커 스레드 풀을 사용하여 파일 업로드를 비동기로 처리</br>
 * 
 * <br>업로드 실패시 자동 재시도, Asset의 상태 추적</br>
 * 
 * <br>AutoCloseable 구현으로 리소스 자동 정리</br>
 * 
 * 프로세스가 시작할때 
 * <pre>{@code
 *  // 1. Main.java 에서 try-with-resources 로 사용
 * try (AssetService assetService = new Assetservice()) {}
 *      
 *  // 2. 다른 서비스에서 AssetService 인스턴스 전달
 *  ContentService contentService = new ContentService(assetService);
 * 
 *  // 3. 로직 실행행
 *  List<AssetServiceCreate> createdList = ...;
 *  List<Asset> savedAsset = assetService.submitUploadFileList(createdList);
 * 
 *  // 4. AutoClose
 *      
 * }
 * }</pre>
 * 
 *  @see Asset
 *  @see UploadStatus
 */
public class AssetService implements AutoCloseable {
    
    /**
     * 업로드 대기 중인 Asset을 보관하는 블로킹 큐.
     * 워커 스레드들이 이 큐에서 작업을 가져와 처리합니다.
     */
    private final BlockingQueue<Asset> uploadQueue;
    
    /**
     * Asset 업로드를 처리하는 워커 스레드 풀.
     */
    private final ExecutorService workerExecutor;
    
    /**
     * 워커 스레드 개수 (기본값: 5).
     */
    private static final int threadSize=5;



    public AssetService() {
        this.uploadQueue = new LinkedBlockingQueue<>();
        this.workerExecutor = Executors.newFixedThreadPool(threadSize);

        createUploadWorkers();
    }

    // 실제 서비스라면 file도 같이 입력
    /**
     * 여러 파일의 업로드 요청 후 비동기 처리
     * 
     * @param assetcreateList
     * @return 생성된 Asset List
     * @see AssetServiceCreate
     */
    public List<Asset> submitUploadFileList(List<AssetServiceCreate> assetCreateList) throws IllegalArgumentException {
        List<CreateAsset> createAssetList = Optional.ofNullable(
            assetCreateList).orElse(List.of())
            .stream().map(AssetServiceCreate::toCreateAsset).toList();
        List<Asset> assetList = createAssetList.stream()
            .map(Asset::create)
            .toList();
        
        //TODO: repository에 asset들 상태 추가
        //DB먼저 저장 하고-> 큐에 넘겨야지 큐가 바로 실패를 하더라도 DB에 update가능

        for(Asset asset:assetList){
            assetAppendQueue(asset);
        }

        return assetList; // createAsset에서 uuid가 생성되기때문에 Asset전체 반환
    }

    /**
     * ContendId를 기준으로 연결된 모든 Asset의 상태를 조회
     * 
     * @param contentId를 조회할 Content 의 Id
     * @return Content에 연결된 모든 Asset 리스트
     */
    public List<Asset> getAssetStatus(Integer contentId) throws IllegalArgumentException {
        Optional.ofNullable(contentId).orElseThrow(() -> 
            new IllegalArgumentException("조회하고자 하는 컨텐츠 값이 비어있습니다.")
        );

        // TODO: Repository에서 조회, List AssetServiceRebuild DTO find by contendId
        // Repository의 return 은 notnull
        List<AssetServiceRebuild> assetDTOList = null;

        List<RebuildAsset> rebuildList = assetDTOList.stream()
            .map(AssetServiceRebuild::toRebuildAsset).toList();

        List<Asset> assetList = rebuildList.stream()
            .map(Asset::rebuild).toList();
        
        return assetList;
    }

    // 실제 서비스라면 file도 같이 입력필요
    /**
     * 최종적으로 Upload에 실패한 Asset을 재업로드 수행
     * 
     * @param contentId
     * @throw IllegalStateException Asset이 재시도 횟수 초과로 불가능한 경우
     */
    public void reUpload(Integer contentId) {
        // TODO: Repository에서 해당 contentId 를 가진 Asset 조회
        // assetRepository.findByContentID(contentId)
        List<AssetServiceRebuild> assetDTOList = null;

        List<RebuildAsset> rebuildList = Optional.ofNullable(
            assetDTOList).orElse(List.of())
            .stream().filter(dto -> dto.status() == UploadStatus.FAILED)
            .map(AssetServiceRebuild::toRebuildAsset).toList();

        List<Asset> assetList = rebuildList.stream()
            .map(Asset::rebuild).toList();


        for (Asset asset:assetList) {
            if (asset.canRetry()) {
                asset.incrementRetryCount();
                asset.statusUpdateRetry();
                assetAppendQueue(asset);
            } else {
                throw new IllegalStateException("재시도 불가능한 상태입니다.");
            }
        }
    }



    /**
     * Asset을 큐에 추가
     * 
     * @param asset 큐에 추가할 asset
     */
    private void assetAppendQueue(Asset asset) {
        try {
            uploadQueue.put(asset);
            asset.statusUpdateUploading(); // DB에서 pending과 uploading은 같은 accepted라 변경 필요 없음
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            asset.statusUpdateFailed(); 
        }
    }


    // 아래가 worker
    /**
     * 설정된 갯수의 워커스레드 생성
     * 
     * <p>각 워커는 큐를 모니터링하며 Asset을 꺼내 업로드 처리 수행</p>
     */
    private void createUploadWorkers() {
        IntStream.range(0,threadSize)
            .forEach(eachThread -> this.workerExecutor.submit(new Worker()));
    }
    
    /**
     * 워커 스레드 메인루프
     * 큐에서 Asset을 꺼내 {@link #processAsset(Asset)} 을 호출하여 처리
     * 인터럽트 발생 시, 루프 종료하고 스레드 정리
     * 
     * <br>스레드는 명시적으로 존재하지만, cpu 리소스는 점유하지않음 </br>
     */
    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Asset asset = uploadQueue.take();
                    processAsset(asset);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        /**
         * Asset 업로드 처리
         * 
         * 실제 케이스에서는 parsing request 를 처리
         * 
         * 1. 업로드
         * 2-1. 성공시 상태 success로 변경 후 DB 업데이트
         * 2-2. 실패 시 상태 FAILED로 변경 
         * 2-2-1. 재시도 가능하면 RETRY로 변경 후 큐에 재추가
         * 2-2-2. 재시도 불가능하면 FAILED로 상태 변경 후 DB 업데이트트
         * 
         * @param asset
         */
        private void processAsset(Asset asset) {
            try {
                Thread.sleep(3000); // 실제로는 다른 storage에 전송하는 작업을 작성

                asset.statusUpdateSuccess();

                // TODO: Repository update
                // assetRepository.update(asset);

            } catch (Exception e) { 
                asset.statusUpdateFailed();
                System.err.println("업로드 실패");
                if (asset.canRetry()) {
                    asset.incrementRetryCount();
                    asset.statusUpdateRetry();
                    assetAppendQueue(asset);
                } else {
                    System.out.println("파일 업로드 실패 : " + asset.getOriginalFilename());
                    // TODO: Repository 에 FAILED상태로 update
                    // assetRepository.update(asset);
                }
            }
        }
    }

    /**
     * AssetService를 종료하고 모든 리소스를 정리합니다.
     * 
     * <p>이 메서드는 다음 순서로 종료 절차를 수행합니다:</p>
     * <ol>
     *   <li>워커 스레드 풀에 종료 신호 전송 ({@code shutdown()})</li>
     *   <li>최대 5초 동안 진행 중인 작업 완료 대기</li>
     *   <li>5초 내 종료되지 않으면 강제 종료 시도</li>
     *   <li>인터럽트 발생 시 즉시 종료</li>
     * </ol>
     * 
     * <p><strong>주의:</strong> 큐에 남아있는 작업은 처리되지 않고 버려집니다.
     * 모든 작업이 완료될 때까지 대기하려면 종료 전에 큐가 비어있는지 확인하세요.</p>
     */
    @Override
    public void close() {
        workerExecutor.shutdown();
        try { // queue가 비어있는지, worker가 일을 계속 하고있는지를 확인 하고 종료하고싶은데?
            if (!workerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                workerExecutor.shutdown();
            }
        } catch (InterruptedException e) {
            workerExecutor.shutdown();
            Thread.currentThread().interrupt();
        }
        System.out.println("AssetService 종료");
    }
}
