package com.eteng.scaffolding.component.aspect;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.eteng.scaffolding.common.annotation.GlobalTransaction;
import com.eteng.scaffolding.common.constant.DefaultNullDataConstant;
import com.eteng.scaffolding.component.exception.GlobalTransactionException;
import com.eteng.scaffolding.service.MmsLocalMessageService;
import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 全局事务切面
 * @FileName GlobalTransactionAspect
 * @Author eTeng
 * @Date 2020/3/19 15:15
 * @Description
 */
@Aspect
@Component
@Slf4j
public class GlobalTransactionAspect {

    @Autowired
    private MmsLocalMessageService mmsLocalMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment environment;

    /**
     * 全局事务注解端点
     */
    @Pointcut("execution(public * com.eteng.scaffolding..*(..)) && @annotation(com.eteng.scaffolding.common.annotation.GlobalTransaction)")
    public void startPoint(){}

    @Around(value = "startPoint()")
    public void start(ProceedingJoinPoint joinPoint)throws Throwable{
        log.info("开启全局事务......");
        MmsLocalMessageDTO build = null;
        String exchange = null;
        String routingKey = null;
        Integer maxRetry = null;
        try{
            Object message = joinPoint.proceed();
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            Method method = signature.getMethod();
            GlobalTransaction globalTransaction = method.getDeclaredAnnotation(GlobalTransaction.class);
            if (globalTransaction != null) {
                if(message == null){
                    throw GlobalTransactionException.faild("操作失败！");
                }
                String s = objectMapper.writeValueAsString(message);
                exchange = globalTransaction.exchange();
                routingKey = globalTransaction.routingKey();
                maxRetry = globalTransaction.maxRetry();
                build = new MmsLocalMessageDTO();
                build.setSendStatus(MmsLocalMessageDTO.UNSEND);
                build.setMessageId(IdUtil.simpleUUID());
                build.setVersion(0);
                build.setBody(s);
                build.setRetry(0);
                build.setMaxRetry(maxRetry);
                // 从Properties获取
                String exchangeVal = environment.getProperty(exchange);
                if(StrUtil.isBlank(exchangeVal)){
                    exchangeVal = exchange;
                }
                build.setExchange(exchangeVal);
                String routingKeyVal = environment.getProperty(routingKey);
                if (StrUtil.isBlank(routingKeyVal)) {
                    routingKeyVal = routingKey;
                }
                build.setRoutingKey(routingKeyVal);
                mmsLocalMessageService.create(build);
                log.info("发送消息成功，消息的id为：{}",build.getMessageId());
            }
        }catch (Exception var1){
            log.error("开启全局事务失败，切面的方法名称为：start(),消息的id为：{},异常信息为：{}",
                    build == null? DefaultNullDataConstant.DEFALUT_TEXT :build.getMessageId(),var1.getMessage());
            throw var1;
        }
    }
}


