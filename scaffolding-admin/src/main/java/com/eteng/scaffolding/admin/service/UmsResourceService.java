package com.eteng.scaffolding.admin.service;

import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsResourceQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsResourceService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<UmsResourceDTO>
    */
    Page<UmsResourceDTO> queryAll(UmsResourceQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmsResourceDTO>
    */
    List<UmsResourceDTO> queryAll(UmsResourceQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmsResourceDTO
     */
    UmsResourceDTO findById(String id);

    UmsResourceDTO create(UmsResourceDTO resources);

    void update(UmsResourceDTO resources);

    void delete(String id);

    void download(List<UmsResourceDTO> all, HttpServletResponse response) throws IOException;
}