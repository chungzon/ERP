CREATE TABLE [dbo].[Orders_ReportGenerator] (
[id] int NOT NULL IDENTITY(1,1) ,
[order_id] int NULL ,
[company_name] nvarchar(50) NOT NULL ,
[product_list] nvarchar(MAX) NULL ,
[remark] nvarchar(200) NULL 
)

ALTER TABLE [dbo].[Orders_ReportGenerator] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[Orders_ReportGenerator] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE