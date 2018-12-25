
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

class ButtonsHandler implements EventHandler<ActionEvent>{
    GraphGUI myGUI;
    Graph g = new Graph();
    ArrayList<Vertex> waitingVertices = new ArrayList<>();
    ArrayList<Vertex> toBeMovedVertices = new ArrayList<>();

    public ButtonsHandler(GraphGUI gui){
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