package cn.edu.zucc.shy.manager;

import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ArgsReader {
    public static Pair<Integer, ArrayList<Object>> argsReader(String[] args) {
        ArrayList<Object> variables = new ArrayList<>();
        OutputStreamWriter output = new OutputStreamWriter(System.out);
        FindWay map = new FindWay();
        int type = 0, hasMap = 0;
        String[] names = new String[2];
        // write your code here
        if (args.length == 1 && args[0].compareTo("-help") == 0) {
            System.out.println("用法 java subway [-options]");
            System.out.println("\t\t(地铁路线规划程序)");
            System.out.println("其中选项包括：");
            System.out.println("\t-map <地铁信息文件>\n\t\t\t指定地铁信息文件（必须项）");
            System.out.println("\t-o <输出文件>\t指定输出文件（若不指定则输出控制台）");
            System.out.println("\t-a <指定地铁线路>\n\t\t\t指定地铁线路输出");
            System.out.println("\t-b <起点站> <目的站>\n\t\t\t指定起点站与终点站，规划规划路线");
            System.out.println("\t\t(-a，-b 仅能选择其一，若两者都无，则输出所有线路地铁)");
            System.out.println("\t-help\t\t查看帮助信息（单独使用）");
            System.out.println("Powered by 孙华阳!");
            return new Pair<>(-1, null);
        }
        for (int i = 0; i < args.length; i++) {
            String tmp = args[i];
            if (tmp.charAt(0) == '-') {
                try {
                    if (tmp.compareTo("-map") == 0) {
                        String filepath;
                        i++;
                        filepath = args[i];
                        if (filepath.charAt(0) == '-')
                            throw new Exception("缺少-map后的一个参数");
                        map = new FindWay(filepath);
                        hasMap = 1;
                    } else if (tmp.compareTo("-o") == 0) {
                        String filepath;
                        i++;
                        filepath = args[i];
                        if (filepath.charAt(0) == '-')
                            throw new Exception("缺少-o后的一个参数");
                        output = new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_8);
                    } else if (tmp.compareTo("-a") == 0) {
                        if (type != 0) {
                            System.out.println("-a,-b都只能出现一次，使用-help参数查看帮助");
                            return new Pair<>(-1, null);
                        }
                        type = 1;
                        i++;
                        String lineName = args[i];
                        if (lineName.charAt(0) == '-')
                            throw new Exception("缺少-a后的一个参数");
                        names[0] = lineName;
                    } else if (tmp.compareTo("-b") == 0) {
                        if (type != 0) {
                            System.out.println("-a,-b都只能出现一次，使用-help参数查看帮助");
                            return new Pair<>(-1, null);
                        }
                        type = 2;
                        for (int j = 0; j < 2; j++) {
                            i++;
                            String stationName = args[i];
                            if (stationName.charAt(0) == '-')
                                throw new Exception("缺少-b后的参数");
                            names[j] = stationName;
                        }
                    } else if (tmp.compareTo("-help") == 0) {
                        System.out.println(tmp + "参数请单独使用");
                        return new Pair<>(-1, null);
                    } else {
                        System.out.println("无效参数" + tmp + "，使用-help参数查看帮助");
                        return new Pair<>(-1, null);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(tmp + "后参数错误，文件不存在，使用-help参数查看帮助");
                    return new Pair<>(-1, null);
                } catch (Exception e) {
                    System.out.println(tmp + "后参数错误，使用-help参数查看帮助");
                    return new Pair<>(-1, null);
                }
            }
        }
        if (hasMap == 0) {
            System.out.println("请使用-map参数传入地铁数据，使用-help参数查看帮助");
            return new Pair<>(-1, null);
        }
        variables.add(map);
        variables.add(names);
        variables.add(output);
        return new Pair<>(type, variables);
    }
}
