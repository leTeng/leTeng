package com.eteng.scaffolding.admin.component.listener.data;

import com.eteng.scaffolding.admin.pojo.UmsMenu;
import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;

import java.util.Collection;

/**
 * 临时权限数据管理器
 * @FileName MenuQueryCacheDataPowerListener
 * @Author eTeng
 * @Date 2020/1/5 13:53
 * @Description
 */
public interface DataManager {

    Collection<UmsRoleDTO> getRole(String dataType,String key)throws Exception;

    boolean putRole(String dataType,String key,UmsRoleDTO... value)throws Exception;

    boolean removeRole(String dataType, String... key)throws Exception;

    Collection<UmsMenuDTO> getMenu(String dataType, String key)throws Exception;

    boolean putMenu(String dataType,String key,UmsMenuDTO... value)throws Exception;

    boolean removeMenu(String dataType,String... key)throws Exception;

    Collection<UmsResourceDTO> getResource(String dataType, String key)throws Exception;

    boolean putResource(String dataType,String key,UmsResourceDTO... value)throws Exception;

    boolean removeResource(String dataType, String... key)throws Exception;
}
