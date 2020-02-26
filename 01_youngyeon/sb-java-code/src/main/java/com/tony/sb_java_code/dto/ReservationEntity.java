package com.tony.sb_java_code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationEntity {

    public void test() {
        List list = Arrays.asList(1,2,3,4);
        list.add("1");
        list.get(0);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String consultingId; // 상담자 아이디

    private String number; // 예약번호

    private String gb; // 예약구분 = 폰종류, 가격정책

    private int evaluation; // 평가

    private ReserState state; // 대기, 진행중, 완료

    private String startDateTime; // 예약 시작 시간

    private String endDateTime; // 예약 종료 시간

    private long totalTimeCount; // 총 예약 소요시간 = 종료시간 - 시작시간

    private LocalDateTime regDate; // 등록 시간

    private String regName; // 등록자

    private LocalDateTime upDate; // 수정 시간

    private String upName; // 수정자

}
