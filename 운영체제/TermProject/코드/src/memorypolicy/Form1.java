package memorypolicy;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class Form1 extends JFrame {
    private JComboBox<String> comboPolicy;
    private JTextField tbReferenceString;
    private JTextField tbFrameSize;
    private JButton btnRun, btnRand, btnSave;
    private JLabel lbPageFaultRatio;
    private JTextArea tbConsole;
    private JTextArea tbSummary;
    private DrawPanel drawPanel;
    private ChartPanel chartPanel;

    private CorePolicy core;  // 인터페이스 타입으로 변경
    private BufferedImage resultImage;

    public Form1() {
        setTitle("Memory Simulator");
        setSize(1300, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JLabel lbPolicy = new JLabel("Policy");
        lbPolicy.setBounds(20, 10, 50, 25);
        add(lbPolicy);
        comboPolicy = new JComboBox<>(new String[]{"FIFO", "LFU", "SC", "LFU-SC"});
        comboPolicy.setBounds(20, 40, 120, 30);
        add(comboPolicy);

        JLabel lbRef = new JLabel("Reference String");
        lbRef.setBounds(160, 10, 120, 25);
        add(lbRef);

        tbReferenceString = new JTextField();
        tbReferenceString.setBounds(160, 40, 500, 30);
        add(tbReferenceString);

        JLabel lbFrame = new JLabel("#Frame");
        lbFrame.setBounds(680, 10, 60, 25);
        add(lbFrame);

        tbFrameSize = new JTextField("4");
        tbFrameSize.setBounds(680, 40, 60, 30);
        add(tbFrameSize);

        btnRun = new JButton("Run");
        btnRun.setBounds(760, 30, 100, 40);
        add(btnRun);

        btnRand = new JButton("Random");
        btnRand.setBounds(880, 30, 100, 40);
        add(btnRand);

        btnSave = new JButton("Save");
        btnSave.setBounds(1000, 30, 100, 40);
        add(btnSave);

        JLabel lbPFLabel = new JLabel("Page Fault Rate (%) = ");
        lbPFLabel.setBounds(900, 680, 180, 30);
        add(lbPFLabel);

        lbPageFaultRatio = new JLabel("0.00%");
        lbPageFaultRatio.setBounds(1080, 680, 100, 30);
        add(lbPageFaultRatio);

        // drawPanel 생성 및 JScrollPane에 넣기
        drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(1500, 35 * 20)); // ← 예비 높이 설정 (세로 스크롤 위해 큼직하게)

        JScrollPane scrollPane = new JScrollPane(drawPanel);
        scrollPane.setBounds(20, 100, 850, 570);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // ← 이거로 수정!
        add(scrollPane);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBounds(900, 100, 380, 570);
        add(rightPanel);

        JLabel lblConsole = new JLabel("Console Output:");
        lblConsole.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(lblConsole);

        tbConsole = new JTextArea();
        tbConsole.setEditable(false);
        tbConsole.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tbConsole.setLineWrap(true);
        tbConsole.setWrapStyleWord(true);
        JScrollPane scrollConsole = new JScrollPane(tbConsole);
        scrollConsole.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollConsole.setPreferredSize(new Dimension(380, 200));
        scrollConsole.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
        scrollConsole.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(scrollConsole);

        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(380, 180));
        chartPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 180));
        chartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(chartPanel);

        JLabel lblSummary = new JLabel("Summary:");
        lblSummary.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSummary.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        rightPanel.add(lblSummary);

        tbSummary = new JTextArea();
        tbSummary.setEditable(false);
        tbSummary.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tbSummary.setLineWrap(true);
        tbSummary.setWrapStyleWord(true);
        JScrollPane scrollSummary = new JScrollPane(tbSummary);
        scrollSummary.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollSummary.setPreferredSize(new Dimension(380, 160));
        scrollSummary.setMaximumSize(new Dimension(Short.MAX_VALUE, 160));
        scrollSummary.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(scrollSummary);

        resultImage = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_ARGB);

        btnRun.addActionListener(e -> onRun());
        btnRand.addActionListener(e -> onRandom());
        btnSave.addActionListener(e -> onSave());

        tbFrameSize.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });
    }

    private void onRun() {
        String policyStr = (String) comboPolicy.getSelectedItem();
        String refString = tbReferenceString.getText().trim();
        int frameSize;

        if (refString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Reference string is empty");
            return;
        }

        try {
            frameSize = Integer.parseInt(tbFrameSize.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid frame size");
            return;
        }

        // 전체 실행 시간 측정 시작
        long startTime = System.nanoTime();

        // 각 정책별 CorePolicy 객체 생성
        switch (policyStr) {
            case "FIFO" -> core = new FifoCore(frameSize);
            case "LFU" -> core = new LfuCore(frameSize);
            case "SC" -> core = new ScCore(frameSize);
            case "LFU-SC" -> core = new LfuScHybridCore(frameSize);
            default -> {
                JOptionPane.showMessageDialog(this, "Unsupported policy");
                return;
            }
        }

        tbConsole.setText("");

        for (char ch : refString.toCharArray()) {
            Page.STATUS status = core.operate(ch);
            String statusStr = switch (status) {
                case PAGEFAULT -> "Page Fault";
                case MIGRATION -> "Migrated";
                case HIT -> "Hit";
            };
            tbConsole.append("DATA " + ch + " is " + statusStr + "\n");
        }

        // 전체 실행 시간 측정 종료
        long endTime = System.nanoTime();
        double elapsedMillis = (endTime - startTime) / 1_000_000.0;

        // 성능 지연 시간 가정: Page Fault 1건당 5ms
        int totalPageFaults = core.getFaultCount() + core.getMigrationCount();
        double delayByFaults = totalPageFaults * 5.0;

        // 파이차트 생성
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("HIT", core.getHitCount());
        dataset.setValue("FAULT(DISMISS)", totalPageFaults);

        JFreeChart pieChart = ChartFactory.createPieChart("Page Fault Stats", dataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        DecimalFormat df = new DecimalFormat("0");
        DecimalFormat pf = new DecimalFormat("0.00%");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})", df, pf));

        chartPanel.setChart(pieChart);

        int total = core.getHitCount() + core.getFaultCount();
        double ratio = total == 0 ? 0 : ((double) core.getFaultCount() / total) * 100;
        lbPageFaultRatio.setText(String.format("%.2f%%", ratio));

        int numReferences = refString.length();
        int numPages = (int) core.getPageHistory().stream().map(p -> p.data).distinct().count();

        String summaryText = String.format(
                "Number of references: %d%n" +
                        "Number of pages: %d%n" +
                        "Algorithm used: %s%n" +
                        "Number of frames: %d%n" +
                        "Execution time: %.2f ms%n" +
                        "Estimated delay by Page Faults: %.2f ms%n",
                numReferences, numPages, policyStr, frameSize,
                elapsedMillis, delayByFaults
        );
        tbSummary.setText(summaryText);

        drawPanel.setCoreData(core, frameSize, refString.length());
        drawPanel.repaint();


    }

    private void onRandom() {
        Random rand = new Random();
        int count = rand.nextInt(46) + 5;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            char c = (char) (rand.nextInt(26) + 'A');
            sb.append(c);
        }
        tbReferenceString.setText(sb.toString());


    }

    private void onSave() {
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage img = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = img.createGraphics();

                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());

                drawPanel.paint(g2d);
                g2d.dispose();

                javax.imageio.ImageIO.write(img, "jpg", chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Image saved successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save image: " + ex.getMessage());
            }
        }
    }

    static class DrawPanel extends JPanel {
        private CorePolicy core;
        private int windowSize;
        private int dataLength;

        public void setCoreData(CorePolicy core, int windowSize, int dataLength) {
            this.core = core;
            this.windowSize = windowSize;
            this.dataLength = dataLength;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (core == null) return;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            Queue<Character> pseudoQueue = new LinkedList<>();

            int gridSize = 30;
            int gridSpace = 5;

            for (int i = 0; i < dataLength; i++) {
                List<Page> frameAtStep = core.getFrameStateAtStep(i);

                // draw frame box
                for (int j = 0; j < windowSize; j++) {
                    int x = i * (gridSize + gridSpace);
                    int y = (j + 1) * gridSize; // 0번째 줄은 데이터 레이블

                    drawGrid(g2d, x, y, gridSize);
                }

                // draw labels (reference string)
                Page histPage = core.getPageHistory().get(i);
                int labelX = i * (gridSize + gridSpace);
                int labelY = 0;
                drawGridText(g2d, labelX, labelY, gridSize, histPage.data);

                // draw current frame contents
                for (int j = 0; j < frameAtStep.size(); j++) {
                    Page page = frameAtStep.get(j);
                    int x = i * (gridSize + gridSpace);
                    int y = (j + 1) * gridSize;
                    drawGridText(g2d, x, y, gridSize, page.data);
                }

                // draw highlight on the actual accessed frame
                int highlightX = i * (gridSize + gridSpace);
                int highlightY = histPage.loc * gridSize;

                drawGridHighlight(g2d, highlightX, highlightY, gridSize, histPage.status);

                drawGridText(g2d, highlightX, highlightY, gridSize, histPage.data);
            }

        }


        private String colorToString(Color color) {
            if (Color.GREEN.equals(color)) return "GREEN";
            if (Color.RED.equals(color)) return "RED";
            if (new Color(128, 0, 128).equals(color)) return "PURPLE";
            return "UNKNOWN";
        }

        private void drawGrid(Graphics2D g, int x, int y, int size) {
            g.setColor(Color.WHITE);
            g.drawRect(x, y, size, size);
        }

        private void drawGridHighlight(Graphics2D g, int x, int y, int size, Page.STATUS status) {
            Color color = switch (status) {
                case HIT -> Color.GREEN;
                case MIGRATION -> new Color(128, 0, 128);
                case PAGEFAULT -> Color.RED;
            };
            g.setColor(color);
            g.fillRect(x, y, size, size);
        }

        private void drawGridText(Graphics2D g, int x, int y, int size, char value) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.charWidth(value);
            int textHeight = fm.getAscent();
            int tx = x + (size - textWidth) / 2;
            int ty = y + (size + textHeight) / 2 - 4;
            g.drawString(String.valueOf(value), tx, ty);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new Form1().setVisible(true);
        });
    }
}
