CREATE TABLE [dbo].[Orders_ReviewStatus] (
[id] int NOT NULL IDENTITY(1,1) ,
[order_id] int NULL ,
[review_status] int NOT NULL DEFAULT ((0)) ,
[review_object] int NOT NULL 
)
ALTER TABLE [dbo].[Orders_ReviewStatus] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[Orders_ReviewStatus] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE