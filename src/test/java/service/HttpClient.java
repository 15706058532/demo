package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * http请求工具类
 * <br/>
 * Created in 2019-06-25 9:21
 *
 * @author Zhenfeng Li
 */
public class HttpClient {
    @Test
    public void test1() {
        List<Integer> yList = Lists.newArrayList();
        List<String> xList = Lists.newArrayList();
        Double sumNum = 0D;

        for (int K = 0; K < 1; K++) {
            String microIndex = getMicroIndex("雪花秀", MicroindexDateEnum.THREE_MONTH);
            JSONArray objects = JSONArray.parseArray(microIndex);
            JSONObject jsonObject = objects.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("trend");
            JSONArray array = jsonObject.getJSONArray("s");
            List<Integer> integers = array.toJavaList(Integer.class);
            JSONArray xs = jsonObject.getJSONArray("x");
            List<String> x = xs.toJavaList(String.class);
            Double num = jsonObject.getJSONObject("last").getDouble("num");
            if (yList.isEmpty()) {
                yList = integers;
                xList = x;
            } else {
                for (int i = 0; i < integers.size(); i++) {
                    yList.set(i, yList.get(i) + integers.get(i));
                }
            }
        }
        for (Integer integer : yList) {
            sumNum +=integer;
            System.out.println(integer);
        }
        for (String integer : xList) {
            System.out.println(integer);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        //前60天-前30天
        for (Integer integer : yList.subList(yList.size()-60,yList.size()-30)) {
            sumNum +=integer;
            System.out.println(integer);
        }
        for (String integer : xList.subList(xList.size()-60,yList.size()-30)) {
            System.out.println(integer);
        }
        System.out.println(sumNum);
        sumNum = 0D;
        System.out.println("------------------------------------------------------------");
        //前30天
        for (Integer integer : yList.subList(yList.size()-30,yList.size())) {
            sumNum +=integer;
            System.out.println(integer);
        }
        for (String integer : xList.subList(xList.size()-30,xList.size())) {
            System.out.println(integer);
        }
        System.out.println(sumNum);
    }
    /*
    9136
20636
10498
14303
210702
180350
9011
24743
18275
11869
25147
16906
21393
32581
206389
11745
18114
17093
16652
18292
7952
10470
11888
11579
15373
8584
27646
40293
37259
10195
154128
12060
5月25日
5月26日
5月27日
5月28日
5月29日
5月30日
5月31日
6月1日
6月2日
6月3日
6月4日
6月5日
6月6日
6月7日
6月8日
6月9日
6月10日
6月11日
6月12日
6月13日
6月14日
6月15日
6月16日
6月17日
6月18日
6月19日
6月20日
6月21日
6月22日
6月23日
6月24日
6月25日
1241262.0
    * */

    public String getMicroIndex(String keyword, MicroindexDateEnum date) {
        Map<String, String> param = new HashMap<>();
        param.put("word", keyword);
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Accept-Language", "zh-CN,zh;q=0.8");
        header.put("Referer", "http://data.weibo.com/index/newindex?visit_type=trend&wid=1011793598474");
        header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        String json = doGet("http://data.weibo.com/index/ajax/newindex/searchword", param, header);
        JSONObject jsonObject = JSON.parseObject(json);
        if (Objects.equals(jsonObject.getString("code"), "101")) {
            return null;
        }
        String html = jsonObject.getString("html");
        Document parse = Jsoup.parse(html);
        Element li = parse.selectFirst("li");
        String text = li.text();
        if (!Objects.equals(text, keyword)) {
            return null;
        }
        String wid = li.attr("wid");
        param.clear();
        param.put("wid", wid);
        param.put("dateGroup", MicroindexDateEnum.getEnum(date.name()));
        String data = doGet("http://data.weibo.com/index/ajax/newindex/getchartdata", param, header);
        JSONObject dataJson = JSON.parseObject(data);
        return dataJson.getString("data");
    }

    public static String doGet(String url, Map<String, String> param, Map<String, String> header) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (Objects.nonNull(param)) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            //添加header
            if (Objects.nonNull(header)) {
                for (String key : header.keySet()) {
                    httpGet.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    public static String doPost(String url, Map<String, String> param, Map<String, String> header) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            //添加header
            if (Objects.nonNull(header)) {
                for (String key : header.keySet()) {
                    httpPost.setHeader(key, header.get(key));
                }
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public static String doPost(String url) {
        return doPost(url, null, null);
    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }
}
