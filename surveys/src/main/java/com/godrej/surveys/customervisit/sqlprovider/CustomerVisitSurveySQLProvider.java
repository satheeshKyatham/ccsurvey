package com.godrej.surveys.customervisit.sqlprovider;

public class CustomerVisitSurveySQLProvider {

private StringBuilder baseQuery = new StringBuilder();
	
	public CustomerVisitSurveySQLProvider() {
		baseQuery.append("SELECT a.propstrength__applicant_email__c as email,a.rm_name__c custom20 ")
		.append(",a.propstrength__applicant_mobile__c as mobile, ")
		.append("a.propstrength__primary_applicant_name__c as firstName, '' as lastName")
		.append(",a.propstrength__booking_date__c dateOfBooking")
		.append(",to_char(a.propstrength__booking_date__c,'dd-mm-yyyy') bookingDate ")
		.append(",a.name, #{segmentCode} as segmentCode ")
		.append(", #{transactionDate} as transactionDate ")
		.append(",a.propstrength__property_name__c as field1,Project_Name__c as field15 ")
		.append(",a.sap_customer_code__c AS field2 ")
		.append(",b.propstrength__income_tax_permanent_account_no__c as field4")
		.append(",b.mailing_city__c as field6")
		.append(", #{region} AS field8")
		.append(",a.Project_Phases__c as field9")
		.append(",a.PropStrength__Welcome_Letter_Note__c as field11")
		.append(",a.Handover_Date__c as field13")
		.append(",to_char(a.cvst__c,'dd-mm-yyyy') as field14")
		.append(",b.FirstName as field16")
		.append(",CASE WHEN a.handover_date__c IS NULL THEN 'PRE_POSSESSION' ") 
		.append(" ELSE 'POST_POSSESSION' END AS surveyType")
		.append(",propstrength__property_name__c AS propertyName")
		.append(",'ECRM' AS field3, '6126647' as field18 ")
		.append(", handover_done_by__c as field7 ")
		.append(",#{instanceId} as instance_id ")
		.append(", now() as created_on ")
		.append(", #{name} as survey ")
		.append(", #{viewOnly} as viewOnly ")
		.append(" FROM salesforce.propstrength__application_booking__c a ")
	    .append(" INNER JOIN salesforce.contact b on (a.propstrength__primary_customer__c =b.sfid)")
		/*
		 * .append(" WHERE A.propstrength__project__c=#{sfid} ")
		 * .append("AND A.propstrength__active__c=true AND ")
		 * .append(" a.dnd__c= false ")
		 * .append("  AND a.propstrength__status__c= 'Deal Approved'")
		 * .append(" AND (a.customer_status__c IS NULL OR ")
		 * .append(" A.customer_status__c NOT IN ('Legal Case','Voluntary Cancellation Request Received','Pre-termination sent','Termination sent','Registration Termination') )"
		 * ) .append(" AND a.propstrength__applicant_email__c is not null ")
		 */
	    ;
	}
	
	public final String getBasicWhereClause() {
		StringBuilder whereClause = new StringBuilder();
		 whereClause.append(" WHERE A.propstrength__project__c=#{sfid} ")
		.append(" AND A.propstrength__active__c=true AND ")
		.append(" a.dnd__c= false ")
		.append("  AND a.propstrength__status__c= 'Deal Approved'")
		.append(" AND (a.customer_status__c IS NULL OR ")
		.append(" A.customer_status__c NOT IN ('Legal Case','Voluntary Cancellation Request Received','Pre-termination sent','Termination sent','Registration Termination') )")
		.append(" AND a.propstrength__applicant_email__c is not null ");
		return whereClause.toString();
	}

	
	public final String getContactSQL() {
		StringBuilder query = new StringBuilder();
		query.append(baseQuery.toString())
			.append(getBasicWhereClause())
				 .append(" AND a.cvst__c >= (date(to_char(now(),'mm-dd-yyyy'))- integer'1') AND cvst__c <= (date(to_char(now(),'mm-dd-yyyy')))")
				 .append("   AND ( (CVSSD__C IS NULL AND a.CVSS__C <> true) OR CVSSD__C < ((date(to_char(now(),'mm-dd-yyyy'))- integer'45'))) ")
				 .append(" ORDER BY a.propstrength__booking_date__c,firstName, a.name ASC  ");
		return query.toString();
	}	 
	
