package com.pjt.domain;

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
@Table(name="Community")
public class Community {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cmntyId;
	private String cmntyNm;
	private String cmntyDay;
	
}
