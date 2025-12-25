DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('Orders')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'isOffset' AND object_id = OBJECT_ID(N'Orders'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders DROP CONSTRAINT ' + @ConstraintName );
alter table Orders alter COLUMN isOffset int NOT NULL;
ALTER TABLE Orders ADD CONSTRAINT DF_isOffset DEFAULT (0) for isOffset;