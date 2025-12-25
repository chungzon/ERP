DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('Orders_ShoppingInfo')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'PurchaserCellphone' AND object_id = OBJECT_ID(N'Orders_ShoppingInfo'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders_ShoppingInfo DROP CONSTRAINT ' + @ConstraintName );
alter table Orders_ShoppingInfo alter COLUMN PurchaserCellphone nvarchar(12) NOT NULL;
ALTER TABLE Orders_ShoppingInfo ADD CONSTRAINT DF_Orders_ShoppingInfo_PurchaserCellphone DEFAULT '' for PurchaserCellphone

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('Orders_ShoppingInfo')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'RecipientCellphone' AND object_id = OBJECT_ID(N'Orders_ShoppingInfo'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE Orders_ShoppingInfo DROP CONSTRAINT ' + @ConstraintName );
alter table Orders_ShoppingInfo alter COLUMN RecipientCellphone nvarchar(12) NOT NULL;
ALTER TABLE Orders_ShoppingInfo ADD CONSTRAINT DF_Orders_ShoppingInfo_RecipientCellphone DEFAULT '' for RecipientCellphone

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('SubBill')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'Remark' AND object_id = OBJECT_ID(N'SubBill'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill DROP CONSTRAINT ' + @ConstraintName );
alter table SubBill alter COLUMN Remark nvarchar(MAX) NOT NULL;
ALTER TABLE SubBill ADD CONSTRAINT DF_SubBill_Remark DEFAULT '' for Remark

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('SubBill')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'CashierRemark' AND object_id = OBJECT_ID(N'SubBill'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill DROP CONSTRAINT ' + @ConstraintName );
alter table SubBill alter COLUMN CashierRemark nvarchar(MAX) NOT NULL;
ALTER TABLE SubBill ADD CONSTRAINT DF_SubBill_CashierRemark DEFAULT '' for CashierRemark

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('SubBill_ShoppingInfo')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'PurchaserCellphone' AND object_id = OBJECT_ID(N'SubBill_ShoppingInfo'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill_ShoppingInfo DROP CONSTRAINT ' + @ConstraintName );
alter table SubBill_ShoppingInfo alter COLUMN PurchaserCellphone nvarchar(12) NOT NULL;
ALTER TABLE SubBill_ShoppingInfo ADD CONSTRAINT DF_SubBill_ShoppingInfo_PurchaserCellphone DEFAULT '' for PurchaserCellphone

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('SubBill_ShoppingInfo')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'RecipientCellphone' AND object_id = OBJECT_ID(N'SubBill_ShoppingInfo'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill_ShoppingInfo DROP CONSTRAINT ' + @ConstraintName );
alter table SubBill_ShoppingInfo alter COLUMN RecipientCellphone nvarchar(12) NOT NULL;
ALTER TABLE SubBill_ShoppingInfo ADD CONSTRAINT DF_SubBill_ShoppingInfo_RecipientCellphone DEFAULT '' for RecipientCellphone

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('SubBill_Items')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'Remark' AND object_id = OBJECT_ID(N'SubBill_Items'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE SubBill_Items DROP CONSTRAINT ' + @ConstraintName );
alter table SubBill_Items alter COLUMN Remark nvarchar(MAX) NOT NULL;
ALTER TABLE SubBill_Items ADD CONSTRAINT DF_SubBill_Items_Remark DEFAULT '' for Remark

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('ReturnOrder')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'Remark' AND object_id = OBJECT_ID(N'ReturnOrder'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE ReturnOrder DROP CONSTRAINT ' + @ConstraintName );
alter table ReturnOrder alter COLUMN Remark nvarchar(MAX) NOT NULL;
ALTER TABLE ReturnOrder ADD CONSTRAINT DF_ReturnOrder_Remark DEFAULT '' for Remark

SELECT @ConstraintName = Name FROM SYS.DEFAULT_CONSTRAINTS WHERE PARENT_OBJECT_ID = OBJECT_ID('ReturnOrder_Items')
AND PARENT_COLUMN_ID = ( SELECT column_id FROM sys.columns WHERE NAME = N'Remark' AND object_id = OBJECT_ID(N'ReturnOrder_Items'))
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE ReturnOrder_Items DROP CONSTRAINT ' + @ConstraintName );
alter table ReturnOrder_Items alter COLUMN Remark nvarchar(MAX) NOT NULL;
ALTER TABLE ReturnOrder_Items ADD CONSTRAINT DF_Return_Items_Remark DEFAULT '' for Remark