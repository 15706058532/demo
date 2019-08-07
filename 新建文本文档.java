DataSourceView dataSourceView = new DataSourceView();
        Document document = jsoupConnection(articleLink, sogouWechatHeaders, true, false).get();
        DataSource dataSource = new DataSource();
        dataSource.setIsOfficial(false);
        Element qrCodeArea = document.select("#js_profile_qrcode").first().child(0);
        dataSource.setSourceName(qrCodeArea.child(0).text());
        dataSource.setOutsideId(dataSource.getSourceName() /*+ qrCodeArea.child(2).child(1).text()*/);
        dataSource.setSourceDesc(qrCodeArea.child(3).child(1).text());
        dataSource.setExtra(Collections.EMPTY_MAP);
        dataSource.setChannel(Constant.DsChannel.WECHAT);
        /*Elements scripts = document.select("script");
        int index = 0;
        for (int i = 0; i < scripts.size(); i++) {
            if (scripts.html().contains("var biz = \"\"||\"")) {
                log.info("script index  " + i);
                index = i;
            }
        }
        Element script = scripts.get(index);
        Pattern bizPattern = Pattern.compile("var biz = \"\"\\|\\|\"\\S+\"");*/
        Element script = document.selectFirst("#activity-detail").child(0);
        Pattern bizPattern = Pattern.compile("var\\sbiz\\s\\=\\s\"\"\\|\\|\"\\S+\""); // js脚本中获取
        Matcher matcher = bizPattern.matcher(script.html());
        if (matcher.find()) {
        String bizLine = matcher.group(0);
        String biz = bizLine.substring(15, bizLine.length() - 1);
        dataSource.setBizKey(biz);
        }
        Elements scripts = document.select("script");
        Element properties = scripts.get(scripts.size() - 5);
        Pattern headImgPattern = Pattern.compile("var\\sori_head_img_url\\s\\=\\s\"\\S+\""); // js脚本中获取
        Matcher headImgMatcher = headImgPattern.matcher(properties.html());
        if (headImgMatcher.find()) {
        //log.info(properties.html());

        String imgUrl = headImgMatcher.group(0);
        dataSource.setSourcePicUrl(imgUrl.substring(24, imgUrl.length() - 1));
        }
        dataSourceView.setContent(Lists.newArrayList(dataSource));
        dataSourceView.setTotalPages(1);
        log.info("======================>成功解析微信,bizKey====>{}", dataSourceView.getContent().get(0).getBizKey());
        log.info("======================>成功解析微信,bizKey====>{}", dataSourceView.getContent().get(0).getBizKey());