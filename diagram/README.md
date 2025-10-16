# LMS System FlowChart

LMS 시스템의 전체 흐름과 구조를 시각화한 다이어그램 모음입니다.

## 📑 목차

1. [전체 시스템 아키텍처](./01-system-architecture.md) - 레이어별 구조와 컴포넌트 관계
2. [카테고리 관리 흐름](./02-category-flow.md) - 카테고리 CRUD 작업 프로세스
3. [강의(Course) 관리 흐름](./03-course-flow.md) - 강의 생성/수정/삭제 프로세스
4. [도메인 객체 관계도](./04-domain-relationships.md) - 도메인 엔티티 간 관계

## 🏗️ 시스템 개요

본 프로젝트는 학습 관리 시스템(LMS)으로, 다음과 같은 레이어 구조를 갖습니다:

- **클라이언트 레이어**: 사용자 인터페이스
- **서비스 레이어**: 비즈니스 로직 처리
- **리포지토리 레이어**: 데이터 접근 계층
- **도메인 레이어**: 핵심 도메인 객체
- **데이터 레이어**: 데이터베이스 연결 관리

## 🔧 기술 스택

- **Language**: Java
- **Database**: MySQL (lms)
- **Connection Pool**: HikariCP
- **Transaction Management**: JDBC Manual Transaction

