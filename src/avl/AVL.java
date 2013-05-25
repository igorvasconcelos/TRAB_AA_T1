package avl;

import java.util.ArrayList;

/* ================================================================
This is BST with balancing "height" (= AVL)

Each node has a height to determine if the BST is balanced
================================================================ */

public class AVL
{
	public static final class Elem
	{
		private Elem	mLeft;		// Left child
		private Elem	mRight;		// Right  child
		private Elem	mParent;	// Parent in the tree
		private int		mElem;		// Element being stored here
		private int		mHeight;	// Balance of the element
		private double	mPriority;	// Its priority

		/**
		 * Returns the element represented by this tree element.
		 * 
		 * @return The element represented by this tree element.
		 */
		public int getValue()
		{
			return mElem;
		}
		
		/**
		 *  Sets the element associated with this tree element.
		 *
		 *  @param value The element to associate with this tree element.
		 */
		public void setValue( int value )
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
		public Elem( int elem, double priority )
		{
			mRight = mLeft = mParent = null;
			mElem = elem;
			mPriority = priority;
		}

	}
	
	public Elem root;	// References the root node of the BST

	public AVL()
	{
		root = null;
	}

/* ================================================================
   findEntry(k): find entry with key k 

   Return:  reference to (k,v) IF k is in BST
            reference to parent(k,v) IF k is NOT in BST (for put)
   ================================================================ */
public Elem findEntry( Elem k )
{
	Elem curr_node;   // Help variable
	Elem prev_node;   // Help variable

    /* --------------------------------------------
	  Find the node with key == "k" in the BST
       -------------------------------------------- */
    curr_node = root;  // Always start at the root node
    prev_node = root;  // Remember the previous node for insertion

    while ( curr_node != null )
    {
       if ( Integer.compare( k.mElem, curr_node.mElem ) < 0 )
	  {
	     prev_node = curr_node;       // Remember prev. node
	     curr_node = curr_node.mLeft;  // Continue search in left subtree
	  }
       else if ( Integer.compare( k.mElem, curr_node.mElem ) > 0 )
	  {
	     prev_node = curr_node;       // Remember prev. node
	     curr_node = curr_node.mRight; // Continue search in right subtree
	  }
       else
	  {
	     // Found key in BST 
	     return curr_node;
	  }
    }

    /* ======================================
	  When we reach here, k is NOT in BST
       ====================================== */
    return prev_node;		// Return the previous (parent) node
}

/* ================================================================
   get(k): find key k and return assoc. value
   ================================================================ */
public Elem get( Elem k )
{
	Elem p;   // Help variable

    /* --------------------------------------------
	  Find the node with key == "key" in the BST
       -------------------------------------------- */
    p = findEntry( k );

    if ( Integer.compare( k.mElem, p.mElem ) == 0 )
	  return p;
    else
	  return null;
}

/* ================================================================
   put(k, v): store the (k,v) pair into the BST

      1. if the key "k" is found in the BST, we do nothing
      1. if the key "k" is NOT found in the BST, we insert
	    a new node containing (k, v)
   ================================================================ */
public void put( int k, double v )
{
	Elem p, q;   // Help variable

    /* ----------------------------------------------------------
	  Just like linked list, insert in an EMPTY BST
	  must be taken care off separately by an if-statement
       ---------------------------------------------------------- */
    if ( root == null )
    {  // Insert into an empty BST

      root = new Elem( k, v );
	  root.mHeight = 1;
	  return;
    }

    /* --------------------------------------------
	  Find the node with key == "key" in the BST
       -------------------------------------------- */
    q = new Elem( k, v );
    p = findEntry( q );

    if ( Integer.compare( q.mElem, p.mElem ) == 0 )
    {
      p.mPriority = v;			// Update value
	  return;
    }

    /* --------------------------------------------
	  Insert a new entry (k,v) under p !!!
       -------------------------------------------- */
    q = new Elem( k, v );
    q.mHeight = 1;
    q.mParent = p;

    if 	( Integer.compare( q.mElem, p.mElem ) < 0 )
	  p.mLeft = q;            	// Add q as left child
    else 
	  p.mRight = q;           	// Add q as right child

    /* --------------------------------------------
       Recompute the height of all parent nodes...
       -------------------------------------------- */
    recompHeight(p);

    /* --------------------------------------------
       Check for height violation
       -------------------------------------------- */
    Elem x, y, z;
    x = y = z = q;      // Start search at q (new node)

    while ( x != null )
    {
       if ( diffHeight(x.mLeft, x.mRight) <= 1 )
       {
          z = y;
          y = x;
	     x = x.mParent;
       }
	  else
	     break;
    }


    if ( x != null )
    {
       /* --------------------------------------------
          Print tree after insertiom
          -------------------------------------------- */
/*
       System.out.println("********************************************");
       System.out.println("Unbalanced AVL tree after insertion !!!");
       System.out.println("********************************************");
       System.out.println("Tree before rebalance:\n");
	  printBST();
       System.out.println("-------------------------------------------");
*/

       tri_node_restructure( x, y, z );
/*
       System.out.println("Tree after rebalance:\n");
	  printBST();
       System.out.println("********************************************");
*/
    }
}

/* =======================================================
   tri_node_restructure(x, y, z):

          x = parent(y)
          y = parent(z)
   ======================================================= */
public Elem tri_node_restructure( Elem x, Elem y, Elem z)
{
  /* *******************************************************************
     Determine the parent child relationships between (y,z) and (x,y))
     ******************************************************************* */
  boolean zIsLeftChild = (z == y.mLeft);
  boolean yIsLeftChild = (y == x.mLeft);

  /* =======================================================
	Determine the configuration:

	   find out which nodes are in positions a, b and c
	   given in the following legend:

                             b
                           /   \
                          a     c
	======================================================= */
  Elem a, b, c;
  Elem T0, T1, T2, T3;

  if (zIsLeftChild && yIsLeftChild) 
  { /* Configuration 1 */
//  System.out.println("Use tri-node restructuring op #1");

    a = z;                     //          x=c
    b = y;                     //         /  \
    c = x;                     //       y=b  T3 
    T0 = z.mLeft;               //      /  \ 
    T1 = z.mRight;              //    z=a  T2
    T2 = y.mRight;              //   /  \
    T3 = x.mRight;              //  T0  T1
  }
  else if (!zIsLeftChild && yIsLeftChild) 
  { /* Configuration 2 */
//  System.out.println("Use tri-node restructuring op #2");

    a = y;                     //       x=c
    b = z;                     //      /  \
    c = x;                     //    y=a  T3
    T0 = y.mLeft;               //   /  \
    T1 = z.mLeft;               //  T0 z=b
    T2 = z.mRight;              //     /  \ 
    T3 = x.mRight;              //    T1  T2
  }
  else if (zIsLeftChild && !yIsLeftChild) 
  { /* Configuration 4 */
//  System.out.println("Use tri-node restructuring op #4");

    a = x;                     //      x=a
    b = z;                     //     /  \
    c = y;                     //    T0  y=c
    T0 = x.mLeft;               //       /  \ 
    T1 = z.mLeft;               //      z=b  T3
    T2 = z.mRight;              //     /  \  
    T3 = y.mRight;              //    T1  T2 
  }
  else 
  { /* Configuration 3 */
//  System.out.println("Use tri-node restructuring op #3");

    a = x;                     //       x=a
    b = y;                     //      /  \
    c = z;                     //     T0  y=b
    T0 = x.mLeft;               //        /  \
    T1 = y.mLeft;               //        T1 z=c
    T2 = z.mLeft;               //          /  \
    T3 = z.mRight;              //         T2  T3
  }
   
  /* ------------------------------------------------------------------
     Put b at x's place (make b the root of the new subtree !)
     ------------------------------------------------------------------ */
  if ( x == root )
  {  /* If x is the root node, handle the replacement  differently.... */

     root = b;                   // b is now root
     b.mParent = null;
  }
  else 
  {
	  Elem xParent;

     xParent = x.mParent;   // Find x's parent

     if ( x == xParent.mLeft ) 
     { /* Link b to the left branch of x's parent */
       b.mParent = xParent;
       xParent.mLeft = b;
     }
     else 
     { /* Link b to the right branch of x's parent */
       b.mParent = xParent;
       xParent.mRight = b;
     }
  }

  /* ------------------
     Make:   b
            / \
           a   c
     ------------------ */
  b.mLeft = a;
  a.mParent = b;
  b.mRight = c;
  c.mParent = b;


  /* ------------------
     Make:   b
            / \
           a   c
          / \
         T0 T1
     ------------------ */
  a.mLeft = T0;
  if ( T0 != null ) T0.mParent = a;
  a.mRight = T1;
  if ( T1 != null ) T1.mParent = a;

  /* ------------------
     Make:   b
            / \
           a   c
              / \
             T2 T3
     ------------------ */
  c.mLeft = T2;
  if ( T2 != null ) T2.mParent= c;
  c.mRight= T3;
  if ( T3 != null ) T3.mParent= c;

  recompHeight(a);
  recompHeight(c);

  return b;
}

/* =======================================================
removeMin(): delete minimum priority
======================================================= */
public Elem getMin()
{
	Elem min, curr;   // Help variable
	ArrayList<Elem> k = new ArrayList<Elem>();
	
	if( root == null) return null;
	min = root;
	k.add( root );
	while( !(k.isEmpty() ) )
	{
		curr = k.remove(0);
		if( curr.mPriority < min.mPriority )
		{
			min = curr;
		}
		if( curr.mLeft != null ) k.add( curr.mLeft );
		if( curr.mRight != null ) k.add( curr.mRight );
	}
    return min;
}

/* =======================================================
   remove(k): delete entry containg key k
   ======================================================= */
public void remove(Elem k)
{
	Elem p;     // Help variables
	Elem parent;   // parent node
	Elem succ;     // successor node

    /* --------------------------------------------
       Find the node with key == "key" in the BST
       -------------------------------------------- */
    p = findEntry(k);

    if ( !( Integer.compare( k.mElem, p.mElem ) == 0 ) )
       return;			// Not found ==> nothing to delete....


    /* ========================================================
	  Hibbard's Algorithm
	  ======================================================== */

    if ( p.mLeft == null && p.mRight == null ) // Case 0: p has no children
    {
	  parent = p.mParent;

	  if( parent == null ) root = null;
	  else
	  {
		  /* --------------------------------
		     Delete p from p's parent
		     -------------------------------- */
		  if ( parent.mLeft == p )
		     parent.mLeft = null;
		  else
		     parent.mRight = null;
	
		  k = null;
	       /* --------------------------------------------
	          Recompute the height of all parent nodes...
	          -------------------------------------------- */
	       recompHeight( parent );
	
	       /* --------------------------------------------
	          Re-balance AVL tree starting at ActionPos
	          -------------------------------------------- */
	       rebalance ( parent );     // Rebalance AVL tree after delete at parent
	  }
      return;
    }

    if ( p.mLeft == null )                 // Case 1a: p has 1 (right) child
    {
       parent = p.mParent;

	  /* ----------------------------------------------
	     Link p's right child as p's parent child
          ---------------------------------------------- */
       if ( parent == null ) // root case
       {
    	   root = p.mRight;
    	   root.mParent = null;
    	   /* --------------------------------------------
	          Recompute the height of all parent nodes...
	          -------------------------------------------- */
	       recompHeight( parent );
	
	       /* --------------------------------------------
	          Re-balance AVL tree starting at ActionPos
	          -------------------------------------------- */
	       rebalance ( parent );     // Rebalance AVL tree after delete at parent
       }
       else
       {
	       if ( parent.mLeft == p )
	          parent.mLeft = p.mRight;
	       else
	          parent.mRight = p.mRight;
	
	       /* --------------------------------------------
	          Recompute the height of all parent nodes...
	          -------------------------------------------- */
	       recompHeight( parent );
	
	       /* --------------------------------------------
	          Re-balance AVL tree starting at ActionPos
	          -------------------------------------------- */
	       rebalance ( parent );     // Rebalance AVL tree after delete at parent
       }
       return;
    }

    if ( p.mRight == null )                 // Case 1b: p has 1 (left) child
    {
       parent = p.mParent;

       /* ----------------------------------------------
          Link p's left child as p's parent child
          ---------------------------------------------- */
       if ( parent == null ) // root case
       {
    	   root = p.mLeft;
    	   root.mParent = null;
    	   /* --------------------------------------------
	          Recompute the height of all parent nodes...
	          -------------------------------------------- */
	       recompHeight( parent );
	
	       /* --------------------------------------------
	          Re-balance AVL tree starting at ActionPos
	          -------------------------------------------- */
	       rebalance ( parent );     // Rebalance AVL tree after delete at parent
       }
       else
       {
	       if ( parent.mLeft == p )
	          parent.mLeft = p.mLeft;
	       else
	          parent.mRight = p.mLeft;
	
	       /* --------------------------------------------
	          Recompute the height of all parent nodes...
	          -------------------------------------------- */
	       recompHeight( parent );
	
	       /* --------------------------------------------
	          Re-balance AVL tree starting at ActionPos
	          -------------------------------------------- */
	       rebalance ( parent );     // Rebalance AVL tree after delete at parent
	
	       return;
       }
    }

    /* ================================================================
	  Tough case: node has 2 children - find successor of p

	  succ(p) is as as follows:  1 step right, all the way left

	  Note: succ(p) has NOT left child !
       ================================================================ */
    succ = p.mRight;			// p has 2 children....

    while ( succ.mLeft != null )
	   succ = succ.mLeft;

    p.mElem = succ.mElem;		// Replace p with successor
    p.mPriority = succ.mPriority;


    /* --------------------------------
       Delete succ from succ's parent
       -------------------------------- */
    parent = succ.mParent;		// Prepare for deletion

    parent.mLeft = succ.mRight;	// Link right tree to parent's left

    /* --------------------------------------------
       Recompute the height of all parent nodes...
       -------------------------------------------- */
    recompHeight( parent );

    /* --------------------------------------------
       Re-balance AVL tree starting at ActionPos
       -------------------------------------------- */
    rebalance ( parent );     // Rebalance AVL tree after delete at parent

    return;

}


public void rebalance(Elem p)
{
	Elem x, y, z;

   while ( p != null )
   { 
      if ( diffHeight(p.mLeft, p.mRight) > 1 )
      {

         x = p;
         y = tallerChild( x );
         z = tallerChild( y );

         //System.out.println("After tri_node_restructure: " + x.getValue() + " " + y.getValue() + " " + z.getValue());
         //System.out.println();
         p = tri_node_restructure( x, y, z );

	    //printBST();
         //System.out.println("==========================================");

      }

      p = p.mParent;
   }
}

public Elem tallerChild(Elem p)
{
   if ( p.mLeft == null )
	 return p.mRight;

   if ( p.mRight == null )
	 return p.mLeft;

   if ( p.mLeft.mHeight > p.mRight.mHeight )
	 return p.mLeft;
   else
	 return p.mRight;
}


/* =======================================================
   Show what the BST look like....
   ======================================================= */
public void printnode(Elem x, int h)
{
   for (int i = 0; i < h; i++)
      System.out.print("               ");

   System.out.print("[" + x.mElem + "," + x.mPriority + "](h=" + x.mHeight + ")");

   if ( diffHeight( x.mLeft, x.mRight) > 1 )
      System.out.println("*");
   else
      System.out.println();
}

public void printBST()
{
   showR( root, 0 );
   System.out.println("================================");
}

public void showR(Elem t, int h)
{
   if (t == null)
      return;

   showR(t.mRight, h + 1);
   printnode(t, h);
   showR(t.mLeft, h + 1);
}


/* ================================================================
   maxHeight(t1,t2): compute max height of 2 (sub)trees
   ================================================================ */
public int maxHeight( Elem t1, Elem t2 )
{
   int h1, h2;

   if ( t1 == null )
      h1 = 0;
   else
      h1 = t1.mHeight;

   if ( t2 == null )
      h2 = 0;
   else
      h2 = t2.mHeight;

   return (h1 >= h2) ? h1 : h2 ;
}

/* ================================================================
   diffHeight(t1,t2): compute difference in height of 2 (sub)trees
   ================================================================ */
public int diffHeight( Elem t1, Elem t2 )
{
   int h1, h2;

   if ( t1 == null )
      h1 = 0;
   else
      h1 = t1.mHeight;

   if ( t2 == null )
      h2 = 0;
   else
      h2 = t2.mHeight;

   return ((h1 >= h2) ? (h1-h2) : (h2-h1)) ;
}

/* ================================================================
   recompHeight(x): recompute height starting at x (and up)
   ================================================================ */
public void recompHeight( Elem x )
{
   while ( x != null )
   {
      x.mHeight = maxHeight( x.mLeft, x.mRight ) + 1;
      x = x.mParent;
   }
}

}
