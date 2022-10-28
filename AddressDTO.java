package com.tougher.app.v1.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddressDTO {
	private Long id;
	private String unitNumber;
	private String street;
	private String suburb;
	@NotNull
	private String postcode;
}
