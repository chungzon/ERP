CREATE TABLE [dbo].[SubBill_ReportGenerator] (
[id] int NOT NULL IDENTITY(1,1) ,
[subBill_id] int NULL ,
[company_name] nvarchar(50) NOT NULL ,
[product_list] nvarchar(MAX) NULL ,
[remark] nvarchar(200) NULL 
)

ALTER TABLE [dbo].[SubBill_ReportGenerator] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[SubBill_ReportGenerator] ADD FOREIGN KEY ([subBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE