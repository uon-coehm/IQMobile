
IF NOT EXISTS(SELECT * FROM mst_Section WHERE SectionName = N'Linkage'
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
) VALUES (N'Linkage', 0, 0, 1
, GETDATE()
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 5
, 0
)
END

IF NOT EXISTS(Select * FROM lnk_FormTabSection WHERE TabID = 
(SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
AND SectionID = (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Linkage' AND DeleteFlag = 0)
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
, (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Linkage' AND DeleteFlag = 0)
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form')
, 1, GETDATE())
END

--// HTS Linkage //--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Linked To Care')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Linked To Care', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Linked To Care'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'YES'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Linked To Care')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NO'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Linked To Care')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NA (HIV Negative Clients)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Linked To Care')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)

END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSLinkedToCare')
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

VALUES (N'HTSLinkedToCare'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Linked To Care')
, N'LinkedToCareID'
, NULL
, 0)

END

ELSE

PRINT('HTSLinkedToCare PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSLinkedToCare')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Linkage')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSLinkedToCare')
, N'If Positive, was the Client Linked to Care?'
, 1, GETDATE()
, 1
, 1) -- Sequence
END

ELSE
PRINT ('HTSLinkedToCare  Already in HTS Lab Form')
--////--



--// CCC Number //--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSCCCNumber')
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

VALUES (N'HTSCCCNumber'
, N'dtl_HTSEncounter'
, 1
, NULL
, 1
, GETDATE()
, 300
, NULL
, N'CCCNumber'
, NULL
, 0)

END

ELSE

PRINT('HTSCCCNumber PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSCCCNumber')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Linkage')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSCCCNumber')
, N'Enter the CCC Number Given'
, 1, GETDATE()
, 1
, 2) -- Sequence
END

ELSE
PRINT ('HTSCCCNumber  Already in HTS Lab Form')
--////--




