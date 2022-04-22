import java.util.ArrayList;

/**
 * @Author hone
 * @Date 2020/11/28 17:13
 * @Version 1.0
 * <p>
 * 蚁群算法思路：
 * 1. 创建 距离矩阵 、 蚂蚁类
 */
public class Ant {

    public ArrayList<Integer> a;

    public String name;

    public double distace = 0;

    public double pheromone = 0;

    public Ant(String name) {
        a = new ArrayList<>();
        this.name = name;
    }

    public void addCity(Integer i) {
        a.add(i);
    }

    public void addDistance(double i) {
        distace += i;
    }

    public String toString() {

        String[] dic = {"起始点", "双子座", "培训楼", "科技楼"};

        String res = "蚂蚁" + name + "的行进路线为：";

        for (Integer tmp : a) {
            res += dic[tmp] + " -> ";
        }

        res += distace + "  遗留信息素：" + pheromone;

        return res;
    }

}

