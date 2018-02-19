--check duplicates

UPDATE 
	mst_Patient 
		WITH(ROWLOCK) SET mst_Patient.Status=0, mst_patient.Client_Code='1010101010' 
	where 
		Ptn_pk= 61395

Insert into 
	ord_visit(
		Ptn_Pk,LocationID,VisitDate,VisitType,DataQuality,DeleteFlag,UserID,CreateDate)
	values (
		61395, 1024,'2016/05/26 12:00:00 AM',115,0,0,1, Getdate())


Insert into 
	lnk_patientprogramstart(
		Ptn_Pk,ModuleID,StartDate,UserID,CreateDate)
	values (
		61395, 5,'2016/05/26 12:00:00 AM',1, Getdate())


