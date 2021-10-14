package com.lagou.edu.framework.servlet;

import com.lagou.edu.framework.annotation.LgController;
import com.lagou.edu.framework.annotation.LgService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LgDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();
    private List<String> classNames = new ArrayList<>();
    private Map<String, Object> ioc = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1.获取配置项文件 SpringMvc.properties
        String propertiesConfigLocation = config.getInitParameter("propertiesConfigLocation");
        doLoadConfiguration("propertiesConfigLocation");

        // 2.扫描指定的包名，扫描注解
        doScanPackage(properties.getProperty("scanPackage"));

        // 3.将扫描到的类生成 bean 对象
        doInitInstance();

        // 4.实现 bean 对象的属性自动注入
        doFiedAutoWired();

        // 5.生成一个 HandlerMapping 处理器映射器 将配置好的 url 和 method 建立映射关系
        initHandlerMapping();

        System.out.println("==================== lgMvc 初始化成功 ====================");
    }

    private void doFiedAutoWired() {

    }

    /**
     * 基于 classNames 缓存的类的全限定名，以及反射技术，完成对象的创建和管理
     */
    private void doInitInstance() {
        if (classNames.isEmpty()) return;

        try {
            for (String className : classNames) {
                Class<?> aClass = Class.forName(className);
                String firstLowerSimpleName = toFirstLower(aClass.getSimpleName());

                if (aClass.isAnnotationPresent(LgController.class)) {
                    extracted(aClass, firstLowerSimpleName);

                } else if (aClass.isAnnotationPresent(LgService.class)) {
                    LgService annotation = aClass.getAnnotation(LgService.class);
                    String beanName = annotation.value();

                    if (!beanName.equals("")) {
                        ioc.put(beanName, aClass.newInstance());
                    } else {
                        beanName = firstLowerSimpleName;
                        extracted(aClass, firstLowerSimpleName);
                    }

                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        ioc.put(anInterface.getSimpleName(), ioc.get(beanName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extracted(Class<?> aClass, String firstLowerSimpleName) throws InstantiationException, IllegalAccessException {

        Object o = aClass.newInstance();
        ioc.put(firstLowerSimpleName, o);
    }

    private String toFirstLower(String simpleName) {
        char c = Character.toLowerCase(simpleName.charAt(0));
        char[] chars = simpleName.toCharArray();
        chars[0] = c;
        return new String(chars);
    }

    /**
     * 扫描包 com.lagou.demo
     *
     * @param packagePath
     */
    private void doScanPackage(String packagePath) {
        String scanPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + packagePath.replaceAll("\\.", "/");
        File pack = new File(scanPath);

        File[] files = pack.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归处理
                doScanPackage(packagePath + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }

    private void initHandlerMapping() {

    }

    private void doLoadConfiguration(String propertiesConfigLocation) {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(propertiesConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