	public final String getContactQuery() {
		StringBuilder query =  new StringBuilder();
		query.append(baseQuery.toString())
		.append(" WHERE a.name IN ")
		.append(" ( SELECT name from rmsurvey.g_contacts where instance_id  = #{instanceId} and (repeated is null OR repeated =false) ) ");
		return query.toString();
	}

	public final String parkRecords() {
		StringBuilder query =  new StringBuilder();
		query.append(" INSERT INTO rmsurvey.g_contacts (email,field20 ,mobile ,firstName,lastName ,bookingDate , bookingDateStr,") 
		.append("	name,segmentCode ,transactionDate , field1 ,field15 ,field2 ,field4 ,field6 ,")
		.append("	field8 ,field9 ,field11 ,field13 ,field14 ,field16 ,surveyType ,propertyName ,") 
		.append("	field3,field18 ,field7,instance_id ,created_on,survey, viewonly ) ")
		.append(getContactSQL());	
		return query.toString();
	}
	
	public final String getFirstReminderSQL() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a.propstrength__applicant_email__c as email,a.rm_name__c custom20 ")
		.append(",a.propstrength__applicant_mobile__c as mobile, ")
		.append("a.propstrength__primary_applicant_name__c as firstName, '' as lastName")
		.append(",a.propstrength__booking_date__c dateOfBooking")
		.append(",to_char(a.propstrength__booking_date__c,'dd-mm-yyyy') bookingDate ")
		.append(",a.name, #{segmentCode} as segmentCode ")
		.append(", #{transactionDate} as transactionDate ")
		.append(",a.propstrength__property_name__c as field1,Project_Name__c as field15 ")
		.append(",a.sap_customer_code__c AS field2 ")
		.append(",b.propstrength__income_tax_permanent_account_no__c as field4")
		.append(",b.mailing_city__c as field6")
		.append(", #{region} AS field8")
		.append(",a.Project_Phases__c as field9")
		.append(",a.PropStrength__Welcome_Letter_Note__c as field11")
		.append(",a.Handover_Date__c as field13")
		.append(",to_char(a.cvst__c,'dd-mm-yyyy') as field14")
//		.append(",a.SiteVisitOtherThanHandoverReceivedDate__c as field14")
		.append(",b.FirstName as field16")
		.append(",CASE WHEN a.handover_date__c IS NULL THEN 'PRE_POSSESSION' ") 
		.append(" ELSE 'POST_POSSESSION' END AS surveyType")
		.append(",propstrength__property_name__c AS propertyName")
		.append(",'ECRM' AS field3, '6126647' as field18 ")
		.append(", handover_done_by__c as field7 ")
		.append(",#{instanceId} as instance_id ")
		.append(", now() as created_on ")
		.append(", #{name} as survey ")
		.append(", #{viewOnly} as viewOnly ")
		.append(", a.sfid ")
		.append(" FROM salesforce.propstrength__application_booking__c a ")
	    .append(" INNER JOIN salesforce.contact b on (a.propstrength__primary_customer__c =b.sfid)")
		.append( " WHERE A.propstrength__project__c=#{sfid} ")
				/* .append("AND  a.handover_date__c IS NULL ") */
				/*
				 * .append(" AND A.propstrength__booking_date__c BETWEEN to_date(#{fromDate},'dd-mm-yyyy') AND to_date(#{toDate},'dd-mm-yyyy') "
				 * )
				 */
		.append(" AND CVSSD__C =date(to_char(now(),'mm-dd-yyyy'))- integer'2' ")
		 .append(" AND a.CVFRD__c IS NULL ") 
		.append(" ORDER BY a.propstrength__booking_date__c,a.propstrength__primary_applicant_name__c ASC  ");
		return query.toString();	
	}
	
	public final String getSecondReminderSQL() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a.propstrength__applicant_email__c as email,a.rm_name__c custom20 ")
		.append(",a.propstrength__applicant_mobile__c as mobile, ")
		.append("a.propstrength__primary_applicant_name__c as firstName, '' as lastName")
		.append(",a.propstrength__booking_date__c dateOfBooking")
		.append(",to_char(a.propstrength__booking_date__c,'dd-mm-yyyy') bookingDate ")
		.append(",a.name, #{segmentCode} as segmentCode ")
		.append(", #{transactionDate} as transactionDate ")
		.append(",a.propstrength__property_name__c as field1,Project_Name__c as field15 ")
		.append(",a.sap_customer_code__c AS field2 ")
		.append(",b.propstrength__income_tax_permanent_account_no__c as field4")
		.append(",b.mailing_city__c as field6")
		.append(", #{region} AS field8")
		.append(",a.Project_Phases__c as field9")
		.append(",a.PropStrength__Welcome_Letter_Note__c as field11")
		.append(",a.Handover_Date__c as field13")
		.append(",to_char(a.cvst__c,'dd-mm-yyyy') as field14")
