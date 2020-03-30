package com.eteng.scaffolding.admin.service;

import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsMenuQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsMenuService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<UmsMenuDTO>
    */
    Page<UmsMenuDTO> queryAll(UmsMenuQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmsMenuDTO>
    */
    List<UmsMenuDTO> queryAll(UmsMenuQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmsMenuDTO
     */
    UmsMenuDTO findById(String id);

    UmsMenuDTO create(UmsMenuDTO resources);

    void update(UmsMenuDTO resources);

    void delete(String id);

    void download(List<UmsMenuDTO> all, HttpServletResponse response) throws IOException;
}