package com.ruoyi.quartz.task;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyTool {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String platformAndroid;

    private int interval;
    private final String sdk = "-dks-md5-sdf236dfd";
    private String playerId;
    private String version = "9.23.23254";
    private long dksTimes;
    private String okTaskInfo = "10026,10006,10040,10035,10002,10032,10025,10009,10020,10021,10027,10010,10037,10001,10003,10004,10005,10007,10008,10011,10012,10013,10014,10015,10016,10017,10018,10019,10022,10023,10024,10028,10029,10030,10031,10033,10034,10036,10038,10039,10041,10042,10043,10044,10045,10046,10047,10048,10049,10050";
//    完成任务
    private String completeAPI;
//    领取奖励
    private String receiveAPI;
//    补充任务
    private String supplementAPI;
    private List<Map<String, Object>> taskIdJson = new ArrayList<>();


    // 初始化时间戳
    public MyTool() {
        this.dksTimes = System.currentTimeMillis();
    }

    // 打印当前时间戳
    public void printDksTime() {
        System.out.println("Current dksTimes: " + dksTimes);
    }

    // 生成 MD5
    private String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 生成 MD5 数据
    public String createMd5Data(String taskInfo) {
        if (playerId == null || playerId.isEmpty()) {
            throw new IllegalStateException("playerId has not been initialized.");
        }
        String md5Input = String.format("%s,%s,%d,%s%s", playerId, version, dksTimes, taskInfo, sdk);
        return generateMD5(md5Input);
    }

    // 处理任务
    private void processTasks(String apiUrl, int requiredStatus) {
        for (Map<String, Object> task : taskIdJson) {
            int status = (int) task.get("status");
            if (status == requiredStatus) {
                String taskId = task.get("task_id").toString();
                String md5Data = createMd5Data(taskId);

                Map<String, Object> requestData = new HashMap<>();
                requestData.put("playerId", playerId);
                requestData.put("md5Data", md5Data);

                if (status == 2){
                    requestData.put("taskInfo", taskId);
                }else {
                    requestData.put("task_id", taskId);
                }

                requestData.put("time", dksTimes);
                requestData.put("version", version);
                requestData.put("platform", platformAndroid);

                sendPostRequest(apiUrl, requestData);
            }
        }
    }

    // 接收奖励
    public void receive() {
        processTasks(receiveAPI, 1);
    }

    // 补充任务
    public void supplement() {
        processTasks(supplementAPI, 2);
    }

    // 构建完成所有任务的请求体只需要发送一次
    public String complete() {
        String md5Data = createMd5Data(okTaskInfo);
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("playerId", playerId);
        requestData.put("md5Data", md5Data);
        requestData.put("okTaskInfo", okTaskInfo);
        requestData.put("time", dksTimes);
        requestData.put("version", version);
        requestData.put("platform", platformAndroid);

        return sendPostRequest(completeAPI, requestData);
    }

    // 解析任务 JSON 响应
    public List<Map<String, Object>> parseTaskIdJson(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        if (!jsonResponse.has("listSCDPlayerTaskStatus")) {
            System.err.println("Invalid response format: Missing 'listSCDPlayerTaskStatus'.");
            return new ArrayList<>();
        }

        JSONArray taskList = jsonResponse.getJSONArray("listSCDPlayerTaskStatus");
        List<Map<String, Object>> formattedTaskList = new ArrayList<>();
        for (int i = 0; i < taskList.length(); i++) {
            JSONObject task = taskList.getJSONObject(i);
            Map<String, Object> taskData = new HashMap<>();
            taskData.put("task_id", task.getInt("task_id"));
            taskData.put("status", task.getInt("status"));
            formattedTaskList.add(taskData);
        }
        this.taskIdJson = formattedTaskList;
        System.out.println("Formatted Task List: " + taskIdJson);
        return taskIdJson;
    }

    // 发送 POST 请求的通用方法
    private String sendPostRequest(String apiUrl, Map<String, Object> requestData) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = new JSONObject(requestData).toString();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
                Thread.sleep(interval);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                    ? connection.getInputStream() : connection.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            connection.disconnect();
            return response.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 主方法，用于测试
    public void execute(String sqlCode,String channel,Integer i) {
            platformAndroid = channel;
            interval = i;
            completeAPI = "https://surviveserver"+platformAndroid+".dksgames.com:9440/getSCDPlayerTaskInitAPI.json";
            receiveAPI = "https://surviveserver"+platformAndroid+".dksgames.com:9440/getSCDPlayerTaskRewardAPI.json";
            supplementAPI = "https://surviveserver"+platformAndroid+".dksgames.com:9440/getSCDPlayerTaskRefreshAPI.json";

        String dicQuery = String.format(
                    "SELECT css_class FROM sys_dict_data WHERE dict_code = '%s'",
                    sqlCode
            );

            List<Map<String, Object>> sqlResult = jdbcTemplate.queryForList(dicQuery);

            if (sqlResult.isEmpty()) {
                System.out.println("No query found in the dictionary.");
                return;
            }

            String taskQuery = sqlResult.get(0).get("css_class").toString();
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(taskQuery);

            for (Map<String, Object> row : resultList) {
                playerId = (String) row.get("playerId");
                System.out.println("Processing playerId: " + playerId);

                String response = complete();
                parseTaskIdJson(response);
                receive();
                supplement();
                parseTaskIdJson(response);
                response = complete();
                parseTaskIdJson(response);
                receive();
            }
    }
}