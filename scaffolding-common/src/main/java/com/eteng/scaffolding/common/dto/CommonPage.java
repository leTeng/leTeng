package com.eteng.scaffolding.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 分页数据封装类
 * Created by macro on 2019/4/19.
 */
@ApiModel
public class CommonPage<T> {
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("页面大小")
    private Integer pageSize;
    @ApiModelProperty("总页数")
    private Integer totalPage;
    @ApiModelProperty("总记录数")
    private Long total;
    @ApiModelProperty("分页数据")
    private List<T> list;

    /**
     * 将自定义的结果分页后的list转为分页信息
     */
    public static <T> CommonPage<T> toPage(List<T> list, Pageable pageable,long count) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage((int)(count % pageSize == 0? count % pageSize : count % pageSize + 1));
        result.setPageNum(pageNumber);
        result.setPageSize(pageSize);
        result.setTotal(count);
        result.setList(list);
        return result;
    }

    /**
     * 将自定义的结果分页后的list转为分页信息
     */
    public static <T> CommonPage<T> toPage(List<T> list, Integer pageNum, Integer pageSize,long count) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage((int)(count % pageSize == 0? count / pageSize : count / pageSize + 1));
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(count);
        result.setList(list);
        return result;
    }

    /**
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
