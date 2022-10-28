package com.tougher.app.v1.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import com.tougher.app.v1.model.enums.Gender;
import com.tougher.app.v1.model.ref.Hobby;
import com.tougher.app.v1.model.ref.Occupation;

import lombok.Data;

@Entity
@Table(name = "Employee")
//See how to use LIKE in query
@NamedQuery(name = "Employee.findFirstName", query = "SELECT e FROM Employee e WHERE lower(e.firstName) LIKE concat('%', lower(:name),'%') ")
@Data
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@NotBlank
	@Column(name = "first_name", length = 20)
	private String firstName;

	@Column(name = "last_name", length = 20)
	private String lastName;

	@Column(length = 1)
	private Gender gender;

	@Column(columnDefinition = "DATE")
	private LocalDate birthday;

	// Bidirectional OneToOne - Shared PK
	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
	private PersonalDetail personalDetail;

	// Bidirectional OneToOne - FK on child
	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
	private ProfessionalDetail professionalDetail;

	// Unidirectional OneToOne - FK on parent
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "work_detail_id", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_WORK_DETAIL"))
	private WorkDetail workDetail;

	// Unidirectional ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "occupation_id", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_OCCUPATION"))
	private Occupation occupation;

	// Unidirectional ManyToMany
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Employee_Hobby", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "hobby_id"), foreignKey = @ForeignKey(name = "FK_EMPLOYEE_HOBBY_EMPLOYEE"), inverseForeignKey = @ForeignKey(name = "FK_EMPLOYEE_HOBBY_HOBBY"))
	private List<Hobby> hobbies = new ArrayList<Hobby>();

	// Bidirectional OneToMany
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<Address> addresses = new ArrayList<Address>();

	// Unidirectional OneToMany
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Employee_Phone", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "phone_id"), foreignKey = @ForeignKey(name = "FK_EMPLOYEE_PHONE_EMPLOYEE"), inverseForeignKey = @ForeignKey(name = "FK_EMPLOYEE_PHONE_PHONE"))
	private List<Phone> phones = new ArrayList<Phone>();
}
