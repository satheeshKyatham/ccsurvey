package com.godrej.surveys.rm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.godrej.surveys.dto.BaseDto;
/**
 * Contact details of RM Survey
 * @author Vivek Birdi
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RMSurveyContactDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2165045093833715656L;
	private String firstName;
	private String lastName;
	private String mobile;
	private String segmentCode;
	private String transactionDate; // format dd/mm/yyyy
	private String email;
	private String field20;
	private String name;
	private String bookingDate;
	private String field1;
	private String field15;
	private String sentDate;
	private boolean sentStatus;
	
	private String field2;
	private String field4;
	private String field6;
	private String field8;
	private String field9;
	private String field11;
	private String field13;
	private String field14;
	private String field16;
	private String field18;
	private String propertyName;
	
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSegmentCode() {
		return segmentCode;
	}
	public void setSegmentCode(String segmentCode) {
		this.segmentCode = segmentCode;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getField20() {
		return field20;
	}
	public void setField20(String field20) {
		this.field20 = field20;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField15() {
		return field15;
	}
	public void setField15(String field15) {
		this.field15 = field15;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public boolean isSentStatus() {
		return sentStatus;
	}
	public void setSentStatus(boolean sentStatus) {
		this.sentStatus = sentStatus;
	}

	
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		this.field6 = field6;
	}

	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}

	public String getField11() {
		return field11;
	}

	public void setField11(String field11) {
		this.field11 = field11;
	}

	public String getField13() {
		return field13;
	}

	public void setField13(String field13) {
		this.field13 = field13;
	}

	public String getField14() {
		return field14;
	}

	public void setField14(String field14) {
		this.field14 = field14;
	}

	public String getField16() {
		return field16;
	}

	public void setField16(String field16) {
		this.field16 = field16;
	}
	public String getField18() {
		return field18;
	}
	public void setField18(String field18) {
		this.field18 = field18;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((field20 == null) ? 0 : field20.hashCode());
		return result;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RMSurveyContactDto other = (RMSurveyContactDto) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (field20 == null) {
			if (other.field20 != null)
				return false;
		} else if (!field20.equals(other.field20))
			return false;
		return true;
	}

	
}
