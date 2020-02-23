package com.tony.sb_java_code.service;

import com.tony.sb_java_code.dto.BoardEntity;
import com.tony.sb_java_code.dto.BoardVo;
import com.tony.sb_java_code.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class BoardService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BoardRepository boardRepository;

    @PostConstruct
    public void initBoardEntity() {
        // 더미 데이터 저장
        // 페이징 처리를 위해 100개 이상 데이터를 쌓아 본다.
        for(int i=0; i<100; i++) {
           boardRepository.save(BoardEntity.builder()
              .title("테스트 제목" + i).content("테스트 콘텐츠" + i).build());
        }
        findNativeXmlQuery(1L);

        boardPaging();
    }

    /**
     * 페이징 처리
     */
    public void boardPaging() {
        boardRepository
                .findBoardEntitiesByTitleContains(PageRequest.of(1, 10), "")
                .forEach(b -> log.info(b.toString()));
    }

    public Page<BoardVo> boardPage(Pageable pageable, String title) {
       return boardRepository.findBoardEntitiesByTitleContains(pageable, title).map(
          b-> {
             BoardVo boardVo = new BoardVo();
             boardVo.setContent(b.getContent());
             boardVo.setId(b.getId());
             boardVo.setTitle(b.getTitle());
             boardVo.setRegDateTime(LocalDateTime.now());
             boardVo.setRegWriter("admin");
             return boardVo;
          }
       );
    }

    /**
     * xml로 sql을 불러와서 처리하는 케이스
     * @param id
     */
    public void findNativeXmlQuery(Long id) {
        String sql = "Board.findAll";
        List<BoardEntity> r1 = entityManager.createNamedQuery(sql, BoardEntity.class).getResultList();
        System.out.println(r1);

        sql = "Board.findById";
        BoardEntity r2 = entityManager.createNamedQuery(sql, BoardEntity.class)
                .setParameter("id", 1)
                .getSingleResult();
        System.out.println(r2);

//        emf.close();
    }

    /**
     * 직접 sql query를 스트링으로 박아서 처리하는 케이스
     * @param id
     * @return
     */
    public BoardVo findByIdNativeQuery(Long id) {
        String sql = "SELECT id, title, content FROM BOARD_ENTITY WHERE ID LIKE ?";
        Query nativeQuery = entityManager.createNativeQuery(sql,BoardEntity.class).setParameter(1, id);
        List<BoardEntity> resultList = nativeQuery.getResultList();
        return resultList.stream()
                .map(b -> {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setTitle(b.getTitle());
                    boardVo.setId(b.getId());
                    boardVo.setContent(b.getContent());
                    return boardVo;
                }).findFirst().get();
    }

    public List<BoardVo> findMyTitle(String title) {
        return boardRepository.findMyTitle(title)
                .stream()
                .map(b -> {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(b.getId());
                    boardVo.setTitle(b.getTitle());
                    boardVo.setContent(b.getContent());
                    boardVo.setRegDateTime(LocalDateTime.now());
                    boardVo.setRegWriter("ADMIN");
                    return boardVo;
                }).collect(Collectors.toList());
    }

    public List<BoardVo> findByTitle(String title) {
        return boardRepository.findByTitle(title)
                .stream()
                .map(b -> {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(b.getId());
                    boardVo.setTitle(b.getTitle());
                    boardVo.setContent(b.getContent());
                    boardVo.setRegDateTime(LocalDateTime.now());
                    boardVo.setRegWriter("ADMIN");
                    return boardVo;
                }).collect(Collectors.toList());
    }

    public List<BoardVo> findByTitleContains(String title) {
        return boardRepository.findByTitleContains(title)
                .stream()
                .map(b -> {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(b.getId());
                    boardVo.setTitle(b.getTitle());
                    boardVo.setContent(b.getContent());
                    boardVo.setRegDateTime(LocalDateTime.now());
                    boardVo.setRegWriter("ADMIN");
                    return boardVo;
                }).collect(Collectors.toList());
    }

    public List<BoardVo> findByTitleLike(String title) {
        return boardRepository.findByTitleLike("%"+title+"%")
                .stream()
                .map(b -> {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(b.getId());
                    boardVo.setTitle(b.getTitle());
                    boardVo.setContent(b.getContent());
                    boardVo.setRegDateTime(LocalDateTime.now());
                    boardVo.setRegWriter("ADMIN");
                    return boardVo;
                }).collect(Collectors.toList());
    }

    public List<BoardVo> finaAll() {
        return boardRepository.findAll().stream()
                .map(b -> {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(b.getId());
                    boardVo.setTitle(b.getTitle());
                    boardVo.setContent(b.getContent());
                    boardVo.setRegDateTime(LocalDateTime.now());
                    boardVo.setRegWriter("ADMIN");
                    return boardVo;
                }).collect(Collectors.toList());
    }

    public void save(BoardVo boardVo) {
        // 데이터 변환
        BoardEntity boardEntity = modelMapper.map(boardVo, BoardEntity.class);
        log.info("boardVo: " + boardVo.toString());
        log.info("boardEntity: " + boardEntity.toString());
        boardRepository.save(boardEntity);
    }

    public void edit(BoardVo boardVo) {
        BoardEntity boardEntity1 = boardRepository.findById(boardVo.getId()).get();
        boardEntity1.setContent(boardVo.getContent());
        boardEntity1.setTitle(boardVo.getTitle());
        boardRepository.save(boardEntity1);
    }

    public void remove(BoardVo boardVo) {
        BoardEntity boardEntity = modelMapper.map(boardVo, BoardEntity.class);
        boardRepository.delete(boardEntity);
    }
}
