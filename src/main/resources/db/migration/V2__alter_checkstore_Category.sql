DECLARE @ConstraintName nvarchar(200)  
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('CheckStore') AND PARENT_COLUMN_ID = (SELECT column_id FROM sys.columns WHERE NAME = N'FirstCategory' AND object_id = OBJECT_ID(N'CheckStore'))  
IF @ConstraintName IS NOT NULL  
EXEC('ALTER TABLE CheckStore DROP CONSTRAINT ' + @ConstraintName)  

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('CheckStore') AND PARENT_COLUMN_ID = (SELECT column_id FROM sys.columns WHERE NAME = N'SecondCategory' AND object_id = OBJECT_ID(N'CheckStore'))  
IF @ConstraintName IS NOT NULL  
EXEC('ALTER TABLE CheckStore DROP CONSTRAINT ' + @ConstraintName)  

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('CheckStore') AND PARENT_COLUMN_ID = (SELECT column_id FROM sys.columns WHERE NAME = N'ThirdCategory' AND object_id = OBJECT_ID(N'CheckStore'))  
IF @ConstraintName IS NOT NULL  
EXEC('ALTER TABLE CheckStore DROP CONSTRAINT ' + @ConstraintName)  

alter table CheckStore alter COLUMN FirstCategory int null  
alter table CheckStore alter COLUMN SecondCategory int null  
alter table CheckStore alter COLUMN ThirdCategory int null  
exec sp_rename 'CheckStore.FirstCategory','FirstCategory_Id'  
exec sp_rename 'CheckStore.SecondCategory','SecondCategory_Id'  
exec sp_rename 'CheckStore.ThirdCategory','ThirdCategory_Id'  