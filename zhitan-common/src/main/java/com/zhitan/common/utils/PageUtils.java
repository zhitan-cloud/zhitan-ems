package com.zhitan.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zhitan.common.core.page.PageDomain;
import com.zhitan.common.core.page.TableSupport;
import com.zhitan.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 * 
 * @author zhitan
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
    
    public static Page getPage(){
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        
        return new Page(pageNum,pageSize);
    }

    public static <T> Page<T> getPageInfo() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        return new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());
    }

    public static <R> Page<R> getPageInfo(Class<R> R) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        return new Page<R>(pageDomain.getPageNum(), pageDomain.getPageSize());
    }

}
