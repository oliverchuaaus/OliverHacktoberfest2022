package com.tougher.app.v1.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.tougher.app.v1.dto.ref.HobbyDTO;
import com.tougher.app.v1.dto.ref.OccupationDTO;

import lombok.Data;

@Data
public class EmployeeDTO {

	private Long id;
	@NotNull
	private String firstName;

	private String lastName;

	private String gender;

	private LocalDate birthday;

	private OccupationDTO occupation;

	private PersonalDetailDTO personalDetail;

	private ProfessionalDetailDTO professionalDetail;

	private WorkDetailDTO workDetail;
	@Valid
	private List<AddressDTO> addresses = new ArrayList<AddressDTO>();

	private List<PhoneDTO> phones = new ArrayList<PhoneDTO>();

	private List<HobbyDTO> hobbies = new ArrayList<HobbyDTO>();

}
