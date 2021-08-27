package com.godrej.surveys.resident.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.godrej.surveys.dto.BaseDto;

/**
 * RM Survey RequestDto
 * @author Vivek Birdi
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResidentSurveyRequestDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2560278431791965096L;
	private String surveyID;
	private String sendEmail;
	private String sendSMS;
	private String smsTemplateID;
	private String invitationTemplateID;
	private List<ResidentSurveyContactDto> contacts;

	public String getSurveyID() {
		return surveyID;
	}
	public void setSurveyID(String surveyID) {
		this.surveyID = surveyID;
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
	public String getSmsTemplateID() {
		return smsTemplateID;
	}
	public void setSmsTemplateID(String smsTemplateID) {
		this.smsTemplateID = smsTemplateID;
	}
	public String getInvitationTemplateID() {
		return invitationTemplateID;
	}
	public void setInvitationTemplateID(String invitationTemplateID) {
		this.invitationTemplateID = invitationTemplateID;
	}
	public List<ResidentSurveyContactDto> getContacts() {
		return contacts;
	}
	public void setContacts(List<ResidentSurveyContactDto> contacts) {
		this.contacts = contacts;
	}
	
}
