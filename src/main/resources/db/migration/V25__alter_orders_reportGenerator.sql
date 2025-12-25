IF OBJECT_ID('dbo.Orders_ReportGenerator', 'U') IS NOT NULL
DROP TABLE [dbo].[Orders_ReportGenerator];

CREATE TABLE [dbo].[Orders_ReportGenerator] (
[id] int NOT NULL IDENTITY(1,1) ,
[order_id] int NULL ,
[export_manufacturer_id] int NULL ,
[remark] nvarchar(255) NULL ,
[insert_dateTime] datetime NOT NULL 
)
ALTER TABLE [dbo].[Orders_ReportGenerator] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[Orders_ReportGenerator] ADD FOREIGN KEY ([export_manufacturer_id]) REFERENCES [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
ALTER TABLE [dbo].[Orders_ReportGenerator] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE

CREATE TABLE [dbo].[Orders_ReportGenerator_item] (
[id] int NOT NULL IDENTITY(1,1) ,
[report_generator_id] int NULL ,
[item_number] int NOT NULL ,
[item_name] nvarchar(255) NOT NULL 
)
ALTER TABLE [dbo].[Orders_ReportGenerator_item] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[Orders_ReportGenerator_item] ADD FOREIGN KEY ([report_generator_id]) REFERENCES [dbo].[Orders_ReportGenerator] ([id]) ON DELETE SET NULL ON UPDATE CASCADE