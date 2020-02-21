/*
Writing Prim Algorithm in C language
*/
#include <stdio.h>
#include <stdlib.h>
#define MAX_d 20000
typedef struct EdgeList* Edge;
struct EdgeList{
    //structure Edge
    int vertice;
    int weight;
    struct EdgeList* next;
};
Edge initialize(int ver,int weight){
    //initialize 1 Edge
    Edge temp=(Edge)malloc(sizeof(Edge));
    temp->vertice=ver;
    temp->weight=weight;
    temp->next=NULL;
    return temp;
}
Edge push(Edge head,Edge pushed_node){
    //push an Edge to the list
    if(head==NULL){
        head=pushed_node;
        return head;
    }
    else{
        pushed_node->next=head;
        return pushed_node;
    }
}
void switch_arr(int* arr,int i,int j,int* pos){
    //switch the position i and j in arr
    pos[arr[i]]=j;
    pos[arr[j]]=i;
    int temp=arr[i];
    arr[i]=arr[j];
    arr[j]=temp;
}
void enHeap(int* arr,int i,int length,int* d,int* pos)
{
    //enHeap at position i, node parent has smaller distance than children
    int parent=i;
    while(2*parent+1<length){
        if(2*parent+2==length){
            //there's only left child
            int left_child=2*parent+1;
            if(d[arr[parent]]<d[arr[left_child]])switch_arr(arr,parent,left_child,pos);
            break;
        }
        else{
            //there are both left and right children
            int left_child=2*parent+1;
            int right_child=2*parent+2;
            int temp;//determine which child is smaller of d
            if(d[arr[left_child]]<=d[arr[right_child]])temp=left_child;
            else temp=right_child;
            if(d[arr[parent]]>d[arr[temp]]){
                //switch the position if parent>temp
                switch_arr(arr,parent,temp,pos);
                parent=temp;
            }
            else break;//stop if parent<=temp
        }
    }
}
int pop(int* arr,int* length,int* d,int* pos){
    //pop the root of Heap
    int top_heap=arr[0];
    switch_arr(arr,0,*length-1,pos);//switch the first and the last of Heap
    (*length)--;//remove the last from Heap
    enHeap(arr,0,*length,d,pos);//enHeap at position 0
    return top_heap;
}
void update(int* arr,int* d,int i,int* pos){
    //when d[i] changed its value, we must update Heap at position i
    int current=i;//current node
    while(current>0){
        int parent=(current-1)/2;
        if(d[arr[parent]]>d[arr[current]]){
            switch_arr(arr,current,parent,pos);
            current=parent;
        }
        else break;
    }
}
void printResult(int* Trace,int n){
    int i;
    for(i=1;i<n;i++){
        printf("(%d %d)\n",i,Trace[i]);
    }
}
int main()
{
   int n,m;//number of vertice and edge
   FILE* fptr;//pointer of file
   fptr=fopen("input.txt","r");
   if(fptr==NULL){
       printf("Cannot open input file");
       return -1;
   }
   fscanf(fptr,"%d",&n);//read n
   fscanf(fptr,"%d",&m);//read m
   int i;
   //allocation list of Edge
   Edge* elist=(Edge*)malloc(n*sizeof(Edge));
   for(i=0;i<n;i++)elist[i]=NULL;
   //declare the vertice1,vertice2 variable
   int ver1,ver2,weight;
   for(i=0;i<m;i++){
       fscanf(fptr,"%d",&ver1);
       fscanf(fptr,"%d",&ver2);
       fscanf(fptr,"%d",&weight);
       Edge pushed_edge1=initialize(ver2,weight);//create a Node of vertice2 and weight
       Edge pushed_edge2=initialize(ver1,weight);
       elist[ver1]=push(elist[ver1],pushed_edge1);//push it into edge
       elist[ver2]=push(elist[ver2],pushed_edge2);
   }
   fclose(fptr);
   int* arr=(int*)malloc(n*sizeof(int));//arr stands for Heap element
   //We first initialize the Heap
   for(i=0;i<n;i++)arr[i]=i;
   int length=n;//length of Heap
   int* d=(int*)malloc(n*sizeof(int));//distance from each vertice to tree
   for(i=0;i<n;i++)d[i]=MAX_d;//set d=inf for each vertice, except vertice 0
   d[0]=0;
   char* free=(char*)malloc(n*sizeof(char));//determine the vertice is visit or not, valued=0 if visited 1 if not visited
   for(i=0;i<n;i++)free[i]=1;
   int* Trace=(int*)malloc(n*sizeof(int*));//for Tracing, Trace[i]=j mean that we choose the edge (j,i)(j before i)
   for(i=0;i<n;i++)Trace[i]=-1;
   int* pos=(int*)malloc(n*sizeof(int));//position of each vertice in Heap
   for(i=0;i<n;i++)pos[i]=i;
   int count=0;//count the number of current vertice in the tree
   while(1){
       int vertice=pop(arr,&length,d,pos);//pop from Heap
       free[vertice]=0;//set free to false
       count++;
       if(count==n)break;//break when all vertice are in tree
       Edge edge_iter=elist[vertice];//set a pointer pointing to the head of edgelist of current vertice
       while(edge_iter!=NULL){
           int adj_vertice=edge_iter->vertice;//get the adjacency vertice
           if(free[adj_vertice] && d[adj_vertice]>edge_iter->weight){
               //if adj_vertice is free and the weight is smaller than the d, we update the d
               d[adj_vertice]=edge_iter->weight;
               Trace[adj_vertice]=vertice;
               update(arr,d,pos[adj_vertice],pos);
           }
           edge_iter=edge_iter->next;
       }
   }
   printResult(Trace,n);
}