package com.pphh.rpc.rpc;

/**
 * Created by mh on 2018/2/18.
 */
public class URL {

    private String protocol;
    private String host;
    private int port;
    private String path;

    public URL() {
        this.protocol = "http";
        this.host = "127.0.0.1";
        this.port = 80;
        this.path = "rpc";
    }

    public URL(String host, int port) {
        this("http", host, port, "rpc");
    }

    public URL(String protocol, String host, int port, String path) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public String toString() {
        return String.format("%s://%s:%s/%s", protocol, host, port, path);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public static URL valueOf(String url) {
        String protocol = null;
        String host = null;
        int port = 80;
        String path = null;

        int i = url.indexOf("://");
        if (i >= 0) {
            protocol = url.substring(0, i);
            url = url.substring(i + 3);

            i = url.indexOf("/");
            if (i >= 0) {
                path = url.substring(i + 1);
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
            rt = new URL(protocol, host, port, path);
        }

        return rt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        URL url = (URL) obj;

        if (port != url.port) return false;
        if (!protocol.equals(url.protocol)) return false;
        if (!host.equals(url.host)) return false;
        return path.equals(url.path);

    }

    @Override
    public int hashCode() {
        int result = protocol.hashCode();
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
        result = 31 * result + path.hashCode();
        return result;
    }
}
