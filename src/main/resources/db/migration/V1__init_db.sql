/*
Navicat SQL Server Data Transfer

Source Server         : TestSql
Source Server Version : 105000
Source Host           : 203.64.91.19:1433
Source Database       : Test_Way_Len
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 105000
File Encoding         : 65001

Date: 2021-03-24 17:55:47
*/


-- ----------------------------
-- Table structure for Bank
-- ----------------------------
CREATE TABLE [dbo].[Bank] (
[id] int NOT NULL IDENTITY(1,1) ,
[BankCode] nvarchar(3) NOT NULL ,
[BankNickName] nvarchar(50) NOT NULL ,
[BankName] nvarchar(50) NOT NULL ,
[ContactPerson1] nvarchar(20) NOT NULL ,
[ContactPerson2] nvarchar(20) NOT NULL ,
[Telephone1] nvarchar(25) NOT NULL ,
[Telephone2] nvarchar(25) NOT NULL ,
[Fax] nvarchar(25) NOT NULL ,
[Address] nvarchar(255) NOT NULL ,
[Remark] nvarchar(255) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for BigGoFilter
-- ----------------------------
CREATE TABLE [dbo].[BigGoFilter] (
[StoreName] nvarchar(255) NOT NULL DEFAULT '' ,
[WebKeyWord] nvarchar(255) NOT NULL DEFAULT '' ,
[Status] bit NOT NULL DEFAULT ((0)) ,
[Resource] int NOT NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'BigGoFilter', 
'COLUMN', N'Resource')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'0：商城， 1：拍賣'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'BigGoFilter'
, @level2type = 'COLUMN', @level2name = N'Resource'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'0：商城， 1：拍賣'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'BigGoFilter'
, @level2type = 'COLUMN', @level2name = N'Resource'
GO

-- ----------------------------
-- Table structure for BookCase
-- ----------------------------
CREATE TABLE [dbo].[BookCase] (
[ProductArea] nvarchar(20) NOT NULL ,
[ProductFloor] nvarchar(20) NOT NULL ,
[Level] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_genb2b
-- ----------------------------
CREATE TABLE [dbo].[category_genb2b] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(50) NULL ,
[Name] nvarchar(50) NULL ,
[Value] nvarchar(50) NOT NULL ,
[Layer] nvarchar(1) NOT NULL ,
[LastUpdate_Category] bit NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_jinghao
-- ----------------------------
CREATE TABLE [dbo].[category_jinghao] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(50) NULL ,
[Name] nvarchar(80) NULL ,
[Value] nvarchar(50) NOT NULL ,
[Layer] nvarchar(50) NOT NULL ,
[LastUpdate_Category] nvarchar(1) NOT NULL ,
[Exist] nvarchar(1) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_ktnet
-- ----------------------------
CREATE TABLE [dbo].[category_ktnet] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(50) NULL ,
[Name] nvarchar(50) NULL ,
[Value] nvarchar(50) NOT NULL ,
[Layer] nvarchar(10) NOT NULL ,
[LastUpdate_Category] bit NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_ruten
-- ----------------------------
CREATE TABLE [dbo].[category_ruten] (
[category] nvarchar(50) NOT NULL ,
[name] nvarchar(50) NOT NULL ,
[value] nvarchar(50) NOT NULL DEFAULT '' ,
[layer] nvarchar(10) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for category_senao
-- ----------------------------
CREATE TABLE [dbo].[category_senao] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(50) NULL ,
[Name] nvarchar(50) NULL ,
[Value] nvarchar(50) NOT NULL ,
[Layer] nvarchar(50) NOT NULL ,
[LastUpdate_Category] bit NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_synnex
-- ----------------------------
CREATE TABLE [dbo].[category_synnex] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(50) NULL ,
[Name] nvarchar(80) NULL ,
[Value] nvarchar(50) NOT NULL ,
[Layer] nvarchar(50) NOT NULL ,
[LastUpdate_Category] bit NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_unitech
-- ----------------------------
CREATE TABLE [dbo].[category_unitech] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(50) NULL ,
[Name] nvarchar(80) NULL ,
[Value] nvarchar(50) NOT NULL ,
[Layer] nvarchar(50) NOT NULL ,
[LastUpdate_Category] bit NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_weblink
-- ----------------------------
CREATE TABLE [dbo].[category_weblink] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(50) NULL ,
[Name] nvarchar(80) NULL ,
[Value] nvarchar(50) NOT NULL ,
[Layer] nvarchar(50) NOT NULL ,
[LastUpdate_Category] bit NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for category_xander
-- ----------------------------
CREATE TABLE [dbo].[category_xander] (
[id] int NOT NULL IDENTITY(1,1) ,
[Category] nvarchar(100) NULL ,
[Name] nvarchar(100) NULL ,
[Value] nvarchar(100) NOT NULL ,
[Layer] nvarchar(10) NOT NULL ,
[LastUpdate_Category] bit NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_genb2b
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_genb2b] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(50) NOT NULL ,
[Name] nvarchar(80) NOT NULL ,
[NewCategory] nvarchar(50) NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_jinghao
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_jinghao] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(5) NOT NULL ,
[Name] nvarchar(80) NULL ,
[NewCategory] nvarchar(5) NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_ktnet
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_ktnet] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(50) NOT NULL ,
[Name] nvarchar(80) NOT NULL ,
[NewCategory] nvarchar(50) NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_senao
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_senao] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(50) NOT NULL ,
[Name] nvarchar(80) NOT NULL ,
[NewCategory] nvarchar(50) NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_synnex
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_synnex] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(50) NOT NULL ,
[Name] nvarchar(50) NOT NULL ,
[NewCategory] nvarchar(50) NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_unitech
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_unitech] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(5) NOT NULL ,
[Name] nvarchar(80) NULL ,
[NewCategory] nvarchar(5) NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_weblink
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_weblink] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(50) NOT NULL ,
[Name] nvarchar(50) NOT NULL ,
[NewCategory] nvarchar(50) NOT NULL ,
[Exist] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for categorymapping_xander
-- ----------------------------
CREATE TABLE [dbo].[categorymapping_xander] (
[id] int NOT NULL IDENTITY(1,1) ,
[FirstCategory] nvarchar(50) NOT NULL ,
[Name] nvarchar(50) NOT NULL ,
[NewCategory] nvarchar(50) NOT NULL ,
[Exist] bit NOT NULL
)


GO

