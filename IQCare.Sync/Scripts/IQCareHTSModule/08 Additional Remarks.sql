
IF NOT EXISTS(SELECT * FROM mst_Section WHERE SectionName = N'Additional Remarks'
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
) VALUES (N'Additional Remarks', 0, 0, 1
, GETDATE()
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 5
, 0
)
END

IF NOT EXISTS(Select * FROM lnk_FormTabSection WHERE TabID = 
(SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
AND SectionID = (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Additional Remarks' AND DeleteFlag = 0)
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
, (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Additional Remarks' AND DeleteFlag = 0)
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form')
, 1, GETDATE())
END




IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSAdditionalRemarks')
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

VALUES (N'HTSAdditionalRemarks'
, N'dtl_HTSEncounter'
, 8
, NULL
, 1
, GETDATE()
, 300
, NULL
, N'AdditionalRemarks'
, NULL
, 0)

END

ELSE

PRINT('HTSAdditionalRemarks PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSAdditionalRemarks')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Additional Remarks')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSAdditionalRemarks')
, N'Remarks'
, 1, GETDATE()
, 1
, 1) -- Sequence
END

ELSE
PRINT ('HTSAdditionalRemarks  Already in HTS Lab Form')
--////--