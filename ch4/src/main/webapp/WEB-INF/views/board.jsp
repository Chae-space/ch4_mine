<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--원래는 core인데 가끔 버전 문제가 일어날 때 있음 -> "_rt" 붙이면 해결됨-->
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page session="true" %>
<c:set var="loginId" value="${sessionScope.id}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? 'Login' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chae board.jsp</title>
    <!--원래 /css/menu.css였는데 에러떠서 /resources 붙이니까 빨간줄안뜸. 근데 실행했을 때 화면에안떠서 걍 다시 지움-->
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.7.0.js"></script>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: "Noto Sans KR", sans-serif;
        }

        .container {
            width: 50%;
            margin: auto;
        }

        .writing-header {
            position: relative;
            margin: 20px 0 0 0;
            padding-bottom: 10px;
            border-bottom: 1px solid #323232;
        }

        input {
            width: 100%;
            height: 35px;
            margin: 5px 0px 10px 0px;
            border: 1px solid #e9e8e8;
            padding: 8px;
            background: #f8f8f8;
            outline-color: #e6e6e6;
        }

        textarea {
            width: 100%;
            background: #f8f8f8;
            margin: 5px 0px 10px 0px;
            border: 1px solid #e9e8e8;
            resize: none;
            padding: 8px;
            outline-color: #e6e6e6;
        }

        .frm {
            width: 100%;
        }

        .btn {
            background-color: rgb(236, 236, 236); /* Blue background */
            border: none; /* Remove borders */
            color: black; /* White text */
            padding: 6px 12px; /* Some padding */
            font-size: 16px; /* Set a font size */
            cursor: pointer; /* Mouse pointer on hover */
            border-radius: 5px;
        }

        .btn:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div id="menu">
    <ul>
        <li id="logo">chae board.jsp</li>
        <li><a href="<c:url value='/'/>">Home</a></li>
        <li><a href="<c:url value='/board/list'/>">Board</a></li>
        <li><a href="<c:url value='${loginOutLink}'/>">${loginOut}</a></li>
        <li><a href="<c:url value='/register/add'/>">Sign in</a></li>
        <li><a href=""><i class="fa fa-search"></i></a></li>
    </ul>
</div>
<script>
    let msg = "${msg}"
    if (msg == "write error") alert("게시물 등록에 실패했습니다. 다시 시도해주세요")
    if (msg == "modify error") alert("게시물 수정에 실패하였습니다. 다시 시도해 주세요");

</script>
<div class="container">
    <h2 class="writing-header">게시물 ${mode=="new" ? "글쓰기" : "읽기"}</h2>
    <form id="form" class="frm" action="" method="post">
        <input type="hidden" name="bno" value="${boardDto.bno}" readonly="readonly">
        <input type="text" name="title" value="<c:out value='${boardDto.title}'/>" placeholder="  제목을 입력해 주세요." ${mode=="new" ? "" : "readonly='readonly'"}><br>
        <textarea name="content" rows="20" placeholder=" 내용을 입력해 주세요." ${mode=="new" ? "" : "readonly='readonly'"}><c:out value='${boardDto.content}'/></textarea><br>

        <c:if test="${mode eq 'new'}">
            <button type="button" id="writeBtn" class="btn btn-write"><i class="fa fa-pencil"></i> 등록</button>
        </c:if>
        <c:if test="${mode ne 'new'}">
            <button type="button" id="writeNewBtn" class="btn btn-write"><i class="fa fa-pencil"></i> 글쓰기</button>
        </c:if>
        <c:if test="${boardDto.writer eq loginId}">
            <button type="button" id="modifyBtn" class="btn btn-modify"><i class="fa fa-edit"></i> 수정</button>
            <button type="button" id="removeBtn" class="btn btn-remove"><i class="fa fa-trash"></i> 삭제</button>
        </c:if>
        <button type="button" id="listBtn" class="btn btn-list"><i class="fa fa-bars"></i> 목록</button>
    </form>