-- ----------------------------
-- Table structure for checkstore
-- ----------------------------
CREATE TABLE [dbo].[checkstore] (
[ProductName] nvarchar(200) NOT NULL DEFAULT '' ,
[ProductCode] nvarchar(50) NOT NULL ,
[Unit] nvarchar(10) NOT NULL ,
[BatchPrice] decimal(9,1) NOT NULL ,
[SinglePrice] decimal(9,1) NOT NULL ,
[Pricing] decimal(9,1) NOT NULL ,
[VipPrice1] decimal(9,1) NOT NULL ,
[VipPrice2] decimal(9,1) NOT NULL ,
[VipPrice3] decimal(9,1) NOT NULL ,
[Brand] nvarchar(50) NOT NULL DEFAULT '' ,
[Describe] nvarchar(MAX) NOT NULL DEFAULT '' ,
[Remark] nvarchar(MAX) NOT NULL DEFAULT '' ,
[NewFirstCategory] nvarchar(20) NOT NULL DEFAULT '' ,
[FirstCategory] nvarchar(15) NOT NULL DEFAULT '' ,
[SecondCategory] nvarchar(15) NOT NULL DEFAULT '' ,
[ThirdCategory] nvarchar(15) NOT NULL DEFAULT '' ,
[SupplyStatus] nvarchar(50) NOT NULL DEFAULT '' ,
[Picture1] nvarchar(MAX) NULL DEFAULT '' ,
[Picture2] nvarchar(MAX) NULL DEFAULT '' ,
[Picture3] nvarchar(MAX) NULL DEFAULT '' ,
[Vendor] nvarchar(30) NOT NULL DEFAULT '' ,
[VendorCode] nvarchar(20) NOT NULL DEFAULT '' ,
[KeyinDate] nvarchar(20) NOT NULL DEFAULT '' ,
[UpdateDate] nvarchar(20) NOT NULL DEFAULT '' ,
[PreviousStatus] int NULL DEFAULT '' ,
[Status] int NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Table structure for CompanyBankInfo
-- ----------------------------
CREATE TABLE [dbo].[CompanyBankInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[BankID] int NOT NULL DEFAULT '' ,
[BankAccount] nvarchar(14) NOT NULL DEFAULT '' ,
[BankBranch] nvarchar(50) NOT NULL DEFAULT '' ,
[BankAccountName] nvarchar(50) NOT NULL DEFAULT '' ,
[AccountNickName] nvarchar(50) NOT NULL DEFAULT '' ,
[Remark] nvarchar(255) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for connectparameters
-- ----------------------------
CREATE TABLE [dbo].[connectparameters] (
[name] nvarchar(40) NULL ,
[value] nvarchar(70) NULL ,
[Checked] nvarchar(1) NULL 
)


GO

-- ----------------------------
-- Table structure for Customer
-- ----------------------------
CREATE TABLE [dbo].[Customer] (
[id] int NOT NULL IDENTITY(1,1) ,
[ObjectID] nvarchar(20) NOT NULL ,
[ObjectName] nvarchar(80) NOT NULL DEFAULT '' ,
[ObjectNickName] nvarchar(80) NOT NULL DEFAULT '' ,
[PersonInCharge] nvarchar(20) NOT NULL DEFAULT '' ,
[ContactPerson] nvarchar(20) NOT NULL DEFAULT '' ,
[Email] nvarchar(150) NOT NULL DEFAULT '' ,
[MemberID] nvarchar(20) NOT NULL DEFAULT '' ,
[InvoiceTitle] nvarchar(50) NOT NULL DEFAULT '' ,
[TaxIDNumber] nvarchar(8) NOT NULL DEFAULT '' ,
[OrderTax] bit NOT NULL DEFAULT '' ,
[ReceivableDiscount] decimal(3,2) NOT NULL ,
[PrintPricing] bit NOT NULL DEFAULT '' ,
[SaleModel] int NOT NULL ,
[SaleDiscount] decimal(3,2) NOT NULL ,
[Remark] nvarchar(255) NOT NULL DEFAULT '' ,
[StoreCode] nvarchar(10) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for Customer_Address
-- ----------------------------
CREATE TABLE [dbo].[Customer_Address] (
[id] int NOT NULL IDENTITY(1,1) ,
[Customer_id] int NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[CompanyAddress] nvarchar(255) NOT NULL DEFAULT '' ,
[DeliveryAddress] varchar(255) NOT NULL DEFAULT '' ,
[InvoiceAddress] nvarchar(255) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for Customer_Phone
-- ----------------------------
CREATE TABLE [dbo].[Customer_Phone] (
[id] int NOT NULL IDENTITY(1,1) ,
[Customer_id] int NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[Telephone1] nvarchar(25) NOT NULL DEFAULT '' ,
[Telephone2] nvarchar(25) NOT NULL DEFAULT '' ,
[Cellphone] nvarchar(12) NOT NULL DEFAULT '' ,
[Fax] nvarchar(25) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for Customer_ReceiveInfo
-- ----------------------------
CREATE TABLE [dbo].[Customer_ReceiveInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[Customer_id] int NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[ReceivableDay] int NOT NULL DEFAULT '' ,
[IsCheckoutByMonth] bit NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Table structure for customer_repair_order
-- ----------------------------
CREATE TABLE [dbo].[customer_repair_order] (
[id] int NOT NULL IDENTITY(1,1) ,
[number] bigint NULL ,
[repair_order_id] int NULL ,
[receive_date] date NULL ,
[address] nvarchar(100) NOT NULL ,
[customer_id] int NULL ,
[shipping_fee] float(53) NULL DEFAULT ((0)) ,
[tax] float(53) NULL DEFAULT ((0)) ,
[charge] bit NULL DEFAULT ((1)) ,
[description] nvarchar(MAX) NULL DEFAULT '' ,
[complete_date] date NULL ,
[status_id] int NULL ,
[comment] nvarchar(MAX) NULL ,
[warranty] bit NULL DEFAULT ((0)) ,
[create_date] date NOT NULL 
)


GO

-- ----------------------------
-- Table structure for customer_repair_order_item
-- ----------------------------
CREATE TABLE [dbo].[customer_repair_order_item] (
[id] int NOT NULL IDENTITY(1,1) ,
[repair_item_id] int NULL ,
[customer_order_id] int NULL ,
[count] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for IAECrawlerAccount
-- ----------------------------
CREATE TABLE [dbo].[IAECrawlerAccount] (
[id] int NOT NULL IDENTITY(1,1) ,
[ObjectID] nvarchar(20) NULL ,
[Account] nvarchar(20) NOT NULL ,
[Password] varchar(20) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for IAECrawlerAccount_Belong
-- ----------------------------
CREATE TABLE [dbo].[IAECrawlerAccount_Belong] (
[id] int NOT NULL IDENTITY(1,1) ,
[account_id] int NULL ,
[belong_id] int NULL 
)


GO

-- ----------------------------
-- Table structure for IAECrawlerAccount_ExportQuotation_Manufacturer
-- ----------------------------
CREATE TABLE [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] (
[id] int NOT NULL IDENTITY(1,1) ,
[Account] nvarchar(8) NULL ,
[Manufacturer_id] int NULL ,
[ManufacturerNickName] nvarchar(50) NOT NULL ,
[DefaultSelect] bit NOT NULL ,
[ExportQuotation] bit NOT NULL DEFAULT ('0') 
)


GO

-- ----------------------------
-- Table structure for IAECrawlerAccount_Payment
-- ----------------------------
CREATE TABLE [dbo].[IAECrawlerAccount_Payment] (
[id] int NOT NULL IDENTITY(1,1) ,
[Account_id] int NOT NULL ,
[Payment_id] int NOT NULL ,
[Belong_id] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for IAECrawlerBelong
-- ----------------------------
CREATE TABLE [dbo].[IAECrawlerBelong] (
[id] int NOT NULL IDENTITY(1,1) ,
[BelongName] nvarchar(20) NOT NULL ,
[BelongURL] nvarchar(255) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for IAECrawlerInvoice_Order
-- ----------------------------
CREATE TABLE [dbo].[IAECrawlerInvoice_Order] (
[id] int NOT NULL IDENTITY(1,1) ,
[invoice_id] int NOT NULL ,
[order_id] int NULL ,
[subbill_id] int NULL 
)


GO

-- ----------------------------
-- Table structure for IAECrawlerPayment
-- ----------------------------
CREATE TABLE [dbo].[IAECrawlerPayment] (
[id] int NOT NULL IDENTITY(1,1) ,
[RanKey] int NOT NULL ,
[SummonsCode] nvarchar(20) NOT NULL ,
[ObjectID] nvarchar(20) NULL ,
[PayDate] date NOT NULL ,
[PayAmount] int NOT NULL ,
[RemittanceFee] int NOT NULL ,
[BankAccount] nvarchar(14) NOT NULL ,
[ProjectCode] nvarchar(30) NOT NULL ,
[InvoiceContent] nvarchar(255) NOT NULL ,
[InvoiceDate] date NULL ,
[InvoiceNumber] nvarchar(10) NULL ,
[InvoiceAmount] int NULL ,
[Status] int NOT NULL ,
[ReviewStatus] bit NOT NULL ,
[Source] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for logindata
-- ----------------------------
CREATE TABLE [dbo].[logindata] (
[name] nvarchar(50) NOT NULL ,
[value] nvarchar(MAX) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for Manufacturer
-- ----------------------------
CREATE TABLE [dbo].[Manufacturer] (
[id] int NOT NULL IDENTITY(1,1) ,
[ObjectID] nvarchar(20) NOT NULL ,
[ObjectName] nvarchar(80) NOT NULL DEFAULT '' ,
[ObjectNickName] nvarchar(80) NOT NULL DEFAULT '' ,
[PersonInCharge] nvarchar(20) NOT NULL DEFAULT '' ,
[ContactPerson] nvarchar(20) NOT NULL DEFAULT '' ,
[Email] nvarchar(150) NOT NULL DEFAULT '' ,
[InvoiceTitle] nvarchar(50) NOT NULL DEFAULT '' ,
[TaxIDNumber] nvarchar(8) NOT NULL DEFAULT '' ,
[OrderTax] bit NOT NULL DEFAULT '' ,
[PayableDiscount] decimal(3,2) NOT NULL ,
[Remark] nvarchar(255) NOT NULL DEFAULT '' ,
[DefaultPaymentMethod] int NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Table structure for Manufacturer_Address
-- ----------------------------
CREATE TABLE [dbo].[Manufacturer_Address] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[Manufacturer_id] int NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[CompanyAddress] nvarchar(255) NOT NULL DEFAULT '' ,
[DeliveryAddress] nvarchar(255) NOT NULL DEFAULT '' ,
[InvoiceAddress] nvarchar(255) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for Manufacturer_ContactDetail
-- ----------------------------
CREATE TABLE [dbo].[Manufacturer_ContactDetail] (
[id] int NOT NULL IDENTITY(1,1) ,
[order_id] int NULL ,
[subBill_id] int NULL ,
[isCheckout] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for Manufacturer_PayInfo
-- ----------------------------
CREATE TABLE [dbo].[Manufacturer_PayInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[Manufacturer_id] int NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[PayableDay] int NOT NULL DEFAULT '' ,
[CheckTitle] nvarchar(50) NOT NULL DEFAULT '' ,
[CheckDueDay] int NOT NULL ,
[DiscountRemittanceFee] bit NOT NULL DEFAULT '' ,
[RemittanceFee] int NOT NULL ,
[DiscountPostage] bit NOT NULL DEFAULT '' ,
[Postage] int NOT NULL ,
[BankID] int NULL ,
[BankBranch] nvarchar(50) NOT NULL DEFAULT '' ,
[AccountName] nvarchar(50) NOT NULL DEFAULT '' ,
[BankAccount] nvarchar(30) NOT NULL DEFAULT '' ,
[IsCheckoutByMonth] bit NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Table structure for Manufacturer_Phone
-- ----------------------------
CREATE TABLE [dbo].[Manufacturer_Phone] (
[id] int NOT NULL IDENTITY(1,1) ,
[Manufacturer_id] int NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[Telephone1] nvarchar(25) NOT NULL DEFAULT '' ,
[Telephone2] nvarchar(25) NOT NULL DEFAULT '' ,
[Cellphone] nvarchar(12) NOT NULL DEFAULT '' ,
[Fax] nvarchar(25) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for manufacturer_repair_order
-- ----------------------------
CREATE TABLE [dbo].[manufacturer_repair_order] (
[id] int NOT NULL IDENTITY(1,1) ,
[number] bigint NULL ,
[send_date] date NULL ,
[address] nvarchar(50) NULL DEFAULT '' ,
[retrieve_date] date NULL ,
[shipping_fee] float(53) NOT NULL DEFAULT ((0)) ,
[manufacturer_id] int NULL ,
[comment] nvarchar(MAX) NULL ,
[charge] bit NULL DEFAULT ((1)) 
)


GO

-- ----------------------------
-- Table structure for Orders
-- ----------------------------
CREATE TABLE [dbo].[Orders] (
[id] int NOT NULL IDENTITY(1,1) ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[OrderDate] date NOT NULL DEFAULT '' ,
[OrderSource] bit NOT NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[isCheckout] bit NOT NULL DEFAULT '' ,
[NumberOfItems] int NOT NULL DEFAULT '' ,
[EstablishSource] bit NOT NULL DEFAULT '' ,
[isBorrowed] bit NOT NULL DEFAULT '' ,
[isOffset] bit NOT NULL DEFAULT '' ,
[WaitingOrderDate] date NULL DEFAULT NULL ,
[WaitingOrderNumber] bigint NULL DEFAULT NULL ,
[AlreadyOrderDate] date NULL DEFAULT NULL ,
[AlreadyOrderNumber] bigint NULL DEFAULT NULL ,
[UpdateDateTime] datetime NOT NULL DEFAULT (getdate()) ,
[Remark] nvarchar(200) NOT NULL DEFAULT '' ,
[ReviewStatus] int NOT NULL DEFAULT ((0)) ,
[CashierRemark] nvarchar(200) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'OrderDate')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'採購(報價)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'OrderDate'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'採購(報價)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'OrderDate'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'OrderSource')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單來源(0:廠商  1:客戶)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'OrderSource'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單來源(0:廠商  1:客戶)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'OrderSource'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'ObjectID')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'客戶(廠商)編號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'ObjectID'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'客戶(廠商)編號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'ObjectID'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'isCheckout')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'結帳與否(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'isCheckout'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'結帳與否(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'isCheckout'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'NumberOfItems')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'項目個數'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'NumberOfItems'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'項目個數'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'NumberOfItems'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'EstablishSource')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單建立方式(0:系統建立  1:人工建立)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'EstablishSource'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單建立方式(0:系統建立  1:人工建立)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'EstablishSource'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'isBorrowed')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否借測(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'isBorrowed'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否借測(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'isBorrowed'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'isOffset')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否沖帳(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'isOffset'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否沖帳(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'isOffset'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'WaitingOrderDate')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderDate'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderDate'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'WaitingOrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'AlreadyOrderDate')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderDate'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderDate'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'AlreadyOrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'UpdateDateTime')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單更新時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'UpdateDateTime'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單更新時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'UpdateDateTime'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders', 
'COLUMN', N'Remark')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'Remark'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders'
, @level2type = 'COLUMN', @level2name = N'Remark'
GO

-- ----------------------------
-- Table structure for Orders_ExportQuotationItem
-- ----------------------------
CREATE TABLE [dbo].[Orders_ExportQuotationItem] (
[id] int NOT NULL IDENTITY(1,1) ,
[ItemName] nvarchar(255) NOT NULL ,
[Discount] decimal(4,3) NOT NULL ,
[DefaultSelect] bit NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Table structure for Orders_InvoiceInfo
-- ----------------------------
CREATE TABLE [dbo].[Orders_InvoiceInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[InvoiceNumber] nvarchar(10) NOT NULL ,
[InvoiceYear] int NOT NULL ,
[InvoiceDate] date NOT NULL ,
[InvoicePrice] int NOT NULL ,
[InvoiceType] bit NOT NULL DEFAULT ((0)) ,
[Invalid] bit NOT NULL DEFAULT ((0)) ,
[Ignore] bit NOT NULL DEFAULT ((0)) ,
[Order_id] int NULL DEFAULT NULL ,
[SubBill_id] int NULL DEFAULT NULL ,
[invoice_manufacturerNickName_id] int NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'InvoiceNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'發票號碼'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'發票號碼'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'InvoiceYear')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'發票年份'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceYear'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'發票年份'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceYear'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'InvoiceDate')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'發票日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceDate'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'發票日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceDate'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'InvoicePrice')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'發票金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoicePrice'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'發票金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoicePrice'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'InvoiceType')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'發票類型(0:二聯單  1:三聯單)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceType'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'發票類型(0:二聯單  1:三聯單)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'InvoiceType'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'Invalid')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否做廢(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'Invalid'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否做廢(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'Invalid'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'Ignore')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否忽略(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'Ignore'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否忽略(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'Ignore'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'Order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'Order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'Order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'SubBill_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_InvoiceInfo', 
'COLUMN', N'invoice_manufacturerNickName_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'報價單廠商簡稱的索引(對應的資料表：IAECrawlerAccount_ExportQuotation_Manufacturer)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'invoice_manufacturerNickName_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'報價單廠商簡稱的索引(對應的資料表：IAECrawlerAccount_ExportQuotation_Manufacturer)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_InvoiceInfo'
, @level2type = 'COLUMN', @level2name = N'invoice_manufacturerNickName_id'
GO

-- ----------------------------
-- Table structure for Orders_Items
-- ----------------------------
CREATE TABLE [dbo].[Orders_Items] (
[id] int NOT NULL IDENTITY(1,1) ,
[Order_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[ItemNumber] int NOT NULL DEFAULT '' ,
[ISBN] nvarchar(13) NOT NULL DEFAULT '' ,
[ProductName] nvarchar(255) NOT NULL DEFAULT '' ,
[Quantity] int NOT NULL DEFAULT '' ,
[Unit] nvarchar(10) NOT NULL DEFAULT '' ,
[BatchPrice] decimal(9,2) NOT NULL DEFAULT '' ,
[SinglePrice] decimal(9,2) NOT NULL DEFAULT '' ,
[Pricing] decimal(9,2) NOT NULL DEFAULT '' ,
[PriceAmount] int NOT NULL DEFAULT '' ,
[Remark] nvarchar(255) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'Order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'ItemNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'品項項號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'ItemNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'品項項號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'ItemNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'ISBN')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品ISBN'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'ISBN'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品ISBN'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'ISBN'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'ProductName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'品名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'ProductName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'品名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'ProductName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'Quantity')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Quantity'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Quantity'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'Unit')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Unit'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Unit'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'BatchPrice')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'成本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'BatchPrice'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'成本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'BatchPrice'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'SinglePrice')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'單價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'SinglePrice'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'單價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'SinglePrice'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'Pricing')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'定價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Pricing'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'定價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Pricing'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'PriceAmount')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'PriceAmount'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'PriceAmount'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Items', 
'COLUMN', N'Remark')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Remark'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Items'
, @level2type = 'COLUMN', @level2name = N'Remark'
GO

-- ----------------------------
-- Table structure for Orders_Picture
-- ----------------------------
CREATE TABLE [dbo].[Orders_Picture] (
[id] int NOT NULL IDENTITY(1,1) ,
[Order_id] int NULL ,
[ItemNumber] int NOT NULL DEFAULT '' ,
[Picture] nvarchar(MAX) NOT NULL DEFAULT '' ,
[Source] nvarchar(20) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for Orders_Price
-- ----------------------------
CREATE TABLE [dbo].[Orders_Price] (
[id] int NOT NULL IDENTITY(1,1) ,
[Order_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[TotalPriceNoneTax] int NOT NULL DEFAULT '' ,
[Tax] int NOT NULL DEFAULT '' ,
[Discount] int NOT NULL DEFAULT '' ,
[TotalPriceIncludeTax] int NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Price', 
'COLUMN', N'Order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'Order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'Order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Price', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Price', 
'COLUMN', N'TotalPriceNoneTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceNoneTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceNoneTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Price', 
'COLUMN', N'Tax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'Tax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'Tax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Price', 
'COLUMN', N'Discount')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'折讓'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'Discount'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'折讓'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'Discount'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_Price', 
'COLUMN', N'TotalPriceIncludeTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceIncludeTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceIncludeTax'
GO

-- ----------------------------
-- Table structure for Orders_ProjectInfo
-- ----------------------------
CREATE TABLE [dbo].[Orders_ProjectInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[Order_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[ProjectName] nvarchar(100) NOT NULL DEFAULT '' ,
[ProjectQuantity] nvarchar(5) NOT NULL DEFAULT '' ,
[ProjectUnit] nvarchar(5) NOT NULL DEFAULT '' ,
[ProjectPriceAmount] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectTotalPriceNoneTax] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectTax] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectTotalPriceIncludeTax] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectDifferentPrice] nvarchar(10) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'Order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'Order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'Order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案名稱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案名稱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectQuantity')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectQuantity'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectQuantity'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectUnit')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectUnit'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectUnit'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectPriceAmount')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectPriceAmount'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectPriceAmount'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectTotalPriceNoneTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceNoneTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceNoneTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectTotalPriceIncludeTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceIncludeTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceIncludeTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ProjectInfo', 
'COLUMN', N'ProjectDifferentPrice')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'差額(總金額含稅 - 專案總金額含稅)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectDifferentPrice'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'差額(總金額含稅 - 專案總金額含稅)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectDifferentPrice'
GO

-- ----------------------------
-- Table structure for Orders_Reference
-- ----------------------------
CREATE TABLE [dbo].[Orders_Reference] (
[id] int NOT NULL IDENTITY(1,1) ,
[Order_Id] int NULL ,
[Order_Reference_Id] int NULL ,
[SubBill_Reference_Id] int NULL 
)


GO

-- ----------------------------
-- Table structure for Orders_ReviewStatusPicture
-- ----------------------------
CREATE TABLE [dbo].[Orders_ReviewStatusPicture] (
[id] int NOT NULL IDENTITY(1,1) ,
[reviewStatus_record_id] int NULL ,
[picture] nvarchar(MAX) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for Orders_ReviewStatusRecord
-- ----------------------------
CREATE TABLE [dbo].[Orders_ReviewStatusRecord] (
[id] int NOT NULL IDENTITY(1,1) ,
[order_id] int NULL ,
[subject] nvarchar(255) NOT NULL ,
[record] varchar(255) NOT NULL ,
[record_time] datetime NOT NULL ,
[review_status] int NOT NULL DEFAULT ('0') 
)


GO

-- ----------------------------
-- Table structure for Orders_ShoppingInfo
-- ----------------------------
CREATE TABLE [dbo].[Orders_ShoppingInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[Order_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[PurchaserName] nvarchar(10) NOT NULL DEFAULT '' ,
[PurchaserTelephone] nvarchar(17) NOT NULL DEFAULT '' ,
[PurchaserCellphone] nvarchar(12) NULL DEFAULT '' ,
[PurchaserAddress] nvarchar(255) NOT NULL DEFAULT '' ,
[RecipientName] nvarchar(10) NOT NULL DEFAULT '' ,
[RecipientTelephone] nvarchar(17) NOT NULL DEFAULT '' ,
[RecipientCellphone] nvarchar(12) NULL DEFAULT '' ,
[RecipientAddress] nvarchar(255) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'Order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'Order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'Order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'採購(報價)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'PurchaserName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'PurchaserTelephone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserTelephone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserTelephone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'PurchaserCellphone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserCellphone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserCellphone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'PurchaserAddress')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserAddress'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserAddress'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'RecipientName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'RecipientTelephone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientTelephone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientTelephone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'RecipientCellphone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientCellphone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientCellphone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'Orders_ShoppingInfo', 
'COLUMN', N'RecipientAddress')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientAddress'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'Orders_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientAddress'
GO

-- ----------------------------
-- Table structure for parameter
-- ----------------------------
CREATE TABLE [dbo].[parameter] (
[name] nvarchar(40) NULL ,
[value] nvarchar(MAX) NULL 
)


GO

-- ----------------------------
-- Table structure for PayableReceivable
-- ----------------------------
CREATE TABLE [dbo].[PayableReceivable] (
[id] int NOT NULL IDENTITY(1,1) ,
[OrderNumber] bigint NOT NULL ,
[OrderDate] date NOT NULL ,
[OrderObject] nvarchar(10) NOT NULL ,
[ObjectID] nvarchar(20) NOT NULL ,
[CheckNumber] nvarchar(9) NULL ,
[CheckDueDate] date NULL ,
[CompanyBank_id] int NULL ,
[ObjectBank_id] int NULL ,
[ObjectBankBranch] nvarchar(50) NOT NULL ,
[ObjectBankAccount] nvarchar(30) NOT NULL ,
[ObjectAccountName] nvarchar(50) NOT NULL ,
[ObjectPerson] nvarchar(10) NOT NULL ,
[InvoiceNumber] nvarchar(10) NOT NULL ,
[Remark] nvarchar(255) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for PayableReceivable_Document
-- ----------------------------
CREATE TABLE [dbo].[PayableReceivable_Document] (
[id] int NOT NULL IDENTITY(1,1) ,
[PayableReceivable_id] int NULL DEFAULT '' ,
[Order_id] int NULL ,
[ReturnOrder_id] int NULL ,
[SubBill_id] int NULL ,
[OrderObject] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for PayableReceivable_Price
-- ----------------------------
CREATE TABLE [dbo].[PayableReceivable_Price] (
[id] int NOT NULL IDENTITY(1,1) ,
[PayableReceivable_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL ,
[Cash] int NOT NULL ,
[Deposit] int NOT NULL ,
[OtherDiscount] int NOT NULL ,
[CashDiscount] int NOT NULL ,
[CheckPrice] int NOT NULL ,
[OffsetPrice] int NOT NULL ,
[TotalPriceIncludeTax] int NOT NULL ,
[RemittanceFeeAndPostage] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for PreparativeProduction
-- ----------------------------
CREATE TABLE [dbo].[PreparativeProduction] (
[Id] int NOT NULL IDENTITY(1,1) ,
[StoreId] int NULL ,
[ISBN] nvarchar(13) NOT NULL ,
[Name] nvarchar(200) NOT NULL ,
[Category] int NULL ,
[Attribute] int NULL ,
[AttributeOption] nvarchar(50) NULL ,
[Descript] nvarchar(MAX) NOT NULL ,
[Stock] int NOT NULL ,
[Price] decimal(9,1) NOT NULL ,
[PackageWidth] int NULL ,
[PackageHeight] int NULL ,
[PackageLength] int NULL ,
[PackageWeight] decimal(9,1) NOT NULL ,
[DaysToShip] int NOT NULL ,
[PreOrder] bit NULL DEFAULT ((0)) ,
[CreateDateTime] datetime NOT NULL ,
[UpdateDateTime] datetime NOT NULL ,
[IsAcution] bit NOT NULL DEFAULT ((0)) 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'Id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待上架商品id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待上架商品id'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'StoreId')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'store(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'StoreId'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'store(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'StoreId'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'ISBN')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品ISBN - store(ISBN)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'ISBN'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品ISBN - store(ISBN)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'ISBN'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'Name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品名稱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品名稱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'Category')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品類別 - ShopeeCategory(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Category'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品類別 - ShopeeCategory(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Category'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'Attribute')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品屬性 - SopeeAttribute(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Attribute'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品屬性 - SopeeAttribute(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Attribute'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'AttributeOption')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品屬性值'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'AttributeOption'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品屬性值'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'AttributeOption'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'Descript')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Descript'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品描述'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Descript'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'Stock')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'存量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Stock'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'存量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Stock'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'Price')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'價格'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Price'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'價格'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'Price'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'PackageWidth')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'包裹寬'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageWidth'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'包裹寬'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageWidth'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'PackageHeight')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'包裹高'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageHeight'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'包裹高'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageHeight'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'PackageLength')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'包裹長'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageLength'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'包裹長'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageLength'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'PackageWeight')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'重量(KG)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageWeight'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'重量(KG)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PackageWeight'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'DaysToShip')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'備貨天數'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'DaysToShip'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'備貨天數'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'DaysToShip'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'PreOrder')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否預購'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PreOrder'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否預購'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'PreOrder'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'CreateDateTime')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'建立時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'CreateDateTime'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'建立時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'CreateDateTime'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'PreparativeProduction', 
'COLUMN', N'UpdateDateTime')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'更新時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'UpdateDateTime'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'更新時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'PreparativeProduction'
, @level2type = 'COLUMN', @level2name = N'UpdateDateTime'
GO

-- ----------------------------
-- Table structure for product_url
-- ----------------------------
CREATE TABLE [dbo].[product_url] (
[Category] nvarchar(255) NOT NULL ,
[Url] nvarchar(255) NOT NULL ,
[Price] nvarchar(10) NOT NULL ,
[ProductCode] nvarchar(50) NOT NULL ,
[Other] nvarchar(255) NOT NULL ,
[VendorCode] nvarchar(5) NOT NULL ,
[Checked] nvarchar(10) NULL ,
[Url_Exist] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ProductBidCategory
-- ----------------------------
CREATE TABLE [dbo].[ProductBidCategory] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL DEFAULT '' ,
[ISBN] nvarchar(13) NOT NULL ,
[YahooCategory] nvarchar(24) NOT NULL DEFAULT '' ,
[RutenCategory] nvarchar(24) NOT NULL DEFAULT '' ,
[ShopeeCategory] nvarchar(24) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for ProductBookCase
-- ----------------------------
CREATE TABLE [dbo].[ProductBookCase] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL DEFAULT '' ,
[ISBN] nvarchar(13) NOT NULL DEFAULT '' ,
[ProductArea] nvarchar(20) NULL DEFAULT '' ,
[ProductFloor] nvarchar(20) NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for ProductCategory
-- ----------------------------
CREATE TABLE [dbo].[ProductCategory] (
[CategoryID] nvarchar(20) NOT NULL ,
[CategoryName] nvarchar(50) NOT NULL DEFAULT '' ,
[DiscountQuantity] int NOT NULL DEFAULT ((0)) ,
[Discount] decimal(3,2) NOT NULL ,
[PreferentialDiscount] decimal(3,2) NOT NULL ,
[VipDiscount] decimal(3,2) NOT NULL ,
[StartDate] date NULL DEFAULT '' ,
[EndDate] date NULL DEFAULT NULL ,
[CategoryLayer] int NOT NULL ,
[id] int NOT NULL IDENTITY(0,1) 
)


GO

-- ----------------------------
-- Table structure for ProductOnShelf
-- ----------------------------
CREATE TABLE [dbo].[ProductOnShelf] (
[ISBN] nvarchar(13) NOT NULL ,
[Web] bit NOT NULL ,
[Yahoo] bit NOT NULL ,
[Ruten] bit NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ProductPicture
-- ----------------------------
CREATE TABLE [dbo].[ProductPicture] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL DEFAULT '' ,
[ISBN] nvarchar(13) NOT NULL ,
[Picture1] nvarchar(MAX) NOT NULL DEFAULT '' ,
[Picture2] nvarchar(MAX) NOT NULL DEFAULT '' ,
[Picture3] nvarchar(MAX) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for ProductSaleStatus
-- ----------------------------
CREATE TABLE [dbo].[ProductSaleStatus] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL DEFAULT '' ,
[ISBN] nvarchar(13) NOT NULL ,
[WaitingPurchaseQuantity] int NOT NULL DEFAULT ((0)) ,
[WaitingIntoInStock] int NOT NULL DEFAULT ((0)) ,
[NeededPurchaseQuantity] int NOT NULL DEFAULT ((0)) ,
[WaitingShipmentQuantity] int NOT NULL DEFAULT ((0)) 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ProductSaleStatus', 
'COLUMN', N'WaitingPurchaseQuantity')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待入倉數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'WaitingPurchaseQuantity'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待入倉數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'WaitingPurchaseQuantity'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ProductSaleStatus', 
'COLUMN', N'WaitingIntoInStock')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待入庫存'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'WaitingIntoInStock'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待入庫存'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'WaitingIntoInStock'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ProductSaleStatus', 
'COLUMN', N'NeededPurchaseQuantity')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'需進貨數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'NeededPurchaseQuantity'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'需進貨數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'NeededPurchaseQuantity'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ProductSaleStatus', 
'COLUMN', N'WaitingShipmentQuantity')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待出貨數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'WaitingShipmentQuantity'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待出貨數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ProductSaleStatus'
, @level2type = 'COLUMN', @level2name = N'WaitingShipmentQuantity'
GO

-- ----------------------------
-- Table structure for ProductTag
-- ----------------------------
CREATE TABLE [dbo].[ProductTag] (
[id] int NOT NULL IDENTITY(1,1) ,
[ISBN] nvarchar(20) NOT NULL DEFAULT '' ,
[Tag] nvarchar(20) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for ProductWaitingOnShelf
-- ----------------------------
CREATE TABLE [dbo].[ProductWaitingOnShelf] (
[ISBN] nvarchar(13) NOT NULL ,
[Date] date NOT NULL 
)


GO

-- ----------------------------
-- Table structure for repair_item
-- ----------------------------
CREATE TABLE [dbo].[repair_item] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL ,
[cost] float(53) NULL DEFAULT ((0)) ,
[model] nvarchar(100) NULL DEFAULT '' ,
[specification] nvarchar(100) NULL DEFAULT '' ,
[amount] int NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Table structure for repair_item_factory_setting
-- ----------------------------
CREATE TABLE [dbo].[repair_item_factory_setting] (
[id] int NOT NULL IDENTITY(1,1) ,
[customer_repair_order_item_id] int NULL ,
[factory_setting] nvarchar(100) NULL DEFAULT '' ,
[factory_serial_number] nvarchar(100) NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for repair_orders_purchase
-- ----------------------------
CREATE TABLE [dbo].[repair_orders_purchase] (
[id] int NOT NULL IDENTITY(1,1) ,
[repair_id] int NULL ,
[order_id] int NULL 
)


GO

-- ----------------------------
-- Table structure for repair_orders_shipment
-- ----------------------------
CREATE TABLE [dbo].[repair_orders_shipment] (
[id] int NOT NULL IDENTITY(1,1) ,
[repair_id] int NULL ,
[order_id] int NULL 
)


GO

-- ----------------------------
-- Table structure for repair_status
-- ----------------------------
CREATE TABLE [dbo].[repair_status] (
[id] int NOT NULL IDENTITY(1,1) ,
[name] nvarchar(30) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ReturnOrder
-- ----------------------------
CREATE TABLE [dbo].[ReturnOrder] (
[id] int NOT NULL IDENTITY(1,1) ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[OrderDate] date NOT NULL DEFAULT '' ,
[OrderSource] bit NOT NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[isCheckout] bit NOT NULL DEFAULT '' ,
[NumberOfItems] int NOT NULL DEFAULT '' ,
[isBorrowed] bit NOT NULL ,
[UpdateDateTime] datetime NOT NULL DEFAULT (getdate()) ,
[Remark] nvarchar(255) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for ReturnOrder_Items
-- ----------------------------
CREATE TABLE [dbo].[ReturnOrder_Items] (
[id] int NOT NULL IDENTITY(1,1) ,
[ReturnOrder_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[ItemNumber] int NOT NULL DEFAULT '' ,
[ISBN] nvarchar(13) NOT NULL DEFAULT '' ,
[ProductName] nvarchar(255) NOT NULL DEFAULT '' ,
[Quantity] int NOT NULL DEFAULT '' ,
[Unit] nvarchar(10) NOT NULL DEFAULT '' ,
[BatchPrice] decimal(9,2) NOT NULL DEFAULT '' ,
[SinglePrice] decimal(9,2) NOT NULL DEFAULT '' ,
[Pricing] decimal(9,2) NOT NULL DEFAULT '' ,
[PriceAmount] int NOT NULL DEFAULT '' ,
[Remark] nvarchar(255) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for ReturnOrder_Price
-- ----------------------------
CREATE TABLE [dbo].[ReturnOrder_Price] (
[id] int NOT NULL IDENTITY(1,1) ,
[ReturnOrder_id] int NULL DEFAULT '' ,
[OrderNumber] nvarchar(12) NOT NULL DEFAULT '' ,
[TotalPriceNoneTax] int NOT NULL DEFAULT '' ,
[Tax] int NOT NULL DEFAULT '' ,
[Discount] int NOT NULL DEFAULT '' ,
[TotalPriceIncludeTax] int NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for ReturnOrder_ProjectInfo
-- ----------------------------
CREATE TABLE [dbo].[ReturnOrder_ProjectInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[ReturnOrder_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[ProjectName] nvarchar(100) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for RutenOnShelf
-- ----------------------------
CREATE TABLE [dbo].[RutenOnShelf] (
[ISBN] nvarchar(13) NOT NULL ,
[BidCode] nvarchar(20) NOT NULL ,
[Visitors] int NOT NULL DEFAULT ((0)) ,
[Followers] int NOT NULL DEFAULT ((0)) ,
[SellingVolume] int NOT NULL DEFAULT ((0)) ,
[EditTime] datetime NOT NULL ,
[Status] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ShopeeAttribute
-- ----------------------------
CREATE TABLE [dbo].[ShopeeAttribute] (
[Id] int NOT NULL ,
[CategoryId] int NULL ,
[Name] nvarchar(50) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ShopeeAttributeOption
-- ----------------------------
CREATE TABLE [dbo].[ShopeeAttributeOption] (
[Id] int NOT NULL IDENTITY(1,1) ,
[AttributeId] int NULL ,
[Name] nvarchar(50) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ShopeeCategory
-- ----------------------------
CREATE TABLE [dbo].[ShopeeCategory] (
[Id] int NOT NULL ,
[ParentId] int NOT NULL ,
[Name] nvarchar(255) NULL 
)


GO

-- ----------------------------
-- Table structure for ShopeeCategoryTree
-- ----------------------------
CREATE TABLE [dbo].[ShopeeCategoryTree] (
[Id] int NOT NULL IDENTITY(1,1) ,
[SId] int NOT NULL ,
[Name] nvarchar(500) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ShopeeItem
-- ----------------------------
CREATE TABLE [dbo].[ShopeeItem] (
[Id] int NOT NULL IDENTITY(1,1) ,
[ShopeeItemId] bigint NOT NULL ,
[PreparationId] int NULL ,
[ISBN] nvarchar(13) NOT NULL ,
[Name] nvarchar(200) NOT NULL ,
[Category] int NULL ,
[Attribute] int NULL ,
[AttributeOption] nvarchar(50) NULL ,
[Descript] nvarchar(MAX) NOT NULL ,
[Stock] int NOT NULL ,
[Price] int NULL ,
[PackageWidth] int NULL ,
[PackageHeight] int NULL ,
[PackageLength] int NULL ,
[PackageWeight] decimal(9,1) NOT NULL ,
[DaysToShip] int NULL ,
[PreOrder] bit NULL DEFAULT ((0)) ,
[url] nvarchar(500) NOT NULL ,
[ImgUrl] nvarchar(500) NOT NULL ,
[AuctionDatetime] datetime NOT NULL ,
[UpdateDateTime] datetime NOT NULL 
)


GO

-- ----------------------------
-- Table structure for ShopeeShippingFee
-- ----------------------------
CREATE TABLE [dbo].[ShopeeShippingFee] (
[Id] int NOT NULL IDENTITY(1,1) ,
[Store_Id] int NULL ,
[ISBN] nvarchar(13) NOT NULL ,
[IsBlackCatOwnExpense] bit NULL DEFAULT ((0)) ,
[BlackCatShippingFee] int NULL ,
[IsSevenElevenOwnExpense] bit NULL DEFAULT ((0)) ,
[SevenElevenShippingFee] int NOT NULL ,
[IsFamilyOwnExpense] bit NULL DEFAULT ((0)) ,
[FamilyShippingFee] int NOT NULL ,
[IsHiLifeOwnExpense] bit NULL DEFAULT ((0)) ,
[HiLifeShippingFee] int NOT NULL ,
[IsAllowBlackCat] bit NOT NULL DEFAULT ((1)) ,
[IsAllowSevenEleven] bit NOT NULL DEFAULT ((1)) ,
[IsAllowFamily] bit NOT NULL DEFAULT ((1)) ,
[IsAllowHiLife] bit NOT NULL DEFAULT ((1)) 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'Store_Id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'store(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'Store_Id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'store(id)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'Store_Id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'ISBN')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品ISBN - store(ISBN)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'ISBN'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品ISBN - store(ISBN)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'ISBN'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsBlackCatOwnExpense')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(黑貓宅配運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsBlackCatOwnExpense'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(黑貓宅配運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsBlackCatOwnExpense'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'BlackCatShippingFee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'黑貓宅配運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'BlackCatShippingFee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'黑貓宅配運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'BlackCatShippingFee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsSevenElevenOwnExpense')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(7-11運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsSevenElevenOwnExpense'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(7-11運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsSevenElevenOwnExpense'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'SevenElevenShippingFee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'7-11運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'SevenElevenShippingFee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'7-11運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'SevenElevenShippingFee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsFamilyOwnExpense')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(全家運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsFamilyOwnExpense'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(全家運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsFamilyOwnExpense'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'FamilyShippingFee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'全家運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'FamilyShippingFee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'全家運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'FamilyShippingFee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsHiLifeOwnExpense')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(萊爾富運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsHiLifeOwnExpense'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否自行負擔(萊爾富運費)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsHiLifeOwnExpense'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'HiLifeShippingFee')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'萊爾富運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'HiLifeShippingFee'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'萊爾富運費'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'HiLifeShippingFee'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsAllowBlackCat')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否允許黑貓宅配'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowBlackCat'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否允許黑貓宅配'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowBlackCat'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsAllowSevenEleven')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否允許7-11貨運'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowSevenEleven'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否允許7-11貨運'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowSevenEleven'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsAllowFamily')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否允許全家貨運'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowFamily'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否允許全家貨運'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowFamily'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'ShopeeShippingFee', 
'COLUMN', N'IsAllowHiLife')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否允許萊爾富貨運'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowHiLife'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否允許萊爾富貨運'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'ShopeeShippingFee'
, @level2type = 'COLUMN', @level2name = N'IsAllowHiLife'
GO

-- ----------------------------
-- Table structure for SpecificationTemplate
-- ----------------------------
CREATE TABLE [dbo].[SpecificationTemplate] (
[Id] int NOT NULL IDENTITY(1,1) ,
[Name] nvarchar(255) NOT NULL ,
[Content] nvarchar(MAX) NOT NULL ,
[Type] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for store
-- ----------------------------
CREATE TABLE [dbo].[store] (
[id] int NOT NULL IDENTITY(1,1) ,
[ISBN] nvarchar(13) NOT NULL DEFAULT '' ,
[InternationalCode] nvarchar(20) NOT NULL DEFAULT '' ,
[FirmCode] nvarchar(20) NOT NULL DEFAULT '' ,
[ProductName] nvarchar(200) NOT NULL DEFAULT '' ,
[ProductCode] nvarchar(50) NOT NULL DEFAULT '' ,
[Unit] nvarchar(10) NOT NULL ,
[Brand] nvarchar(50) NOT NULL DEFAULT '' ,
[Describe] nvarchar(MAX) NOT NULL DEFAULT '' ,
[Remark] nvarchar(MAX) NOT NULL DEFAULT '' ,
[SupplyStatus] nvarchar(50) NOT NULL DEFAULT '' ,
[InStock] int NOT NULL ,
[SafetyStock] int NOT NULL DEFAULT ((0)) ,
[InventoryQuantity] int NOT NULL ,
[Vendor] nvarchar(30) NOT NULL DEFAULT '' ,
[VendorCode] nvarchar(20) NOT NULL DEFAULT '' ,
[Status] int NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Table structure for store_category
-- ----------------------------
CREATE TABLE [dbo].[store_category] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL ,
[FirstCategory] nvarchar(20) NOT NULL DEFAULT '' ,
[SecondCategory] nvarchar(20) NOT NULL DEFAULT '' ,
[ThirdCategory] nvarchar(20) NOT NULL DEFAULT '' ,
[NewFirstCategory] nvarchar(20) NOT NULL DEFAULT '' ,
[NewSecondCategory] nvarchar(20) NOT NULL DEFAULT '' ,
[NewThirdCategory] nvarchar(20) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'store_category', 
'COLUMN', N'store_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'對應到 [store].[id]'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'store_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'對應到 [store].[id]'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'store_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'store_category', 
'COLUMN', N'FirstCategory')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'經銷商更新(網站上)的大類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'FirstCategory'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'經銷商更新(網站上)的大類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'FirstCategory'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'store_category', 
'COLUMN', N'SecondCategory')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'經銷商更新(網站上)的中類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'SecondCategory'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'經銷商更新(網站上)的中類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'SecondCategory'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'store_category', 
'COLUMN', N'ThirdCategory')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'經銷商更新(網站上)的小類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'ThirdCategory'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'經銷商更新(網站上)的小類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'ThirdCategory'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'store_category', 
'COLUMN', N'NewFirstCategory')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'ERP內的大類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'NewFirstCategory'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'ERP內的大類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'NewFirstCategory'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'store_category', 
'COLUMN', N'NewSecondCategory')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'ERP內的中類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'NewSecondCategory'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'ERP內的中類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'NewSecondCategory'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'store_category', 
'COLUMN', N'NewThirdCategory')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'ERP內的小類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'NewThirdCategory'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'ERP內的小類別'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'store_category'
, @level2type = 'COLUMN', @level2name = N'NewThirdCategory'
GO

-- ----------------------------
-- Table structure for store_date
-- ----------------------------
CREATE TABLE [dbo].[store_date] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL ,
[InventoryDate] date NULL DEFAULT NULL ,
[KeyinDate] date NOT NULL DEFAULT NULL ,
[UpdateDate] date NULL DEFAULT NULL ,
[PurchaseDate] date NULL DEFAULT NULL ,
[SaleDate] date NULL DEFAULT NULL ,
[ShipmentDate] date NULL DEFAULT NULL 
)


GO

-- ----------------------------
-- Table structure for store_price
-- ----------------------------
CREATE TABLE [dbo].[store_price] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL ,
[BatchPrice] decimal(9,2) NOT NULL ,
[SinglePrice] decimal(9,2) NOT NULL ,
[Pricing] decimal(9,2) NOT NULL ,
[VipPrice1] decimal(9,2) NOT NULL ,
[VipPrice2] decimal(9,2) NOT NULL ,
[VipPrice3] decimal(9,2) NOT NULL ,
[Discount] decimal(3,2) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for store_specificationContent
-- ----------------------------
CREATE TABLE [dbo].[store_specificationContent] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL ,
[content] nvarchar(MAX) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for store_specificationProductName
-- ----------------------------
CREATE TABLE [dbo].[store_specificationProductName] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_id] int NULL ,
[name] nvarchar(255) NOT NULL 
)


GO

-- ----------------------------
-- Table structure for SubBill
-- ----------------------------
CREATE TABLE [dbo].[SubBill] (
[id] int NOT NULL IDENTITY(1,1) ,
[Order_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[OrderDate] date NOT NULL DEFAULT '' ,
[OrderSource] bit NOT NULL DEFAULT '' ,
[ObjectID] nvarchar(20) NOT NULL DEFAULT '' ,
[isCheckout] bit NOT NULL DEFAULT '' ,
[NumberOfItems] int NOT NULL DEFAULT '' ,
[isBorrowed] bit NOT NULL DEFAULT '' ,
[isOffset] bit NOT NULL DEFAULT '' ,
[WaitingOrderDate] date NULL DEFAULT NULL ,
[WaitingOrderNumber] bigint NULL DEFAULT NULL ,
[AlreadyOrderDate] date NULL DEFAULT NULL ,
[AlreadyOrderNumber] bigint NULL DEFAULT NULL ,
[UpdateDateTime] datetime NOT NULL DEFAULT (getdate()) ,
[Remark] nvarchar(255) NOT NULL DEFAULT '' ,
[CashierRemark] nvarchar(255) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'Order_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'Order_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'Order_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'OrderDate')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'OrderDate'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'OrderDate'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'OrderSource')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單來源(0:廠商  1:客戶)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'OrderSource'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單來源(0:廠商  1:客戶)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'OrderSource'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'ObjectID')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'客戶(廠商)編號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'ObjectID'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'客戶(廠商)編號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'ObjectID'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'isCheckout')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'結帳與否(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'isCheckout'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'結帳與否(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'isCheckout'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'NumberOfItems')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'項目個數'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'NumberOfItems'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'項目個數'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'NumberOfItems'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'isBorrowed')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否借測(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'isBorrowed'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否借測(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'isBorrowed'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'isOffset')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否沖帳(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'isOffset'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否沖帳(0:否  1:是)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'isOffset'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'WaitingOrderDate')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)日期，同[Orders].[WaitingOrderDate]欄位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderDate'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)日期，同[Orders].[WaitingOrderDate]欄位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderDate'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'WaitingOrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)單號，同[Orders].[WaitingOrderNumber]欄位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'待入倉(待出貨)單號，同[Orders].[WaitingOrderNumber]欄位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'WaitingOrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'AlreadyOrderDate')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderDate'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)日期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderDate'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'AlreadyOrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'進貨(出貨)單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'AlreadyOrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'UpdateDateTime')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單更新時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'UpdateDateTime'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單更新時間'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'UpdateDateTime'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill', 
'COLUMN', N'Remark')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'貨單備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'Remark'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'貨單備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill'
, @level2type = 'COLUMN', @level2name = N'Remark'
GO

-- ----------------------------
-- Table structure for SubBill_Items
-- ----------------------------
CREATE TABLE [dbo].[SubBill_Items] (
[id] int NOT NULL IDENTITY(1,1) ,
[SubBill_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[SeriesNumber] int NOT NULL ,
[ItemNumber] int NOT NULL DEFAULT '' ,
[ISBN] nvarchar(13) NOT NULL DEFAULT '' ,
[ProductName] nvarchar(255) NOT NULL DEFAULT '' ,
[Quantity] int NOT NULL DEFAULT '' ,
[Unit] nvarchar(10) NOT NULL DEFAULT '' ,
[BatchPrice] decimal(9,2) NOT NULL DEFAULT '' ,
[SinglePrice] decimal(9,2) NOT NULL DEFAULT '' ,
[Pricing] decimal(9,2) NOT NULL DEFAULT '' ,
[PriceAmount] int NOT NULL DEFAULT '' ,
[Remark] nvarchar(255) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'SubBill_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'SeriesNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品序號(建立子貨單用)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'SeriesNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品序號(建立子貨單用)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'SeriesNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'ItemNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品項號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'ItemNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品項號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'ItemNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'ISBN')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品ISBN'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'ISBN'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品ISBN'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'ISBN'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'ProductName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'品名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'ProductName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'品名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'ProductName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'Quantity')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Quantity'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Quantity'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'Unit')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Unit'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Unit'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'BatchPrice')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'成本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'BatchPrice'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'成本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'BatchPrice'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'SinglePrice')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'單價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'SinglePrice'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'單價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'SinglePrice'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'Pricing')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'定價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Pricing'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'定價'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Pricing'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'PriceAmount')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'PriceAmount'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'PriceAmount'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Items', 
'COLUMN', N'Remark')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'商品備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Remark'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'商品備註'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Items'
, @level2type = 'COLUMN', @level2name = N'Remark'
GO

-- ----------------------------
-- Table structure for SubBill_Price
-- ----------------------------
CREATE TABLE [dbo].[SubBill_Price] (
[id] int NOT NULL IDENTITY(1,1) ,
[SubBill_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[TotalPriceNoneTax] int NOT NULL DEFAULT '' ,
[Tax] int NOT NULL DEFAULT '' ,
[Discount] int NOT NULL DEFAULT '' ,
[TotalPriceIncludeTax] int NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Price', 
'COLUMN', N'SubBill_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Price', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Price', 
'COLUMN', N'TotalPriceNoneTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceNoneTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceNoneTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Price', 
'COLUMN', N'Tax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'Tax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'Tax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Price', 
'COLUMN', N'Discount')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'折讓'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'Discount'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'折讓'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'Discount'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_Price', 
'COLUMN', N'TotalPriceIncludeTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceIncludeTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_Price'
, @level2type = 'COLUMN', @level2name = N'TotalPriceIncludeTax'
GO

-- ----------------------------
-- Table structure for SubBill_ProjectInfo
-- ----------------------------
CREATE TABLE [dbo].[SubBill_ProjectInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[SubBill_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[ProjectName] nvarchar(100) NOT NULL DEFAULT '' ,
[ProjectQuantity] nvarchar(5) NOT NULL DEFAULT '' ,
[ProjectUnit] nvarchar(5) NOT NULL DEFAULT '' ,
[ProjectPriceAmount] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectTotalPriceNoneTax] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectTax] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectTotalPriceIncludeTax] nvarchar(10) NOT NULL DEFAULT '' ,
[ProjectDifferentPrice] nvarchar(10) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'SubBill_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案名稱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案名稱'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectQuantity')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectQuantity'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案數量'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectQuantity'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectUnit')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectUnit'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案單位'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectUnit'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectPriceAmount')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectPriceAmount'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案總金額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectPriceAmount'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectTotalPriceNoneTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceNoneTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案總金額未稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceNoneTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案稅額'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectTotalPriceIncludeTax')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'專案總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceIncludeTax'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'專案總金額含稅'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectTotalPriceIncludeTax'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ProjectInfo', 
'COLUMN', N'ProjectDifferentPrice')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'差額(總金額含稅 - 專案總金額含稅)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectDifferentPrice'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'差額(總金額含稅 - 專案總金額含稅)'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ProjectInfo'
, @level2type = 'COLUMN', @level2name = N'ProjectDifferentPrice'
GO

-- ----------------------------
-- Table structure for SubBill_Reference
-- ----------------------------
CREATE TABLE [dbo].[SubBill_Reference] (
[id] int NOT NULL IDENTITY(1,1) ,
[SubBill_Id] int NULL ,
[SubBill_Reference_Id] int NULL ,
[Order_Reference_Id] int NULL 
)


GO

-- ----------------------------
-- Table structure for SubBill_ShoppingInfo
-- ----------------------------
CREATE TABLE [dbo].[SubBill_ShoppingInfo] (
[id] int NOT NULL IDENTITY(1,1) ,
[SubBill_id] int NULL DEFAULT '' ,
[OrderNumber] bigint NOT NULL DEFAULT '' ,
[PurchaserName] nvarchar(10) NOT NULL DEFAULT '' ,
[PurchaserTelephone] nvarchar(17) NOT NULL DEFAULT '' ,
[PurchaserCellphone] nvarchar(11) NOT NULL DEFAULT '' ,
[PurchaserAddress] nvarchar(255) NOT NULL DEFAULT '' ,
[RecipientName] nvarchar(10) NOT NULL DEFAULT '' ,
[RecipientTelephone] nvarchar(17) NOT NULL DEFAULT '' ,
[RecipientCellphone] nvarchar(11) NOT NULL DEFAULT '' ,
[RecipientAddress] nvarchar(255) NOT NULL DEFAULT '' 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'SubBill_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單索引'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'SubBill_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'OrderNumber')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'子貨單單號'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'OrderNumber'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'PurchaserName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'PurchaserTelephone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserTelephone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserTelephone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'PurchaserCellphone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserCellphone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserCellphone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'PurchaserAddress')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'訂購人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserAddress'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'訂購人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'PurchaserAddress'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'RecipientName')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientName'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientName'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'RecipientTelephone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientTelephone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人電話'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientTelephone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'RecipientCellphone')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientCellphone'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人手機'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientCellphone'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'SubBill_ShoppingInfo', 
'COLUMN', N'RecipientAddress')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'收件人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientAddress'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'收件人地址'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'SubBill_ShoppingInfo'
, @level2type = 'COLUMN', @level2name = N'RecipientAddress'
GO

-- ----------------------------
-- Table structure for sysdiagrams
-- ----------------------------
CREATE TABLE [dbo].[sysdiagrams] (
[name] sysname NOT NULL ,
[principal_id] int NOT NULL ,
[diagram_id] int NOT NULL IDENTITY(1,1) ,
[version] int NULL ,
[definition] varbinary(MAX) NULL 
)


GO

-- ----------------------------
-- Table structure for SystemSetting
-- ----------------------------
CREATE TABLE [dbo].[SystemSetting] (
[ConfigName] nvarchar(200) NOT NULL DEFAULT '' ,
[ConfigValue] nvarchar(MAX) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for update_result
-- ----------------------------
CREATE TABLE [dbo].[update_result] (
[Name] nvarchar(50) NOT NULL ,
[Value] int NOT NULL 
)


GO

-- ----------------------------
-- Table structure for Version
-- ----------------------------
CREATE TABLE [dbo].[Version] (
[id] int NOT NULL IDENTITY(1,1) ,
[version] nvarchar(20) NOT NULL ,
[versionContent] nvarchar(MAX) NOT NULL DEFAULT '' 
)


GO

-- ----------------------------
-- Table structure for YahooOnShelf
-- ----------------------------
CREATE TABLE [dbo].[YahooOnShelf] (
[ISBN] nvarchar(13) NOT NULL ,
[BidCode] nvarchar(20) NOT NULL ,
[Visitors] int NOT NULL DEFAULT ((0)) ,
[Followers] int NOT NULL DEFAULT ((0)) ,
[SellingVolume] int NOT NULL DEFAULT ((0)) ,
[EditTime] datetime NOT NULL ,
[Status] int NOT NULL 
)


GO

-- ----------------------------
-- Indexes structure for table Bank
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Bank
-- ----------------------------
ALTER TABLE [dbo].[Bank] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table BigGoFilter
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table BigGoFilter
-- ----------------------------
ALTER TABLE [dbo].[BigGoFilter] ADD PRIMARY KEY ([Resource], [WebKeyWord])
GO

-- ----------------------------
-- Indexes structure for table BookCase
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table BookCase
-- ----------------------------
ALTER TABLE [dbo].[BookCase] ADD PRIMARY KEY ([ProductArea], [ProductFloor])
GO

-- ----------------------------
-- Indexes structure for table category_genb2b
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_genb2b
-- ----------------------------
ALTER TABLE [dbo].[category_genb2b] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table category_jinghao
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_jinghao
-- ----------------------------
ALTER TABLE [dbo].[category_jinghao] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table category_ktnet
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_ktnet
-- ----------------------------
ALTER TABLE [dbo].[category_ktnet] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table category_senao
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_senao
-- ----------------------------
ALTER TABLE [dbo].[category_senao] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table category_synnex
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_synnex
-- ----------------------------
ALTER TABLE [dbo].[category_synnex] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table category_unitech
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_unitech
-- ----------------------------
ALTER TABLE [dbo].[category_unitech] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table category_weblink
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_weblink
-- ----------------------------
ALTER TABLE [dbo].[category_weblink] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table category_xander
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table category_xander
-- ----------------------------
ALTER TABLE [dbo].[category_xander] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_genb2b
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_genb2b
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_genb2b] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_jinghao
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_jinghao
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_jinghao] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_ktnet
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_ktnet
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_ktnet] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_senao
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_senao
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_senao] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_synnex
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_synnex
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_synnex] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_unitech
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_unitech
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_unitech] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_weblink
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_weblink
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_weblink] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table categorymapping_xander
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table categorymapping_xander
-- ----------------------------
ALTER TABLE [dbo].[categorymapping_xander] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table checkstore
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table checkstore
-- ----------------------------
ALTER TABLE [dbo].[checkstore] ADD PRIMARY KEY ([ProductCode])
GO

-- ----------------------------
-- Indexes structure for table CompanyBankInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table CompanyBankInfo
-- ----------------------------
ALTER TABLE [dbo].[CompanyBankInfo] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table CompanyBankInfo
-- ----------------------------
ALTER TABLE [dbo].[CompanyBankInfo] ADD UNIQUE ([BankAccount] ASC)
GO

-- ----------------------------
-- Indexes structure for table Customer
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Customer
-- ----------------------------
ALTER TABLE [dbo].[Customer] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table Customer
-- ----------------------------
ALTER TABLE [dbo].[Customer] ADD UNIQUE ([ObjectID] ASC)
GO
ALTER TABLE [dbo].[Customer] ADD UNIQUE ([ObjectID] ASC)
GO
ALTER TABLE [dbo].[Customer] ADD UNIQUE ([ObjectID] ASC)
GO

-- ----------------------------
-- Indexes structure for table Customer_Address
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Customer_Address
-- ----------------------------
ALTER TABLE [dbo].[Customer_Address] ADD PRIMARY KEY ([id], [ObjectID])
GO

-- ----------------------------
-- Indexes structure for table Customer_Phone
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Customer_Phone
-- ----------------------------
ALTER TABLE [dbo].[Customer_Phone] ADD PRIMARY KEY ([id], [ObjectID])
GO

-- ----------------------------
-- Indexes structure for table Customer_ReceiveInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Customer_ReceiveInfo
-- ----------------------------
ALTER TABLE [dbo].[Customer_ReceiveInfo] ADD PRIMARY KEY ([id], [ObjectID])
GO

-- ----------------------------
-- Indexes structure for table customer_repair_order
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table customer_repair_order
-- ----------------------------
ALTER TABLE [dbo].[customer_repair_order] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table customer_repair_order
-- ----------------------------
ALTER TABLE [dbo].[customer_repair_order] ADD UNIQUE ([number] ASC)
GO

-- ----------------------------
-- Indexes structure for table customer_repair_order_item
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table customer_repair_order_item
-- ----------------------------
ALTER TABLE [dbo].[customer_repair_order_item] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table IAECrawlerAccount
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table IAECrawlerAccount
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table IAECrawlerAccount_Belong
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table IAECrawlerAccount_Belong
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount_Belong] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table IAECrawlerAccount_ExportQuotation_Manufacturer
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table IAECrawlerAccount_ExportQuotation_Manufacturer
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table IAECrawlerAccount_ExportQuotation_Manufacturer
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ADD UNIQUE ([Account] ASC)
GO

-- ----------------------------
-- Indexes structure for table IAECrawlerAccount_Payment
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table IAECrawlerAccount_Payment
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount_Payment] ADD PRIMARY KEY ([Account_id], [Payment_id], [Belong_id])
GO

-- ----------------------------
-- Indexes structure for table IAECrawlerBelong
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table IAECrawlerBelong
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerBelong] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table IAECrawlerInvoice_Order
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table IAECrawlerInvoice_Order
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerInvoice_Order] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table IAECrawlerInvoice_Order
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerInvoice_Order] ADD UNIQUE ([order_id] ASC, [subbill_id] ASC)
GO
ALTER TABLE [dbo].[IAECrawlerInvoice_Order] ADD UNIQUE ([invoice_id] ASC)
GO

