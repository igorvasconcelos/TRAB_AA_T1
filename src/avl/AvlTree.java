package avl;

import java.util.*; // For ArrayList

public final class AVLTree<T>
{
	public class T
	{
		public int compareTo( T arg0 )
		{
			if ( this.compareTo( arg0 ) < 0 ) return -1;
			if ( this.compareTo( arg0 ) > 0 ) return 1;
			return 0;
		}
	}
	
	public static final class Elem<T>
	{
		private Elem<T>	mLeft;		// Left child
		private Elem<T>	mRight;		// Right  child
		private Elem<T>	mParent;	// Parent in the tree
		private T		mElem;		// Element being stored here
		private int		mBalance;	// Balance of the element
		private double	mPriority;	// Its priority

		/**
		 * Returns the element represented by this tree element.
		 * 
		 * @return The element represented by this tree element.
		 */
		public T getValue()
		{
			return mElem;
		}
		
		/**
		 *  Sets the element associated with this tree element.
		 *
		 *  @param value The element to associate with this tree element.
		 */
		public void setValue( T value )
		{
			mElem = value;
		}
		
		/**
		 * Returns the priority of this element.
		 * 
		 * @return The priority of this element.
		 */
		public double getPriority()
		{
			return mPriority;
		}
		
		/**
		 * Constructs a new Entry that holds the given element with the indicated priority.
		 * 
		 * @param elem The element stored in this node.
		 * @param priority The priority of this element.
		 */
		Elem( T elem, double priority )
		{
			mRight = mLeft = mParent = null;
			mElem = elem;
			mPriority = priority;
		}

	}
	/* Pointer to the root element in the tree. */
	private Elem<T> mRoot = null;
	
	/* Cached size of the tree, so we don't have to recompute this explicitly. */
	private int mSize = 0;
	
	public Elem<T> getRoot()
	{
		return mRoot;
	}

	/**
	 * Returns whether the heap is empty.
	 * 
	 * @return Whether the heap is empty.
	 */
	public boolean isEmpty()
	{
		return getRoot() == null;
	}
	
	/**
	 * Returns the number of elements in the heap.
	 * 
	 * @return The number of elements in the heap.
	 */
	public int size()
	{
		return mSize;
	}
	
	/**
	 * Recursive method to insert an element into a tree.
	 *
	 * @param p The node currently compared, usually you start with the root.
	 * @param q The node to be inserted.
	 */
	public Elem<T> insertAVL( Elem<T> p, Elem<T> q )
	{
		// If  node to compare is null, the node is inserted. If the root is null, it is the root of the tree.
		if ( p == null )
		{
			this.mRoot = q;
			++mSize;
		}
		else
		{
			// If compare node is smaller, continue with the left node
			if ( q.mElem.compareTo( p.mElem ) < 0 )
			{
				if ( p.mLeft == null )
				{
					p.mLeft = q;
					q.mParent = p;
					// Node is inserted now, continue checking the balance
					recursiveBalance( p );
					++mSize;
					return q;
				}
				else
				{
					insertAVL( p.mLeft, q );
				}
   
			}
			else if ( q.mElem.compareTo( p.mElem ) > 0 )
			{
				if ( p.mRight == null )
				{
					p.mRight = q;
					q.mParent = p;
					// Node is inserted now, continue checking the balance
					recursiveBalance( p );
					++mSize;
					return q;
				}
				else
				{
					insertAVL( p.mRight, q );
				}
			}
			else
			{
				// do nothing: This node already exists
				return null;
			}
		}
		return q;
	}
	
	/**
	 * Finds a node and calls a method to remove the node.
	 *
	 * @param p The node to start the search.
	 * @param q The node to remove.
	 */
	public void removeAVL( Elem<T> p, Elem<T> q )
	{
		if( p == null )
		{
			throw new NoSuchElementException("Tree is empty.");
		}
		else
		{
			if ( p.mElem.compareTo( q.mElem ) > 0 )
			{
				removeAVL( p.mLeft, q );
			}
			else if ( p.mElem.compareTo( q.mElem ) < 0 )
			{
				removeAVL( p.mRight, q );
			}
			else if ( p.mElem.compareTo( q.mElem ) == 0 )
			{
				--mSize;
				// we found the node in the tree.. now lets go on!
				//removeFoundNode(p);
				Elem<T> r, s;
				// at least one child of q, q will be removed directly
				if ( q.mLeft == null || q.mRight == null )
				{
					// the root is deleted
					if ( q.mParent == null )
					{
						this.mRoot = null;
						q = null;
						return;
					}
					r = q;
				}
				else
				{
					// q has two children �> will be replaced by successor
					r = successor( q );
					q.mElem = r.mElem;
				}
				if ( r.mLeft != null )
				{
					s = r.mLeft;
				}
				else
				{
					s = r.mRight;
				}
				if ( s != null )
				{
					s.mParent = r.mParent;
				}
				if ( r.mParent == null )
				{
					this.mRoot = s;
				}
				else
				{
					if ( r == r.mParent.mLeft )
					{
						r.mParent.mLeft = s;
					}
					else
					{
						r.mParent.mRight = s;
					}
					// balancing must be done until the root is reached.
					recursiveBalance( r.mParent );
				}
				r = null;
			}
		}
	}
	
