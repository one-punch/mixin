package com.wteam.mixin.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Administrator
 */
public class ExcelUtils {
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(ExcelUtils.class.getName());
    
    private HSSFWorkbook hssfworkbook;

    private HSSFWorkbook wb;

    private XSSFWorkbook hssfworkbook2;


    private CellStyle titleStyle; // 标题行样式
    private Font titleFont; // 标题行字体
    private CellStyle dateStyle; // 日期行样式
    private Font dateFont; // 日期行字体
    private CellStyle headStyle; // 表头行样式
    private Font headFont; // 表头行字体
    private CellStyle contentStyle; // 内容行样式
    private Font contentFont; // 内容行字体
    
    private final static int MAX_COLUMN = 7;
    
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static ExcelUtils getInstance() {
        return new ExcelUtils();
    }

    /**
     * 读取office 2003 xls
     * 
     * @param file
     * @return
     */
    public List<List<Object>> loadXls(File file) {
        List<List<Object>> rowsList = new ArrayList<>();// 存储单位：行
        List<Object> rowList;// 存储单位：列
        try {
            InputStream input = new FileInputStream(file);
            POIFSFileSystem fs = new POIFSFileSystem(input);
            wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            // Iterate over each row in the sheet
            Iterator<?> rows = sheet.rowIterator();
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow)rows.next();
                System.out.println("Row #" + row.getRowNum());
                // Iterate over each cell in the row and print out the cell"s
                // content
                Iterator<?> cells = row.cellIterator();
                rowList = new ArrayList<>();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell)cells.next();
                    // 如果大于最大列数退出循环
                    if (cell.getColumnIndex() > MAX_COLUMN) break;

                    System.out.println("Cell #" + cell.getColumnIndex() + cell.getCellType());
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            System.out.println(cell.getNumericCellValue());
                            rowList.add(cell.getNumericCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_BLANK:
                            System.out.println(cell.getStringCellValue());
                            rowList.add(null);
                            break;
                        case HSSFCell.CELL_TYPE_STRING:
                            System.out.println(cell.getStringCellValue());
                            rowList.add(cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            rowList.add(cell.getBooleanCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            System.out.println(cell.getCellFormula());
                            rowList.add(cell.getCellFormula());
                            break;
                        default:
                            System.out.println("unsuported sell type");
                            break;
                    }
                }
                rowsList.add(rowList);
            }

            return rowsList;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    


    /**
     * 导出文件
     * 
     * @param setInfo
     * @param outputExcelFileName
     * @return
     * @throws IOException
     */
    public boolean export2File(ExcelExportData setInfo,
            String outputExcelFileName) throws Exception {
        
        FileOutputStream fop = new FileOutputStream(outputExcelFileName);
        
        export2Stream(setInfo, fop);
        fop.flush();
        fop.close();
        return true;
    } 

    /**
     * 导出到byte数组
     * 
     * @param setInfo
     * @return
     * @throws Exception
     *//*
    public static byte[] export2ByteArray(ExcelExportData setInfo)
            throws Exception {
        return export2Stream(setInfo).toByteArray();
    }*/

    /**
     * 导出到流
     * 
     * @param setInfo
     * @return
     * @throws Exception
     */
    public void export2Stream(ExcelExportData setInfo, OutputStream outputStream)
            throws Exception {
        init();
        
        Set<Entry<String, List<?>>> set = setInfo.getDataMap().entrySet();
        String[] sheetNames = setInfo.getDataMap().keySet().stream().toArray(String[]::new);
        HSSFSheet[] sheets = getSheets(setInfo.getDataMap().size(), sheetNames);
        int sheetNum = 0;
        for (Entry<String, List<?>> entry : set) {
            // Sheet
            List<?> objs = entry.getValue();

            // 标题行
            createTableTitleRow(setInfo, sheets, sheetNum);

            // 日期行
            createTableDateRow(setInfo, sheets, sheetNum);

            // 表头
            creatTableHeadRow(setInfo, sheets, sheetNum);

            // 表体
            String[] fieldNames = setInfo.getFieldNames().get(sheetNum);

            int rowNum = 3;
            for (Object obj : objs) {
                HSSFRow contentRow = sheets[sheetNum].createRow(rowNum);
                contentRow.setHeight((short) 300);
                HSSFCell[] cells = getCells(contentRow, setInfo.getFieldNames()
                        .get(sheetNum).length);
                int cellNum = 1; // 去掉一列序号，因此从1开始
                if (fieldNames != null) {
                    for (int num = 0; num < fieldNames.length; num++) {
                        HSSFCell cell = cells[cellNum];
                        try {
                            Object value = PropertyUtils.getProperty(obj, fieldNames[num]);
                            if (value instanceof Date) {
                                cell.setCellValue(value == null ? "" : FORMAT.format(value));
                            } else
                            if (value instanceof Boolean) {
                                cell.setCellValue(value == null ? "否" : (Boolean)value ? "是" : "否");
                            } else {
                                cell.setCellValue(value == null ? "" : value.toString());
                            }
                            cellNum++;
                        }
                        catch (Exception e) {
                            LOG.error("{} 没有 {}字段",obj, fieldNames[num]);
                            cell.setCellValue("");
                            cellNum++;
                        }
                    }
                }
                rowNum++;
            }
            adjustColumnSize(sheets, sheetNum, fieldNames); // 自动调整列宽
            sheetNum++;
        }
        wb.write(outputStream);

    }
    
