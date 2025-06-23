package memorypolicy;

import java.util.*;

public class LfuScHybridCore implements CorePolicy {
    private final int p_frame_size;
    private final List<Page> frame_window;
    private final List<Page> pageHistory;
    private final Map<Character, Integer> frequencyMap;
    private final Map<Character, Boolean> referenceBit;

    private int hit = 0;
    private int fault = 0;
    private int migration = 0;
    private int cursor = 0;

    public LfuScHybridCore(int frame_size) {
        this.p_frame_size = frame_size;
        this.frame_window = new ArrayList<>();
        this.pageHistory = new ArrayList<>();
        this.frequencyMap = new HashMap<>();
        this.referenceBit = new HashMap<>();
    }

    @Override
    public Page.STATUS operate(char data) {
        Page newPage = new Page();
        newPage.pid = Page.CREATE_ID++;
        newPage.data = data;

        boolean found = false;

        for (Page p : frame_window) {
            if (p.data == data) {
                found = true;
                break;
            }
        }

        if (found) {
            hit++;
            newPage.status = Page.STATUS.HIT;
            frequencyMap.put(data, frequencyMap.get(data) + 1);
            referenceBit.put(data, true);
        } else {
            fault++;

            if (frame_window.size() >= p_frame_size) {
                Page victim = selectVictim();
                if (victim != null) {
                    frame_window.remove(victim);
                    frequencyMap.remove(victim.data);
                    referenceBit.remove(victim.data);
                }
                migration++;
                newPage.status = Page.STATUS.MIGRATION;
            } else {
                newPage.status = Page.STATUS.PAGEFAULT;
            }

            frame_window.add(newPage);
            frequencyMap.put(data, 1);
            referenceBit.put(data, true);
        }

        // 정렬 및 loc 설정
        frame_window.sort(Comparator
                .comparingInt((Page p) -> frequencyMap.get(p.data))
                .thenComparingInt(p -> p.pid));

        for (int i = 0; i < frame_window.size(); i++) {
            if (frame_window.get(i).data == data) {
                newPage.loc = i + 1;
                break;
            }
        }

        cursor = frame_window.size();
        pageHistory.add(newPage);
        return newPage.status;
    }

    private Page selectVictim() {
        for (int round = 0; round < 2; round++) {
            frame_window.sort(Comparator
                    .comparingInt((Page p) -> frequencyMap.get(p.data))
                    .thenComparingInt(p -> p.pid));

            for (Page p : frame_window) {
                if (Boolean.FALSE.equals(referenceBit.getOrDefault(p.data, false))) {
                    return p;
                } else if (round == 0) {
                    referenceBit.put(p.data, false);
                }
            }
        }
        return null; // fallback (should not happen)
    }

    @Override
    public List<Page> getFrameStateAtStep(int step) {
        Map<Character, Integer> freq = new HashMap<>();
        LinkedHashMap<Character, Page> currentMap = new LinkedHashMap<>();

        for (int i = 0; i <= step && i < pageHistory.size(); i++) {
            Page p = pageHistory.get(i);
            freq.put(p.data, freq.getOrDefault(p.data, 0) + 1);
            currentMap.put(p.data, p);
        }

        List<Page> current = new ArrayList<>(currentMap.values());
        current.sort(Comparator
                .comparingInt((Page p) -> freq.get(p.data))
                .thenComparingInt(p -> p.pid));
        while (current.size() > p_frame_size) {
            current.remove(0);
        }

        for (int i = 0; i < current.size(); i++) {
            current.get(i).loc = i + 1;
        }

        return current;
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
        Map<Character, Page> uniqueMap = new LinkedHashMap<>();
        for (Page p : frame_window) {
            uniqueMap.put(p.data, p);
        }
        List<Page> list = new ArrayList<>(uniqueMap.values());
        list.sort(Comparator
                .comparingInt((Page p) -> frequencyMap.getOrDefault(p.data, 0))
                .thenComparingInt(p -> p.pid));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).loc = i + 1;
        }
        return new LinkedList<>(list);
    }

    @Override
    public int getCursor() { return cursor; }
    @Override
    public int getFrameSize() { return p_frame_size; }
}