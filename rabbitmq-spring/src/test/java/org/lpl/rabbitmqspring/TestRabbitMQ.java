package org.lpl.rabbitmqspring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * Created by liupenglei on 2018/9/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRabbitMQ {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void testAdmin() throws Exception {

        //声明一个exchange
        rabbitAdmin.declareExchange(new DirectExchange("test.direct",false,false));

        rabbitAdmin.declareExchange(new TopicExchange("test.topic",false,false));

        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout",false,false));

        //声明一个queue
        rabbitAdmin.declareQueue(new Queue("test.direct.queue",false));

        rabbitAdmin.declareQueue(new Queue("test.topic.queue",false));

        rabbitAdmin.declareQueue(new Queue("test.fanout.queue",false));

        //queue绑定exchange的第一种方式
        rabbitAdmin.declareBinding(new Binding("test.direct.queue",
                Binding.DestinationType.QUEUE,
                "test.direct","direct",new HashMap<String,Object>()));

        //queue绑定exchange的第二种方式
        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("test.topic.queue",false))
                        .to(new TopicExchange("test.topic",false,false))
                        .with("user.#"));

        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("test.fanout.queue",false))
                        .to(new FanoutExchange("test.fanout",false,false)));

        //清空队列中的数据
        rabbitAdmin.purgeQueue("test.topic.queue",false);
    }
}
