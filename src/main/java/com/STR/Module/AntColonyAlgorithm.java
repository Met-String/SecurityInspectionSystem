package com.STR.Module;

import com.STR.entity.Site;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AntColonyAlgorithm {
//    private final String pythonScriptPath = "C:\\Users\\24957\\Desktop\\Ant-Colony-Optimization\\aco_tsp.py";
    private final String pythonScriptPath = "/home/Yosafire/InspectionSystem/aco_tsp.py";
    private String jsonInput = "[[41866.0, 63085.0], [41987.0, 62621.0], [41706.0, 62093.0], [41701.0, 61120.0], [41966.0, 60666.0]]";
    public List<Site> run(List<Site> sites) {
        List<List<Double>> points = new ArrayList<>();
        // 此处为天津理工大学后勤处坐标
        points.add(Arrays.asList(117.145944 * 1000,39.057393 * 1000));
        for (Site site : sites) {
            points.add(Arrays.asList(site.getLongitude() * 1000, site.getLatitude() * 1000));
        }
        List<Site> newSites = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonInput = objectMapper.writeValueAsString(points);
            ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScriptPath, jsonInput);
            processBuilder.redirectErrorStream(true); // 合并标准错误和标准输出流
            // 启动进程
            Process process = processBuilder.start();
            // 读取进程的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ArrayList<Integer> PointList = objectMapper.readValue(reader.readLine(), ArrayList.class);
            System.out.println("排序结果为：" + PointList.toString());
            // 对传入的sites进行重组
            newSites = new ArrayList<>();
            for (int i : PointList) {
                if(i > 1) newSites.add(sites.get(i - 2));
            }
            String line;
            System.out.println("运算结果为:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束
            int exitCode = process.waitFor();
            System.out.println("进程结束！: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newSites;
    }
}

