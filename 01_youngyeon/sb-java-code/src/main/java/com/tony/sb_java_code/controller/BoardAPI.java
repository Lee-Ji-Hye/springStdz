package com.tony.sb_java_code.controller;

import com.tony.sb_java_code.dto.BoardVo;
import com.tony.sb_java_code.dto.ResultDto;
import com.tony.sb_java_code.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping("/api")
public class BoardAPI {

    @Autowired
    BoardService service;

    /**
     * 전체검색
     * Default: XML
     * http://localhost:8080/api/boardJsonXml => xml
     * http://localhost:8080/api/boardJsonXml.json => json
     * http://localhost:8080/api/boardJsonXml.xml => xml
     * @param boardVo
     * @return
     */
    @GetMapping(value = "/boardJsonXml", produces = {APPLICATION_JSON_VALUE,APPLICATION_XML_VALUE} )
    public ResponseEntity<?> getXmlJsonBoard(@ModelAttribute BoardVo boardVo) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.finaAll()), HttpStatus.OK);
    }

    /**
     * 게시판 전체 JSON 방식으로 검색
     * @param boardVo
     * @return
     */
    @GetMapping(value = "/board", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoard(@ModelAttribute BoardVo boardVo) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.finaAll()), HttpStatus.OK);
    }

    /**
     * 게시판 제목중에 하나라도 포함되어 있는 값 검색
     * 받는값 그대로 넘겨도 알아서 %%를 붙여서 DBMS로 값을 던진다.
     * 와일드 카드로 제목이 하나라도 포함되어 있으면 검색됨
     * @param boardVo
     * @param title
     * @return
     */
    @GetMapping(value = "/boardContains", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoardContains(@ModelAttribute BoardVo boardVo, String title) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.findByTitleContains(title)), HttpStatus.OK);
    }

    /**
     * 게시판 제목중에 하나라도 포함되어 있는 값 검색
     * 서비스나 받을때 %를 내가 직접 넣어서 DBMS로 값을 던져야 한다.
     * 주의! 휴먼에러날 우려가 있으니 왠만해서는 contains로 해결하는게~
     * @param boardVo
     * @param title
     * @return
     */
    @GetMapping(value = "/boardLike", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoardLike(@ModelAttribute BoardVo boardVo, String title) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.findByTitleLike(title)), HttpStatus.OK);
    }

    /**
     * 게시판 제목으로 검색
     * 모든 제목이 완벽하게 일치해야 검색됨
     * @param boardVo
     * @param title
     * @return
     */
    @GetMapping(value = "/board/{title}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoardTitle(@ModelAttribute BoardVo boardVo, @PathVariable String title) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.findByTitle(title)), HttpStatus.OK);
    }
    @GetMapping(value = "/board/my/{title}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoardMyTitle(@ModelAttribute BoardVo boardVo, @PathVariable String title) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.findMyTitle(title)), HttpStatus.OK);
    }
    @GetMapping(value = "/board/id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoardById(@ModelAttribute BoardVo boardVo, @PathVariable Long id) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.findByIdNativeQuery(id)), HttpStatus.OK);
    }

    /**
     * 게시판 목록
     * jackson-dataformat-xml: xml포맷팅 빈 등록,
     * jaxb-api: 자바9부터 빠진 xml함수 의존성관리
     * 참고: http://zetcode.com/springboot/restxml/
     * @param boardVo
     * @return
     */
    @GetMapping(value = "/boardXml", produces = APPLICATION_XML_VALUE)
    public ResponseEntity<?> getXmlBoard(@ModelAttribute BoardVo boardVo) {
        return new ResponseEntity<ResultDto>(getResultBodyBuild(service.finaAll()), HttpStatus.OK);
    }

    /**
     * 게시판 내용 저장
     * http post localhost:8080/api/board title="누구냐 넌" content="설명을  해줘"
     * @param boardVo
     * @return
     */
    @PostMapping("/board")
    public ResponseEntity<ResultDto> postBoard(@RequestBody BoardVo boardVo) {
        service.save(boardVo);
        return ResponseEntity.ok().body(getResultBodyBuild());
    }

    private ResultDto getResultBodyBuild() {
        return ResultDto.builder()
                .code("COM001")
                .msg("SUCCESS")
                .body("").build();
    }
    private ResultDto getResultBodyBuild(Object body) {
        return ResultDto.builder()
                .code("COM001")
                .msg("SUCCESS")
                .body(body).build();
    }

    /**
     * 게시판 내용 수정
     * @param boardVo
     * @return
     */
    @PutMapping("/board")
    public ResponseEntity<ResultDto> putBoard(@RequestBody BoardVo boardVo) {
        service.edit(boardVo);
        return ResponseEntity.ok().body(getResultBodyBuild());
    }

    /**
     * 게시판 삭제
     * @param boardVo
     * @return
     */
    @DeleteMapping(value = "/board")
    public ResponseEntity<ResultDto> delBoard(@RequestBody BoardVo boardVo) {
        service.remove(boardVo);
        return ResponseEntity.ok().body(getResultBodyBuild());
    }
}
