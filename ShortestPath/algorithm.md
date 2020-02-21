**Problem: Given a Graph: n vertices, m edges. We put a weight coeffient on each edge of Graph. We call the length of the way between 2 vertices is the total weight on that way. Our problem is: with 2 vertices S and F, determine the shortest path from S to F.**
Since in undirect Graph, for each edge, we can replace it with 2 edges with opposite direction, our problem will be solved in Direct Graph.
In our program, we stored Graph information in a matrice (n,n) where n is number of vertices in Graph, and will be called WeightMatrice in our program. We will assume that:  
- if 2 vertices (i,j) are not connected, we set the weight of edge (i,j) 20000,(assume that all the path's length on Graph doesn't exceed 20000).  
- For (i,i) in WeightMatrice, we set WeightMatrice[i][i]=0.  
- For (i,j) if connected, we set WeightMatrice[i][j]=weight[i][j]  

Before we dive deeper in algorithm, let's consider structure of our program. Our programs consists of these classes:  
  - an abstract `class` **Graph**: this class includes basic variables and method for our algorithm. You can see more detail in my notation of source code. 
  - `class` **Ford-Bellman**:extends `class` **Graph**: implement **Ford-Bellman** algorithm.
  - `class` **Djistra**: extends `class` Graph: implement **Djistra** algorithm
  - `class` **ShortestPath**: contains function main.  
  
 **1.Use Ford-Bellman algorithm**  
 - If our Graph contains a cycle that weighs negative, we couldn't find the shortest path in the Graph (because if we go through that cycle many times, the length of the path is smaller than previous time).  
 - Ford-Bellman algorithm is used for Graph that doesn't have negative cycle. Firstly, for each vertice i we set d[i]=20000 if i!=S and d[S]=0. d[i] means that the current shortest length we found from S to i.  
 - For 2 vertices i and j that are connected, we can optimze d[j] by: d[j]=min{d[i]+WeightMatrice[i][j]}.
 - For each loop our code is:  
    ```Java
    for(i=0;i<n;i++){  
       for(j=0;j<n;j++){  
          if(d[j]>d[i]+WeightMatrice[i][j]{  
             d[j]=d[i]+WeightMatrice[i][j];  
             }  
           }  
         } 
      ```
  - We need to loop this n times. After n times we will find the answer.
  - So why is this algorithm corrects. We can see after k loop, d[i] is the shortest path to vertice i for less than k edges. So after n loop this is the shortest path for less than n edges.  
  - When we look at the algorithm, we easily see that the complexity of the algorithm is O(n^3), quite slow.
  - To improve the speed of this algorithm, we look at the alternative solution, that is **Djistra** algorithm. 
  
  **2. Use Djistra algorithm**  
  - In **Ford-Bellman** implementation, after we optimize d[j] by vertice d[i] that connected to d[j]. But d[i] is not optimized, so later we may optimized d[i] and once again, optimized d[j]. This make the speed so slow.
  - To avoid this, in **Djistra** algorithm, we proprose a concept: *In each loop, we choose a vertice that cannot be optimized, and then optimize the other vertice that can be optimized via that vertice, that could reduce time.*  
 - So how's a vertice that cannot be optimized, that is the vertice that currently has the smallest d in list of optimizable
 vertices. Why?. We could simply prove this as follow: Consider vertice i- d[i] is the smallest d of optimizable vertice. If we can use vertice j to optimize vertice i, so d[i]=d[j]+WeightMatrice[j][i]. If the all weight of Graph is non-negative, then d[j]<d[i],constrast to the assuption d[i] is the smallest. That's why **Djistra algorithm is only implemented in non-negative weight Graph.**  
-  Let's summarize the algorithm:  
   - Setup d, Trace like **Ford-Bellman** algorithm.  
   - For each loop, find the vertice with smallest d, unfree it, searching for its free adjacency and optimize them via d[j]=min{d[i]+WeightMatrice[i][j]}
   - The algorithm stops when unfree F or cannot found any vertices with d<20000. In the second case, our Graph is not connected.  
- But if you set up the algorithm like above, this is quite slow because at each step, we must find the smallest d. This take us a complexity of O(n).  
- To improve the speed of algorithm, i suggest using Heap, a popular structure to store orderal sets. The parent of our Heap will be smaller than its children. Each time to find the d, we only need to pop the root of tree, that takes a comlexity of O(1). Each time d[i] is optimized, we use update method to put it into higher hierarchy.
- Class discription:  
   - Attributes:
     - arr: stores the vertice in heap
     - length: length of Heap
     - pos: pos[i] means that position of vertice i in Heap.
     - free: free[i] determines vertice i is free.
   - Method:
      - switchArr(int i,int j): switch 2 element in Heap.
      - setup(): setup arr,length,d,Trace,free.
      - enHeap(int i): enHeap at position i.
      - update(int i): update at position i.
      - findPath():implement the abstract method in Graph and write to file.
- About the algorithm complexity, all the setup takes O(n), in the loop, we pop that takes O(log2n),we searching for the adjancy vertice, takes about O(n),in each searching, we update that takes O(log2n). The total cost is about O(n^2log2n).That's pretty smaller than **Ford-Bellman**.

**3.Use Topo order to find path between 2 vertice**
- We call Graph that has no cycles is non-cycle Graph, in that type of Graph, we renumber the vertice of Graph with this principle: If (i,j) is an edge of Graph, after renumber, i must smaller than j. We can simply achive by this method: calcuate all inVer[i], where inVer[i] is the total edges that come to i, then if inVer[i]==0, we renumber it and take it and all the adjacency edges from Graph, substract the correspond verice j that has deleted edges into it. For convinience, we will use stack for storing zero inVer vertice.
- After reorder, the shortest path can be achive by this:
    ```Java
     for(int i=newVertice[S];i<newVertice[F];i++){
                for(int j=i+1;j<=newVertice[F];j++){
                    if(d[oldVertice[j]]>d[oldVertice[i]]+WeightMatrice[oldVertice[i]][oldVertice[j]]){
                        d[oldVertice[j]]=d[oldVertice[i]]+WeightMatrice[oldVertice[i]][oldVertice[j]];
                        Trace[oldVertice[j]]=oldVertice[i];
                    }
                }
            } where oldVerice and newVertice, refer to my notation in source code.
      ```
- Class discription:
   - Attributes:
      - newVertice
      - oldVertice
    - Method:
       - setup(): setup the oldVertice, newVertice, d and Trace
       - reorder(): reorder the Graph
       - findPath(): find path
- Algorithm complexity: 
    - Setup takes O(n).
    - reorder takes O(n^2) for convert from WeightMatrice into Inver, O(n^2) for later loop.
    - O(n^2) for find path.
    - The overall is O(n^2).
