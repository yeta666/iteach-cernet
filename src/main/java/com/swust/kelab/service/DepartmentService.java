package com.swust.kelab.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Department;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.DepartmentModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.ExportAsExcel;

@Service(value = "departmentService")
public class DepartmentService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReSelectCourseDAO reSelectCourseDAO;
    
    private DepartmentDao departmentDao;

    @Autowired
    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Transactional
    public List<Department> findDepartmentsByType(int depa_type) {
        return departmentDao.queryDepatmentByType(depa_type);
    }
    @Transactional
    public List<Department> findParentDepartmentsByType(int depa_type) {
        return departmentDao.queryParentDepatmentByType(depa_type);
    }

    /**
     * 通过id获取部门信息
     * 
     * @param departId
     *            部门id
     * @return 部门对象
     * @see Department
     */
    public Department queryDepartmentById(int departId) {
        Department depart = null;
        try {
            depart = departmentDao.queryDepartmentById(departId);
        } catch (Exception e) {
            logger.error("queryDepartType error!\n" + e.getLocalizedMessage());
        }
        return depart;
    }

    /**
     * 根据上级机构和关键词查询
     * 
     * @param parentId
     *            上级机构id
     * @param keyword
     *            模糊查询关键词
     * @return 查询机构列表
     */
    public List<Department> queryDepartmentByParent(int parentId, String keyword) {
        List<Department> departmentList = null;
        try {
            if (keyword != null && !keyword.equals("")) {
                Map<String, Object> parameter = new HashMap<String, Object>();
                parameter.put("parentId", parentId);
                parameter.put("keyword", keyword);
                departmentList = departmentDao.queryDepartmentByParent(parameter);
            } else {
                departmentList = departmentDao.queryDepartmentByParent(parentId);
            }
        } catch (Exception e) {
            logger.error("select failed!\n" + e.getLocalizedMessage());
        }
        return departmentList;
    }

    /**
     * 根据机构id更新机构信息
     * 
     * @param department
     */
    public String updateOneDepartment(Department department) {
        Department oriDepart=null;
        try {
            oriDepart=departmentDao.queryDepartmentById(
                    department.getDepaId());
        } catch (Exception e) {
            logger.error("queryDepartmentById failed!\n" + e.getLocalizedMessage());
        }
        if(oriDepart==null){
            return "fail";
        }
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("depaName", department.getDepaName());
        parameter.put("depaAbbreviation", department.getDepaAbbreviation());
        parameter.put("depaCode", department.getDepaCode());
        parameter.put("depaParentId", department.getDepaParentId());
        parameter.put("depaType", department.getDepaType());
        parameter.put("depaId", department.getDepaId());
        int result = -1;
        try {
            result = departmentDao.updateOneDepartment(parameter);
        } catch (Exception e) {
            logger.error("update failed!\n" + e.getLocalizedMessage());
            return "fail";
        }
        if (result > -1) {
            //更新选课表中的机构名称
            String newDepaName=department.getDepaName();
            if(newDepaName!=null && !newDepaName.equals(
                    oriDepart.getDepaName())){
                Map query=new HashMap();
                query.put("depaId", department.getDepaId());
                query.put("depaName", newDepaName);
                try {
                    reSelectCourseDAO.updateUserDepaInfo(query);
                } catch (Exception e) {
                    logger.error("updateUserDepaInfo failed!\n" + e.getLocalizedMessage());
                }
            }
            return "success";
        } else {
            return "fail";
        }
    }

    /**
     * 根据机构id批量删除机构
     * 
     * @param departmentIds
     *            机构id，id之间用逗号分隔
     */
    public String deleteBatchDepartment(String departmentIds) {
        if (departmentIds != null && !departmentIds.equals("") && departmentIds.matches("\\d+(,\\d+)*")) {
            int result = -1;
            try {
                result = departmentDao.deleteBatchDepartment(Arrays.asList(departmentIds.split(",")));
            } catch (Exception e) {
                logger.error("delete failed!\n" + e.getLocalizedMessage());
                return "fail";
            }
            if (result > -1) {
                return "success";
            } else {
                return "fail";
            }
        }
        return "fail";
    }

    /**
     * 通过部门查询部门
     * 
     * @param depart
     * @return
     */
    public List<Department> findDepartmentByDepart(Department depart) {
        return departmentDao.findDepartmentsByDepar(depart);
    }

    /**
     * 添加新的部门
     * 
     * @param depart
     * @return
     */
    public Integer insertDepartment(Department depart) {
        return departmentDao.insertDepartment(depart);
    }

    /**
     * 机构筛选查询
     * 
     * @param query
     *            查询对象，包括 搜索字段,searchType 为1：按名称搜索，2：部门简称 3：部门代码 和搜索关键词，以及分页信息
     * @param screenType
     *            筛选条件 0：全部，1,：市，2：区，3：学校
     * @param parentId 上级机构id
     * @param departId 机构id
     * @return 查询结果列表
     */
    public QueryData queryByMutiTerm(CommonQuery query, int screenType,int parentId,int departId) {
        if (query.getSearchWord() == null || query.getSearchWord().equals("")) {
            query.setSearchType(0); // 关键词为空则匹配所有的
        }
        ListQuery myQuery = query.format();
        myQuery.put("depaType", screenType);
        myQuery.put("parentId", parentId);
        myQuery.put("departId", departId);
        List<DepartmentModel> list = new ArrayList<DepartmentModel>();
        int allCount = 0;
        int totalPage = 0;
        QueryData result=new QueryData();
        allCount= departmentDao.countByMutiTerm(myQuery);
        result.setTotalCount(allCount);
        if(query.getRecordPerPage()<=0){
            query.setRecordPerPage(20);
        }
        totalPage=QueryData.computeTotalPage(allCount, query.getRecordPerPage());
        result.setTotalPage(totalPage);
        if(allCount>0){
            if(query.getPageArray()==null){
                query.setPageArray(new int[]{1,2});
            }
            int startIndex=(query.getPageArray()[0]-1)*query.getRecordPerPage();
            int fetchSize=query.getPageArray().length*query.getRecordPerPage();
            //    System.out.println("第"+startIndex+"页，每页"+fetchSize+"条");
            myQuery.fill("startIndex", startIndex);
            myQuery.fill("maxCount", fetchSize);
            try{
                list = departmentDao.queryByMutiTerm(myQuery);
            } catch (Exception e) {
                logger.error("select by muti failed!\n" + e.getLocalizedMessage());
            }
            if(list==null||list.size()<=0){
                return result;
            }
            //特殊处理：设置机构的上级机构名和类型名
            Map<Integer,String> departNames=new HashMap<Integer, String>();
            for (DepartmentModel dm : list) {
                departNames.put(dm.getDepaId(), dm.getDepaName());
                switch (dm.getDepaType()) {
                case 1:dm.setDepaTypeName("市");                
                break;
                case 2:dm.setDepaTypeName("区");                
                break;
                case 3:dm.setDepaTypeName("学校");                
                break;
                default:
                    break;
                }
            }
            for (DepartmentModel dm : list) {
                if(dm.getDepaParentId()==null){
                    dm.setParentDepaName("无");
                    continue;
                }
                int pId=dm.getDepaParentId();
                if(departNames.containsKey(pId)){
                    dm.setParentDepaName(departNames.get(pId));
                }else{
                    Department temp=departmentDao.queryDepartmentById(pId);
                    dm.setParentDepaName(temp.getDepaName());
                    departNames.put(temp.getDepaId(), temp.getDepaName());
                }
            }
            departNames.clear();
            departNames=null;
            //	System.out.println("count:"+list.size());
            List<PageData> pageDataList = Lists.newArrayList();  
            return initialQueryData(result,pageDataList,query,list,totalPage);
        }
        return result;
    }
    /**
     * 按照分页封装数据
     * @param result 返回封装结果
     * @param pageDataList 分页数据列表
     * @param query 查询条件
     * @param list 数据列表
     * @param totalPage 总页码
     * @return
     */
    private QueryData initialQueryData(QueryData result,List<PageData> pageDataList
    		,CommonQuery query, List<DepartmentModel> list ,int totalPage){
        for (int k = 0; k < query.getPageArray().length; k++) {
            int page = query.getPageArray()[k];
            if (page <= 0 || page > totalPage) {
                continue;
            }
          int  startIndex=k*query.getRecordPerPage();
            int endIndex=startIndex+query.getRecordPerPage();
            if(startIndex>=list.size()){
                continue;
            }
            if(endIndex>list.size()){
                endIndex=list.size();
            }
            List<DepartmentModel> pageDatas=list.subList(startIndex, endIndex);
            pageDataList.add(new PageData(page, pageDatas));
        }
        result.setPageData(pageDataList);
        return result;
    }
    /**
     * 机构筛选查询 并写入excel文件以供下载
     * @param query
     *            查询对象，包括 搜索字段,searchType 为1：按名称搜索，2：部门简称 3：部门代码 和搜索关键词，以及分页信息
     * @param screenType
     *            筛选条件 0：全部，1,：市，2：区，3：学校
     * @return 查询 写入的文件路径
     */
    public String queryAndGetExcel( String basePath,CommonQuery query, int screenType){
        if (query.getSearchWord() == null || query.getSearchWord().equals("")) {
            query.setSearchType(0); // 关键词为空则匹配所有的
        }
        ListQuery myQuery = query.format();
        myQuery.put("depaType", screenType);
        List<DepartmentModel> list = departmentDao.queryByMutiTerm(myQuery);
        Map<Integer,String> departNames=new HashMap<Integer, String>();
        for (DepartmentModel dm : list) {
            departNames.put(dm.getDepaId(), dm.getDepaName());
            switch (dm.getDepaType()) {
            case 1:dm.setDepaTypeName("市");                
            break;
            case 2:dm.setDepaTypeName("区");                
            break;
            case 3:dm.setDepaTypeName("学校");                
            break;
            default:
                break;
            }
        }
        for (DepartmentModel dm : list) {
            if(dm.getDepaParentId()==null){
                dm.setParentDepaName("无");
                continue;
            }
            int parentId=dm.getDepaParentId();
            if(departNames.containsKey(parentId)){
                dm.setParentDepaName(departNames.get(parentId));
            }else{
                Department temp=departmentDao.queryDepartmentById(parentId);
                dm.setParentDepaName(temp.getDepaName());
                departNames.put(temp.getDepaId(), temp.getDepaName());
            }
        }
        departNames.clear();
        departNames=null;
        String path = "upload/temp/组织机构表.xls";
        File file = new File(basePath+"/"+path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("创建文件失败!\n"+e.getLocalizedMessage());
        }
        Map<String, String> properName=new HashMap<String, String>();
        properName.put("depaId", "机构id");
        properName.put("depaName", "机构名称");
        properName.put("depaAbbreviation", "机构简称");
        properName.put("depaCode", "机构代码");
        properName.put("parentDepaName", "上级机构");
        properName.put("depaTypeName", "机构类型");
        if(ExportAsExcel.exportExcel("com.swust.kelab.model.DepartmentModel",properName, "组织机构", file, list)){
            return path;
        }
        return null;
    } 
    
    /**
     * 批量插入机构
     * 
     * @param departs 机构列表
     * @return        操作结果
     */
    public String batchInsertDeparts(List<Department> departs){
        String result="success";
        int insertNum=0;
        try {
            insertNum=departmentDao.batchInsertDeparts(departs);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("batchInsertDeparts\n"+e.getLocalizedMessage());
        }
        if(insertNum<departs.size()){
            result="fail";
        }
        return result;
    }
    
    /**
     * 解析导入的excel文件，生成要导入的机构列表
     * 
     * @param sheet excel文件中的工作表
     * @return 机构列表
     */
    public List<Department> parseExcelToDeparts(HSSFSheet sheet){
        List<Department> result=new ArrayList<Department>();
        try {
            for(int i=1;i<=sheet.getLastRowNum();i++){
                Department depart=new Department();
                HSSFRow currentRow=sheet.getRow(i);
                if(currentRow==null||currentRow.getCell(0)==null){
                    break;
                }
                String depaName=currentRow.getCell(1)
                        .getStringCellValue();
                depart.setDepaName(depaName);
                String depaAbbreviation=currentRow.getCell(2)
                        .getStringCellValue();
                depart.setDepaAbbreviation(depaAbbreviation);
                HSSFCell cell3=currentRow.getCell(3);
                if(cell3.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    depart.setDepaCode(String.valueOf(
                            (int)cell3.getNumericCellValue()));
                }else{
                    depart.setDepaCode(cell3.getStringCellValue());
                }
                HSSFCell cell4=currentRow.getCell(4);
                if(cell4.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    double paId=cell4.getNumericCellValue();
                    if(paId==0.0){
                        depart.setDepaParentId(null);
                    }else{
                        if(paId-(int)paId!=0){
                            return null;
                        }
                        depart.setDepaParentId((int)paId);
                    }
                }else{
                    if(cell4.getStringCellValue()==null||
                            cell4.getStringCellValue()==""){
                        depart.setDepaParentId(null);
                    }else{
                        depart.setDepaParentId(Integer.parseInt(
                                cell4.getStringCellValue()));
                    }
                }
                double type=currentRow.getCell(5).getNumericCellValue();
                if(type!=1.0&&type!=2.0&&type!=3.0){
                    return null;
                }
                int depaType=(int)type;
                depart.setDepaType(depaType);
                result.add(depart);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("解析Excel工作表失败！\n",e);
            result=null;
        }
        return result;
    }

    /**
	 * 通过部门id和部门类型查询部门列表<br>
	 * @param depaType
	 * @param depaIds
	 * @return
	 * @throws Exception
	 */
	public List<Department> viewAllDepaListBelongsToParentDepar(
				Integer depaType,String depaParentIds) {
		try {
			switch(depaType) {
			case 1 :		//	市级部门
				return departmentDao.viewAllDepaListBelongsToParentDepar(null, depaType,null);
			case 2 :		//	区级部门
			case 3 :		//	市级部门
				List<Department> allList = new ArrayList<Department>();
				if(depaParentIds != null) {
					String[] didArray = depaParentIds.split(",");
					if(didArray.length > 0) {
						for(String didString : didArray) {
							allList.addAll(
									departmentDao.viewAllDepaListBelongsToParentDepar(
											Integer.valueOf(didString), depaType,null));
						}
						return allList;
					} else
						return null;
				} else {
					allList.addAll(
							departmentDao.viewAllDepaListBelongsToParentDepar(null, depaType,null));
					return allList;
				}
			default :
				break;
			}
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("viewAllDepaListBelongsToParentDepar error!");
			return null;
		}
	}
}


