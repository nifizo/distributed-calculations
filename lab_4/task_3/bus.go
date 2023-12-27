package main

import (
	"fmt"
	"math/rand"
	"sync"
	"task-3/list_graph"
	"task-3/priority_queue"
	"time"
)

func main() {
	g := &list_graph.Graph{}
	wg := &sync.WaitGroup{}

	// Initialize graph with some vertices and edges
	for i := 0; i < 10; i++ {
		g.AddVertex(i)
		if i > 0 {
			g.AddEdge(i-1, i, float64(i))
		}
	}

	// Start threads
	wg.Add(4)
	go changeTicketPrice(g, wg)
	go addRemoveVoyages(g, wg)
	go addRemoveCities(g, wg)
	go findPaths(g, wg)

	wg.Wait()
}

func changeTicketPrice(g *list_graph.Graph, wg *sync.WaitGroup) {
	defer wg.Done()

	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 10; i++ {
		src := rand.Intn(g.GetVerticesNumber())
		dest := rand.Intn(g.GetVerticesNumber())
		price := rand.Float64() * 100

		g.SetWeight(src, dest, price)
		fmt.Printf("Changed ticket price from %d to %d to %.2f\n", src, dest, price)

		time.Sleep(1 * time.Second)
	}
}

func addRemoveVoyages(g *list_graph.Graph, wg *sync.WaitGroup) {
	defer wg.Done()

	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 5; i++ {
		src := rand.Intn(g.GetVerticesNumber())
		dest := rand.Intn(g.GetVerticesNumber())
		price := rand.Float64() * 100

		g.AddEdge(src, dest, price)
		fmt.Printf("Added voyage from %d to %d with price %.2f\n", src, dest, price)

		time.Sleep(2 * time.Second)

		g.RemoveEdge(src, dest)
		fmt.Printf("Removed voyage from %d to %d\n", src, dest)

		time.Sleep(2 * time.Second)
	}
}

func addRemoveCities(g *list_graph.Graph, wg *sync.WaitGroup) {
	defer wg.Done()

	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 5; i++ {
		vertex := g.GetVerticesNumber() + i

		g.AddVertex(vertex)
		fmt.Printf("Added city %d\n", vertex)

		time.Sleep(2 * time.Second)

		g.RemoveVertex(vertex)
		fmt.Printf("Removed city %d\n", vertex)

		time.Sleep(2 * time.Second)
	}
}

func findPaths(g *list_graph.Graph, wg *sync.WaitGroup) {
	defer wg.Done()

	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 10; i++ {
		src := rand.Intn(g.GetVerticesNumber())
		dest := rand.Intn(g.GetVerticesNumber())

		price, path := g.DijkstraOneToOne(src, dest, priority_queue.NewFibHeap())
		fmt.Printf("Price from %d to %d: %d. Path: %v\n", src, dest, price, path)

		time.Sleep(1 * time.Second)
	}
}
