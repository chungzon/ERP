DECLARE @ConstraintName nvarchar(200)
SELECT
@ConstraintName = name FROM sys.indexes
WHERE [object_id] = OBJECT_ID('dbo.SubBill_Reference') and name like 'UQ%'
IF @ConstraintName IS NOT NULL
EXEC('ALTER TABLE SubBill_Reference DROP CONSTRAINT ' + @ConstraintName)