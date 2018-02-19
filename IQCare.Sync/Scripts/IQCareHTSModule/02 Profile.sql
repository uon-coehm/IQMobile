IF NOT EXISTS(SELECT * FROM mst_Section WHERE SectionName = N'Profile'
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
) VALUES (N'Profile', 0, 0, 1
, GETDATE()
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 1
, 0
)
END

IF NOT EXISTS(Select * FROM lnk_FormTabSection WHERE TabID = 
(SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
AND SectionID = (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Profile' AND DeleteFlag = 0)
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
, (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Profile' AND DeleteFlag = 0)
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form')
, 1, GETDATE())
END



IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Key Population', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'NA: Not Applicable'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'SW: Sex Worker'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'MSM: Men who have sex with men'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'IDU: Intravenous Drug Users'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'O: Others (specify)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)
END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSKeyPop')
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

VALUES (N'HTSKeyPop'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Key Population')
, N'KeyPopulationID'
, NULL
, 0)

END

ELSE

PRINT('HTSKeyPop PDF Already Exists!')

--// Add PDFs to Form //--

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields 
WHERE PDFName = N'HTSKeyPop')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Profile')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSKeyPop')
, N'Select Key Population'
, 1, GETDATE()
, 1
, 1)
END

ELSE
PRINT ('HTSKeyPop  Already in HTS Lab Form')






IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Disability', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'NA: Not Disabled'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'D: Deaf/Hearing Impaired'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'B: Blind/Visually Impaired'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'M: Mentally Challenged'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'P: Physically Challenged'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'O: Other (specify)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)
END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSDisability')
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

VALUES (N'HTSDisability'
, N'dtl_PatientDisability'
, 9
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Disability')
, N'DisabilityID'
, NULL
, 0)

END

ELSE

PRINT('HTSDisability PDF Already Exists!')

--// Add PDFs to Form //--

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields 
WHERE PDFName = N'HTSDisability')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Profile')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSDisability')
, N'Is the Client Disabled? (Select all that apply)'
, 1, GETDATE()
, 1
, 4)
END

ELSE
PRINT ('HTSDisability  Already in HTS Lab Form')



IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSOtherDisability')
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

VALUES (N'HTSOtherDisability'
, N'dtl_PatientDisability'
, 1
, NULL
, 1
, GETDATE()
, 300
, NULL
, N'DisabilityText'
, NULL
, 0)

END

ELSE

PRINT('HTSOtherDisability PDF Already Exists!')

--// Add PDFs to Form //--

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields 
WHERE PDFName = N'HTSOtherDisability')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Profile')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSOtherDisability')
, N'Specify Other Disability'
, 1, GETDATE()
, 1
, 5)
END

ELSE
PRINT ('HTSDisability  Already in HTS Lab Form')