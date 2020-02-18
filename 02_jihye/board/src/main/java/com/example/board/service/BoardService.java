package com.example.board.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.example.board.vo.BoardVO;
import com.example.board.vo.ResultVO;

public interface BoardService {
	public void selectCafeList(HttpServletRequest req, Model model);
	
	
	
	
	
	//---------------------------
	public ResultVO insertBoard(BoardVO vo);
}