-- ----------------------------
-- Indexes structure for table IAECrawlerPayment
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table IAECrawlerPayment
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerPayment] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table IAECrawlerPayment
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerPayment] ADD UNIQUE ([RanKey] ASC)
GO

-- ----------------------------
-- Indexes structure for table logindata
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table logindata
-- ----------------------------
ALTER TABLE [dbo].[logindata] ADD PRIMARY KEY ([name])
GO

-- ----------------------------
-- Indexes structure for table Manufacturer
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Manufacturer
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table Manufacturer
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer] ADD UNIQUE ([ObjectID] ASC)
GO
ALTER TABLE [dbo].[Manufacturer] ADD UNIQUE ([ObjectID] ASC)
GO

-- ----------------------------
-- Indexes structure for table Manufacturer_Address
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Manufacturer_Address
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_Address] ADD PRIMARY KEY ([id], [ObjectID])
GO

-- ----------------------------
-- Indexes structure for table Manufacturer_ContactDetail
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Manufacturer_ContactDetail
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_ContactDetail] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table Manufacturer_ContactDetail
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_ContactDetail] ADD UNIQUE ([order_id] ASC, [subBill_id] ASC)
GO

-- ----------------------------
-- Indexes structure for table Manufacturer_PayInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Manufacturer_PayInfo
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_PayInfo] ADD PRIMARY KEY ([id], [ObjectID])
GO

