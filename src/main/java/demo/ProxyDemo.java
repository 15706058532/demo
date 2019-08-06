package demo;

import java.io.IOException;
    import java.net.Authenticator;
    import java.net.InetSocketAddress;
    import java.net.PasswordAuthentication;
    import java.net.Proxy;

    import org.jsoup.Jsoup;
    import org.jsoup.nodes.Document;

    /***
     * 通过阿布云代理访问指定URL 内容
     * 此处 Jsoup Version 1.9.1
     * @author sun
     * JDK 8u111版本后环境下：要访问的目标页面为HTTPS协议时，需修改“jdk.http.auth.tunneling.disabledSchemes”值
     */
    public class ProxyDemo
    {
        // 代理隧道验证信息
        final static String ProxyUser = "HYUZO675Q961F3FP";
        final static String ProxyPass = "EB11B871CC5692A4";

        // 代理服务器
        final static String ProxyHost = "http-pro.abuyun.com";
        final static Integer ProxyPort = 9010;
        // 设置IP切换头
        final static String ProxyHeadKey = "Proxy-Switch-Ip";
        //党委yes的时候表示更换ip
        final static String ProxyHeadVal = "no";
        public static String getUrlProxyContent(String url)
        {
            Authenticator.setDefault(new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
                }
            });

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

            try
            {
                // 此处自己处理异常、其他参数等
                Document doc = Jsoup.connect(url)
//                    .followRedirects(false)
//                    .timeout(3000)
                    .header(ProxyHeadKey, ProxyHeadVal)
                    .proxy(proxy)
                    // 忽略证书校验
//                    .validateTLSCertificates(false)
                    .get()
                ;

                if(doc != null) {
                    System.out.println(doc.body().html());
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        public static void main(String[] args) throws Exception
        {
            // 要访问的目标页面
//            String targetUrl = "https://www.baidu.com/";

            // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
            System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

//            String targetUrl = "http://proxy.abuyun.com/switch-ip";
            String targetUrl = "http://proxy.abuyun.com/current-ip";
            getUrlProxyContent(targetUrl);
        }
    }