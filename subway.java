import cn.edu.zucc.shy.Main;

public class subway {
    public static void main(String[] args) {
        Main.main(args);
    }
}
/*
cd src
javac -encoding UTF-8 subway.java cn.edu.zucc.shy.Main.java cn.edu.zucc.shy.model/NowAt.java cn.edu.zucc.shy.model/Edge.java cn.edu.zucc.shy.model/Line.java cn.edu.zucc.shy.model/Station.java cn.edu.zucc.shy.manager/ArgsReader.java cn.edu.zucc.shy.manager/FindWay.java cn.edu.zucc.shy.manager/TxtLineReader.java
java subway -map ../subway.txt  -o 1.txt
 */