package priority_queue

import (
	"bytes"
	"container/list"
	"errors"
	"fmt"
	"math"
)

type HeapInterface interface {
	Num() uint
	Insert(tag interface{}, key float64) error
	Minimum() (interface{}, float64)
	ExtractMin() (interface{}, float64)
	DecreaseKey(tag interface{}, key float64) error
}

// FibHeap represents a Fibonacci heap.
type FibHeap struct {
	roots       *list.List             // List of roots
	index       map[interface{}]*node  // Hash table of key-value pairs
	treeDegrees map[uint]*list.Element // Hash table of tree degrees
	min         *node                  // Node with the minimum key
	num         uint                   // Number of nodes in the heap
}

// Structure of a Fibonacci heap node
type node struct {
	self     *list.Element // Reference to itself in the parent's children list
	parent   *node         // Reference to the parent node
	children *list.List    // Reference to the list of child nodes
	degree   uint          // Number of child nodes
	position uint          // Key for the node in the treeDegrees hash table
	tag      interface{}   // Value stored in the heap
	key      float64       // Key-priority of the value
	marked   bool          // Indicates whether the node has lost children since becoming a child of another node
}

// Constructor for the heap
func NewFibHeap() *FibHeap {
	heap := new(FibHeap)
	heap.roots = list.New()
	heap.index = make(map[interface{}]*node)
	heap.treeDegrees = make(map[uint]*list.Element)
	heap.num = 0
	heap.min = nil

	return heap
}

// Num returns the total number of values in the heap.
func (heap *FibHeap) Num() uint {
	return heap.num
}

// Insert inserts the input value and key into the heap.
// Attempting to insert a duplicate value will result in an error.
// The valid key range is (-inf, +inf].
// Attempting to insert a key of -inf will result in an error.
// Attempting to insert a value equal to nil will result in an invalid address error.
func (heap *FibHeap) Insert(tag interface{}, key float64) error {
	if tag == nil {
		return errors.New("Input tag is nil ")
	}

	return heap.insert(tag, key)
}

// Minimum returns the value with the minimum key and the key itself in the heap.
// An empty heap will return nil and -inf.
func (heap *FibHeap) Minimum() (interface{}, float64) {
	if heap.num == 0 {
		return nil, math.Inf(-1)
	}

	return heap.min.tag, heap.min.key
}

// ExtractMin returns the value with the minimum key and the key itself in the heap, and then removes them from the heap.
// An empty heap will return nil/-inf and won't remove anything.
func (heap *FibHeap) ExtractMin() (interface{}, float64) {
	if heap.num == 0 {
		return nil, math.Inf(-1)
	}

	min := heap.extractMin()

	return min.tag, min.key
}

// DecreaseKey updates the key of the given value in the heap to the input key.
// If the input key is greater or equal to the current key, or if the key is -inf, an error will be returned.
// If the input value does not exist in the heap, an error will be returned.
func (heap *FibHeap) DecreaseKey(tag interface{}, key float64) error {
	if tag == nil {
		return errors.New("Input tag is nil ")
	}

	if math.IsInf(key, -1) {
		return errors.New("Negative infinity key is reserved for internal usage ")
	}

	if node, exists := heap.index[tag]; exists {
		return heap.decreaseKey(node, key)
	}

	return errors.New("Value is not found ")
}

// String provides some basic debugging information about the heap.
// It returns the total number, size of roots, size of the index, and the current minimum value in the heap.
// It also returns the topology of trees based on a DFS search.
func (heap *FibHeap) String() string {
	var buffer bytes.Buffer

	if heap.num != 0 {
		buffer.WriteString(fmt.Sprintf("Total number: %d, Root Size: %d, Index size: %d,\n", heap.num, heap.roots.Len(), len(heap.index)))
		buffer.WriteString(fmt.Sprintf("Current minimum: key(%f), tag(%v),\n", heap.min.key, heap.min.tag))
		buffer.WriteString(fmt.Sprintf("Heap detail:\n"))
		probeTree(&buffer, heap.roots)
		buffer.WriteString(fmt.Sprintf("\n"))
	} else {
		buffer.WriteString(fmt.Sprintf("Heap is empty.\n"))
	}

	return buffer.String()
}

