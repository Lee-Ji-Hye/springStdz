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

function eventInit() {

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
            // 전체선택 체크박스가 해제된 경우
        } else {
            //해당화면에 모든 checkbox들의 체크를해제시킨다.
            $("input[type=checkbox]").prop("checked",false);
        }
    });

    // 검색
    $("#btn-search").on("click", function(e) {
        e.preventDefault();
        let searchTxt = $("#txt-search").val();
        search(searchTxt);
    });
    // 수정
    $("#btn-edit").on("click", function(e) {
        e.preventDefault();
        // input 활성화
        enableDetailInput();
    });
    // 삭제
    $("#btn-remove").on("click", function(e) {
        e.preventDefault();
        // 실제로 서버로 보낸다. 단 물어본다.
        if(confirm("서버로 보내면 진짜 지우는데 책임지겠습니까?")) {
            // 서버로 ..
        }
    });
    // 완료
    $("#btn-comp").on("click", function(e) {
        e.preventDefault();
        // 실제로 서버로 보낸다.
        disableDetailInput();
    });
    // 저장
    $("#btn-save").on("click", function(e) {
        e.preventDefault();
        $("#txt-error-msg").css("display", "block");
        let title = $("#txt-input-title").val();
        let content = $("#txt-input-content").val();
        let list = cookieRead(window.CUR_LIST);
        if(list.filter(b=>b.title===title).length > 0){
            $("#txt-error-msg").html("이미 값이 존재합니다.");
            return;
        }
        let item = {id: `${guid()}<임시번호>`, title: title, content:content};
        list.push(item);
        $("#txt-error-msg").html(JSON.stringify(item));
        cookieSave(window.CUR_LIST, list);
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

function search(title="") {
    ajax("/api/boardContains", "GET", {title:title}).done(function(result, status, responseObj) {
        // console.log(result, status, responseObj);
        cookieSave( window.CUR_LIST, cbBoardList(result.body));
        tableDraw(cbBoardList(result.body));
        // eventInit();
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