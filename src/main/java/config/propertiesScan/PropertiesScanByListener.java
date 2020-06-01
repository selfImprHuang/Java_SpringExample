package config.propertiesScan;

import util.FileUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 通过这个类来扫描获取所有的properties下面的文件
 * 这个监听器在Servlet上下文初始化过程中进行处理
 * @author  志军
 */
@WebListener
public class PropertiesScanByListener implements ServletContextListener {

    private Map<String, String> map = new HashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String resourcesPath = servletContext.getRealPath("/") + "WEB-INF\\classes\\cache";
        traceFilesAndGetProperties(resourcesPath);
        servletContext.setAttribute("properties", map);
        System.out.println(map);
    }

    private void traceFilesAndGetProperties(String path) {
        if (FileUtil.isDir(path)) {
            File[] files = new File(path).listFiles();
            if (files == null || files.length == 0) {
                System.out.println("文件夹是空的!");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traceFilesAndGetProperties(file2.getAbsolutePath());
                    } else {
                        //是否是配置文件
                        if (file2.getAbsolutePath().contains(".properties")) {
                            getProperties(file2.getAbsolutePath());
                        }
                    }
                }
            }
            return;
        }

        if (FileUtil.isFile(path)){
            if (new File(path).getAbsolutePath().contains(".properties")) {
                getProperties(new File(path).getAbsolutePath());
            }
            return;
        }

        throw new RuntimeException("文件不存在");
    }

    private void getProperties(String filePath) {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(filePath);
            properties.load(in);
            for (Object o : properties.keySet()) {
                String key = (String) o;
                String value = properties.getProperty(key);
                map.put(key, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e );
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("文件流关闭失败："+e.toString());
            }
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