func probeTree(buffer *bytes.Buffer, tree *list.List) {
	buffer.WriteString(fmt.Sprintf("< "))
	for e := tree.Front(); e != nil; e = e.Next() {
		buffer.WriteString(fmt.Sprintf("%f ", e.Value.(*node).key))
		if e.Value.(*node).children.Len() != 0 {
			probeTree(buffer, e.Value.(*node).children)
		}
	}
	buffer.WriteString(fmt.Sprintf("> "))
}

func (heap *FibHeap) decreaseKey(n *node, key float64) error {
	if key >= n.key {
		return errors.New("New key is not smaller than current key ")
	}

	n.key = key
	if n.parent != nil {
		parent := n.parent
		if n.key < n.parent.key {
			heap.cut(n)
			heap.cascadingCut(parent)
		}
	}

	if n.parent == nil && n.key < heap.min.key {
		heap.min = n
	}

	return nil
}

func (heap *FibHeap) cut(n *node) {
	n.parent.children.Remove(n.self)
	n.parent.degree--
	n.parent = nil
	n.marked = false
	n.self = heap.roots.PushBack(n)
}

func (heap *FibHeap) cascadingCut(n *node) {
	if n.parent != nil {
		if !n.marked {
			n.marked = true
		} else {
			parent := n.parent
			heap.cut(n)
			heap.cascadingCut(parent)
		}
	}
}

func (heap *FibHeap) consolidate() {
	for tree := heap.roots.Front(); tree != nil; tree = tree.Next() {
		heap.treeDegrees[tree.Value.(*node).position] = nil
	}

	for tree := heap.roots.Front(); tree != nil; {
		if heap.treeDegrees[tree.Value.(*node).degree] == nil {
			heap.treeDegrees[tree.Value.(*node).degree] = tree
			tree.Value.(*node).position = tree.Value.(*node).degree
			tree = tree.Next()
			continue
		}

		if heap.treeDegrees[tree.Value.(*node).degree] == tree {
			tree = tree.Next()
			continue
		}

		for heap.treeDegrees[tree.Value.(*node).degree] != nil {
			anotherTree := heap.treeDegrees[tree.Value.(*node).degree]
			heap.treeDegrees[tree.Value.(*node).degree] = nil
			if tree.Value.(*node).key <= anotherTree.Value.(*node).key {
				heap.roots.Remove(anotherTree)
				heap.link(tree.Value.(*node), anotherTree.Value.(*node))
			} else {
				heap.roots.Remove(tree)
				heap.link(anotherTree.Value.(*node), tree.Value.(*node))
				tree = anotherTree
			}
		}
		heap.treeDegrees[tree.Value.(*node).degree] = tree
		tree.Value.(*node).position = tree.Value.(*node).degree
	}

	heap.resetMin()
}

func (heap *FibHeap) insert(tag interface{}, key float64) error {
	if math.IsInf(key, -1) {
		return errors.New("Negative infinity key is reserved for internal usage ")
	}

	if _, exists := heap.index[tag]; exists {
		return errors.New("Duplicate tag is not allowed ")
	}

	node := new(node)
	node.children = list.New()
	node.tag = tag
	node.key = key

	node.self = heap.roots.PushBack(node)
	heap.index[node.tag] = node
	heap.num++

	if heap.min == nil || heap.min.key > node.key {
		heap.min = node
	}

	return nil
}

func (heap *FibHeap) extractMin() *node {
	min := heap.min

	children := heap.min.children
	if children != nil {
		for e := children.Front(); e != nil; e = e.Next() {
			e.Value.(*node).parent = nil
			e.Value.(*node).self = heap.roots.PushBack(e.Value.(*node))
		}
	}

	heap.roots.Remove(heap.min.self)
	heap.treeDegrees[min.position] = nil
	delete(heap.index, heap.min.tag)
	heap.num--

	if heap.num == 0 {
		heap.min = nil
	} else {
		heap.consolidate()
	}

	return min
}

func (heap *FibHeap) link(parent, child *node) {
	child.marked = false
	child.parent = parent
	child.self = parent.children.PushBack(child)
	parent.degree++
}

func (heap *FibHeap) resetMin() {
	heap.min = heap.roots.Front().Value.(*node)
	for tree := heap.min.self.Next(); tree != nil; tree = tree.Next() {
		if tree.Value.(*node).key < heap.min.key {
			heap.min = tree.Value.(*node)
		}
	}
}
