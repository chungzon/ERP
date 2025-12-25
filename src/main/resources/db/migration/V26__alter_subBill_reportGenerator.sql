IF OBJECT_ID('dbo.SubBill_ReportGenerator', 'U') IS NOT NULL
DROP TABLE [dbo].[SubBill_ReportGenerator];

CREATE TABLE [dbo].[SubBill_ReportGenerator] (
[id] int NOT NULL IDENTITY(1,1) ,
[subBill_id] int NULL ,
[export_manufacturer_id] int NULL ,
[remark] nvarchar(250) NULL ,
[insert_dateTime] datetime NOT NULL 
)
ALTER TABLE [dbo].[SubBill_ReportGenerator] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[SubBill_ReportGenerator] ADD FOREIGN KEY ([export_manufacturer_id]) REFERENCES [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
ALTER TABLE [dbo].[SubBill_ReportGenerator] ADD FOREIGN KEY ([subBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE

CREATE TABLE [dbo].[SubBill_ReportGenerator_item] (
[id] int NOT NULL IDENTITY(1,1) ,
[report_generator_id] int NULL ,
[item_number] int NOT NULL ,
[item_name] nvarchar(255) NOT NULL 
)
ALTER TABLE [dbo].[SubBill_ReportGenerator_item] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[SubBill_ReportGenerator_item] ADD FOREIGN KEY ([report_generator_id]) REFERENCES [dbo].[SubBill_ReportGenerator] ([id]) ON DELETE SET NULL ON UPDATE CASCADE