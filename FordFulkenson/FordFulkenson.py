# implement Ford Fulkenson algorithm
# The input is given in the input.txt
# The first line contains 4 integers, represents the number of vertex, number of edges, source vertice and sink vertice
# The next lines contain 3 integers, represent the edge and capacity on that edge
# For simplicity, we assume we solve the problem in simple graph
class Queue:
    '''Queue structure for implementing BFS algorith
    '''
    def __init__(self):
        # initialize Queue array
        self.arr=[]
    def enQueue(self,value):
        self.arr.append(value)
    def deQueue(self):
        value=self.arr[0]
        self.arr.pop(0)
        return value
    def isEmpty(self):
        return True if len(self.arr)==0 else False
class FordFulkenson:
    def readFile(self,file_name='input.txt'):
        '''
        Read data from input file.
        Return True if sucessful, else return False
        '''
        try:
            file=open(file_name,'r')
            line=file.readline().split(' ') #read the first line and use string seperate
            self.n=int(line[0]) # number of vertex
            self.m=int(line[1]) # number of edges
            self.S=int(line[2]) # starting vertice
            self.F=int(line[3]) # finishing vertice
            self.matrix=[[0 for i in range(self.n)]for j in range(self.n)] # matrix of edges in Graph
            for i in range(self.m):
                line=file.readline().split(' ')
                p=int(line[0]) # The first vertice
                q=int(line[1]) # The second vertice
                self.matrix[p][q]=int(line[2]) #capacity of the edge (p,q)
            file.close()
            return True
        except IOError:
            print('Error when opening file')
            return False
        except Exception as e:
            print('An error has occur')
            return False
    def BFS(self):
        '''
        BFS algorithm in Graph to find the path from S to F in Graph
        return a tuple  if sucessfully find a path, else return None
        '''
        queue=Queue() #initialize a new queue
        queue.enQueue(self.S) #add the starting vertice to Queue
        Trace=[30000 for i in range(self.n)] #The trace array
        Trace[self.S]=-1
        delta=[30000 for i in range(self.n)] #the minimum weight on the path from S to F
        while(not queue.isEmpty()):
            vertice=queue.deQueue() #pop from queue
            if(vertice==self.F):return (Trace,delta[self.F])
            for i in range(self.n):
                #iterate through all vertice in Graph
                if(self.matrix[vertice][i]!=0 and Trace[i]==30000):
                    queue.enQueue(i) #push i to queue if i is connected to vertice and i is not visited
                    if(abs(self.matrix[vertice][i])<delta[vertice]):
                        delta[i]=abs(self.matrix[vertice][i])
                    else:delta[i]=delta[vertice]
                    Trace[i]=vertice #add vertice to Trace[i]
        return None
    def write_to_file(self,max_flow,file_name='output.txt'):
        try:
            file=open(file_name,'w')
            file.write("The maximum flow from S to F = "+str(max_flow)+'\n')
            for i in range(self.n):
                for j in range(self.n):
                    if(self.matrix[i][j]<0):
                        file.write('('+str(j)+','+str(i)+')'+'='+str(-self.matrix[i][j])+'\n')
            file.close()
        except IOError:
            print('Error when opening output file')
        except Exception as e:
            print('An error has occured')
            print(e)
    def Ford_Fulkenson(self):
        '''
        The ford-fulkenson algorithm
        '''
        self.readFile()
        path=self.BFS() #use BFS to find path from S to F
        total_flow=0 #the max_flow from S to F
        while(path!=None):
            Trace=path[0]
            lowest_weight=path[1]
            total_flow+=lowest_weight
            # we gain the flow in the path we find from S to F
            j=self.F
            i=Trace[j]
            while(i!=-1):
                #We consider 2 cases, if (i,j) is a forward vertice
                if(self.matrix[i][j]>0):
                    #We decrease weight of i,j by lowest_weight
                    self.matrix[i][j]-=lowest_weight
                    self.matrix[j][i]-=lowest_weight
                else:
                    #We increase weight of (i,j) by the lowest weight
                    self.matrix[i][j]+=lowest_weight
                    self.matrix[j][i]+=lowest_weight
                j=i
                i=Trace[i]
            path=self.BFS()
        self.write_to_file(max_flow=total_flow)
graph=FordFulkenson()
graph.Ford_Fulkenson()