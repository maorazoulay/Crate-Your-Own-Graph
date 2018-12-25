import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class Vertex{
    double x,y;
    int id = 0;
    public static int idIncr = -1;

    public Vertex(double x, double y){
        this.x = x;
        this.y = y;
        id = idIncr++;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setX(double newX){
        x = newX;
    }

    public void setY(double newY){
        y = newY;
    }
}

class Graph{
    HashMap<Vertex, HashSet<Vertex>> graphComponents; //Main data structure
    HashMap<Integer, HashSet<Vertex>> seperateComponents; //To store the seperate components of the graph
    Vertex identifiedVertex = new Vertex(0,0);
    Vertex point1;
    Vertex point2;
 
    public Graph() {
        graphComponents = new HashMap<>();
    }

    public Graph(GG9364 gui){
        graphComponents = new HashMap<>();
       //mainGUI = gui;
    }

    public boolean hasVertex(Vertex clickedVertex){
        Iterator<Vertex> it =  graphComponents.keySet().iterator();
        while (it.hasNext()) {      //iterate through the values and check if clicked vertex is within range
            Vertex listVertex = it.next();
            double distance = ((listVertex.getX() - clickedVertex.getX()) * (listVertex.getX() - clickedVertex.getX()) +(listVertex.getY() - clickedVertex.getY()) * (listVertex.getY() - clickedVertex.getY()));
            if(distance <= 20){
                System.out.println("vertex exists");
                identifiedVertex = listVertex;
                return true;
            }
        }
        return false;
    }

    public void addVertex(Vertex v){
        graphComponents.put(v, new HashSet<>());
        System.out.println("vertex " + v.id + " added");
    }

    public void removeVertex(Vertex v){
        graphComponents.remove(v);
        graphComponents.keySet().forEach((vertex) -> {
            graphComponents.get(vertex).remove(v);
        });
        System.out.println(v.id + " has been removed");
    }

    public void addEdge(Vertex key, Vertex value){
        graphComponents.get(key).add(value);
        graphComponents.get(value).add(key);
        System.out.println("Edge added");
    }
    public boolean hasEdge(Vertex vertex){
        for(Vertex key: graphComponents.keySet()){
            for(Vertex value: graphComponents.get(key)){
                double distance = Math.abs((value.y - key.y) * vertex.x - (value.x - key.x) * vertex.y + value.x*key.y - value.y*key.x)  //denominator
                        / Math.sqrt(Math.pow((value.y-key.y),2) + Math.pow(value.x-key.y, 2)); //numerator
                if(distance <= 2){
                    point1 = key;
                    point2 = value;
                    return true;
                }
            }
        }
        return false;
    }

    public void removeEdge(Vertex keyVertex, Vertex valueVertex){
        graphComponents.get(keyVertex).remove(valueVertex);
        graphComponents.get(valueVertex).remove(keyVertex);
    }

    public void addAllEdges(){
        HashSet<Vertex> allKeys = new HashSet<>();
        for(Vertex vertex: graphComponents.keySet()){  //remove key and its values and replace it with same key but a with new HashSet
            allKeys.add(vertex);
        }
        System.out.println(allKeys);
        for(Vertex vertex: graphComponents.keySet()){
            graphComponents.replace(vertex, allKeys); //replace key's values with a HashSet containing all other keys
        }
    }

    public void moveVertex(Vertex oldVertex, Vertex newVertex){
        HashSet<Vertex> temp = graphComponents.get(oldVertex);
        graphComponents.remove(oldVertex);
        graphComponents.put(newVertex, temp);
        graphComponents.keySet().forEach((vertex) ->{
            if(graphComponents.get(vertex).remove(oldVertex)){
                graphComponents.get(vertex).add(newVertex);
            }
        });
    }

    public boolean isConnectedKeys(Vertex key1, Vertex key2){
        return graphComponents.get(key2).contains(key1);
    }

    public boolean isOneComponent(boolean[] hasVisited){
        for (int i = 1; i <= hasVisited.length; i++) {
            if(hasVisited[i] == false)
                return false;
        }
        return true;
    }

    public void numOfComponents(){
        HashMap<Vertex, Boolean> hasVisited = null;
        boolean put = false;

        for(Vertex vertex: graphComponents.keySet()){
            hasVisited.put(vertex, false);  //gives me a NullPointerException
        }
//        for(Vertex vertex: graphComponents.keySet()){
//            DFS(hasVisited, vertex);
//            if(isOneComponent(hasVisited)){
//                System.out.println("1 component");
//                return;
//            }
//        }
        return;
    }

        private void DFS(HashMap<Vertex, Boolean> hasVisited, Vertex v) {
            if(hasVisited.get(v)) {
    		return;
    	    }
            hasVisited.put(v, true);

            Iterator<Vertex> it = graphComponents.get(v).iterator();
            while(it.hasNext()) {
                Vertex temp = it.next();
                DFS(hasVisited, temp);
            }
        }
}


class ButtonsHandler implements EventHandler<ActionEvent>{
    GG9364 myGUI;
    Graph g = new Graph();
    ArrayList<Vertex> waitingVertices = new ArrayList<>();
    ArrayList<Vertex> toBeMovedVertices = new ArrayList<>();

    public ButtonsHandler(GG9364 gui){
        myGUI = gui;
    }

    public void reDraw(){
        myGUI.gc.clearRect(0, 0, myGUI.canvas.getWidth(), myGUI.canvas.getHeight());
        myGUI.gc.setFill(Color.WHITE);
        myGUI.gc.fillRect(0, 0, myGUI.canvas.getWidth(), myGUI.canvas.getHeight());
        if(myGUI.gc.getFill().equals(Color.WHITE))
            myGUI.gc.setFill(Color.RED);
        for(Vertex vertex : g.graphComponents.keySet()){
            myGUI.gc.setFill(Color.RED);
            myGUI.gc.fillOval(vertex.getX()-5.0, vertex.getY()-5.0, 10, 10);
            //System.out.print("(" + vertex.x + "," + vertex.y + ")  ");
        }
        for(Vertex vertex: g.graphComponents.keySet()){
            for(Vertex valueVertex: g.graphComponents.get(vertex)){
                myGUI.gc.setStroke(Color.BLUE);
                myGUI.gc.strokeLine(vertex.getX(), vertex.getY(), valueVertex.getX(), valueVertex.getY());
            }
        }
        //System.out.println(g.graphComponents.keySet()); //for testing
    }
    public boolean isFull(){
        return (waitingVertices.size() == 2);
    }
    public boolean isFull2(){
        return (toBeMovedVertices.size() == 2);
    }

    @Override
    public void handle(ActionEvent event) {
        myGUI.canvas.setOnMouseClicked(e -> {
        Vertex vertex = new Vertex(e.getX(), e.getY());

        if(myGUI.addVertex.isSelected()){
            clearButtons();
            myGUI.addVertex.setSelected(true);
            if(!g.hasVertex(vertex)){
                g.addVertex(vertex);
                reDraw();
            }
        }

        else if(myGUI.addEdge.isSelected()){
            clearButtons();
            myGUI.addEdge.setSelected(true);
            if(g.hasVertex(vertex)){
                vertex = g.identifiedVertex;
                waitingVertices.add(vertex);
                myGUI.gc.setFill(Color.GREEN);
                myGUI.gc.fillOval(vertex.getX()-5.0, vertex.getY()-5.0, 10, 10);
                if(isFull()){
                    g.addEdge(waitingVertices.get(0), waitingVertices.get(1));
                    waitingVertices = new ArrayList<>();
                    reDraw();
                }
            }
        }
        else if (myGUI.removeVertex.isSelected()){
            clearButtons();
            myGUI.removeVertex.setSelected(true);
            if(g.hasVertex(vertex)){
                g.removeVertex(g.identifiedVertex);
                reDraw();
            }
        }

        else if(myGUI.removeEdge.isSelected()){
            clearButtons();
            myGUI.removeEdge.setSelected(true);
            if(g.hasEdge(vertex)){
                g.removeEdge(g.point1, g.point2);
                reDraw();
            }
        }

        else if(myGUI.moveVertex.isSelected()){
            clearButtons();
            myGUI.moveVertex.setSelected(true);
            if(g.hasVertex(vertex)){
                vertex = g.identifiedVertex;
                toBeMovedVertices.add(vertex);
                myGUI.gc.setFill(Color.GREEN);
                myGUI.gc.fillOval(vertex.getX()-5.0, vertex.getY()-5.0, 10, 10);
            }
            if(!toBeMovedVertices.get(0).equals(vertex))
                toBeMovedVertices.add(vertex);
            if(isFull2()){
                  g.moveVertex(toBeMovedVertices.get(0), toBeMovedVertices.get(1));
                  toBeMovedVertices = new ArrayList<>();
                  reDraw();
            }
        }
        });

        myGUI.btn1.setOnMouseClicked(e ->{
           g.addAllEdges();
           reDraw();
        });

        myGUI.btn2.setOnMouseClicked(e ->{
            for(Vertex key1: g.graphComponents.keySet()){
                myGUI.gc.setFill(Color.color(Math.random(), Math.random(), Math.random()));
                myGUI.gc.setStroke(myGUI.gc.getFill());
                for(Vertex key2: g.graphComponents.keySet()){
                   // for(Vertex key2: g.graphComponents.keySet()){
                        if(g.isConnectedKeys(key1, key2)){
                            myGUI.gc.fillOval(key1.getX()-5.0, key1.getY()-5.0, 10, 10);
                            myGUI.gc.fillOval(key2.getX()-5.0, key2.getY()-5.0, 10, 10);
                            myGUI.gc.strokeLine(key1.getX(), key1.getY(), key2.getX(), key2.getY());
                        }
                   // }


                }
                //myGUI.gc.fillOval(key.getX()-5.0, key.getY()-5.0, 10, 10);
            }
        });

        myGUI.btn3.setOnAction(e ->{
            g.numOfComponents();
            //int currentNumOfComponents = g.numOfComponents(g.graphComponents);
            //System.out.println(currentNumOfComponents + " num of components");
//            for(Vertex vertex: g.graphComponents.keySet()){
//                g.graphComponents.remove(vertex);
//                if(g.numOfComponents(g.graphComponents) > currentNumOfComponents){
//                    g.graphComponents.put(vertex, g.graphComponents.get(vertex));
//                    myGUI.gc.setFill(Color.CHARTREUSE);
//                    myGUI.gc.fillOval(vertex.getX()-5.0, vertex.getY()-5.0, 10, 10);
//                }
//            }
        });
    }
    public void clearButtons(){
        myGUI.addVertex.setSelected(false);
        myGUI.addEdge.setSelected(false);
        myGUI.removeVertex.setSelected(false);
        myGUI.removeEdge.setSelected(false);
        myGUI.moveVertex.setSelected(false);
    }
}

public class GG9364 extends Application {
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
