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
 * 用户角色关系实体
* @author eTeng
* @date 2019-12-27
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "UPDATE ums_user_role SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_user_role) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ums_user_role SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_user_role) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="ums_user_role",uniqueConstraints = {@UniqueConstraint(name = "del_user_id_role_id_del_uni",columnNames = {"del","user_id","role_id"})})
public class UmsUserRole extends BaseEntity {

    /**
     * 用户id
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UmsUser userId ;

    /**
     * 角色id
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UmsRole roleId;

    public void copy(UmsUserRole source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}