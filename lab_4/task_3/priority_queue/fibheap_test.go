package priority_queue

import (
	"math"
	"testing"
)

func TestPriorityQueue(t *testing.T) {
	heap := NewFibHeap()

	// Test Insert and Num
	if err := heap.Insert("A", 5.0); err != nil {
		t.Errorf("Error inserting value A: %v", err)
	}
	if err := heap.Insert("B", 3.0); err != nil {
		t.Errorf("Error inserting value B: %v", err)
	}
	if num := heap.Num(); num != 2 {
		t.Errorf("Expected 2 values in the heap, got %d", num)
	}

	// Test Minimum
	minValue, minKey := heap.Minimum()
	if minValue != "B" || minKey != 3.0 {
		t.Errorf("Expected minimum value B with key 3.0, got %v with key %f", minValue, minKey)
	}

	// Test ExtractMin
	extractedValue, extractedKey := heap.ExtractMin()
	if extractedValue != "B" || extractedKey != 3.0 {
		t.Errorf("Expected extracted value B with key 3.0, got %v with key %f", extractedValue, extractedKey)
	}
	if num := heap.Num(); num != 1 {
		t.Errorf("Expected 1 value in the heap after extraction, got %d", num)
	}

	// Test DecreaseKey
	if err := heap.DecreaseKey("A", 2.0); err != nil {
		t.Errorf("Error decreasing key for value A: %v", err)
	}
	minValue, minKey = heap.Minimum()
	if minValue != "A" || minKey != 2.0 {
		t.Errorf("Expected minimum value A with key 2.0, got %v with key %f", minValue, minKey)
	}
}

func TestPriorityQueueWithErrors(t *testing.T) {
	heap := NewFibHeap()

	// Test inserting a duplicate value
	if err := heap.Insert("A", 5.0); err != nil {
		t.Errorf("Error inserting value A: %v", err)
	}
	if err := heap.Insert("A", 3.0); err == nil {
		t.Error("Expected an error when inserting a duplicate value, got nil")
	}

	// Test inserting a value with nil tag
	if err := heap.Insert(nil, 3.0); err == nil {
		t.Error("Expected an error when inserting a value with a nil tag, got nil")
	}

	// Test inserting a value with -inf key
	if err := heap.Insert("B", math.Inf(-1)); err == nil {
		t.Error("Expected an error when inserting a value with -inf key, got nil")
	}

	// Test decreasing key for a non-existing value
	if err := heap.DecreaseKey("C", 2.0); err == nil {
		t.Error("Expected an error when decreasing key for a non-existing value, got nil")
	}

	// Test decreasing key to a value greater than or equal to the current key
	if err := heap.DecreaseKey("A", 5.0); err == nil {
		t.Error("Expected an error when decreasing key to a value greater than or equal to the current key, got nil")
	}

	// Test decreasing key to -inf
	if err := heap.DecreaseKey("A", math.Inf(-1)); err == nil {
		t.Error("Expected an error when decreasing key to -inf, got nil")
	}
}
