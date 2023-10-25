package com.chaespace.board.dao;

import com.chaespace.board.domain.BoardDto;
import com.chaespace.board.domain.SearchCondition;
import com.fastcampus.ch4.domain.*;
import org.apache.ibatis.session.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {
    //Autowired로 session 주입받기
    @Autowired
    SqlSession session;
    String namespace = "com.fastcampus.ch4.dao.BoardMapper.";


    @Override
    public int count() throws Exception {

        //selectOne() : MyBatis 라이브러리 메소드. DB에서 한 개의 결과를 반환하는 쿼리를 실행할 때 사용함.
        return session.selectOne(namespace + "count");
    } // T selectOne(String statement)


    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    } // int delete(String statement)


    @Override
    public int delete(Integer bno, String writer) throws Exception {
        Map map = new HashMap();
        map.put("bno", bno);
        map.put("writer", writer);
        return session.delete(namespace + "delete", map);
    } // int delete(String statement, Object parameter)

    @Override
    public int insert(BoardDto dto) throws Exception {
        return session.insert(namespace + "insert", dto);
    } // int insert(String statement, Object parameter)

    @Override
    public List<BoardDto> selectAll() throws Exception {
        return session.selectList(namespace + "selectAll");
    } // List<E> selectList(String statement)

    //여기서 에러나는건 service계층으로 던져줌
    @Override
    public BoardDto select(int bno) throws Exception {
        /*session 객체를 이용해서 한줄 갖고옴
        "select"는 SQL문 아이디
        boardMapper.xml의 id-select, 파라미터타입-int(bno), resultType=BoardDto가 다 맞아떨어져야함*/
        return session.selectOne(namespace + "select", bno);

    }


    @Override
    public List<BoardDto> selectPage(Map map) throws Exception {
        return session.selectList(namespace + "selectPage", map);
    } // List<E> selectList(String statement, Object parameter)

    @Override
    public int update(BoardDto dto) throws Exception {
        return session.update(namespace + "update", dto);
    } // int update(String statement, Object parameter)

    @Override
    public int increaseViewCnt(Integer bno) throws Exception {
        return session.update(namespace + "increaseViewCnt", bno);
    } // int update(String statement, Object parameter)

    @Override
    public int searchResultCnt(SearchCondition sc) throws Exception {
        System.out.println("sc in searchResultCnt() = " + sc);
        System.out.println("session = " + session);
        return session.selectOne(namespace + "searchResultCnt", sc);
    } // T selectOne(String statement, Object parameter)

    @Override
    public List<BoardDto> searchSelectPage(SearchCondition sc) throws Exception {
        return session.selectList(namespace + "searchSelectPage", sc);
    } // List<E> selectList(String statement, Object parameter)

    @Override
    public int updateCommentCnt(Integer bno, int cnt) {
        Map map = new HashMap();    //맵퍼xml파일의 updateCommentCnt에선 map을 파라미터로 받고있으므로.
        map.put("cnt", cnt);
        map.put("bno", bno);
        return session.update(namespace + "updateCommentCnt", map);
    }


}