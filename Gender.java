package com.tougher.app.v1.model.enums;

import lombok.Getter;

@Getter
public enum Gender {
	MALE("M", "Male"), FEMALE("F", "Female"), INDETERMINATE("X", "Indeterminate");

	String code;
	String desc;

	Gender(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
