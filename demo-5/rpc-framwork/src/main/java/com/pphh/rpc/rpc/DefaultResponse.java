/*
 * Copyright 2018 peipeihh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
package com.pphh.rpc.rpc;

import java.io.Serializable;

/**
 * Created by huangyinhuang on 1/11/2018.
 */

public class DefaultResponse implements Response, Serializable {

    private static final long serialVersionUID = -8924333945227948972L;

    private Object value;
    private Exception exception;
    private long requestId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Exception getException() {
        return this.exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

}
