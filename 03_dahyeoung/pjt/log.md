## Error
### 1.1 Repository 등 spring elements를 인식하지 못할 때
- bean 주입 테스트를 해본다.

#### package scan 관련 문제
- `Application` 에서 scan 할 패키지를 지정해주면 된다.

### 1.2 h2 repository table name
- group이라는 이름은 안되는 듯??? -> 예약어인지 확인이 필요하다.