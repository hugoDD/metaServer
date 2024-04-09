import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/8/18 15:22
 * @Version: 1.0
 */

public class TestSet2Str {

    public static void main(String[] args) {
        Set<String> testSet = new LinkedHashSet<>();
        testSet.add("a");
        testSet.add("b");
        testSet.add("c");

        StringBuffer resultSb = new StringBuffer();
        testSet.forEach(o -> resultSb.append(o).append(","));
        System.out.println(resultSb);
    }

}
