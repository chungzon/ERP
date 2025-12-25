DECLARE @ConstraintName nvarchar(200) 
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('Orders_items') 
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'Remark' AND object_id = OBJECT_ID(N'Orders_items')) 
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders_items DROP CONSTRAINT ' + @ConstraintName );
alter table Orders_Items alter COLUMN Remark nvarchar(MAX) NOT NULL;
ALTER TABLE Orders_Items ADD CONSTRAINT DF_Orders_Items_Remark DEFAULT '' for Remark