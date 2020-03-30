package com.eteng.scaffolding.admin.component.aspect.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作结果
 * @FileName OperatingResult
 * @Author eTeng
 * @Date 2020/1/8 16:08
 * @Description
 */

@Builder
@Getter
@Setter
public class OperatingResult {
    /**
     * 是否继续向下执行
     */
    private boolean isContinue;
    /**
     * 返回的数据
     */
    private Object data;
}