-- ----------------------------
-- Indexes structure for table Manufacturer_Phone
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Manufacturer_Phone
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_Phone] ADD PRIMARY KEY ([id], [ObjectID])
GO

-- ----------------------------
-- Indexes structure for table manufacturer_repair_order
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table manufacturer_repair_order
-- ----------------------------
ALTER TABLE [dbo].[manufacturer_repair_order] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table manufacturer_repair_order
-- ----------------------------
ALTER TABLE [dbo].[manufacturer_repair_order] ADD UNIQUE ([number] ASC)
GO

-- ----------------------------
-- Indexes structure for table Orders
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders
-- ----------------------------
ALTER TABLE [dbo].[Orders] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Triggers structure for table Orders
-- ----------------------------
CREATE TRIGGER [dbo].[UpdateDatetime_trigger_Orders]
ON [dbo].[Orders]
AFTER UPDATE
AS
  UPDATE Orders SET UpdateDateTime = GETDATE()
  WHERE id IN (SELECT DISTINCT id FROM inserted);





GO

-- ----------------------------
-- Indexes structure for table Orders_ExportQuotationItem
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_ExportQuotationItem
-- ----------------------------
ALTER TABLE [dbo].[Orders_ExportQuotationItem] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table Orders_InvoiceInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_InvoiceInfo
-- ----------------------------
ALTER TABLE [dbo].[Orders_InvoiceInfo] ADD PRIMARY KEY ([InvoiceNumber], [InvoiceYear])
GO

