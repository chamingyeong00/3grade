// ScCore.java
package memorypolicy;

import java.util.*;

public class ScCore implements CorePolicy {
    private final int frameSize;
    private final List<Page> pageHistory;
    private final int[] frames;
    private final boolean[] secondChance;
    private int pointer = 0;
    private int hit = 0, fault = 0, migration = 0;
    private int currentTime = 0;
    private final Queue<Page> frameWindow;
    private final List<List<Character>> frameSnapshots; // 🔽 추가: 시간별 스냅샷 저장

    public ScCore(int frameSize) {
        this.frameSize = frameSize;
        this.frames = new int[frameSize];
        Arrays.fill(frames, -1);
        this.secondChance = new boolean[frameSize];
        this.pageHistory = new ArrayList<>();
        this.frameWindow = new LinkedList<>();
        this.frameSnapshots = new ArrayList<>();
    }

    @Override
    public List<Page> getFrameStateAtStep(int step) {
        if (step >= frameSnapshots.size()) return new ArrayList<>();

        List<Character> snapshot = frameSnapshots.get(step);
        List<Page> result = new ArrayList<>();

        for (int i = 0; i < snapshot.size(); i++) {
            Character c = snapshot.get(i);
            if (c == null) continue;
            Page p = new Page();
            p.data = c;
            p.loc = i + 1;
            result.add(p);
        }

        return result;
    }

    @Override
    public Page.STATUS operate(char dataChar) {
        currentTime++;
        int pageNum = dataChar;
        Page page = new Page();
        page.pid = Page.CREATE_ID++;
        page.data = dataChar;

        for (int i = 0; i < frameSize; i++) {
            if (frames[i] == pageNum) {
                secondChance[i] = true;
                hit++;
                page.status = Page.STATUS.HIT;
                page.loc = i + 1;
                pageHistory.add(page);
                refreshFrameWindow();
                recordSnapshot();
                return page.status;
            }
        }

        for (int i = 0; i < frameSize; i++) {
            if (frames[i] == -1) {
                frames[i] = pageNum;
                secondChance[i] = false;
                fault++;
                page.status = Page.STATUS.PAGEFAULT;
                page.loc = i + 1;
                pageHistory.add(page);
                refreshFrameWindow();
                recordSnapshot();
                return page.status;
            }
        }

        while (true) {
            if (!secondChance[pointer]) {
                frames[pointer] = pageNum;
                secondChance[pointer] = false;
                page.status = Page.STATUS.MIGRATION;
                page.loc = pointer + 1;
                pointer = (pointer + 1) % frameSize;
                fault++;
                migration++;
                break;
            } else {
                secondChance[pointer] = false;
                pointer = (pointer + 1) % frameSize;
            }
        }

        pageHistory.add(page);
        refreshFrameWindow();
        recordSnapshot();
        return page.status;
    }

    private void refreshFrameWindow() {
        frameWindow.clear();
        for (int i = 0; i < frameSize; i++) {
            if (frames[i] != -1) {
                Page p = new Page();
                p.data = (char) frames[i];
                p.loc = i + 1;
                frameWindow.add(p);
            }
        }
    }

    private void recordSnapshot() {
        List<Character> snapshot = new ArrayList<>();
        for (int val : frames) {
            snapshot.add(val == -1 ? null : (char) val);
        }
        frameSnapshots.add(snapshot);
    }

    public List<List<Character>> getFrameSnapshots() {
        return frameSnapshots;
    }

    @Override
    public int getHitCount() { return hit; }
    @Override
    public int getFaultCount() { return fault; }
    @Override
    public int getMigrationCount() { return migration; }
    @Override
    public List<Page> getPageHistory() { return pageHistory; }

    @Override
    public Queue<Page> getCurrentFrames() {
        return new LinkedList<>(frameWindow);
    }

    @Override
    public int getCursor() {
        return pointer;
    }

    @Override
    public int getFrameSize() {
        return frameSize;
    }
}
