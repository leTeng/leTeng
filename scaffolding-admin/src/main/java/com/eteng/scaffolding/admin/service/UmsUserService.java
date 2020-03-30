package com.eteng.scaffolding.admin.service;

import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsUserService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<UmsUserDTO>
    */
    Page<UmsUserDTO> queryAll(UmsUserQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmsUserDTO>
    */
    List<UmsUserDTO> queryAll(UmsUserQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmsUserDTO
     */
    UmsUserDTO findById(String id);

    UmsUserDTO create(UmsUserDTO resources);

    UmsUserDTO update(UmsUserDTO resources);

    UmsUserDTO delete(String id);

    void download(List<UmsUserDTO> all, HttpServletResponse response) throws IOException;

    /**
     * 用户名称查询用户
     * @param userName 用户名
     * @return
     */
    UmsUserDTO findByUserName(String userName);

    /**
     * 用户名称查询用户
     * @param phoneNum 手机号
     * @return
     */
    UmsUserDTO findByPhone(String phoneNum);

    /**
     * 加载指定用户的所有权限
     * @param userId
     * @return
     */
    Collection<String> loadAuthority(String userId) throws Exception;

    /**
     * 获取当前登陆用户
     * @return
     */
    UmsUserDTO currentUser();
}