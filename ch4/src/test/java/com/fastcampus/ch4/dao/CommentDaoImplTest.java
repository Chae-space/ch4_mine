package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class CommentDaoImplTest {
   @Autowired
   CommentDao commentDao;

    @Test
    public void count() throws Exception {
        //bno가 1번인 게시물의 모든 댓글을 지움
        commentDao.deleteAll(1);
        assertTrue(commentDao.count(1)==0);
    }

    @Test
    public void delete() throws Exception {
        commentDao.deleteAll(1);
        CommentDto commentDto = new CommentDto(1, 0, "comment", "asdf");
        assertTrue(commentDao.insert(commentDto)==1);
        assertTrue(commentDao.count(1)==1);
    }

    @Test
    public void insert() throws Exception {

        //다 지운후게 insert하면 count는 1이 돼야함
        commentDao.deleteAll(1);
        CommentDto commentDto = new CommentDto(1, 0, "comment", "asdf");
        assertTrue(commentDao.insert(commentDto)==1);
        assertTrue(commentDao.count(1)==1);

        //하나 더넣어서 count는 2
        commentDto = new CommentDto(1, 0, "comment", "asdf");
        assertTrue(commentDao.insert(commentDto)==1);
        assertTrue(commentDao.count(1)==2);
    }

    @Test
    public void selectAll() throws Exception {

        //다 지운후에 하나 insert했으므로 count는 1
        commentDao.deleteAll(1);
        CommentDto commentDto = new CommentDto(1, 0, "comment", "asdf");
        assertTrue(commentDao.insert(commentDto)==1);
        assertTrue(commentDao.count(1)==1);

        //하나있는 상태에서 selecAll로 가져오면 size는 1
        List<CommentDto> list = commentDao.selectAll(1);
        assertTrue(list.size()==1);

        commentDto = new CommentDto(1, 0, "comment", "asdf");
        assertTrue(commentDao.insert(commentDto)==1);
        assertTrue(commentDao.count(1)==2);

        list = commentDao.selectAll(1);
        assertTrue(list.size()==2);
    }

    @Test
    public void select() throws Exception {
        commentDao.deleteAll(1);
        CommentDto commentDto = new CommentDto(1, 0, "comment", "asdf");
        assertTrue(commentDao.insert(commentDto)==1);
        assertTrue(commentDao.count(1)==1);

        List<CommentDto> list = commentDao.selectAll(1);

        //comment랑 commenter꺼내서 Dto꺼랑 같은지 확인
        String comment = list.get(0).getComment();
        String commenter = list.get(0).getCommenter();
        assertTrue(comment.equals(commentDto.getComment()));
        assertTrue(commenter.equals(commentDto.getCommenter()));
    }

    @Test
    public void update() throws Exception {
        // 다지우고 insert해서 count가 1인지 확인하고
        commentDao.deleteAll(1);
        CommentDto commentDto = new CommentDto(1, 0, "comment", "asdf");
        assertTrue(commentDao.insert(commentDto)==1);
        assertTrue(commentDao.count(1)==1);

        //방금 insert한거 selectAll해서 리스트에 넣어준다음에 리스트에서 댓글번호를 가져옴, 내용은 comment2로 바꿈
        List<CommentDto> list = commentDao.selectAll(1);
        commentDto.setCno(list.get(0).getCno());
        commentDto.setComment("comment2");
//        Dao의 update는 session의 update를 호출하는데 session의 update는 영향받은 row의 수를 리턴하므로
//        update가 성공하면 1을 리턴하게 됨
        assertTrue(commentDao.update(commentDto)==1);

        //그 다음에 다시 select 해와서 셋팅해준 commentDto랑 같은지 비교하고 같으면 update가 잘된것
        list = commentDao.selectAll(1);
        String comment = list.get(0).getComment();
        String commenter = list.get(0).getCommenter();
        assertTrue(comment.equals(commentDto.getComment()));
        assertTrue(commenter.equals(commentDto.getCommenter()));
    }
}