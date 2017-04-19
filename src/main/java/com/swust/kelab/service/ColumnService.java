package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.Action;
import com.swust.kelab.domain.FunctionAction;
import com.swust.kelab.domain.ReAuthority;
import com.swust.kelab.model.FunctionModel;
import com.swust.kelab.repos.AuthorityDAO;
import com.swust.kelab.repos.RoleDao;
import com.swust.kelab.utils.CompressUtil;
import com.swust.kelab.utils.CookieUtil;

/**
 * 栏目service
 * 
 * @author yangzq
 * 
 */

@Service()
public class ColumnService {

    @Resource
    private AuthorityDAO authorityDao;
    @Resource
    private RoleDao roleDao;

    /**
     * 仅获取栏目
     * 
     * @author yangzq
     * @param response
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryAllColumns(Integer userId, Integer firstCol, HttpServletResponse response,
            HttpServletRequest request) throws Exception {
        // 加载已近存放的cookie值
        Cookie[] cookies = request.getCookies();
        // 用户已拥有的操作权限
        String authValue = CookieUtil.findCookieValue(cookies, "authCookie");
        // 系统需要拦截的操作权限
        String allActVal = CookieUtil.findCookieValue(cookies, "allActions");
        boolean authFlag = false;// 操作标识，若已经拥有值，则以下处理绕过，不需要重复设置cookie
        boolean allActFlag = false;// 同上
        if (authValue == null || authValue.equals("")) {
            authFlag = true;
        }
        if (allActVal == null || allActVal.equals("")) {
            allActFlag = true;
        }
        // 获取用户的所有角色id
        List<Integer> roleIds = roleDao.selectRoleIdsByUserId(userId);
        // 根据权限查询所有权限对应的authoritys
        List<ReAuthority> authoritys = authorityDao.selectActions(roleIds);
        if (authoritys == null) {
            return null;
        }
        // 合并相同元素，重组actionId
        mergeList(authoritys);
        List<String> reauIds = new ArrayList<String>();
        List<Integer> fumoIds = new ArrayList<Integer>();
        for (ReAuthority reau : authoritys) {
            fumoIds.add(reau.getReauFumoId());
            for (String actionId : reau.getReauActions().split(",")) {
                reauIds.add(reau.getReauFumoId() + "&" + actionId);
            }
        }
        HashSet h = new HashSet(reauIds);
        reauIds.clear();
        reauIds.addAll(h);
        // 根据筛选过的actionId查询所有模块 ---历史记录 by yangzq actionIds.toString().replaceAll(" ", "").replace("[", "").replace("]",
        // "").split(",")
        // 从缓存读取所有栏目进行筛选
        List<FunctionModel> functions = authorityDao.queryCompetence();
        if (firstCol == 0 || firstCol == null) {
            firstCol = 1;// 若为空，则显示个人中心
        }
        // 封装一级栏目
        List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
        int count = 0;// 计数值，对应角色筛选的权限list里的对象
        // 匹配权限使用的字符串
        String authorityStr = "";
        String authBlackList = "";
        for (FunctionModel fun : functions) {
            boolean subflag = false;
            if (fun.getFunctions() != null) {
                // 子模块封装subList
                List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
                for (FunctionModel subfunc : fun.getFunctions()) {
                    // 每个用户的拦截权限仅处理一次，到cookie里
                    if (allActFlag) {
                        for (Action action : subfunc.getActions()) {
                            if (action.getActiType() == 2 || action.getActiType().equals(2)) {
                                // 拼装权限黑名单
                                if (authBlackList == "" || authBlackList.equals("")) {
                                    authBlackList += subfunc.getFumoId() + action.getActiUrl();
                                } else {
                                    authBlackList += "," + subfunc.getFumoId() + action.getActiUrl();
                                }
                            }
                        }
                    }
                    // 角色包含二级模块权限则添加
                    if (fumoIds.contains(subfunc.getFumoId())) {
                        subflag = true;// 标识是否有此一级栏目
                        // 用户cookie为空才加载
                        if (authFlag) {
                            for (Action action : subfunc.getActions()) {
                                if ((action.getActiType() == 2 || action.getActiType().equals(2))
                                        && reauIds.contains(subfunc.getFumoId() + "&" + action.getActiId().toString())) {
                                    // 拼装用户拥有的权限
                                    if (authorityStr == "" || authorityStr.equals("")) {
                                        authorityStr += subfunc.getFumoId() + action.getActiUrl();
                                    } else {
                                        authorityStr += "," + subfunc.getFumoId() + action.getActiUrl();
                                    }
                                }
                            }
                        }
                        // 加载二级栏目,有传入值则匹配，无传入值，则默认权限第一栏目
                        //if (fun.getFumoId() == firstCol) {
                            // 子模块功能封装为subcols
                            Map<String, Object> subcolmap = new HashMap<String, Object>();
                            subcolmap.put("colid", subfunc.getFumoId());// 子模块id
                            subcolmap.put("colname", subfunc.getFumoName());// 子模块名称
                            subcolmap.put("colurl", subfunc.getFumoUrl());// 子模块URL
                            subcolmap.put("colicon", subfunc.getFumoIcon());// 子模块icon
                            // 子模块功能封装list
                            List<Map<String, Object>> sub = new ArrayList<Map<String, Object>>();
                            // 遍历子模块功能
                            for (Action action : subfunc.getActions()) {
                                // 若查询结果得到的action符合权限策略，则存放到结果中
                                if (authoritys.get(count).getReauActions().indexOf(action.getActiId().toString()) != -1) {
                                    Map<String, Object> subCol = new HashMap<String, Object>();
                                    subCol.put("colid", subfunc.getFumoId() + "+" + action.getActiId());// 子模块对应功能id
                                    subCol.put("colname", action.getActiName());// 子模块对应功能名称
                                    subCol.put("colurl", action.getActiUrl());// 子模快对应功能url
                                    subCol.put("colType", action.getActiType());
                                    sub.add(subCol);
                                }
                            }
                            count++;// 二级栏目遍历一次，循环下一个功能
                            // 子模块处理好封装到subcols
                            subcolmap.put("subacs", sub);
                            subList.add(subcolmap);
                        //}
                    }
                }
                if (subflag) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("colid", fun.getFumoId());
                    map.put("colname", fun.getFumoName());
                    for (FunctionModel subFunc : fun.getFunctions()) {
                        if (fumoIds.contains(subFunc.getFumoId())) {
                            map.put("colurl", subFunc.getFumoUrl() + "?firstCol=" + fun.getFumoId() + "&secondCol="
                                    + subFunc.getFumoId());
                            break;
                        }
                    }
                    if (map.get("colurl") == null) {
                        map.put("colurl", fun.getFumoUrl());
                    }
                    map.put("colicon", fun.getFumoIcon());
                    // 将子模块放到父模块里
                    map.put("subcols", subList);
                    cols.add(map);
                }
            }
        }
        if (authFlag) {
            String authstr = CookieUtil.authorityCookieVal(authorityStr, request);
            Cookie cookie = new Cookie("authCookie", CompressUtil.compressData(authstr));
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        if (allActFlag) {
            String authstr = CookieUtil.authorityCookieVal(authBlackList, request);
            Cookie cookie = new Cookie("allActions", CompressUtil.compressData(authstr));
            cookie.setPath("/");
            cookie.setMaxAge(100000);
            response.addCookie(cookie);
        }
        return cols;
    }

    /**
     * 合并相同fumoId中的ActionId
     * 
     * @author yangzq
     * @param list
     */
    private static void mergeList(List<ReAuthority> list) {
        // 去掉重复,并保持list的顺序
        LinkedHashMap<Integer, ReAuthority> map = new LinkedHashMap<Integer, ReAuthority>();
        for (ReAuthority bean : list) {
            if (map.containsKey(bean.getReauFumoId())) {
                bean.setReauActions(map.get(bean.getReauFumoId()).getReauActions() + "," + bean.getReauActions());
            }
            map.put(bean.getReauFumoId(), bean);
        }
        list.clear();
        list.addAll(map.values());
    }

