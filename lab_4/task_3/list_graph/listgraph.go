package list_graph

import (
	"math"
	"sync"
	"task-3/priority_queue"
)

// Edge represents a weighted edge in a graph
type Edge struct {
	src, dest int
	weight    float64
}

// Graph represents a weighted graph using an adjacency list
type Graph struct {
	edges    []Edge
	vertices []int

	lock sync.RWMutex
}

func (g *Graph) GetVerticesNumber() int {
	return len(g.vertices)
}

func (g *Graph) GetEdgesNumber() int {
	return len(g.edges)
}

// AddVertex adds a new vertex to the graph
func (g *Graph) AddVertex(vertex int) {
	g.lock.Lock()
	defer g.lock.Unlock()

	var found bool = false
	for i := 0; i < len(g.vertices); i++ {
		if g.vertices[i] == vertex {
			found = true
			break
		}
	}

	if !found {
		g.vertices = append(g.vertices, vertex)
	}
}

func (g *Graph) RemoveVertex(vertex int) {
	g.lock.Lock()
	defer g.lock.Unlock()

	for i, v := range g.vertices {
		if v == vertex {
			g.vertices = append(g.vertices[:i], g.vertices[i+1:]...)
			break
		}
	}

	for i := 0; i < len(g.edges); i++ {
		if g.edges[i].src == vertex || g.edges[i].dest == vertex {
			g.edges = append(g.edges[:i], g.edges[i+1:]...)
			i--
		}
	}
}

// AddEdge adds a new weighted edge to the graph
func (g *Graph) AddEdge(src, dest int, weight float64) {
	g.lock.Lock()
	defer g.lock.Unlock()

	edge := Edge{src, dest, weight}
	g.edges = append(g.edges, edge)

	var found bool = false
	for i := 0; i < len(g.vertices); i++ {
		if g.vertices[i] == src {
			found = true
			break
		}
	}

	if !found {
		g.vertices = append(g.vertices, src)
	}

	found = false
	for i := 0; i < len(g.vertices); i++ {
		if g.vertices[i] == dest {
			found = true
			break
		}
	}

	if !found {
		g.vertices = append(g.vertices, dest)
	}
}

// RemoveEdge removes an edge from the graph
func (g *Graph) RemoveEdge(src, dest int) {
	g.lock.Lock()
	defer g.lock.Unlock()

	for i, edge := range g.edges {
		if edge.src == src && edge.dest == dest {
			g.edges = append(g.edges[:i], g.edges[i+1:]...)
			break
		}
	}
}

// SetWeight sets the weight of an edge
func (g *Graph) SetWeight(src, dest int, weight float64) {
	g.lock.Lock()
	defer g.lock.Unlock()

	for i, edge := range g.edges {
		if edge.src == src && edge.dest == dest {
			g.edges[i].weight = weight
			break
		}
	}
}

// BellmanFord detects negative weight cycles and returns the shortest paths
// from the source vertex to all other vertices in the graph
func (g *Graph) BellmanFord(src int) ([]float64, []int, bool) {
	g.lock.RLock()
	defer g.lock.RUnlock()

	n := len(g.vertices)
	dist := make([]float64, n)
	prev := make([]int, n)

	// Initialize distance array with infinity and previous array with -1
	for i := 0; i < n; i++ {
		dist[i] = math.MaxInt32
		prev[i] = -1
	}

	// Set distance from source vertex to itself as 0
	dist[src] = 0

	// Relax edges n-1 times
	for i := 1; i < n; i++ {
		for _, edge := range g.edges {
			u, v, w := edge.src, edge.dest, edge.weight
			if dist[u]+w < dist[v] {
				dist[v] = dist[u] + w
				prev[v] = u
			}
		}
	}

	// Check for negative weight cycles by relaxing edges one more time
	for _, edge := range g.edges {
		u, v, w := edge.src, edge.dest, edge.weight
		if dist[u]+w < dist[v] {
			return nil, nil, true
		}
	}

	return dist, prev, false
}

// Dijkstra returns the shortest paths from the source vertex to all other vertices
// in the graph using Dijkstra's algorithm
func (g *Graph) Dijkstra(src int, pq priority_queue.HeapInterface) ([]float64, []int) {
	g.lock.RLock()
	defer g.lock.RUnlock()

	n := len(g.vertices)
	dist := make([]float64, n)
	prev := make([]int, n)
	visited := make([]bool, n)

	// Initialize distance array with infinity and previous array with -1
	for i := 0; i < n; i++ {
		dist[i] = math.Inf(1)
		prev[i] = -1
	}

	// Set distance from source vertex to itself as 0
	dist[src] = 0

	// Insert source vertex into priority queue
	pq.Insert(src, float64(dist[src]))

	for pq.Num() > 0 {
		// Extract minimum distance vertex from priority queue
		u, _ := pq.ExtractMin()

		if visited[u.(int)] {
			continue
		}

		visited[u.(int)] = true

		// Relax edges
		for _, edge := range g.edges {
			if edge.src == u.(int) {
				v := edge.dest
				w := edge.weight
				if dist[u.(int)]+w < dist[v] {
					dist[v] = dist[u.(int)] + w
					prev[v] = u.(int)
					pq.Insert(v, float64(dist[v]))
				}
			}
		}
	}

	return dist, prev
}

// Dijkstra returns the shortest path from the source vertex to the end vertex
func (g *Graph) DijkstraOneToOne(src, end int, pq priority_queue.HeapInterface) (float64, []int) {
	g.lock.RLock()
	defer g.lock.RUnlock()

	n := len(g.vertices)
	dist := make([]float64, n)
	prev := make([]int, n)
	visited := make([]bool, n)

	// Initialize distance array with infinity and previous array with -1
	for i := 0; i < n; i++ {
		dist[i] = math.Inf(1)
		prev[i] = -1
	}

	// Set distance from source vertex to itself as 0
	dist[src] = 0

	// Insert source vertex into priority queue
	pq.Insert(src, dist[src])

	for pq.Num() > 0 {
		// Extract minimum distance vertex from priority queue
		u, _ := pq.ExtractMin()

		if visited[u.(int)] {
			continue
		}

		visited[u.(int)] = true

		// Relax edges
		for _, edge := range g.edges {
			if edge.src == u.(int) {
				v := edge.dest
				w := edge.weight
				if dist[u.(int)]+w < dist[v] {
					dist[v] = dist[u.(int)] + w
					prev[v] = u.(int)
					pq.Insert(v, float64(dist[v]))
				}
			}
		}

		// If we've visited the end vertex, we can stop early
		if u.(int) == end {
			break
		}
	}

	// Build the shortest path from source to end vertex
	path := []int{}
	if dist[end] < math.Inf(1) {
		for j := end; j != -1; j = prev[j] {
			path = append([]int{j}, path...)
		}
	}

	return dist[end], path
}
