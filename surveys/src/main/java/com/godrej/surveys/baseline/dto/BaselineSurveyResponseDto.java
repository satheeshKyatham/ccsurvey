package com.godrej.surveys.baseline.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.godrej.surveys.dto.BaseDto;

/**
 * 
 * @author Vivek Birdi
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaselineSurveyResponseDto extends BaseDto {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6632452925302161374L;
	private String emailStatus;
	@JsonProperty("ImportStatus")
	private String importStatus;
	@JsonProperty("ImportCount")
	private int importCount;
	private BaselineSurveyErrorDto error;
	private String status;
	
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	public String getImportStatus() {
		return importStatus;
	}
	public void setImportStatus(String importStatus) {
		this.importStatus = importStatus;
	}
	public int getImportCount() {
		return importCount;
	}
	public void setImportCount(int importCount) {
		this.importCount = importCount;
	}
	public BaselineSurveyErrorDto getError() {
		return error;
	}
	public void setError(BaselineSurveyErrorDto error) {
		this.error = error;
	}	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "RMSurveyResponseDto [emailStatus=" + emailStatus + ", importStatus=" + importStatus + ", importCount="
				+ importCount + ", error=" + error + "]" + ", status=" + status + "]";
	}
	
	
}
