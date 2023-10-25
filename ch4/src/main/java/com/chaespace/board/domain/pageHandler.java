package com.chaespace.board.domain;

public class pageHandler {

//    private int page;       //현재 페이지
//    private int pageSize;   //한 페이지의 게시물 개수
//    private String option;
//    private String keyword;

    private SearchCondition sc;
    private int naviSize=10;   //페이지 네비게이션의 크기
    private int totalPage;  //전체 페이지의 개수

    private int totalCnt;   //총 페이지의 개수
    private int beginPage;  //네비게이션의 첫번째 페이지
    private int endPage;    //네비게이션의 마지막 페이지
    private boolean showPrev; //이전 페이지로 이동하는 링크를 보여줄 것인지 여부(1 2 3 ~ 10까지 있을 때, 1은 더 앞이 없으므로 꺽쇠(<)가 뜨면 안됨)
    private boolean showNext; //다음 페이지로 이동하는 링크를 보여줄 것인지 여부(최종 페이지의 경우 그다음이 없으므로 꺽쇠(>)가 뜨면 안됨)


    //생성자 생성
//    public pageHandler(int totalCnt, int page, int pageSize) {
//        this.totalPage = totalCnt;
//        this.page = page;
//        this.pageSize = pageSize;

//        //총페이지 개수는 총 게시물 개수를 한페이지 크기로 나누면 나옴
//        //만약 나눈 값이 남으면 페이지 하나가 더필요하므로 ceil함수로 올림
//        totalPage = (int) Math.ceil(totalCnt /(double)pageSize);

//        //시작페이지는 (페이지-1)/10 * 10 +1 하면 나옴 ->처음에 페이지-1안해줘서 밑의 네비게이션 시작페이지가 잘못뜸
//        beginPage = (page-1) / naviSize * naviSize + 1;

//        //끝페이지는 시작페이지+10
//        //근데 마지막의 경우 토탈 페이지가 더 적을수 있으므로 더 작은거 고르는 min()함수 써줌
//        endPage = Math.min(beginPage + naviSize-1, totalPage);

//        //앞의목록 보여주는 꺽쇠(<)의 경우 시작 페이지가 1만 아니면 다 뜸
//        showPrev = beginPage != 1;
//        //그다음목록 보여주는 꺽쇠<>)의 경우 끝페이지가 총페이지 수랑 같지만 않으면 다뜸
//        showNext = endPage != totalPage;
//    }

    public void doPaging(int totalCnt, SearchCondition sc) {
        this.totalPage = totalCnt;

        //총페이지 개수는 총 게시물 개수를 한페이지 크기로 나누면 나옴
        //만약 나눈 값이 남으면 페이지 하나가 더필요하므로 ceil함수로 올림
        totalPage = (int) Math.ceil(totalCnt /(double)sc.getPageSize());

        //시작페이지는 (페이지-1)/10 * 10 +1 하면 나옴 ->처음에 페이지-1안해줘서 밑의 네비게이션 시작페이지가 잘못뜸
        beginPage = (sc.getPage()-1) / naviSize * naviSize + 1;

        //끝페이지는 시작페이지+10
        //근데 마지막의 경우 토탈 페이지가 더 적을수 있으므로 더 작은거 고르는 min()함수 써줌
        endPage = Math.min(beginPage + naviSize-1, totalPage);

        //앞의목록 보여주는 꺽쇠(<)의 경우 시작 페이지가 1만 아니면 다 뜸
        showPrev = beginPage != 1;
        //그다음목록 보여주는 꺽쇠<>)의 경우 끝페이지가 총페이지 수랑 같지만 않으면 다뜸
        showNext = endPage != totalPage;
    }

//    //totalCnt 랑 page값만 받은 경우의 생성자 : pageSize를 10으로
//    public pageHandler(int totalCnt, int page) {
//        this(totalCnt, page, 10);
//    }

    public pageHandler(int totalCnt, SearchCondition sc){

        this.totalCnt = totalCnt;
        this.sc = sc;
        doPaging(totalCnt, sc);
    }


    //게터세터 생성
    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getNaviSize() {
        return naviSize;
    }

    public void setNaviSize(int naviSize) {
        this.naviSize = naviSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }




    //페이지 네비게이션을 프린트하는 메소드
    void print() {
        System.out.println("page = " + sc.getPage());

        //showPrev 참이면 이전페이지가는 링크를 보여주고 거짓이면 빈문자열 출력
        System.out.print(showPrev ? "[prev] " : "");

        for (int i = beginPage; i <= endPage; i++) {
            System.out.print(i + " ");
        }
        System.out.println(showNext ? "[NEXT]" : "");

    }

    public SearchCondition getSc() {
        return sc;
    }

    public void setSc(SearchCondition sc) {
        this.sc = sc;
    }

//toString 생성

    @Override
    public String toString() {
        return "pageHandler{" +
                "sc=" + sc +
                ", naviSize=" + naviSize +
                ", totalPage=" + totalPage +
                ", totalCnt=" + totalCnt +
                ", beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", showPrev=" + showPrev +
                ", showNext=" + showNext +
                '}';
    }
}
