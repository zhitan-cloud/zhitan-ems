package com.dingzhuo.energy.project.reportForm.domain;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.dingzhuo.energy.framework.config.RuoYiConfig;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 焦化工序综合能耗 定制的 生成Excel
 */
public class JtZhnhExcel {
    /*public static void main(String[] args) {
        JtZhnhExcel a = new JtZhnhExcel();
        String downLoadFilePaht = a.writeExcel("e:\\jhnhrep.xlsx",new Object());
        System.out.println("生成后可以下载的文件路径:"+downLoadFilePaht);
    }*/
    /**
     * 生成焦炭综合能耗Excel报表
     * @param modeFilePath  要生成的Excel使用的模板绝对路径
     * @param list    这个就是一张报表的 JavaBean
     */
    public String writeExcel(String modeFilePath, List<consolidatedStatements> list) {
        OutputStream out = null;

        // 生成的下载文件路径
        String fownLoadFileName = System.currentTimeMillis()+ "_zhnhRep.xlsx";
        //String fownLoadFileName = "jhnhrep" + ".xlsx";
        //这个方法要注意下  只有在boot中启动才有效，如果单独run这个类做测试，要改下里面的内容
        String downLoadFilePath = getAbsoluteFile(fownLoadFileName);
        // 读取Excel文档
        File finalXlsxFile = createNewFile(modeFilePath,downLoadFilePath);//复制模板，
        Workbook workBook = null;
        try {
            workBook = getWorkbok(finalXlsxFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // sheet 对应一个工作页 插入数据开始 ------
        Sheet sheet = workBook.getSheetAt(0);
        Row row2 = sheet.getRow(2);// 获取到第3行
        Cell B3 = row2.getCell(1);// 3行 2列 投入_折标系数
        B3.setCellValue(list.get(0).getTrRljmZb());  // 这里就改成jtzhnhBean.get值就行，然后又几行写几行的数据就完活了
        Cell C3 = row2.getCell(2);// 3行 3列 投入_实物量
        C3.setCellValue(list.get(0).getTrRljmSwl());
        Cell D3 = row2.getCell(3);// 3行 4列 投入_折标准煤
        D3.setCellValue(list.get(0).getTrRljmZbzm());
        Cell F3 = row2.getCell(5);// 3行 6列 产出_折标系数
        F3.setCellValue(list.get(0).getCcJtZb());
        Cell G3 = row2.getCell(6);// 3行 7列 产出_实物量
        G3.setCellValue(list.get(0).getCcJtSwl());
        Cell H3 = row2.getCell(7);// 3行 8列 产出_折标准煤
        H3.setCellValue(list.get(0).getCcJtZbzm());

        Row row3 = sheet.getRow(3);// 获取到第4行
        Cell B4 = row3.getCell(1);// 4行 2列 投入_折标系数
        B4.setCellValue(list.get(0).getTrDianZb());  // 这里就改成jtzhnhBean.get值就行，然后又几行写几行的数据就完活了
        Cell C4 = row3.getCell(2);// 4行 3列 投入_实物量
        C4.setCellValue(list.get(0).getTrDianSwl());
        Cell D4 = row3.getCell(3);// 4行 4列 投入_折标准煤
        D4.setCellValue(list.get(0).getTrDianZbzm());
        Cell F4 = row3.getCell(5);// 4行 6列 产出_折标系数
        F4.setCellValue(list.get(0).getCcJyZb());
        Cell G4 = row3.getCell(6);// 4行 7列 产出_实物量
        G4.setCellValue(list.get(0).getCcJySwl());
        Cell H4 = row3.getCell(7);// 4行 8列 产出_折标准煤
        H4.setCellValue(list.get(0).getCcJyZbzm());

        Row row4 = sheet.getRow(4);// 获取到第5行
        Cell B5 = row4.getCell(1);// 5行 2列 投入_折标系数
        B5.setCellValue(list.get(0).getTrShuiZb());  // 这里就改成jtzhnhBean.get值就行，然后又几行写几行的数据就完活了
        Cell C5 = row4.getCell(2);// 5行 3列 投入_实物量
        C5.setCellValue(list.get(0).getTrShuiSwl());
        Cell D5 = row4.getCell(3);// 5行 4列 投入_折标准煤
        D5.setCellValue(list.get(0).getTrShuiZbzm());
        Cell F5 = row4.getCell(5);// 5行 6列 产出_折标系数
        F5.setCellValue(list.get(0).getCcCbZb());
        Cell G5 = row4.getCell(6);// 5行 7列 产出_实物量
        G5.setCellValue(list.get(0).getCcCbSwl());
        Cell H5 = row4.getCell(7);// 5行 8列 产出_折标准煤
        H5.setCellValue(list.get(0).getCcCbZbzm());

        Row row5 = sheet.getRow(5);// 获取到第6行
        Cell B6 = row5.getCell(1);// 6行 2列 投入_折标系数
        B6.setCellValue(list.get(0).getTrZqZb());  // 这里就改成jtzhnhBean.get值就行，然后又几行写几行的数据就完活了
        Cell C6 = row5.getCell(2);// 6行 3列 投入_实物量
        C6.setCellValue(list.get(0).getTrZqSwl());
        Cell D6 = row5.getCell(3);//6行 4列 投入_折标准煤
        D6.setCellValue(list.get(0).getTrZqZbzm());
        Cell F6 = row5.getCell(5);// 6行 6列 产出_折标系数
        F6.setCellValue(list.get(0).getCcMqZb());
        Cell G6 = row5.getCell(6);// 6行 7列 产出_实物量
        G6.setCellValue(list.get(0).getCcMqSwl());
        Cell H6 = row5.getCell(7);// 6行 8列 产出_折标准煤
        H6.setCellValue(list.get(0).getCcMqZbzm());

        Row row6 = sheet.getRow(6);// 获取到第7行
        Cell F7 = row6.getCell(5);// 7行 6列 产出_折标系数
        F7.setCellValue(list.get(0).getCcDianZb());
        Cell G7 = row6.getCell(6);// 7行 7列 产出_实物量
        G7.setCellValue(list.get(0).getCcDianSwl());
        Cell H7 = row6.getCell(7);// 7行 8列 产出_折标准煤
        H7.setCellValue(list.get(0).getCcDianZbzm());

        Row row7 = sheet.getRow(7);// 8获取到第3行
        Cell F8 = row7.getCell(5);// 8行 6列 产出_折标系数
        F8.setCellValue(list.get(0).getCcZqZb());
        Cell G8 = row7.getCell(6);// 8行 7列 产出_实物量
        G8.setCellValue(list.get(0).getCcZqSwl());
        Cell H8 = row7.getCell(7);// 8行 8列 产出_折标准煤
        H8.setCellValue(list.get(0).getCcZqZbzm());

        Row row8 = sheet.getRow(8);// 获取到第9行
        Cell B9 = row8.getCell(3);// 9行 2列 投入_折标系数
        B9.setCellValue(list.get(0).getTrZbzm());  // 这里就改成jtzhnhBean.get值就行，然后又几行写几行的数据就完活了
        Cell F9 = row8.getCell(6);// 9行 6列 产出_折标系数
        F9.setCellValue(list.get(0).getCcZbzm());

        Row row9 = sheet.getRow(9);// 获取到第10行
        Cell C9 = row9.getCell(3);// 10行 2列 投入_折标系数
        C9.setCellValue(list.get(0).getJhbm());  // 这里就改成jtzhnhBean.get值就行，然后又几行写几行的数据就完活了
        Cell G9 = row9.getCell(6);// 10行 6列 产出_折标系数
        G9.setCellValue(list.get(0).getZhnh());
        //插入数据结束
        try {
            out = new FileOutputStream(finalXlsxFile);
            workBook.write(out);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fownLoadFileName;
    }
    /**
     * 获取Excel工作簿
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        wb = new XSSFWorkbook(in);
        return wb;
    }
    /**
     * 根据模板新建文件并返回文件流用于填充数据
     * @param path   这个是模板路径
     * @param downLoadFilePath 这个是生成带数据的报表 下载路径
     * @return
     */
    private static File createNewFile(String path,String downLoadFilePath) {
        // 读取模板，并赋值到新文件************************************************************
        // 文件模板路径
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("原模板文件不存在");
        }
        // 写入到新的excel
        File newFile = new File(downLoadFilePath);
        try {
            newFile.createNewFile();
            // 复制模板到新文件
            FileUtils.copyFile(file, newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    private String getAbsoluteFile(String filename) {
        //Web运行的方法
        String downloadPath = RuoYiConfig.getDownloadPath() + filename;
        //main方法独立运行测试
        //String downloadPath = "e:\\"+filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }
}
