package com.chaespace.board.dao;

import com.chaespace.board.domain.BoardDto;
import com.chaespace.board.domain.SearchCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class BoardDaoImplTest {

    @Autowired
    BoardDao boardDao;

//    @Test
//    public void select() throws Exception {
//        //BoardDao가 주입 잘 되는지 확인(null이 아닌지)
//        assertTrue(boardDao != null);
//        System.out.println("boardDao=" + boardDao);
//
//        //결과값 가져올 resultType이 BoardDto
//        BoardDto boardDto = boardDao.select(1);
//        System.out.println("boardDto = " + boardDto);
//
//        //boardDto의 게시물 번호가 1과 같은지 확인
//        assertTrue(boardDto.getBno().equals(1));
//    }


    @Test
    public void insertTestData() throws Exception {
        boardDao.deleteAll();
        //반복문으로 게시물 220개 쓰기
        for (int i = 1; i <= 220; i++) {
            BoardDto boardDto = new BoardDto("titled" + i, "naengmoo", "asdf");
            boardDao.insert(boardDto);
        }
    }


    @Test
    public void countTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 1);

        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 2);
    }

    @Test
    public void deleteAllTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.deleteAll() == 1);
        assertTrue(boardDao.count() == 0);

        boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.deleteAll() == 2);
        assertTrue(boardDao.count() == 0);
    }

    @Test
    public void deleteTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        Integer bno = boardDao.selectAll().get(0).getBno();
        assertTrue(boardDao.delete(bno, boardDto.getWriter()) == 1);
        assertTrue(boardDao.count() == 0);

        assertTrue(boardDao.insert(boardDto) == 1);
        bno = boardDao.selectAll().get(0).getBno();
        assertTrue(boardDao.delete(bno, boardDto.getWriter() + "222") == 0);
        assertTrue(boardDao.count() == 1);

        assertTrue(boardDao.delete(bno, boardDto.getWriter()) == 1);
        assertTrue(boardDao.count() == 0);

        assertTrue(boardDao.insert(boardDto) == 1);
        bno = boardDao.selectAll().get(0).getBno();
        assertTrue(boardDao.delete(bno + 1, boardDto.getWriter()) == 0);
        assertTrue(boardDao.count() == 1);
    }

    @Test
    public void insertTest() throws Exception {
        boardDao.deleteAll();
        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 2);

        boardDao.deleteAll();
        boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 1);
        //System.out.println("BoardDto=" + boardDto);
    }

    @Test
    public void selectAllTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        List<BoardDto> list = boardDao.selectAll();
        assertTrue(list.size() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        list = boardDao.selectAll();
        assertTrue(list.size() == 1);

        assertTrue(boardDao.insert(boardDto) == 1);
        list = boardDao.selectAll();
        assertTrue(list.size() == 2);

    }

    @Test
    public void selectTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        Integer bno = boardDao.selectAll().get(0).getBno();
        //System.out.println("bno="+bno);
        boardDto.setBno(bno);
        BoardDto boardDto2 = boardDao.select(bno);
        assertTrue(boardDto.equals(boardDto2));
    }

    @Test
    public void selectPageTest() throws Exception {
        boardDao.deleteAll();

        for (int i = 1; i <= 10; i++) {
            BoardDto boardDto = new BoardDto("" + i, "no content" + i, "asdf");
            boardDao.insert(boardDto);
        }

        Map map = new HashMap();
        map.put("offset", 0);
        map.put("pageSize", 3);

        List<BoardDto> list = boardDao.selectPage(map);
        assertTrue(list.get(0).getTitle().equals("10"));
        assertTrue(list.get(1).getTitle().equals("9"));
        assertTrue(list.get(2).getTitle().equals("8"));

        map = new HashMap();
        map.put("offset", 0);
        map.put("pageSize", 1);

        list = boardDao.selectPage(map);
        assertTrue(list.get(0).getTitle().equals("10"));

        map = new HashMap();
        map.put("offset", 7);
        map.put("pageSize", 3);

        list = boardDao.selectPage(map);
        assertTrue(list.get(0).getTitle().equals("3"));
        assertTrue(list.get(1).getTitle().equals("2"));
        assertTrue(list.get(2).getTitle().equals("1"));
    }

    @Test
    public void searchSelectPageTest() throws Exception {
        boardDao.deleteAll();
        for (int i = 1; i <= 20; i++) {
            BoardDto boardDto = new BoardDto("title" + i, "content", "asdf"+i);
            boardDao.insert(boardDto);
        }

        //sc의 인자들은 테스트를 위해 지정한 값. 실제로는 boardList.jsp에서 받아오게 됨
        //title 검색 잘되는지 테스트
        SearchCondition sc = new SearchCondition(1, 10, "T", "title2");  //title2%
        System.out.println("sc="+ sc);
        List<BoardDto> list = boardDao.searchSelectPage(sc);
        System.out.println("Offset: " + sc.getOffset());
        System.out.println("list= " + list);
        System.out.println(list.size());
       assertTrue(list.size() == 2);  //title2랑 title20 두 개



        //writer 검색 잘되는지 테스트
        sc = new SearchCondition(1, 10, "W", "asdf2");  //asdf2%
        System.out.println("sc="+ sc);
        list = boardDao.searchSelectPage(sc);
        System.out.println("list= " + list);
        System.out.println(list.size());
        assertTrue(list.size() == 2);  //asdf2랑 asdf20 두 개
    }

    @Test
    public void searchResultCntTest() throws Exception {
        boardDao.deleteAll();
        for (int i = 1; i <= 20; i++) {
            BoardDto boardDto = new BoardDto("title" + i, "content", "asdf"+i);
            boardDao.insert(boardDto);
        }

        //title검색
        SearchCondition sc = new SearchCondition(1, 10, "T", "title2");  //title2%
        int cnt = boardDao.searchResultCnt(sc);
        System.out.println("cnt = " + cnt);
        assertTrue(cnt== 2);  //title2랑 title20 두 개


        //writer검색
        sc = new SearchCondition(1, 10, "W", "asdf2");  //asdf2%
        cnt = boardDao.searchResultCnt(sc);
        System.out.println("cnt = " + cnt);
        assertTrue(cnt== 2);  //asdf2랑 asdf20 두 개
    }

    @Test
    public void updateTest() throws Exception {
        boardDao.deleteAll();
        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        Integer bno = boardDao.selectAll().get(0).getBno();
        System.out.println("bno = " + bno);
        boardDto.setBno(bno);
        boardDto.setTitle("yes title");
        assertTrue(boardDao.update(boardDto) == 1);

        BoardDto boardDto2 = boardDao.select(bno);
        assertTrue(boardDto.equals(boardDto2));
    }

    @Test
    public void increaseViewCntTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 1);

        Integer bno = boardDao.selectAll().get(0).getBno();
        assertTrue(boardDao.increaseViewCnt(bno) == 1);

        boardDto = boardDao.select(bno);
        assertTrue(boardDto != null);
        assertTrue(boardDto.getView_cnt() == 1);

        assertTrue(boardDao.increaseViewCnt(bno) == 1);
        boardDto = boardDao.select(bno);
        assertTrue(boardDto != null);
        assertTrue(boardDto.getView_cnt() == 2);
    }
}