package RBTree;

public class RedBlackBST<T>
{
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	private Node<T> root;
	public static class Node<T> 
	{
		T key; // key
		double val; // associated data
		Node<T> left, right, parent; // subtrees
		int N; // # nodes in this subtree
		boolean color; // color of link from
		// parent to this node
		public Node( T key, double val, int N, boolean color )
		{
			this.key = key;
			this.val = val;
			this.N = N;
			this.color = color;
			this.parent = null;
		}
		
		public T getKey() {
			return key;
		}
		public double getVal() {
			return val;
		}
	}

	/**
	 * @return the root
	 */
	public Node<T> getRoot() {
		return root;
	}

	public int size()
	{
		return size(root);
	}
	
	private int size( Node<T> h )
	{
		if ( h == null ) return 0;
		else return h.N;
	}
	
	private boolean isRed( Node<T> h )
	{
		if (h == null) return false;
		return h.color == RED;
	}
	
	private Node<T> rotateLeft( Node<T> h )
	{
		Node<T> x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		h.color = RED;
		x.N = h.N;
		h.N = 1 + size( h.left ) + size( h.right );
		return x;
	}
	
	private Node<T> rotateRight( Node<T> h )
	{
		Node<T> x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		x.N = h.N;
		h.N = 1 + size( h.left ) + size( h.right );
		return x;
	}
	
	private void flipColors( Node<T> h )
	{
		h.color = RED;
		h.left.color = BLACK;
		h.right.color = BLACK;
	}
	
	public T get( double val )
	{
		return get( root, val );
	}
	
	private T get( Node<T> h, double val )
	{
		// Return value associated with key in the subtree rooted at h;
		// return null if key not present in subtree rooted at h.
		if ( h == null ) return null;
		int cmp = val < h.val? -1 : val > h.val? 1: 0;
		if ( cmp < 0 ) return get( h.left, val );
		else if ( cmp > 0 ) return get( h.right, val );
		else
			return h.key;
	}
	
	public Node<T> put( T key, double val )
	{ // Search for key. Update value if found; grow table if new.
		root = put( root, key, val );
		root.color = BLACK;
		return root;
	}
	
	private Node<T> put( Node<T> h, T key, double val )
	{
		if ( h == null ) // Do standard insert, with red link to parent.
			return new Node<T>( key, val, 1, RED );
		int cmp = val < h.val? -1 : val > h.val? 1: 0;
		if ( cmp < 0 ) h.left = put( h.left, key, val );
		else if ( cmp > 0 ) h.right = put( h.right, key, val );
		else h.val = val;
		if ( isRed( h.right ) && !isRed( h.left ) ) h = rotateLeft( h );
		if ( isRed( h.left ) && isRed( h.left.left ) ) h = rotateRight( h );
		if ( isRed( h.left ) && isRed( h.right ) ) flipColors( h );
		h.N = size( h.left ) + size( h.right ) + 1;
		return h;
	}
	
	private Node<T> moveRedLeft( Node<T> h )
	{
		// Assuming that h is red and both h.left and h.left.left
		// are black, make h.left or one of its children red.
		flipColors( h );
		if ( isRed( h.right.left ) )
		{
			h.right = rotateRight( h.right );
			h = rotateLeft(h);
		}
		return h;
	}
	
	private Node<T> moveRedRight( Node<T> h )
	{
		// Assuming that h is red and both h.right and h.right.left
		// are black, make h.right or one of its children red.
		flipColors( h );
		if ( !isRed( h.left.left ) ) h = rotateRight(h);
		return h;
	}
	
	public Node<T> deleteMin()
	{ 
		Node<T> del = null;
		if (!isRed( root.left ) && !isRed( root.right ) ) {
			root.color = RED;
		}
		//System.out.println( "Entrando no deleteMin na raiz" );
		del = deleteMin( root );
		if ( !isEmpty() ) 
		{
			root.color = BLACK;
		}
		return del;
	}
	
	private Node<T> deleteMin( Node<T> h )
	{
		
		if ( h.left == null ) 
		{
			return null;
		}
		if ( !isRed( h.left ) && !isRed( h.left.left ) )
		{
			//System.out.println( "Entrando no moveRedLeft no nó " + h.key );
			h = moveRedLeft( h );
			
		}
		//System.out.println( "Entrando no deleteMin no nó " + h.left.key );
		h.left = deleteMin( h.left );
		
		return balance( h );
	}
	
