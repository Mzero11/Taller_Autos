package trabajofinal.app.base.domain.controller.datastruct.list;


public class LinkedList<E> {
    private Node<E> head;
    private Node<E> last;
    private Integer size;// para que no ocupe memoria al ser llamada en otra clase

    

    public LinkedList(){
        head = null;
        last = null;
        size = 0;
    }

    public Boolean isEmpty(){
        return head == null || size == 0;
    }

    private Node<E> getNode(Integer pos) {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("tu coso esta vacio");
        }
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException("Fuera de rango");
        }else if (pos == 0) {
            return head;
        }else if ((size.intValue() - 1) == pos.intValue()){
            return last;
        }else{
            Node<E> preview = head;
            Integer cont = 0;
            while (cont < pos) {
                preview = preview.getNext();
                cont++;
            }
            return preview;
        }
        
    }

    private E getDataFist() {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("tu coso esta vacio");
        }else{
            return head.getData();
        }
    }

    private E getDataLast() {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("tu coso esta vacio");
        }else{
            return last.getData();
        }
    }
    
    public E get(Integer pos){
        return getNode(pos).getData();
        /*if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("La lista está vacía");
        }
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException("Fuera de rango");
        }else if (pos == 0) {
            return getDataFist();
        }else if (size.intValue() == pos.intValue()){
            return getDataLast();
        }else{
            return getNode(pos).getData();
        }*/
    }

    private void addFirst(E data) {
        if(isEmpty()){
            Node<E> aux = new Node<>(data);
            head = aux;
            last = aux;
        } else {
            Node<E> head_old = head;
            Node<E> aux = new Node<>(data, head_old);
            head = aux;
        }
        size++;
    }

    private void addLast(E data) {
        if(isEmpty()){
            addFirst(data);
        } else{
            Node<E> aux = new Node<>(data);
            last.setNext(aux);
            last = aux;
            size++;
        }
    }
    
    public void add(E data, Integer pos) throws Exception {
        if (pos < 0 || pos >= size){
            throw new ArrayIndexOutOfBoundsException("Fuera de rango");
        } else if (pos == 0){
            addFirst(data);
        } else if(size.intValue() == pos.intValue()){
            addLast(data);
        }else {
            Node<E> serach_preview = getNode(pos-1);
            Node<E> preview = getNode(pos);
            Node<E> aux = new Node<>(data, preview);
            serach_preview.setNext(aux);
            size++;
        }
    }

    public void add(E data) throws Exception {
        addLast(data);    
    }

    
    public String print(){
        if (isEmpty()) {
            return "Ta vacia tu coso oe"; 
        }else{
            StringBuilder txt = new StringBuilder();
            Node<E> help = head;
            while (help != null) {
                txt.append(help.getData()).append(" - ");
                help = help.getNext();
            }
            txt.append("\n");
            return txt.toString();
        }
    } 

    public void clear() {
        head = null;
        last = null;
        size = 0;
    }

    public void update(E data, Integer pos) throws Exception {
        getNode(pos).setData(data);
    }

    /* 
    public E deleteFirst() throws Exception {
        if (isEmpty()) {
            throw new Exception("Lista vacia");
        } else {
            E element = head.getData();
            head = head.getNext(); 
            if (size == 1) { 
                last = null; 
            }
            size--; 
                return element; 
        }
    }
    
    public E deleteLast() throws Exception {
        if (isEmpty()) {
            throw new Exception("Lista vacia");
        } else {
            E element = last.getData();
            if (size == 1) { 
                head = null;
                last = null;
            } else {
                Node<E> aux = getNode(size - 2); 
                aux.setNext(null); 
                last = aux; 
            }
            size--;
            return element; 
        }
    }

    public E delete(Integer pos) throws Exception{
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("La lista está vacía");
        }else if (pos < 0 || pos >= size){
            throw new ArrayIndexOutOfBoundsException("Fuera de rango");
        } else if (pos == 0){
            return deleteFirst();
        } else if (pos == size-1){
            return deleteLast(); 
        } else{
            Node<E> previo = getNode(pos-1);
            Node<E> current = getNode(pos);
            E element = current.getData();
            previo.setNext(current.getNext());
            current = null; // Eliminar el nodo actual
            size--;
            return element;
        }
    }*/

    public E[] toArray(){
        Class clazz = null;
        E[] matriz = null;
        if(this.size > 0){
            clazz = head.getData().getClass();
            matriz = (E[]) java.lang.reflect.Array.newInstance(clazz, this.size);
            Node<E> aux = head;
            for (int i = 0; i < this.size; i++) {
                matriz[i] = aux.getData();
                aux = aux.getNext();
            }
        }
        return matriz;
    }

    public LinkedList<E> toList(E[] matriz) throws Exception{
        clear();
        for (int i = 0; i < matriz.length; i++) {
            this.add(matriz[i]);
        }
        return this;
    }

    protected E deleteFirst() throws Exception {
        if (isEmpty()) {
            throw new Exception("Lista vacia");
        } else {
            E element = head.getData();
            Node<E> aux = head.getNext();
            head = aux;
            if (size.intValue() == 1) 
                last = null;
            size--;
            return element; 
        }
    }

    protected E deleteLast() throws Exception {
        if (isEmpty()) {
            throw new Exception("Lista vacia");
        } else { 
            E element = last.getData();
            Node<E> aux = getNode(size - 2);
        
            if (aux == null) {
                last = null;
                if (size == 2) {
                    last = head;                    
                } else {
                    head = null;
                }
            } else {
                last = null;
                last = aux;
                last.setNext(null);
            }

            size--;
            return element; 
        }
    }

    public E delete(Integer pos, E data) throws Exception{
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("tu coso esta vacio");
        }
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException("Fuera de rango");
        }else if (pos == 0) {
            return deleteFirst();
        }else if ((size.intValue() - 1) == pos.intValue()){
            return deleteLast();
        }else{
            Node<E> preview = getNode(pos -1);
            Node<E> actualy = getNode(pos);
            E element = preview.getData();
            Node<E> next = actualy.getNext();
            actualy = null;
            preview.setNext(next);
            size--;
            return element;
        }
        
    }
   
    public Integer getSize() {
        return this.size;
    }
    
    public Node<E> getHead() {
        return head;
    }
}