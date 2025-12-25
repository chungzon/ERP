CREATE TABLE [dbo].[SubBill_Picture] (
[id] int NOT NULL IDENTITY(1,1) ,
[SubBill_id] int NULL ,
[ItemNumber] int NOT NULL DEFAULT '' ,
[Picture] nvarchar(MAX) NOT NULL DEFAULT '' ,
[Source] nvarchar(20) NOT NULL DEFAULT '' 
)
ALTER TABLE [dbo].[SubBill_Picture] ADD PRIMARY KEY ([id], [ItemNumber], [Source])
ALTER TABLE [dbo].[SubBill_Picture] ADD FOREIGN KEY ([SubBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE