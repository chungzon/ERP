DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('SubBill')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'isOffset' AND object_id = OBJECT_ID(N'SubBill'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill DROP CONSTRAINT ' + @ConstraintName );
alter table SubBill alter COLUMN isOffset int NOT NULL;
ALTER TABLE SubBill ADD CONSTRAINT DF_subBill_isOffset DEFAULT (0) for isOffset;