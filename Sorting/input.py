#This is the file to generate data for sorting
import numpy as np
import time
np.random.seed(2)
data=np.random.permutation(30000)
try:
    openFile=open('input.txt','w')
    for i in data:openFile.write(str(i)+' ')
except Exception as ex:
    print("Error:")
    print(ex)
start_time=time.time()
data=np.sort(data,kind='heapsort')
end_time=time.time()
print(end_time-start_time)
print(data)
