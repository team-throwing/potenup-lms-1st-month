package com.lms.repository.asset;

import com.lms.domain.asset.Asset;
import com.lms.repository.exception.DatabaseException;

import java.util.List;

public interface AssetRepository {

    /**
     * @param asset 에셋
     * @return 생성된 컨텐츠 에셋
     * @throws IllegalArgumentException asset 이 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    Asset create(Asset asset);

    /**
     * @param assets 에셋
     * @return 생성된 에셋 목록
     * @throws IllegalArgumentException assets 가 null 또는 empty
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<Asset> createAll(List<Asset> assets);

    /**
     * @param contentId 컨텐츠 id
     * @return 컨텐츠 id 에 해당하는 에셋 목록
     * @throws IllegalArgumentException contentId 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<Asset> findAll(long contentId);

    /**
     * @param asset 에셋
     * @throws IllegalArgumentException asset 이 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void update(Asset asset);

    /**
     * @param id 에셋 id
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void delete(long id);
}
