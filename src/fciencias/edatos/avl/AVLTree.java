package fciencias.edatos.avl;

import java.util.Scanner;
import javax.swing.plaf.synth.SynthPasswordFieldUI;
import javax.swing.plaf.synth.SynthToggleButtonUI;

/**
* Implementación de árbol AVL
* @author Itzel Morales García
* @author Josue Eduardo Morales Torres
* @version 3.0 Noviembre 2021 (Anterior 2.0 Julio 2021).
* @since Estructuras de Datos 2022-1.
*/
public class AVLTree<K extends Comparable, T> implements TDABinarySearchTree<K, T>{

	/**
	 * Nodo de un arbol AVL.
	 */
	public class AVLNode{

		/** Altura del nodo. */
		public int altura;

		/**Factor de equilibrio del nodo. */
		public int fe;

		/** Hijo izquierdo. */
		public AVLNode izquierdo;

		/** Hijo derecho. */
		public AVLNode derecho;

		/** Padre del nodo. */
		public AVLNode padre;

		/** Elemento almacenado en el nodo. */
		public T elemento;

		/** Clave del nodo. */
		public K clave;

        /** Raíz del árbol */
		private AVLTree<K, T>.AVLNode raiz;

		/**
		 * Crea un nuevo nodo AVL
		 * @param element el elemento a almacenar.
		 * @param key la clave del nodo.
		 * @param padre el padre del nodo
		 */
		public AVLNode(T element, K key, AVLNode padre){
			elemento = element;
			clave = key;
			this.padre = padre;
			altura = this.getAltura();
			raiz = this.raiz;
		}

		/**
		 * Calcula la altura del nodo.
		 */
		public int getAltura(){
			// Si este nodo es hoja
			if(izquierdo == null && derecho==null){
				return 0;
			} else if(izquierdo != null && derecho != null){ // Dos hijos
				int max = izquierdo.getAltura() > derecho.getAltura() ? izquierdo.getAltura() : derecho.getAltura();
				return 1 + max;
			} else{ // Tiene solo un hijo
				boolean tieneIzquierdo = izquierdo!=null;
				return 1 + (tieneIzquierdo ? izquierdo.getAltura() : derecho.getAltura());
			}
		}

		/**
		 * Actualiza la altura del nodo.
		 */
		public void actualizaAltura(){
			this.altura = this.getAltura();
		}
	}

		/**
		 * altura del nodo
		 */
		private int altura(AVLNode a){
			return a == null ? -1 : a.altura;
		}

	private AVLNode raiz;

	@Override
	public T retrieve(K k){
		return null;
	}

	/**
	 * Obtenia el nodo con una clave específica.
	 * @param k la clave a buscar
	 * @param actual el nodo actual
	 * @return el nodo con clave k o null si no existe.
	 */
	private AVLNode retrieve(K k, AVLNode actual){
		// Verificamos que actual es null
		if(actual == null)
			return null;

		int compare = k.compareTo(actual.clave);

		// Si existe el elemento
		if(compare == 0){
			return actual;
		}

		if(compare < 0){ // Caso del hijo izquiero
			return retrieve(k, actual.izquierdo);
		} else { // Caso del hijo derecho
			return retrieve(k, actual.derecho);
		}
	}

	/**Obetener el Factor de equilibrio
	 * @param AVLNode el nodo del cuál se va a obtener el factor de equilibrio
	 */
	public int obtenerFE(AVLNode eq){
		if (eq == null){
			return -1;
		}else{
			return eq.fe;
		}
	}

	/** Rotación simple a la izquierda
	 * @param AVLNode, el nodo a partir del cual se va a hacer la rotación
	 */
		public AVLNode rotacionIzquierda(AVLNode c){
			AVLNode aux = c.izquierdo;
			c.izquierdo = aux.derecho;
			aux.derecho = c;
			c.fe = Math.max(obtenerFE(c.izquierdo), obtenerFE(c.derecho))+1;
			aux.fe = Math.max(obtenerFE(aux.izquierdo), obtenerFE(aux.derecho))+1;
			return aux;
		}

