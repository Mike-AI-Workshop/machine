-- ----------------------------
--  Records for Home Page Content
--  Initialize data for Home.vue
-- ----------------------------

-- Banner Section
INSERT INTO `system_content` (`content_key`, `content_value`, `description`) VALUES
('home.banner.title', '智能机房管理平台', '首页 - 横幅主标题'),
('home.banner.subtitle', '高效、可靠、可视化的机房资产与运维解决方案', '首页 - 横幅副标题'),
('home.banner.button.text', '开始探索', '首页 - 横幅按钮文字');

-- Carousel Section
-- The image URLs stored here are initial placeholders.
-- They should be updated via the admin interface using the file upload API.
INSERT INTO `system_content` (`content_key`, `content_value`, `description`) VALUES
('home.carousel.item1.image_url', '/home-backend1.jpg', '首页 - 轮播图1的图片URL'),
('home.carousel.item2.image_url', '/home-backend2.jpg', '首页 - 轮播图2的图片URL'),
('home.carousel.item3.image_url', '/home-backend3.jpg', '首页 - 轮播图3的图片URL');

-- Feature Cards Section
INSERT INTO `system_content` (`content_key`, `content_value`, `description`) VALUES
('home.feature_cards.intro.title', '区段简介', '首页 - 简介卡片标题'),
('home.feature_cards.intro.summary', '这里是关于本区段的详细介绍，包括其历史、规划和定位。', '首页 - 简介卡片摘要'),
('home.feature_cards.intro.details', '这里是关于区段简介的更详尽的描述。我们可以添加更多的段落、图片或者图表来丰富内容。\n\n例如，介绍项目的背景、发展历程和未来的愿景等等。', '首页 - 简介卡片详情内容'),
('home.feature_cards.intro.button.text', '了解更多 →', '首页 - 简介卡片按钮文字'),
('home.feature_cards.business.title', '区段业务', '首页 - 业务卡片标题'),
('home.feature_cards.business.summary', '本区段专注于提供领先的智能化解决方案，服务覆盖多个行业领域。', '首页 - 业务卡片摘要'),
('home.feature_cards.business.details', '这里是关于区段业务的更详尽的描述。可以详细介绍我们提供的服务、技术优势和成功案例。\n\n<ul>\n  <li>智能化解决方案 A</li>\n  <li>云服务集成 B</li>\n  <li>数据分析平台 C</li>\n</ul>', '首页 - 业务卡片详情内容'),
('home.feature_cards.business.button.text', '了解更多 →', '首页 - 业务卡片按钮文字');

-- Footer Section
INSERT INTO `system_content` (`content_key`, `content_value`, `description`) VALUES
('home.footer.text', '@2025智能机房管理系统', '首页 - 页脚文字'); 