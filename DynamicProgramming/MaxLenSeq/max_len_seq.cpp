#include <iostream>
#include <fstream>
using namespace std;

int binary_search(int *seq,int *s,int s_min,int s_max,int compare){
    if(compare<seq[s[s_max]])return s_max;
    if(compare>seq[s[1]])return 0;
    int i=(s_min+s_max)/2;
    while(true){
        if(seq[s[i]]==compare)return i-1;
        else if(seq[s[i]]>compare){
            if(seq[s[i+1]]<compare)return i;
            else{
                s_min=i;
                i=(s_min+s_max)/2;
            }
        }
        else{
            s_max=i;
            i=(s_min+s_max)/2;
        }
    }
}

int main(){
    freopen("input.txt","r",stdin);
    int n; //number of elements in the sequence
    cin>>n;
    int *seq=new int[n+2];//initialize the seq array
    seq[0]=-20000;seq[n+1]=20000;
    for(int i=1;i<=n;i++)cin>>seq[i];
    int *L=new int[n+2];//initialize the array L, L[i] indicates the longest increasing sequence start from seq[i]
    int *T=new int[n+2];//traceback array
    int *s=new int[n+2];//StartOf array
    L[n+1]=0;
    s[0]=n+1;
    T[0]=n+1;
    for(int i=0;i<=n;i++)s[i]=0;
    int m=0;
    for(int i=n;i>=0;i--){
        int p=binary_search(seq,s,0,m,seq[i]);
        L[i]=L[s[p]]+1;
        T[i]=s[p];
        if(L[i]>m){
            m=L[i];
            s[m]=i;
        }
        else{
            if(seq[i]>seq[s[L[i]]])s[L[i]]=i;
        }
    }
    ofstream f;
    f.open("output.txt",ios::out);
    f<<m<<endl;
    int i=T[0];
    f<<"The maximum length of increasing sequence:"<<endl;
    while(i!=0){
        f<<i<<" ";
        i=T[i];
    }
    f.close();
}

