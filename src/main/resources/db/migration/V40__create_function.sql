CREATE FUNCTION [dbo].[getNewestCustomerID](@AreaTitle VARCHAR(10))
RETURNS VARCHAR(50)
	BEGIN
		DECLARE @digit INT
		DECLARE @string VARCHAR(10)
		DECLARE @id INT
			SET @string = ''
			SET @id = 0

		if(LEN(@AreaTitle) >= (SELECT CONVERT(INT,ConfigValue) FROM SystemSetting WHERE ConfigName='CustomerIDLength'))
			SET @AreaTitle = SUBSTRING(@AreaTitle,1,2)

		if(ISNUMERIC(@AreaTitle) = 1)
			BEGIN
			SET @digit = (SELECT CONVERT(INT,ConfigValue) FROM SystemSetting WHERE ConfigName='CustomerIDLength')
			SET @id = (select case when (SELECT MAX(ObjectID) FROM Customer WHERE ObjectID like @AreaTitle + '%') is null then 1 else (SELECT MAX(CONVERT(INT,ObjectID)+1) FROM Customer WHERE ObjectID like @AreaTitle + '%') END C)
			SET @string = (SELECT REPLICATE('0', @digit-LEN(@id)) + RTRIM(CAST(@id AS CHAR)))

			SET @string = (select case when (@id = 1) then RTRIM(CAST(@AreaTitle AS CHAR)) + (SELECT REPLICATE('0', @digit-LEN(@AreaTitle)-1) + RTRIM(CAST(@id AS CHAR))) else (SELECT REPLICATE('0', @digit-LEN(@id)) + RTRIM(CAST(@id AS CHAR))) END) 

			END
		ELSE
		if(@AreaTitle != '')
			BEGIN
			SET @digit = (SELECT CONVERT(INT,ConfigValue) FROM SystemSetting WHERE ConfigName='CustomerIDAreaLength')
			SET @id = (select case when (SELECT MAX(ObjectID) FROM Customer WHERE ObjectID like @AreaTitle + '%') is null then 1 else (SELECT MAX(CONVERT(INT,SUBSTRING(ObjectID,2,@digit))+1) FROM Customer WHERE ObjectID like @AreaTitle + '%' and ISNUMERIC(SUBSTRING(ObjectID,2,@digit))=1) END C)
			SET @string = (SELECT REPLICATE('0', @digit-LEN(@id)) + RTRIM(CAST(@id AS CHAR)))
			END
		ELSE
			BEGIN
			SET @digit = (SELECT CONVERT(INT,ConfigValue) FROM SystemSetting WHERE ConfigName='CustomerIDLength')
			SET @id = (SELECT CASE WHEN (SELECT MAX(CONVERT(INT,A.ObjectID)) FROM Customer A WHERE ISNUMERIC(A.ObjectID)=1) IS NULL THEN 1 ELSE (SELECT MAX(CONVERT(INT,A.ObjectID)+1) FROM Customer A WHERE ISNUMERIC(A.ObjectID)=1) END C)
			SET @string = (SELECT REPLICATE('0', @digit-LEN(@id)) + RTRIM(CAST(@id AS CHAR)))
			END
		RETURN @string;
	END
GO

CREATE FUNCTION [dbo].[getNewestManufacturerID](@IDTitle VARCHAR(10))
RETURNS VARCHAR(20)
	BEGIN
		DECLARE @digit INT
		DECLARE @string VARCHAR(10)
		DECLARE @id INT
		SET @digit = (SELECT CONVERT(INT,ConfigValue) FROM SystemSetting WHERE ConfigName='ManufacturerIDLength')
		SET @string = ''
		SET @id = 0
		SET @id = (SELECT CASE WHEN (SELECT MAX(ObjectID) FROM Manufacturer A WHERE ObjectID like @IDTitle + '%') is null then 1 else (SELECT MAX(CONVERT(INT,SUBSTRING(ObjectID,2,@digit))+1) FROM Manufacturer WHERE ObjectID like @IDTitle+ '%' and ISNUMERIC(SUBSTRING(ObjectID,2,@digit))=1) END C)

		if(@id = (REPLICATE('9',@digit-1)+1))
				SET @string = null
		ELSE if(@IDTitle = '0')
				SET @string = (SELECT REPLICATE('0', @digit-LEN(@id)) + RTRIM(CAST(@id AS CHAR)))
		ELSE
				SET @string = CONVERT(INT,@IDTitle + REPLICATE('0', @digit-LEN(@digit))) + (SELECT REPLICATE('0', @digit-LEN(@id)) + RTRIM(CAST(@id AS CHAR)))
		RETURN @string;
	END

GO

CREATE FUNCTION [dbo].[getNewestOrderNumber](@OrderSource VARCHAR(10), @GenerateOrderNumberMethod VARCHAR(10),@OrderNumber VARCHAR(50))
RETURNS VARCHAR(50)
	BEGIN
		DECLARE @digit INT
		DECLARE @id INT

		SET @digit = (SELECT CONVERT(INT,ConfigValue) FROM SystemSetting WHERE ConfigName='OrderNumberLength')
		SET @id = 0

		if (@OrderSource = '採購單' or @OrderSource = '待入倉單' or (@OrderSource = '進貨子貨單' and @GenerateOrderNumberMethod = '日期') or
                @OrderSource = '報價單' or @OrderSource = '待出貨單' or (@OrderSource = '出貨子貨單' and @GenerateOrderNumberMethod = '日期'))
			BEGIN
			SET @OrderNumber =
							(select case when (select Max(OrderNumber) from (select Max(OrderNumber) as OrderNumber from Orders where OrderNumber like @OrderNumber + '%' union
										select Max(WaitingOrderNumber) as OrderNumber from Orders where WaitingOrderNumber like @OrderNumber + '%' union
                    select Max(AlreadyOrderNumber) as OrderNumber from Orders where AlreadyOrderNumber like @OrderNumber + '%' union
                    select Max(AlreadyOrderNumber) as OrderNumber from SubBill where AlreadyOrderNumber like @OrderNumber + '%') as OrderNumber) is null then '0'
				else '1' end C)
				END

		ELSE IF (@OrderSource = '進貨子貨單' or @OrderSource = '出貨子貨單')
			BEGIN
			SET @OrderNumber =
					(select case when (select Max(OrderNumber) from SubBill where OrderNumber like @OrderNumber + '%') is null then '1' else '0' end C)
			END
		ELSE
			BEGIN
			SET @OrderNumber =
				(select case when (select Max(OrderNumber) from ReturnOrder where OrderNumber like @OrderNumber + '%') is null then '1' else '0' end C)
			END
			RETURN @OrderNumber;
	END
GO
