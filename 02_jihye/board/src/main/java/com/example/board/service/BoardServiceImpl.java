package com.example.board.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.board.consts.ViewTitle;
import com.example.board.dao.BoardDAO;
import com.example.board.vo.BoardVO;
import com.example.board.vo.ResultVO;


@Service
public class BoardServiceImpl implements BoardService {

	@Autowired 
	BoardDAO boardDao;
	
	@Override
	public void selectCafeList(HttpServletRequest req, Model model) {
		BoardVO vo = BoardVO.builder()
				.userId(req.getParameter("userId"))
				.build();		
		
		List<BoardVO> list = boardDao.selectBoardList(vo);
		
		model.addAttribute("tagName", ViewTitle.BOARDLIST.getValue());
		model.addAttribute("list", list);
	}

	@SuppressWarnings("null")
	@Override
	public ResultVO insertBoard(BoardVO vo) {
		System.out.println("ss");
		System.out.println(vo.getUserId());
		
		int cnt = boardDao.insertBoard(vo);
		//{ code: 1, msg: success, body: {totalCount: 1}}
		
		ResultVO result = null;
		if(cnt > 0) {
			result = new ResultVO();
			System.out.println("11111111111");
			result.setUniqKey(vo.getUserId());
			System.out.println("222222222222");
			result.setMsg("success");
			result.setTotalCnt(cnt);
		}
		
		return result;
	}

}
