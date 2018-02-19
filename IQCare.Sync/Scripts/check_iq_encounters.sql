/*
delete from [dbo].[patient] where id!=481
delete from iqcare.[dbo].dtl_fb_DisabilityHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from iqcare.[dbo].dtl_fb_HTCReferredTO  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from iqcare.[dbo].dtl_fb_InformationSourceHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from iqcare.[dbo].dtl_fb_MARPsHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from iqcare.[dbo].dtl_fb_ServicesGivenHTC  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from iqcare.[dbo].dtl_fb_VisitReasonHTC where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from iqcare.[dbo].[DTL_FBCUSTOMFIELD_KNH_HTC_Form]  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from  iqcare.dbo.dtl_PatientContacts where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from  iqcare.dbo.[DTL_PATIENTHOUSEHOLDINFO] where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
delete from  iqcare.dbo.[DTL_RURALRESIDENCE] where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
delete from  iqcare.dbo.[DTL_URBANRESIDENCE]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
delete from  iqcare.dbo.[DTL_PATIENTHIVPREVCAREENROLLMENT]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));			
delete from  iqcare.dbo.[DTL_PATIENTGUARANTOR]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
delete from  iqcare.dbo.[DTL_PATIENTDEPOSITS]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));			
delete from  iqcare.dbo.[DTL_PATIENTINTERVIEWER]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
delete from  iqcare.dbo.[DTL_FBCUSTOMFIELD_Patient_Registration] 	 where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
delete from  iqcare.dbo.ord_Visit where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from  iqcare.dbo.mst_Patient where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
delete from  iqcare.dbo.lnk_patientprogramstart where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));


select * from iqcare.[dbo].dtl_fb_DisabilityHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from iqcare.[dbo].dtl_fb_HTCReferredTO  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from iqcare.[dbo].dtl_fb_InformationSourceHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from iqcare.[dbo].dtl_fb_MARPsHTC   where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from iqcare.[dbo].dtl_fb_ServicesGivenHTC  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from iqcare.[dbo].dtl_fb_VisitReasonHTC where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from iqcare.[dbo].[DTL_FBCUSTOMFIELD_KNH_HTC_Form]  where Ptn_pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from  iqcare.dbo.dtl_PatientContacts where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from  iqcare.dbo.[DTL_PATIENTHOUSEHOLDINFO] where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
select * from  iqcare.dbo.[DTL_RURALRESIDENCE] where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
select * from  iqcare.dbo.[DTL_URBANRESIDENCE]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
select * from  iqcare.dbo.[DTL_PATIENTHIVPREVCAREENROLLMENT]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));			
select * from  iqcare.dbo.[DTL_PATIENTGUARANTOR]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
select * from  iqcare.dbo.[DTL_PATIENTDEPOSITS]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));			
select * from  iqcare.dbo.[DTL_PATIENTINTERVIEWER]  where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
select * from  iqcare.dbo.[DTL_FBCUSTOMFIELD_Patient_Registration] 	 where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));	
select * from  iqcare.dbo.ord_Visit where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from  iqcare.dbo.mst_Patient where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));
select * from  iqcare.dbo.lnk_patientprogramstart where Ptn_Pk in (SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)));

*/


/*
select * from [dbo].[user] where username like '%estXUse%'
select * from [dbo].[lookup] where codeid =-1
select * from [dbo].[patient] where lastname like '%erryX%' or lastname like '%amp%' or lastname like '%haves%' or firstname like '%Jamie%' or firstname like '%android%'
select * from [dbo].[module] where name like '%estModul%'
select * from [dbo].[encountertype] where module_id in		(select id from [dbo].[module] where name like '%estModul%')
select * from [dbo].[mconcept] where encounterType_id in	(select id from [dbo].[encountertype] where module_id in	(select id from [dbo].[module] where name like '%estModul%'))
select * from [dbo].[encounter] where encounterType_id in	(select id from [dbo].[encountertype] where module_id in	(select id from [dbo].[module] where name like '%estModul%'))
select * from [dbo].[observation] where encounter in		(select id from [dbo].[encounter] where encounterType_id in	(select id from [dbo].[encountertype] where module_id in	(select id from [dbo].[module] where name like '%estModul%')))

*/
--	data from test android

-- delete from [dbo].[patient] where id!=481

select * from [dbo].[patient] --where firstname like '%erryX%'
select * from [dbo].[encounter] --where patient_id in	(select id from [dbo].[patient] where firstname like '%erryX%')
select * from [dbo].[observation] --where encounter in (select id from [dbo].[encounter] where patient_id in	(select id from [dbo].[patient] where firstname like '%erryX%'))


SELECT  Ptn_Pk, CreateDate, Client_Code FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL))


SELECT * from iqcare.[dbo].ord_Visit where Ptn_Pk in(SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL))) AND VisitType=12 union
SELECT * from iqcare.[dbo].ord_Visit where Ptn_Pk in(SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL))) AND VisitType=115 union
SELECT * from iqcare.[dbo].ord_Visit where Ptn_Pk in(SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL))) AND VisitType=219
SELECT * from iqcare.[dbo].lnk_patientprogramstart where Ptn_Pk in(SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)))

select * from iqcare.[dbo].[DTL_FBCUSTOMFIELD_KNH_HTC_Form] where Ptn_Pk in(SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)))
select * from iqcare.[dbo].dtl_fb_DisabilityHTC  where Ptn_Pk in(SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)))
select * from iqcare.[dbo].dtl_fb_MARPsHTC  where Ptn_Pk in(SELECT Ptn_Pk FROM IQCare.dbo.mst_Patient WHERE (NOT (SyncId IS NULL)))
