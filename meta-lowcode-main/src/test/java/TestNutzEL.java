import org.nutz.el.El;
import org.nutz.lang.Lang;
import org.nutz.lang.util.Context;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/8/14 12:23
 * @Version: 1.0
 */

public class TestNutzEL {

    public static void main(String[] args) {
        PersonPojo p = new PersonPojo();
        Counter counter = new Counter();
        Context context = Lang.context();
        context.set("p", p);
        context.set("cc", counter);
        //El.eval(context, "p.setName(\"abc\")");
        El.eval(context, "p.setName('abc')");
        El.eval(context, "p.setAge(cc.count(10))");
        System.out.println(p.getName());
        System.out.println(p.getAge());
    }

}
