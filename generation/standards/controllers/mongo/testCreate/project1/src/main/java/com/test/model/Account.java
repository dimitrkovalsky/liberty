package com.test.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;

@Entity(value = "account", noClassnameStored = true)
class Account {
	@Id
	private Integer internalId = 0;
	private String androidId = "";
	private Date created = null;

	public Integer getInternalId(){
		return internalId;
	}

	public String getAndroidId(){
		return androidId;
	}

	public Date getCreated(){
		return created;
	}

	public void setInternalId(Integer internalId){
		this.internalId = internalId;
	}

	public void setAndroidId(String androidId){
		this.androidId = androidId;
	}

	public void setCreated(Date created){
		this.created = created;
	}
}