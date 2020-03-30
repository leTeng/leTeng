package com.eteng.scaffolding.common.bean;

import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author Zheng Jie
 * @Date 2019年10月24日20:48:53
 */
@Getter
@Setter
public class BaseDTO  implements Serializable {

    /**
     * 添加数据参数校验组
     */
    public interface C extends Default {}

    /**
     * 修改删除参数校验组
     */
    public interface Ru extends Default{}

    public BaseDTO() {
    }

    public BaseDTO(@NotBlank(groups = Ru.class, message = "id不能为空") @Null(groups = C.class, message = "id要为空") String id, Date createTime, Date updateTime, Integer del) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.del = del;
    }
    @ApiModelProperty("id")
    @NotBlank(groups = Ru.class,message = "id不能为空")
    @Null(groups = C.class,message = "id要为空")
    private String id;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = DTFormatConstant.ZONE_SHANGHAI,pattern = DTFormatConstant.DATE_TIME)
    private Date createTime = new Date();

    @ApiModelProperty("最后更新时间")
    @JsonFormat(timezone = DTFormatConstant.ZONE_SHANGHAI,pattern = DTFormatConstant.DATE_TIME)
    private Date updateTime = new Date();

    // 删除标识
    @ApiModelProperty(value = "是否已删除",notes = "0 表示未删除，≠0 表示已删除")
    private Integer del = 0;

    public @interface Update {}

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
}
