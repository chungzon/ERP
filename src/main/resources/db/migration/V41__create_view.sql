SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategoryGenb2bTree] as select  t1.id,t3.name as layer1name,t2.name as layer2name,t1.name as layer3name, t3.name+N'►'+t2.name+N'►'+t1.name as display from category_genb2b t1  left join  category_genb2b t2 on t1.Category = t2.Value left join  category_genb2b t3  on t2.Category = t3.Value where t1.Layer=3  and t2.Layer=2 and t3.Layer=1 
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategoryJinghaoTree] AS  select  t1.id,t2.name as layer1name,t1.name as layer2name,  t2.name+N'►'+t1.name as display from category_jinghao t1  left join  category_jinghao t2 on t1.Category = t2.Value  where t1.Layer=2  and t2.Layer=1
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategoryKTnetTree] as select  t1.id,t3.name as layer1name,t2.name as layer2name,t1.name as layer3name, t3.name+N'►'+t2.name+N'►'+t1.name as display from category_ktnet t1  left join  category_ktnet t2 on t1.Category = t2.Value left join  category_ktnet t3  on t2.Category = t3.Value where t1.Layer=3  and t2.Layer=2 and t3.Layer=1 
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategorySenaoTree] AS select  t1.id,t3.name as layer1name,t2.name as layer2name,t1.name as layer3name, t3.name+N'►'+t2.name+N'►'+t1.name as display from category_senao t1  left join  category_senao t2 on t1.Category = t2.Value left join  category_senao t3  on t2.Category = t3.Value where t1.Layer=3  and t2.Layer=2 and t3.Layer=1 
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategorySynnexTree] as select  t1.id,t2.name as layer1name,t1.name as layer2name, t2.name+N'►'+t1.name as display from category_synnex t1  left join  category_synnex t2 on t1.Category = t2.Value  where t1.Layer=2  and t2.Layer=1
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategoryUnitechTree] AS  select  t1.id,t3.name as layer1name,t2.name as layer2name,t1.name as layer3name, t3.name+N'►'+t2.name+N'►'+t1.name as display from category_unitech t1  left join  category_unitech t2 on t1.Category = t2.Value left join  category_unitech t3  on t2.Category = t3.Value where t1.Layer=3  and t2.Layer=2 and t3.Layer=1 
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategoryWeblinkTree] AS select  t1.id,t3.name as layer1name,t2.name as layer2name,t1.name as layer3name, t3.name+N'►'+t2.name+N'►'+t1.name as display from category_weblink t1  left join  category_weblink t2 on t1.Category = t2.Value left join  category_weblink t3  on t2.Category = t3.Value where t1.Layer=3  and t2.Layer=2 and t3.Layer=1 
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[V_CategoryXanderTree] AS select  t1.id,t3.name as layer1name,t2.name as layer2name,t1.name as layer3name,  case When t2.name=t3.name and t2.name=t1.name then t2.name when t2.name=t3.name then t2.name+N'►'+t1.name  else t3.name+N'►'+t2.name+N'►'+t1.name end AS display   from category_xander t1  left join  category_xander t2 on t1.Category = t2.Value left join  category_xander t3  on t2.Category = t3.Value
GO
