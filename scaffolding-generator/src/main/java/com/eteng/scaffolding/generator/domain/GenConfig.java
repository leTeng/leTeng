package com.eteng.scaffolding.generator.domain;

import com.eteng.scaffolding.common.bean.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 代码生成配置
 * @author Zheng Jie
 * @date 2019-01-03
 */
@Data
@Entity
@Table(name = "gen_config")
public class GenConfig extends BaseEntity {

    @Id
    @GeneratedValue(generator = "u_id")
    @GenericGenerator(name = "u_id",strategy = "uuid.hex")
    private String id;

    // 包路径
    private String pack;

    // 模块名
    @Column(name = "module_name")
    private String moduleName;

    // 前端文件路径
    private String path;

    // 前端文件路径
    @Column(name = "api_path")
    private String apiPath;

    // 作者
    private String author;

    // 表前缀
    private String prefix;

    // 是否覆盖
    private Boolean cover;
}
