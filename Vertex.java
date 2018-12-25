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