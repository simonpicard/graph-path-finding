# graph-path-finding

## Quickstart

```
git clone git@github.com:simonpicard/graph-path-finding.git
cd graph-path-finding
cd src
make
java Main graph.txt CI
make clean
```
## Introduction 

In the context of the 2nd bachelor's degree course in computer science, we have been asked to carry out a project aimed at applying the concepts we have studied. The program is entirely coded in Java and is exclusively accessible on the command line.

This application is in fact a GPS that allows, from a given bar, to list the fastest routes to other bars as well as a path passing through each of the bars. This program is designed to run on a smartphone.

In the rest of this document, the different algorithms, problems encountered and their resolutions will be explained with some illustrations in pseudo-code.

## Use

### Execution

To run, this application receives in parameter information allowing the creation of a graph and a starting bar. The graph must be represented by a txt file where :
- The first line contains the number of points of the graph, in our case, bars
- The following lines indicate the name of the different bars
- And finally, the relations between each of them in the form "bar_departure" "bar_arrival" "time".
It is not necessary to provide all the relations to run the program but the results will be missing or less efficient.

```
7
CI
CP
CM
CD
CK
...
CI CM 4 
CI CK 16 
CI CP 4 
CP ISEP 2 
CP CM 1 
...
```

Above, an example of a graph description file

### Result

Here is an example of the execution of the application:

```
>./main graph.txt CI
Plus courts chemins :
CI => CP : 4.0 min
CI => CM : 4.0 min
CI => CD : 1.0 min
CI => CM => CK : 8.0 min CI => CP => ISEP : 6.0 min CI => CIG : 8.0 min
Chemin passant par tous les bars :
CI => CD => CM => CK => CIG => CP => ISEP : 20.0 min
```

## Parser

### How it works

In order to retrieve the information from the text file, a class had to be implemented. This one works as follows:
- Adding the content of the file in the buffer 
- The reading of the buffer
- Closing the buffer

#### Creation of the buffer

For the sake of speed, the use of a buffer was the most appropriate solution because caching allows you to avoid having to access the mass memory and thus avoid long and useless trips to it.

```
FileInputStream input = new FileInputStream(new File(fileName)) ; 
InputStreamReader inputReader = new InputStreamReader(input) ; 
m_buffer = new BufferedReader(inputReader) ;
```

The file is first opened and read from the File and FileInputStream functions. The FileInputStream function represents the file by byte and not by character. So you have to use the InputStreamReader function to do the translation. And finally introduce this translation in the buffer.

#### Reading the buffer

Since the buffer provides the readLine method, we can browse the file content easily. Since the readLine method returns null when we reach the end of the file, we can simply use it in a while loop.

#### Closing the bffer

In order to free the memory, it is imperative to close the bffer with the close method.

### Usage

#### Creation of a list

To be able to identify each bar that will be found as an index in the graph, their names have been placed in a Vector list where the index will be the same in both the list and the graph.

#### Creation of the graph

The realization of the graph in the form of a matrix turned out to be the most adequate solution in our case. First, we had to initialize an empty square matrix of the size of the graph elements where the value [i][j] corresponds to the time it takes to go from bar i to bar j. If this information does not exist, its value will be 0. Then, an update of the matrix is performed to place the correct values according to the text file that the function has received.

## Path

The Path class, after having received the information given in parameters, looks for the different possible paths between each bar from the starting bar as well as the shortest path aproximately covering each bar exactly once.

To solve these two problems, the implementation of the Dijkstra algorithm was necessary.

### Dijkstra

To compute the shortest paths from an initial vertex, we can modify Prim's algo- rithm by storing in a vector, which we will call dist, the distance of each vertex from the initial vertex x. When we add a vertex k to the tree under construction, we update the fringe by going through the list of successors of this vertex k. For each vertex t of this list of successors, the distance from vertex x to the vertices of this list is equal to dist[k] + distance from k to t.

The fundamental property on which this algorithm is based is that, in a digraph, the shortest path between a vertex a and a vertex c is composed of the shortest path between vertex a and a vertex b and the shortest path between this vertex b and vertex c, and this for any vertex b of the digraph lying on the shortest path between vertices a and c. The Dijkstra algorithm performs this search for the shortest paths from an initial vertex s to all other vertices of the digraph. To realize this, we manipulate a set M of vertices containing initially all the vertices of the graph except the initial vertex s.

We also manipulate two vectors, dist and prec, such that for each vertex i distinct from vertex s, the value contained in dist[i] indicates the shortest known distance between vertex s and vertex i, and prec[i] takes the number of the vertex preceding vertex i on the shortest path under construction between vertex s and i. Initially, dist[i] contains the weight present on the arc between vertex s and vertex i. If this arc does not exist, the weight is assumed to be infinite. The corresponding entries of the vector prec are all initially worth s, since we initially consider only paths with a single arc between vertex s and all other vertices. Then, by discovering new arcs, we try to lengthen, in number of arcs, the paths, eventually finding that we improve, in terms of distance, the existing situation.

To do this, we perform a loop that removes from the set M the vertex m whose corresponding value in the dist vector, namely dist[m], is the smallest. For each successor vertex y of this vertex m, we check if it belongs to the set M. If the vertex does not belong, it means that it is already processed. Otherwise, we update the vectors dist and prec if, by passing through vertex m, vertex y is closer to the initial vertex s. If dist[m] was infinity when removing the minimum from the set M, this means that vertex m belongs to another component than vertex s. This will then also be the case for all the other vertices potentially still present in M. In this situation, the search for the shortest paths from the vertex s can stop. These vertices still present in the set M are at an infinite distance from the vertex s, value already present for these vertices in the dist vector.

The algorithm uses the backtracking method. Starting from a starting point, the function establishes a list of bars to which the starting bar can go. For each of these bars, in order of increasing distance, we call back the function by indicating that we have passed by this bar. When the function has been called recursively as many times as there are bars in total, a solution has been found. For the sake of optimization, the solution is not necessarily the best, but the first one found.

## Conclusion

The algorithms allowing to search the paths as well, towards each bar, as the one passing by all the bars have been successfully realized.

To satisfy the statement as much as possible, a bash executable file named project could be launched via the command line ./project followed by the file representing the graph and the bar of departure.
