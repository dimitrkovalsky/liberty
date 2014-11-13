package com.liberty.controllers;

import com.liberty.common.ProjectConfig;
import com.liberty.treeview.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Maxxis
 * Date: 09.11.2014
 * Time: 12:49
 */
public class Controller {
    private Path rootPath;
    private ExecutorService service = Executors.newFixedThreadPool(3);
    private StringProperty messageProp= new SimpleStringProperty();
    @FXML
    private TreeView<PathItem> locationTreeView;
    @FXML
    private AnchorPane filesViewer;

    // the initialize method is automatically invoked by the FXMLLoader - it's magic
//    public void initialize() {
//        scanProjectDirectory();
//    }

    public void scanProjectDirectory(Stage stage) {
        rootPath = Paths.get(ProjectConfig.projectPath());
        PathItem pathItem = new PathItem(rootPath);
//        locationTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
//                    if (mouseEvent.getClickCount() == 2) {
//                        System.out.println("Double clicked");
//
//                    }
//                }
//            }
//        });
        locationTreeView.setRoot(createNode(pathItem));
        locationTreeView.setEditable(false);
        locationTreeView.setCellFactory((TreeView<PathItem> p) -> {
            final PathTreeCell cell = new PathTreeCell(stage, messageProp);
            setDragDropEvent(stage, cell);
            return cell;
        });

    }

    public void setCodeStyleArea(String sampleCode) {
        CodeArea codeArea = new CodeArea();
        String stylesheet = getClass().getResource("java-keywords.css").toExternalForm();
        IntFunction<String> format = (digits -> " %" + digits + "d ");

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea, format, stylesheet));
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });
        codeArea.replaceText(0, 0, sampleCode);
        filesViewer.getChildren().add(codeArea);
        filesViewer.getStylesheets().add(stylesheet);
    }

    private void setDragDropEvent(Stage stage, final PathTreeCell cell) {
        // The drag starts on a gesture source
        cell.setOnDragDetected(event -> {
            TreeItem<PathItem> item = cell.getTreeItem();
            if (item != null && item.isLeaf()) {
                Dragboard db = cell.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                List<File> files = Arrays.asList(cell.getTreeItem().getValue().getPath().toFile());
                content.putFiles(files);
                db.setContent(content);
                event.consume();
            }
        });
        // on a Target
        cell.setOnDragOver(event -> {
            TreeItem<PathItem> item = cell.getTreeItem();
            if ((item != null && !item.isLeaf()) &&
                    event.getGestureSource() != cell &&
                    event.getDragboard().hasFiles()) {
                Path targetPath = cell.getTreeItem().getValue().getPath();
                PathTreeCell sourceCell = (PathTreeCell) event.getGestureSource();
                final Path sourceParentPath = sourceCell.getTreeItem().getValue().getPath().getParent();
                if (sourceParentPath.compareTo(targetPath) != 0) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
            }
            event.consume();
        });
        // on a Target
        cell.setOnDragEntered(event -> {
            TreeItem<PathItem> item = cell.getTreeItem();
            if ((item != null && !item.isLeaf()) &&
                    event.getGestureSource() != cell &&
                    event.getDragboard().hasFiles()) {
                Path targetPath = cell.getTreeItem().getValue().getPath();
                PathTreeCell sourceCell = (PathTreeCell) event.getGestureSource();
                final Path sourceParentPath = sourceCell.getTreeItem().getValue().getPath().getParent();
                if (sourceParentPath.compareTo(targetPath) != 0) {
                    cell.setStyle("-fx-background-color: powderblue;");
                }
            }
            event.consume();
        });
        // on a Target
        cell.setOnDragExited(event -> {
            cell.setStyle("-fx-background-color: white");
            event.consume();
        });
        // on a Target
        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                final Path source = db.getFiles().get(0).toPath();
                final Path target = Paths.get(
                        cell.getTreeItem().getValue().getPath().toAbsolutePath().toString(),
                        source.getFileName().toString());
                if (Files.exists(target, LinkOption.NOFOLLOW_LINKS)) {
                    Platform.runLater(() -> {
                        BooleanProperty replaceProp = new SimpleBooleanProperty();
                        CopyModalDialog dialog = new CopyModalDialog(stage, replaceProp);
                        replaceProp.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    FileCopyTask task = new FileCopyTask(source, target);
                                    service.submit(task);
                                }
                            }
                        });
                    });
                } else {
                    FileCopyTask task = new FileCopyTask(source, target);
                    service.submit(task);
                    task.setOnSucceeded(value -> {
                        Platform.runLater(() -> {
                            TreeItem<PathItem> item = PathTreeItem.createNode(new PathItem(target));
                            cell.getTreeItem().getChildren().add(item);
                        });
                    });
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        // on a Source
        cell.setOnDragDone(event -> {
            ;
        });
    }

    private TreeItem<PathItem> createNode(PathItem pathItem) {
        return PathTreeItem.createNode(pathItem);
    }

    private static final String sampleCode = String.join("\n", new String[] {
            "package com.example;",
            "",
            "import java.util.*;",
            "",
            "public class Foo extends Bar implements Baz {",
            "",
            "   public static void main(String[] args) {",
            "       for(String arg: args) {",
            "           if(arg.length() != 0)",
            "               System.out.println(arg);",
            "           else",
            "               System.err.println(\"Warning: empty string as argument\");",
            "       }",
            "   }",
            "",
            "}"
    });

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"]|\\\")*\"";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
    );
}
