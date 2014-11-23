package com.liberty.ui.treeview;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * this is to search file by using concurrent Task
 *
 * @author tomo
 */
public class SearchTask extends Task<Void> {
    private Path path;
    private String pattern;
    private StringProperty resultString;
    
    public SearchTask(Path path, String pattern) {
        this.path = path;
        this.pattern = pattern;
        resultString = new SimpleStringProperty();
    }

    @Override
    protected Void call() throws Exception {
        updateProgress(0, 0);
        updateMessage("searching ...");
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            int cnt = 0;
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (matcher.matches(file.getFileName())) {
                    cnt++;
                    resultString.setValue(file.toAbsolutePath().toString()); // to bind to the ListItem
                    updateMessage(String.format("%d files founded", cnt)); // to bind to the Label
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return null;
    }

    public StringProperty getResultString() {
        return resultString;
    }
}