	/**
	 * Check the balance for each node recursively and call required methods for balancing the tree until the root is reached.
	 *
	 * @param cur : The node to check the balance for, usually you start with the parent of a leaf.
	 */
	public void recursiveBalance( Elem<T> cur )
	{
		// we do not use the balance in this class, but the store it anyway
		setBalance( cur );
		int balance = cur.mBalance;
 
		// check the balance
		if ( balance == -2 )
		{
			if ( height( cur.mLeft.mLeft ) >= height( cur.mLeft.mRight ) )
			{
				cur = rotateRight( cur );
			}
			else
			{
				cur = doubleRotateLeftRight( cur );
			}
		}
		else if ( balance == 2 )
		{
			if ( height( cur.mRight.mRight ) >= height( cur.mRight.mLeft ) )
			{
				cur = rotateLeft( cur );
			}
			else
			{
				cur = doubleRotateRightLeft( cur );
			}
		}
		// we did not reach the root yet
		if ( cur.mParent != null )
		{
			recursiveBalance( cur.mParent );
		}
		else
		{
			this.mRoot = cur;
			System.out.println( "���� Balancing finished �����-" );
		}
	}
	
	/**
	 * Left rotation using the given node.
	 *
	 * @param n The node for the rotation.
	 * @return The root of the rotated tree.
	 */
	public Elem<T> rotateLeft( Elem<T> n )
	{
		Elem<T> v = n.mRight;
		v.mParent = n.mParent;
		n.mRight = v.mLeft;
		if ( n.mRight != null )
		{
			n.mRight.mParent = n;
		}
		v.mLeft = n;
		n.mParent = v;
 
		if ( v.mParent != null )
		{
			if ( v.mParent.mRight == n )
			{
				v.mParent.mRight = v;
			}
			else if ( v.mParent.mLeft == n )
			{
				v.mParent.mLeft = v;
			}
		}
		setBalance( n );
		setBalance( v );
		return v;
	}

	/**
	 * Right rotation using the given node.
	 *
	 * @param n The node for the rotation
	 * @return The root of the new rotated tree.
	 */
	public Elem<T> rotateRight( Elem<T> n )
	{
		Elem<T> v = n.mLeft;
		v.mParent = n.mParent;
		n.mLeft = v.mRight;
		if ( n.mLeft != null )
		{
			n.mLeft.mParent = n;
		}
		v.mRight = n;
		n.mParent = v;
		if ( v.mParent != null )
		{
			if ( v.mParent.mRight == n )
			{
				v.mParent.mRight = v;
			}
			else if ( v.mParent.mLeft == n )
			{
				v.mParent.mLeft = v;
			}
		}
		setBalance( n );
		setBalance( v );
		return v;
	}

	/**
	 * @param u The node for the rotation.
	 * @return The root after the double rotation.
	 */
	public Elem<T> doubleRotateLeftRight( Elem<T> u )
	{
		u.mLeft = rotateLeft( u.mLeft );
		return rotateRight( u );
	}

	/**
	 * @param u The node for the rotation.
	 * @return The root after the double rotation.
	 */
	public Elem<T> doubleRotateRightLeft( Elem<T> u )
	{
		u.mRight = rotateRight( u.mRight );
		return rotateLeft( u );
	}
	
	/**
	 * Returns the successor of a given node in the tree (search recursivly).
	 *
	 * @param q The predecessor.
	 * @return The successor of node q.
	 */
	public Elem<T> successor( Elem<T> q )
	{
		if ( q.mRight != null )
		{
			Elem<T> r = q.mRight;
			while( r.mLeft != null )
			{
				r = r.mLeft;
			}
			return r;
		}
		else
		{
			Elem<T> p = q.mParent;
			while( p != null && q == p.mRight )
			{
				q = p;
				p = q.mParent;
			}
			return p;
		}
	}
 
	/**
	 * Calculating the "height" of a node.
	 *
	 * @param cur
	 * @return The height of a node (-1, if node is not existent eg. NULL).
	 */
	private int height( Elem<T> cur )
	{
		if ( cur == null )
		{
			return -1;
		}
		if ( cur.mLeft == null && cur.mRight == null )
		{
			return 0;
		}
		else if ( cur.mLeft == null )
		{
			return 1 + height( cur.mRight );
		}
		else if ( cur.mRight == null )
		{
			return 1 + height( cur.mLeft );
		}
		else
		{
			return 1 + maximum( height( cur.mLeft ), height( cur.mRight ) );
		}
	}
 
	/**
	 * Return the maximum of two integers.
	 */
	private int maximum( int a, int b )
	{
		if ( a >= b )
		{
			return a;
		}
		else
		{
			return b;
		}
	}
	
	/**
	 * Calculating the "balance" of a node.
	 *
	 * @param cur
	 */
	private void setBalance( Elem<T> cur )
	{
		cur.mBalance = height( cur.mRight ) - height( cur.mLeft );
	}
}
