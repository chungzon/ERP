DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS
WHERE PARENT_OBJECT_ID = OBJECT_ID('ProductGroup') AND PARENT_COLUMN_ID = (SELECT column_id FROM sys.columns WHERE NAME = N'SinglePrice' AND object_id = OBJECT_ID(N'ProductGroup'))
IF @ConstraintName IS NOT NULL EXEC('ALTER TABLE ProductGroup DROP CONSTRAINT ' + @ConstraintName)

ALTER TABLE ProductGroup ALTER COLUMN SinglePrice decimal(9,1) NOT NULL ALTER TABLE ProductGroup ADD DEFAULT ((0)) FOR SinglePrice
ALTER TABLE ProductGroup ALTER COLUMN SmallSinglePrice decimal(9,1)