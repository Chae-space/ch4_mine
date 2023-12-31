package com.chaespace.board.service;

import com.chaespace.board.dao.BoardDao;
import com.chaespace.board.dao.CommentDao;
import com.chaespace.board.domain.BoardDto;
import com.chaespace.board.domain.CommentDto;
import com.fastcampus.ch4.dao.*;
import com.fastcampus.ch4.domain.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class CommentServiceImplTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentDao commentDao;
    @Autowired
    BoardDao boardDao;


    //댓글 삭제
    @Test
    public void remove() throws Exception {
        //일단 게시판 다 지우고 새로운 게시물 넣고 잘 들어갔는지 확인
        boardDao.deleteAll();
        BoardDto boardDto = new BoardDto("hello", "hello", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        //그 게시물 번호를 얻어와서 출력
        Integer bno = boardDao.selectAll().get(0).getBno();
        System.out.println("bno = " + bno);

        //해당 게시물번호에 있는 댓글 다 지움
        commentDao.deleteAll(bno);
        CommentDto commentDto = new CommentDto(bno,0,"hi","qwer");

        //댓글 다지워졌으므로 댓글 카운트는 0이어야 함
        assertTrue(boardDao.select(bno).getComment_cnt() == 0);
        //write()로 댓글 썼으므로 댓글 카운트는 1이어야 함
        assertTrue(commentService.write(commentDto)==1);
        assertTrue(boardDao.select(bno).getComment_cnt() == 1);

        Integer cno = commentDao.selectAll(bno).get(0).getCno();

        // remove로 댓글 삭제한 후 댓글 카운트가 다시 0이되는지 확인. remove할 때 문제가 생기면 롤백이 잘 되는지 확인하기 위해
        // 일부러 예외를 발생시키고 Tx가 취소되는지 확인해야 함.
        int rowCnt = commentService.remove(cno, bno, commentDto.getCommenter());
        assertTrue(rowCnt==1);
        assertTrue(boardDao.select(bno).getComment_cnt() == 0);
    }

    @Test
    public void write() throws  Exception {
        boardDao.deleteAll();

        BoardDto boardDto = new BoardDto("hello", "hello", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        Integer bno = boardDao.selectAll().get(0).getBno();
        System.out.println("bno = " + bno);

        commentDao.deleteAll(bno);
        CommentDto commentDto = new CommentDto(bno,0,"hi","qwer");

        assertTrue(boardDao.select(bno).getComment_cnt() == 0);
        assertTrue(commentService.write(commentDto)==1);

        Integer cno = commentDao.selectAll(bno).get(0).getCno();
        assertTrue(boardDao.select(bno).getComment_cnt() == 1);
    }
}