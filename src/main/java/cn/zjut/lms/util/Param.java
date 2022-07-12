package cn.zjut.lms.util;

public interface Param {
    /**
     * 图书状态
     * 1:上架;2:下架;
     */
    public static Integer BOOK_STATUS_ON = 1; //上架
    public static Integer BOOK_STATUS_OFF = 0; //下架

    /**
     * 图书借阅历史
     * 1:借阅;2:归还;3:遗失;4:未及时归还'
     */
    public static Integer BOOK_BORROW_STATUS_BORROW = 1; //借阅
    public static Integer BOOK_BORROW_STATUS_RETURN = 2; //归还
    public static Integer BOOK_BORROW_STATUS_LOST = 3; //遗失
    public static Integer BOOK_BORROW_STATUS_TIMEOUT = 4; //未及时归还

}
