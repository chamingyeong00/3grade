// FifoCore.java
package memorypolicy;

import java.util.*;

public class FifoCore implements CorePolicy {
    private int cursor = 0;
    private final int p_frame_size;
    private final Queue<Page> frame_window;
    private final List<Page> pageHistory;

    private int hit = 0;
    private int fault = 0;
    private int migration = 0;

    public FifoCore(int frame_size) {
        this.p_frame_size = frame_size;
        frame_window = new LinkedList<>();
        pageHistory = new ArrayList<>();
    }

    @Override
    public Page.STATUS operate(char data) {
        Page newPage = new Page();
        boolean found = false;
        int locIndex = -1;
        int index = 0;

        for (Page page : frame_window) {
            if (page.data == data) {
                found = true;
                locIndex = index;
                break;
            }
            index++;
        }

        newPage.pid = Page.CREATE_ID++;
        newPage.data = data;

        if (found) {
            newPage.status = Page.STATUS.HIT;
            hit++;
            newPage.loc = locIndex + 1;
        } else {
            if (frame_window.size() >= p_frame_size) {
                newPage.status = Page.STATUS.MIGRATION;
                frame_window.poll();
                cursor = p_frame_size;
                migration++;
                fault++;
            } else {
                newPage.status = Page.STATUS.PAGEFAULT;
                cursor++;
                fault++;
            }
            newPage.loc = cursor;
            frame_window.offer(newPage);
        }

        pageHistory.add(newPage);
        return newPage.status;
    }

    @Override
    public int getHitCount() { return hit; }
    @Override
    public int getFaultCount() { return fault; }
    @Override
    public int getMigrationCount() { return migration; }
    @Override
    public List<Page> getPageHistory() { return pageHistory; }

    // 🔽 추가 구현
    @Override
    public Queue<Page> getCurrentFrames() {
        return new LinkedList<>(frame_window);
    }

    @Override
    public int getCursor() {
        return cursor;
    }

    @Override
    public int getFrameSize() {
        return p_frame_size;
    }
    @Override
    public List<Page> getFrameStateAtStep(int step) {
        Queue<Page> sim = new LinkedList<>();
        for (int i = 0; i <= step && i < pageHistory.size(); i++) {
            Page p = pageHistory.get(i);
            if (sim.stream().anyMatch(pg -> pg.data == p.data)) {
                continue;
            }
            if (sim.size() == p_frame_size) sim.poll();
            sim.offer(p);
        }
        return new ArrayList<>(sim);
    }

}