    /**
     * 查找一级栏目
     * 
     * @author yangzq
     * @return
     */
    public List<FunctionModel> firLevelCol() {
        return authorityDao.firLevelCol();
    }

    /**
     * 查找二级栏目
     * 
     * @author yangzq
     * @param firId
     * @return
     */
    public List<FunctionModel> secLevelCol(int firId) {
        return authorityDao.secLevelCol(firId);
    }

    /**
     * 删除栏目
     * 
     * @author yangzq
     * @param data
     */
    @Transactional
    public void delCols(String[] data) {
        // TODO Auto-generated method stub
        for (String d : data) {
            String[] delCols = d.replaceAll("colType=|firstCol=|secondCol=|colId=", "").split("&");
            if (delCols[0].equals("1")) {
                Map<String, Object> del = new HashMap<String, Object>();
                del.put("fumoId", Integer.parseInt(delCols[1]));
                authorityDao.deleteFunctionAction(del);
                List<FunctionModel> firModels = authorityDao.secLevelCol(Integer.parseInt(delCols[1]));
                for (FunctionModel func : firModels) {
                    del.put("fumoId", func.getFumoId());
                    List<String> actionids = authorityDao.queryFuncActionsByFumoIds(func.getFumoId().toString());
                    authorityDao.deleteFunctionAction(del);
                    authorityDao.deleteFunctionModel(del);
                    authorityDao.deleteAction(actionids);
                }
                del.put("fumoId", Integer.parseInt(delCols[1]));
                authorityDao.deleteFunctionModel(del);
            } else if (delCols[0].equals("2")) {
                Map<String, Object> del = new HashMap<String, Object>();
                del.put("fumoId", Integer.parseInt(delCols[2]));
                List<String> actionids = authorityDao.queryFuncActionsByFumoIds(delCols[2]);
                authorityDao.deleteFunctionAction(del);
                authorityDao.deleteFunctionModel(del);
                authorityDao.deleteAction(actionids);
            } else if (delCols[0].equals("3")) {
                Map<String, Object> del = new HashMap<String, Object>();
                del.put("fumoId", Integer.parseInt(delCols[2]));
                del.put("actId", Integer.parseInt(delCols[3]));
                List<String> ids = authorityDao.queryFuncActionFumoIds(delCols[3]);
                boolean flag = false;
                for (String id : ids) {
                    if (id != delCols[2] && !id.equals(delCols[2])) {
                        flag = true;
                    }
                }
                authorityDao.deleteFunctionAction(del);
                if (!flag) {
                    List<String> actionIds = new ArrayList<String>();
                    actionIds.add(delCols[3]);
                    authorityDao.deleteAction(actionIds);
                }
            }
        }
    }

