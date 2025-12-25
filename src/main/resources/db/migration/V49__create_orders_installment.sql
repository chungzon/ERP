CREATE TABLE [dbo].[Orders_Installment] (
[id] int NOT NULL IDENTITY(1,1),
[order_id] int NULL ,
[pay_receive_document_id] int NULL ,
[installment] int NOT NULL ,
[total_price] int NULL
)

ALTER TABLE [dbo].[Orders_Installment] ADD PRIMARY KEY ([id])
ALTER TABLE [dbo].[Orders_Installment] ADD FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION
ALTER TABLE [dbo].[Orders_Installment] ADD FOREIGN KEY ([pay_receive_document_id]) REFERENCES [dbo].[PayableReceivable_Document] ([id]) ON DELETE SET NULL ON UPDATE NO ACTION