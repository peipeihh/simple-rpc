package quickstart;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by huangyinhuang on 1/19/2018.
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:motan_client.xml");

        FooService service = (FooService) ctx.getBean("remoteService");
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                System.out.println(service.hello("motan" + i));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("a exception is received on motan" + i);
            }
            Thread.sleep(2000);
        }

        System.out.println("motan demo is finish.");
        System.exit(0);
    }
}
