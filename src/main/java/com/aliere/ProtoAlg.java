package com.aliere;

import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ProtoAlg {
    String name;
    List<ProtoParam> params;

    public static class ProtoParam {
        String name;
        int value;
        boolean valid;
        EventHandler<ActionEvent> action;

        public ProtoParam(String name) {
            this.name = name;
        }

        protected ProtoParam(String name, EventHandler<ActionEvent> action) {
            this(name);
            this.action = action;
        }
    }

    public ProtoAlg(String name, ProtoParam... params) {
        this.name = name;
        this.params = Arrays.asList(params);
    }
}