    /**
     * 栏目操作新增
     * 
     * @author yangzq
     * @param type
     * @param parentid
     * @param colname
     * @param colurl
     * @param colorder
     * @return
     */
    @Transactional
    public void saveNewCol(int type, int parentid, String colname, String colurl, String colicon, int colorder)
            throws Exception {
        if (type == 1) {
            FunctionModel func = new FunctionModel();
            func.setFumoName(colname);
            func.setFumoOrdinal(colorder);
            func.setFumoType(1);
            func.setFumoParentid(null);
            func.setFumoUrl(colurl);
            func.setFumoDescribe(null);
            func.setFumoIcon(colicon);
            authorityDao.saveFunctionModel(func);
        } else if (type == 2) {
            FunctionModel func = new FunctionModel();
            func.setFumoName(colname);
            func.setFumoOrdinal(colorder);
            func.setFumoType(2);
            func.setFumoParentid(parentid);
            func.setFumoUrl(colurl);
            func.setFumoDescribe(null);
            func.setFumoIcon(null);
            authorityDao.saveFunctionModel(func);
        } else if (type == 3) {
            Action action = new Action();
            action.setActiName(colname);
            action.setActiUrl(colurl);
            action.setActiType(2);
            action.setActiDescribe(null);
            int actiId = authorityDao.saveAction(action);
            FunctionAction funcAction = new FunctionAction();
            funcAction.setFuacActiId(actiId);
            funcAction.setFuacFumoId(parentid);
            authorityDao.saveFuncAction(funcAction);
        } else {

        }
    }

    /**
     * 编辑栏目
     * 
     * @author yangzq
     * @param type
     * @param parentid
     * @param colname
     * @param colurl
     * @param colorder
     * @return
     */
    @Transactional
    public int editCols(int type, int parentid, int colid, String colname, String colurl, int colorder)
            throws Exception {
        if (type == 1) {
            FunctionModel func = new FunctionModel();
            func.setFumoName(colname);
            func.setFumoOrdinal(colorder);
            func.setFumoType(1);
            func.setFumoId(colid);
            func.setFumoParentid(null);
            func.setFumoUrl(colurl);
            func.setFumoDescribe(null);
            func.setFumoIcon(null);
            authorityDao.editFunctionModel(func);
        } else if (type == 2) {
            FunctionModel func = new FunctionModel();
            func.setFumoName(colname);
            func.setFumoOrdinal(colorder);
            func.setFumoType(2);
            func.setFumoId(colid);
            func.setFumoParentid(parentid);
            func.setFumoUrl(colurl);
            func.setFumoDescribe(null);
            func.setFumoIcon(null);
            authorityDao.editFunctionModel(func);
        } else if (type == 3) {
            Action action = new Action();
            action.setActiId(colid);
            action.setActiName(colname);
            action.setActiUrl(colurl);
            action.setActiType(2);
            action.setActiDescribe(null);
            authorityDao.editAction(action);
        } else {

        }
        return 0;
    }

}
