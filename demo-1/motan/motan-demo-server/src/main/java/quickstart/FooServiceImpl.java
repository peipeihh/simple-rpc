package quickstart;

/**
 * Created by huangyinhuang on 1/19/2018.
 */
public class FooServiceImpl implements FooService {

    @Override
    public String hello(String name) {
        System.out.println(name + " invoked rpc service");
        return "hello " + name;
    }

}
