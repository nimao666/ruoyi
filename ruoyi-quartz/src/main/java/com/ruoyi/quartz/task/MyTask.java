package com.ruoyi.quartz.task;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class MyTask {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void showTime(Integer interval,String channel,String Sql) {
        //控制循环间隔时间double interval   控制请求渠道String channel   指定数据源 String Sql
        String urlString = "https://surviveserver"+ channel +".dksgames.com:9440/userLoginSign.json";
        String updateurl = "https://surviveserver"+ channel +".dksgames.com:9440/getPlayerUseADTCount.json";
        String updateurl1 = "https://surviveserver"+ channel +".dksgames.com:9440/userResAdvertCount.json";

        // 判断 channel 是否为 1，如果是，则更新 updateurl 为 updateurl1 的值
        boolean ios = "1".equals(channel);
        if (ios) {
            updateurl = updateurl1;
        }

        // 查询签到sql
        String dic_sql = String.format(
                "SELECT css_class FROM sys_dict_data where dict_code = '%s'",
                Sql
        );

        List<Map<String, Object>> sql = jdbcTemplate.queryForList(dic_sql);

        try {
            if (!sql.isEmpty()) {
                List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql.get(0).get("css_class").toString());
//                循环次数
                for (int i = 0; i < 10; i++) {
                    boolean isLastIteration = (i == 9); // 提前判断是否为最后一次循环
                    for (Map<String, Object> row : resultList) {
                        System.out.println(row);
                        update(row, updateurl,ios,isLastIteration);

                        // 在最后一次循环时调用 `update` 方法
                        if (isLastIteration) {
                            qiandao(row, urlString, isLastIteration);
                        }
                        // 休眠时间控制放在内部循环后，确保每次操作间隔
                        Thread.sleep(interval);
                    }
                }
            } else {
                System.out.println("No query found in the dictionary.");
            }
        } catch (Exception e) {
            System.err.println("查询失败：" + e.getMessage());
        }

    }

    public void qiandao(Map<String, Object> row,String channel,boolean isLastIteration) {
        Map<String, Object> R = row;
        // 获取 playerId 的值
        Object playerId = R.get("playerId");

        // 检查 playerId 是否存在
        if (playerId == null) {
            System.out.println("Error: playerId is null");
            return;
        }

        String jsonInputString = String.format("{\"playerId\": %s}", playerId);

        try {
            // 创建 URL 对象
            URL url = new URL(channel);

            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置请求方法为 POST
            conn.setRequestMethod("POST");

            // 设置请求头
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            // 启用输出流
            conn.setDoOutput(true);


            if (isLastIteration){
                // 写入请求体并发送
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // 获取响应代码
                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                // 获取响应体
                InputStream inputStream;
                if (responseCode >= 200 && responseCode < 300) {
                    inputStream = conn.getInputStream(); // 成功时获取输入流
                } else {
                    inputStream = conn.getErrorStream(); // 失败时获取错误流
                    if (inputStream == null) {
                        System.out.println("Error response body is null.");
                    }
                }

                String responseBody = ""; // 用来存储响应内容

                // 读取响应内容并拼接
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());  // 拼接每行响应内容
                    }
                    responseBody = response.toString();  // 将拼接好的响应体保存到 responseBody
                    System.out.println("Response Body: " + responseBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 将响应内容转换为 JSON 对象
                try {
                    // 使用 org.json 库解析 JSON 字符串
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    System.out.println("Parsed JSON Response: " + jsonResponse);

                    // 如果你需要访问 JSON 对象中的字段，可以这样做：
                    JSONObject playerStatus = jsonResponse.getJSONObject("playerStatus");

                    int playerId1 = playerStatus.getInt("id");
                    int noney = playerStatus.getInt("noney");

                    String updateSql = String.format(
                            "UPDATE accountpoints SET noney = %d WHERE playerId = %d",
                            noney, playerId1
                    );

                    jdbcTemplate.update(updateSql);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 检查响应
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Request was successful.");
                } else {
                    System.out.println("Request failed.");
                }
            }

            // 关闭连接
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Map<String, Object> row,String channel,boolean ios,boolean isLastIteration) {
        Map<String, Object> R = row;
        // 获取 playerId 的值
        Object playerId = R.get("playerId");

        // 检查 playerId 是否存在
        if (playerId == null) {
            System.out.println("Error: playerId is null");
            return;
        }

        //创建请求体
        String jsonInputString = String.format("{\"playerId\": %s}", playerId);

        try {
            // 创建 URL 对象
            URL url = new URL(channel);

            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置请求方法为 POST
            conn.setRequestMethod("POST");

            // 设置请求头
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            // 启用输出流
            conn.setDoOutput(true);

            // 写入请求体
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 获取响应代码
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 获取响应体
            InputStream inputStream;
            if (responseCode >= 200 && responseCode < 300) {
                inputStream = conn.getInputStream(); // 成功时获取输入流
            } else {
                inputStream = conn.getErrorStream(); // 失败时获取错误流
                if (inputStream == null) {
                    System.out.println("Error response body is null.");
                }
            }

            String responseBody = ""; // 用来存储响应内容

            // 读取响应内容并拼接
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());  // 拼接每行响应内容
                }
                responseBody = response.toString();  // 将拼接好的响应体保存到 responseBody
                System.out.println("Response Body: " + responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isLastIteration){
                // 将响应内容转换为 JSON 对象存入是数据库
                try {
                    // 使用 org.json 库解析 JSON 字符串
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    System.out.println("Parsed JSON Response: " + jsonResponse);


                    // 获取 playerGuangGaoRes 对象
                    JSONObject playerGuangGaoRes;
                    int advertCount1;
                    int advertCount2;
                    String updateSql;

                    // 获取当前日期并格式化为 YYYY-MM-DD
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);
                    if (ios){
                        playerGuangGaoRes = jsonResponse.getJSONObject("playerStatus");
                        advertCount1 = playerGuangGaoRes.getInt("advert1");
                        advertCount2 = playerGuangGaoRes.getInt("advert2");
                        updateSql = String.format(
                                "UPDATE accountpointsios SET advertCount1 = %d, advertCount2 = %d, utime = '%s' WHERE playerId = %s",
                                advertCount1,advertCount2,formattedDate,playerId
                        );
                    }else {
                        playerGuangGaoRes = jsonResponse.getJSONObject("playerGuangGaoRes");
                        advertCount1 = playerGuangGaoRes.getInt("advertCount1");
                        advertCount2 = playerGuangGaoRes.getInt("advertCount2");
                        updateSql = String.format(
                                "UPDATE accountpoints SET advertCount1 = %d, advertCount2 = %d, utime = '%s' WHERE playerId = %s",
                                advertCount1,advertCount2,formattedDate,playerId
                        );
                    }
                    jdbcTemplate.update(updateSql);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            // 检查响应
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Request was successful.");
            } else {
                System.out.println("Request failed.");
            }
            // 关闭连接
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
