import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/8/14 11:35
 * @Version: 1.0
 */

public class TestAviator {

    private static void testHello() {
        Expression exp = AviatorEvaluator.getInstance().compile("println('Hello, Aviator')");
        exp.execute();
    }

    private static void testPojo() throws NoSuchMethodException, IllegalAccessException {
        //String scriptText = " let p = new PersonPojo(); p.setName('aaa'); println(p.getName()) ";
        PersonPojo p = new PersonPojo();
        AviatorEvaluator.addInstanceFunctions("Person", PersonPojo.class);
        //String scriptText = " p.setName('aaa'); println(p.getName()) ";
        String scriptText = " Person.setName(p, 'aaa'); println(Person.getName(p)) ";
        Expression exp = AviatorEvaluator.getInstance().compile(scriptText);
        exp.execute(exp.newEnv("p", p));
        System.out.println(p.getName());
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {
        testHello();

//        testPojo();
    }

}