    /**
     * 输出到http响应中
     * @param response
     * @param data
     * @throws IOException
     * @throws Exception
     */
    public static void exportToHttpResponse(HttpServletResponse response, String fileName, ExcelExportData data) throws IOException, Exception {
        response.reset();// 不加这一句的话会出现下载错误
        response.setHeader("Content-disposition",
            "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));// 设定输出文件头
        response.setContentType("application/vnd.ms-excel");// 定义输出类型
        
        ExcelUtils.getInstance().export2Stream(data,response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    /**
     * @Description: 初始化
     */
    private void init() {
        wb = new HSSFWorkbook();

        titleFont = wb.createFont();
        titleStyle = wb.createCellStyle();
        dateStyle = wb.createCellStyle();
        dateFont = wb.createFont();
        headStyle = wb.createCellStyle();
        headFont = wb.createFont();
        contentStyle = wb.createCellStyle();
        contentFont = wb.createFont();

        initTitleCellStyle();
        initTitleFont();
        initDateCellStyle();
        initDateFont();
        initHeadCellStyle();
        initHeadFont();
        initContentCellStyle();
        initContentFont();
    }

    /**
     * @Description: 自动调整列宽
     */
    private void adjustColumnSize(HSSFSheet[] sheets, int sheetNum,
            String[] fieldNames) {
        for (int i = 0; i < fieldNames.length + 1; i++) {
            sheets[sheetNum].autoSizeColumn(i, true);
        }
    }

    /**
     * @Description: 创建标题行(需合并单元格)
     */
    private void createTableTitleRow(ExcelExportData setInfo,
            HSSFSheet[] sheets, int sheetNum) {
        CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, setInfo.getFieldNames().get(sheetNum).length);
        sheets[sheetNum].addMergedRegion(titleRange);
        HSSFRow titleRow = sheets[sheetNum].createRow(0);
        titleRow.setHeight((short) 800);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue(setInfo.getTitles()[sheetNum]);
    }

    /**
     * @Description: 创建日期行(需合并单元格)
     */
    private void createTableDateRow(ExcelExportData setInfo,
            HSSFSheet[] sheets, int sheetNum) {
        CellRangeAddress dateRange = new CellRangeAddress(1, 1, 0, setInfo
                .getFieldNames().get(sheetNum).length);
        sheets[sheetNum].addMergedRegion(dateRange);
        HSSFRow dateRow = sheets[sheetNum].createRow(1);
        dateRow.setHeight((short) 350);
        HSSFCell dateCell = dateRow.createCell(0);
        dateCell.setCellStyle(dateStyle);
        // dateCell.setCellValue("导出时间：" + new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // .format(new Date()));
        dateCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date()));
    }

    /**
     * @Description: 创建表头行(需合并单元格)
     */
    private void creatTableHeadRow(ExcelExportData setInfo,
            HSSFSheet[] sheets, int sheetNum) {
        // 表头
        HSSFRow headRow = sheets[sheetNum].createRow(2);
        headRow.setHeight((short) 350);
        // 序号列
        HSSFCell snCell = headRow.createCell(0);
        snCell.setCellStyle(headStyle);
        snCell.setCellValue("序号");
        // 列头名称
        for (int num = 1, len = setInfo.getColumnNames().get(sheetNum).length; num <= len; num++) {
            HSSFCell headCell = headRow.createCell(num);
            headCell.setCellStyle(headStyle);
            headCell.setCellValue(setInfo.getColumnNames().get(sheetNum)[num - 1]);
        }
    }

    /**
     * @Description: 创建所有的Sheet
     */
    private HSSFSheet[] getSheets(int num, String[] names) {
        HSSFSheet[] sheets = new HSSFSheet[num];
        for (int i = 0; i < num; i++) {
            sheets[i] = wb.createSheet(names[i]);
        }
        return sheets;
    }

