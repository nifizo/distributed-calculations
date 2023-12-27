package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"sync"
	"task-c/barrier"
)

type ArrayComp struct {
	barrier     *barrier.Barrier
	sumsMonitor *SumMonitor
	arr         []int
	id          int
	finishG     *sync.WaitGroup
}

func (comp *ArrayComp) doSumComparison() {
	for {
		sum := calculateSum(comp.arr)
		comp.sumsMonitor.set(comp.id, sum)
		comp.printArray(sum)
		comp.barrier.Await()
		if comp.sumsMonitor.equal() {
			break
		} else {
			comp.changeRandom()
		}
		comp.barrier.Await()
	}
	comp.finishG.Done()
}

func (comp *ArrayComp) changeRandom() {
	toDO := rand.Intn(3)
	switch toDO {
	case 0:
		return
	case 1:
		index := rand.Intn(len(comp.arr))
		if comp.arr[index] < 10 {
			comp.arr[index]++
		}
		return
	case 2:
		index := rand.Intn(len(comp.arr))
		if comp.arr[index] > 0 {
			comp.arr[index]--
		}
		return
	default:
		panic("Invalid choice")
	}
}

func (comp *ArrayComp) printArray(sum int) {
	str := ""
	str += "array-" + strconv.Itoa(comp.id) + " [ "
	for i := 0; i < len(comp.arr); i++ {
		str += strconv.Itoa(comp.arr[i])
		str += " "
	}
	fmt.Println(str+" ] ", strconv.Itoa(sum))
}

func calculateSum(arr []int) int {
	sum := 0
	for i := 0; i < len(arr); i++ {
		sum += arr[i]
	}
	return sum
}

type SumMonitor struct {
	sums []int
}

func (s *SumMonitor) set(id int, sum int) {
	s.sums[id] = sum
}
func (s *SumMonitor) equal() bool {
	firstElement := s.sums[0]
	for _, num := range s.sums {
		if num != firstElement {
			return false
		}
	}
	return true
}
func randomArray(elements int) []int {
	var result []int

	for i := 0; i < elements; i++ {
		randomNumber := rand.Intn(10)
		result = append(result, randomNumber)
	}

	return result
}

func main() {
	N := 3
	elements := 10
	monitor := SumMonitor{make([]int, N)}
	barrier := barrier.MakeBarrier(N)
	group := sync.WaitGroup{}
	group.Add(N)
	for i := 0; i < N; i++ {
		arrayComp := ArrayComp{barrier, &monitor, randomArray(elements), i, &group}
		go arrayComp.doSumComparison()
	}
	group.Wait()
}
