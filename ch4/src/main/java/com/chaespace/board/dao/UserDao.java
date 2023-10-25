package com.chaespace.board.dao;

import com.chaespace.board.domain.User;
import com.fastcampus.ch4.domain.*;

public interface UserDao {
    User selectUser(String id) throws Exception;
    int deleteUser(String id) throws Exception;
    int insertUser(User user) throws Exception;
    int updateUser(User user) throws Exception;
    int count() throws Exception;
    void deleteAll() throws Exception;
}