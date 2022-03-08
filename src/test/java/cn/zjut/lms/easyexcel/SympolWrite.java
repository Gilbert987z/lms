//package cn.zjut.lms.easyexcel;
//
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.ExcelWriter;
//import com.alibaba.excel.write.metadata.WriteSheet;
//import org.junit.jupiter.api.Test;
//
//public class SympolWrite {
//    /**
//     * 最简单的写
//     * <p>
//     * 1. 创建excel对应的实体对象 参照{@link DemoData}
//     * <p>
//     * 2. 直接写即可
//     */
//    @Test
//    public void simpleWrite() {
//        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
//
//        // 写法1 JDK8+
//        // since: 3.0.0-beta1
//        String fileName =  "simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        // 如果这里想使用03 则 传入excelType参数即可
//        EasyExcel.write(fileName, DemoData.class)
//                .sheet("模板")
//                .doWrite(() -> {
//                    // 分页查询数据
//                    return data();
//                });
//
//        // 写法2
//        fileName = "simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        // 如果这里想使用03 则 传入excelType参数即可
//        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());
//
//        // 写法3
//        fileName = "simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写
//        ExcelWriter excelWriter = null;
//        try {
//            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
//            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
//            excelWriter.write(data(), writeSheet);
//        } finally {
//            // 千万别忘记finish 会帮忙关闭流
//            if (excelWriter != null) {
//                excelWriter.finish();
//            }
//        }
//    }
//}
