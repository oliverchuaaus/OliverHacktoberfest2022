package com.tougher.app.v1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "Personal_Detail")
@Data
public class PersonalDetail {

	@Id
	@Column(name = "employee_id")
	private Long id;

	@Column(length = 20)
	private String requiredDetail;

	@Column(length = 20)
	private String detail;

	@OneToOne
	@MapsId
	@JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "FK_PERSONAL_DETAIL_EMPLOYEE"))
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Employee employee;
}
