IF NOT EXISTS(SELECT name from sys.tables WHERE name = 'dtl_HTSEncounter')
BEGIN
CREATE TABLE [dbo].[dtl_HTSEncounter](
	[HTSEncounterID] [int] IDENTITY(1,1) NOT NULL,
	[Ptn_Pk] [int] NOT NULL,
	[Visit_Pk] [int] NOT NULL,
	[LocationID] [int] NOT NULL,
	[KeyPopulationID] [int] NULL,
	[KeyPopulationText] varchar(max) NULL,
	[EverTestedID] [int] NULL,
	[RetestMonths] decimal(18,2) NULL,
	[SelfTestedID] INT NULL,	
	[ConsentID] INT NULL,
	[TestedAsID] INT NULL,
	[PartnerClientCode] VARCHAR(100) NULL,
	[StrategyID] INT NULL,
	[FinalResultID] INT NULL,
	[FinalResultGivenID] INT NULL,
	[CoupleDiscordantID] INT NULL,
	[TBScreeningID] INT NULL,
	[LinkedToCareID] INT NULL,
	[CCCNumber] VARCHAR(100) NULL,
	[AdditionalRemarks] VARCHAR(MAX) NULL,
	[CreateDate] datetime NULL,
	[UserID] int NULL,
 CONSTRAINT [PK_dtl_HTSEncounter] PRIMARY KEY CLUSTERED 
(
	[HTSEncounterID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

PRINT ('dtl_HTSEncounter Created')
END

IF NOT EXISTS(SELECT name from sys.tables WHERE name = 'dtl_PatientDisability')
BEGIN
CREATE TABLE [dbo].[dtl_PatientDisability](
	[PatientDisabilityID] [int] IDENTITY(1,1) NOT NULL,
	[Ptn_Pk] [int] NOT NULL,
	[Visit_Pk] [int] NOT NULL,
	[LocationID] [int] NOT NULL,
	[DisabilityID] [int] NULL,
	[DisabilityText] varchar(max) NULL,
	[CreateDate] datetime NULL,
	[UserID] int NULL,
 CONSTRAINT [PK_dtl_PatientDisability] PRIMARY KEY CLUSTERED 
(
	[PatientDisabilityID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

PRINT ('dtl_PatientDisability Created!')
END


IF NOT EXISTS (SELECT name FROM sys.tables WHERE name = N'DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form')
BEGIN
CREATE TABLE DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form(
ID [int] IDENTITY(1,1) NOT NULL,
Ptn_Pk [int] NOT NULL,
Visit_Pk [int] NOT NULL,
LocationId [int] NOT NULL,
UserId [int] NOT NULL,
SectionId [int] NULL,
FormID [int] NULL,
CreateDate datetime NULL,
UpdateDate datetime NULL,
[TestKitID] [int] NULL,
[TestKitCategoryID] [INT] NULL,
[TestKitLotNumber] VARCHAR(100) NULL,
[TestKitExpiryDate] DATETIME NULL,
[HIVTestResultID] INT NULL,
 CONSTRAINT [PK_DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

PRINT ('DTL_CUSTOMFORM_HIVTest_HTS_Lab_Form Created!')
END

--ord_Visit IQMobileVisitType
IF NOT EXISTS(Select name from sys.columns WHERE name = N'IQMobileVisitType')
BEGIN
ALTER TABLE ord_Visit ADD IQMobileVisitType INT NULL
END
