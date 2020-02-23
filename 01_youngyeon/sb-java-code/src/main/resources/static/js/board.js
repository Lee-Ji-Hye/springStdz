let Board = function() {};
Board.prototype = {
};
function cookieInit() {
    window.localStorage.clear();
}
function cookieSave(key, value) {
    window.localStorage.setItem(key, JSON.stringify(value));
}

function cookieRead(key) {
    return JSON.parse(window.localStorage.getItem(key));
}

function disableDetailInput() {
    $("#txt-detail-id").prop("disabled", "disabled");
    $("#txt-detail-title").prop("disabled", "disabled");
    $("#txt-detail-content").prop("disabled", "disabled");
}

function enableDetailInput() {
    $("#txt-detail-id").prop("disabled", "");
    $("#txt-detail-title").prop("disabled", "");
    $("#txt-detail-content").prop("disabled", "");
}

// ajax("/api/board", "GET");
function ajax(url="", method="GET", data={}) {
    return $.ajax({
        url: url,
        type: method,
        data: method === "GET" ? data: JSON.stringify(data),
        accept: "application/json",
        contentType: "application/json; charset=utf-8",
        success:function(data){}
    });
}

Array.prototype.remove = function(idx) {
    if (idx > -1) this.splice(idx, 1);
};

function duplicateCheck(list) {
    if (list.filter(b=>b.title===title).length > 0) {
        return true;
    } else {
        return false;
    }
}

function eventInit() {

    // 페이지당 콤보박스 체인지
    $("#sel-pageSize").on("change", function(e){
        fnPage(0, $(this).val(), $("#txt-search").val() + "");
    });

    // 검색 버튼
    $("#btn-search").on("click", function(e) {
        e.preventDefault();
        fnPage(0, $("#sel-pageSize").val(), $("#txt-search").val() + "");
    });

    // 서버보내기
    $("#btn-sel-send").on("click", function(e) {
        e.preventDefault();
        let list = cookieRead(window.CUR_LIST);
        let checkedList = [];
        $("input:checkbox[name='chk-tr']").each(function(){
            if($(this).is(":checked") == true) {
                let id = $(this).attr("id").split("_")[1];
                let boardItem = list
                    .filter(b => b.id+"" == id)
                    .map(b => {
                        return {title: b.title, content: b.content}
                    })[0];
                checkedList.push(boardItem);
                ajax("/api/board", "POST", boardItem).done(function(result, status, responseObj){
                    $("#btn-search").click();
                });
            }
        });
        console.log(checkedList);
    });

    // 선택한 값 가져와서 삭제하기.
    $("#btn-sel-remove").on("click", function(e) {
        e.preventDefault();
        let checkedList = [];
        let list = cookieRead(window.CUR_LIST);
        $("input:checkbox[name='chk-tr']").each(function(){
            if($(this).is(":checked") == true) {
                let id = $(this).attr("id").split("_")[1];
                checkedList.push(id);
            }
        });
        console.log(checkedList);
        for (let i=0; i<list.length; i++) {
            for (let j=0; j<checkedList.length; j++) {
                if (list[i].id+"" === checkedList[j]) {
                    // 삭제
                    list.remove(i);
                }
            }
        }
        // console.log(list);
        cookieSave(window.CUR_LIST, list);
        tableDraw(list);
    });

    // 전체선택
    $("#chk_all").on("click", function(e) {
        //만약 전체 선택 체크박스가 체크된상태일경우
        if($("#chk_all").prop("checked")) {
            //해당화면에 전체 checkbox들을 체크해준다
            $("input[type=checkbox]").prop("checked",true);
        } else {
            //해당화면에 모든 checkbox들의 체크를해제시킨다.
            $("input[type=checkbox]").prop("checked",false);
        }
    });

    // 수정
    $("#btn-edit").on("click", function(e) {
        e.preventDefault();
        // input 활성화
        enableDetailInput();
        window.BTN_EDIT_FLAG = true;
        $("#txt-error-msg").css("display", "block");

    });

    // 삭제
    $("#btn-remove").on("click", function(e) {
        e.preventDefault();
        if(confirm("정말로 삭제할까요?")) {
            setTimeout(function(){
                enableDetailInput();
                window.BTN_EDIT_FLAG = false;
                $("#btn-comp").click();
            }, 1);
        }
        disableDetailInput();
    });

    // 완료
    $("#btn-comp").on("click", function(e) {
        e.preventDefault();
        // ==================================================
        // 변수 선언
        // ==================================================
        let id = $("#txt-detail-id").val();
        let title = $("#txt-detail-title").val();
        let content = $("#txt-detail-content").val();
        let list = cookieRead(window.CUR_LIST);
        let item = {id: id, title: title, content:content};
        let editList = list.filter(b=>b.id !== item.id*1);
        // ==================================================
        // 버튼 삭제, 수정 정의 플러그
        // ==================================================
        if(window.BTN_EDIT_FLAG === true) {
            // ==================================================
            // 중복 체크
            // ==================================================
            if(duplicateCheck(list)){
                $("#txt-error-msg").html("이미 값이 존재합니다.");
                return;
            }
            editList.push(item);
        }
        // $("#txt-error-msg").html(JSON.stringify(item));

        // ==================================================
        // 변경된 리스트를 로컬스토리지 저장
        // ==================================================
        cookieSave(window.CUR_LIST, editList);
        // ==================================================
        // 입력텍스트(id, title, content) 비활성화
        // ==================================================
        disableDetailInput();
        // ==================================================
        // 목록 페이지 탭 이동
        // ==================================================
        openTab('list');
        // ==================================================
        // 목록 페이지 데이터 재출력
        // ==================================================
        listData();
    });

    // 임시저장
    $("#btn-save").on("click", function(e) {
        e.preventDefault();
        $("#txt-error-msg").css("display", "block");
        // ==================================================
        // 변수 선언
        // ==================================================
        let title = $("#txt-input-title").val();
        let content = $("#txt-input-content").val();
        let list = cookieRead(window.CUR_LIST);
        // ==================================================
        // 중복 체크
        // ==================================================
        if(duplicateCheck(list)) {
            $("#txt-error-msg").html("이미 값이 존재합니다.");
            return;
        }
        // ==================================================
        // 값 추가
        // ==================================================
        let item = {id: `${guid()}<임시번호>`, title: title, content:content};
        list.push(item);
        // $("#txt-error-msg").html(JSON.stringify(item));
        // ==================================================
        // 변경된 리스트를 로컬스토리지 저장
        // ==================================================
        cookieSave(window.CUR_LIST, list);
        // ==================================================
        // 목록 페이지 탭 이동
        // ==================================================
        openTab('list');
        // ==================================================
        // 목록 페이지 데이터 재출력
        // ==================================================
        listData();

    });
    // 취소
    $("#btn-cancel").on("click", function(e) {
        e.preventDefault();
    });

    // 태이블 행 마우스 모양
    // $("#tbl-result tr").hover(function() {
    //     $(this).css("cursor","pointer");
    // }, function() {
    //     $(this).css("cursor","default");
    // });
}

