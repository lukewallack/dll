****************
* dll - Double Linked List project
* CS 221
* 11/18/2025
* Luke Wallack
**************** 

OVERVIEW:
 This program consists of a double linked list implementation, using Node objects that contain pointers to the previous and next Node in the list. These Node's contain references to elements (of integer types in the test class case, though this can be changed depending on the use case) as well.


INCLUDED FILES:
 * ListTester.java - source file/test class, tests functionality of each method in the DLL implementation
 * IUDoubleLinkedList.java - source file, implements functionality to methods declared in the IndexedUnsortedList interface.
 * IndexedUnsortedList.java - interface defining methods to be implemented in various list classes, double linked list in this case.
 * Node.java - source file, defines Node objects which contain references to elements, and a next/previous node reference to create lists.
 * README.md - this file


COMPILING AND RUNNING:
 From the directory containing all source files, compile the
 test class with the following command in the console:
 $ javac ListTester.java

 Run the compiled class file with the command:
 $ java ListTester

 The console will display the test results of the list implementation.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:
 The important part of this program is the IUDoubleLinkedList source file, and subsequently the Node source file. Essentially, the double linked list implements IndexedUnsortedList, and defines all the methods present in there. Double linked list is a list that contains Node objects, which have pointers to an element, the previous node in the list, and the next node in the list. This makes it easy to traverse through the list, you can just follow a chain of Node's via their previous/next Node.

 One of the important elements of the program is the DLLIterator, which implements ListIterator<T>. The list iterator is capable of traversing a chain of Node objects within the list, either going forwards by following the "next" pointers, or backwards by following the "previous" pointers via Node.getNext() and Node.getPrevious(). This makes it very easy and efficient to locate elements, providing a constant runtime for each individual change/move within the list.

 In general, the list is quite simple to understand. The key to understanding the program is understanding and visualizing the Node class. You can think of this list of Nodes as a linear list. There is one node at the beginning, which is called the head, and this node points to the next node in the list. The next node is followed by other nodes, and each node points to the node before it, as well as after it, until you get to the end of the list. The end of the list contains the final node: the tail. The tail has no next node, so getNext() will return null.

 Changes to the list are made via the setNext and setPrevious methods in the Node class. In order for a Node to be considered present in the list, it must have a node that points to it, and it must also point to another node (whether that be the previous or next node). That means that, in order to remove a Node from the list, you must alter the target's previous and next node. It is difficult to describe this process in words, so I will give an example:
 See the list of nodes [A,B,C]:
 In this list, A, B, and C are nodes in our double linked list. Each node has a reference to an element, and two other references, next and previous. Node A has no previous, but B is returned by A.getNext(), and is our head node. B points to A and C as the previous and next Nodes respectively. C is the tail node, and has no next, but points to B as it's previous node. To remove node B from the list, we use the setNext method on A: A.setNext(B.getNext()), which will change A's next node from B to C. Node A is no longer connected to node B. However, node C is still connected to node B via previous. So, we use setPrev() on C: C.setPrev(B.getPrev()). This changes C's previous node from B to A. Now, nodes A and C are no longer connected to node B. It can be removed from the list, and then the element can be returned in the remove method.

 I skipped over how to locate an element in the previous description, which is also an important algorithm to understand. To locate an element in the list, you start from the head node, and as long as there is a next node, you will use Node.getNext() until the next node's element is equal to the element you are trying to locate. You use this algorithm every time you want to remove or add an element (unless you are trying to remove/add the first or last element). Adding is also very similar to removing; you create a new node, passing through an element to be assigned to the element reference, and connect it via the setNext and setPrev methods.


TESTING:
 This program was easy to test, because I could use the test class that I spent weeks developing at this point, which contains around 9000 tests of 80+ change scenarios. This means I was able to effectively test every method, and it made debugging super easy. Because of this test class, the program can handle bad input via exceptions, and I would say that the program is idiot-proof, since it will tell you exactly what's wrong in the console. As far as I know, there aren't any bugs (assuming I trust what the test class says).


DISCUSSION:
 There were a lot of issues I encountered when trying to implement the Double Linked List, so I will just name some of the more notable ones. To clarify, actually implementing the methods for DLL was not that difficult, since it was pretty similar to the single linked list I made before. However, the ListIterator was a whole different beast. I had so many issues with creating the constructor for the list iterator that required a starting index. First, I forgot to check whether or not the starting index was valid, which I overlooked for way too long until I noticed it during debugging. Also, I was failing to correctly instantiate the tail variable, which was causing a bunch of null pointer exceptions that I also noticed after debugging. Besides these two things, there were a bunch of other minor things that I cannot even begin to remember, but the most time-consuming part of this whole project for me was just trying to completely understand exactly what I was implementing. I had to spend time researching the documentation and other things regarding how double linked lists and list iterators worked, so that I could actually visualize what I was trying to implement in this project. Once I did that, everything started to click one-by-one.