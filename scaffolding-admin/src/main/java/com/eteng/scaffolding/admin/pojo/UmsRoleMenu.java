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
 * 角色菜单关系实体
* @author eTeng
* @date 2019-12-27
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "UPDATE ums_role_menu SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_role_menu) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ums_role_menu SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ums_role_menu) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="ums_role_menu",indexes = {@Index(name = "role_menu_role_id_menu_id_del_idx",columnList = "del,role_id,menu_id")})
public class UmsRoleMenu extends BaseEntity {

    /**
     * 菜单id
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id")
    private UmsMenu menuId ;

    /**
     * 角色id
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UmsRole roleId ;

    public void copy(UmsRoleMenu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}