function guid() {
    function s4() {
        return ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
    // return Math.floor(Math.random() * 1000) + 1;
}

function cbBoardList(list=[], rowNumber=1) {
    if (list.length === 0) {
        for(let i=0; i<rowNumber; i++) {
            list.push({
                id: (i+1),
                title: "검색된 제목이 없습니다.",
                content: "검색된 내용도 없습니다."
            });
        }
        return list;
    }
    return list;
}

function fnPage(page=0, size=1, title="") {
    ajax(`/api/boardPage`, "GET", {
        page: page, size: size, title: title
    }).done(function(result, status, responseObj) {
        console.log(result, status, responseObj);
        let content = result.content;
        let totalPages = result.totalPages;
        let pageable = result.pageable;
        cookieSave( window.CUR_LIST, cbBoardList(content));
        tableDraw(cbBoardList(content));
        pagingDraw(pageable, totalPages, title+"");
    });
}

function pagingDraw(pageable, totalPages, title) {
    // ==================================================
    // 페이징 변수 선언
    // ==================================================
    let currPageNo = pageable.pageNumber;
    let pageSize = pageable.pageSize;
    let viewPageNo = 1;
    let pageHtml = "";

    // ==================================================
    // 페이징 html 태그 만들기
    // ==================================================
    pageHtml += `<a pageInfo='${0}|${pageSize}|${title}' class="w3-button a-pageNo">«</a>`;
    for(let i=0; i<totalPages; i++) {
        if (i === currPageNo) {
            pageHtml += `<a pageInfo='${(viewPageNo-1)}|${pageSize}|${title}' class="w3-button a-pageNo w3-green">${viewPageNo}</a>`;
        } else {
            pageHtml += `<a pageInfo='${(viewPageNo-1)}|${pageSize}|${title}' class="w3-button  a-pageNo">${viewPageNo}</a>`;
        }
        viewPageNo++;
    }
    pageHtml += `<a pageInfo='${totalPages-1}|${pageSize}|${title}' class="w3-button a-pageNo">»</a>`;

    // ==================================================
    // 페이징 html 그리기
    // ==================================================
    $("#div-page").empty();
    $("#div-page").html(pageHtml);

    // ==================================================
    // 페이징 이벤트 만들기
    // ==================================================
    $(".a-pageNo").on("click", function(e){
        let pageInfo = $(this).attr("pageInfo").split("|");
        let cur = pageInfo[0];
        let size = pageInfo[1];
        let title = pageInfo[2];
        fnPage(cur, size, title);
    });
}

function tableDraw(list) {
    let tblResult = $("#tbl-result");
    tblResult.empty();
    list.map(b => {
        tblResult.append(`
                <tr>
                    <td><input class="w3-check" id="chk_${b.id}" name="chk-tr" type="checkbox"></td>
                    <td><button class="w3-btn w3-black w3-round" name="btn-tr-detail">상세[${b.id}]</button></td>
                    <td>${b.title}</td>
                    <td>${b.content}</td>
                </tr>
            `);
    });

    // 행 클릭: 부득이 하게 뺌뺌
   $("button[name=btn-tr-detail]").on("click", function(e) {
        let idx = $(this).closest('tr').prevAll().length;
        openTab("detail"); // 탭 이동
        detailData(cookieRead( window.CUR_LIST)[idx]); // 값 설정
    });
}

function listData() {
    tableDraw(cookieRead(window.CUR_LIST));
    $("input[type=checkbox]").prop("checked", false);
    // eventInit();
}

function detailData(item) {
    $("#txt-detail-id").val(item.id);
    $("#txt-detail-title").val(item.title);
    $("#txt-detail-content").val(item.content);
}

function openTab(tabName) {
    let totalTabName = document.getElementsByClassName("tabName");
    for (let i = 0; i < totalTabName.length; i++) {
        totalTabName[i].style.display = "none";
    }
    document.getElementById(tabName).style.display = "block";
    $("#txt-error-msg").css("display", "none");
}