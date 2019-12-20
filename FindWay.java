package cn.edu.zucc.shy.manager;

import javafx.util.Pair;
import cn.edu.zucc.shy.model.Edge;
import cn.edu.zucc.shy.model.Line;
import cn.edu.zucc.shy.model.NowAt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class FindWay {
    private ArrayList<Line> lines;
    private ArrayList<Pair<String, Vector<Pair<Integer, Integer>>>> stations;
    private Map<String, Integer> mpLine;
    private Map<String, Integer> mpStation;
    private ArrayList<Vector<Edge>> edges;

    public FindWay() {
        lines = new ArrayList<>();
        stations = new ArrayList<>();
        mpLine = new HashMap<>();
        mpStation = new HashMap<>();
        edges = new ArrayList<>();
    }

    public FindWay(String filepath) throws FileNotFoundException {
        this();
        lines = TxtLineReader.read(filepath);
        for (int i = 0; i < lines.size(); i++) {
            mpLine.put(lines.get(i).getName(), i);
            for (int j = 0; j < lines.get(i).getStations().size(); j++) {
                String stationName = lines.get(i).getStations().get(j).getName();
                if (mpStation.get(stationName) == null) {
                    Vector<Pair<Integer, Integer>> p = new Vector<>();
                    p.add(new Pair<>(i, j));
                    stations.add(new Pair<>(stationName, p));
                    mpStation.put(stationName, stations.size() - 1);
                } else {
                    stations.get(mpStation.get(stationName)).getValue().add(new Pair<>(i, j));
                }
            }
        }
        for (int i = 0; i < stations.size(); i++) {
            edges.add(new Vector<>());
        }
        for (int i = 0; i < lines.size(); i++) {
            int u = mpStation.get(lines.get(i).getStations().get(0).getName());
            for (int j = 1; j < lines.get(i).getStations().size(); j++) {
                int v = mpStation.get(lines.get(i).getStations().get(j).getName());
                edges.get(u).add(new Edge(v, i));
                edges.get(v).add(new Edge(u, i));
                u = v;
            }
        }
    }

    public void outLineInf(String lineName, OutputStreamWriter output) throws IOException, NullPointerException {
        lines.get(mpLine.get(lineName)).print(output);
    }

    public void outWay(String start, String end, OutputStreamWriter output) throws Exception {
        int success_flag = 0;
        Integer st = mpStation.get(start), en = mpStation.get(end);
        StringBuilder r = new StringBuilder();
        int minLength = 0x3f3f3f3f, minChange = 0x3f3f3f3f;
        if (st == null || en == null)
            throw new Exception("站点不存在");
        for (Pair<Integer, Integer> it : stations.get(st).getValue()) {
            int[] vis = new int[stations.size()];
            NowAt[] path = new NowAt[stations.size()];
            Queue<NowAt> que = new PriorityQueue<>((o1, o2) -> {
                if (o1.length == o2.length) {
                    return o1.changeLine - o2.changeLine;
                }
                return o1.length - o2.length;
            });
            que.add(new NowAt(-1, st, it.getKey(), 0, 0));
            while (que.size() != 0) {
                NowAt tmp = que.poll();
                if (vis[tmp.atStation] == 1) {
                    continue;
                }
                path[tmp.atStation] = tmp;
                vis[tmp.atStation] = 1;
                if (tmp.atStation == en) {
                    success_flag = 1;
                    if (minLength > tmp.length || minLength == tmp.length && minChange > tmp.changeLine) {
                        minLength = Math.min(minLength, tmp.length);
                        minChange = Math.min(minChange, tmp.changeLine);
                        r = new StringBuilder();
                        int now = en, line = path[now].atLine;
                        while (now != -1) {
                            r.insert(0, " " + stations.get(now).getKey());
                            now = path[now].from;
                            if (now == -1 || path[now].atLine != line) {
                                int oldLine = line;
                                if (now != -1) {
                                    r.insert(0, " " + stations.get(now).getKey());
                                    line = path[now].atLine;
                                }
                                r.insert(0, "\n<" + lines.get(oldLine).getName() + ">");
                            }
                        }
                        output.write("从 " + start + " 到 " + end + " 共经过" + (minChange + 1) + "条线路，" + (minLength + 1) + "个站点" + r);
                    }
                }
                for (Object itt : edges.get(tmp.atStation)) {
                    if (vis[((Edge) itt).getTo()] == 0) {
                        que.add(new NowAt(tmp.atStation, ((Edge) itt).getTo(), ((Edge) itt).getAtLine(), tmp.length + 1, tmp.changeLine + (((Edge) itt).getAtLine() == tmp.atLine ? 0 : 1)));
                    }
                }
            }
        }
        if (success_flag == 0) {
            System.out.println(start + " 与 " + end + " 无法通过地铁到达");
            return;
        }
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
}