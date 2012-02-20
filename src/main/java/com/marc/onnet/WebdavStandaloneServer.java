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
public class WebdavStandaloneServer {
    public static void main(String[] args) {
        WebdavStandaloneServer startup = new WebdavStandaloneServer();
        String defaultContextPath = "/";
        int defaultPort = 80;

        if (args.length > 0 && args.length <= 2) {
            File rootPath = new File(args[0]);
            if (rootPath.exists()) {
                if (args.length == 2) {
                    defaultPort = Integer.parseInt(args[1]);
                }
                startup.start(defaultContextPath, defaultPort, args[0]);
            }
        }

        else {

            String msg = "Le nombre d'arguments passe est faux:"
                         + " exemple de ligne de commande : \n\tjava -jar webdav-standalone-1.0.jar \"path\\to\\share\""
                         + "\n\tjava -jar webdav-standalone-1.0.jar \"path\\to\\share\" 8080";
            throw new IllegalArgumentException(msg);
        }
    }


    public WebdavStandaloneServer() {
    }


    public void start(String contextPath, int port, String fileSystemRootPath) {

        FileSystemResourceFactory fileSystemResourceFactory
              = new FileSystemResourceFactory(new File(fileSystemRootPath), getSecurityManager(), contextPath);

        fileSystemResourceFactory.setLockManager(new FsMemoryLockManager());

        HttpManager httpManager = new HttpManager(fileSystemResourceFactory);
        List<Service> httpAdapters = new ArrayList<Service>();
        Http11ResponseHandler responseHandler = httpManager.getResponseHandler();

        SimpletonServer simpletonServer = new SimpletonServer(100, 20, responseHandler);
        simpletonServer.setHttpPort(port);
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
