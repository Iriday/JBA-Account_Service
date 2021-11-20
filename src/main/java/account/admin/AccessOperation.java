package account.admin;

public enum AccessOperation {
    LOCK(false, "locked"),
    UNLOCK(true, "unlocked");

    public final boolean boolValue;
    public final String strValue;

    AccessOperation(boolean boolVal, String strVal) {
        this.boolValue = boolVal;
        this.strValue = strVal;
    }
}