-- ----------------------------
-- Uniques structure for table Orders_InvoiceInfo
-- ----------------------------
ALTER TABLE [dbo].[Orders_InvoiceInfo] ADD UNIQUE ([id] ASC, [InvoiceNumber] ASC, [InvoiceYear] ASC)
GO

-- ----------------------------
-- Indexes structure for table Orders_Items
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_Items
-- ----------------------------
ALTER TABLE [dbo].[Orders_Items] ADD PRIMARY KEY ([id], [OrderNumber], [ItemNumber], [ISBN])
GO

-- ----------------------------
-- Indexes structure for table Orders_Picture
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_Picture
-- ----------------------------
ALTER TABLE [dbo].[Orders_Picture] ADD PRIMARY KEY ([id], [ItemNumber], [Source])
GO

-- ----------------------------
-- Indexes structure for table Orders_Price
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_Price
-- ----------------------------
ALTER TABLE [dbo].[Orders_Price] ADD PRIMARY KEY ([id], [OrderNumber])
GO

-- ----------------------------
-- Indexes structure for table Orders_ProjectInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_ProjectInfo
-- ----------------------------
ALTER TABLE [dbo].[Orders_ProjectInfo] ADD PRIMARY KEY ([id], [OrderNumber])
GO

-- ----------------------------
-- Indexes structure for table Orders_Reference
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_Reference
-- ----------------------------
ALTER TABLE [dbo].[Orders_Reference] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table Orders_Reference
-- ----------------------------
ALTER TABLE [dbo].[Orders_Reference] ADD UNIQUE ([Order_Id] ASC, [Order_Reference_Id] ASC, [SubBill_Reference_Id] ASC)
GO

