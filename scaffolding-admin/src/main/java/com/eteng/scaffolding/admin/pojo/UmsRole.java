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
import java.util.HashSet;
import java.util.Set;

/**
 * 角色实体
* @author eTeng
* @date 2019-12-27
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false,exclude = {"userRoles","roleMenus","roleResources"})
@SQLDelete(sql = "UPDATE ums_role SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_role) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ums_role SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_role) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="ums_role",uniqueConstraints = {@UniqueConstraint(name = "name_tag_del_idx",columnNames = {"del","name","tag"})},
        indexes = {@Index(name = "name_del_idx",columnList = "del,name")})
public class UmsRole extends BaseEntity {

    /**
     * 角色的名称(和角色的标签联合唯一)
     */
    @Column(name = "name",nullable = false)
    private String name  = "N" ;

    /**
     * 角色的标签(和角色的名称联合唯一)
     */
    @Column(name = "tag",nullable = false)
    private String tag  = "N" ;

    /**
     * 角色的权限
     */
    @Column(name = "permission")
    private String permission ;

    /**
     * 角色的描述
     */
    @Column(name = "description")
    private String description ;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "roleId",cascade = CascadeType.REMOVE)
    private Set<UmsUserRole> userRoles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "roleId",cascade = CascadeType.REMOVE)
    private Set<UmsRoleMenu> roleMenus = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "roleId",cascade = CascadeType.REMOVE)
    private Set<UmsRoleResource> roleResources = new HashSet<>();

    public void copy(UmsRole source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}