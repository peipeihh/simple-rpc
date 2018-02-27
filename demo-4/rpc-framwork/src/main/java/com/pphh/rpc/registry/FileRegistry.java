package com.pphh.rpc.registry;

import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.util.LogUtil;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mh on 2018/2/18.
 * A file registry, which store and discover service in a file
 * To be developed
 */
public class FileRegistry implements Registry {

    static String HOST_SPLITOR = ",";
    static String REGISTRY_SPLITOR = "==";
    private String sharedFile;

    public FileRegistry(String filePath) {
        this.sharedFile = filePath;
    }

    @Override
    public void register(URL remoteRpcUrl, String serviceName) {
        HashMap<String, String> registries = readFromFile();

        // refresh the service's registry by add the rpc remote host
        String providerHosts = registries.get(serviceName);
        Set<URL> providers = filter(providerHosts, remoteRpcUrl);
        providers.add(remoteRpcUrl);

        providerHosts = "";
        for (URL url : providers) {
            providerHosts += url.toString() + FileRegistry.HOST_SPLITOR;
        }
        registries.put(serviceName, providerHosts);

        writeToFile(registries);
    }

    @Override
    public void unregister(URL remoteRpcUrl, String serviceName) {
        HashMap<String, String> registries = readFromFile();

        // refresh the service's registry by remove the rpc remote host
        String providerHosts = registries.get(serviceName);
        Set<URL> providers = filter(providerHosts, remoteRpcUrl);

        providerHosts = "";
        for (URL url : providers) {
            providerHosts += url.toString() + FileRegistry.HOST_SPLITOR;
        }
        registries.put(serviceName, providerHosts);

        writeToFile(registries);
    }

    @Override
    public Set<URL> discover(String serviceName) {
        HashMap<String, String> registryMap = readFromFile();

        String remoteHosts = registryMap.get(serviceName);
        Set<URL> remoteServiceUrls = new HashSet<>();
        String[] urls = remoteHosts.split(HOST_SPLITOR);
        for (String url : urls) {
            URL provider = URL.valueOf(url);
            if (provider != null) {
                remoteServiceUrls.add(provider);
            }
        }

        return remoteServiceUrls;
    }

    /**
     * Input a list of provider and remove
     * @param providerHosts a list of provider hosts which is separated by HOST_SPLITOR
     * @param providerInFilter a provider to be removed
     * @return a list of provider after removing
     */
    private Set<URL> filter(String providerHosts, URL providerInFilter){
        Set<URL> providers = new HashSet<>();

        if (providerHosts != null){
            String[] urls = providerHosts.split(FileRegistry.HOST_SPLITOR);
            for (String url : urls) {
                if (!providerInFilter.equals(URL.valueOf(url))) {
                    providers.add(URL.valueOf(url));
                }
            }
        }

        return providers;
    }

    private HashMap<String, String> map(Set<String> registrySet) {
        HashMap<String, String> registries = new HashMap<>();
        for (String registry : registrySet) {
            String[] map = registry.split(REGISTRY_SPLITOR);
            if (map.length == 2) {
                String serviceName = map[0];
                String serviceProvider = map[1];
                if (!registries.containsKey(serviceName)) {
                    registries.put(serviceName, serviceProvider);
                }
            }
        }
        return registries;
    }

    private HashMap<String, String> readFromFile() {
        Set<String> registrySet = new HashSet<>();

        BufferedReader reader = null;
        try {
            FileReader file = new FileReader(this.sharedFile);
            reader = new BufferedReader(file);
            String temp;
            while ((temp = reader.readLine()) != null) {
                if (!registrySet.contains(temp)) {
                    registrySet.add(temp);
                }
            }
            reader.close();
        } catch (IOException e) {
            LogUtil.print("receive an exception when trying to read registry records from file " + this.sharedFile);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LogUtil.print("receive an exception when trying to close file reader");
                }
            }
        }

        return map(registrySet);
    }

    private void writeToFile(HashMap<String, String> registries) {

        BufferedWriter writer = null;
        try {
            FileWriter file = new FileWriter(this.sharedFile, false);
            writer = new BufferedWriter(file);
            for (Object key : registries.keySet()) {
                String service = key.toString();
                String provider = registries.get(service);
                writer.write(service + this.REGISTRY_SPLITOR + provider);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            LogUtil.print("receive an exception when trying to write registry records to file " + this.sharedFile);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LogUtil.print("receive an exception when trying to close file writer");
                }
            }
        }
    }
}
