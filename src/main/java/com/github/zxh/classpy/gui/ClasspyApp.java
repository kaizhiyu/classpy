package com.github.zxh.classpy.gui;

import com.github.zxh.classpy.gui.tree.UiBuilder;
import com.github.zxh.classpy.classfile.ClassFile;
import com.github.zxh.classpy.classfile.ClassParser;
import com.github.zxh.classpy.gui.hex.ClassHex;
import java.io.File;
import java.nio.file.Files;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author zxh
 */
public class ClasspyApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //System.out.println(Font.getFamilies());
        
        
        stage.setTitle("Classpy");
        
        BorderPane root = new BorderPane();
        
        
        SplitPane mainPane = new SplitPane();
        mainPane.getItems().add(new Button("aaaaaa"));
        mainPane.getItems().add(new Button("aaaaaa"));
        mainPane.getItems().add(new Button("aaaaaa"));
        
        TextArea textArea = new TextArea("11223333\nwww");
//        textArea.setFont(Font.);
        textArea.positionCaret(2);
        textArea.selectPositionCaret(11);
        textArea.setOnMouseClicked(e -> {
            System.out.println(textArea.getSelection());
        });
        
        root.setTop(createMenuBar(stage, root));
//        root.setCenter(mainPane);
        root.setCenter(textArea);
        
        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private MenuBar createMenuBar(Stage stage, BorderPane root) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);
        MenuItem openMenuItem = new MenuItem("Open...");
        fileMenu.getItems().add(openMenuItem);
        
        openMenuItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open .class or .jar File");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CLASS", "*.class"),
                new FileChooser.ExtensionFilter("JAR", "*.jar")
            );
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                // todo
                Task<ClassFile> task = new Task<ClassFile>() {

                    @Override
                    protected ClassFile call() throws Exception {
                        byte[] x = Files.readAllBytes(file.toPath());
                        ClassFile cf = ClassParser.parse(x);
                        return cf;
                    }
                    
                };
                
                task.setOnSucceeded(e -> {
                    ClassFile cf = (ClassFile) e.getSource().getValue();
                    System.out.println(cf);
                    TreeView<?> tree = UiBuilder.build(cf);
                    root.setCenter(tree);
                    
                    ClassHex hex = new ClassHex(cf);
                    System.out.println(hex.getHexString());
                    System.out.println(hex.getAsciiString());
                    
                });
                
                task.setOnFailed(e -> {
                    System.out.println(e.getSource().getException());
                });
                
                new Thread(task).start();
            }
        });
        
        return menuBar;
    }
    
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
