package memorypolicy;

import java.util.Objects;

public class Page {
    public static int CREATE_ID = 0;

    public enum STATUS {
        HIT,
        PAGEFAULT,
        MIGRATION
    }

    public int pid;
    public int loc;
    public char data;
    public STATUS status;

    public Page() {}

    public Page(int pid, int loc, char data, STATUS status) {
        this.pid = pid;
        this.loc = loc;
        this.data = data;
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Page page = (Page) obj;
        return data == page.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

}
