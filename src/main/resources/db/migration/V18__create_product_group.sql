CREATE TABLE [dbo].[ProductGroup] (
[id] int NOT NULL IDENTITY(1,1) , 
[ItemNumber] int NOT NULL , 
[GroupName] nvarchar(255) NOT NULL , 
[Quantity] int NOT NULL , 
[Unit] nvarchar(10) NOT NULL , 
[PriceAmount] int NOT NULL  
)
ALTER TABLE [dbo].[ProductGroup] ADD PRIMARY KEY ([id]) 