//		.append(",a.SiteVisitOtherThanHandoverReceivedDate__c as field14")
		.append(",b.FirstName as field16")
		.append(",CASE WHEN a.handover_date__c IS NULL THEN 'PRE_POSSESSION' ") 
		.append(" ELSE 'POST_POSSESSION' END AS surveyType")
		.append(",propstrength__property_name__c AS propertyName")
		.append(",'ECRM' AS field3, '6126647' as field18 ")
		.append(", handover_done_by__c as field7 ")
		.append(",#{instanceId} as instance_id ")
		.append(", now() as created_on ")
		.append(", #{name} as survey ")
		.append(", #{viewOnly} as viewOnly ")
		.append(", a.sfid ")
		.append(" FROM salesforce.propstrength__application_booking__c a ")
	    .append(" INNER JOIN salesforce.contact b on (a.propstrength__primary_customer__c =b.sfid)")
		.append( " WHERE A.propstrength__project__c=#{sfid} ")
				/* .append("AND a.handover_date__c IS NOT NULL  ") */
				/*
				 * .append(" AND A.propstrength__booking_date__c BETWEEN to_date(#{fromDate},'dd-mm-yyyy') AND to_date(#{toDate},'dd-mm-yyyy') "
				 * )
				 */
		.append(" AND  CVSSD__C =date(to_char(now(),'mm-dd-yyyy'))- integer'4' ")
		.append(" AND CVSRD__c IS NULL ")
		.append(" ORDER BY a.propstrength__booking_date__c,a.propstrength__primary_applicant_name__c ASC  ");
		return query.toString();	
	}
	
	public String getStatusUpdateQuery() {
		StringBuilder query =  new StringBuilder();
		query.append("UPDATE salesforce.propstrength__application_booking__c a SET ") 
			.append(" CVSSD__C= now(),CVSS__C=true")
			.append(" WHERE ") 
			.append("a.propstrength__project__c= #{sfid} ") 
			.append(" AND a.propstrength__active__c=true AND ") 
			.append(" a.dnd__c= false " ) 
			.append("  AND a.propstrength__status__c = 'Deal Approved'")
			.append(" AND (a.customer_status__c IS NULL OR ")
			.append(" A.customer_status__c NOT IN ('Legal Case','Voluntary Cancellation Request Received','Pre-termination sent','Termination sent','Registration Termination') )")
			.append(" AND a.propstrength__applicant_email__c is not null ")
			 .append(" AND a.cvst__c >= (date(to_char(now(),'mm-dd-yyyy'))- integer'1') AND cvst__c <= (date(to_char(now(),'mm-dd-yyyy')))")
			 .append("   AND ( (CVSSD__C IS NULL AND a.CVSS__C <> true) OR CVSSD__C < ((date(to_char(now(),'mm-dd-yyyy'))- integer'45'))) ");

		return query.toString();
	}
	
	
	public String getStatusUpdateFromContactLogsQuery() {
		StringBuilder query =  new StringBuilder();
		query.append("UPDATE salesforce.propstrength__application_booking__c a SET ") 
			.append(" CVSSD__C = survey_sent_date")
			.append(",CVSS__C= survey_sent")
			.append(" FROM rmsurvey.g_contacts b ")
			.append( " Where a.name = b.name and b.instance_id = #{instanceId}");

		return query.toString();
	}

	
	public String getClearingUpdateQuery() {
		StringBuilder query =  new StringBuilder();
		query.append("UPDATE salesforce.propstrength__application_booking__c SET ") 
			.append("SVSSD__C = null WHERE sfid in ( SELECT sfid FROM (") 
				.append(" SELECT row_number () over (partition BY x.email") 
				.append(" ORDER BY ") 
				.append(" x.bookingdate,") 
				.append(" x.firstname, x.name ASC) as updateId ") 
				.append(" , x.*  FROM ") 
				.append(" (SELECT ") 
				.append(" a.propstrength__applicant_email__c                   AS email,") 
				.append(" a.propstrength__applicant_mobile__c                  AS mobile,") 
				.append(" a.propstrength__primary_applicant_name__c            AS firstname, ") 
				.append(" '' AS lastname, ") 
				.append(" to_char(a.propstrength__booking_date__c, 'dd-mm-yyyy') bookingdate, ") 
				.append(" a.name, ") 
				.append(" a.sfid, ") 
				.append(" a.propstrength__property_name__c                     AS field1 ") 
				.append(" ,a.propstrength__booking_date__c ") 
				.append(" FROM ")
				.append(" salesforce.propstrength__application_booking__c   a " ) 
				.append(" INNER JOIN salesforce.contact  b ON ( a.propstrength__primary_customer__c = b.sfid ) ") 
				.append(" WHERE ")  
				.append(" a.propstrength__project__c = #{sfid} ") 
				.append(" AND a.propstrength__active__c = true ") 
				.append(" AND a.dnd__c = false ") 
				.append(" AND a.propstrength__status__c = 'Deal Approved' ") 
				.append(" AND (a.customer_status__c IS NULL OR ")
				.append(" A.customer_status__c NOT IN ('Legal Case','Voluntary Cancellation Request Received','Pre-termination sent','Termination sent','Registration Termination') )")
				.append(" AND a.propstrength__applicant_email__c is not null ")
				 .append(" AND a.cvst__c >= (date(to_char(now(),'mm-dd-yyyy'))- integer'45') AND cvst__c <= (date(to_char(now(),'mm-dd-yyyy')))")
				.append(" AND a.CVSSD__C = CURRENT_DATE")
				.append( " ORDER BY ") 
				.append(" a.propstrength__booking_date__c, ")  
				.append(" a.propstrength__primary_applicant_name__c, a.name ASC ") 
				.append(" ) X   ) Z WHERE updateId >1 )");
		
		return query.toString();
	}

	
	
	
	public String getPostStatusUpdateQuery() {
		StringBuilder query =  new StringBuilder();
		query.append("UPDATE salesforce.propstrength__application_booking__c a SET ") 
			.append(" baseline_survey_sent_date__c= now(),baseline_survey_sent__c=true")
			.append(" WHERE ") 
			.append("a.propstrength__project__c= #{sfid} ") 
			.append("AND  a.handover_date__c IS NOT NULL ") 
			.append(" AND A.propstrength__booking_date__c BETWEEN to_date(#{fromDate},'dd-mm-yyyy') AND to_date(#{toDate},'dd-mm-yyyy') ")			
			.append(" AND a.propstrength__active__c=true AND ") 
			.append("a.dnd__c= false " ) 
			.append("AND a.propstrength__status__c= 'Deal Approved' ") 
			.append("AND (a.customer_status__c IS NULL OR ") 
			.append(" a.customer_status__c NOT IN ('Legal Case','Voluntary Cancellation Request Received'") 
			.append(" ,'Pre-termination sent','Termination sent','Registration Termination') ") 
			.append(" )  ") 
			.append(" AND a.propstrength__applicant_email__c is not null ") 
			.append("AND a.baseline_survey_sent_date__c IS NULL AND a.baseline_survey_sent__c <> true");
		return query.toString();
	}
	
	public String getUpdateFirstReminderStatusSQL() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE salesforce.propstrength__application_booking__c")
		.append(" SET CVFRD__c=now()")
		.append(" WHERE sfid=#{sfid}");	
		return query.toString();
	}
	
	public String getUpdateSecondReminderStatusSQL() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE salesforce.propstrength__application_booking__c")
		.append(" SET CVSRD__c=now()")
		.append(" WHERE sfid=#{sfid}");	
		return query.toString();
	}
}
