package com.fastcampus.ch4.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fastcampus.ch4.domain.*;
import com.fastcampus.ch4.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.*;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;


    @PostMapping("/write")
    //그쪽에서 보내준 내용을 받아야 하므로 인자로 BoardDto
    public String write(BoardDto boardDto, HttpSession session, Model m, RedirectAttributes rattr) {

        String writer = (String) session.getAttribute("id");
        //boardDto에 글쓴사람 id를 넣어줘야 함.
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.write(boardDto);

            //예외는 안났지만 rowCnt가 1이 아닐 수도 있으므로 이때도 예외 던져주기
            if (rowCnt != 1) {
                throw new Exception("Write failed");
            }

            //메세지가 한번만 뜨도록 모델말고 RedirectAttributes에 저장하기(세션을 이용한 1회성 저장)
            rattr.addFlashAttribute("msg", "write ok");
            //글 다쓴 후에는 목록으로 돌아가기
            return "redirect:/board/list";
        } catch (Exception e) {
            e.printStackTrace();
            //모델을 이용해서 입력했던 내용 다시 돌려줌(자기가 썼던 내용 볼 수 있게)
            m.addAttribute("boardDto", boardDto);
            //메세지를 줘서 왜 진행이 안되는지를 알려주기
            rattr.addFlashAttribute("msg", "write error");
            //예외 발생 시 다시 글쓰기 화면으로
            return "board";
        }

    }


    @GetMapping("/write")
    public String write(Model m) {
        //board.jsp를 게시물 읽기와 쓰기 둘다 사용하므로 구별을 위해 게시물을 쓸 때는 model에 mode값을 new로 담아서줌
        m.addAttribute("mode", "new");
        //jsp에서는 mode값을 확인해서 new일 때에는 쓰기에 알맞게 세팅해줌
        return "board";
    }


    @PostMapping("/modify")
    //그쪽에서 보내준 내용을 받아야 하므로 인자로 BoardDto
    public String modify(BoardDto boardDto, HttpSession session, Model m, RedirectAttributes rattr) {

        String writer = (String) session.getAttribute("id");
        //boardDto에 글쓴사람 id를 넣어줘야 함.
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.modify(boardDto);

            //예외는 안났지만 rowCnt가 1이 아닐 수도 있으므로 이때도 예외 던져주기
            if (rowCnt != 1) {
                throw new Exception("Modify failed");
            }

            //메세지가 한번만 뜨도록 모델말고 RedirectAttributes에 저장하기(세션을 이용한 1회성 저장)
            rattr.addFlashAttribute("msg", "modify completed");
            //글 다쓴 후에는 목록으로 돌아가기
            return "redirect:/board/list";
        } catch (Exception e) {
            e.printStackTrace();
            //모델을 이용해서 입력했던 내용 다시 돌려줌(자기가 썼던 내용 볼 수 있게)
            m.addAttribute("boardDto", boardDto);
            //메세지를 줘서 왜 진행이 안되는지를 알려주기
            rattr.addFlashAttribute("msg", "modify error");
            //예외 발생 시 다시 글쓰기 화면으로
            return "board";
        }

    }

    //삭제는 post 요청이므로
    @PostMapping("/remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m, HttpSession session, RedirectAttributes rattr) {
        //writer도 service의 remove에 줘야하는데 writer는 세션에서 아이디로 구함
        String writer = (String) session.getAttribute("id");
        try {
            //이렇게 모델에 담아주면 redirect할때 자동으로 붙음(ex. redirect:/board/list?page= ~~ 이런식으로)
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

            int rowCnt = boardService.remove(bno, writer);
            //1이 아니면 예외 던지기
            if (rowCnt != 1) {
                throw new Exception("board remove error");
            }

            //모델이 아니라 RedirectAttribtes에 저장하는 이유 : 메세지가 한번만 뜨게 하기 위해서
            rattr.addFlashAttribute("msg", "delete ok");  //한글로 하면 인코딩&디코딩 다해줘야되니까 일단 영어로
        } catch (Exception e) {
            e.printStackTrace();
            //RedirectAttribtes의 addFlashAttributes()를 사용하면 메세지가 한번만 뜸.
            rattr.addFlashAttribute("msg", "delete error");
        }

        //게시물이 삭제된 후에는 다시 게시물 목록으로 이동
        return "redirect:/board/list";
    }


    @GetMapping("/read")
    public String read(Integer bno, Integer page, Integer pageSize, Model m) {

        try {
            //인자로 받은 bno로 서비스의 read()를 호출하고 처리된 결과를 Dto로 받아옴
            BoardDto boardDto = boardService.read(bno);

            //Dto를 모델(m)에 담아서 board.jsp로 전달
            //m.addAttribute("boardDto", boardDto);와 아래문장 동일함
            m.addAttribute(boardDto);

            //뷰로 넘겨줌
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //서비스에서 읽어온 게시물 내용이 board.jsp로 전달됨
        return "board";
    }

    @GetMapping("/list")
    public String list(SearchCondition sc, Model m, HttpServletRequest request) {
        if (!loginCheck(request))
            return "redirect:/login/login?toURL=" + request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

//         인자에서 page, pageSize 지웠으므로 일단 주석처리
//        if (page == null) page = 1;
//        if (pageSize == null) pageSize = 10;

        try {
            int totalCnt = boardService.getSearchResultCnt(sc);
            pageHandler pageHandler = new pageHandler(totalCnt, sc);

//            Map map = new HashMap();
//            map.put("offset", (page - 1) * pageSize);
//            map.put("pageSize", pageSize);

            List<BoardDto> list = boardService.getSearchResultPage(sc);
            //모델에 추가
            m.addAttribute("list", list);
            //이 페이지핸들러가 값을 계산해서 다가지고있기 때문에 얘를 jsp에 넘겨주면 jsp에서 이 값을 가지고 페이징 할 수 있음
            m.addAttribute("ph", pageHandler);
//            m.addAttribute("page", page);
//            m.addAttribute("pageSize", pageSize);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id") != null;
    }
}