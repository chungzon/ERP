DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = name FROM sys.key_constraints WHERE  [type] = 'UQ' AND [parent_object_id] = Object_id('dbo.IAECrawlerInvoice_Order')
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE IAECrawlerInvoice_Order DROP CONSTRAINT ' + @ConstraintName )
SELECT @ConstraintName = name FROM sys.key_constraints WHERE  [type] = 'UQ' AND [parent_object_id] = Object_id('dbo.IAECrawlerInvoice_Order')
IF @ConstraintName IS NOT NULL EXEC ( 'ALTER TABLE IAECrawlerInvoice_Order DROP CONSTRAINT ' + @ConstraintName )

ALTER TABLE [dbo].[IAECrawlerInvoice_Order] ADD UNIQUE ([order_id] ASC, [subbill_id] ASC)
ALTER TABLE [dbo].[IAECrawlerInvoice_Order] ADD FOREIGN KEY ([invoice_id]) REFERENCES [dbo].[IAECrawlerPayment] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
ALTER TABLE [dbo].[IAECrawlerInvoice_Order] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
ALTER TABLE [dbo].[IAECrawlerInvoice_Order] ADD FOREIGN KEY ([subbill_id]) REFERENCES [dbo].[SubBill] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION