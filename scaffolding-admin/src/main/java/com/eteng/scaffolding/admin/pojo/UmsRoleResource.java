package com.eteng.scaffolding.admin.pojo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.eteng.scaffolding.common.bean.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 角色资源实体关系
* @author eTeng
* @date 2019-12-27
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "UPDATE ums_role_resource SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_role_resource) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ums_role_resource SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_role_resource) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="ums_role_resource",uniqueConstraints = {@UniqueConstraint(name = "role_resource_role_id_menu_resource_id_del_idx",
        columnNames = {"del","role_id","resource_id"})})
public class UmsRoleResource extends BaseEntity {

    /**
     * 角色id
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UmsRole roleId ;

    /**
     * 资源id
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    private UmsResource resourceId ;

    public void copy(UmsRoleResource source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}