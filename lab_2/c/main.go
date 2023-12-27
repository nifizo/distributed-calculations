package main

import (
	"fmt"
	"math"
	"math/rand"
	"strconv"
	"time"
)

type Monk struct {
	energy int
	name   string
}

func battle(monk1, monk2 *Monk, winnerCh chan<- *Monk) {

	fmt.Println(monk1.name, monk1.energy, "vs", monk2.name, monk2.energy)
	time.Sleep(1000 * time.Millisecond)

	if monk1.energy > monk2.energy {
		winnerCh <- monk1
	} else {
		winnerCh <- monk2
	}
}

func main() {
	monkNumber := int(math.Pow(float64(2), float64(rand.Intn(8)))) // 2^0 to 2^7
	monks := make([]*Monk, monkNumber)
	for i := 0; i < monkNumber; i++ {
		monks[i] = &Monk{rand.Intn(100), "Monk " + strconv.Itoa(i)}
	}

	winnerCh := make(chan *Monk)
	defer close(winnerCh)

	round := 1
	for len(monks) > 1 {
		fmt.Printf("Round %d:\n", round)
		round++

		for i := 0; i < len(monks); i += 2 {
			go battle(monks[i], monks[i+1], winnerCh)
		}

		// Update monks list for next round
		newMonks := make([]*Monk, 0)
		for i := 0; i < len(monks); i += 2 {
			winner := <-winnerCh
			fmt.Println("Winner: ", winner.name, " with Energy: ", winner.energy)
			newMonks = append(newMonks, winner)
		}

		monks = newMonks
		fmt.Println("Monks left:", len(monks))
		time.Sleep(3000 * time.Millisecond)
	}

	fmt.Println("Final winner:", monks[0].name, " with Energy: ", monks[0].energy)
}
