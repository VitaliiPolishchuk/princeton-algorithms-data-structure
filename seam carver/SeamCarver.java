import edu.princeton.cs.algs4.*;

import java.awt.*;

public class SeamCarver {

    Picture picture;

    double energy[][];

    SeamCarver(Picture picture){
        this.picture = picture;

        this.setEnergy();
    }

    public Picture picture(){
        return this.picture;
    }

    public int height(){
        return picture.height();
    }

    public int width(){
        return picture.width();
    }

//    get color energy
    public double energy(int x, int y){

        if(x+1 > 220){
            int i = 0;
        }

        if(x - 1 < 0 || x + 1 >= this.height() ||
           y - 1 < 0 || y + 1 >= this.width()){
            return 1000;
        } else{
            double gradientX = this.gradient(x,     y - 1, x,     y + 1);
            double gradientY = this.gradient(x - 1, y,     x + 1, y);

            return (double)Math.sqrt(gradientX + gradientY);
        }
    }

    public int[] findVerticalSeam() { // sequence of indices for horizontal seam
        EdgeWeightedDigraph edgeWeightedDigraph = createVerticalEdgeWeightedDigraph();

        DijkstraChosenVertex dijkstraChosenVertex = new DijkstraChosenVertex(edgeWeightedDigraph, width());

        int vertexStartMinPath = vertexVerticalDigraph(0, 0);
        int vertexFinishMinPath = vertexVerticalDigraph(height() - 1, 0);

        for(int indexTopWidth = 0; indexTopWidth < this.width(); indexTopWidth++){
            int currentVertexStart = vertexVerticalDigraph(0,indexTopWidth);
            for(int indexBottomWidth = 0; indexBottomWidth < this.width(); indexBottomWidth++){
                int currentVertexFinish = vertexVerticalDigraph(height() - 1, indexBottomWidth);

                if(dijkstraChosenVertex.dist(vertexStartMinPath,vertexFinishMinPath) > dijkstraChosenVertex.dist(currentVertexStart, currentVertexFinish)){
                    vertexStartMinPath = currentVertexStart;
                    vertexFinishMinPath = currentVertexFinish;
                }
            }
        }

        int[] minPath = new int[height()];
        int counter = 0;

        boolean firstEdge = true;
        for(DirectedEdge directedEdge : dijkstraChosenVertex.path(vertexStartMinPath, vertexFinishMinPath)){
            if(firstEdge){
                minPath[counter++] = directedEdge.from() % width();
                minPath[counter++] = directedEdge.to() % width();
                firstEdge = false;
            } else {
                minPath[counter++] = directedEdge.to() % width();
            }
        }

        return minPath;
    }

    public int [] findHorizontalSeam(){
        EdgeWeightedDigraph edgeWeightedDigraph = createHorizontalEdgeWeightedDigraph();

        DijkstraChosenVertex dijkstraChosenVertex = new DijkstraChosenVertex(edgeWeightedDigraph, height());

        int vertexStartMinPath = vertexHorizontalDigraph(0, 0);
        int vertexFinishMinPath = vertexHorizontalDigraph(0, width() - 1);

        for(int indexLeftHeight = 0; indexLeftHeight < this.height(); indexLeftHeight++){
            int currentVertexStart = vertexHorizontalDigraph(indexLeftHeight, 0);
            for(int indexRightHeight = 0; indexRightHeight < this.height(); indexRightHeight++){
                int currentVertexFinish = vertexHorizontalDigraph(indexRightHeight, width() - 1);

                if(dijkstraChosenVertex.dist(vertexStartMinPath,vertexFinishMinPath) > dijkstraChosenVertex.dist(currentVertexStart, currentVertexFinish)){
                    vertexStartMinPath = currentVertexStart;
                    vertexFinishMinPath = currentVertexFinish;
                }
            }
        }

        int[] minPath = new int[width()];
        int counter = 0;

        boolean firstEdge = true;
        for(DirectedEdge directedEdge : dijkstraChosenVertex.path(vertexStartMinPath, vertexFinishMinPath)){
            if(firstEdge){
                minPath[counter++] = directedEdge.from() % height();
                minPath[counter++] = directedEdge.to() % height();
                firstEdge = false;
            } else {
                minPath[counter++] = directedEdge.to() % height();
            }
        }

        return minPath;
        }

    public    void removeVerticalSeam(int[] seam){
        Picture newPicture = new Picture(width() - 1, height());

        for(int i = 0; i < height(); i++){
            int regress = 0;
            for(int j = 0; j < width(); j++){
                if(j == seam[i]){
                    regress = 1;
                } else {
                    newPicture.set(j - regress, i, picture.get(j, i));
                }
            }
        }

        this.picture = newPicture;

        this.setEnergy();
    }

    public    void removeHorizontalSeam(int[] seam){
        Picture newPicture = new Picture(width(), height() - 1);

        for(int i = 0; i < width(); i++){
            int regress = 0;
            for(int j = 0; j < height(); j++){
                if(j == seam[i]){
                    regress = 1;
                } else {
                    newPicture.set(j - regress, i, picture.get(j, i));
                }
            }
        }

        this.picture = newPicture;

        this.setEnergy();
    }

    private void setEnergy(){
        energy = new double[picture.height()][picture.width()];
        for(int i = 0; i < picture.height(); i++){
            for(int j = 0; j < picture.width(); j++){
                energy[i][j] = energy(i, j);
            }
        }
    }

    private EdgeWeightedDigraph createVerticalEdgeWeightedDigraph(){
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(this.height() * this.width());

        for(int indexHeight = 0; indexHeight < this.height(); indexHeight++){
            for(int indexWeigth = 0; indexWeigth < this.width(); indexWeigth++){
                if(indexHeight + 1 < this.height() && indexWeigth - 1 > 0) { //left vertex belong current
                    DirectedEdge leftDirectedEdge = new DirectedEdge(vertexVerticalDigraph(indexHeight, indexWeigth),
                                                                     vertexVerticalDigraph(indexHeight + 1, indexWeigth - 1),
                                                                     energy[indexHeight + 1][indexWeigth - 1]);
                    edgeWeightedDigraph.addEdge(leftDirectedEdge);
                }
                if(indexHeight + 1 < this.height()) { //vertex belong current
                    DirectedEdge centralDirectedEdge = new DirectedEdge(vertexVerticalDigraph(indexHeight, indexWeigth),
                                                                        vertexVerticalDigraph(indexHeight + 1, indexWeigth),
                                                                        energy[indexHeight + 1][ indexWeigth]);
                    edgeWeightedDigraph.addEdge(centralDirectedEdge);
                }
                if(indexHeight + 1 < this.height() && indexWeigth + 1 < this.width()) { //right vertex belong current
                    DirectedEdge rightDirectedEdge = new DirectedEdge(vertexVerticalDigraph(indexHeight, indexWeigth),
                                                                      vertexVerticalDigraph(indexHeight + 1, indexWeigth + 1),
                                                                      energy[indexHeight + 1][indexWeigth + 1]);
                    edgeWeightedDigraph.addEdge(rightDirectedEdge);
                }
            }
        }

        return edgeWeightedDigraph;
    }

    private EdgeWeightedDigraph createHorizontalEdgeWeightedDigraph(){
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(this.height() * this.width());

        for(int indexWidth = 0; indexWidth < this.width(); indexWidth++){
            for(int indexHeight = 0; indexHeight < this.height(); indexHeight++){
                if(indexWidth + 1 < this.width() && indexHeight - 1 > 0) { //top vertex right current
                    DirectedEdge topDirectedEdge = new DirectedEdge(vertexHorizontalDigraph(indexHeight, indexWidth), vertexHorizontalDigraph(indexHeight - 1, indexWidth + 1), energy[indexHeight - 1][ indexWidth + 1]);
                    edgeWeightedDigraph.addEdge(topDirectedEdge);
                }
                if(indexWidth + 1 < this.width()) { //vertex belong current
                    DirectedEdge centralDirectedEdge = new DirectedEdge(vertexHorizontalDigraph(indexHeight, indexWidth), vertexHorizontalDigraph(indexHeight, indexWidth + 1), energy[indexHeight][indexWidth + 1]);
                    edgeWeightedDigraph.addEdge(centralDirectedEdge);
                }
                if(indexWidth + 1 < this.width() && indexHeight + 1 < this.height()) { //right vertex belong current
                    DirectedEdge bottomDirectedEdge = new DirectedEdge(vertexHorizontalDigraph(indexHeight, indexWidth), vertexHorizontalDigraph(indexHeight + 1, indexWidth + 1), energy[indexHeight + 1][indexWidth + 1]);
                    edgeWeightedDigraph.addEdge(bottomDirectedEdge);
                }
            }
        }

        return edgeWeightedDigraph;
    }

    private int vertexVerticalDigraph(int x, int y){

        return x * this.width() + y;
    }

    private int vertexHorizontalDigraph(int x, int y){

        if(y * this.height() + (x % this.height()) == 2500){
            int i =0;
        }

        return y * this.height() + (x % this.height());
    }

//    take coordinate of two cells and return gradient
    private double gradient(int x_l, int y_l,int x_n, int y_n) { //x_l is x last, x_n is x next;
        Color color_l = picture.get(y_l, x_l);
        int green_l = color_l.getGreen();
        int blue_l = color_l.getBlue();
        int red_l = color_l.getRed();

        Color color_n = picture.get(y_n, x_n);
        int green_n = color_n.getGreen();
        int blue_n = color_n.getBlue();
        int red_n = color_n.getRed();

        return Math.pow(green_l - green_n, 2) + Math.pow(red_l - red_n, 2) + Math.pow(blue_l - blue_n, 2);
    }

    public static void main(String[] args){
    }
}
