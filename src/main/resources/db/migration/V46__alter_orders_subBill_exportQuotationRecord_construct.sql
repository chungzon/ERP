ALTER TABLE Orders_ExportQuotationRecord ADD export_object int not null default(0);
ALTER TABLE Orders_ExportQuotationRecord ADD export_totalPriceIncludeTax int not null default(0);
ALTER TABLE SubBill_ExportQuotationRecord ADD export_object int not null default(0);
ALTER TABLE SubBill_ExportQuotationRecord ADD export_totalPriceIncludeTax int not null default(0);