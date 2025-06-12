package trabajofinal.app.base.domain.controller.datastruct.list;


public class Node <E> {
    //private Object data; -- dato generico, usado en java 8
    private E data;
    private Node<E> next; // siguiente nodo

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }
    
    public Node(E data) {
        this.data = data;
        this.next = null;
    }

    public Node() {
        this.data = null;
        this.next = null;
    }

}

