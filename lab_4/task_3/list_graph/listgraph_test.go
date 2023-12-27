package list_graph

import (
	"task-3/priority_queue"
	"testing"
)

func TestGraph(t *testing.T) {
	// Create a new graph
	g := &Graph{}

	// Add vertices and edges
	g.AddVertex(0)
	g.AddVertex(1)
	g.AddEdge(0, 1, 5.0)

	// Verify the number of vertices and edges
	if len(g.vertices) != 2 {
		t.Errorf("Expected 2 vertices, got %v", len(g.vertices))
	}

	if len(g.edges) != 1 {
		t.Errorf("Expected 1 edge, got %v", len(g.edges))
	}

	// Run Bellman-Ford algorithm
	dist, prev, hasNegativeCycle := g.BellmanFord(0)
	if hasNegativeCycle {
		t.Error("Expected no negative weight cycle")
	}

	// Verify the shortest distances
	expectedDist := []float64{0, 5.0}
	if !float64SlicesAreEqual(dist, expectedDist) {
		t.Errorf("Bellman-Ford: Expected distances %v, got %v", expectedDist, dist)
	}

	// Verify the shortest path
	expectedPrev := []int{-1, 0}
	if !intSlicesAreEqual(prev, expectedPrev) {
		t.Errorf("Bellman-Ford: Expected previous vertices %v, got %v", expectedPrev, prev)
	}

	// Run Dijkstra's algorithm
	dist, prev = g.Dijkstra(0, priority_queue.NewFibHeap())

	// Verify the shortest distances
	expectedDist = []float64{0, 5.0}
	if !float64SlicesAreEqual(dist, expectedDist) {
		t.Errorf("Dijkstra: Expected distances %v, got %v", expectedDist, dist)
	}

	// Verify the shortest path
	expectedPrev = []int{-1, 0}
	if !intSlicesAreEqual(prev, expectedPrev) {
		t.Errorf("Dijkstra: Expected previous vertices %v, got %v", expectedPrev, prev)
	}
}

func float64SlicesAreEqual(slice1, slice2 []float64) bool {
	if len(slice1) != len(slice2) {
		return false
	}
	for i := range slice1 {
		if slice1[i] != slice2[i] {
			return false
		}
	}
	return true
}

func intSlicesAreEqual(slice1, slice2 []int) bool {
	if len(slice1) != len(slice2) {
		return false
	}
	for i := range slice1 {
		if slice1[i] != slice2[i] {
			return false
		}
	}
	return true
}
