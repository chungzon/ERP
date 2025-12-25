DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = name
FROM   sys.key_constraints
WHERE  [type] = 'PK'
AND [parent_object_id] = Object_id('dbo.Orders_Items')
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders_Items DROP CONSTRAINT ' + @ConstraintName ) 
ALTER TABLE [dbo].[Orders_Items] ADD PRIMARY KEY ([id])

SELECT @ConstraintName = name
FROM   sys.key_constraints
WHERE  [type] = 'PK'
AND [parent_object_id] = Object_id('dbo.SubBill_Items')
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill_Items DROP CONSTRAINT ' + @ConstraintName ) 
ALTER TABLE [dbo].[SubBill_Items] ADD PRIMARY KEY ([id])

SELECT @ConstraintName = name
FROM   sys.key_constraints
WHERE  [type] = 'PK'
AND [parent_object_id] = Object_id('dbo.ReturnOrder_Items')
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE ReturnOrder_Items DROP CONSTRAINT ' + @ConstraintName ) 
ALTER TABLE [dbo].[ReturnOrder_Items] ADD PRIMARY KEY ([id])

CREATE TABLE [dbo].[Orders_Items_Group] ( 
[id] int NOT NULL IDENTITY(1,1) , 
[item_id] int NULL , 
[item_quantity] int NOT NULL , 
[group_id] int NULL  
) 
ALTER TABLE [dbo].[Orders_Items_Group] ADD PRIMARY KEY ([id]) 
ALTER TABLE [dbo].[Orders_Items_Group] ADD FOREIGN KEY ([group_id]) REFERENCES [dbo].[ProductGroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE 
ALTER TABLE [dbo].[Orders_Items_Group] ADD FOREIGN KEY ([item_id]) REFERENCES [dbo].[Orders_Items] ([id]) ON DELETE SET NULL ON UPDATE CASCADE 