package com.saleoa.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtil {
	/** jar�е��ļ�·���ָ��� */
    private static final char SLASH_CHAR = '/';
    /** �����ָ��� */
    private static final char DOT_CHAR = '.';

    /**
     * �ڵ�ǰ��Ŀ��Ѱ��ָ�����µ�������
     * 
     * @param packageName ��'.'�ָ��İ���
     * @param recursion �Ƿ�ݹ�����
     * @return �ð����µ�������
     */
    /*public static List<Class<?>> getClass(String packageName, boolean recursive) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        try {
            //��ȡ��ǰ�̵߳���װ��������Ӧ������Ӧ����Դ
            Enumeration<URL> iterator = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(DOT_CHAR, File.separatorChar));
            while (iterator.hasMoreElements()) {
                URL url = iterator.nextElement();
                String protocol = url.getProtocol();
                System.out.println(protocol);
                List<Class<?>> childClassList = Collections.emptyList();
                switch (protocol) {
                    case "file":
                        childClassList = getClassInFile(url, packageName, recursive);
                        break;
                    case "jar":
                        childClassList = getClassInJar(url, packageName, recursive);
                        break;
                    default:
                        //��ĳЩWEB������������WAR��ʱ����������TOMCATһ����WAR����ѹΪĿ¼�ģ���JBOSS7������ʹ����һ�ֽ�VFS��Э��
                        System.out.println("unknown protocol " + protocol);
                        break;
                }
                classList.addAll(childClassList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }*/

    /**
     * �ڸ������ļ����ļ�����Ѱ��ָ�����µ�������
     * 
     * @param filePath ����·��
     * @param packageName ��'.'�ָ��İ���
     * @param recursive �Ƿ�ݹ�����
     * @return �ð����µ�������
     */
    /*public static List<Class<?>> getClassInFile(String filePath, String packageName, boolean recursive) {
        Path path = Paths.get(filePath);
        return getClassInFile(path, packageName, recursive);
    }*/

    /**
     * �ڸ������ļ����ļ�����Ѱ��ָ�����µ�������
     * 
     * @param url ����ͳһ��Դ��λ��
     * @param packageName ��'.'�ָ��İ���
     * @param recursive �Ƿ�ݹ�����
     * @return �ð����µ�������
     */
    /*public static List<Class<?>> getClassInFile(URL url, String packageName, boolean recursive) {
        try {
            Path path = Paths.get(url.toURI());
            return getClassInFile(path, packageName, recursive);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }*/

    /**
     * �ڸ������ļ����ļ�����Ѱ��ָ�����µ�������
     *  
     * @param path ����·��
     * @param packageName ��'.'�ָ��İ���
     * @param recursive �Ƿ�ݹ�����
     * @return �ð����µ�������
     */
    /*public static List<Class<?>> getClassInFile(Path path, String packageName, boolean recursive) {
        if (!Files.exists(path)) {
            return Collections.emptyList();
        }
        List<Class<?>> classList = new ArrayList<Class<?>>();
        if (Files.isDirectory(path)) {
            if (!recursive) {
                return Collections.emptyList();
            }
            try {
                //��ȡĿ¼�µ������ļ�
                Stream<Path> stream = Files.list(path);
                Iterator<Path> iterator = stream.iterator();
                while (iterator.hasNext()) {
                    classList.addAll(getClassInFile(iterator.next(), packageName, recursive));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //���ڴ�����ļ����������·��, ����Ҫ�õ��ļ���ʵ��·��, �����������IOException
                path = path.toRealPath();
                String pathStr = path.toString();
                //�����õ���һ���"aa:\bb\...\cc.class"��ʽ���ļ���, Ҫȥ��ĩβ�����ͺ�׺(.class)
                int lastDotIndex = pathStr.lastIndexOf(DOT_CHAR);
                //Class.forNameֻ����ʹ����'.'�ָ�����������ʽ
                String className = pathStr.replace(File.separatorChar, DOT_CHAR);
                //��ȡ��������ʼλ��
                int beginIndex = className.indexOf(packageName);
                if (beginIndex == -1) {
                    return Collections.emptyList();
                }
                className = lastDotIndex == -1 ? className.substring(beginIndex) : className.substring(beginIndex, lastDotIndex);
                classList.add(Class.forName(className));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classList;
    }*/

    /**
     * �ڸ�����jar����Ѱ��ָ�����µ�������
     *  
     * @param filePath ����·��
     * @param packageName ��'.'�ָ��İ���
     * @param recursive �Ƿ�ݹ�����
     * @return �ð����µ�������
     */
    public static List<Class<?>> getClassInJar(String filePath, String packageName, boolean recursive) {
        try {
            JarFile jar = new JarFile(filePath);
            return getClassInJar(jar, packageName, recursive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * �ڸ�����jar����Ѱ��ָ�����µ�������
     *  
     * @param url jar����ͳһ��Դ��λ��
     * @param packageName ��'.'�ָ��İ���
     * @param recursive �Ƿ�ݹ�����
     * @return �ð����µ�������
     */
    public static List<Class<?>> getClassInJar(URL url, String packageName, boolean recursive) {
        try {
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            return getClassInJar(jar, packageName, recursive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * �ڸ�����jar����Ѱ��ָ�����µ�������
     *  
     * @param jar jar����
     * @param packageName ��'.'�ָ��İ���
     * @param recursive �Ƿ�ݹ�����
     * @return �ð����µ�������
     */
    public static List<Class<?>> getClassInJar(JarFile jar, String packageName, boolean recursive) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        //�õ�������ݹ�õ���jar�������е�Ŀ¼���ļ�
        Enumeration<JarEntry> iterator = jar.entries();
        while (iterator.hasMoreElements()) {
            //�����õ���һ���"aa/bb/.../cc.class"��ʽ��Entry�� "��·��"
            JarEntry jarEntry = iterator.nextElement();
            if (!jarEntry.isDirectory()) {
                String name = jarEntry.getName();
                //�����õ����ļ�,Ҫȥ��ĩβ��.class
                int lastDotClassIndex = name.lastIndexOf(".class");
                if(lastDotClassIndex != -1) {
                    int lastSlashIndex = name.lastIndexOf(SLASH_CHAR);
                    name = name.replace(SLASH_CHAR, DOT_CHAR);
                    if(name.startsWith(packageName)) {
                        if(recursive || packageName.length() == lastSlashIndex) {
                            String className = name.substring(0, lastDotClassIndex);
                            System.out.println(className);
                            try {
                                classList.add(Class.forName(className));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return classList;
    }
}
