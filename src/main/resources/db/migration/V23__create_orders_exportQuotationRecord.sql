CREATE TABLE [dbo].[Orders_ExportQuotationRecord] (
[id] int NOT NULL IDENTITY(1,1) ,
[order_id] int NULL ,
[export_content] int NOT NULL ,
[export_format] int NOT NULL ,
[export_manufacturer_id] int NULL ,
[status] int NOT NULL ,
[insert_dateTime] datetime NOT NULL 
)
ALTER TABLE [dbo].[Orders_ExportQuotationRecord] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[Orders_ExportQuotationRecord] ADD FOREIGN KEY ([export_manufacturer_id]) REFERENCES [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
ALTER TABLE [dbo].[Orders_ExportQuotationRecord] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE