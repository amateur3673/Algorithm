import java.io.*;
//finding connected components in Graph
//Graph information can be stored in adjancency matrice, edge list, adjancency list
//in this program, Graph information will be stored in adjacency list
//input information is in input.txt:
//The first line is n and m (number of vertices and edges in Graph), seperated by a space
//Following m lines are (i,j) where (i,j) are the edges in Graph
class Node{
    //like node of linklist we've known
    int value;
    Node next;
    Node(int value){
        this.value=value;
        this.next=null;
    }
}
class AdjancencyList{
    //this like the link list we've known
    Node head;//head of the node
    AdjancencyList(){
        head=null;
    }
    void append(int value){
        //append new node to the head of List
        Node newNode=new Node(value);
        if(head==null){
            head=newNode;
        }
        else{
            newNode.next=head;
            head=newNode;
        }
    }
}
class Stack{
    private int[] arr;//array stores stack
    private int length;//length of stack
    Stack(int n){
        arr=new int[n];
        length=0;
    }
    void push(int value){
        arr[length]=value;
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
class Queue{
    private int[] arr;//array stores in queue
    private int start;//starting position of Queue
    private int end; //finishing position of Queue
    Queue(int n){
        arr=new int[n];
        start=0;end=0;
    }
    void enQueue(int value){
        arr[end]=value;
        end++;
    }
    int deQueue(){
        start++;
        return arr[start-1];
    }
    boolean isEmpty(){
        if(start==end)return true;
        else return false;
    }
}
class Graph{
    private int n;//number of vertices
    private int m;//number of edges
    private boolean[] visit;//decide a vertice that was visited or not false if not, else true.
    private AdjancencyList[] aLists;//AdjacencyList of all vertice in Graph
    private void readFile(){
        try{
            File inFile=new File("input.txt");
            FileReader fWriter=new FileReader(inFile);
            BufferedReader reader=new BufferedReader(fWriter);
            String myStr=reader.readLine();
            String[] strArr=myStr.split(" ");
            n=Integer.parseInt(strArr[0]);
            m=Integer.parseInt(strArr[1]);
            visit=new boolean[n];//default was false
            aLists=new AdjancencyList[n];
            for(int i=0;i<n;i++)aLists[i]=new AdjancencyList();
            for(int i=0;i<m;i++){
                myStr=reader.readLine();
                strArr=myStr.split(" ");
                int p=Integer.parseInt(strArr[0]);
                int q=Integer.parseInt(strArr[1]);
                aLists[p].append(q);
                aLists[q].append(p);
            }
            reader.close();
        }
        catch(Exception e){
            System.out.println("Error:");
            e.printStackTrace();
            System.out.println(e);
        }
    }
    private void DFS(int i){
        //implement the DFS's algorithm
        Stack stack=new Stack(n);
        stack.push(i);
        while(!stack.isEmpty()){
            int vertice=stack.pop();
            System.out.print(vertice+" ");
            visit[vertice]=true;
            Node iterNode=aLists[vertice].head;
            while(iterNode!=null){
                if(!visit[iterNode.value])stack.push(iterNode.value);
                iterNode=iterNode.next;
            }
        }
        System.out.print("\n");
    }
    private void BFS(int i){
        //implement the BFS's algorithm
        Queue queue=new Queue(n);
        queue.enQueue(i);
        while(!queue.isEmpty()){
            int vertice=queue.deQueue();
            System.out.print(vertice+" ");
            visit[vertice]=true;
            Node iterNode=aLists[vertice].head;
            while(iterNode!=null){
                if(!visit[iterNode.value])queue.enQueue(iterNode.value);
                iterNode=iterNode.next;
            }
        }
        System.out.print("\n");
    }
    void ConnectedComponent(){
        readFile();
        int i=0;
        while(true){
            BFS(i);
            int j;
            for(j=0;j<n;j++){
                if(!visit[j]){
                    i=j;
                    break;
                }
            }
            if(j==n)break;
        }
    }
}
class Digraph{
    /*
    class description:
    Attribute:
    m: number of edges
    n: number of vertices
    aList: adjacency vertice list
    Number: order of vertice in DFS
    Low: the smallest Number in a DFS tree starts from root i
    Method:
    readFile: read the input
    visit: recursion by i(the currently vertice)
    StrongConnectedComponent: implement visit for finding strong connected component purposes 
    */
    private int m;//number of edges
    private int n;//number of vertices
    private AdjancencyList[] aLists;//adjacency list
    private int[] Low;
    private int[] Number;
    private void readFile(){
        try{
            File infile=new File("input2.txt");
            FileReader filereader=new FileReader(infile);
            BufferedReader reader=new BufferedReader(filereader);
            String str=reader.readLine();
            String[] arrStr=str.split(" ");
            n=Integer.parseInt(arrStr[0]);
            m=Integer.parseInt(arrStr[1]);
            aLists=new AdjancencyList[n];
            Low=new int[n];
            Number=new int[n];
            for(int i=0;i<n;i++)Number[i]=-1;
            for(int i=0;i<n;i++)aLists[i]=new AdjancencyList();
            for(int i=0;i<m;i++){
                str=reader.readLine();
                arrStr=str.split(" ");
                int p=Integer.parseInt(arrStr[0]);
                int q=Integer.parseInt(arrStr[1]);
                aLists[p].append(q);
            }
            reader.close();
        }
        catch(Exception e){
            System.out.println("Error");
            System.out.println(e);
            e.printStackTrace();
        }
    }
    private void visit(int i,int count,Stack stack){
        Number[i]=count;
        Low[i]=count;
        stack.push(i);
        Node currentNode=aLists[i].head;
        while(currentNode!=null){
            if(Number[currentNode.value]==-1){
                visit(currentNode.value,count+1,stack);
                if(Low[i]>Low[currentNode.value])Low[i]=Low[currentNode.value];
            }
            else{
                if(Low[i]>Number[currentNode.value])Low[i]=Number[currentNode.value];
            }
            currentNode=currentNode.next;
        }
        if(Low[i]==Number[i]){
            while(true){
                int value=stack.pop();
                System.out.print(value+" ");
                if(value==i)break;
            }
            System.out.print("\n");
        }
    }
    void StrongConnectedComponent(){
        readFile();
        int i;
        for(i=0;i<n;i++){
            if(Number[i]==-1){
                Stack stack=new Stack(n);
                visit(i,0,stack);
            }
        }
    }
}
class Visiting{
    public static void main(String argv[]){
        Graph graph=new Graph();
        graph.ConnectedComponent();
        Digraph digraph=new Digraph();
        digraph.StrongConnectedComponent();
    }
}
