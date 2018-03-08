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
package com.pphh.demo.feign;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * Created by huangyinhuang on 3/8/2018.
 */
public interface HttpBinClient {

    @RequestLine("GET /ip")
    IpInfo getMyIpInfo();

    @RequestLine("GET /get")
    RequestInfo getRequest();

    @RequestLine("POST /post")
    @Headers("Content-Type: application/xml")
    @Body("<login \"user_name\"=\"{user_name}\" \"password\"=\"{password}\"/>")
    RequestInfo postXmlRequest(@Param("user_name") String user, @Param("password") String password);

    @RequestLine("POST /post")
    @Headers("Content-Type: application/json")
    // json curly braces must be escaped!
    @Body("%7B\"user_name\": \"{user_name}\", \"password\": \"{password}\"%7D")
    RequestInfo postJsonRequest(@Param("user_name") String user, @Param("password") String password);


    class IpInfo {
        public String origin;
    }

    class RequestInfo {
        public Object args;
        public Object data;
        public Object headers;
        public String origin;
        public String url;
    }
}
