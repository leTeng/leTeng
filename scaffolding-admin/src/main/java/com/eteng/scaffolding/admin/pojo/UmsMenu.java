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
 * 菜单实体
* @author eTeng
* @date 2019-12-27
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false,exclude = "roleMenus")
@SQLDelete(sql = "UPDATE ums_menu SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_menu) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ums_menu SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_menu) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="ums_menu", uniqueConstraints = {@UniqueConstraint(name = "uri_del_idex",columnNames = {"del","uri"})},
        indexes = {@Index(name = "type_type_idx",columnList = "del,type"), @Index(name = "parent_id_del_idx",columnList = "del,parent_id")})
public class UmsMenu extends BaseEntity {

    /**
     * 菜单名称
     */
    @Column(name = "name",nullable = false)
    private String name ;

    /**
     * 菜单的路径
     */
    @Column(name = "uri",nullable = false)
    private String uri ;

    /**
     * 是否显示菜单(1为显示，0为不显示)
     */
    @Column(name = "enable",nullable = false)
    private Boolean enable  = true ;

    /**
     * 上一级菜单
     */
    @Column(name = "parent_id",nullable = false)
    private String parentId  = "N" ;

    /**
     * 菜单排序
     */
    @Column(name = "sort",nullable = false)
    private Integer sort  = 0 ;

    /**
     * 菜单的权限值
     */
    @Column(name = "value")
    private String value ;

    /**
     * 菜单的类型(0为目录，1为菜单，2为按钮)
     */
    @Column(name = "type",nullable = false)
    private Integer type  = 0 ;

    /**
     * 菜单的图标
     */
    @Column(name = "icon")
    private String icon ;

    /**
     * 菜单描述
     */
    @Column(name = "description")
    private String description ;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "menuId",cascade = CascadeType.REMOVE)
    private Set<UmsRoleMenu> roleMenus = new HashSet<>();

    public void copy(UmsMenu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}