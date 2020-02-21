import java.io.*;
abstract class SearchType{
    int[] arr;//the array for searching
    int key;//the value we need to search in arr
    abstract void Search();
    void readFile(){
        //read from input
        try {
            File infile=new File("input.txt");
            FileReader filereader=new FileReader(infile);
            BufferedReader reader=new BufferedReader(filereader);
            String myStr=reader.readLine();
            key=Integer.parseInt(myStr);
            myStr=reader.readLine();
            reader.close();
            String[] strarr=myStr.split(" ");
            arr=new int[arr.length];
            for(int i=0;i<strarr.length;i++)arr[i]=Integer.parseInt(strarr[i]);
        } catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
        }
    }
}
class SequencialSearching extends SearchType{
    void Search(){
        for(int i=0;i<arr.length;i++){
            if(arr[i]==key){
                System.out.println("Found at position "+i);
            }
            else System.out.println("Not Found");
        }
    }
}
class BinarySearching extends SearchType{
    BinarySearching(boolean sortState){
        if(sortState==true){
            //we must sort the arr before find
        }
    }
    void Search(){
        int first=0;
        int last=arr.length;
        while(true){
            if(last==first)break;
            int index=(first+last)/2;
            if(arr[index]==key){
                System.out.print("Found at position "+index);
                break;
            }
            else if(arr[index]>key)last=index;
            else first=index;
        }
        System.out.print("Not Found");
    }
}
class Node{
    //Node of Binary Search tree
    int value;
    Node left;
    Node right;
    Node(int value){
        this.value=value;
        this.left=null;
        this.right=null;
    }
}
abstract class SearchTree{
    Node root;
    void deleteNode(int value){
        //First we must search the node with its value is value and the parent of that node
        //if the target node is node root, we must delete it from tree
        if(root.value==value){
            //if there is no right node, assign the root node to the left node
            if(root.right==null)root=root.left;
            else{
                //checking if the first right node has left node
                //No: assign the right node to the root
                Node parent=root.right;
                if(parent.left==null)root=root.right;
                else{
                    //Yes: go left until the last node on our path and bring it to the root
                    Node child=root.left;
                    while(child.left!=null){
                        parent=child;
                        child=parent.left;
                    }
                    parent.left=null;//Cut the relationship with parent node
                    root.value=child.value;//assign the value of child to root
                }
            }
        }
        else{
            //first we found node with value of value
            Node parent=root;
            Node child;
            if(root.value>=value)child=parent.left;
            else child=parent.right;
            while(child!=null && child.value!=value){
                parent=child;
                if(child.value>=value)child=child.left;
                else child=child.right;
            }
            if(child==null)System.out.print("Couldn't find the node to delete");
            else{
                //we found the node
                if(child.left==null){
                    if(child.right==null){
                        //We simply remove child from the tree by assign the node parent point to null instead of child
                        if(parent.left==child)parent.left=null;
                        else parent.right=null;
                    }
                    else{
                        //we simply assign the parent pointer to the right  node of child instead of child
                        if(parent.left==child)parent.left=child.right;
                        else parent.right=child.right;
                    }
                }
                else{
                    if(child.right==null){
                        //Like the case 2
                        if(parent.left==child)parent.left=child.left;
                        else parent.right=child.left;
                    }
                    else{
                        //This is the toughest, we go to the first right node, and go the the left to the last node
                        Node subParent=child.right;
                        if(subParent.left==null){
                            if(parent.left==child)parent.left=subParent;
                            else parent.right=subParent;
                        }
                        else{
                            Node subChild=subParent.left;
                            while(subChild.left!=null){
                                subParent=subChild;
                                subChild=subChild.left;
                            }
                            //assign the left node of subParent by null
                            subParent.left=null;
                            child.value=subChild.value;
                        }
                    }
                }
            }
        }
    }
}
class BinarySearchTree extends SearchTree{
    void buildTree(int[] arr){
        //build tree based on the arr
        root=new Node(arr[0]);
        for(int i=1;i<arr.length;i++){
            //Create a new node with value arr[i]
           Node iter=root;
           while(true){
               if(arr[i]<=iter.value){
                   //we move to the left node if the current value < node value
                   if(iter.left==null){
                       Node new_node=new Node(arr[i]);
                       iter.left=new_node;
                       break;
                   }
                   else iter=iter.left;
               }
               else{
                   //if the value is larger then node, go right
                   if(iter.right==null){
                       Node new_node=new Node(arr[i]);
                       iter.right=new_node;
                       break;
                   }
                   else iter=iter.right;
               }
           }
        }
    }
    void Searching(int value){
        Node iter=root;
        while(iter!=null && iter.value!=value){
            if(iter.value>value)iter=iter.left;
            else iter=iter.right;
        }
        if(iter==null){
            System.out.println("Couldn't found");
        }
        else System.out.println("Found");
    }
}
class DigitalSearchTree extends SearchTree{
    private int numberOfDigit;
    private int[] digital2Binary(int number){
        //calculate the conversion from digital to binary of number
        int[] arr=new int[numberOfDigit];
        int i=numberOfDigit-1;
        while(number>0){
            arr[i]=number%2;
            i--;
            number/=2;
        }
        for(int j=0;j<=i;j++)arr[j]=0;
        return arr;
    }
    void buildTree(int[] arr,int digitNumber){
        //we convert each element into binary,we go through every element in the binary array
        //until we reach the null node(for each step we turn left if the current digit is zero
        //else we turn right) 
        numberOfDigit=digitNumber;
        root=new Node(arr[0]);
        for(int i=1;i<arr.length;i++){
            int[] convert_arr=digital2Binary(arr[i]);
            Node current_node=root;
            int j=0;//the current position in we consider in arr
            if(convert_arr[j]==0){
                if(current_node.left==null){
                    //we create a new node to the left
                    Node new_node=new Node(arr[i]);
                    current_node.left=new_node;
                }
                else{
                    current_node=current_node.left;//we keep moving to the left
                    j++;
                }
            }
            else{
                if(current_node.right==null){
                    //we create a new node to the right
                    Node new_node=new Node(arr[i]);
                    current_node.right=new_node;
                }
                else {
                    current_node=current_node.right;//we keep moving to the right
                    j++;
                }
            }
        }
    }
    void Searching(int value){
        int[] arr=digital2Binary(value);
        if(root.value==value){
            System.out.println("Found");
        }
        else{
            int j=0;
            Node currentNode=root;
            while(currentNode!=null && currentNode.value!=value){
                if(arr[j]==0)currentNode=currentNode.left;
                else currentNode=currentNode.right;
                j++;
            }
            //checking the node is null or found
            if(currentNode==null){
                System.out.println("Not found");
            }
            else System.out.println("found");
        }
    }
}
class RadicalSearchTree{
    Node root=new Node(-1);
    private int numberOfDigit;
    private int[] decimal2Binary(int number){
        //convert a number from decimal to binary
        int[] arr=new int[numberOfDigit];
        int j=numberOfDigit-1;
        while(number>0){
            arr[j]=number%2;
            j--;
            number/=2;
        }
        for(int i=0;i<=j;i++)arr[i]=0;
        return arr;
    }
    void buildTree(int[] arr,int digit){
        //We create intermediate nodes that contains no information
        //we go through all element in binary arr, if zero we turn left and one we turn right
        //we create a node at the end
        numberOfDigit=digit;
        for(int i=0;i<arr.length;i++){
            Node currenNode=root;
            int[] convert_arr=decimal2Binary(arr[i]);
            for(int j=0;j<numberOfDigit-1;j++){
                if(convert_arr[j]==0){
                    if(currenNode.left==null){
                        //we create a node with no information to the left
                        Node newNode=new Node(-1);
                        currenNode.left=newNode;
                        currenNode=newNode;
                    }
                    else currenNode=currenNode.left;
                }
                else{
                    if(currenNode.right==null){
                        //we create a new node to the right
                        Node newNode=new Node(-1);
                        currenNode.right=newNode;
                        currenNode=newNode;
                    }
                    else currenNode=currenNode.right;
                }
            }
            //create a node to the left of currentNode
            Node newNode=new Node(arr[i]);
            if(convert_arr[numberOfDigit-1]==0)currenNode.left=newNode;
            else currenNode.right=newNode;
        }
    }
    void deleteNode(int value){
        int[] arr=decimal2Binary(value);
        Node ancestorNode=root;
        Node currentNode=root;
        boolean direction;
        if(arr[0]==0) direction=true;
        else direction=false;
        for(int i=0;i<numberOfDigit-1;i++){
            if(arr[i]==0){
                currentNode=currentNode.left;
                if(currentNode.right!=null){
                    //mark the ancestor node
                    ancestorNode=currentNode;
                    if(arr[i+1]==0)direction=true;
                    else direction=false;
                }
            }
            else{
                currentNode=currentNode.right;
                if(currentNode.left!=null){
                    //mark the ancestor node
                    ancestorNode=currentNode;
                    if(arr[i+1]==0)direction=true;
                    else direction=false;
                }
            }
        }
        if(direction)ancestorNode.left=null;
        else ancestorNode.right=null;
    }
    void Searching(int value){
        Node currentNode=root;
        int[] arr=decimal2Binary(value);
        for(int i=0;i<arr.length;i++){
            if(arr[i]==0){
                if(currentNode.left==null){System.out.println("Not Found");break;}
                else currentNode=currentNode.left;
            }
            else{
                if(currentNode.right==null){
                    System.out.println("Not Found");
                    break;
                }
                else currentNode=currentNode.right;
            }
        }
        System.out.println("Found");
    }
}
class Searching{
    public static void main(String[] argv){

    }
}