		/** Rotación simple a la derecha 
		 * @param AVLNode nodo a partir dle cual se va a hacer la rotación
		*/
		public AVLNode rotacionDerecha(AVLNode c){
			AVLNode aux = c.derecho;
			c.derecho = aux.izquierdo;
			c.fe = Math.max(obtenerFE(c.izquierdo), obtenerFE(c.derecho)+1);
			aux.fe = Math.max(obtenerFE(aux.izquierdo), obtenerFE(aux.derecho))+1;
			return aux;
		}

		/**Rotación doble a la izquierda
		 * @param AVLNode nodo a partir dle cual se va a hacer la rotación doble
		 */
		public AVLNode rotacionDobleIzq(AVLNode c){
			AVLNode temp;
			c.izquierdo = rotacionDerecha(c.izquierdo);
			temp = rotacionIzquierda(c);
			return temp;
		}

		/** Rotación doble a la izquierda
		 * @param AVLNode nodo a partir dle cual se va a hacer la rotación doble
		 */
		public AVLNode rotacionDobleDer(AVLNode c){
			AVLNode temp;
			c.derecho = rotacionIzquierda(c.derecho);
			temp = rotacionDerecha(c);
			return temp;
		}

	@Override
	public void insert(T e, K k){
		if(raiz == null){ // Arbol vacío
			raiz = new AVLNode(e, k, null);
			return;
		}

		AVLNode v = insert(e, k, raiz);
		v.actualizaAltura();
		if(k.compareTo(raiz.clave)<0){
			if(altura(v.izquierdo)- altura(v.derecho) == 2)
				if(k.compareTo(v.izquierdo.clave)<0){
					v = rotacionIzquierda(v);
				}else{
					v = rotacionDobleIzq(v);
					
				}
		}else{
		if(altura(v.izquierdo)- altura(v.derecho) == 2)
			if(k.compareTo(v.derecho.clave)>0){
				v = rotacionDerecha(v);
				}else{
					v = rotacionDobleDer(v);
				}
		}
		v.altura = max(altura(v.izquierdo), altura(v.derecho)) +1;
	}

	/**
	 * Inserta un nodo de forma recursiva.
	 * @param e el elemento a insertar
	 * @param k es la clave del nodo a insertar
	 * @param actual el nodo actual
	 * @return 
	 */
	public AVLNode insert(T e, K k, AVLNode actual){
		if(k.compareTo(actual.clave)<0){ // Verificamos sobre el izquierdo
			if(actual.izquierdo == null){ // Insertamos en esa posición
				actual.izquierdo = new AVLNode(e, k, actual);
				return actual.izquierdo;
			} else  if(actual.izquierdo!=null){ // Recursión sobre el izquierdo
				return insert(e, k, actual.izquierdo);
			}
			/*if(altura(actual.izquierdo)- altura(actual.derecho) == 2)
				if(k.compareTo(actual.izquierdo.clave)<0){
					actual = rotacionIzquierda(actual);
				}else{
					actual = rotacionDobleIzq(actual);
					
				}*/
		
		}else{ // Verificamos sobre la derecha
			if(actual.derecho == null){ // Insertamos en esa posición
				actual.derecho = new AVLNode(e, k, actual);
				return actual.derecho;
			} else if( actual.derecho !=null) { // Recursión sobre el derecho
				return insert(e, k, actual.derecho);
			}
			/*if(altura(actual.izquierdo)- altura(actual.derecho) == 2)
				if(k.compareTo(actual.izquierdo.clave)<0){
					actual = rotacionDerecha(actual);
				}else{
					actual = rotacionDobleDer(actual);
				}*/
		
		}
	//	actual.altura = max(altura(actual.izquierdo), altura(actual.derecho)) +1;
		return actual;
	}

	private static int max(int lhs, int rhs){
		return lhs > rhs ? lhs : rhs;
	}

	@Override
	public T delete(K k){
		AVLNode v = retrieve(k, raiz);

		// El elemento que queremos eliminar no está en el árbol
		if(v == null){
			return null;
		}

		T eliminado = v.elemento;

		// Eliminar con auxiliar
		AVLNode w = delete(v);

		return eliminado;
	}

