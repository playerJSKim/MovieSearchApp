# project_Movie

 - 네이버 영화 검색 API를 사용했습니다.
 - 검색 후 리스트의 영화를 클릭 하면 웹브라우저로 이동됩니다.
 - 검색버튼 옆의 최근검색 버튼을 누르면 검색이력이 나옵니다.
 ( 최근 10개의 검색어가 표출됩니다. )
 - 최근 검색어 목록에서 선택시 해당 검색어로 재검색 해줍니다.


2022.07.11
 - MVVM 구조로 변경중
  : Sharedpreferences -> Room
  : fragment와 Activity data 통신 
    intent -> ViewModel
 
 #아직 통신 모듈이 view와 coupling되지 않도록 모듈화되지 않음
  
