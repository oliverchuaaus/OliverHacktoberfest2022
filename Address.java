package com.tougher.app.v1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name = "Address")
@Data
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(length = 20)
	private String unitNumber;
	@Column(length = 20)
	private String street;
	@Column(length = 20)
	@Size(max = 30)
	private String suburb;
	@Column(length = 4)
	@NotNull
	private String postcode;

	@ManyToOne
	@JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "FK_ADDRESS_EMPLOYEE"))
	private Employee employee;
}
