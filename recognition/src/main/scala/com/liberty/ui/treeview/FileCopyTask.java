package com.liberty.ui.treeview;

import javafx.concurrent.Task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * File copy Task
 * 
 * @author tomo
 */
public class FileCopyTask extends Task<Void> {
    private Path source;
    private Path target;

    public FileCopyTask(Path source, Path target) {
        this.source = source;
        this.target = target;
    }
    
    @Override
    protected Void call() throws Exception {
        Files.copy(this.source, this.target, StandardCopyOption.REPLACE_EXISTING);
        return null;
    }
}
