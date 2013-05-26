package avl;

/**
 * This class represents nodes in an AVL tree.
 * AVL tree is implemented in AVLTree.java.
 * 
 * @author Eric Alexander
 *
 * @param <K>	Object type for keys
 * @param <V>	Object type for values
 */
public class AVLNode
{
	private int key;
	private double value;
	private AVLNode left;
	private AVLNode right;
	private int height;
	
	public AVLNode( int k, double v )
	{
		key = k;
		value = v;
		left = null;
		right = null;
		height = 0;
	}
	
	public void setKey( int k )
	{
		key = k;
	}
	
	public int getKey()
	{
		return key;
	}
	
	public void setValue( double v )
	{
		value = v;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public void setLeft( AVLNode n )
	{
		left = n;
	}
	
	public AVLNode getLeft()
	{
		return left;
	}
	
	public void setRight( AVLNode n )
	{
		right = n;
	}
	
	public AVLNode getRight()
	{
		return right;
	}
	
	public void setHeight( int h )
	{
		height = h;
	}
	
	public int getHeight()
	{
		return height;
	}
}
