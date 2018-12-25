import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;

public class GraphGUI extends Application {
    RadioButton addVertex, addEdge, removeVertex,removeEdge, moveVertex;
    Button btn1, btn2, btn3, btn4;
    Canvas canvas;
    BorderPane root;
    VBox leftMenu;
    ButtonsHandler bh = new ButtonsHandler(this);
    GraphicsContext gc;
    Graph guiGraph = new Graph();
    ToggleGroup group;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graph GUI");
        leftMenu = new VBox();
        leftMenu.setSpacing(20);
        leftMenu.setPadding(new Insets(20, 0, 10, 10));
        group = new ToggleGroup();

        canvas = new Canvas(735, 460);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        root = new BorderPane();
        root.setLeft(leftMenu);
        root.setCenter(canvas);

        Scene scene = new Scene(root, 900, 460);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Buttons
        addVertex = new RadioButton("Add Vertex");
        addVertex.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        addVertex.setToggleGroup(group);
        addVertex.setOnAction(bh);

        addEdge = new RadioButton("Add Edge");
        addEdge.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        addEdge.setToggleGroup(group);
        addEdge.setOnAction(bh);

        removeVertex = new RadioButton("Remove Vertex");
        removeVertex.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        removeVertex.setToggleGroup(group);
        removeVertex.setOnAction(bh);

        removeEdge = new RadioButton("Remove Edge");
        removeEdge.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        removeEdge.setToggleGroup(group);
        removeEdge.setOnAction(bh);

        moveVertex = new RadioButton("Move Vertex");
        moveVertex.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        moveVertex.setToggleGroup(group);
        moveVertex.setOnAction(bh);

        btn1 = new Button("Add All Edges");
        btn1.setPrefSize(100, 40);
        btn1.setOnAction(bh);

        btn2 = new Button("Connected\nComponents");
        btn2.setPrefSize(100, 40);
        btn2.setOnAction(bh);

        btn3 = new Button("Show Cut\nVertices");
        btn3.setPrefSize(100, 40);
        btn3.setOnAction(bh);

        btn4 = new Button("Help");
        btn4.setPrefSize(100, 40);

        leftMenu.getChildren().addAll(addVertex, addEdge, removeVertex, removeEdge, moveVertex, btn1, btn2, btn3, btn4);

        btn4.setOnAction((ActionEvent e) -> {
            StackPane help = new StackPane();
            help.setAlignment(Pos.TOP_LEFT);
            Text tx = new Text("This is an interactive GUI which can be\nused to create graphs by adding vertices\nand connect them with edges.");
            tx.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
            tx.setFill(Color.CORNFLOWERBLUE);
            help.getChildren().add(tx);
            Stage helpStage = new Stage();
            helpStage.setScene(new Scene(help, 600, 230));
            helpStage.setTitle("Help");
            helpStage.show();
        });
   }
    public static void main(String[] args) {
        launch(args);

    }
}
