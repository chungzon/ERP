CREATE TABLE [dbo].[SubBill_ExportQuotationRecord] (
[id] int NOT NULL IDENTITY(1,1) ,
[subBill_id] int NULL ,
[export_content] int NOT NULL ,
[export_format] int NOT NULL ,
[export_manufacturer_id] int NULL ,
[status] int NOT NULL ,
[insert_dateTime] datetime NOT NULL 
)
ALTER TABLE [dbo].[SubBill_ExportQuotationRecord] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[SubBill_ExportQuotationRecord] ADD FOREIGN KEY ([export_manufacturer_id]) REFERENCES [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
ALTER TABLE [dbo].[SubBill_ExportQuotationRecord] ADD FOREIGN KEY ([subBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE