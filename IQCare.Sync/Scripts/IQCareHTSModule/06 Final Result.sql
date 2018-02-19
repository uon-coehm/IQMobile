
IF NOT EXISTS(SELECT * FROM mst_Section WHERE SectionName = N'Final Result'
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
) VALUES (N'Final Result', 0, 0, 1
, GETDATE()
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 4
, 0
)
END

IF NOT EXISTS(Select * FROM lnk_FormTabSection WHERE TabID = 
(SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
AND SectionID = (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Final Result' AND DeleteFlag = 0)
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
, (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Final Result' AND DeleteFlag = 0)
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form')
, 1, GETDATE())
END

--// Final Result //--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Final Result', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'N: Negative (Non-Reactive)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'P: Positive (Reactive)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'Ic: Inconclusive'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)

END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSFinalResult')
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

VALUES (N'HTSFinalResult'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result')
, N'FinalResultID'
, NULL
, 0)

END

ELSE

PRINT('HTSFinalResult PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSFinalResult')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Final Result')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSFinalResult')
, N'Select the Final Result'
, 1, GETDATE()
, 1
, 5) -- Sequence
END

ELSE
PRINT ('HTSFinalResult  Already in HTS Lab Form')
--////--



--// Final Result Given //--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result Given')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Final Result Given', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result Given'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'YES'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result Given')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NO'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result Given')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)


END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSFinalResultGiven')
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

VALUES (N'HTSFinalResultGiven'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Final Result Given')
, N'FinalResultGivenID'
, NULL
, 0)

END

ELSE

PRINT('HTSFinalResultGiven PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSFinalResultGiven')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Final Result')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSFinalResultGiven')
, N'Was the Client Given the Final Result?'
, 1, GETDATE()
, 1
, 6) -- Sequence
END

ELSE
PRINT ('HTSFinalResultGiven  Already in HTS Lab Form')
--////--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Couple Discordant')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Couple Discordant', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Couple Discordant'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'YES'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Couple Discordant')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NO'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Couple Discordant')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NA'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Couple Discordant')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)


END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSCoupeDiscordant')
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

VALUES (N'HTSCoupeDiscordant'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Couple Discordant')
, N'CoupleDiscordantID'
, NULL
, 0)

END

ELSE

PRINT('HTSCoupeDiscordant PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSCoupeDiscordant')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Final Result')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSCoupeDiscordant')
, N'If Tested as a Couple, are the Clients Discordant?'
, 1, GETDATE()
, 1
, 7) -- Sequence
END

ELSE
PRINT ('HTSCoupeDiscordant  Already in HTS Lab Form')
--////--


--// Couple Discordant //--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS TB Screening')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS TB Screening', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TB Screening'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'Pr TB: Presumed TB'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TB Screening')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NS: No Signs'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TB Screening')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'ND: Not Done'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TB Screening')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'TBRX: On TB Treatment'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TB Screening')
, 4
, 0
, 0
, 1
, GETDATE()
, 1)


END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTBScreening')
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

VALUES (N'HTSTBScreening'
, N'dtl_HTSEncounter'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TB Screening')
, N'TBScreeningID'
, NULL
, 0)

END

ELSE

PRINT('HTSTBScreening PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTBScreening')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'Final Result')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTBScreening')
, N'TB Screening Result'
, 1, GETDATE()
, 1
, 8) -- Sequence
END

ELSE
PRINT ('HTSTBScreening  Already in HTS Lab Form')
--////--