    /**
     * @Description: 创建内容行的每一列(附加一列序号)
     */
    private HSSFCell[] getCells(HSSFRow contentRow, int num) {
        HSSFCell[] cells = new HSSFCell[num + 1];

        for (int i = 0, len = cells.length; i < len; i++) {
            cells[i] = contentRow.createCell(i);
            cells[i].setCellStyle(contentStyle);
        }

        // 设置序号列值，因为出去标题行和日期行，所有-2
        cells[0].setCellValue(contentRow.getRowNum() - 2);

        return cells;
    }

    /**
     * @Description: 初始化标题行样式
     */
    private void initTitleCellStyle() {
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titleStyle.setFont(titleFont);
        titleStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
    }

    /**
     * @Description: 初始化日期行样式
     */
    private void initDateCellStyle() {
        dateStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
        dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dateStyle.setFont(dateFont);
        dateStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
    }

    /**
     * @Description: 初始化表头行样式
     */
    private void initHeadCellStyle() {
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headStyle.setFont(headFont);
        headStyle.setFillBackgroundColor(IndexedColors.YELLOW.index);
        headStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        headStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headStyle.setBorderRight(CellStyle.BORDER_THIN);
        headStyle.setTopBorderColor(IndexedColors.BLUE.index);
        headStyle.setBottomBorderColor(IndexedColors.BLUE.index);
        headStyle.setLeftBorderColor(IndexedColors.BLUE.index);
        headStyle.setRightBorderColor(IndexedColors.BLUE.index);
    }

    /**
     * @Description: 初始化内容行样式
     */
    private void initContentCellStyle() {
        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
        contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        contentStyle.setFont(contentFont);
        contentStyle.setBorderTop(CellStyle.BORDER_THIN);
        contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
        contentStyle.setBorderLeft(CellStyle.BORDER_THIN);
        contentStyle.setBorderRight(CellStyle.BORDER_THIN);
        contentStyle.setTopBorderColor(IndexedColors.BLUE.index);
        contentStyle.setBottomBorderColor(IndexedColors.BLUE.index);
        contentStyle.setLeftBorderColor(IndexedColors.BLUE.index);
        contentStyle.setRightBorderColor(IndexedColors.BLUE.index);
        contentStyle.setWrapText(true); // 字段换行
    }

    /**
     * @Description: 初始化标题行字体
     */
    private void initTitleFont() {
        titleFont.setFontName("华文楷体");
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setCharSet(Font.DEFAULT_CHARSET);
        titleFont.setColor(IndexedColors.BLUE_GREY.index);
    }

    /**
     * @Description: 初始化日期行字体
     */
    private void initDateFont() {
        dateFont.setFontName("隶书");
        dateFont.setFontHeightInPoints((short) 12);
        dateFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        dateFont.setCharSet(Font.DEFAULT_CHARSET);
        dateFont.setColor(IndexedColors.BLUE_GREY.index);
    }

    /**
     * @Description: 初始化表头行字体
     */
    private void initHeadFont() {
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headFont.setCharSet(Font.DEFAULT_CHARSET);
        headFont.setColor(IndexedColors.BLUE_GREY.index);
    }

    /**
     * @Description: 初始化内容行字体
     */
    private void initContentFont() {
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 12);
        contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        contentFont.setCharSet(Font.DEFAULT_CHARSET);
        contentFont.setColor(IndexedColors.BLUE_GREY.index);
    }

    /**
     * Excel导出数据类
     * 
     * @author jimmy
     *
     */
    public static class ExcelExportData {

        /**
         * 导出数据 key:String 表示每个Sheet的名称 value:List<?> 表示每个Sheet里的所有数据行
         */
        private LinkedHashMap<String, List<?>> dataMap;

        /**
         * 每个Sheet里的顶部大标题
         */
        private String[] titles;

        /**
         * 单个sheet里的数据列标题
         */
        private List<String[]> columnNames;

        /**
         * 单个sheet里每行数据的列对应的对象属性名称
         */
        private List<String[]> fieldNames;

        public List<String[]> getFieldNames() {
            return fieldNames;
        }

        public void setFieldNames(List<String[]> fieldNames) {
            this.fieldNames = fieldNames;
        }

        public String[] getTitles() {
            return titles;
        }

        public void setTitles(String[] titles) {
            this.titles = titles;
        }

        public List<String[]> getColumnNames() {
            return columnNames;
        }

        public void setColumnNames(List<String[]> columnNames) {
            this.columnNames = columnNames;
        }

        public LinkedHashMap<String, List<?>> getDataMap() {
            return dataMap;
        }

        public void setDataMap(LinkedHashMap<String, List<?>> dataMap) {
            this.dataMap = dataMap;
        }

    }
    
    public static void main(String[] args) {
        
        Object[] array = new Object[]{"a", "b"};
        
        String[] arr = Stream.of(array).toArray(String[]::new);
        
        System.out.println(Arrays.toString(arr));
    }

}
