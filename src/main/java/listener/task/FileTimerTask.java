/*
 * Copyright (c) 2020.
 */

package listener.task;

import java.io.File;
import java.util.TimerTask;

/**
 * 这是一个文件的TimerTask，需要继承TimerTask并实现执行方法（Run）
 * @author 志军
 */
public class FileTimerTask extends TimerTask {

    private String path;

    public FileTimerTask() {
    }

    public FileTimerTask(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        System.out.println("文件管理开始=========");
        path = path + System.getProperty("file.separator") + "pdf";
        System.out.println("path" + path);
        File file = new File(path);
        runTask(file);
    }

    private void runTask(File file) {
        System.out.println("开始执行任务，要删除的文件名为" + (file.exists() ? file.getName() : "我是不存在的"));
    }
}
