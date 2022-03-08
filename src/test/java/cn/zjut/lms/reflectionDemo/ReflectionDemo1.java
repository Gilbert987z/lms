package cn.zjut.lms.reflectionDemo;
//什么叫反射
public class ReflectionDemo1 extends Object {

    public static void main(String[] args) throws ClassNotFoundException {
        //通过反射获取类的class对象
        Class c1 = Class.forName("cn.zjut.lms.reflectionDemo.User");
        System.out.println(c1);
        Class c2 = Class.forName("cn.zjut.lms.reflectionDemo.User");
        Class c3 = Class.forName("cn.zjut.lms.reflectionDemo.User");
        Class c4 = Class.forName("cn.zjut.lms.reflectionDemo.User");
        //一个类在内存中只有一个class对象
        //一个类被加载后,类的整个结构都会被封装在class对象中。
        System.out.println(c2.hashCode());
        System.out.println(c3.hashCode());
        System.out.println(c4.hashCode());

    }
}
///实体类:pojo , entity
class User{
    private String name;

}