-- ----------------------------
-- Indexes structure for table Orders_ReviewStatusPicture
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_ReviewStatusPicture
-- ----------------------------
ALTER TABLE [dbo].[Orders_ReviewStatusPicture] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table Orders_ReviewStatusRecord
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_ReviewStatusRecord
-- ----------------------------
ALTER TABLE [dbo].[Orders_ReviewStatusRecord] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table Orders_ShoppingInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Orders_ShoppingInfo
-- ----------------------------
ALTER TABLE [dbo].[Orders_ShoppingInfo] ADD PRIMARY KEY ([id], [OrderNumber])
GO

-- ----------------------------
-- Indexes structure for table PayableReceivable
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table PayableReceivable
-- ----------------------------
ALTER TABLE [dbo].[PayableReceivable] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table PayableReceivable_Document
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table PayableReceivable_Document
-- ----------------------------
ALTER TABLE [dbo].[PayableReceivable_Document] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table PayableReceivable_Price
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table PayableReceivable_Price
-- ----------------------------
ALTER TABLE [dbo].[PayableReceivable_Price] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table PreparativeProduction
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table PreparativeProduction
-- ----------------------------
ALTER TABLE [dbo].[PreparativeProduction] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Uniques structure for table PreparativeProduction
-- ----------------------------
ALTER TABLE [dbo].[PreparativeProduction] ADD UNIQUE ([ISBN] ASC)
GO
ALTER TABLE [dbo].[PreparativeProduction] ADD UNIQUE ([ISBN] ASC)
GO

