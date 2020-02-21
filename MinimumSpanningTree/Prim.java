import java.io.*;
class PrimAl{
    int n;//number of vertice
    int m;//number of edges
    int[][] weightMat;//weight matrice
    int length;//length of Heap
    int[] d;//distance to spanning tree
    int[] arr;//array of Heap
    int[] Trace;//for Tracing
    boolean[] visit;//if we visited the vertice the
    int[] pos;//position of each vertice in Heap 
    void readFile(){
        try{
            File inFile=new File("input.txt");
            FileReader fileReader=new FileReader(inFile);
            BufferedReader reader=new BufferedReader(fileReader);
            String myStr=reader.readLine();
            String[] strArr=myStr.split(" ");
            n=Integer.parseInt(strArr[0]);
            m=Integer.parseInt(strArr[1]);
            weightMat=new int[n][n];
            for(int i=0;i<m;i++){
                myStr=reader.readLine();
                strArr=myStr.split(" ");
                int p=Integer.parseInt(strArr[0]);
                int q=Integer.parseInt(strArr[1]);
                int w=Integer.parseInt(strArr[2]);
                weightMat[p][q]=w;
                weightMat[q][p]=w;
            }
            reader.close();
        }
        catch(Exception e){
            System.out.println("Error");
            System.out.println(e);
            e.printStackTrace();
        }
    }
    void switchArr(int i,int j){//switch between position i and j of arr
        pos[arr[i]]=j;
        pos[arr[j]]=i;
        int temp=arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }
    void enHeap(int i){
        int parent=i;
        while(2*parent+1<length){
            if(2*parent+2==length){
                int leftChild=2*parent+1;
                if(d[arr[parent]]>d[arr[leftChild]])switchArr(parent,leftChild);
                break;
            }
            else{
                int leftChild=2*parent+1;
                int rightChild=2*parent+2;
                int temp;
                if(d[arr[leftChild]]<=d[arr[rightChild]])temp=leftChild;
                else temp=rightChild;
                if(d[arr[parent]]>d[arr[temp]]){
                    switchArr(parent,temp);
                    parent=temp;
                }
                else break;
            }
        }
    }
    void update(int i){
        //update at position i
        int current=i;//the current position
        while(current>0){
            int parent=(current-1)/2;
            if(d[arr[parent]]>d[arr[current]]){
                switchArr(parent,current);
                current=parent;
            }
            else break;
        }
    }
    int pop(){
        //pop the root of tree
        int returnVal=arr[0];
        switchArr(0,length-1);
        length--;
        enHeap(0);
        return returnVal;
    }
    void PrimAlgorithm(){
        readFile();
        arr=new int[n];
        for(int i=0;i<n;i++)arr[i]=i;
        length=n;
        d=new int[n];
        for(int i=0;i<n;i++)d[i]=20000;
        d[0]=0;
        Trace=new int[n];
        for(int i=0;i<n;i++)Trace[i]=-1;
        for(int i=(length-1)/2;i>=0;i--)enHeap(i);
        int count=0;//count number of vertice in Tree
        visit=new boolean[n];
        pos=new int[n];
        for(int i=0;i<n;i++)pos[i]=i;
        while(true){
            int vertice=pop();//pop the first from the Heap
            visit[vertice]=true;//assign false for visited vertice
            count++;
            if(count==n)break;//we stop when tree has n vertices
            for(int i=0;i<n;i++){
                if(weightMat[vertice][i]>0 && visit[i]==false){
                    if(d[i]>weightMat[vertice][i]){
                        //We only consider the unvisited vertice
                        d[i]=weightMat[vertice][i];//update the distance
                        update(pos[i]);//update the Heap
                        Trace[i]=vertice;//Trace back
                    }
                }
            }
        }
        writeToFile();
    }
    void writeToFile(){
        try{
            File outFile=new File("output.txt");
            FileWriter fileWriter=new FileWriter(outFile);
            BufferedWriter writer=new BufferedWriter(fileWriter);
            writer.write("The edge of lowest spanning tree:\n");
            int sum=0;
            for(int i=0;i<n;i++){
                if(Trace[i]>=0){
                    writer.write("("+i+" "+Trace[i]+")"+"="+weightMat[i][Trace[i]]+"\n");
                    sum+=weightMat[i][Trace[i]];
                }
            }
            writer.write("The weight is "+sum);
            writer.close();
        }
        catch(Exception e){
            System.out.println("Error");
            System.out.println(e);
            e.printStackTrace();
        }
    }
    void display(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                System.out.print(weightMat[i][j]+" ");
            }
            System.out.print("\n");
        }
    }
}
class Prim{
    public static void main(String argv[]){
        PrimAl prim=new PrimAl();
        prim.PrimAlgorithm();
    }
}