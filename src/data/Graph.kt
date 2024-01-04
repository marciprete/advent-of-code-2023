package data

import java.util.*
import kotlin.collections.ArrayList

class WeightedPair<T>(val vertex: Vertex<T>, private val weight: Double) : Comparable<WeightedPair<T>> {
    override fun compareTo(other: WeightedPair<T>): Int {
       return weight.compareTo(other.weight)
    }
}
data class Vertex<T> (val data: T) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vertex<*>

        return data == other.data
    }

    override fun hashCode(): Int {
        return data?.hashCode() ?: 0
    }
}
data class Edge<T> (val source: Vertex<T>, val destination: Vertex<T>, val weight: Double? = null)

class AdjacencyList<T> {
    private val adjacencyMap = mutableMapOf<Vertex<T>, ArrayList<Edge<T>>>()

    fun createVertex(data: T): Vertex<T> {
//        val vertex = Vertex(adjacencyMap.count(), data)
        val vertex = Vertex(data)
        adjacencyMap[vertex] = arrayListOf()
        return vertex
    }

    fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double? = 0.0) {
        val edge = Edge(source, destination, weight)
        adjacencyMap[source]?.add(edge)
    }

    fun size() : Int {
        return adjacencyMap.size
    }

    fun longestPath(from: Vertex<T>, to: Vertex<T>) : Double? {

        /*1) Initialize distances of all vertices as negative infinite.*/
        val distances = this.adjacencyMap.keys.associateWith { 0.0 }.toMutableMap()
        /*
        2) Create an empty priority_queue pq.  Every item
           of pq is a pair (weight, vertex). Weight (or
           distance) is used  as first item  of pair
           as first item is by default used to compare
           two pairs
           */
        val priorityQueue: PriorityQueue<WeightedPair<T>> = PriorityQueue(this.size())
        /*
        3) Insert source vertex into pq and make its
           distance as 0.
           */
        priorityQueue.add(WeightedPair(from, 0.0))
        distances[from] = 0.0
        /*
        4) While either pq doesn't become empty
            a) Extract minimum distance vertex from pq.
               Let the extracted vertex be u.
            b) Loop through all adjacent of u and do
               following for every vertex v.
                   // If there is a shorter path to v
                   // through u.
                   If dist[v] > dist[u] + weight(u, v)
                       (i) Update distance of v, i.e., do
                             dist[v] = dist[u] + weight(u, v)
                       (ii) Insert v into the pq (Even if v is
                            already there)
                            */
        while (priorityQueue.isNotEmpty()) {
            val vertex = priorityQueue.poll().vertex
            adjacencyMap[vertex]?.forEach {
                val sibling = it.destination
                if(distances[sibling]!! < (distances[vertex]!! + it.weight!!)) {
                    distances[sibling] = distances[vertex]!! + it.weight;
                    priorityQueue.add(WeightedPair(sibling, distances[vertex]!!));
                }
            }
        }
        /*5) Print distance array dist[] to print all shortest
           paths.
         */
        println("Vertex Distance from Source");
        distances.forEach {
            println("${it.key}: ${it.value}")
        }
        return distances[to]
    }


    override fun toString(): String {
        return buildString {
            adjacencyMap.forEach { (vertex, edges) ->
                val edgeString = edges.joinToString { it.destination.data.toString()+"(${it.weight})" }
                append("${vertex.data} -> [$edgeString]\n")
            }
        }
    }
}
