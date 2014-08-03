package com.test.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import java.lang.Float;
import java.lang.Integer;
import java.lang.String;

@Entity(value = "location", noClassnameStored = true)
class Location {
	@Id
	private Integer accountId = 0;
	private Float latitude = 0f;
	private Float longitude = 0f;
	private String mark = "";

	public Integer getAccountId(){
		return accountId;
	}

	public Float getLatitude(){
		return latitude;
	}

	public Float getLongitude(){
		return longitude;
	}

	public String getMark(){
		return mark;
	}

	public void setAccountId(Integer accountId){
		this.accountId = accountId;
	}

	public void setLatitude(Float latitude){
		this.latitude = latitude;
	}

	public void setLongitude(Float longitude){
		this.longitude = longitude;
	}

	public void setMark(String mark){
		this.mark = mark;
	}
}