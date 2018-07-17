/**
 * 
 */
package com.vag.base.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.vag.pfm.annotation.EntityProperty;

/**
 * @author GOBINATH A
 *
 */
public class UserVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -715788948714242103L;
	
	@EntityProperty(columnName="userId")
	private int userId;
	
	@EntityProperty(columnName="userName")
	private String userName;
	
	@EntityProperty(columnName="password")
	private String password;
	
	@EntityProperty(columnName="firstName")
	private String firstName;
	
	@EntityProperty(columnName="lastName")
	private String lastName;
	
	@EntityProperty(columnName="emailId")
	private String emailId;
	
	@EntityProperty(columnName="aadharNo")
	private int aadharNo;
	
	@EntityProperty(columnName="mobileNo")
	private int mobileNo;
	
	@EntityProperty(columnName="isActive")
	private String isActive;
	
	@EntityProperty(columnName="createdBy")
	private int createdBy;
	
	@EntityProperty(columnName="createdOn")
	private Timestamp createdOn;
	
	@EntityProperty(columnName="lastUpdatedBy")
	private int lastUpdatedBy;
	
	@EntityProperty(columnName="lastUpdatedOn")
	private Timestamp lastUpdatedOn;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(int aadharNo) {
		this.aadharNo = aadharNo;
	}

	public int getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(int mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public int getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(int lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
