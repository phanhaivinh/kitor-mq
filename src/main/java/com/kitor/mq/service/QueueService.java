package com.kitor.mq.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

/**
 * Created by vinhphan on 6/4/18.
 */

@Service
@Slf4j
public class QueueService {

    @Autowired
    private RabbitTemplate template;

    private String getQueueName(Task.Type type) {
        return  type.toString().toUpperCase();
    }

    public void sendTask(Task task) {
        try {
            String name =  getQueueName(task.type);
            log.info("sending task " + task.toString() +"--- " + task.errors.toString());
            if (!task.isValid()) {
               log.info("invalid task " + task.toString() + task.errors.toString());
               return;
            }
            template.convertAndSend(name, task.encode());
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
