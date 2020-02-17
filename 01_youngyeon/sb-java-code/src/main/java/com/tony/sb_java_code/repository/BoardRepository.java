package com.tony.sb_java_code.repository;

import com.tony.sb_java_code.dto.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByTitle(String title);

    List<BoardEntity> findByTitleContains(String title);

    List<BoardEntity> findByTitleLike(String title);

    List<BoardEntity> findByTitleAndContent(String title, String content);

    @Query(value = "select id, title, content from BOARD_ENTITY  where title like :title%  ", nativeQuery=true)
    List<BoardEntity> findMyTitle(@Param("title") String title);

    // 페이징 처리
    Page<BoardEntity> findBoardEntitiesBy(Pageable pageable);

}
