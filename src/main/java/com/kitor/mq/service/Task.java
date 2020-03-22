package com.kitor.mq.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


/**
 * Created by vinhphan on 5/31/18.
 */

@Slf4j
public final class Task implements Serializable {
    public static final String TYPE_NAME = "_type";

    public Task(Type type, Map<String, String> params) {
        this.type = type;
        this.params.putAll(params);
    }

    
    public enum Type {
        TEST,
        USER,
        EMAIL,
    }

    public Type type;
    private Map<String, String> params = new HashMap<>();
    public List<String> errors = new ArrayList<String>();

    public String encode() {
        params.put(TYPE_NAME, type.toString());
        Gson gson = new Gson();
        return gson.toJson(params);
    }

    public Map<String, String> getParams(){
        return this.params;
    }
    private static Task load(Map<String, String> params) {
        log.info("load " + params);
        Object type = params.remove(TYPE_NAME);
        log.info("type is " + type);

        if (type == null) {
            throw new IllegalArgumentException("missing type for params " + params);
        }

        return new Task(Type.valueOf(type.toString()), params);
    }


    public static Task decode(String s) throws IOException {
        if (s == null) return null;
        try {
            HashMap map = new ObjectMapper().readValue(s, HashMap.class);
            return load(map);
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isValid() {
        switch (type) {
            case TEST:
                checkRequired("id,message");
                break;
            case EMAIL:
                checkRequired("password,user,template");
                break;
        }
        return errors.size() == 0;
    }

    private void checkRequired(String keys) {
        Arrays.stream(keys.split(",")).forEach((String item) -> {
            if (!this.params.containsKey(item)) errors.add("missing params" + item);
        });
    }

    @Override
    public String toString() {
        return "Task{" +
                "type='" + this.type.toString() + '\'' +
                "params" + this.params.toString() + '\'' + "}";
    }

}
