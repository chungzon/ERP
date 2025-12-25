DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = f.name
FROM sys.foreign_keys f 
INNER JOIN sys.objects o ON f.parent_object_id = o.object_id 
INNER JOIN sys.schemas s ON o.schema_id = s.schema_id 
INNER JOIN sys.objects r ON f.referenced_object_id = r.object_id 
INNER JOIN sys.schemas sc ON r.schema_id = sc.schema_id 
where o.name = 'Orders_ReviewStatusRecord'
ORDER BY o.name
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders_ReviewStatusRecord DROP CONSTRAINT ' + @ConstraintName )