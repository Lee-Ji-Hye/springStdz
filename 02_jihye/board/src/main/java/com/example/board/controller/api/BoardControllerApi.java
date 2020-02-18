package com.example.board.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.board.service.BoardService;
import com.example.board.vo.BoardVO;
import com.example.board.vo.ResultVO;

@RestController
@RequestMapping("/api")
public class BoardControllerApi {
	
	@Autowired
	BoardService boardService;

	@PostMapping(value="/board")
	public ResultVO insertBoeard(@ModelAttribute  BoardVO vo) {
		ResultVO result = boardService.insertBoard(vo);
		return result;
	}
}