	private AVLNode delete(AVLNode v){
		if(v.izquierdo!=null && v.derecho!=null){ // Tiene dos hijos
			AVLNode menor = findMin(v.derecho);
			swap(menor, v);
			return delete(menor);
		} else if(v.izquierdo==null && v.derecho==null){ // No tiene hijos
			boolean esIzquierdo = v.padre.izquierdo==v;
			if(esIzquierdo){
				v.padre.izquierdo = null;
			} else{
				v.padre.derecho = null;
			}
			return v.padre;
		} else{ // Sólo tiene un hijo
			boolean hijoIzquierdo = v.izquierdo!=null;
			if(hijoIzquierdo){
				swap(v, v.izquierdo);
				return delete(v.izquierdo);
			} else{
				swap(v, v.derecho);
				return delete(v.derecho);
			}
		}
	}

	@Override
	public T findMin(){
		return null;
	}

	private void swap(AVLNode v, AVLNode w){
		T value = v.elemento;
		K clave = v.clave;
		v.elemento = w.elemento;
		v.clave = w.clave;
		w.elemento = value;
		w.clave = clave;
	}

	private AVLNode findMin(AVLNode actual){
		if(actual == null)
			return null;
		AVLNode iterador = actual;

		while(iterador.izquierdo != null){
			iterador = actual.izquierdo;
		}

		return iterador;

	}

	@Override
	public T findMax(){
		return null;
	}

	private AVLNode findMax(AVLNode actual){
		if(actual == null)
			return null;
		AVLNode iterador = actual;

		while(iterador.derecho != null){
			iterador = actual.derecho;
		}

		return iterador;
	}

	@Override
	public void preorden(){
		preorden(raiz);
	}

	private void preorden(AVLNode actual){
		if(actual==null)
			return;

		System.out.println(actual.elemento);
		preorden(actual.izquierdo);
		preorden(actual.derecho);
	}

	@Override
	public void inorden(){
		inorden(raiz);
	}

	private void inorden(AVLNode actual){
		if(actual==null)
		return;

		inorden(actual.izquierdo);
		System.out.println(actual.elemento);
		inorden(actual.derecho);
	}

	@Override
	public void postorden(){
		postorden(raiz);
	}

	private void postorden(AVLNode actual){
		if(actual== null)
		return;

		postorden(actual.izquierdo);
		postorden(actual.derecho);
		System.out.println(actual.elemento);
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		AVLTree<Integer, String> arbol = new AVLTree<>();
		do{
			System.out.println("[1]Obtener la altura\n"+
			"[1] Inserta un nodo\n"+
			"[2] Elimina\n"+
			"[3] Recupera objeto\n"+
			"[4] FindMin\n"+
			"[5] FindMax\n"+
			"[6] Preorden\n"+
			"[7] Inorden\n"+
			"[8] Postorden\n"+
			"[9] Salir\n"+
			"Elige una opción: ");

			int opcion = sc.nextInt();
			switch(opcion){
				/**Inserta un elemento */
				case 1:
					System.out.println("Digite la clave a agregar");
					int clave = sc.nextInt();
					sc.nextLine();
					
					System.out.println("Digite el elemento a agregar");
					String e = sc.nextLine();
					arbol.insert(e, clave);
					break;
				/**Elimina un elemento */
				case 2:
					System.out.println("Digite la clave del elemento a eliminar");
					int aeliminar = sc.nextInt();
					sc.nextLine();
					System.out.println("Se elimino el elemento : "+arbol.delete(aeliminar));
					break;
				/** Recupera elemento */
				case 3:
					System.out.println("Digite la clave del elemento a buscar");
					int dat = sc.nextInt();
					sc.nextLine();
					System.out.println(arbol.retrieve(dat));
					break;
				/** FindMin */
				case 4:
					System.out.println(arbol.findMin());
					break;
				/** FindMax */
				case 5:
					System.out.println(arbol.findMax());
					break;
				/** Preorden */
				case 6:
					System.out.println("\n Recorrido en Preorden");
					arbol.preorden();
					System.out.println("\n");
					break;
				/** Inorden */
				case 7:
				System.out.println("\n Recorrido en Inorden");
					arbol.inorden();
					System.out.println("\n");
					break;
				/** Postorden */
				case 8:
				System.out.println("\n Recorrido en Postorden");
					arbol.postorden();
					System.out.println("\n");
					break;
				case 9:
					return;
				default:
					System.out.println("Opción inválida");
					
			}
		}while(true);

	}
}