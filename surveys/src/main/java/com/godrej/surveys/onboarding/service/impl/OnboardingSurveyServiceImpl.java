package com.godrej.surveys.onboarding.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.godrej.surveys.contactlog.dao.ContactLogDao;
import com.godrej.surveys.dao.ProjectDao;
import com.godrej.surveys.dto.BookingParam;
import com.godrej.surveys.dto.ErrorDto;
import com.godrej.surveys.dto.ProjectDto;
import com.godrej.surveys.dto.ResponseDto;
import com.godrej.surveys.onboarding.controller.SpringSftpController;
import com.godrej.surveys.onboarding.dao.OnboardingSurveyRequestDao;
import com.godrej.surveys.onboarding.dto.OnboardingSurveyContactDto;
import com.godrej.surveys.onboarding.dto.OnboardingSurveyExcelHelper;
import com.godrej.surveys.onboarding.dto.OnboardingSurveyReminderContactDto;
import com.godrej.surveys.onboarding.dto.OnboardingSurveyReminderResponseDto;
import com.godrej.surveys.onboarding.dto.OnboardingSurveyReminderResponseWrapperDto;
import com.godrej.surveys.onboarding.dto.OnboardingSurveyResponseDto;
import com.godrej.surveys.onboarding.dto.OnboardingSurveyResponseWrapperDto;
import com.godrej.surveys.onboarding.service.OnboardingSurveyAPIService;
import com.godrej.surveys.onboarding.service.OnboardingSurveyService;
import com.godrej.surveys.service.ProjectService;
import com.godrej.surveys.util.AppConstants;
import com.godrej.surveys.util.CommonUtil;
import com.godrej.surveys.util.DateUtil;

@Service
public class OnboardingSurveyServiceImpl implements OnboardingSurveyService {

	@Autowired
	private OnboardingSurveyRequestDao surveyRequestDao;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private OnboardingSurveyAPIService rmSurveyAPI;

	@Autowired
	private ProjectDao projectDao;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ContactLogDao contactLogDao;

	@Autowired
	private SpringSftpController springSftpController;
	
