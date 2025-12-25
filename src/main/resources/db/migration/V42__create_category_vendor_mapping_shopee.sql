CREATE TABLE [dbo].[category_genb2b_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[category_jinghao_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[category_ktnet_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[category_senao_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[category_synnex_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[category_unitech_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[category_weblink_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[category_xander_mapping_shopee](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[gid] [int] NOT NULL,
	[spid] [int] NOT NULL,
	[shopee_display] [nvarchar](300) NULL,
	[display] [nvarchar](300) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[category_genb2b_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_genb2b_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_genb2b] ([id])
GO
ALTER TABLE [dbo].[category_jinghao_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_jinghao_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_jinghao] ([id])
GO
ALTER TABLE [dbo].[category_ktnet_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_ktnet_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_ktnet] ([id])
GO
ALTER TABLE [dbo].[category_senao_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_senao_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_senao] ([id])
GO
ALTER TABLE [dbo].[category_synnex_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_synnex_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_synnex] ([id])
GO
ALTER TABLE [dbo].[category_unitech_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_unitech_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_unitech] ([id])
GO
ALTER TABLE [dbo].[category_weblink_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_weblink_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_weblink] ([id])
GO
ALTER TABLE [dbo].[category_xander_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([spid])
REFERENCES [dbo].[ShopeeCategory] ([Id])
GO
ALTER TABLE [dbo].[category_xander_mapping_shopee]  WITH CHECK ADD FOREIGN KEY([gid])
REFERENCES [dbo].[category_xander] ([id])
GO
