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
 * 资源实体
* @author eTeng
* @date 2019-12-27
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false,exclude = "roleResources")
@SQLDelete(sql = "UPDATE ums_resource SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_resource) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ums_resource SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_resource) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="ums_resource",uniqueConstraints = {@UniqueConstraint(name = "uri_method_del_unique",columnNames = {"del","uri","method"})},
        indexes = {@Index(name = "type_del_idx",columnList = "del,type")})
public class UmsResource extends BaseEntity {

    /**
     * 资源路径
     */
    @Column(name = "uri",nullable = false)
    private String uri ;

    /**
     * 资源的请求方法
     */
    @Column(name = "method",nullable = false)
    private String method ;

    /**
     * 资源类型
     */
    @Column(name = "type")
    private Integer type ;

    /**
     * 资源描述
     */
    @Column(name = "description")
    private String description ;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "resourceId",cascade = CascadeType.REMOVE)
    private Set<UmsRoleResource> roleResources = new HashSet<>();

    public void copy(UmsResource source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}