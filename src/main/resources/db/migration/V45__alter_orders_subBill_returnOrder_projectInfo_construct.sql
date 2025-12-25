DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('Orders_ProjectInfo')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'ProjectName' AND object_id = OBJECT_ID(N'Orders_ProjectInfo'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders_ProjectInfo DROP CONSTRAINT ' + @ConstraintName );
alter table Orders_ProjectInfo alter COLUMN ProjectName nvarchar(MAX) NOT NULL;
ALTER TABLE Orders_ProjectInfo ADD CONSTRAINT DF_Orders_ProjectInfo_ProjectName DEFAULT '' for ProjectName;

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('SubBill_ProjectInfo')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'ProjectName' AND object_id = OBJECT_ID(N'SubBill_ProjectInfo'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill_ProjectInfo DROP CONSTRAINT ' + @ConstraintName );
alter table SubBill_ProjectInfo alter COLUMN ProjectName nvarchar(MAX) NOT NULL;
ALTER TABLE SubBill_ProjectInfo ADD CONSTRAINT DF_SubBill_ProjectInfo_ProjectName DEFAULT '' for ProjectName;

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('ReturnOrder_ProjectInfo')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'ProjectName' AND object_id = OBJECT_ID(N'ReturnOrder_ProjectInfo'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE ReturnOrder_ProjectInfo DROP CONSTRAINT ' + @ConstraintName );
alter table ReturnOrder_ProjectInfo alter COLUMN ProjectName nvarchar(MAX) NOT NULL;
ALTER TABLE ReturnOrder_ProjectInfo ADD CONSTRAINT DF_ReturnOrder_ProjectInfo_ProjectName DEFAULT '' for ProjectName;