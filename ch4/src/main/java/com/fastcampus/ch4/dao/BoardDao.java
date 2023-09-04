package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.SearchCondition;

import java.util.List;
import java.util.Map;

public interface BoardDao {


    //여기서 에러나는건 service계층으로 던져줌
    int count() throws Exception // T selectOne(String statement)
    ;

    int deleteAll() // int delete(String statement)
    ;

    int delete(Integer bno, String writer) throws Exception // int delete(String statement, Object parameter)
    ;

    int insert(BoardDto dto) throws Exception // int insert(String statement, Object parameter)
    ;

    List<BoardDto> selectAll() throws Exception // List<E> selectList(String statement)
    ;

    //여기서 에러나는건 service계층으로 던져줌
    BoardDto select(int bno) throws Exception;

    List<BoardDto> selectPage(Map map) throws Exception // List<E> selectList(String statement, Object parameter)
    ;

    int update(BoardDto dto) throws Exception // int update(String statement, Object parameter)
    ;

    int increaseViewCnt(Integer bno) throws Exception // int update(String statement, Object parameter)
    ;

    int searchResultCnt(SearchCondition sc) throws Exception // T selectOne(String statement, Object parameter)
    ;

    List<BoardDto> searchSelectPage(SearchCondition sc) throws Exception // List<E> selectList(String statement, Object parameter)
    ;

    int updateCommentCnt(Integer bno, int cnt);
}