select * from iqcare.[dbo].[mst_VisitType] where VisitTypeID=219
--	219-KNH HTC Form

Insert into 
	[ord_visit](ptn_pk, LocationID, VisitDate, VisitType, DataQuality, UserID, Signature, CreateDate)Values(61395,1024, '27-May-2016', 219,'0',1, 0, GetDate())
	
Insert into [DTL_FB_MARPsHTC]([ptn_pk], [Visit_Pk], [LocationID], [MARPsHTC], [UserID], [CreateDate], DateField1, DateField2, NumericField)
values (61395,  IDENT_CURRENT('Ord_Visit'),1024,4047,1, Getdate() , '2016/05/27 9:46:03 AM', '2016/05/27 9:46:03 AM', 0) 

Insert into [DTL_FB_DisabilityHTC]([ptn_pk], [Visit_Pk], [LocationID], [DisabilityHTC], [UserID], [CreateDate], DateField1, DateField2, NumericField)
values (61395,  IDENT_CURRENT('Ord_Visit'),1024,4064,1, Getdate() , '2016/05/27 9:47:00 AM', '2016/05/27 9:47:00 AM', 0) 

Insert into [DTL_FB_VisitReasonHTC]([ptn_pk], [Visit_Pk], [LocationID], [VisitReasonHTC], [UserID], [CreateDate], DateField1, DateField2, NumericField)
values (61395,  IDENT_CURRENT('Ord_Visit'),1024,4070,1, Getdate() , '2016/05/27 9:47:24 AM', '2016/05/27 9:47:24 AM', 0) 

Insert into [DTL_FB_VisitReasonHTC]([ptn_pk], [Visit_Pk], [LocationID], [VisitReasonHTC], [UserID], [CreateDate], DateField1, DateField2, NumericField)
values (61395,  IDENT_CURRENT('Ord_Visit'),1024, 4072,1, Getdate() , '2016/05/27 9:47:24 AM', '2016/05/27 9:47:24 AM', 0) 

Insert into [DTL_FB_InformationSourceHTC]([ptn_pk], [Visit_Pk], [LocationID], [InformationSourceHTC], [UserID], [CreateDate], DateField1, DateField2, NumericField)
values (61395,  IDENT_CURRENT('Ord_Visit'),1024,4092,1, Getdate() , '2016/05/27 9:47:50 AM', '2016/05/27 9:47:50 AM', 0) 

Insert into [DTL_FB_HTCReferredTO]([ptn_pk], [Visit_Pk], [LocationID], [HTCReferredTO], [UserID], [CreateDate], DateField1, DateField2, NumericField)
values (61395,  IDENT_CURRENT('Ord_Visit'),1024,6792,1, Getdate() , '2016/05/27 9:48:27 AM', '2016/05/27 9:48:27 AM', 0) 

Insert into [DTL_FB_ServicesGivenHTC]([ptn_pk], [Visit_Pk], [LocationID], [ServicesGivenHTC], [UserID], [CreateDate], DateField1, DateField2, NumericField)
values (61395,  IDENT_CURRENT('Ord_Visit'),1024,6787,1, Getdate() , '2016/05/27 9:48:59 AM', '2016/05/27 9:48:59 AM', 0) 

Insert into [DTL_FBCUSTOMFIELD_KNH_HTC_Form](Ptn_pk,Visit_Pk,LocationId,UserID,CreateDate,[HTCRegisteredTime],[HTCTimeIn],[ClientKNHStaffMember],[ServiceArea],[HTCcounsellorCode],[PreviousHIVResult],[TestingStrategy],[CounsellingMode],[OccupationHTC],[EducationHTC],[MaritalStatusHTC],[PregnantHTC],[ServiceHTC],[OtherDisabilityHTC],[OtherVisitReasonHTC],[OtherInfoSourceHTC],[TBScreeningHTC],[SexualEncounter],[CondomUseSteady],[CondomUseNonSteady],[CondomUseLastSex],[ConsentHTC],[ConsentNotGiven],[Determine],[Determine2],[Bioline],[Unigold],[ElisaResultHTC],[Discrepant],[FinalResultToday],[ResultGiven],[CoupleDiscordant],[DBSCollected],[CondomsIssuedHTC],[Noofmalecondoms],[Nooffemalecondoms],[OtherHTCReferredTO],[OtherServicesGivenHTC],[HTCTimeOut] )
Values(61395,IDENT_CURRENT('Ord_Visit'),1024,1, GetDate(),'00:00','00:00','1','6741','101','4129','4028','4030','4033','4039','4042','4185','4060','disable','visitimg','net','4103','0','0','0','0','0','4124','0','6795','0','0','0','0','4150','4155','0','0','0','12','23','CCC','XService','00:20' )