-- ----------------------------
-- Indexes structure for table product_url
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table product_url
-- ----------------------------
ALTER TABLE [dbo].[product_url] ADD PRIMARY KEY ([ProductCode])
GO

-- ----------------------------
-- Indexes structure for table ProductBidCategory
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductBidCategory
-- ----------------------------
ALTER TABLE [dbo].[ProductBidCategory] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table ProductBookCase
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductBookCase
-- ----------------------------
ALTER TABLE [dbo].[ProductBookCase] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table ProductCategory
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductCategory
-- ----------------------------
ALTER TABLE [dbo].[ProductCategory] ADD PRIMARY KEY ([CategoryID], [CategoryLayer])
GO

-- ----------------------------
-- Indexes structure for table ProductOnShelf
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductOnShelf
-- ----------------------------
ALTER TABLE [dbo].[ProductOnShelf] ADD PRIMARY KEY ([ISBN])
GO

-- ----------------------------
-- Indexes structure for table ProductPicture
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductPicture
-- ----------------------------
ALTER TABLE [dbo].[ProductPicture] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table ProductSaleStatus
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductSaleStatus
-- ----------------------------
ALTER TABLE [dbo].[ProductSaleStatus] ADD PRIMARY KEY ([id], [ISBN])
GO

-- ----------------------------
-- Indexes structure for table ProductTag
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductTag
-- ----------------------------
ALTER TABLE [dbo].[ProductTag] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table ProductWaitingOnShelf
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ProductWaitingOnShelf
-- ----------------------------
ALTER TABLE [dbo].[ProductWaitingOnShelf] ADD PRIMARY KEY ([ISBN])
GO

-- ----------------------------
-- Indexes structure for table repair_item
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table repair_item
-- ----------------------------
ALTER TABLE [dbo].[repair_item] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table repair_item_factory_setting
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table repair_item_factory_setting
-- ----------------------------
ALTER TABLE [dbo].[repair_item_factory_setting] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table repair_orders_purchase
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table repair_orders_purchase
-- ----------------------------
ALTER TABLE [dbo].[repair_orders_purchase] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table repair_orders_shipment
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table repair_orders_shipment
-- ----------------------------
ALTER TABLE [dbo].[repair_orders_shipment] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table repair_status
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table repair_status
-- ----------------------------
ALTER TABLE [dbo].[repair_status] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table repair_status
-- ----------------------------
ALTER TABLE [dbo].[repair_status] ADD UNIQUE ([name] ASC)
GO

-- ----------------------------
-- Indexes structure for table ReturnOrder
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ReturnOrder
-- ----------------------------
ALTER TABLE [dbo].[ReturnOrder] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Triggers structure for table ReturnOrder
-- ----------------------------
CREATE TRIGGER [dbo].[UpdateDatetime_trigger_ReturnOrder]
ON [dbo].[ReturnOrder]
AFTER UPDATE
AS
  UPDATE ReturnOrder SET UpdateDateTime = GETDATE()
  WHERE id IN (SELECT DISTINCT id FROM inserted);



GO

-- ----------------------------
-- Indexes structure for table ReturnOrder_Items
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ReturnOrder_Items
-- ----------------------------
ALTER TABLE [dbo].[ReturnOrder_Items] ADD PRIMARY KEY ([id], [OrderNumber], [ItemNumber], [ISBN])
GO

-- ----------------------------
-- Indexes structure for table ReturnOrder_Price
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ReturnOrder_Price
-- ----------------------------
ALTER TABLE [dbo].[ReturnOrder_Price] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table ReturnOrder_ProjectInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ReturnOrder_ProjectInfo
-- ----------------------------
ALTER TABLE [dbo].[ReturnOrder_ProjectInfo] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table RutenOnShelf
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table RutenOnShelf
-- ----------------------------
ALTER TABLE [dbo].[RutenOnShelf] ADD PRIMARY KEY ([ISBN])
GO

-- ----------------------------
-- Indexes structure for table ShopeeAttribute
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ShopeeAttribute
-- ----------------------------
ALTER TABLE [dbo].[ShopeeAttribute] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Indexes structure for table ShopeeAttributeOption
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ShopeeAttributeOption
-- ----------------------------
ALTER TABLE [dbo].[ShopeeAttributeOption] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Indexes structure for table ShopeeCategory
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ShopeeCategory
-- ----------------------------
ALTER TABLE [dbo].[ShopeeCategory] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Uniques structure for table ShopeeCategory
-- ----------------------------
ALTER TABLE [dbo].[ShopeeCategory] ADD UNIQUE ([Id] ASC, [ParentId] ASC)
GO
ALTER TABLE [dbo].[ShopeeCategory] ADD UNIQUE ([Id] ASC)
GO

-- ----------------------------
-- Indexes structure for table ShopeeCategoryTree
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ShopeeCategoryTree
-- ----------------------------
ALTER TABLE [dbo].[ShopeeCategoryTree] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Uniques structure for table ShopeeCategoryTree
-- ----------------------------
ALTER TABLE [dbo].[ShopeeCategoryTree] ADD UNIQUE ([SId] ASC)
GO

-- ----------------------------
-- Indexes structure for table ShopeeItem
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ShopeeItem
-- ----------------------------
ALTER TABLE [dbo].[ShopeeItem] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Uniques structure for table ShopeeItem
-- ----------------------------
ALTER TABLE [dbo].[ShopeeItem] ADD UNIQUE ([ShopeeItemId] ASC)
GO

-- ----------------------------
-- Indexes structure for table ShopeeShippingFee
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ShopeeShippingFee
-- ----------------------------
ALTER TABLE [dbo].[ShopeeShippingFee] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Uniques structure for table ShopeeShippingFee
-- ----------------------------
ALTER TABLE [dbo].[ShopeeShippingFee] ADD UNIQUE ([ISBN] ASC)
GO

-- ----------------------------
-- Indexes structure for table SpecificationTemplate
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SpecificationTemplate
-- ----------------------------
ALTER TABLE [dbo].[SpecificationTemplate] ADD PRIMARY KEY ([Id])
GO

-- ----------------------------
-- Indexes structure for table store
-- ----------------------------
CREATE UNIQUE CLUSTERED INDEX [ISBN] ON [dbo].[store]
([ISBN] ASC) 
WITH (IGNORE_DUP_KEY = ON)
GO

-- ----------------------------
-- Primary Key structure for table store
-- ----------------------------
ALTER TABLE [dbo].[store] ADD PRIMARY KEY NONCLUSTERED ([id])
GO

-- ----------------------------
-- Uniques structure for table store
-- ----------------------------
ALTER TABLE [dbo].[store] ADD UNIQUE ([ISBN] ASC)
GO

-- ----------------------------
-- Indexes structure for table store_category
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table store_category
-- ----------------------------
ALTER TABLE [dbo].[store_category] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table store_date
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table store_date
-- ----------------------------
ALTER TABLE [dbo].[store_date] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table store_price
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table store_price
-- ----------------------------
ALTER TABLE [dbo].[store_price] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table store_specificationContent
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table store_specificationContent
-- ----------------------------
ALTER TABLE [dbo].[store_specificationContent] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table store_specificationProductName
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table store_specificationProductName
-- ----------------------------
ALTER TABLE [dbo].[store_specificationProductName] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table SubBill
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SubBill
-- ----------------------------
ALTER TABLE [dbo].[SubBill] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Triggers structure for table SubBill
-- ----------------------------
CREATE TRIGGER [dbo].[UpdateDatetime_trigger_SubBill]
ON [dbo].[SubBill]
AFTER UPDATE
AS
  UPDATE SubBill SET UpdateDateTime = GETDATE()
  WHERE id IN (SELECT DISTINCT id FROM inserted);



GO

-- ----------------------------
-- Indexes structure for table SubBill_Items
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SubBill_Items
-- ----------------------------
ALTER TABLE [dbo].[SubBill_Items] ADD PRIMARY KEY ([id], [OrderNumber], [SeriesNumber], [ISBN])
GO

-- ----------------------------
-- Indexes structure for table SubBill_Price
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SubBill_Price
-- ----------------------------
ALTER TABLE [dbo].[SubBill_Price] ADD PRIMARY KEY ([id], [OrderNumber])
GO

-- ----------------------------
-- Indexes structure for table SubBill_ProjectInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SubBill_ProjectInfo
-- ----------------------------
ALTER TABLE [dbo].[SubBill_ProjectInfo] ADD PRIMARY KEY ([id], [OrderNumber])
GO

-- ----------------------------
-- Indexes structure for table SubBill_Reference
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SubBill_Reference
-- ----------------------------
ALTER TABLE [dbo].[SubBill_Reference] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table SubBill_Reference
-- ----------------------------
ALTER TABLE [dbo].[SubBill_Reference] ADD UNIQUE ([SubBill_Id] ASC, [SubBill_Reference_Id] ASC, [Order_Reference_Id] ASC)
GO

-- ----------------------------
-- Indexes structure for table SubBill_ShoppingInfo
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SubBill_ShoppingInfo
-- ----------------------------
ALTER TABLE [dbo].[SubBill_ShoppingInfo] ADD PRIMARY KEY ([id], [OrderNumber])
GO

-- ----------------------------
-- Indexes structure for table sysdiagrams
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table sysdiagrams
-- ----------------------------
ALTER TABLE [dbo].[sysdiagrams] ADD PRIMARY KEY ([diagram_id])
GO

-- ----------------------------
-- Uniques structure for table sysdiagrams
-- ----------------------------
ALTER TABLE [dbo].[sysdiagrams] ADD UNIQUE ([principal_id] ASC, [name] ASC)
GO

