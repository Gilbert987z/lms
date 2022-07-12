package cn.zjut.lms.callback;

public class MainBusiness {

    private CallbackService callback;

    public void execute(CallbackService callback) {
        this.callback = callback;
        callBack();
    }

    public void callBack() {
        callback.callBackFunc();
    }
}
