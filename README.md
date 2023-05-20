# Capstone design

## 설명
- 회사에서 업무를 처리할 때 생긴 데이터를 원하는 형태의 데이터로 바꿔주는 업무보조 프로그램
- 파이썬 서버를 활용해 웹 프로그래밍과 상호작용 
- open ai를 활용해 데이터를 이미지로 변환 가능
- 2차 로그인, 웹 로그인시 활용될 otp code 확인 기능 등 보안에 중점


## 구현 목록 및 진행 상황 - 백엔드 서버가 있다는 가정하 진행
- 회원 가입, 로그인 기능 // clear
- 2차 로그인 기능 // clear
- 회원 정보 확인 // clear
- 30초주기 otp key를 서버로 보내고 otp code를 할당받음 // clear
- ocr 기능(이미지를 text 형태로 변환) // clear
- 멀티 데이터 기능(데이터 형식 변환 기능) // 5.24~5.31 구현 예정
- 서버 없이 테스트할 수 있는 부분 체크하기 (2차 로그인, 30초 주기 함수 호출 등)
- 서버와 통합 및 에러사항 찾기 (최종발표 가능)
- 발생할 수 있는 버그 수정

- 
## 발견된 최종발표와 별개인 에러사항
- ocr 이미지 변경(한 번 가져온 이미지를 다른 이미지로 변경 기능 x)
- ocr 이미지 초기화(다른 화면으로 나갔다 들어오면 이미지가 초기화됨, 이미지 변경 기능이 없어 해당 에러를 활용해 이미지를 변경, 위의 에러를 해결한 뒤 해결 예정)
- 30초 마다 서버 연동시 객체 생성(30초에 한 번씩 retroit 객체가 생성됨-> 메모리 낭비, 수정 예정)


## 충동 피하기
- 작업하다 끝까지 못했어도 깃허브에 올려놓기(그 사이에 다른사람이 수정해놓으면 충돌)
- 작업시작할때 깃허브에서 업데이트 받고 시작하기(항상 최신 코드로 작업)
- 작업시간 안겹치게하기(같은 시간에 작업하면 업데이트가 안된 코드로 작업할 확률 높음, 보통 오전에 할듯?)



