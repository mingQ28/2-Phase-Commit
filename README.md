# 2PC (Spring Boot + Atomikos)

이 프로젝트는 Spring Boot 환경에서 **두 개의 MySQL 데이터베이스**를 대상으로 Atomikos JTA를 활용한 **2PC(2-Phase Commit)** 트랜잭션을 구현·테스트하는 예제입니다.

## 📌 개요
- 두 개의 DB(FirstDB, SecondDB)에 대해 하나의 글로벌 트랜잭션으로 출금·입금 작업을 처리
- 오류 발생 시 전체 작업을 롤백하여 데이터 정합성을 보장
- Atomikos를 통한 JTA 트랜잭션 매니저 설정

## 🗂️ 사전 설정
- `resources` 폴더 내 `schema.sql`과 `data.sql`을 개인 MySQL에 적용하여 초기 스키마와 데이터를 생성

## 📝 참고
- **Atomikos JTA**: 분산 환경에서 여러 데이터 소스에 걸친 트랜잭션을 관리하는 구현체
- **2PC(2-Phase Commit)**: 각 참여자(데이터 소스)에 Prepare 요청 후 Commit/Abort를 수행하여 데이터 일관성을 확보하는 프로토콜
