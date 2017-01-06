package smsh.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.wteam.mixin.model.vo.BusinessBalanceRecordVo;
import com.wteam.mixin.utils.ExcelUtils;
import com.wteam.mixin.utils.FileUtils;
import com.wteam.mixin.utils.ExcelUtils.ExcelExportData;

public class TestFileUtils {
	
	@Test
	public void test() {
		
		System.out.println("TestFileUtils.test()" + FileUtils.getResourcePath("../../src/main/resources/validate/messages.properties"));
		Properties properties = FileUtils.getConfigProperties("../../src/main/resources/validate/messages.properties");
		
		properties.entrySet().stream()
		.sorted((e1,e2) -> ((String)e1.getKey()).compareTo((String)e2.getKey()))
		.forEach(e -> {
			System.out.println(e.getKey() + "\n" + e.getValue() + "\n");
		});;
	}
	
    @Test
    public void test_excel_out() throws Exception {
        String fileName = FileUtils.getResourcePath("../../src/test/resources/1.xls");
        System.out.println("TestFileUtils.test()" + fileName);
        
        FileUtils.createFile(fileName);
        ExcelExportData setInfo = new ExcelExportData();
        List<String[]> columNames = new ArrayList<String[]>();
        columNames.add(new String[] { "商家编号", "金额","之前的余额", "来源", "来源编号", "电话", "创建时间" });
//        columNames.add(new String[] { "运单号", "代理人" });

        List<String[]> fieldNames = new ArrayList<String[]>();
        fieldNames.add(new String[] { "businessId", "money" , "businessBeforeMoney" , "sourceName" , "sourceId" , "tel", "createTime" });
//        fieldNames.add(new String[] { "awbNumber", "agent" });

        LinkedHashMap<String, List<?>> map = new LinkedHashMap<String, List<?>>();
        
        List<BusinessBalanceRecordVo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++ ) {
            list.add(new BusinessBalanceRecordVo(1L, BigDecimal.ONE, BigDecimal.ONE, 1, 1L, "asdfasdf", new Date()));
        }
        
        map.put("商家财务报表", list);
//        map.put("运单月报(2月)", null);
        
        
        setInfo.setDataMap(map);
        setInfo.setFieldNames(fieldNames);
        setInfo.setTitles(new String[] { "商家财务报表"});
        setInfo.setColumnNames(columNames);
        
        ExcelUtils.getInstance().export2File(setInfo, fileName);

    }
	
	@Test
	public void test_getByteArray() throws IOException {
		
		long startTime,endTime;
		
		String path = "E:\\PPDownload\\视频\\华丽上班族HD国语中字.mp4";
		
		startTime = System.currentTimeMillis();
		FileUtils.copy(new File(path), "E:\\PPDownload\\视频\\华丽上班族HD国语中字.1.mp4");
		endTime = System.currentTimeMillis();
		
		System.out.println(endTime - startTime);
		
	}
	
	@Test
	public void test_getFileSize() throws IOException {
		long startTime,endTime;
		
		String path = "E:\\PPDownload\\视频\\华丽上班族HD国语中字.mp4";
		
		startTime = System.currentTimeMillis();
		long size = new File(path).length();
		endTime = System.currentTimeMillis();
		
		System.out.println(endTime - startTime);
		System.out.println(size);
		
	}

}
