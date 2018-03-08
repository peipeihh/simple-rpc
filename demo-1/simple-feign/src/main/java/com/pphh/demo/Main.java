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
package com.pphh.demo;

import com.pphh.demo.feign.GithubClient;
import com.pphh.demo.feign.GithubClient.Contributor;
import com.pphh.demo.feign.GithubClient.Repository;
import com.pphh.demo.feign.HttpBinClient;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;

import java.util.List;

/**
 * Created by huangyinhuang on 3/8/2018.
 */
public class Main {

    public static void main(String[] args) {
        demoSimpleFeignClient();
        demoCustomizedFeignClient();
        demoPostFeignClient();
    }

    public static void demoSimpleFeignClient() {
        HttpBinClient httpBinClient = Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .client(new OkHttpClient())
                .target(HttpBinClient.class, "https://httpbin.org");

        System.out.println("== Print my public ip address ==");
        HttpBinClient.IpInfo ipInfo = httpBinClient.getMyIpInfo();
        System.out.println(ipInfo.origin);
    }

    public static void demoCustomizedFeignClient() {
        GithubClient githubClient = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GithubClient.class, "https://api.github.com");

        System.out.println("== Print all contributors to OpenFeign project ==");
        int count = 0;
        List<Contributor> contributors = githubClient.contributors("OpenFeign", "feign");
        for (Contributor contributor : contributors) {
            String info = String.format("[%s] %s (%s)", ++count, contributor.login, contributor.contributions);
            System.out.println(info);
        }

        System.out.println("== Print all repositories by user peipeihh ==");
        count = 0;
        List<Repository> repositories = githubClient.repos("peipeihh");
        for (Repository repository : repositories) {
            String info = String.format("[%s] %s (%s)", ++count, repository.name, repository.url);
            System.out.println(info);
        }
    }

    public static void demoPostFeignClient() {
        HttpBinClient httpBinClient = Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .client(new OkHttpClient())
                .target(HttpBinClient.class, "https://httpbin.org");

        System.out.println("== Print get request info ==");
        HttpBinClient.RequestInfo requestInfo = httpBinClient.getRequest();
        printRequestInfo(requestInfo);

        System.out.println("== Print post request info (json) ==");
        requestInfo = httpBinClient.postJsonRequest("username", "password");
        printRequestInfo(requestInfo);

        System.out.println("== Print post request info (xml) ==");
        requestInfo = httpBinClient.postXmlRequest("username", "password");
        printRequestInfo(requestInfo);
    }

    private static void printRequestInfo(HttpBinClient.RequestInfo requestInfo) {
        System.out.println("url   : " + requestInfo.url);
        System.out.println("data  : " + requestInfo.data);
        System.out.println("origin: " + requestInfo.origin);
        System.out.println("args  : " + requestInfo.args);
    }

}
