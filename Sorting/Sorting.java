import java.io.*;
abstract class SortType{
    /*Attribute in SortType Class
    arr: array for sorting
    Method:
    printResult(): prints result after sorting
    readFile(): read from input.txt
    writeToFile(): Write to output.txt the array after sorting
    Sort(): abstract method, overload in sub class
    SwithArr(i,j):Switch position of ith and jth element in arr*/
    int[] arr;
    abstract void Sort();
    void SwithArr(int i,int j){
        int temp=arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }
    void printResult(){
        for(int i=0;i<arr.length;i++)System.out.print(arr[i]+" ");
    }
    void readFile(){
        try {
            File inFile=new File("input.txt");
            FileReader filereader=new FileReader(inFile);
            BufferedReader reader=new BufferedReader(filereader);
            String myStr=reader.readLine();
            String[] strarr=myStr.split(" ");
            arr=new int[strarr.length];
            for(int i=0;i<strarr.length;i++)arr[i]=Integer.parseInt(strarr[i]);
            reader.close();
        } catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
        }
    }
}
class SelectionSort extends SortType{
    void Sort(){
        for(int i=0;i<arr.length-1;i++){
            for(int j=i+1;j<arr.length;j++){
                if(arr[i]>arr[j])SwithArr(i, j);
            }
        }
        //printResult();
    }
}
class QuickSort extends SortType{
    /*This class includes these methods:
    Quick(int i,int j): Sorting the array from position i to j
    Sort(): overwrite the Sort of the abstract class*/
    private void Quick(int S,int F){
        if(S<F){
            int i=S,j=F;
            int pivot=arr[(S+F)/2];
            do{
                while(arr[i]<pivot)i++;
                while(arr[j]>pivot)j--;
                if(i<=j){
                    if(i<j)SwithArr(i, j);
                    i++;j--;
                }
            }while(i<=j);
            Quick(S,j);
            Quick(i,F);
        }
    }
    void Sort(){
        Quick(0,arr.length-1);
        //printResult();
    }
}
class HeapSort extends SortType{
    /*
    Class Attribute:
    int length: Length of Heap
    Class Method:
    void enHeap(int i): enHeap at position i in Heap
    void pop(): pick an element in root and move it to the bottom of Heap
    */
    private int length;
    private void enHeap(int i){
        int parent=i;
        while(2*parent+1<length){
            //if we're in Heap
            if(2*parent+2==length){
                //if there's only nodeChild left
                int leftChild=2*parent+1;
                if(arr[parent]<arr[leftChild])SwithArr(parent,leftChild);
                break;
            }
            else{
                //If there's 2 node childs
                int leftChild=2*parent+1;
                int rightChild=2*parent+2;
                int temp;
                if(arr[leftChild]>arr[rightChild])temp=leftChild;
                else temp=rightChild;
                if(arr[parent]<arr[temp]){
                    SwithArr(parent,temp);
                    parent=temp;
                }
                else break;
            }
        }
    }
    private void pop(){
        SwithArr(0,length-1);
        length--;
        enHeap(0);
    }
    void Sort(){
        length=arr.length;
        for(int i=(length-1)/2;i>=0;i--)enHeap(i);
        for(int i=1;i<arr.length;i++)pop();
        //printResult();
    }
}
class MergeSort extends SortType{
    /*This class includes these methods:
    Merge: merge 2 sorted array into a new sorted array
    Sort: overwrite the method Sort in SortType
    */
    void Merge(int m,int n,int p,int[] arr1,int[] arr2){
        //Merge the sorted partition (m,n) and partition(n+1,p) into a new partition
        //arr1 stores the old order, arr2 stores the new order
        int i=m,j=n+1;//iter of arr1
        int k=m;//iter of arr2
        while(i<=n||j<=p){
            if(i<=n&&j<=p){
                if(arr1[i]<arr1[j]){
                    arr2[k]=arr1[i];
                    i++;k++;
                }
                else{
                    arr2[k]=arr1[j];
                    j++;k++;
                }
            }
            else if(i>n && j<=p){
                while(j<=p){
                    arr2[k]=arr1[j];
                    k++;j++;
                }
            }
            else if(i<=n && j>p){
                while(i<=n){
                    arr2[k]=arr1[i];
                    k++;i++;
                }
            }
        }
    }
    void Sort(){
        int[] arrClone=new int[arr.length];
        int m=1;//m is the length of the subarr we gonna sort
        boolean state=true;//state=true we sort the subarr and stored to arrClone,else we sort the arrClone
        //store in arr
        while(m<=arr.length){
            int i=0;
            while(true){
                if(i<arr.length){
                    //i is not exceed the arr
                    if(i+2*m-1<=arr.length-1){
                        //we could sort (i,i+m-1,i+2*m-1)
                        if(state)Merge(i,i+m-1,i+2*m-1,arr,arrClone);
                        else Merge(i,i+m-1,i+2*m-1,arrClone,arr);
                    }
                    else if(i+m-1<arr.length-1){
                        //we could sort (i,i+m-1,arr.length-1)
                        if(state)Merge(i,i+m-1,arr.length-1,arr,arrClone);
                        else Merge(i,i+m-1,arr.length-1,arrClone,arr);
                    }
                    else{
                        //we transfer the (i,arr.length-1) to new arr due to state
                        if(state){
                            for(int j=i;j<arr.length;j++)arrClone[j]=arr[j];
                        }
                        else{
                            for(int j=i;j<arr.length;j++)arr[j]=arrClone[j];
                        }
                    }
                }
                i+=2*m;
                if(i>=arr.length)break;
            }
            state=!state;m*=2;
        }
        if(!state)arr=arrClone;
        //printResult();
    }    
}
class Sorting{
    public static void main(String[] argv){
       SortType sort=new HeapSort();
       sort.readFile();
       long start=System.currentTimeMillis();
       sort.Sort();
       long end=System.currentTimeMillis();
       System.out.println((end-start)/1000F);
    }
}
