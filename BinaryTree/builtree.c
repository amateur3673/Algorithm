//Build a full-tree from 1 to n
#include <stdio.h>
#include <stdlib.h>
typedef struct LinkList* Node;
struct LinkList{
    int value;
    struct LinkList* left;
    struct LinkList* right;
};
Node node_init(int value){
   Node construct_node=(Node)malloc(sizeof(struct LinkList));
   construct_node->value=value;
   construct_node->left=NULL;
   construct_node->right=NULL;
   return construct_node;
}
void enQueue(Node pushed_node,Node* queue,int* end){
    queue[*end]=pushed_node;
    (*end)++;
}
Node deQueue(Node* queue,int* start,int* finish){
    if(*start<*finish){
       (*start)++;
    }
}
Node get(Node* queue,int start){
    //get the top of the queue
    return queue[start];
}
void travelsal(Node root){
    //use preorder travelsal
    if(root->left!=NULL)travelsal(root->left);
    if(root->right!=NULL)travelsal(root->right);
    printf("%d\n",root->value);
}
int main(){
    int n=7;
    //printf("Nhap vao n: ");scanf("%d",&n);
    Node root;
    int i;
    Node* queue=(Node*)malloc(n*sizeof(Node));
    int start=0,end=0;
    int* number=(int*)malloc(n*sizeof(int));
    for(i=0;i<n;i++)number[i]=0;
    root=node_init(1);
    enQueue(root,queue,&end);
    for(int i=2;i<n;i++){
        Node temp=get(queue,start);
        number[temp->value]++;
        Node construct_node=node_init(i);
        if(number[temp->value]==1){
            temp->left=construct_node;
        }
        else{
            temp->right=construct_node;
            deQueue(queue,&start,&end);
        }
        enQueue(construct_node,queue,&end);
    }
    travelsal(root);
}