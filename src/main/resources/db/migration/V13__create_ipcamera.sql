CREATE TABLE [dbo].[IpCamera] (
[id] int NOT NULL identity(1,1),
[name] nvarchar(100) NOT NULL ,
[rtsp] nvarchar(255) NOT NULL 
)
ALTER TABLE [dbo].[IpCamera] ADD PRIMARY KEY ([id])