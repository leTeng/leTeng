package com.eteng.scaffolding.configuration;

import com.eteng.scaffolding.common.transaction.ConfirmMessageHandler;
import com.eteng.scaffolding.common.transaction.ReturnMessageHandler;
import com.eteng.scaffolding.common.transaction.RoutingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * RabbitMq配置
 * setConfirmCallback返回ack的一个坑，就是当消息成功发送到交换器，但是没有匹配的队列，就会触发 ReturnCallback 回调，
 * 而且消息也丢失了，最致命的是setConfirmCallback回调返回的ack却是true，如果单靠这个ack来判断消息是否成功到达mq，
 * 就有一定概率造成消息丢失。
 * 引用：https://juejin.im/post/5c6f63d4f265da2d9b5e1697
 * @FileName RabbitMqConfig
 * @Author eTeng
 * @Date 2020/3/12 22:34
 * @Description
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    @Autowired
    private RoutingProperties routingProperties;

    @Autowired
    private Collection<ConfirmMessageHandler> confirmMessageHandlers;

    @Autowired
    private Collection<ReturnMessageHandler> returnMessageHandlers;

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(routingProperties.getExchange(),true,false);
    }

    @Bean
    public Queue postQueue(){
        // 持久化、非排他、非自动删除
        return new Queue(routingProperties.getPostQueue(),true,false,false);
    }

    @Bean
    public Queue deleteQueue(){
        // 持久化、非排他、非自动删除
        return new Queue(routingProperties.getDeleteQueue(),true,false,false);
    }

    @Bean
    public Queue putQueue(){
        // 持久化、非排他、非自动删除
        return new Queue(routingProperties.getPutQueue(),true,false,false);
    }

    @Bean
    public Binding bindExchangOnPostQueue(){
        return BindingBuilder.bind(postQueue()).to(exchange()).with(routingProperties.getPostQueue());
    }

    @Bean
    public Binding bindExchangOnPutQueue(){
        return BindingBuilder.bind(putQueue()).to(exchange()).with(routingProperties.getPutQueue());
    }

    @Bean
    public Binding bindExchangOnDeleteQueue(){
        return BindingBuilder.bind(deleteQueue()).to(exchange()).with(routingProperties.getDeleteQueue());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory){
        // 设置发送消息手动确认(可以在配置文件配置)
        connectionFactory.setPublisherConfirms(true);
        // 设置发送消息未找到目标exchange,queue后，手动处理
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 消息发送成功成功确认
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause) -> {
            String id = correlationData.getId();
            Message returnedMessage = correlationData.getReturnedMessage();
            log.info("消息确认回调，消息id为：{}，ack:{}",id,ack);
            confirmMessageHandlers.forEach(c -> {
                if (c.support(id)) {
                    c.handler(id,ack);
                }
            });
        });
        // 设置ReturnCallback必须设置Mandatory -> true
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey) -> {
            log.error("消息未找到队列，回退消息。消息为：{}，exchange:{},routingKey:{}",message,exchange,routingKey);
            returnMessageHandlers.stream().forEach(r -> {
                if (r.support(exchange,routingKey)) {
                    r.handler(exchange,routingKey,message);
                }
            });
        });
        // 设置数据序列化(因为发送的数据已经序列化为Json，所以发送时不需要再次系列化)
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
