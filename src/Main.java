import java.util.ArrayList;

/**
 * @Author hone
 * @Date 2020/11/28 16:51
 * @Version 1.0
 * <p>
 * <p>
 * 蚁群算法思路：
 * 1. 创建 距离矩阵、 蚂蚁类 并 初始化 信息素矩阵；
 * 2. 初始化 蚂蚁个数、 禁忌表；
 * 3. 使 蚂蚁 从 不同 的地方作为始发点，进行路线选择 规则如下：
 * 3-1 从 禁忌表 中 开始选择 转移概率 最大且未走过的城市并行走
 * <p>
 * 转移概率 =
 * 信息素^ 权重1 * ( 距离^ - 权重2 )
 * ------------------------------------------------------
 * ∑(所有城市) 信息素^ 权重1 * ( 距离 ^ - 权重2 )
 * <p>
 * 3-2 每一只蚂蚁 全部都 走过之后 更新信息素矩阵 更新规则为  信息素 = 挥发后的信息素 + 每一只蚂蚁留下的信息素
 * <p>
 * 4. 更新 信息素矩阵；
 * 5. 重复 步骤 2 直到 所有蚂蚁的 行进路径 或者 行进距离 都相同或者到达迭代次数时 停止。
 * 6. 输出 行进距离 和 行进路径。
 */
public class Main {

//========= 参数定义 ==========//

    static int ANT_NUMBER = 4;
    static double VOLATILE_RATE = 0.8;
    static double PHERO_COUNT = 100;
    static int ITERATIONS = 1500;
    static int PARAM_PHEROMONE = 1;
    static int PARAM_DISTANCE = 1;

    //距离方阵
    public static double[][] distance = {
            {0, 1300, 1646, 2170},
            {1400, 0, 346, 870},
            {1800, 400, 0, 524},
            {2300, 900, 600, 0}
    };

//    public static double[][] distance = {
//            //  A     B     C     D     E     F     G     H
//            {   0,  256,  882, 1400, 1532, 1536, 1810, 1800},//A
//            { 256,    0,  647, 1250, 1380, 1380, 1655, 1641},//B
//            { 882,  647,    0,  600,  730,  730, 1005,  991},//C
//            { 1400,1250,  600,    0,  132,  130,  405,  393},//D
//            {1532, 1380,  730,  132,    0,  152,  427,  261},//E
//            {1536, 1380,  730,  130,  152,    0,  275,  413},//F
//            {1810, 1655, 1005,  405,  427,  275,    0,  147},//G
//            {1800, 1641,  991,  393,  261,  413,  147,    0}//H
//    };

    //信息素方阵
    public static double[][] pheromone = {
            {0, 2, 2, 2},
            {2, 0, 2, 2},
            {2, 2, 0, 2},
            {2, 2, 2, 0}
    };
//    public static double[][] pheromone = {
//            {0, 2, 2, 2, 2, 2, 2, 2},
//            {2, 0, 2, 2, 2, 2, 2, 2},
//            {2, 2, 0, 2, 2, 2, 2, 2},
//            {2, 2, 2, 0, 2, 2, 2, 2},
//            {2, 2, 2, 2, 0, 2, 2, 2},
//            {2, 2, 2, 2, 2, 0, 2, 2},
//            {2, 2, 2, 2, 2, 2, 0, 2},
//            {2, 2, 2, 2, 2, 2, 2, 0}
//    };

    //========== 主函数 ==========//
    public static void main(String[] args) {
        prints(pheromone);

        // 开始迭代
        for (int iteration = 0; iteration < ITERATIONS; iteration++) {
            System.out.println("第 " + iteration + " 代 ---->");
            // 初始化本代蚂蚁
            Ant[] Ants = new Ant[ANT_NUMBER];
            for (int name = 0; name < Ants.length; name++) {
                Ants[name] = new Ant(name + "");
            }
            int index = 0;
            // 本代蚂蚁开始寻路
            for (Ant ant : Ants) {

                // 初始化禁忌表
                ArrayList<Integer> tabuTable = new ArrayList<>();
                for (int city = 0; city < distance.length; city++) {
                    tabuTable.add(city);
                }

                // 随机选择一个出发点 并 移除出发点
//                int start_city = Util.randomNumber(0,4);
                Integer now_city = tabuTable.get(index % tabuTable.size());
                Integer start = now_city;
                index++;

//                System.out.println("蚂蚁 " + ant.name + " 出发地点为：" + now_city);
                ant.addCity(now_city);
                tabuTable.remove(now_city);


                // 开始行走
                while (tabuTable.size() > 0) {

                    // 寻找 转移概率最大的目的地
                    Integer next = maxArg(tabuTable, now_city);

                    // 移动蚂蚁 添加 蚂蚁行程 和 行走距离 并 从禁忌表中 移除本次目的地。
                    ant.addCity(next);
                    ant.addDistance(distance[now_city][next]);

                    now_city = next;

                    tabuTable.remove(now_city);

                }
                ant.addCity(start);
                ant.addDistance(distance[now_city][start]);

                ant.pheromone = PHERO_COUNT / ant.distace;
                System.out.println(ant.toString());
            }

            updatePheromone(pheromone, Ants);


            zuiyoulujin(Ants);

        }

    }

    public static void zuiyoulujin(Ant[] Ants) {

        Ant res = Ants[0];
        for (Ant ant : Ants) {
            if (res.distace > ant.distace) {
                res = ant;
            }
        }

        System.out.println("最佳路线为：\n" + res.toString() + "\n");

    }

    /**
     * 更新 信息素表
     *
     * @param pheromone 信息素表
     * @param Ants      各个蚂蚁的具体类
     */
    public static void updatePheromone(double[][] pheromone, Ant[] Ants) {

        // 每回合 挥发 信息素
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = 0; j < pheromone[i].length; j++) {
                pheromone[i][j] = pheromone[i][j] * VOLATILE_RATE;
            }
        }
        for (Ant ant : Ants) {
            for (int i = 0; i < ant.a.size() - 1; i++) {
                pheromone[ant.a.get(i)][ant.a.get(i + 1)] += ant.pheromone;
            }
        }
        prints(pheromone);
    }

    /**
     * 获取 移动概率 最大的城市 对象
     *
     * @param body
     * @param indexX
     * @return
     */
    public static Integer maxArg(ArrayList<Integer> body, int indexX) {

        double sum = 0;
        double max = 0;
        Integer indexY = 0;

        for (Integer tmp : body) {
            sum += compute(pheromone[indexX][tmp], distance[indexX][tmp]);
        }

        for (Integer tmp : body) {

            if (indexX == tmp)
                continue;

            double tm = compute(pheromone[indexX][tmp], distance[indexX][tmp]) / sum;

            if (max < tm) {
                max = tm;
                indexY = tmp;
            }
        }

        return indexY;

    }

    /**
     * 计算 移动概率
     *
     * @param x 该路径的信息素
     * @param y 该路径的长度
     * @return 该 移动概率 数值
     */
    public static double compute(double x, double y) {

        double res = Math.pow(x, PARAM_PHEROMONE) * Math.pow(y, -PARAM_DISTANCE);

        return res;

    }

    // 打印信息素
    public static void prints(double[][] arr) {
        for (double[] tmp : arr) {
            for (double t : tmp) {
                System.out.print(String.format("%.1f", t).trim() + "  ");
            }
            System.out.println();
        }
    }
}