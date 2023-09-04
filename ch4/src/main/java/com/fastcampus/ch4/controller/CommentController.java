package com.fastcampus.ch4.controller;


import com.fastcampus.ch4.domain.CommentDto;
import com.fastcampus.ch4.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("/comments")  // /comments?bn0=1080 이런식으로 게시물 번호를 받아와야함, 요청은 GET방식으로
    @ResponseBody   //@ResponseBody 안붙이면 return list에서 list를 뷰 이름(comments.jsp)로 해석해버림
    //특정 게시물에 달린 댓글 리스트를 반환하는 메소드. bno를 받아와야함.
    public ResponseEntity<List<CommentDto>> list(Integer bno) {

        List<CommentDto> list = null;
        try {
            list = commentService.getList(bno);
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK);   //응답이 200번으로

        } catch (Exception e) {
            e.printStackTrace();
            //에러났을 때는 list 보내나마나므로 빼고 상태를 Bad_Request로
            return new ResponseEntity<List<CommentDto>>(HttpStatus.BAD_REQUEST);  //응답이 400번으로

        }
    }



    //댓글 삭제하는 메소드. 삭제니까 DeleteMapping.
    @DeleteMapping("/comments/{cno}")    //   /comments/cno=1?bno=1085   <--삭제할 댓글 번호와 글번호
    @ResponseBody
    public ResponseEntity<String> remove(@PathVariable Integer cno, Integer bno, HttpSession session){

        //작성자는 세션에서 가져옴
        String commenter = (String)session.getAttribute("id");
        try {
           int rowCnt = commentService.remove(cno, bno,commenter);

           if(rowCnt!=1) throw new Exception("Delete Failed");

            return new ResponseEntity<>("delete ok", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("delete error", HttpStatus.BAD_REQUEST);
        }

    }


    //댓글 작성하는 메소드
    @PostMapping("/comments")  //  /ch4/comments?bno=1085  이런식으로 POST
    @ResponseBody
    //입력한 내용을 받아와야 되므로 CommentDto도 받아와야 함
    public ResponseEntity<String> write(CommentDto dto, Integer bno, HttpSession session){
        String commenter = (String)session.getAttribute("id");



    }

    }