-- ----------------------------
-- Indexes structure for table SystemSetting
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table SystemSetting
-- ----------------------------
ALTER TABLE [dbo].[SystemSetting] ADD PRIMARY KEY ([ConfigName])
GO

-- ----------------------------
-- Indexes structure for table update_result
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table update_result
-- ----------------------------
ALTER TABLE [dbo].[update_result] ADD PRIMARY KEY ([Name])
GO

-- ----------------------------
-- Indexes structure for table Version
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Version
-- ----------------------------
ALTER TABLE [dbo].[Version] ADD PRIMARY KEY ([version])
GO

-- ----------------------------
-- Indexes structure for table YahooOnShelf
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table YahooOnShelf
-- ----------------------------
ALTER TABLE [dbo].[YahooOnShelf] ADD PRIMARY KEY ([ISBN])
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Customer_Address]
-- ----------------------------
ALTER TABLE [dbo].[Customer_Address] ADD FOREIGN KEY ([Customer_id]) REFERENCES [dbo].[Customer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Customer_Phone]
-- ----------------------------
ALTER TABLE [dbo].[Customer_Phone] ADD FOREIGN KEY ([Customer_id]) REFERENCES [dbo].[Customer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Customer_ReceiveInfo]
-- ----------------------------
ALTER TABLE [dbo].[Customer_ReceiveInfo] ADD FOREIGN KEY ([Customer_id]) REFERENCES [dbo].[Customer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[customer_repair_order]
-- ----------------------------
ALTER TABLE [dbo].[customer_repair_order] ADD FOREIGN KEY ([customer_id]) REFERENCES [dbo].[Customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[customer_repair_order] ADD FOREIGN KEY ([repair_order_id]) REFERENCES [dbo].[manufacturer_repair_order] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[customer_repair_order] ADD FOREIGN KEY ([status_id]) REFERENCES [dbo].[repair_status] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[customer_repair_order_item]
-- ----------------------------
ALTER TABLE [dbo].[customer_repair_order_item] ADD FOREIGN KEY ([customer_order_id]) REFERENCES [dbo].[customer_repair_order] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[customer_repair_order_item] ADD FOREIGN KEY ([repair_item_id]) REFERENCES [dbo].[repair_item] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[IAECrawlerAccount]
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount] ADD FOREIGN KEY ([ObjectID]) REFERENCES [dbo].[Manufacturer] ([ObjectID]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[IAECrawlerAccount_Belong]
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount_Belong] ADD FOREIGN KEY ([account_id]) REFERENCES [dbo].[IAECrawlerAccount] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[IAECrawlerAccount_Belong] ADD FOREIGN KEY ([belong_id]) REFERENCES [dbo].[IAECrawlerBelong] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer]
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ADD FOREIGN KEY ([Manufacturer_id]) REFERENCES [dbo].[Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[IAECrawlerAccount_Payment]
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerAccount_Payment] ADD FOREIGN KEY ([Account_id]) REFERENCES [dbo].[IAECrawlerAccount] ([id]) ON DELETE NO ACTION ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[IAECrawlerAccount_Payment] ADD FOREIGN KEY ([Payment_id]) REFERENCES [dbo].[IAECrawlerPayment] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[IAECrawlerPayment]
-- ----------------------------
ALTER TABLE [dbo].[IAECrawlerPayment] ADD FOREIGN KEY ([ObjectID]) REFERENCES [dbo].[Manufacturer] ([ObjectID]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Manufacturer_Address]
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_Address] ADD FOREIGN KEY ([Manufacturer_id]) REFERENCES [dbo].[Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Manufacturer_ContactDetail]
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_ContactDetail] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[Manufacturer_ContactDetail] ADD FOREIGN KEY ([subBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Manufacturer_PayInfo]
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_PayInfo] ADD FOREIGN KEY ([Manufacturer_id]) REFERENCES [dbo].[Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Manufacturer_Phone]
-- ----------------------------
ALTER TABLE [dbo].[Manufacturer_Phone] ADD FOREIGN KEY ([Manufacturer_id]) REFERENCES [dbo].[Manufacturer] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[manufacturer_repair_order]
-- ----------------------------
ALTER TABLE [dbo].[manufacturer_repair_order] ADD FOREIGN KEY ([manufacturer_id]) REFERENCES [dbo].[Manufacturer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_InvoiceInfo]
-- ----------------------------
ALTER TABLE [dbo].[Orders_InvoiceInfo] ADD FOREIGN KEY ([invoice_manufacturerNickName_id]) REFERENCES [dbo].[IAECrawlerAccount_ExportQuotation_Manufacturer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[Orders_InvoiceInfo] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[Orders_InvoiceInfo] ADD FOREIGN KEY ([SubBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_Items]
-- ----------------------------
ALTER TABLE [dbo].[Orders_Items] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_Picture]
-- ----------------------------
ALTER TABLE [dbo].[Orders_Picture] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_Price]
-- ----------------------------
ALTER TABLE [dbo].[Orders_Price] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_ProjectInfo]
-- ----------------------------
ALTER TABLE [dbo].[Orders_ProjectInfo] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_Reference]
-- ----------------------------
ALTER TABLE [dbo].[Orders_Reference] ADD FOREIGN KEY ([Order_Id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_ReviewStatusPicture]
-- ----------------------------
ALTER TABLE [dbo].[Orders_ReviewStatusPicture] ADD FOREIGN KEY ([reviewStatus_record_id]) REFERENCES [dbo].[Orders_ReviewStatusRecord] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_ReviewStatusRecord]
-- ----------------------------
ALTER TABLE [dbo].[Orders_ReviewStatusRecord] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[Orders_ShoppingInfo]
-- ----------------------------
ALTER TABLE [dbo].[Orders_ShoppingInfo] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[PayableReceivable]
-- ----------------------------
ALTER TABLE [dbo].[PayableReceivable] ADD FOREIGN KEY ([CompanyBank_id]) REFERENCES [dbo].[CompanyBankInfo] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[PayableReceivable] ADD FOREIGN KEY ([ObjectBank_id]) REFERENCES [dbo].[Bank] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[PayableReceivable_Document]
-- ----------------------------
ALTER TABLE [dbo].[PayableReceivable_Document] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[PayableReceivable_Document] ADD FOREIGN KEY ([PayableReceivable_id]) REFERENCES [dbo].[PayableReceivable] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[PayableReceivable_Document] ADD FOREIGN KEY ([ReturnOrder_id]) REFERENCES [dbo].[ReturnOrder] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[PayableReceivable_Document] ADD FOREIGN KEY ([SubBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[PayableReceivable_Price]
-- ----------------------------
ALTER TABLE [dbo].[PayableReceivable_Price] ADD FOREIGN KEY ([PayableReceivable_id]) REFERENCES [dbo].[PayableReceivable] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[PreparativeProduction]
-- ----------------------------
ALTER TABLE [dbo].[PreparativeProduction] ADD FOREIGN KEY ([Attribute]) REFERENCES [dbo].[ShopeeAttribute] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[PreparativeProduction] ADD FOREIGN KEY ([Category]) REFERENCES [dbo].[ShopeeCategory] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[PreparativeProduction] ADD FOREIGN KEY ([StoreId]) REFERENCES [dbo].[store] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[PreparativeProduction] ADD FOREIGN KEY ([ISBN]) REFERENCES [dbo].[store] ([ISBN]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ProductBidCategory]
-- ----------------------------
ALTER TABLE [dbo].[ProductBidCategory] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ProductBookCase]
-- ----------------------------
ALTER TABLE [dbo].[ProductBookCase] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ProductPicture]
-- ----------------------------
ALTER TABLE [dbo].[ProductPicture] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ProductSaleStatus]
-- ----------------------------
ALTER TABLE [dbo].[ProductSaleStatus] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[repair_item]
-- ----------------------------
ALTER TABLE [dbo].[repair_item] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[repair_item_factory_setting]
-- ----------------------------
ALTER TABLE [dbo].[repair_item_factory_setting] ADD FOREIGN KEY ([customer_repair_order_item_id]) REFERENCES [dbo].[customer_repair_order_item] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[repair_orders_purchase]
-- ----------------------------
ALTER TABLE [dbo].[repair_orders_purchase] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[repair_orders_purchase] ADD FOREIGN KEY ([repair_id]) REFERENCES [dbo].[customer_repair_order] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[repair_orders_shipment]
-- ----------------------------
ALTER TABLE [dbo].[repair_orders_shipment] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[repair_orders_shipment] ADD FOREIGN KEY ([repair_id]) REFERENCES [dbo].[customer_repair_order] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ReturnOrder_Items]
-- ----------------------------
ALTER TABLE [dbo].[ReturnOrder_Items] ADD FOREIGN KEY ([ReturnOrder_id]) REFERENCES [dbo].[ReturnOrder] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ReturnOrder_Price]
-- ----------------------------
ALTER TABLE [dbo].[ReturnOrder_Price] ADD FOREIGN KEY ([ReturnOrder_id]) REFERENCES [dbo].[ReturnOrder] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ReturnOrder_ProjectInfo]
-- ----------------------------
ALTER TABLE [dbo].[ReturnOrder_ProjectInfo] ADD FOREIGN KEY ([ReturnOrder_id]) REFERENCES [dbo].[ReturnOrder] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ShopeeAttribute]
-- ----------------------------
ALTER TABLE [dbo].[ShopeeAttribute] ADD FOREIGN KEY ([CategoryId]) REFERENCES [dbo].[ShopeeCategory] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ShopeeAttributeOption]
-- ----------------------------
ALTER TABLE [dbo].[ShopeeAttributeOption] ADD FOREIGN KEY ([AttributeId]) REFERENCES [dbo].[ShopeeAttribute] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ShopeeCategoryTree]
-- ----------------------------
ALTER TABLE [dbo].[ShopeeCategoryTree] ADD FOREIGN KEY ([SId]) REFERENCES [dbo].[ShopeeCategory] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ShopeeItem]
-- ----------------------------
ALTER TABLE [dbo].[ShopeeItem] ADD FOREIGN KEY ([Attribute]) REFERENCES [dbo].[ShopeeAttribute] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[ShopeeItem] ADD FOREIGN KEY ([Category]) REFERENCES [dbo].[ShopeeCategory] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[ShopeeItem] ADD FOREIGN KEY ([PreparationId]) REFERENCES [dbo].[PreparativeProduction] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[ShopeeItem] ADD FOREIGN KEY ([ISBN]) REFERENCES [dbo].[store] ([ISBN]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[ShopeeShippingFee]
-- ----------------------------
ALTER TABLE [dbo].[ShopeeShippingFee] ADD FOREIGN KEY ([Store_Id]) REFERENCES [dbo].[store] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[store_category]
-- ----------------------------
ALTER TABLE [dbo].[store_category] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[store_date]
-- ----------------------------
ALTER TABLE [dbo].[store_date] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[store_price]
-- ----------------------------
ALTER TABLE [dbo].[store_price] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[store_specificationContent]
-- ----------------------------
ALTER TABLE [dbo].[store_specificationContent] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[store_specificationProductName]
-- ----------------------------
ALTER TABLE [dbo].[store_specificationProductName] ADD FOREIGN KEY ([store_id]) REFERENCES [dbo].[store] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[SubBill]
-- ----------------------------
ALTER TABLE [dbo].[SubBill] ADD FOREIGN KEY ([Order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[SubBill_Items]
-- ----------------------------
ALTER TABLE [dbo].[SubBill_Items] ADD FOREIGN KEY ([SubBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[SubBill_Price]
-- ----------------------------
ALTER TABLE [dbo].[SubBill_Price] ADD FOREIGN KEY ([SubBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[SubBill_ProjectInfo]
-- ----------------------------
ALTER TABLE [dbo].[SubBill_ProjectInfo] ADD FOREIGN KEY ([SubBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[SubBill_Reference]
-- ----------------------------
ALTER TABLE [dbo].[SubBill_Reference] ADD FOREIGN KEY ([SubBill_Id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[SubBill_ShoppingInfo]
-- ----------------------------
ALTER TABLE [dbo].[SubBill_ShoppingInfo] ADD FOREIGN KEY ([SubBill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
GO
