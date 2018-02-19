--// Create HTS Module -- SET ModuleID = 300 //--

IF NOT EXISTS(SELECT * FROM mst_module WHERE ModuleID = 300 AND ModuleName = 'HTS Module')
BEGIN
SET IDENTITY_INSERT mst_Module ON
INSERT INTO mst_module 
(ModuleID
, ModuleName
, DeleteFlag
, UserId
, CreateDate
, UpdateDate
, [Status]
, UpdateFlag
, Identifier
, PharmacyFlag
, IsPlugIn
, [Sequence]) VALUES
(300
, 'HTS Module'
, 0
, 1
, GETDATE()
, NULL
, 2
, 0
, 0
, 0
, NULL
, NULL)
SET IDENTITY_INSERT mst_Module ON
PRINT('HTS Module Created')

INSERT INTO lnk_FacilityModule
SELECT FacilityID, 300, 1, GETDATE(), NULL 
FROM mst_Facility WHERE DeleteFlag = 0


PRINT('HTS Module Linked to ')

END
--
ELSE
PRINT('Module Already Exists!')

--//Patient Identifiers//--
--//Client_Code//--
if not exists(select b.name c from sys.tables a inner join sys.columns b on a.object_id = b.object_id
where a.name = 'mst_patient'
and b.name = 'Client_Code')
BEGIN

INSERT INTO mst_PatientIdentifier 
(FieldName
, FieldType
, UserId
, CreateDate
, UpdateDate
, UpdateFlag
, Label
, AutoPopulateNumber) VALUES 
('Client_Code'
, 1
, 1
, GETDATE()
, NULL
, 0
, 'Client Code'
, 0)

PRINT ('Client Code INSERTED into mst_PatientIdentifier')

INSERT INTO lnk_PatientModuleIdentifier
(ModuleID, FieldID, UserID, CreateDate, UpdateDate, DeleteFlag)
VALUES
(300, (SELECT IDENT_CURRENT('mst_PatientIdentifier')), 1, GETDATE(), NULL, 0)

PRINT ('Client Code Linked to HTS Module')

ALTER TABLE mst_Patient ADD Client_Code VARCHAR(100) NULL



PRINT ('Client_Code COLUMN ADDED to mst_Patient') 
END
ELSE

PRINT ('Client Code Exists!')

--//SyncID//--

if not exists(select b.name c from sys.tables a inner join sys.columns b on a.object_id = b.object_id
where a.name = 'mst_patient'
and b.name = 'SyncId')
BEGIN

ALTER TABLE mst_Patient ADD SyncId VARCHAR(100) NULL

PRINT ('SyncId COLUMN ADDED to mst_Patient') 
END
ELSE

PRINT ('SyncId Exists!')

--// //--

IF NOT EXISTS(Select * FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
BEGIN
	INSERT [dbo].[mst_Feature] ([FeatureName]
	, [ReportFlag]
	, [DeleteFlag]
	, [AdminFlag]
	, [UserID]
	, [CreateDate]
	, [SystemId]
	, [Published]
	, [CountryId]
	, [ModuleId]
	, [MultiVisit]
	)VALUES 
	(N'HTS Lab Form'
	, 0
	, 0
	, 0
	, 1
	, GETDATE()
	, 1
	, 2
	, 161
	, 300
	, 1)
END

IF NOT EXISTS(SELECT * FROM mst_VisitType WHERE VisitName = N'HTS Module - Enrollment')
BEGIN
	INSERT [dbo].[mst_VisitType] (
	[VisitName]
	, [DeleteFlag]
	, [UserID]
	, [CreateDate]
	, [SystemId]
	, [FeatureID]) 
	VALUES ( N'HTS Module - Enrollment'
	, 0
	, 1
	, GETDATE()
	, 1	
	, NULL)
END

IF NOT EXISTS(SELECT * FROM mst_VisitType WHERE VisitName = N'HTS Lab Form')
BEGIN
	INSERT [dbo].[mst_VisitType] (
	[VisitName]
	, [DeleteFlag]
	, [UserID]
	, [CreateDate]
	, [SystemId]
	, [FeatureID]) 
	VALUES ( N'HTS Lab Form'
	, 0
	, 1
	, GETDATE()	
	, 1
	, (SELECT IDENT_CURRENT('mst_Feature')))
END

--// //--

IF NOT EXISTS(Select TabID FROM Mst_FormBuilderTab
WHERE TabName = N'HTS LAB' 
AND FeatureID = (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
AND DeleteFlag = 0))
BEGIN
INSERT [dbo].[Mst_FormBuilderTab] (
[TabName]
, [FeatureID]
, [DeleteFlag]
, [UserID]
, [CreateDate]
, [seq]
, [Signature]) 

VALUES (N'HTS LAB'
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 0
, 1
, GETDATE()
, 1
, 1)
END


insert into lnk_groupFeatures (FacilityID, ModuleID, GroupId
,FeatureId, FeatureName, TabID
,FunctionId,createDate)                              
values(
(SELECT TOP 1 FacilityID FROM mst_Facility WHERE DeleteFlag = 0)
, 300
, 1
,(Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, N'HTS Lab Form'
, (SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
,1,getdate())   
GO                              
insert into lnk_groupFeatures (FacilityID, ModuleID, GroupId
,FeatureId, FeatureName, TabID
, FunctionId,createDate)                       
values
((SELECT TOP 1 FacilityID FROM mst_Facility WHERE DeleteFlag = 0)
, 300, 1,(Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, N'HTS Lab Form'
, (SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
,2,getdate())  
GO                    

        
insert into lnk_groupFeatures (FacilityID, ModuleID, GroupId
,FeatureId, FeatureName, TabID
,FunctionId,createDate)           
values((SELECT TOP 1 FacilityID FROM mst_Facility WHERE DeleteFlag = 0)
, 300, 1,(Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, N'HTS Lab Form'
, (SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
,3,getdate()) 
GO                             
insert into lnk_groupFeatures (FacilityID, ModuleID, GroupId
,FeatureId , FeatureName, TabID
,FunctionId,createDate)                          
values((SELECT TOP 1 FacilityID FROM mst_Facility WHERE DeleteFlag = 0)
, 300, 1,(Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, N'HTS Lab Form'
, (SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
,4,getdate())  
GO                            
insert into lnk_groupFeatures (FacilityID, ModuleID, GroupId,FeatureId
, FeatureName, TabID
,FunctionId,createDate)                           
values((SELECT TOP 1 FacilityID FROM mst_Facility WHERE DeleteFlag = 0)
, 300, 1,(Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, N'HTS Lab Form'
, (SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
,5,getdate()) 
GO