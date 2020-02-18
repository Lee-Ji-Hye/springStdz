package com.example.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.board.vo.BoardVO;

@Mapper
public interface BoardDAO {
	
	//조회
	@Select("SELECT * FROM BOARD WHERE user_id = #{userId}")
	public List<BoardVO> selectBoardList(BoardVO vo);
	
	@Insert("INSERT INTO BOARD (user_id, title, content) "
			+ "VALUES(#{userId},#{title},#{content})")
	public int insertBoard(BoardVO vo);
	

}
