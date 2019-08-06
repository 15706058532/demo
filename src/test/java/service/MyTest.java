package service;

import com.monitorjbl.xlsx.StreamingReader;
import demo.dao.LoginDao;
import demo.service.LoginService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Habit on 2017-07-01.
 */
public class MyTest {
    ClassPathXmlApplicationContext context;

    @BeforeSuite
    public void before() {
        context = new ClassPathXmlApplicationContext("beans.xml");
    }

    @Test
    public void test1() {
        LoginService bean = context.getBean(LoginService.class);
        LoginDao loginDao = bean.getLoginDao();
        // loginDao.fun();

    }

    @Test
    public void test2() throws IOException {
        //图片的输出路径
        File outFile = new File("E:/test.png");
        //宽度和高度
        int width = 300, height = 300;
        //字体设置
        Font font = new Font("宋体", Font.BOLD, 300);
        //内容
        String value = "我";
        //背景色
        Color blackColor = Color.WHITE;
        //字体颜色
        Color frontColor = Color.BLACK;
        // 创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(blackColor);
        // 先用黑色填充整张图片,也就是背景
        g.fillRect(0, 0, width, height);
        // 在换成红色
        g.setColor(frontColor);
        // 设置画笔字体
        g.setFont(font);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        /** 用于获得垂直居中y */
        int y = clip.y + ((clip.height - fm.getHeight()) / 2) + fm.getAscent();
        /** 用于获得水平居中x */
        int x = clip.x + (clip.width - fm.stringWidth(value)) / 2;
        g.drawString(value, x, y);
        g.dispose();
        // 输出png图片
        ImageIO.write(image, "png", outFile);
    }

    @Test
    public void test3() throws IOException {
        Set<String> ips = new HashSet<String>();
        for (int i = 1; i < 3; i++) {
            Connection connection = Jsoup.connect("http://www.89ip.cn/index_" + i + ".html");
            Connection.Response response = connection.execute();
            String body = response.body();
            Document document = Jsoup.parse(body);
            Elements tbody = document.getElementsByTag("tr");
            if (tbody.isEmpty()) {
                break;
            }
            for (Element element : tbody) {
                Elements children = element.children();
                Element next = children.first();
                String name = next.nodeName();
                if ("th".equalsIgnoreCase(name)) {
                    continue;
                }
                String ip = next.text();
                Element first = children.next().first();
                int port = Integer.valueOf(first.text());
                System.out.println(ip + ":" + port);
                Connection connect = Jsoup.connect("http://www.baidu.com");
                connect.timeout(3000);
                connect.proxy(ip, port);
                try {
                    Connection.Response execute = connect.execute();
                    int status = execute.statusCode();
                    if (status == 200) {
                        ips.add(ip + ":" + port);
                        System.out.println("||||||||||||||   " + ip + "    " + port);
                    }
                } catch (Exception e) {
                }
            }
            System.out.println("--------------------" + i + "-----------------------------");
        }
        System.out.println("***********************************");
        for (String ip : ips) {
            System.out.println(ip);
        }
    }

