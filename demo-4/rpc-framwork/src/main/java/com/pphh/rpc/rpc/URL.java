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

import java.util.*;

/**
 * Created by mh on 2018/2/18.
 */
public class URL {

    private String protocol;
    private String host;
    private int port;
    private String path;
    private Map<String, String> queries;

    public URL() {
        this.protocol = "http";
        this.host = "127.0.0.1";
        this.port = 80;
        this.path = "rpc";
        this.queries = new HashMap<>();
    }

    public URL(String host, int port) {
        this("http", host, port, "rpc", null);
    }

    public URL(String protocol, String host, int port, String path, String fullQuery) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.queries = new HashMap<>();
        this.setQuery(fullQuery);
    }

    public static URL valueOf(String url) {
        String protocol = null;
        String host = null;
        int port = 80;
        String path = null;
        String query = null;

        // an example of url format
        // http://localhost:8080/hello?timestamp=1
        int i = url.indexOf("://");
        if (i >= 0) {
            protocol = url.substring(0, i);
            url = url.substring(i + 3);

            i = url.indexOf("/");
            if (i >= 0) {
                path = url.substring(i + 1);
                int qIndex = path.indexOf("?");
                if (qIndex >= 0) {
                    query = path.substring(qIndex + 1);
                    path = path.substring(0, qIndex);
                }

                url = url.substring(0, i);
                i = url.indexOf(":");
                if (i >= 0) {
                    port = Integer.parseInt(url.substring(i + 1));
                    host = url.substring(0, i);
                }
            }
        }

        // initialize the URL when the input url has an correct string format
        URL rt = null;
        if (protocol != null && host != null && port > 0 && path != null) {
            rt = new URL(protocol, host, port, path, query);
        }

        return rt;
    }

    public String toString() {
        String fullUrl = String.format("%s://%s:%s/%s", protocol, host, port, path);
        if (this.queries != null) {
            String fullQuery = "";
            for (Map.Entry<String, String> entry : this.queries.entrySet()) {
                if (entry.getValue().equals("")) {
                    fullQuery += String.format("%s", entry.getKey());
                } else {
                    fullQuery += String.format("%s=%s", entry.getKey(), entry.getValue());
                }
            }

            if (!fullQuery.equals("")) {
                fullUrl = String.format("%s?%s", fullUrl, fullQuery);
            }
        }

        return fullUrl;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getQuery() {
        return this.queries;
    }

    public void setQuery(String fullQuery) {
        if (fullQuery != null) {
            String[] queries = fullQuery.split("&");

            for (String q : queries) {
                String[] qItems = q.split("=");
                if (qItems.length == 2) {
                    this.queries.put(qItems[0], qItems[1]);
                } else {
                    this.queries.put(qItems[0], "");
                }
            }
        }
    }

    public void setQueryItem(String qKey, String qValue) {
        this.queries.put(qKey, qValue);
    }

    public String findQueryItem(String qKey) {
        return this.queries.get(qKey);
    }

    /**
     * compare url by protocol://host:port/path, which doesn't include the query as comparison
     *
     * @param obj the url to be compared
     * @return return true if two url has same protocol + host + port + path
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        URL url = (URL) obj;

        if (port != url.port) return false;
        if (!protocol.equals(url.protocol)) return false;
        if (!host.equals(url.host)) return false;
        //if (queries != null) return this.toString().equals(url.toString());

        return path.equals(url.path);
    }

    @Override
    public int hashCode() {
        int result = protocol.hashCode();
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
        result = 31 * result + path.hashCode();
        //if (queries != null) result = 31 * result + queries.hashCode();
        return result;
    }
}
