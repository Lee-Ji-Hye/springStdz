package com.example.board.consts;

public enum ViewTitle {
    BOARDLIST("[게시판] 리스트"), BOARDINSERT("[게시판] 등록");
    String value;
    
    private ViewTitle(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

}
