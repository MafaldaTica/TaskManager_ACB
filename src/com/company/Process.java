package com.company;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Process implements Serializable, Comparable<Process>{
    private static final long serialVersionID = 1L;
    private static final AtomicLong atomicRefId = new AtomicLong();

    private transient long refID;

    public void ObjBase() {
        refID = atomicRefId.incrementAndGet();
    }

    public String getpId() {
        return pId;
    }

    public long getrefID() {
        return refID;
    };
    private String pId;
    private Priority priority;
    private Timestamp creationTimestamp;

    // An array of 64+2 digits
    private final static char[] DIGITS66 = {
            '0','1','2','3','4','5','6','7','8','9',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '-','.','_','~'
    };

    public static String toIDString(long i) {
        char[] buf = new char[32];
        int z = 64; // 1 << 6;
        int cp = 32;
        long b = z - 1;
        do {
            buf[--cp] = DIGITS66[(int)(i & b)];
            i >>>= 6;
        } while (i != 0);
        return new String(buf, cp, (32-cp));
    };

    public String createSalt() {
        String ts = String.valueOf(System.currentTimeMillis());
//        String rand = UUID.randomUUID().toString();
//        return DigestUtils.sha1Hex(ts + rand);

        UUID u = UUID.randomUUID();
        return toIDString(u.getMostSignificantBits() + u.getLeastSignificantBits());

    }

    public Process(String priority, Timestamp timestamp) {

        refID = atomicRefId.incrementAndGet();
        pId = createSalt();
        setPriority(priority);
        this.creationTimestamp = timestamp;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        switch (priority) {
            case "medium":
                this.priority = Priority.MEDIUM;
                break;
            case "high":
                this.priority = Priority.HIGH;
                break;
            default:
                this.priority = Priority.LOW;
        }
    }

    public int getValueFromPrio() {
        int value;
        switch (getPriority()) {
            case MEDIUM:
                value = 2;
                break;
            case HIGH:
                value = 1;
                break;
            default:
                value = 3;
        }
        //System.out.println("Pro value is: "+value);
        return value;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
    @Override
    public String toString() {
        return "Process{" +
                "pid='" + pId + '\'' +
                ", priority='" + priority + '\'' +
                ", timestamp='" + creationTimestamp + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(pId, priority, creationTimestamp);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return Objects.equals(priority, process.priority);
        /* return Objects.equals(process.pId, pId) &&
                Objects.equals(priority, process.priority);

         */
    }

    @Override
    public int compareTo(Process proc) {
        /*
        If compareTo returns:
        0  - if both are the same
        <0 - if this process's timestamp is lower than the passed timestamp's process.
        >0 - if this process's timestamp is higher than the passed timestamp's process.
         */

        //System.out.println("Results of the compare");
        //System.out.println("OBJ1: " + this.toString()+ "OBJ2: "+ proc.toString());
        int iTimestamp = this.creationTimestamp.compareTo(proc.getCreationTimestamp());
        /*
        System.out.println("1. Result compare timestamps: " + iTimestamp);
        int iPriority = this.priority.compareTo(proc.getPriority());
        System.out.println("2. Result compare priority: " + iPriority);
        //long lPID = Long.compare(this.pId, proc.getpId());
        int iPID = this.pId.compareTo(proc.getpId());
        System.out.println("3. Result compare PID: " + iPID);
        */
        int i=iTimestamp;
        return i;

        //return 0;
    }

}
