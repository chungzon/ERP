CREATE TABLE [dbo].[Orders_ProductGroup_Price] (
[id] int NOT NULL IDENTITY(1,1),
[order_id] int NULL ,
[total_Price_none_tax] int NOT NULL ,
[tax] int NOT NULL ,
[discount] int NOT NULL ,
[total_Price_include_tax] int NOT NULL
)

ALTER TABLE [dbo].[Orders_ProductGroup_Price] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[Orders_ProductGroup_Price] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION