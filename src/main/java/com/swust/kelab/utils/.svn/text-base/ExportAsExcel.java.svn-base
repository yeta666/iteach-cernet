package com.swust.kelab.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout.Alignment;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import com.swust.kelab.domain.Department;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseSelectModel;

public class ExportAsExcel {
	static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 公共组件，导出Excel表格数据方法
	 * <p>
	 * 使用方法： 数据为List<Object>类型，注意当中object的属性必须为java基本数据类型
	 * 
	 * @author pery
	 * @param className
	 *            数据模型Object的类完整名称 ，eg：com.swust.kelab.Department
	 * @param propertyName
	 *            数据模型类的属性对应的列名， 如“depaId”对应列表“机构id“， 未指定列名的属性不会被导出
	 * @param tableName
	 *            导出的excel中的表名称
	 * @param record
	 *            导出的文件绝对路径（必须为存在的文件，在本方法中没有做文件路径检查）
	 * @param objectList
	 *            要导出的数据集
	 * @return 成功返回true，失败返回false；
	 */
	public static boolean exportExcel(String className, Map propertyName,
			String tableName, File record, List<? extends Object> objectList) {
		HSSFWorkbook wbook = new HSSFWorkbook();
		HSSFSheet wsheet = wbook.createSheet(tableName); // 表名
		HSSFCellStyle cellStyle = getStyle(wbook);
		// 设置Excel表头
		HSSFRow excelTitle = wsheet.createRow(0);
		excelTitle.setHeightInPoints(22);
		if (objectList.size() <= 0) {
			wsheet.autoSizeColumn(0);
			HSSFCell titleCell = excelTitle.createCell(0);
			titleCell.setCellValue("对不起，没有可用数据导出");
			titleCell.setCellStyle(cellStyle);
		}
		Class c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e1) {
			System.out.println("传入反射用到的类名字有误");
			e1.printStackTrace();
			return false;
		}
		java.lang.reflect.Field[] flds = c.getDeclaredFields();
		int j = 0;
		for (int i = 0; i < flds.length; i++) {
			String columName = flds[i].getName();
			if (propertyName.containsKey(columName)) {
				wsheet.setColumnWidth(j + 1, (int) (100 * 35.7));
				HSSFCell titleCell = excelTitle.createCell(j++);
				titleCell.setCellValue((String) propertyName.get(columName));
				titleCell.setCellStyle(cellStyle);
			}
		}
		int count=0;
		int sheetNum=1;
		for (int i = 0; i < objectList.size(); i++) {
		    if(count>=65530){
                count=0;
                j=0;
                sheetNum++;
                wsheet = wbook.createSheet(tableName+sheetNum); // 表名
                wsheet.autoSizeColumn(0);
                cellStyle = getStyle(wbook);
                // 设置Excel表头
                excelTitle = wsheet.createRow(0);
                excelTitle.setHeightInPoints(22);
                for (int k = 0; k < flds.length; k++) {
                    String columName = flds[k].getName();
                    if (propertyName.containsKey(columName)) {
                        wsheet.setColumnWidth(j + 1, (int) (100 * 35.7));
                        HSSFCell titleCell = excelTitle.createCell(j++);
                        titleCell.setCellValue((String) propertyName.get(columName));
                        titleCell.setCellStyle(cellStyle);
                    }
                }
            }
			Object obj = objectList.get(i);
			HSSFRow row = wsheet.createRow(count + 1);
			int k = 0;
			for (j = 0; j < flds.length; j++) {
				String columName = flds[j].getName();
				if (!propertyName.containsKey(columName)) {
					continue;
				}
				HSSFCell hssfCell = row.createCell(k++);
				try {
					flds[j].setAccessible(true);
					Object t = flds[j].get(obj);
					if (t instanceof Date) {
						t = dateFormat.format(t);
					}
					if (t != null) {
						hssfCell.setCellValue(String.valueOf(t));
					}
				} catch (IllegalArgumentException e) {
					System.out.println("该字段不是基本数据格式字段");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					System.out.println("非法访问");
					e.printStackTrace();
				}
			}
			count++;			
		}
		try {
			FileOutputStream fOut = new FileOutputStream(record);
			wbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件路径错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O错误");
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Excel表格的表头格式属性调整
	 * 
	 * @param wbook
	 * @return
	 */
	private static HSSFCellStyle getStyle(HSSFWorkbook wbook) {
		HSSFCellStyle cellStyle = wbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		HSSFFont font = wbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	/**
	 * excel表格的基本数据单元的格式
	 * 
	 * @param wbook
	 * @return
	 */
	private static HSSFCellStyle getDataStyle(HSSFWorkbook wbook) {
        HSSFCellStyle cellStyle = wbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        HSSFFont font = wbook.createFont();
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        return cellStyle;
    }
	
	/**
     * Excel表格属性调整，专用于成绩导出
     * 
     * @param wbook
     * @param type 0表示普通，1表示灰色背景（无数据），2表示红色背景（确认时异常或者不一致的数据）
     * @return
     */
    private static HSSFCellStyle getStuentScoreStyle(HSSFWorkbook wbook,int type) {
        HSSFCellStyle cellStyle = wbook.createCellStyle();
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        if(type==0){
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }else if(type==1){
            cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }else if(type==2){
            cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        cellStyle.setWrapText(true);
        return cellStyle;
    }
	

	/**
	 * 导出学生的学习成绩，特殊处理
	 * @param exportFile 指定的导出文件
	 * @param csms 成绩列表
	 * @param courseEncodings 自动生成的课程编码
	 * 
	 * @return
	 */
	public static boolean exportStudentScore(File exportFile,List<CourseSelectModel> csms,Map<Integer,CourseLearnModel> courseInfo){
	    boolean result=true;
	    //计算sheet的个数，并保存每个sheet的头信息
	    Map<String,List<Integer>> sheets=new HashMap<String, List<Integer>>();
	    Map<Integer,Boolean> existCourIds=new HashMap<Integer,Boolean>();
	    List<Integer> currentCourIds=new ArrayList<Integer>();
	    String lastGrade="undefined";
	    for(CourseSelectModel csm:csms){
	        String grade=csm.getStuGrade();
	        if(grade!=null&&!grade.equals(lastGrade)){
	            if(!lastGrade.equals("undefined")){
	                sheets.put(lastGrade, currentCourIds);
	                existCourIds.clear();
	                currentCourIds=new ArrayList<Integer>();
	            }
	            lastGrade=grade;
	        }
	        if(!existCourIds.containsKey(csm.getCourseId())){
	            currentCourIds.add(csm.getCourseId());
	            existCourIds.put(csm.getCourseId(), true);
	        }
	    }
	    if(currentCourIds.size()>0){
	        sheets.put(lastGrade, currentCourIds);
	        existCourIds.clear();
	    }
	    
	    //遍历成绩列表，创建excel表单
	    HSSFWorkbook wbook = new HSSFWorkbook();
	    int lastStuId=-1;
	    int rowCount=0;
	    lastGrade="undefined";
	    currentCourIds=null;
	    HSSFSheet wsheet=null;
	    HSSFRow currentRow=null;
	    HSSFCell currentCell=null;
	    HSSFCellStyle normalStyle=null;
	    HSSFCellStyle noDataStyle=null;
	    for(CourseSelectModel csm:csms){
	        String grade=csm.getStuGrade();
	        //判断是否应该新建表单
	        if(!grade.equals(lastGrade)){
	            lastGrade=grade;
	            if(sheets.containsKey(grade)){
	                currentCourIds=sheets.get(grade);
    	            //为新的年级创建新的表单
    	            wsheet = wbook.createSheet(grade); //以成绩表名
    	            normalStyle = getStuentScoreStyle(wbook,0);
    	            noDataStyle = getStuentScoreStyle(wbook,1);
    	            //调整sheet的宽度
                    wsheet.setColumnWidth(0,256*10);
                    wsheet.setColumnWidth(1,256*20);
                    for(int i=2;i<currentCourIds.size()+2;i++){
                        wsheet.setColumnWidth(i,256*20);                        
                    }
    	            //设置表头
    	            //第一行显示年级
    	            HSSFRow excelTitle = wsheet.createRow(0);
    	            HSSFCell titleCell = excelTitle.createCell(0);
    	            titleCell.setCellValue("年级");
    	            for(int i=1;i<currentCourIds.size()+2;i++){
    	                HSSFCell tempCell=excelTitle.createCell(i);
    	                tempCell.setCellStyle(normalStyle);
                    }
    	            wsheet.addMergedRegion(new CellRangeAddress(0,0,1,currentCourIds.size()+1));
    	            HSSFCell gradeCell=wsheet.getRow(0).getCell(1);
    	            gradeCell.setCellValue(grade);
    	            //gradeCell.setCellStyle(normalStyle);
    	            //第二三行显示学生和课程的表头
    	            excelTitle = wsheet.createRow(1);
    	            for(int i=0;i<currentCourIds.size()+2;i++){
    	                titleCell=excelTitle.createCell(i);
    	                if(i>=2){
    	                    CourseLearnModel clm=courseInfo.get(currentCourIds.get(i-2));
    	                    titleCell.setCellValue(clm.getEncoding());
    	                }
    	                titleCell.setCellStyle(normalStyle);
                    }
    	            excelTitle = wsheet.createRow(2);
                    for(int i=0;i<currentCourIds.size()+2;i++){
                        titleCell=excelTitle.createCell(i);
                        if(i>=2){
                            CourseLearnModel clm=courseInfo.get(currentCourIds.get(i-2));
                            titleCell.setCellValue(clm.getCourseName());
                        }
                        titleCell.setCellStyle(normalStyle);
                    }
                    wsheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
                    wsheet.addMergedRegion(new CellRangeAddress(1,2,1,1));
                    wsheet.getRow(1).getCell(0).setCellValue("学生姓名");
                    //wsheet.getRow(1).getCell(0).setCellStyle(cellStyle);
                    wsheet.getRow(1).getCell(1).setCellValue("学籍号");
                    //wsheet.getRow(1).getCell(1).setCellStyle(cellStyle);
                    rowCount=3;
                    lastStuId=-1;
	            }
	        }
	        //判断是否换行
	        if(csm.getStuId()!=lastStuId){
	            lastStuId=csm.getStuId();
	            currentRow=wsheet.createRow(rowCount);
	            //写入学生姓名和学籍号
	            HSSFCell firstCell=currentRow.createCell(0);
	            firstCell.setCellValue(csm.getStuName());
	            firstCell.setCellStyle(normalStyle);
	            HSSFCell secondCell=currentRow.createCell(1);
	            secondCell.setCellValue(csm.getStuNum());
	            secondCell.setCellStyle(normalStyle);
	            //预先生成该行的单元格，后面直接插入数据
	            for(int i=2;i<currentCourIds.size()+2;i++){
	                currentCell=currentRow.createCell(i);
	                currentCell.setCellStyle(noDataStyle);
                }
	            rowCount++;
	        }
	        //寻找当前数据的位置
	        for(int i=0;i<currentCourIds.size();i++){
                if(currentCourIds.get(i)==csm.getCourseId()){
                    currentCell=currentRow.getCell(i+2);
                    break;
                }
            }
	        //写入数据
	        if(currentCell!=null){
	            if(csm.getTotalScore()>=60.0){
	                currentCell.setCellValue(csm.getCourseCredit());
	            }else{
	                currentCell.setCellValue(0);
	            }
	            currentCell.setCellStyle(normalStyle);
	        }
        }
        try {
            FileOutputStream fOut = new FileOutputStream(exportFile);
            wbook.write(fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误");
            e.printStackTrace();
            result=false;
        } catch (IOException e) {
            System.out.println("I/O错误");
            e.printStackTrace();
            result=false;
        }
	    return result;
	}
	
	/**
	 * 比较两个excel文件，根据是否一致来判断确认结果是否无误
	 * 
	 * @param basePath     基本文件路径，用于创建临时结果文件
	 * @param location     结果文件相对于basePaht的文件夹路径
	 * @param sourceFile   原始文件
	 * @param confirmFile  确认文件
	 * @param successConfirm 保存确认一致的选课记录
	 * @return   返回 结果文件，null表示文件错误或者文件不匹配，不存在的文件表示确认完全通过
	 */
	public static File confirmExportInfo(String basePath
	        ,String location,File sourceFile,File confirmFile
	        ,List<CourseSelectModel> successConfirm){
	    //构造结果文件
	    String resultFileName=(int)(Math.random()*1000)+"5"
	            +System.currentTimeMillis()+"5"
	            +(int)(Math.random()*10000)+"5"+".xls";
	    File result=new File(basePath+"/"+location+resultFileName);
	    //开始进行确认分析
        try {
            //分别获取excel文件对象
            POIFSFileSystem fsExport=new POIFSFileSystem(
                    new FileInputStream(sourceFile));
            HSSFWorkbook wbExport = new HSSFWorkbook(fsExport);
            POIFSFileSystem fsConfirm=new POIFSFileSystem(
                    new FileInputStream(confirmFile));
            HSSFWorkbook wbConfirm = new HSSFWorkbook(fsConfirm);
            HSSFCellStyle errorStyle=getStuentScoreStyle(wbConfirm, 2);
            //开始比较
            Boolean same=true;
            int sheetNum=wbConfirm.getNumberOfSheets();
            if(wbExport.getNumberOfSheets()!=sheetNum){
                System.out.println("确认文件与原文件sheet数不一致");
                return null;
            }
          //缓存课程信息
            Map<Integer,String> courseNames=new HashMap<Integer, String>();
            for(int i=0;i<sheetNum;i++){
                //判断表单名和行数是否一致
                HSSFSheet wsheetExport=wbExport.getSheetAt(i); 
                HSSFSheet wsheetConfirm=wbConfirm.getSheet(
                        wsheetExport.getSheetName());
                if(wsheetConfirm==null){
                    System.out.println("确认文件没有原文件的sheet："+wsheetExport.getSheetName());
                    return null;
                }
                int rowNum=wsheetExport.getLastRowNum();
                if(rowNum!=wsheetConfirm.getLastRowNum()){
                    System.out.println("sheet："+wsheetExport.getSheetName()+"中的行数不一致！");
                    return null;
                }
                //逐行比较
                if(rowNum>=0){
                    String exportValue=null; 
                    String confirmValue=null;
                    HSSFRow exportRow=null;
                    HSSFRow confirmRow=null;
                    courseNames.clear();
                    //判断表头和用户信息是否一致
                    int contentColumnNum=0;
                    for(int j=0;j<3;j++){
                        exportRow=wsheetExport.getRow(j);
                        confirmRow=wsheetConfirm.getRow(j);
                        int columnNum=exportRow.getPhysicalNumberOfCells();
                        if(columnNum!=confirmRow.getPhysicalNumberOfCells()){
                            System.out.println("表头的列数不一致，sheet："+wsheetExport.getSheetName());
                            return null;
                        }
                        if(j==2){
                            contentColumnNum=columnNum;
                        }
                        for(int k=0;k<columnNum;k++){
                            exportValue=exportRow.getCell(k).getStringCellValue();
                            confirmValue=confirmRow.getCell(k).getStringCellValue();
                            if(!exportValue.equals(confirmValue)){
                                System.out.println("表头内容不一致，sheet："+wsheetExport.getSheetName());
                                return null;
                            }
                            if(j==2&&k>1){
                                courseNames.put(k, confirmValue);
                            }
                        }
                    }
                    for(int j=3;j<=rowNum;j++){
                        exportRow=wsheetExport.getRow(j);
                        confirmRow=wsheetConfirm.getRow(j);
                        for(int k=0;k<2;k++){
                            exportValue=exportRow.getCell(k).getStringCellValue();
                            confirmValue=confirmRow.getCell(k).getStringCellValue();
                            if(!exportValue.equals(confirmValue)){
                                System.out.println("学生信息不一致！行数："+k+"，sheet："+wsheetExport.getSheetName());
                                return null;
                            }
                        }
                    }
                    //判断学分是否一致
                    double exportCredit=0,confirmCredit=0;
                    String stuId=null;
                    for(int j=3;j<=rowNum;j++){
                        exportRow=wsheetExport.getRow(j);
                        confirmRow=wsheetConfirm.getRow(j);
                        stuId=confirmRow.getCell(1).getStringCellValue();
                        for(int k=2;k<contentColumnNum;k++){
                            exportCredit=exportRow.getCell(k).getNumericCellValue();
                            confirmCredit=confirmRow.getCell(k).getNumericCellValue();
                            int exportType=exportRow.getCell(k).getCellType();
                            int confirmType=confirmRow.getCell(k).getCellType();
                            if(exportCredit!=confirmCredit){
                                if(same){
                                    same=false;                                    
                                }                               
                                String confirmVaule=""+confirmCredit;
                                if(Math.round(confirmCredit)==confirmCredit){
                                    confirmVaule=""+Math.round(confirmCredit);
                                }
                                String newValue="导出时无该项成绩";
                                if(exportType!=Cell.CELL_TYPE_BLANK){
                                    String exportVaule=""+exportCredit;
                                    if(Math.round(exportCredit)==exportCredit){
                                        exportVaule=""+Math.round(exportCredit);
                                    }
                                    newValue="与导出的值("+exportVaule+")不一致";
                                }
                                HSSFCell confirmCell=confirmRow.getCell(k);
                                if(confirmType!=Cell.CELL_TYPE_BLANK){
                                    newValue=confirmVaule+"("+newValue+")";
                                }
                                confirmCell.setCellValue(newValue);
                                confirmCell.setCellStyle(errorStyle);
                            }else if(confirmCredit>0){
                                CourseSelectModel csm=new CourseSelectModel();
                                csm.setStuNum(stuId);
                                csm.setCourseName(courseNames.get(k));
                                successConfirm.add(csm);
                            }
                        }
                    }
                }                
            }
            //如果存在不一致的地方，则产生结果文件
            if(result.exists()){
                result.delete();
            }
            if(!same){
                result.createNewFile();
                FileOutputStream fOut = new FileOutputStream(result);
                wbConfirm.write(fOut);
                fOut.flush();
                fOut.close();
            }   
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }	    
	    return result;
	}
	
	/**
	 * 导出课程选修情况（特殊处理）
	 * 
	 * @param file    导出时产生的临时文件
	 * @param records 导出的记录
	 * @return
	 */
	public static boolean exportCourseLearningState(File file,List<CourseLearnModel> records){
	    boolean result=true;
	    //异常处理
	    if(file==null || !file.exists() || records==null 
	            || records.isEmpty()){
	        return false;
	    }
	    
	    //构造excel
	    HSSFWorkbook wbook = new HSSFWorkbook();
	    //样式
	    HSSFCellStyle cellStyle = getDataStyle(wbook);
	    int start=0,end=0;
	    //一门课程一个表单
	    while(start<records.size()){
	        String courName=records.get(start).getCourseName();
	        int courId=records.get(start).getCourseId();
	        HSSFSheet wsheet = wbook.createSheet(courName); // 表名
	        //设置Excel表头            
            ExportAsExcel.createExcelStructureForCourseLearning(wsheet, courName, cellStyle);
            //数据行
            int curRow=3;
            HSSFRow dataRow=null;
            HSSFCell dataCell=null;
            while(start<records.size()){
                CourseLearnModel temp=records.get(start);
                if(temp.getCourseId()!=courId){
                    break;
                }
                //相同部门的合并在一起
                String curDepart=records.get(start).getDepaName();
                end=start+1;
                while(end<records.size()){
                    if(records.get(end).getCourseId()!=courId
                            ||!records.get(end).getDepaName().equals(curDepart)){
                        break;
                    }
                    end++;
                }
                if(end-start>1){
                    wsheet.addMergedRegion(new CellRangeAddress(curRow,curRow+(end-start-1),0,0));
                }
                dataRow = wsheet.createRow(curRow++);
                dataCell = dataRow.createCell(0);
                dataCell.setCellValue(curDepart);
                dataCell.setCellStyle(cellStyle);
                //处理部门下的不同年级
                ExportAsExcel.createDataCellForCourseLearning(dataRow, cellStyle, temp);
                start++;
                while(start<end){
                    dataRow = wsheet.createRow(curRow++);
                    ExportAsExcel.createDataCellForCourseLearning(
                            dataRow, cellStyle, records.get(start));
                    start++;
                }                
            }
            wsheet.autoSizeColumn(0);//按内容设置单元格大小
	    }        
        
	    //将excel对象写入文件
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            wbook.write(fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误");
            e.printStackTrace();
            result=false;
        } catch (IOException e) {
            System.out.println("I/O错误");
            e.printStackTrace();
            result=false;
        }
	    return result;
	}
	
	/**
	 * 设置课程选修导出的表头
	 * @param wsheet    excel里面的一个sheet
	 * @param courName  课程名称
	 * @param cellStyle 基本样式
	 */
	public static void createExcelStructureForCourseLearning(
	        HSSFSheet wsheet,String courName,HSSFCellStyle cellStyle){
        //标题行
        wsheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        HSSFRow excelTitle = wsheet.createRow(0);
        excelTitle.setHeightInPoints(22);
        HSSFCell titleCell = excelTitle.createCell(0);
        titleCell.setCellValue(courName);
        titleCell.setCellStyle(cellStyle);
        //列名
        wsheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
        wsheet.addMergedRegion(new CellRangeAddress(1,2,1,1));
        wsheet.addMergedRegion(new CellRangeAddress(1,2,2,2));
        wsheet.addMergedRegion(new CellRangeAddress(1,1,3,6));
        HSSFRow colRow = wsheet.createRow(1);
        HSSFCell colCell = colRow.createCell(0);
        colCell.setCellValue("学校名称");
        colCell.setCellStyle(cellStyle);
        colCell = colRow.createCell(1);
        colCell.setCellValue("年级");
        colCell.setCellStyle(cellStyle);
        colCell = colRow.createCell(2);
        colCell.setCellValue("注册人数");
        colCell.setCellStyle(cellStyle);
        colCell = colRow.createCell(3);
        colCell.setCellValue(courName);
        colCell.setCellStyle(cellStyle);
        colRow = wsheet.createRow(2);
        colCell = colRow.createCell(3);
        colCell.setCellValue("选课人数");
        colCell.setCellStyle(cellStyle);
        colCell = colRow.createCell(4);
        colCell.setCellValue("在线时间");
        colCell.setCellStyle(cellStyle);
        colCell = colRow.createCell(5);
        colCell.setCellValue("学习次数");
        colCell.setCellStyle(cellStyle);
        colCell = colRow.createCell(6);
        colCell.setCellValue("交互次数");
        colCell.setCellStyle(cellStyle);
	}
	
	/**
	 * 课程选修导出时，设置具体的导出值
	 * 
	 * @param dataRow    数据所在行
	 * @param cellStyle  单元格样式
	 * @param clm        数据对象
	 */
	public static void createDataCellForCourseLearning(HSSFRow dataRow,
	        HSSFCellStyle cellStyle,CourseLearnModel clm){
	    //年级
        HSSFCell dataCell=dataRow.createCell(1);
        dataCell.setCellValue(clm.getGradeName());
        dataCell.setCellStyle(cellStyle);
        //注册人数
        dataCell=dataRow.createCell(2);
        dataCell.setCellValue(clm.getStuNum());
        dataCell.setCellStyle(cellStyle);
        //选课人数
        dataCell=dataRow.createCell(3);
        dataCell.setCellValue(clm.getSelectNum());
        dataCell.setCellStyle(cellStyle);
        //在线时间
        dataCell=dataRow.createCell(4);
        dataCell.setCellValue(clm.getLearnTime());
        dataCell.setCellStyle(cellStyle);
        //学习次数
        dataCell=dataRow.createCell(5);
        dataCell.setCellValue(clm.getLearnNum());
        dataCell.setCellStyle(cellStyle);
        //交互次数
        dataCell=dataRow.createCell(6);
        dataCell.setCellValue(clm.getBbsPostNum());
        dataCell.setCellStyle(cellStyle);
	}
	
	public static void main(String[] args) {
		List<Department> list = new ArrayList<Department>();
		Department d = new Department();
		d.setDepaAbbreviation("12Pery");
		d.setDepaCode("32");
		d.setDepaId(1);
		d.setDepaType(2);
		for (long i =0; i<200000; i++)
			list.add(d);
		File file = new File("/tmp/text.xls");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		} else {
			if (file.exists()) {
				file.delete();
				file = new File(new File("/tmp"), "text.xls");
			}
		}
		String tableName = "结果";
		String className = "com.swust.kelab.domain.Department";
		Map<String, String> properName = new HashMap<String, String>();
		properName.put("depaId", "机构id");
		properName.put("depaName", "机构名称");
		properName.put("depaAbbreviation", "机构简称");
		properName.put("depaCode", "机构代码");
		properName.put("parentDepaName", "上级机构");
		properName.put("depaType", "机构类型");
		ExportAsExcel.exportExcel(className, properName, tableName, file, list);
	}
	
}
