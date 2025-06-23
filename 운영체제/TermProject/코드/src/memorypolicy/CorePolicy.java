// CorePolicy.java
package memorypolicy;

import java.util.List;
import java.util.Queue;

public interface CorePolicy {
    Page.STATUS operate(char data);

    int getHitCount();
    int getFaultCount();
    int getMigrationCount();
    List<Page> getPageHistory();

    Queue<Page> getCurrentFrames();
    int getCursor();
    int getFrameSize();
    List<Page> getFrameStateAtStep(int step);
}
