package com.kitor.mq.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * Created by vinhphan on 5/31/18.
 */
@Slf4j
public abstract class ObserverService implements Serializable {
    @Value("${app.queue.prefix}")
    protected  String prefix;

    @RabbitHandler
    public void recieverMessage(String msg) {
        try {
            Task task = Task.decode(msg);
            if (task != null) {
                if (!task.isValid()) {
                    log.info("invalid task ObserverService " + task.errors);
                }else {
                    processTask(task);
                }
            }
        }catch (Exception ex ) {
            ex.printStackTrace();
        }
    }

    public  abstract String getName();

    public abstract void processTask(Task task) throws Exception;
}
