package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Table struct {
	sync.Mutex
	item1, item2 string
}

var smokerSem = make([]chan bool, 3)
var agentSem = make(chan bool, 1)

func smoker(cigarette string, id int, table *Table) {
	for {
		<-smokerSem[id]
		table.Lock()
		if table.item1 != cigarette && table.item2 != cigarette {
			fmt.Println("Smoker with", cigarette, "smokes")
			time.Sleep(time.Millisecond * 500)
			agentSem <- true
		}
		table.Unlock()
	}
}

func agent(table *Table) {
	for {
		table.Lock()
		table.item1 = getRandomItem()
		table.item2 = getRandomItem()
		fmt.Println("Agent puts", table.item1, "and", table.item2, "on the table")

		if table.item1 != "tobacco" && table.item2 != "tobacco" {
			smokerSem[0] <- true // signal to smoker with tobacco
		} else if table.item1 != "paper" && table.item2 != "paper" {
			smokerSem[1] <- true // signal to smoker with paper
		} else if table.item1 != "matches" && table.item2 != "matches" {
			smokerSem[2] <- true // signal to smoker with matches
		}

		table.Unlock()

		<-agentSem // wait for a smoker to finish
	}
}

func getRandomItem() string {
	items := []string{"tobacco", "paper", "matches"}
	return items[rand.Intn(len(items))]
}

func main() {
	rand.Seed(time.Now().UnixNano())
	table := &Table{}

	for i := range smokerSem {
		smokerSem[i] = make(chan bool, 1)
	}

	go smoker("tobacco", 0, table)
	go smoker("paper", 1, table)
	go smoker("matches", 2, table)

	agent(table)
}
