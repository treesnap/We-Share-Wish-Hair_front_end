# We-Share-Wish-Hair_front_end

### Separation Android Repository and Spring Sever Repository
### [Before Commits](https://github.com/EunChanNam/We-Share-Wish-Hair/tree/AND)

#### tasks - 0518
1. 얼굴형, 태그포함 기능 서버연결 
   1. spring 서버 연동 확인, flask 서버도 실행 후 테스트 필요
2. 서버 연결 후 로딩화면 재구성 
   1. FaceFuncActivity에서 요청보내서 결과 받아오고 FaceResultActivity에 intent.putExtra
      1. UploadCallback 인터페이스 활용해서 종료 데이터 파싱
   2. FaceResultActivity에서 getIntent 해서 얼굴형 추천 헤어스타일 recyclerView에 뿌림
3. 태그 포함 요청 처리
 !! 1, 2, 3번 -> plask서버 열어서 테스트 필요
4. 기능 결과화면 추천 헤어스타일 아이템 변경 >> nam 코드 재활용
