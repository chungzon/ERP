DECLARE @ConstraintName nvarchar(200)
SELECT
@ConstraintName = name FROM sys.indexes
WHERE [object_id] = OBJECT_ID('dbo.Orders_Reference') and name like 'UQ%'
IF @ConstraintName IS NOT NULL
EXEC('ALTER TABLE Orders_Reference DROP CONSTRAINT ' + @ConstraintName)