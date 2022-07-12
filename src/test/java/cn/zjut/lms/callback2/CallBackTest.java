package cn.zjut.lms.callback2;

public class CallBackTest implements StateChangeListener{

    public static void main(String[] args) {

        OpenClose oc = new OpenClose();
        //使用setOnChangeListener方法必须要StateChangeListener接口对象，并实现StateChangeListener接口的StateChange方法；
        oc.setOnChangeListener(new StateChangeListener() {
            @Override
            public void StateChange(boolean State) {
                if (State) {
                    System.out.println("开");
                } else {
                    System.out.println("关");
                }
            }
        });
//        StateChangeListener stateChangeListener = new StateChangeListener(this);



    }

//    public void init(){
//        OpenClose oc = new OpenClose();
//        oc.setOnChangeListener(this);
//    }


    @Override
    public void StateChange(boolean State) {

        if (State) {
            System.out.println("开1");
        } else {
            System.out.println("关1");
        }
    }
}
