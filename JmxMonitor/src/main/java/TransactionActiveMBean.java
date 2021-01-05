public interface TransactionActiveMBean {
    //-----------
    // operations
    //-----------

      public javax.management.ObjectName objectName();

//    public void sayHello();
//    public int add(int x, int y);

    //-----------
    // attributes
    //-----------

    public java.lang.Number getNumber();
    public java.lang.Object getValue();

    // a read-only attribute called Name of type String
//    public String getName();

    // a read-write attribute called CacheSize of type int
    //public int getCacheSize();
    //public void setCacheSize(int size);
}