// LfuCore.java
package memorypolicy;

import java.util.*;

public class LfuCore implements CorePolicy {
    private final int p_frame_size;
    private final List<Page> frame_window;
    private final List<Page> pageHistory;
    private final Map<Character, Integer> frequencyMap;

    private int hit = 0;
    private int fault = 0;
    private int migration = 0;
    private int cursor = 0;

    public LfuCore(int frame_size) {
        this.p_frame_size = frame_size;
        this.frame_window = new ArrayList<>();
        this.pageHistory = new ArrayList<>();
        this.frequencyMap = new HashMap<>();
    }

    @Override
    public Page.STATUS operate(char data) {
        Page newPage = new Page();
        newPage.pid = Page.CREATE_ID++;
        newPage.data = data;

        boolean found = false;
        int index = -1;

        for (int i = 0; i < frame_window.size(); i++) {
            if (frame_window.get(i).data == data) {
                found = true;
                index = i;
                break;
            }
        }

        if (found) {
            hit++;
            newPage.status = Page.STATUS.HIT;
            frequencyMap.put(data, frequencyMap.get(data) + 1);

            // 정확한 위치 계산
            frame_window.sort(Comparator
                    .comparingInt((Page p) -> frequencyMap.get(p.data))
                    .thenComparingInt(p -> p.pid));

            for (int i = 0; i < frame_window.size(); i++) {
                if (frame_window.get(i).data == data) {
                    newPage.loc = i + 1;
                    break;
                }
            }
        }else {
            fault++;

            if (frame_window.size() >= p_frame_size) {
                char lfuData = frame_window.get(0).data;
                for (Page p : frame_window) {
                    if (frequencyMap.get(p.data) < frequencyMap.get(lfuData)) {
                        lfuData = p.data;
                    }
                }

                Iterator<Page> it = frame_window.iterator();
                while (it.hasNext()) {
                    if (it.next().data == lfuData) {
                        it.remove();
                        break;
                    }
                }

                frequencyMap.remove(lfuData);
                migration++;
                newPage.status = Page.STATUS.MIGRATION;
            } else {
                newPage.status = Page.STATUS.PAGEFAULT;
            }

            if (!containsData(frame_window, data)) {
                frame_window.add(newPage);
                frequencyMap.put(data, 1);
            }
        }

        frame_window.sort(Comparator
                .comparingInt((Page p) -> frequencyMap.get(p.data))
                .thenComparingInt(p -> p.pid));

        for (int i = 0; i < frame_window.size(); i++) {
            if (frame_window.get(i) == newPage) {
                newPage.loc = i + 1;
                break;
            }
        }

        cursor = frame_window.size();
        pageHistory.add(newPage);
        return newPage.status;
    }

    @Override
    public List<Page> getFrameStateAtStep(int step) {
        Map<Character, Integer> freq = new HashMap<>();
        LinkedHashMap<Character, Page> currentMap = new LinkedHashMap<>();

        for (int i = 0; i <= step && i < pageHistory.size(); i++) {
            Page p = pageHistory.get(i);
            freq.put(p.data, freq.getOrDefault(p.data, 0) + 1);
            currentMap.put(p.data, p); // 중복 자동 제거 (가장 최근의 Page로 덮어씀)
        }

        // 중복 제거 후 리스트로 변환
        List<Page> current = new ArrayList<>(currentMap.values());

        // LFU 정렬 후, frame 크기 초과 시 오래된/적게 사용된 것부터 제거
        current.sort(Comparator
                .comparingInt((Page p) -> freq.get(p.data))
                .thenComparingInt(p -> p.pid));
        while (current.size() > p_frame_size) {
            current.remove(0);
        }

        // loc 설정 (1부터 시작)
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

    // 🔽 추가 구현
    @Override
    public Queue<Page> getCurrentFrames() {
        // 중복 제거를 위해 data 기준으로 필터링
        Map<Character, Page> uniqueMap = new LinkedHashMap<>();
        for (Page p : frame_window) {
            uniqueMap.put(p.data, p); // 중복되면 나중 값으로 덮어씀
        }

        List<Page> list = new ArrayList<>(uniqueMap.values());

        // 정렬 및 loc 재설정
        list.sort(Comparator
                .comparingInt((Page p) -> frequencyMap.getOrDefault(p.data, 0))
                .thenComparingInt(p -> p.pid));

        for (int i = 0; i < list.size(); i++) {
            list.get(i).loc = i + 1;
        }

        return new LinkedList<>(list);
    }

    @Override
    public int getCursor() {
        return cursor;
    }

    @Override
    public int getFrameSize() {
        return p_frame_size;
    }

    private boolean containsData(List<Page> frames, char data) {
        for (Page p : frames) {
            if (p.data == data) return true;
        }
        return false;
    }
}
