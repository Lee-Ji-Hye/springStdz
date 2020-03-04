package com.pjt.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Task")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tskId;            // 할 일 시퀀스
	private Long cmntyId;          // 소속단체 시퀀스
	private Long fldId;            // 분야 시퀀스
	private String tskDes;         // 할 일 명
	private Date tskRegDt;         // 할 일 등록일시
	private Date tskModDt;         // 할 일 수정일시
	private Date tskDueDt;         // 할 일 마감일시
	private Date tskComDt;         // 할 일 완료일시
}
