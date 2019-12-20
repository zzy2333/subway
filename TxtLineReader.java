package cn.edu.zucc.shy.manager;

import cn.edu.zucc.shy.model.Line;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class TxtLineReader {
    public static ArrayList<Line> read(String path) throws FileNotFoundException {
        ArrayList<Line> r = new ArrayList<>();
        Scanner input = new Scanner(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        while (input.hasNextLine()) {
            String it = input.nextLine();
            if (it.length() == 0) continue;
            Line b = new Line(it);
            r.add(b);
        }
        return r;
    }
}
