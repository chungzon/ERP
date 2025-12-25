ALTER TABLE Manufacturer_ContactDetail DROP COLUMN isCheckout 
ALTER TABLE Manufacturer_ContactDetail ADD source_purchase bit not null DEFAULT ((0)) 
ALTER TABLE Manufacturer_ContactDetail ADD source_shipment bit not null DEFAULT ((0)) 
ALTER TABLE Manufacturer_ContactDetail ADD source_schoolPayment bit not null DEFAULT ((0)) 