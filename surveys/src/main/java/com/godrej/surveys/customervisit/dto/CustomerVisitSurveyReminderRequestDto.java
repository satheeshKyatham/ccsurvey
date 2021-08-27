package com.godrej.surveys.customervisit.dto;

import com.godrej.surveys.dto.BaseDto;

public class CustomerVisitSurveyReminderRequestDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String surveyID;
	private String sendEmail;
	private String sendSMS;
	private String invitationTemplateID;
	private String smsTemplateID;
	private CustomerVisitSurveyReminderContactDto contact;
	public String getSurveyID() {
		return surveyID;
	}
	public void setSurveyID(String surveyID) {
		this.surveyID = surveyID;
	}
	public String getInvitationTemplateID() {
		return invitationTemplateID;
	}
	public void setInvitationTemplateID(String invitationTemplateID) {
		this.invitationTemplateID = invitationTemplateID;
	}
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getSendSMS() {
		return sendSMS;
	}
	public void setSendSMS(String sendSMS) {
		this.sendSMS = sendSMS;
	}
	public CustomerVisitSurveyReminderContactDto getContact() {
		return contact;
	}
	public void setContact(CustomerVisitSurveyReminderContactDto contact) {
		this.contact = contact;
	}
	public String getSmsTemplateID() {
		return smsTemplateID;
	}
	public void setSmsTemplateID(String smsTemplateID) {
		this.smsTemplateID = smsTemplateID;
	}

}
