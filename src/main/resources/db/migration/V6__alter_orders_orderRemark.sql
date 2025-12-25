DECLARE @ConstraintName nvarchar (200) 
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('Orders') AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'Remark' AND object_id = OBJECT_ID(N'Orders') ) IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders DROP CONSTRAINT ' + @ConstraintName ) 
alter table Orders alter COLUMN Remark nvarchar(MAX) not null
ALTER TABLE Orders ADD CONSTRAINT DF_Remark DEFAULT '' for Remark
