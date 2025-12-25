drop table BigGoFilter
CREATE TABLE [dbo].[BigGoFilter] (
[id] int NOT NULL IDENTITY(1,1) ,
[store_name] nvarchar(255) NOT NULL DEFAULT '' ,
[link_name] nvarchar(255) NOT NULL DEFAULT '' ,
[classify] int NOT NULL DEFAULT ((0)) ,
[exist] bit NOT NULL DEFAULT ((1))
)
CREATE UNIQUE CLUSTERED INDEX [IDX_link_name] ON [dbo].[BigGoFilter]
([id] ASC, [link_name] ASC)
WITH (IGNORE_DUP_KEY = ON)
ALTER TABLE [dbo].[BigGoFilter] ADD PRIMARY KEY NONCLUSTERED ([id])