
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class Graph{
    HashMap<Vertex, HashSet<Vertex>> graphComponents; //Main data structure
    HashMap<Integer, HashSet<Vertex>> seperateComponents; //To store the seperate components of the graph
    Vertex identifiedVertex = new Vertex(0,0);
    Vertex point1;
    Vertex point2;
 
    public Graph() {
        graphComponents = new HashMap<>();
    }

    public Graph(GraphGUI gui){
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