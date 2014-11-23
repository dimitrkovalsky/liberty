package com.liberty.ui.treeview;

import com.liberty.Main;
import com.liberty.ui.controllers.JavaController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTreeCell extends TreeCell<PathItem>{
    private TextField textField;
    private Path editingPath;
    private StringProperty messageProp;
    private ContextMenu dirMenu = new ContextMenu();
    private ContextMenu fileMenu = new ContextMenu();

    public PathTreeCell(final Stage owner, final StringProperty messageProp) {
        this.messageProp = messageProp;
        MenuItem expandMenu = new MenuItem("Expand");
        expandMenu.setOnAction((ActionEvent event) -> {
            getTreeItem().setExpanded(true);
        });
        MenuItem expandAllMenu = new MenuItem("Expand All");
//        expandAllMenu.setGraphic(new ImageView(
//                new Image(PathTreeCell.class.getResourceAsStream("folder-open.png"))
//        ));
        expandAllMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                expandTreeItem(getTreeItem());
            }
            private void expandTreeItem(TreeItem<PathItem> item) {
                if (item.isLeaf()){
                    return;
                }
                item.setExpanded(true);
                ObservableList<TreeItem<PathItem>> children = item.getChildren();
                children.stream().filter(child -> (!child.isLeaf()))
                    .forEach(child -> expandTreeItem(child));
            }
        });
        MenuItem addMenu = new MenuItem("Add Directory");
        addMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Path newDir = createNewDirectory();
                if (newDir != null) {
                    TreeItem<PathItem> addItem = PathTreeItem.createNode(new PathItem(newDir));
                    getTreeItem().getChildren().add(addItem);
                }
            }
            private Path createNewDirectory() {
                Path newDir = null;
                while (true) {
                    Path path = getTreeItem().getValue().getPath();
                    newDir = Paths.get(path.toAbsolutePath().toString(), "newDirectory" + String.valueOf(getItem().getCountNewDir()));
                    try {
                        Files.createDirectory(newDir);
                        break;
                    } catch (FileAlreadyExistsException ex) {
                        continue;
                    } catch (IOException ex) {
                        cancelEdit();
                        messageProp.setValue(String.format("Creating directory(%s) failed", newDir.getFileName()));
                        break;
                    }
                }
                    return newDir;
            }
        });
        MenuItem deleteMenu =new MenuItem("Delete");
        deleteMenu.setOnAction((ActionEvent event) -> {
            ObjectProperty<TreeItem<PathItem>> prop = new SimpleObjectProperty<>();
            new ModalDialog(owner, getTreeItem(), prop);
            prop.addListener((ObservableValue<? extends TreeItem<PathItem>> ov, TreeItem<PathItem> oldItem, TreeItem<PathItem> newItem) -> {
                try {
                    Files.walkFileTree(newItem.getValue().getPath(), new VisitorForDelete());
                    if (getTreeItem().getParent() == null){
                        // when the root is deleted how to clear the TreeView???
                    } else {
                        getTreeItem().getParent().getChildren().remove(newItem);
                    }
                } catch (IOException ex) {
                    messageProp.setValue(String.format("Deleting %s failed", newItem.getValue().getPath().getFileName()));
                }
            });
        });
        MenuItem openMenu = new MenuItem("Open");
        openMenu.setOnAction((ActionEvent event) -> {
            Main.getLoader().<JavaController>getController().setCodeStyleArea(readFile(getItem().getPath().toFile()));
        });
        dirMenu.getItems().addAll(expandMenu, expandAllMenu, deleteMenu, addMenu);
        fileMenu.getItems().addAll(openMenu, deleteMenu);
    }

    public static String readFile(File file) {

        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileData.toString();
    }

    @Override
    protected void updateItem(PathItem item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
                if (!getTreeItem().isLeaf()) {
                    setContextMenu(dirMenu);
                } else {
                    setContextMenu(fileMenu);
                }
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null){
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
        if (getItem() == null) {
            editingPath = null;
        } else {
            editingPath =getItem().getPath();
        }
    }

    @Override
    public void commitEdit(PathItem pathItem) {
        // rename the file or directory
        if (editingPath != null) {
            try {
                Files.move(editingPath, pathItem.getPath());
            } catch (IOException ex) {
                cancelEdit();
                messageProp.setValue(String.format("Renaming %s filed", editingPath.getFileName()));
            }
        }
        super.commitEdit(pathItem);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getString());
        setGraphic(null);
    }

    private String getString() {
        return getItem().toString();
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased((KeyEvent t) -> {
            if (t.getCode() == KeyCode.ENTER){
                Path path = Paths.get(getItem().getPath().getParent().toAbsolutePath().toString(), textField.getText());
                commitEdit(new PathItem(path));
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }
}