	public boolean isEmpty()
	{
		return this.root == null;
	}
	
	private Node<T> balance( Node<T> h )
	{
		if ( isRed( h.right ) ) h = rotateLeft( h );
		if ( isRed( h.right ) && !isRed( h.left ) ) h = rotateLeft( h );
		if ( isRed( h.left ) && isRed( h.left.left ) ) h = rotateRight( h );
		if ( isRed( h.left ) && isRed( h.right ) ) flipColors( h );
		h.N = size( h.left ) + size( h.right ) + 1;
		return h;
	}
/*	
	public void deleteMax()
	{
		if ( !isRed( root.left ) && !isRed( root.right ) )
			root.color = RED;
		root = deleteMax( root );
		if ( !isEmpty() ) root.color = BLACK;
	}
	
	private Node deleteMax(Node h)
	{
		if ( isRed( h.left ) ) h = rotateRight( h );
		if ( h.right == null ) return null;
		if ( !isRed( h.right ) && !isRed( h.right.left ) )
			h = moveRedRight( h );
		h.right = deleteMax( h.right );
		return balance( h );
	}
*/	
	public Node<T> delete( Node<T> h )
	{
		if ( !isRed( root.left ) && !isRed( root.right ) )
			root.color = RED;
		root = delete( root, h );
		if ( !isEmpty() ) root.color = BLACK;
		return root;
	}
	
	private Node<T> delete( Node<T> h, Node<T> x )
	{
		if ( x.val < h.val )
		{
			if ( !isRed( h.left ) && !isRed( h.left.left ) )
				h = moveRedLeft( h );
			h.left = delete( h.left, x );
		}
		else
		{
			if ( isRed( h.left ) ) h = rotateRight( h );
			if ( x.val == h.val && ( h.right == null ) )
				return null;
			if ( !isRed( h.right ) && !isRed( h.right.left ) )
				h = moveRedRight( h );
			if ( x.val == h.val )
			{
				h.val = min( h.right ).val;
				h.key = min( h.right ).key;
				h.right = deleteMin( h.right );
			}
			else h.right = delete( h.right, x );
		}
		return balance( h );
	}

	public Node<T> min()
	{
		return min( root );
	}
	
	private Node<T> min( Node<T> h )
	{
		if ( h.left == null ) return h;
		return min( h.left );
	}
/*	
	public T floor( T key )
	{
		Node x = floor( root, key );
		if ( x == null ) return null;
		return x.key;
	}
	
	private Node floor(Node h, T key)
	{
		if ( h == null ) return null;
		int cmp = key.compareTo( h.key );
		if ( cmp == 0) return h;
		if ( cmp < 0) return floor( h.left, key );
		Node t = floor( h.right, key );
		if ( t != null) return t;
		else return h;
	}
	
	public T select( int k )
	{
		return select( root, k ).key;
	}
	
	private Node select( Node x, int k )
	{
		// Return Node containing key of rank k.
		if ( x == null ) return null;
		int t = size( x.left );
		if ( t > k ) return select( x.left, k );
		else if ( t < k ) return select( x.right, k - t - 1 );
		else return x;
	}
	
	public int rank(T key)
	{
		return rank( key, root );
	}
	
	private int rank( T key, Node x )
	{
		// Return number of keys less than x.key in the subtree rooted at x.
		if ( x == null ) return 0;
		int cmp = key.compareTo( x.key );
		if ( cmp < 0) return rank( key, x.left );
		else if ( cmp > 0 ) return 1 + size( x.left ) + rank( key, x.right );
		else return size( x.left );
	}
*/	
	public void print( Node<T> x )
	{
		if ( x == null )
		{
			//System.out.println( "Não tem filho" );
			return;
		}
		//System.out.println( "Entrando na subarvore a esquerda" );
		print( x.left );
		System.out.println( "Chave:" + x.key + " - Val: " + x.val );
		//System.out.println( "Entrando na subarvore a direita" );
		print( x.right );
	}
}



