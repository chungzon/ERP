ALTER TABLE ProductGroup ADD SmallSinglePrice int null
ALTER TABLE ProductGroup ADD SmallPriceAmount int null
ALTER TABLE ProductGroup ADD IsCombined bit not null default ((0))