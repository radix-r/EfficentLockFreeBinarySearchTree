import org.junit.Assert;
import org.junit.Test;


public class BSTTester {

    public static void main( String[] args){

        BSTTester test = new BSTTester();

        System.out.println("Starting tests");
        test.addTest();
        test.cmpTest();
        test.findPredTest();
        // test.removeTest();
        System.out.println("Tests finished");

        test = new BSTTester();
        STMBST stmTest = new STMBST();
        BST bst = new BST();

        // Throughput testing



        // add



        // contains

        // remove

    }

    @Test
    public void addTest(){
        LockFreeBST bst = new LockFreeBST();

        boolean added;
        boolean in;

        in = bst.contains(5);
        Assert.assertFalse ("Empty tree contains 5", in);


        added  = bst.add(5);
        Assert.assertTrue ("Add 5", added);
        in = bst.contains(5);
        Assert.assertTrue ("Contains 5", in);
        added  = bst.add(10);
        Assert.assertTrue ("Add 10", added);
        in = bst.contains(10);
        Assert.assertTrue ("Contains 10", in);

        in = bst.contains(5);
        Assert.assertTrue ("Contains 5", in);

        added  = bst.add(10);
        Assert.assertFalse("Re add 10", added);


        added = bst.add(4);
        in = bst.contains(4);
        Assert.assertTrue ("Add 4", added);
        Assert.assertTrue ("4 in tree", in);
        added = bst.add(1);
        in = bst.contains(1);
        Assert.assertTrue ("Add 1", added);
        Assert.assertTrue ("1 in tree", in);
        added = bst.add(6);
        in = bst.contains(6);
        Assert.assertTrue ("Add 6", added);
        Assert.assertTrue ("6 in tree", in);

    }

    @Test
    public void cmpTest(){
        int out = LockFreeBST.cmp(1,1);
        Assert.assertEquals("cmp 1,1",2,out);
        out = LockFreeBST.cmp(1,0);
        Assert.assertEquals("cmp 1,0",1,out);
        out = LockFreeBST.cmp(0,1);
        Assert.assertEquals("cmp 1,0",0,out);

        out = LockFreeBST.cmp(5,Integer.MAX_VALUE);
        Assert.assertEquals("cmp 1,0",0,out);

        out = LockFreeBST.cmp(5,Integer.MIN_VALUE);
        Assert.assertEquals("cmp 1,0",1,out);

    }

    @Test
    public void findPredTest(){
        LockFreeBST bst = new LockFreeBST();

        // populate tree
        boolean added;
        boolean in;
        added = bst.add(4);
        added = bst.add(1);
        added = bst.add(10);
        added = bst.add(6);
        in = bst.contains(6);
        added = bst.add(5);
        added = bst.add(9);
        added = bst.add(-5);
        added = bst.add(-4);

        Node pre;
        int[] dummy = new int[1];

        in = bst.contains(6);
        pre = bst.findPred(10,dummy,dummy);
        Assert.assertEquals("Pred of 10",9,pre.k);

        pre = bst.findPred(1,dummy,dummy);
        Assert.assertEquals("Pred of 1",-4,pre.k);

        pre = bst.findPred(4,dummy,dummy);
        Assert.assertEquals("Pred of 4",1,pre.k);

        // test with vals not in tree
        pre = bst.findPred(11,dummy,dummy);
        Assert.assertEquals("Pred of 11",10,pre.k);

        pre = bst.findPred(3, dummy,dummy);
        Assert.assertEquals("Pred of 3",1,pre.k);

        pre = bst.findPred(-6, dummy,dummy);
        Assert.assertEquals("Pred of -6",-5,pre.k);
    }

    @Test
    public void removeTest(){
        LockFreeBST bst = new LockFreeBST();


        boolean added;
        boolean in;
        boolean removed;

        // populate tree
        added = bst.add(4);
        added = bst.add(1);
        added = bst.add(10);
        added = bst.add(6);

        added = bst.add(5);
        added = bst.add(9);
        added = bst.add(-5);
        added = bst.add(-4);

        // attempt removal
        in = bst.contains(6);
        Assert.assertTrue("Contains 6", in);
        removed = bst.remove(6);
        Assert.assertTrue("Removed 6", removed);
        in = bst.contains(6);
        Assert.assertFalse("Contains 6", in);



    }

}