	@Override
	public List<OnboardingSurveyContactDto> getContacts(String projectSfid, String fromDate, String toDate) {

		ProjectDto project = projectService.getProject(projectSfid);
		if (project == null) {
			return new ArrayList<>();
		}
		try {
			String preSurveyId= "6063132";
			project.setViewOnly("Y");
			String transactionDate = dateUtil.getCurrentDate("dd/MM/yyyy");
			project.setTransactionDate(transactionDate);
			project.setSurveyId(preSurveyId);
			String instanceId = "Onboard"+ Calendar.getInstance().getTimeInMillis();
			project.setInstanceId(instanceId);		
			Integer parkedRecords = surveyRequestDao.parkRecords(project);
			Integer repeated = contactLogDao.markDuplicate(project);
			StringBuilder countLog = new StringBuilder();
			countLog.append("Parked Records count - ").append(parkedRecords)
			.append(" Repeated Record count - ").append(repeated);
			log.info(countLog.toString());

			
			return new ArrayList<>(surveyRequestDao.getContacts(project));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ArrayList<>();
	}

	@Override
	public ResponseDto sendSurvey(String projectSfid, String fromDate, String toDate, String instanceId) {
		ProjectDto project = projectDao.getProject(projectSfid);

		String preSurveyId= "6063132";
		if (project == null) {

			StringBuilder errorMsg = new StringBuilder("No project found for sfid - ");
			errorMsg.append(projectSfid);
			if(log.isInfoEnabled()) {
				log.error(errorMsg.toString());
			}
			return new ResponseDto(true, "No project found for sfid- " + projectSfid);
		}
		project.setFromDate(fromDate);
		project.setToDate(toDate);
		project.setViewOnly("N");
		String transactionDate = dateUtil.getCurrentDate("dd/MM/yyyy");
		project.setTransactionDate(transactionDate);
		project.setSurveyId(preSurveyId);
		project.setInstanceId(instanceId);		
		Integer parkedRecords = surveyRequestDao.parkRecords(project);
		Integer repeated = contactLogDao.markDuplicate(project);
		StringBuilder countLog = new StringBuilder();
		countLog.append("Parked Records count - ").append(parkedRecords)
		.append(" Repeated Record count - ").append(repeated);
		log.info(countLog.toString());

		List<OnboardingSurveyContactDto> contacts = new ArrayList<>(surveyRequestDao.getContacts(project));
		ResponseDto response=null;
		try {
			response = processSurvey(contacts, project);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		springSftpController.sftpcon("D:\\Satheesh\\Projects\\Litmus World\\CustOnboard\\"+instanceId+".csv",AppConstants.LW_ONBOARDING_SURVEY_FOLDER_PATH);
		return response;
	}
	
	@Override
	public List<OnboardingSurveyContactDto> sendSurveyWithScheduler(String projectSfid, String fromDate, String toDate, String instanceId) {
		ProjectDto project = projectDao.getProject(projectSfid);

		String preSurveyId= "6063132";
		if (project == null) {

			StringBuilder errorMsg = new StringBuilder("No project found for sfid - ");
			errorMsg.append(projectSfid);
			if(log.isInfoEnabled()) {
				log.error(errorMsg.toString());
			}
			return null;
		}
		project.setFromDate(fromDate);
		project.setToDate(toDate);
		project.setViewOnly("N");
		String transactionDate = dateUtil.getCurrentDate("dd/MM/yyyy");
		project.setTransactionDate(transactionDate);
		project.setSurveyId(preSurveyId);
		project.setInstanceId(instanceId);		
		Integer parkedRecords = surveyRequestDao.parkRecords(project);
		Integer repeated = contactLogDao.markDuplicate(project);
		StringBuilder countLog = new StringBuilder();
		countLog.append("Parked Records count - ").append(parkedRecords)
		.append(" Repeated Record count - ").append(repeated);
		log.info(countLog.toString());

		List<OnboardingSurveyContactDto> contacts = new ArrayList<>(surveyRequestDao.getContacts(project));
		ResponseDto response=null;
		try {
			response = processSurveyWithScheduler(contacts, project);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * try { surveyRequestDao.clearExtraUpdate(project); }catch (Exception e) {
		 * log.error(e.getMessage() ,e); }
		 */		
		return contacts;
	}
	
	@SuppressWarnings("unused")
	private ResponseDto getFinalResponse(ResponseDto responsePre, ResponseDto responsePost) {
		ResponseDto response = new ResponseDto(false, "");
		boolean hasError =false;
		Integer count =0;
		List<ErrorDto> errors =  new ArrayList<>();
		if(responsePre == null && responsePost ==null) {
			return new ResponseDto(true, "No record to process");
		}
		if(responsePre !=null) {
			hasError = responsePre.isHasError();
			Integer updateCount =  (Integer)(responsePre.getData("importCount")==null?0:responsePre.getData("importCount"));
			count = count +updateCount;
			errors.addAll(responsePre.getErrors());
		}
		if(responsePost !=null) {
			hasError = !hasError?hasError:responsePost.isHasError();
			Integer updateCount =  (Integer)(responsePost.getData("importCount")==null?0:responsePost.getData("importCount"));
			count = count +updateCount;
			errors.addAll(responsePost.getErrors());
		}
		response.setHasError(hasError);
		response.addData("statusUpdateCount", count);
		response.addData("importCount", count);
		response.setErrors(errors);
		return response;
	}

	private ResponseDto processSurvey(List<OnboardingSurveyContactDto> contacts, ProjectDto project) throws Exception {
		ResponseDto response = processSurvey(contacts, project.getInstanceId());
		Integer statusUpdateCount = updateStatus(project);
		response.addData("statusUpdateCount", statusUpdateCount);
		response.addData("statusUpdateCount", 10);
		return response;

	}

	private ResponseDto processSurvey(List<OnboardingSurveyContactDto> contacts, String instanceId) throws Exception {

		if (CommonUtil.isCollectionEmpty(contacts)) {
			return new ResponseDto(true, "No contact for project ");
		}
		int totalImportCount = 0;
		ResponseDto response = new ResponseDto(false, "");
		updateContactLogs(contacts, instanceId);
		OnboardingSurveyExcelHelper excelHelper = new OnboardingSurveyExcelHelper();
		excelHelper.tutorialsToExcel(contacts,instanceId);
		
		
		/*totalImportCount = totalImportCount + getImportCount(responseChunck);
		response.addData("importCount", Integer.valueOf(totalImportCount));*/
		
		
		//Commented By Satheesh Kyahtam- Changes for Litmus World
		/*int pageSize = AppConstants.SURVEY_API_PAGE_SIZE;
		int totalImportCount = 0;
		
		if (contacts.size() <= pageSize) {
			ResponseDto responseChunck = rmSurveyAPI.post(contacts);
			if(responseChunck !=null && !responseChunck.isHasError()) {
				totalImportCount = getImportCount(responseChunck);
				response.addData("importCount", Integer.valueOf(totalImportCount));
				updateContactLogs(contacts, instanceId);
			}
			return response;
		}

		int totalContacts = contacts.size();
		int pages = totalContacts / pageSize;
		int mod = totalContacts % pageSize;
		if (mod > 0) {
			pages = pages + 1;
		}
		int startIndex = 0;
		int endIndex = 0;

		for (int i = 1; i <= pages; i++) {
			if (i == pages) {
				endIndex = ((i - 1) * pageSize + mod);
				log.info("Last Page");
			} else {
				endIndex = i * pageSize;
			}
			List<OnboardingSurveyContactDto> contactChunck = contacts.subList(startIndex, endIndex);

			
			
			ResponseDto responseChunck = rmSurveyAPI.post(contactChunck);
			if(responseChunck !=null && !responseChunck.isHasError()) {
				totalImportCount = totalImportCount + getImportCount(responseChunck);
				response.addData("importCount", Integer.valueOf(totalImportCount));
				updateContactLogs(contactChunck, instanceId);
			}
			startIndex = i * pageSize;
		}*/
		return response;
	}

	private ResponseDto processSurveyWithScheduler(List<OnboardingSurveyContactDto> contacts, ProjectDto project) throws Exception {
		ResponseDto response = processSurveyWithScheduler(contacts, project.getInstanceId());
		Integer statusUpdateCount = updateStatus(project);
		response.addData("statusUpdateCount", statusUpdateCount);
		response.addData("statusUpdateCount", 10);
		return response;

	}
	private ResponseDto processSurveyWithScheduler(List<OnboardingSurveyContactDto> contacts, String instanceId) throws Exception {

		if (CommonUtil.isCollectionEmpty(contacts)) {
			return new ResponseDto(true, "No contact for project ");
		}
		int totalImportCount = 0;
		ResponseDto response = new ResponseDto(false, "");
		updateContactLogs(contacts, instanceId);
		return response;
	}
	private Integer updateContactLogs(List<OnboardingSurveyContactDto> contactChunck, String instanceId) {
		String[] bookings = contactChunck.stream().map(contact -> contact.getName()).collect(Collectors.toList())
				.toArray(new String[0]);
		BookingParam bookingParam = new BookingParam();
		bookingParam.setBookings(bookings);
		bookingParam.setInstanceId(instanceId);
		try {
			contactLogDao.updateSentContacts(bookingParam);
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return 0;
	}

	private Integer updateStatus(ProjectDto project) {
		try {
			Integer updatedContacts = contactLogDao.markContactLogs(project);
			log.info(project.getInstanceId() + " Contact logs marked count - " + updatedContacts);
			return surveyRequestDao.updateStatusFromContactLogs(project);

			/* return surveyRequestDao.updateStatus(project); */
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return 0;
	}

	private int getImportCount(ResponseDto response) {

		if (response == null) {
			return 0;
		}
		OnboardingSurveyResponseWrapperDto rmAPIWrappedResponse = (OnboardingSurveyResponseWrapperDto) response
				.getData("rmSurveyResponse");

		if (rmAPIWrappedResponse == null) {
			return 0;
		}

		OnboardingSurveyResponseDto rmAPIResponse = rmAPIWrappedResponse.getResponse();
		if (rmAPIResponse == null) {
			return 0;
		}
		return rmAPIResponse.getImportCount();
	}

	@Override
	public ResponseDto sendReminder(ProjectDto project) {
		sendFirstReminder(project);
		sendSecondReminder(project);

		return new ResponseDto(false, "Reminders sent");
	}

	public ResponseDto sendFirstReminder(ProjectDto project) {
		Set<OnboardingSurveyReminderContactDto> contacts = getContactsForFirstReminder(project);

		if (CommonUtil.isCollectionEmpty(contacts)) {
			log.info("No Contact for reminder 1");
			return new ResponseDto(false, "No Contact for Reminder 1");
		}

		for (OnboardingSurveyReminderContactDto contact : contacts) {
			contact.setReminderNumber("1");
			ResponseDto response = rmSurveyAPI.postReminder(contact);
			processReminderResponse(response, contact);
		}
		return new ResponseDto(false, "First Reminders Sent");
	}

	public ResponseDto sendSecondReminder(ProjectDto project) {

		Set<OnboardingSurveyReminderContactDto> contacts = getContactsForSecondReminder(project);

		if (CommonUtil.isCollectionEmpty(contacts)) {
			log.info("No Contact for reminder 2");
			return new ResponseDto(false, "No Contact for Reminder 2");
		}

		for (OnboardingSurveyReminderContactDto contact : contacts) {
			contact.setReminderNumber("2");
			ResponseDto response = rmSurveyAPI.postReminder(contact);
			processReminderResponse(response, contact);
		}
		return new ResponseDto(false, "Second Reminders Sent");

	}

	private void processReminderResponse(ResponseDto response, OnboardingSurveyReminderContactDto contact) {
		if(response == null || contact == null || response.isHasError()) {
			return;
		}
		OnboardingSurveyReminderResponseWrapperDto wrappedSurveyReminderReponse = (OnboardingSurveyReminderResponseWrapperDto) response.getData("rmSurveyReminderResponse");
		if(wrappedSurveyReminderReponse == null) {
			return;
		}
		
		OnboardingSurveyReminderResponseDto reminderResponse = wrappedSurveyReminderReponse.getResponse();
		if(reminderResponse == null) {
			return;
		}
		if(CommonUtil.isStringEmpty(reminderResponse.getError())) {
			updateReminderStatus(contact);
		}

	}
	
	private void updateReminderStatus(OnboardingSurveyReminderContactDto contact) {
		if(contact == null) {
			return;
		}
		if("1".equals(contact.getReminderNumber())){
			surveyRequestDao.updateFistReminderStaus(contact);
		}
		if("2".equals(contact.getReminderNumber())) {
			surveyRequestDao.updateSecondReminderStaus(contact);
		}
	}

	@Override
	public void sendReminderToAllRegions() {
		List<String> regions = projectService.getRegions();
		sendReminderRegionWise(regions);
	}

	@Override
	public void sendReminderRegionWise(List<String> regions) {
		if (CommonUtil.isCollectionEmpty(regions)) {
			log.info("No Region, can not process Reminder");
		}
		for (String region : regions) {
			sendReminderToRegion(region);
		}
	}

	private void sendReminderToRegion(String region) {
		if (CommonUtil.isStringEmpty(region)) {
			log.info("No region in input");
			return;
		}

		List<ProjectDto> projects = projectService.getProjects(region);

		if (CommonUtil.isCollectionEmpty(projects)) {
			log.info("No project for region -" + region);
			return;
		}

		for (ProjectDto project : projects) {
			sendReminder(project);
		}
	}

	@Override
	public Set<OnboardingSurveyReminderContactDto> getContactsForFirstReminder(ProjectDto project) {
		try {
			return surveyRequestDao.getContactsForFirstReminder(project);
		} catch (Exception e) {
			log.error("Error", e);
		}
		return new HashSet<>();
	}

	@Override
	public Set<OnboardingSurveyReminderContactDto> getContactsForSecondReminder(ProjectDto project) {
		try {
			return surveyRequestDao.getContactsForSecondReminder(project);
		} catch (Exception e) {
			log.error("Error", e);
		}
		return new HashSet<>();
	}
}
