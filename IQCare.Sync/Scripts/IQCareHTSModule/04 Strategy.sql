--// Strategy SEQ 3 //--
IF NOT EXISTS(SELECT * FROM mst_Section WHERE SectionName = N'Strategy'
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
) VALUES (N'Strategy', 0, 0, 1
, GETDATE()
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 3
, 0
)
END

IF NOT EXISTS(Select * FROM lnk_FormTabSection WHERE TabID = 
(SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
AND SectionID = (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Strategy' AND DeleteFlag = 0)
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
, (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Strategy' AND DeleteFlag = 0)
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form')
, 1, GETDATE())
END

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Consent')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Consent', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Consent'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'YES'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Consent')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NO'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Consent')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)
END

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSConsent')
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

VALUES (N'HTSConsent'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Consent')
, N'ConsentID'
, NULL
, 0)

END

ELSE

PRINT('HTSConsent PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields 
WHERE PDFName = N'HTSConsent')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Strategy')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSConsent')
, N'Consent'
, 1, GETDATE()
, 1
, 1) -- Sequence
END

ELSE
PRINT ('HTSConsent  Already in HTS Lab Form')


--// /////---
IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Tested As')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Tested As', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Tested As'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'I: Individual'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Tested As')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'C: Couple (includes polygamous)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Tested As')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)
END

--////--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestedAs')
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

VALUES (N'HTSTestedAs'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Tested As')
, N'TestedAsID'
, NULL
, 0)

END

ELSE

PRINT('HTSTestedAs PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestedAs')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Strategy')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestedAs')
, N'Client Testing As'
, 1, GETDATE()
, 1
, 2) -- Sequence
END

ELSE
PRINT ('HTSTestedAs  Already in HTS Lab Form')

--// HTS Strategy //--


IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Strategy', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'HP: Regular HTS for patients in the health facility (PITC)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NP: HTS for non-patients'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'VI: Static HTS in integrated VCT sites'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'VS: Static HTS in stand-alone VCT sites'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, 4
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'HB: Home-based HTS'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, 5
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'MO: Mobile and all other outreach HTS'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, 6
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'O: Other'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, 7
, 0
, 0
, 1
, GETDATE()
, 1)
END

--////--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSStrategy')
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

VALUES (N'HTSStrategy'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Strategy')
, N'StrategyID'
, NULL
, 0)

END

ELSE

PRINT('HTSStrategy PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSStrategy')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Strategy')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSStrategy')
, N'Select the HTS Strategy Used'
, 1, GETDATE()
, 1
, 3) -- Sequence
END

ELSE
PRINT ('HTSStrategy  Already in HTS Lab Form')
