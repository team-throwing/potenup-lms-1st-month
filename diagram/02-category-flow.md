# 카테고리 관리 흐름

카테고리의 CRUD(생성, 조회, 수정, 삭제) 작업의 상세 흐름을 보여줍니다.

## 카테고리 관리 플로우차트

```mermaid
flowchart TD
    Start([카테고리 요청]) --> CheckOperation{작업 유형}
    
    CheckOperation -->|생성| CreateCategory[createCategory]
    CheckOperation -->|조회 전체| FindAll[findAll]
    CheckOperation -->|조회 단건| FindById[findById]
    CheckOperation -->|수정| Update[update]
    CheckOperation -->|삭제| Delete[delete]

    CreateCategory --> ValidateDto{DTO 검증}
    ValidateDto -->|실패| ThrowError1[IllegalArgumentException]
    ValidateDto -->|성공| GetConnection1[DataSource에서<br/>Connection 획득]

    GetConnection1 --> SetHolder1[ConnectionHolder에<br/>Connection 저장]
    SetHolder1 --> SetAutoCommit1[AutoCommit 설정 판단]
    SetAutoCommit1 --> ToEntity1[DTO → Entity 변환]
    ToEntity1 --> CallRepo1[Repository.create 호출]
    
    CallRepo1 --> Success1{성공?}
    Success1 -->|성공| Commit1[Commit]
    Success1 -->|실패| Rollback1[Rollback]
    
    Rollback1 --> ThrowError2[RuntimeException]
    Commit1 --> CleanUp1[ConnectionHolder.clear]
    ThrowError2 --> CleanUp1
    CleanUp1 --> CloseConnection1[Connection.close]
    CloseConnection1 --> End1([종료])

    FindAll --> GetConnection2[Connection 획득]
    GetConnection2 --> SetHolder2[ConnectionHolder 설정 판단]
    SetHolder2 --> CallRepoFindAll[Repository.findAll 호출]
    CallRepoFindAll --> ReturnList[List 반환]
    ReturnList --> CleanUp2[정리 및 종료]

    FindById --> GetConnection3[Connection 획득]
    GetConnection3 --> SetHolder3[ConnectionHolder 설정]
    SetHolder3 --> CallRepoFindById[Repository.findById 호출]
    CallRepoFindById --> ReturnCategory[Category 반환]
    ReturnCategory --> CleanUp3[정리 및 종료]

    Update --> GetConnection4[Connection 획득]
    GetConnection4 --> SetAutoCommit4[AutoCommit 설정 판단]
    SetAutoCommit4 --> ToEntity4[DTO → Entity]
    ToEntity4 --> CallRepoUpdate[Repository.update 호출]
    CallRepoUpdate --> Commit4[Commit]
    Commit4 --> CleanUp4[정리 및 종료]

    Delete --> GetConnection5[Connection 획득]
    GetConnection5 --> SetAutoCommit5[AutoCommit 설정 판단]
    SetAutoCommit5 --> CallRepoDelete[Repository.delete 호출]
    CallRepoDelete --> Commit5[Commit]
    Commit5 --> CleanUp5[정리 및 종료]

    style CreateCategory fill:#bbdefb
    style FindAll fill:#c8e6c9
    style FindById fill:#c8e6c9
    style Update fill:#fff9c4
    style Delete fill:#ffcdd2
```

## 주요 작업 설명

### 1. 생성 (Create)
- DTO 유효성 검증
- Connection 획득 및 AutoCommit 비활성화
- DTO → Entity 변환
- Repository를 통해 DB 저장
- 트랜잭션 커밋 또는 롤백
- 리소스 정리

### 2. 조회 (Read)
- **전체 조회 (findAll)**: 모든 카테고리 목록 반환
- **단건 조회 (findById)**: ID로 특정 카테고리 조회
- 읽기 작업이므로 트랜잭션 커밋 불필요

### 3. 수정 (Update)
- Connection 획득
- DTO → Entity 변환
- Repository를 통해 업데이트
- 트랜잭션 커밋

### 4. 삭제 (Delete)
- Connection 획득
- Repository를 통해 삭제 실행
- 트랜잭션 커밋

## 에러 처리

- **IllegalArgumentException**: DTO 검증 실패 시
- **RuntimeException**: 데이터베이스 작업 실패 시
- 모든 실패 시나리오에서 Rollback 실행
- Finally 블록에서 Connection 정리 보장

[← 목차로 돌아가기](./README.md)

