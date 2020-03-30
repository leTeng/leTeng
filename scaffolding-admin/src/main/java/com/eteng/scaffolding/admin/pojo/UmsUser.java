package com.eteng.scaffolding.admin.pojo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.eteng.scaffolding.common.bean.BaseEntity;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体
* @author eTeng
* @date 2019-12-27
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false,exclude = {"userRoles"})
@SQLDelete(sql = "UPDATE ums_user SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_user) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ums_user SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_user) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="ums_user", uniqueConstraints = {@UniqueConstraint(name = "user_name_unique",columnNames = {"del","user_name"}),
        @UniqueConstraint(name = "type_id_unique",columnNames = {"del","type_id"}), @UniqueConstraint(name = "phone_unique",
        columnNames = {"del","phone"})},indexes = {@Index(name = "type_idx",columnList = "type,del")})
public class UmsUser extends BaseEntity {

    /**
     * 登录/登录名称
     */
    @Column(name = "user_name",nullable = false)
    private String userName ;

    /**
     * 密码
     */
    @Column(name = "credentials")
    private String credentials ;

    /**
     * 手机号码
     */
    @Column(name = "phone",nullable = false)
    private String phone  = "N" ;

    /**
     * 用户是否可用(1 表示可用，0表示禁用)
     */
    @Column(name = "enabled",nullable = false)
    private Boolean enabled  = true ;

    /**
     * 用户是否不过期(1表示不过期，0表示过期)
     */
    @Column(name = "account_non_expired",nullable = false)
    private Boolean accountNonExpired  = true ;

    /**
     * 密码是否不过期(1表示不过期，0表示过期)
     */
    @Column(name = "credentials_non_expired",nullable = false)
    private Boolean credentialsNonExpired  = true ;

    /**
     * 用户是否不锁定(1表示不锁定，0表示锁定)
     */
    @Column(name = "account_non_locked",nullable = false)
    private Boolean accountNonLocked  = true ;

    /**
     * 角色Id
     */
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id")
    private UmsRole roleId;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "userId",cascade = CascadeType.REMOVE)
    private Set<UmsUserRole> userRoles = new HashSet<>();

    /**
     * 最后登录时间
     */
    @Column(name = "last_login",nullable = false)
    @JsonFormat(timezone = DTFormatConstant.ZONE_SHANGHAI,pattern = DTFormatConstant.DATE_TIME)
    private Date lastLogin  = new Date() ;

    /**
     * 用户类型
     */
    @Column(name = "type",nullable = false)
    private Integer type ;

    /**
     * 用户类型id
     */
    @Column(name = "type_id",nullable = false)
    private String typeId ;

    public void copy(UmsUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}