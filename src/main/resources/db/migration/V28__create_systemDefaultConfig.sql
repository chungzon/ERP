CREATE TABLE [dbo].[SystemDefaultConfig] (
[id] int NOT NULL identity(1,1),
[config_name] nvarchar(255) NOT NULL ,
[default_value] int NOT NULL 
)
ALTER TABLE [dbo].[SystemDefaultConfig] ADD PRIMARY KEY ([id])