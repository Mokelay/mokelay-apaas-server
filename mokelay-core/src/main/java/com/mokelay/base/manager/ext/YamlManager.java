package com.mokelay.base.manager.ext;

import org.springframework.core.io.FileSystemResource;

import java.io.File;

public abstract class YamlManager {
    /**
     * Get Yaml Folder
     *
     * @param connectionUrl
     * @param resource
     * @return
     */
    public File getYamlBeanAddress(String connectionUrl, String resource) {
        File cf = new FileSystemResource(connectionUrl).getFile();
        if (!cf.exists()) {
            cf.mkdirs();
        }
        File rf = new File(cf, resource);
        if (!rf.exists()) {
            rf.mkdirs();
        }
        return rf;
    }
}
