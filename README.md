#Mini Project

## 실행방법
> #### mvn clean
> #### java -jar ./target/MiniProject-1.0-SNAPSHOT.jar

## 기능 설명
> ### Swagger API확인 가능 : http://localhost:8080/swagger-ui.html 
> ### 1. 주문 시스템 (order-controller)
> #### 1) 주문 등록 API - 프로젝트 과제 검토자의 주문생성 프로그램에서 호출 
> ##### API Request : PUT /order
> #### 2) 주문 업데이트 API - 라이더시스템이 호출
> ##### PUT /deliveryOrder
> #### 3) 주문 조회 API
> ##### GET /order
> ### 2. 배달 시스템 (delivery-controller)
> #### 1) 배달 등록 API - 주문시스템이 호출
> ##### PUT /delivery
> #### 2) 배달 조회 API
> ##### GET /delivery

## 설계 내용
> ### DB Tables
> #### order(주문 정보 관리), menus(메뉴 관리), delivery(배달 정보 관리), riders(라이더 관리)
> #### DDL : resources/db.migration/V1_init.sql 

## 동작
> ## APIs
> ### 1. 주문 등록
> * #### Request Order 의 유효성을 판단
> * #### order 내 Munu 수량 확인 후 menus table내 quantity 감소
> * #### order에 order 정보 등록
> * #### delivery 서비스 deliveryOrder를 호출하여 delivery 등록
> ### 2. 주문 조회
> * #### orders 테이블에 oroderNo로 아이템 존재하는 지 확인 후 응답
> ### 3. 주문 상태 변경
> * #### orders 의 oroderNo로 get. request status로 변경 후 save
> ### 4. 배달 등록
> * #### 요청 받은 order 정보로 devivery table에 delivery 정보 등록
> ### 5. 배달 조회 API 
> * #### devivery table에 delivery 정보 응답
> ## Batch
> ### 1. 트래킹 라이더 배치
> * #### 10초에 한번씩 라이더들의 현재 상태 확인
> * #### 배달이 완료된 라이더의 devlivery, order 정보를 변경하고, 자신의 상태도 변경 
> ### 2. 주문 배정 배치
> * #### 상태가 RECEIPT 상태인 delivery만 index 에서 순회한다
> * #### 일이 없는 라이더에게 해당 deliveryId를 부여하여 배송을 시작한다