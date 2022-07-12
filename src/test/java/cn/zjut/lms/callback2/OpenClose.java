package cn.zjut.lms.callback2;

// 自定义开关--!
class OpenClose {
    // 持有一个接口对象
    StateChangeListener stateChangeListener;

    // 提供注册事件监听的方法
    public void setOnChangeListener(StateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
        //因为是模拟，所以需要在此将控件状态传出
        IsClick(true);
    }
    //是否被点击
    public void IsClick(boolean isClick) {
        // 通过接口对象传控件状态
        stateChangeListener.StateChange(isClick);
    }
}
