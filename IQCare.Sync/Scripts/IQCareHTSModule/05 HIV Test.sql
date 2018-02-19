-- HIVTest Grid View
--select * from mst_Section
IF NOT EXISTS(SELECT * FROM mst_Section WHERE SectionName = N'HIVTest'
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
) VALUES (N'HIVTest', 0, 0, 1
, GETDATE()
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'
	AND DeleteFlag = 0)
, 4
, 1
)
END

IF NOT EXISTS(Select * FROM lnk_FormTabSection WHERE TabID = 
(SELECT TOP 1 TabID FROM [Mst_FormBuilderTab] WHERE TabName = N'HTS LAB' AND DeleteFlag = 0)
AND SectionID = (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'HIVTest' AND DeleteFlag = 0)
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
, (SELECT TOP 1 SectionID FROM mst_Section WHERE SectionName = N'HIVTest' AND DeleteFlag = 0)
, (SELECT TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = 'HTS Lab Form')
, 1, GETDATE())
END

--// Test Kit //--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS TestKit', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'Determine'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'First Response'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'Unigold'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)
END
--////--


--// Test Kit Category //--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit Category')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS TestKit Category', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit Category'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'First Test'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit Category')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'Second Test'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit Category')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'Repeat Test (After Inconclusive Result)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit Category')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)
END
--////--



--// Test Result //--

IF NOT EXISTS (Select CodeID FROM mst_Code WHERE [Name] = N'HTS Test Result')
BEGIN
INSERT INTO mst_Code(Name, DeleteFlag,UserID, CreateDate)
VALUES
(N'HTS Test Result', 0, 1, GETDATE())
END

IF NOT EXISTS(SELECT * FROM mst_Decode 
WHERE CodeID = (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Test Result'))
BEGIN
INSERT INTO mst_Decode(Name, CodeID, SRNo, UpdateFlag, DeleteFlag, UserID, CreateDate, SystemId)
VALUES
(N'N: Negative (Non-Reactive)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Test Result')
, 1
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'P: Positive (Reactive)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Test Result')
, 2
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'I: Invalid'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Test Result')
, 3
, 0
, 0
, 1
, GETDATE()
, 1)

,(N'NA: Not Applicable (If Test 1 is Negative)'
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Test Result')
, 4
, 0
, 0
, 1
, GETDATE()
, 1)

END


IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestResult')
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

VALUES (N'HTSTestResult'
, N'DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS Test Result')
, N'HIVTestResultID'
, NULL
, 0)

END

ELSE

PRINT('HTSTestResult PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestResult')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'HIVTest')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestResult')
, N'Select the Test Result'
, 1, GETDATE()
, 1
, 5) -- Sequence
END

ELSE
PRINT ('HTSTestResult  Already in HTS Lab Form')
--////--



IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKit')
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

VALUES (N'HTSTestKit'
, N'DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit')
, N'TestKitID'
, NULL
, 0)

END

ELSE

PRINT('TestKitID PDF Already Exists!')

--/////---

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitCategory')
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

VALUES (N'HTSTestKitCategory'
, N'DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form'
, 4
, N'mst_Decode'
, 1
, GETDATE()
, 300
, (Select TOP 1 CodeID FROM mst_Code WHERE [Name] = N'HTS TestKit Category')
, N'TestKitCategoryID'
, NULL
, 0)

END

ELSE

PRINT('TestKitCategoryID PDF Already Exists!')


--// HTSTestKitCategory //---

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitCategory')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'HIVTest')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitCategory')
, N'Select the Test Category'
, 1, GETDATE()
, 1
, 1) -- Sequence
END

ELSE
PRINT ('HTSTestKitCategory  Already in HTS Lab Form')

---// HTSTestKit //----

IF NOT EXISTS (SELECT * FROM Lnk_Forms 
WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKit')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'HIVTest')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKit')
, N'Select the Test Kit Used'
, 1, GETDATE()
, 1
, 2) -- Sequence
END

ELSE
PRINT ('HTSTestKit  Already in HTS Lab Form')

--// HTSTestKitLotNumber //--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitLotNumber')
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

VALUES (N'HTSTestKitLotNumber'
, N'DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form'
, 1
, NULL
, 1
, GETDATE()
, 300
, NULL
, N'TestKitLotNumber'
, NULL
, 0)

END

ELSE

PRINT('TestKitLotNumber PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitLotNumber')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'HIVTest')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitLotNumber')
, N'Lot Number'
, 1, GETDATE()
, 1
, 3) -- Sequence
END

ELSE
PRINT ('HTSTestKitLotNumber  Already in HTS Lab Form')


--// Expiry Date //--

IF NOT EXISTS(SELECT PDFName FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitExpiryDate')
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

VALUES (N'HTSTestKitExpiryDate'
, N'DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form'
, 5
, NULL
, 1
, GETDATE()
, 300
, NULL
, N'TestKitExpiryDate'
, NULL
, 0)

END

ELSE

PRINT('TestKitExpiryDate PDF Already Exists!')

IF NOT EXISTS (SELECT * FROM Lnk_Forms WHERE FieldId = (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitExpiryDate')
AND FeatureId = (Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form'))
BEGIN
INSERT [dbo].[Lnk_Forms] 
([FeatureId], [SectionId], [FieldId]
, [FieldLabel], [UserId], [CreateDate], [Predefined]
, [Seq]) 

VALUES 
((Select TOP 1 FeatureID FROM mst_Feature WHERE FeatureName = N'HTS Lab Form')
, (Select TOP 1 SectionID FROM mst_Section WHERE SectionName = N'HIVTest')
, (Select TOP 1 Id FROM Mst_PreDefinedFields WHERE PDFName = N'HTSTestKitExpiryDate')
, N'Expiry Date'
, 1, GETDATE()
, 1
, 4) -- Sequence
END

ELSE
PRINT ('HTSTestKitExpiryDate  Already in HTS Lab Form')