</div>
<script>
    $(document).ready(function(){
        let formCheck = function() {
            let form = document.getElementById("form");
            if(form.title.value=="") {
                alert("제목을 입력해 주세요.");
                form.title.focus();
                return false;
            }

            if(form.content.value=="") {
                alert("내용을 입력해 주세요.");
                form.content.focus();
                return false;
            }
            return true;
        }

        $("#writeNewBtn").on("click", function(){
            location.href="<c:url value='/board/write'/>";
        });

        $("#writeBtn").on("click", function(){
            let form = $("#form");
            form.attr("action", "<c:url value='/board/write'/>");
            form.attr("method", "post");

            if(formCheck())
                form.submit();
        });

        $("#modifyBtn").on("click", function(){
            let form = $("#form");
            let isReadonly = $("input[name=title]").attr('readonly');

            // 1. 읽기 상태이면, 수정 상태로 변경
            if(isReadonly=='readonly') {
                $(".writing-header").html("게시판 수정");
                $("input[name=title]").attr('readonly', false);
                $("textarea").attr('readonly', false);
                $("#modifyBtn").html("<i class='fa fa-pencil'></i> 등록");
                return;
            }

            // 2. 수정 상태이면, 수정된 내용을 서버로 전송
            form.attr("action", "<c:url value='/board/modify${searchCondition.queryString}'/>");
            form.attr("method", "post");
            if(formCheck())
                form.submit();
        });

        $("#removeBtn").on("click", function(){
            if(!confirm("정말로 삭제하시겠습니까?")) return;

            let form = $("#form");
            form.attr("action", "<c:url value='/board/remove${searchCondition.queryString}'/>");
            form.attr("method", "post");
            form.submit();
        });

        $("#listBtn").on("click", function(){
            location.href="<c:url value='/board/list${searchCondition.queryString}'/>";
        });
    });
    <%--제이쿼리문--%>
    <%--브라우저가 위의 html문서를 쫙 읽고 도움을 다 구성하고나면 document.ready()이벤트가 발생함--%>
    <%--$(document).ready(function () {   //main메소드와 같다고 생각하면 됨--%>
    <%--    //css의 셀렉터를 그대로 따라감 -> id앞엔 #을 붙임 -> 위에 있는 것 중 id가 listbtn인 요소를 선택하겠단 뜻.--%>
    <%--    $('#listBtn').on("click", function () {--%>

    <%--        //listbtn을 클릭했을 땐 boardList.jsp로 가야함--%>
    <%--        //page값을 줘야 첫번째 페이지로 안가고 보던 페이지로 갈 수 있음--%>
    <%--        //location은 브라우저에서 url주소치는 그 영역을 의미함. 여기에 href를 주면 브라우저창에서 주소를 직접 입력하는 것과 똑같음--%>
    <%--        //그 요청이 GET으로 감--%>
    <%--        location.href = "<c:url value='/board/list?page=${page}&pageSize=${pageSize}'/>";--%>
    <%--    })--%>

    <%--    $('#removeBtn').on("click", function () {--%>

    <%--        //NO 누르면 그냥 그화면 그대로 stay.--%>
    <%--        if (!confirm("정말로 삭제하시겠습니까?")) return;--%>

    <%--        //아이디가 form인 객체를 가지고 와서 변수에 담음(위의 form의 id가 form이므로)--%>
    <%--        let form = $('#form');--%>
    <%--        //여기서 /board/remove는 BoardController의 @PostMapping에 따라서 정해짐--%>
    <%--        form.attr("action", "<c:url value="/board/remove?page=${page}&pageSize=${pageSize}"/>");--%>
    <%--        form.attr("method", "post");--%>
    <%--        form.submit();   //form의 action을 /board/remove로 하고 메소드를 post로 해서 내용을 전송함--%>
    <%--    });--%>


    <%--    $('#writeBtn').on("click", function () {--%>
    <%--        let form = $('#form');--%>
    <%--        form.attr("action", "<c:url value="/board/write"/>");--%>
    <%--        form.attr("method", "post");--%>
    <%--        form.submit();   //form의 action을 /board/write로 하고 메소드를 post로 해서 내용을 전송함--%>
    <%--    });--%>

    <%--    $('#modifyBtn').on("click", function () {--%>
    <%--        //1. 읽기 상태이면 수정 상태로 변경--%>
    <%--        let form = $('#form');   //폼에 대한 참조를 얻어와서--%>
    <%--        let isReadOnly = $("input[name=title]").attr('readonly') //input태그의 name이 title인 요소의 attribute(readonly)를 읽어옴--%>

    <%--        if (isReadOnly == 'readonly') {   //readonly가 참이면 제목과 내용의 readonly속성을 해제함--%>
    <%--            $("input[name=title]").attr('readonly', false)  //title readonly 해제--%>
    <%--            $("textarea").attr('readonly', false)   //content readonly 해제--%>
    <%--            $("modifyBtn").html("등록")   //쓰기모드일 땐 수정버튼을 등록버튼으로 변경해줌--%>
    <%--            $("h2").html("게시물 수정")    //위에 h2사이즈로 떠있는 제목을 게시물 수정으로 변경해줌--%>
    <%--            return;--%>
    <%--        }--%>


    <%--        //2. 수정 상태이면 수정된 내용을 서버로 전송--%>

    <%--        form.attr("action", "<c:url value="/board/modify"/>");--%>
    <%--        form.attr("method", "post");--%>
    <%--        form.submit();   //form의 action을 /board/write로 하고 메소드를 post로 해서 내용을 전송함--%>
    <%--    });--%>
    <%--})--%>

</script>
</body>
</html>