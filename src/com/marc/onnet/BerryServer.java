package com.marc.onnet;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.http11.Http11ResponseHandler;
import com.ettrema.berry.Berry;
import com.ettrema.berry.simple.SimpletonServer;
import com.ettrema.common.Service;
import com.ettrema.http.fs.FileSystemResourceFactory;
import com.ettrema.http.fs.FsMemoryLockManager;
import com.ettrema.http.fs.SimpleSecurityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class BerryServer {
    public static void main(String[] args) {
        BerryServer startup = new BerryServer();
        startup.start("C:\\dev\\user\\perso");
    }


    public BerryServer() {
    }


    public void start(String rootDirPath) {

        FileSystemResourceFactory fileSystemResourceFactory = new FileSystemResourceFactory(new File(rootDirPath),
                                                                                            getSecurityManager(), "/");
        fileSystemResourceFactory.setLockManager(new FsMemoryLockManager());

        HttpManager httpManager = new HttpManager(fileSystemResourceFactory);
        List<Service> httpAdapters = new ArrayList<Service>();
        Http11ResponseHandler responseHandler = httpManager.getResponseHandler();

        SimpletonServer simpletonServer = new SimpletonServer(100, 20, responseHandler);
        simpletonServer.setHttpPort(80);
        httpAdapters.add(simpletonServer);
        Berry berry = new Berry(httpManager, httpAdapters);
        berry.start();
    }


    private SimpleSecurityManager getSecurityManager() {
        Map<String, String> name = new HashMap<String, String>();
        name.put("tomcat", "tomcat");

        return new SimpleSecurityManager("admin", name) {
            @Override
            public boolean authorise(Request request, Method method, Auth auth, Resource resource) {
                return true;
            }
        };
    }
}
