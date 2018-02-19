Open symmetric key Key_CTC decryption by password=N'ttwbvXWpqb5WOLfLrBgisw==';

INSERT INTO 
	mst_Patient(
		Status, FirstName, MiddleName, LastName, 
		LocationID, RegistrationDate, Sex, DOB, DobPrecision, 
		CountryId, PosId, SatelliteId, UserID, CreateDate, 
		Phone)
VALUES(
	'0', encryptbykey(key_guid('Key_CTC'), 'JJ'), encryptbykey(key_guid('Key_CTC'), 'P'), encryptbykey(key_guid('Key_CTC'), 'MM'), 
	'1024', '26-May-2016', '16', '15-Jun-1991', '1', 
	'648', '01', '06', '1', GETDATE(),
	encryptbykey(key_guid('Key_CTC'), '2XX2342'))

--delete from mst_Patient where Ptn_Pk=IDENT_CURRENT('mst_Patient');
		
INSERT INTO ord_Visit
                         (Ptn_Pk, LocationID, VisitDate, VisitType, UserID, CreateDate)
VALUES        (IDENT_CURRENT('mst_Patient'), '1024', '26-May-2016', 12, '1', GETDATE())

INSERT INTO dtl_PatientContacts
                         (ptn_pk, VisitId, LocationID, UserID, CreateDate, NextofKinName, NextofKinRelationship, NextofKinTelNo)
VALUES        (IDENT_CURRENT('mst_Patient'), IDENT_CURRENT('Ord_Visit'), 1024, 1, GETDATE(), 'kina', '376', '142424145')
Insert into 
	[DTL_PATIENTHOUSEHOLDINFO](
		Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[Occupation])
	Values(
		IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate(),'0')
		
Insert into 
	[DTL_RURALRESIDENCE](
		Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[RuralDistrict],[RuralDivision])
	Values(
		IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate(),'0','0')

Insert into 
	[DTL_URBANRESIDENCE](
		Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[UrbanTown])
	Values(
		IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate(),'0')

Insert into 
	[DTL_PATIENTHIVPREVCAREENROLLMENT](
		Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
	Values(
		IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate())
		
Insert into 
	[DTL_PATIENTGUARANTOR](
		Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[Guarantor1Occupation],[Guarantor2Occupation])
	Values(
		IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate(),'0','0')

Insert into 
	[DTL_PATIENTDEPOSITS](
		Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
	Values(
		IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate())
		
Insert into 
	[DTL_PATIENTINTERVIEWER](
		Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
	Values(
		IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate())

if exists(
	select name from sysobjects where name = 'DTL_FBCUSTOMFIELD_Patient_Registration') 
begin 
	Insert into 
		[DTL_FBCUSTOMFIELD_Patient_Registration](
			Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate)
		Values(
			IDENT_CURRENT('mst_Patient'),IDENT_CURRENT('Ord_Visit'),1024,1, GetDate()) 
end 

update 
	mst_patient set IQNumber = 'IQ-'+convert(varchar,Replicate('0',20-len(x.[ptnIdentifier]))) +x.[ptnIdentifier]  
from (
	select 
		UPPER(substring(convert(varchar(50),decryptbykey(firstname)),1,1))+UPPER(substring(convert(varchar(50),decryptbykey(lastname)),1,1))+convert(varchar,dob,112)+convert(varchar,locationid)+Convert(varchar(10),ptn_pk) [ptnIdentifier] 
	from 
		mst_patient where ptn_pk = ident_current('mst_patient'))x 
	where ptn_pk= ident_current('mst_patient') 
	
	Select 
		ident_current('mst_patient')[ptn_pk], a.IQNumber, b.Visit_ID 
	from 
		mst_patient a inner join Ord_visit b on a.ptn_pk=b.ptn_pk 
	where 
		a.Ptn_Pk=ident_current('mst_patient') and b.visittype=12