DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = CONSTRAINT_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = 'ProductCategory'
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE ProductCategory DROP CONSTRAINT ' + @ConstraintName );
ALTER TABLE ProductCategory ADD PRIMARY KEY (id);
ALTER TABLE ProductCategory ADD UNIQUE (CategoryID, CategoryLayer);