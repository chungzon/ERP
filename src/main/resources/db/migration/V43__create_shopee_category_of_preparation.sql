CREATE TABLE [dbo].[ShopeeCategoryOfPreparation] (
[id] int NOT NULL IDENTITY(1,1) ,
[p_id] int NULL ,
[sc_id] int NULL ,
[shopee_display] nvarchar(300) NULL 
)
GO
ALTER TABLE [dbo].[ShopeeCategoryOfPreparation] ADD PRIMARY KEY ([id])
GO
ALTER TABLE [dbo].[ShopeeCategoryOfPreparation] ADD UNIQUE ([p_id] ASC)
GO
ALTER TABLE [dbo].[ShopeeCategoryOfPreparation] ADD FOREIGN KEY ([sc_id]) REFERENCES [dbo].[ShopeeCategory] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[ShopeeCategoryOfPreparation] ADD FOREIGN KEY ([p_id]) REFERENCES [dbo].[PreparativeProduction] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO