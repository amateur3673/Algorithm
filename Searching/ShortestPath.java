import java.io.*;
import java.util.ArrayList;

//this program is implement 3 fundamental algorithm for finding shortest path between 2 points in
//a Graph
//Because Undirect Graph is just a case of Direct Graph, so in this program, we only implement the
//algorithm in Direct Graph
abstract class Graph{
    //structure of class
    //we use weight matrice to simulate the Graph
    int m;//number of edges
    int n;//number of vertices
    int S;//starting vertice
    int F;//finishing vertice
    int[][] WeightMatrice;//matrice of weight between 2 edges
    int[] Trace;//Use for Tracing purpose
    int[] d;//d[i] is distance from S to i
    /* we assume that:
    2 vertices i,j that is connected then WeightMatrice[i,j]=weight of edge (i,j)
    WeightMatrice[i,i]=0
    WeightMatrice[i,j]=20000 if (i,j) is not connected
    */
    /*
    We read the data from file input.txt, here is the structure of file input.txt
    The first line conprises 4 numbers: n,m,S,F are seperated by a space
    The following m lines comprises of 3 numbers: i,j,w where i,j are the edges from i to j, w is
    the weight, they are all space-seprerated. 
    */
    /*We Write data to file output.txt
    The first line we write the shortest distance from S to F
    After that , we write the route*/
    /*Class method
    readInformation(): read data from input.txt
    display(): just for check
    writeInformation(): write to output.txt
    abstract method: findPath(): each algorithm will overwrite this method*/
    void readInformation(){
        try{
            File inFile=new File("input.txt");
            FileReader filereader=new FileReader(inFile);
            BufferedReader reader=new BufferedReader(filereader);
            String myString=reader.readLine();
            String[] arrString=myString.split(" ");
            n=Integer.parseInt(arrString[0]);
            m=Integer.parseInt(arrString[1]);
            S=Integer.parseInt(arrString[2]);
            F=Integer.parseInt(arrString[3]);
            //initialize arr
            WeightMatrice=new int[n][n];
            //we first convert each element in arr[i][j]=20000 with i!=j
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    if(i!=j)WeightMatrice[i][j]=20000;
                }
            }
            //read data from input
            for(int i=0;i<m;i++){
                myString=reader.readLine();
                arrString=myString.split(" ");
                int p=Integer.parseInt(arrString[0]);
                int q=Integer.parseInt(arrString[1]);
                int w=Integer.parseInt(arrString[2]);
                WeightMatrice[p][q]=w;
            }
            reader.close();
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    void displayGraph(){
        System.out.println(m);
        System.out.println(n);
        System.out.println(F);
        System.out.println(S);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                System.out.print(WeightMatrice[i][j]+" ");
            }
            System.out.print("\n");
        }
    }
    abstract void findPath();
    void TracePath(){
        //we trace back from F to S
        if(d[F]==20000)System.out.print("There's no path from S to F");
        else{
        System.out.println(d[F]);
        int current=F;
        System.out.print(F);
        while(current!=S){
            current=Trace[current];
            System.out.print("<-"+current);
        }
    }
    }
    void writetoFile(){
        try{
            File outFile=new File("output.txt");
            FileWriter fileWriter=new FileWriter(outFile);
            BufferedWriter writer=new BufferedWriter(fileWriter);
            if(d[F]==20000)writer.write("There's no path from S to F");
            else{
                writer.write("The shortest distance from S to F is "+String.valueOf(d[F])+"\n");
                writer.write("The path is:\n");
                int current=F;
                writer.write(String.valueOf(F));
                while(current!=S){
                    current=Trace[current];
                    writer.write("<-"+String.valueOf(current));
                }
                writer.close();
            }
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
class FordBellman extends Graph{
    void findPath(){
        //initialize the Trace
        Trace=new int[n];
        d=new int[n];
        for(int i=0;i<n;i++)Trace[i]=-1;
        for(int i=0;i<n;i++)d[i]=20000;
        d[S]=0;
        for(int i=0;i<n;i++){
            //loop n times
            boolean stop=true;//decideed to quit the loop if stop==true
            for(int u=0;u<n;u++){
                for(int v=0;v<n;v++){
                    if(d[v]>d[u]+WeightMatrice[u][v]){
                        d[v]=d[u]+WeightMatrice[u][v];
                        Trace[v]=u;
                        stop=false;
                    }
                }
            }
            if(stop)break;
        }
        writetoFile();
    }
}
class Djistra extends Graph{
    /*Heap just for implementing Djistra algorithms
    Each node contains the vertice of Graph satisfies: d of parent node is smaller than d of child Node
    Class Attribute:
    arr: stores the vertice in heap
    length: length of heap
    setup(int[] array):setup the arr and length
    enHeap():enHeap
    update(i): update at the position i
    pop():pop the first element from Heap*/
    int[] arr;
    int length;
    int[] pos;//pos[i] is the position of i in Heap
    boolean[] free;//consider a vertice is free or not
    private void switchArr(int i,int j){
        //switch the position i and j of arr
        int temp=arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }
    private void setup(){
        arr=new int[n];
        length=arr.length;
        d=new int[n];
        pos=new int[n];
        Trace=new int[n];
        free=new boolean[n];
        for(int i=0;i<n;i++){
            Trace[i]=-1;
            d[i]=20000;
            arr[i]=i;
            pos[i]=i;
            free[i]=true;
            arr[i]=i;
        }
        d[S]=0;
        arr[0]=S;
        arr[S]=0;
    }
    private void enHeap(int i){
            int parent=i;
            while(2*parent+1<length){
                if(2*parent+2==length){
                    int leftChild=2*parent+1;
                    if(d[arr[parent]]>d[arr[leftChild]]){
                        pos[arr[parent]]=leftChild;
                        pos[arr[leftChild]]=parent;
                        switchArr(parent,leftChild);
                        break;
                    }
                    else break;
                }
                else{
                    int rightChild=2*parent+2;
                    int leftChild=2*parent+1;
                    int temp;
                    if(d[arr[leftChild]]<=d[arr[rightChild]])temp=leftChild;
                    else temp=rightChild;
                    if(d[arr[parent]]>d[arr[temp]]){
                        pos[arr[temp]]=parent;
                        pos[arr[parent]]=temp;
                        switchArr(parent,temp);
                        parent=temp;
                    }
                    else break;
                }
            }
    }
    private void update(int i){
        //we use update at the position i when d of node in position i was updated
        int child=i;
        int parent=(i-1)/2;
        while(child>0){
            if(d[arr[child]]<d[arr[parent]]){
                pos[arr[child]]=parent;
                pos[arr[parent]]=child;
                switchArr(parent,child);
                child=parent;
                parent=(child-1)/2;
            }
            else break;
        }
    }
    private int pop(){
        //switch the position of the first and last element in heap
        int returnValue=arr[0];
        pos[arr[length-1]]=0;
        pos[arr[0]]=length-1;
        switchArr(0,length-1);       
        length--;
        //we must enHeap since we switch the 2 elements
        enHeap(0);
        return returnValue;
    }
    void findPath(){
        //firstly we must enHeap the entire tree
        setup();
        while(true){
            //pop the first in Heap and unfree it
            int vertice=pop();
            if(vertice==F)break;
            free[vertice]=false;
            boolean stop=true;
            //iter through all adjancy of vertice
            for(int i=0;i<n;i++){
                if(WeightMatrice[vertice][i]!=20000 && free[i]){
                    if(d[i]>d[vertice]+WeightMatrice[vertice][i]){
                        d[i]=d[vertice]+WeightMatrice[vertice][i];
                        Trace[i]=vertice;
                        update(pos[i]);
                        stop=false;
                    }
                }
            }
            if(stop)break;
        }
        writetoFile();
    }
}
class Stack{
    //Stack for storing zero inVer edges in Topo order algorithm
    private int[] arr;//stack arr
    private int length;//stack length
    Stack(int n){
        arr=new int[n];
        length=0;
    }
    void push(int pushValue){
        arr[length]=pushValue;
        length++;
    }
    int pop(){
        length--;
        return arr[length];
    }
    boolean isEmpty(){
        if(length==0)return true;
        else return false;
    }
}
class TopoOrder extends Graph{
    /*Class attributes
    newVertice: set private
    oldVertice: set private
    Class Method:
    setup(): setup the attributes, Trace and d
    reorder(): reoder the vertice, if edges (i,j) after reorder, number of i must smaller than i
    findPath(): FindPath and write to File
    */
    private int[] newVertice;//newVertice[i]=j that means the old is i, after reorder changed to j
    private int[] oldVertice;//oldVertice[i]=j that means after changing into vertice i, its prior name is j
    private void setup(){
        newVertice=new int[n];
        oldVertice=new int[n];
        d=new int[n];
        Trace=new int[n];
        for(int i=0;i<n;i++){
            d[i]=20000;
            Trace[i]=-1;
        }
        d[S]=0;
    }
    private boolean reorder(){
        boolean[] mark=new boolean[n];//mark[i]=false that mean vertice is is not reorder
        int[] inVer;//inVer[i] is number of edges that into vertice i
        inVer=new int[n];//count the number of edges that come to vertice
        Stack stk=new Stack(n);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(WeightMatrice[j][i]!=20000 && j!=i)inVer[i]++;
            }
            if(inVer[i]==0)stk.push(i);//detect a zero inVer and push it to stack
        }
        int count=0;//count number of vertice we sucessfully reoder;
        while(!stk.isEmpty()){
            //pop from stack and check if it is a zero inVer
            int vertice=stk.pop();
            mark[vertice]=true;
            newVertice[vertice]=count;
            oldVertice[count]=vertice;
            for(int i=0;i<n;i++){
                if(WeightMatrice[vertice][i]!=20000 && vertice!=i && !mark[i]){
                    inVer[i]--;
                    if(inVer[i]==0)stk.push(i);
                }
            }
            count++;
        }
        if(count<n){
            //if count<n may be the Graph has a cycle, our algorithm won't work on that type of Graph
            System.out.println("Cannot reorder all vertices");
            return false;
        }
        else return true;
    }
    void findPath(){
        setup();
        if(reorder()){
            //when we reorder the Graph, it's convinient to optimize the d
            for(int i=newVertice[S];i<newVertice[F];i++){
                for(int j=i+1;j<=newVertice[F];j++){
                    if(d[oldVertice[j]]>d[oldVertice[i]]+WeightMatrice[oldVertice[i]][oldVertice[j]]){
                        d[oldVertice[j]]=d[oldVertice[i]]+WeightMatrice[oldVertice[i]][oldVertice[j]];
                        Trace[oldVertice[j]]=oldVertice[i];
                    }
                }
            }
        }
        writetoFile();
    }
}
class ShortestPath{
    public static void main(String argv[]){
        Graph myGraph=new TopoOrder();
        myGraph.readInformation();
        myGraph.findPath();
    }
}