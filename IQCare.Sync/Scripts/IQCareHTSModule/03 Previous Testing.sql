--// Previous Testing //--

IF NOT EXISTS(SELECT * FROM mst_Section WHERE SectionName = N'Previous Testing'
AND FeatureId = (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[mst_Section] (
[SectionName]
, [CustomFlag]
, [DeleteFlag]
, [UserID]
, [CreateDate]
, [FeatureId]
, [Seq]
, [IsGridView]
) VALUES (N'Previous Testing', 0, 0, 1
, GETDATE()
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 2
, 0
)
END

IF NOT EXISTS(Select * FROM lnk_FormTabSection WHERE TabID = 
(SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
AND SectionID = (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Previous Testing' AND DeleteFlag = 0)
AND FeatureID = (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form'))
BEGIN
INSERT [dbo].[lnk_FormTabSection] (
[TabID]
, [SectionID]
, [FeatureID]
, [UserID]
, [CreateDate]) 
VALUES 
((SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
, (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Previous Testing' AND DeleteFlag = 0)
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form')
, 1, GETDATE())
END

--// HTS Ever Tested //--


IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Tested')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Ever Tested', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Tested'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'YES'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Tested')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NO'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Tested')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)
END

--//Ever Tested //--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSEverTested')
BEGIN
INSERT [dbo].[Mst_PreDefinedFields] (
[PDFName]
, [PDFTableName]
, [ControlId]
, [BindTable]
, [UserId]
, [CreateDate]
, [ModuleId]
, [CategoryId]
, [BindField]
, [PatientRegistration]
, [DeleteFlag]) 

VALUES (N'HTSEverTested'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Tested')
, N'EverTestedID'
, NULL
, 0)

END

ELSE

PRINT('HTSEverTested PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSEverTested')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Previous Testing')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSEverTested')
, N'Has the Client been Tested (By a HTS Provider) Before?'
, 1, GETDATE()
, 1
, 1) -- Sequence
END

ELSE
PRINT ('HTSEverTested  Already in HTS Lab Form')

--// Months Ago //--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSRetestMonths')
BEGIN
INSERT [dbo].[Mst_PreDefinedFields] (
[PDFName]
, [PDFTableName]
, [ControlId]
, [BindTable]
, [UserId]
, [CreateDate]
, [ModuleId]
, [CategoryId]
, [BindField]
, [PatientRegistration]
, [DeleteFlag]) 

VALUES (N'HTSRetestMonths'
, N'dtl_HTSEncounter'
, 2
, NULL
, 1
, GETDATE()
, 300
, NULL
, N'RetestMonths'
, NULL
, 0)

END

ELSE

PRINT('HTSRetestMonths PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSRetestMonths')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Previous Testing')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSRetestMonths')
, N'How long ago (months)?'
, 1, GETDATE()
, 1
, 2) -- Sequence
END

ELSE
PRINT ('HTSRetestMonths  Already in HTS Lab Form')



--// HTS Ever Tested //--


IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Self Tested')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Ever Self Tested', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Self Tested'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'YES'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Self Tested')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NO'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Self Tested')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)
END

--//Ever Tested //--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSSelfTested')
BEGIN
INSERT [dbo].[Mst_PreDefinedFields] (
[PDFName]
, [PDFTableName]
, [ControlId]
, [BindTable]
, [UserId]
, [CreateDate]
, [ModuleId]
, [CategoryId]
, [BindField]
, [PatientRegistration]
, [DeleteFlag]) 

VALUES (N'HTSSelfTested'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Ever Self Tested')
, N'SelfTestedID'
, NULL
, 0)

END

ELSE

PRINT('HTSEverTested PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSSelfTested')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Previous Testing')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSSelfTested')
, N'Has the Client Self Tested in the last 12 Months??'
, 1, GETDATE()
, 1
, 3) -- Sequence
END

ELSE
PRINT ('HTSEverTested  Already in HTS Lab Form')