    /**
     * 119.139.198.200    3128
     * 218.60.8.98    3129
     *
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        Connection connect = Jsoup.connect("https://www.baidu.com");
//        connect.timeout(3000);
        connect.proxy("proxy.wandouip.com", 8090);
//        System.setProperty("http.proxyHost","119.139.198.200");
//        System.setProperty("http.proxyPort","3128");
//        connect.proxy(Proxy.);
        Connection.Response execute = connect.execute();
        String body = execute.body();
        System.out.println(body);
    }

    @Test
    public void test5() {
        Pattern pattern = Pattern.compile("#(.*?)#");
        Matcher m = pattern.matcher("啊三#大三#大四");
        while (m.find()) {
            System.out.println(m.group(1));
        }
    }

    @Test
    public void test6() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 900);
        map.put("c", 29);
        map.put("d", 40);
        map.put("e", 100);
        Map<String, Integer> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));
        for (Map.Entry<String, Integer> stringIntegerEntry : result.entrySet()) {

            System.out.println(stringIntegerEntry.getKey() + "||||" + stringIntegerEntry.getValue());
        }
    }

    @Test
    public void test7() throws IOException {
        File file = new File("C:\\Users\\15706\\Desktop\\天猫id-20190729.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder builder = new StringBuilder();
        builder.append("[\r\n");
        while (bufferedReader.ready()) {
            builder.append("\"").append(bufferedReader.readLine()).append("\"");
            if (bufferedReader.ready()) {
                builder.append(",");
            }
            builder.append("\r\n");
        }
        builder.append("]");
        File outFile = new File("C:\\Users\\15706\\Desktop\\product_id.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        bufferedWriter.write(builder.toString());
        bufferedWriter.flush();
        bufferedReader.close();
        bufferedWriter.close();
        System.out.println(builder.toString());
//        String text = "";
//        String[] split = text.split("\n");
//        for (String s : split) {
//            System.out.println("\"" + s + "\",");
//        }
    }

    @Test
    public void test8() throws IOException, InterruptedException {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        Random random = new Random();
        Workbook open = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(new FileInputStream("C:\\Users\\15706\\Desktop\\数据源导入模版-0718(1).xlsx"));
        Sheet sheet = open.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == row.getFirstCellNum()) {
                continue;
            }
            if (!row.getCell(3).getStringCellValue().equals("WECHAT")) {
                continue;
            }

            String url = "https://weixin.sogou.com/weixin?type=1&ie=utf8&s_from=input&query=" + row.getCell(5).getStringCellValue();
            Connection connect = getConnection(url);
            connect.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            Document document = connect.get();
            if (document.body().text().contains("用户您好，我们的系统检测到您网络中存在异常访问请求")) {
                System.out.println(111);
            }
            System.out.println(document.body().html());
            Elements newsList2 = document.getElementsByClass("news-list2");
            if (newsList2.isEmpty()) {
                continue;
            }
            Document parse = Jsoup.parse(newsList2.html());
            //头像
            Elements imgBox = parse.getElementsByClass("img-box");
            Document parse1 = Jsoup.parse(imgBox.html());
            Elements img = parse1.getElementsByTag("img");
            String src = "https:" + img.attr("src");
            Elements a = parse1.getElementsByTag("a");
            //详情链接
            String aHref = a.attr("href");
            //名称
            Elements tit = parse.getElementsByClass("tit");
            String text = tit.text();
            //二维码
            Elements pop = parse.getElementsByClass("pop");
            Document parse2 = Jsoup.parse(pop.html());
            Elements img2 = parse2.getElementsByTag("img");
            String src2 = img2.first().attr("src");
            //描述（简介）
            Elements dd = parse.getElementsByTag("dd");
            String text1 = dd.text();
            //月发文数[无法获取]
            Elements monthCountE = parse.getElementsByClass("info");
            String monthCount = monthCountE.next().text().replace("月发文 ", "").replace(" 篇", "");
            //微信认证等级
            Elements script = parse.getElementsByTag("script");
            String levl = script.first().html();
            //微信认证
            Elements dl = parse.getElementsByTag("dl");
            String auth = dl.next().first().text();
            Document parse3 = Jsoup.parse(dl.next().next().html());
            //最近文章
            String aText = parse3.getElementsByTag("a").text();
            //最近文章链接
            String href = "https://weixin.sogou.com" + parse3.getElementsByTag("a").attr("href");
            //最近文章时间戳
            Elements span1 = parse3.getElementsByTag("script");
            String span = span1.html().replace("document.write(timeConvert('", "").replace("'))", "");

            Thread.sleep((random.nextInt(10) + 5) * 1000);
        }
    }

    public Connection getConnection(String url) {
        // 代理隧道验证信息
        String ProxyUser = "H4THGZR0582L8NXC";
        String ProxyPass = "BF72F03A52BEB260";

        // 代理服务器
        String ProxyHost = "http-cla.abuyun.com";
        Integer ProxyPort = 9030;
        // 设置IP切换头
        String ProxyHeadKey = "Proxy-Switch-Ip";
        String ProxyHeadVal = "no";
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        // 此处自己处理异常、其他参数等
        return Jsoup.connect(url)
                .followRedirects(false)
                .timeout(3000)
                .header(ProxyHeadKey, ProxyHeadVal)
                .proxy(proxy)
                // 忽略证书校验
                .validateTLSCertificates(false);
    }

    @Test
    public void test9() {
        Random random = new Random();
        int i = random.nextInt(10) + 11;
        System.out.println(i);
    }

    @Test
    public void test10() throws IOException {
        File file = new File("C:\\Users\\15706\\Desktop\\mkip(3).log");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder builder = new StringBuilder();
        int i = 1;
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            if (line.contains("第5次获取")) {
                line = bufferedReader.readLine();
                builder.append(line.substring(line.lastIndexOf(": ") + 1)).append("\r\n");
                i++;
            }
        }
        System.out.println(i);
        File outFile = new File("C:\\Users\\15706\\Desktop\\店铺.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        bufferedWriter.write(builder.toString());
        bufferedWriter.flush();
        bufferedReader.close();
        bufferedWriter.close();
        System.out.println(builder.toString());
//        String text = "";
//        String[] split = text.split("\n");
//        for (String s : split) {
//            System.out.println("\"" + s + "\",");
//        }
    }

    @Test
    public void test11() {
        try {
            String url = "https://list.tmall.com/search_product.htm?q=bliv%D0%C5%C3%C0%BC%A1%C6%EC%BD%A2%B5%EA&type=p&style=w&spm=a220m.1000862.a2227oh.d100&xl=bliv%D0%C5%C3%C0%BC%A1%C6%EC%BD%A2%B5%EA_2&from=.list.pc_2_suggest";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.addRequestProperty("Referer", "google.com");
            System.out.println("Request URL ... " + url);
            boolean redirect = false;
            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            System.out.println("Response Code ... " + status);
            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = conn.getHeaderField("Location");
                // get the cookie if need, for login
                String cookies = conn.getHeaderField("Set-Cookie");
                // open the new connnection again
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("User-Agent", "Mozilla");
                conn.addRequestProperty("Referer", "google.com");
                System.out.println("Redirect to URL : " + newUrl);
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder html = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
            System.out.println("URL Content... \n" + html.toString());
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
