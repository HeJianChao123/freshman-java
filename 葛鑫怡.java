

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class 葛鑫怡 {
    // 窗口数量（越多多爱心爱心越密集）
    private static final int WINDOW_COUNT = 100;
    // 爱心缩放比例
    private static final int SCALE = 25;
    // 窗口大小
    private static final int WINDOW_WIDTH = 100;
    private static final int WINDOW_HEIGHT = 80;

    // 存储所有创建的窗口
    private static List<JFrame> windows = new ArrayList<>();
    private static Random random = new Random();
    private static List<FloatingText> floatingTexts = new ArrayList<>();

    public static void main(String[] args) {
        // 确保UI操作在事件调度线程中执行
        SwingUtilities.invokeLater(() -> {
            // 获取屏幕中心位置
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int centerX = screenSize.width / 2;
            int centerY = screenSize.height / 2;

            // 生成爱心形状的坐标点
            List<Point> heartPoints = generateHeartPoints(WINDOW_COUNT);

            // 依次创建窗口并显示
            for (int i = 0; i < WINDOW_COUNT; i++) {
                Point point = heartPoints.get(i);
                // 计算窗口位置（基于爱心坐标和屏幕中心）
                int x = centerX + (int) point.x - WINDOW_WIDTH / 2;
                int y = centerY - (int) point.y - WINDOW_HEIGHT / 2;

                // 创建窗口
                JFrame frame = createWindow(x, y, "刘雨熙");

                // 设置窗口依次出现的延迟
                try {
                    Thread.sleep(30); // 每个窗口间隔30毫秒出现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 生成爱心形状的坐标点（使用爱心曲线参数方程）
    private static List<Point> generateHeartPoints(int count) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // 参数方程参数
            double t = (i * 2.0 * Math.PI) / count;
            // 爱心曲线参数方程
            double x = 16 * Math.pow(Math.sin(t), 3);
            double y = 13 * Math.cos(t) - 5 * Math.cos(2 * t)
                    - 2 * Math.cos(3 * t) - Math.cos(4 * t);

            // 缩放坐标
            points.add(new Point((int) (x * SCALE), (int) (y * SCALE)));
        }
        return points;
    }

    // 创建单个窗口
    private static JFrame createWindow(int x, int y, String content) {
        JFrame frame = new JFrame();
        // 设置窗口标题
        frame.setTitle("窗口");
        // 设置窗口位置和大小
        frame.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
        // 关闭窗口时不退出程序（直到最后一个窗口关闭）
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 窗口不可调整大小
        frame.setResizable(false);

        // 添加内容标签
        JLabel label = new JLabel(content, SwingConstants.CENTER);
        label.setFont(new Font("宋体", Font.BOLD, 14));
        label.setForeground(Color.RED);
        frame.add(label);

        // 设置窗口背景色
        frame.getContentPane().setBackground(Color.PINK);

        // 显示窗口
        frame.setVisible(true);

        // 将窗口添加到列表中以便后续关闭
        windows.add(frame);

        return frame;
    }

    // 添加自动关闭功能
    static {
        // 创建关闭计时器
        Timer closeTimer = new Timer(8000, e -> { // 8秒后自动关闭所有窗口
            closeAllWindows();
            // 关闭后显示满屏飘字效果
            showFloatingTextEffect();
        });
        closeTimer.setRepeats(false); // 只执行一次
        closeTimer.start();
    }

    // 关闭所有窗口的方法
    private static void closeAllWindows() {
        SwingUtilities.invokeLater(() -> {
            for (JFrame window : windows) {
                if (window != null && window.isVisible()) {
                    window.dispose();
                }
            }
            windows.clear();
        });
    }

    // 显示满屏飘字效果
    private static void showFloatingTextEffect() {
        SwingUtilities.invokeLater(() -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // 创建飘字窗口
            JFrame floatingFrame = new JFrame();
            floatingFrame.setUndecorated(true); // 无边框
            floatingFrame.setBackground(new Color(0, 0, 0, 0)); // 透明背景
            floatingFrame.setAlwaysOnTop(true); // 始终在最前
            floatingFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 全屏
            floatingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // 创建飘字面板
            FloatingPanel floatingPanel = new FloatingPanel();
            floatingFrame.add(floatingPanel);
            floatingFrame.setVisible(true);

            // 创建30个飘字对象
            for (int i = 0; i < 30; i++) {
                floatingTexts.add(new FloatingText(screenWidth, screenHeight));
            }

            // 启动动画计时器
            Timer animationTimer = new Timer(30, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    floatingPanel.repaint();
                }
            });
            animationTimer.start();

            // 10秒后关闭飘字效果
            Timer closeTimer = new Timer(10000, ev -> {
                animationTimer.stop();
                floatingFrame.dispose();
                System.exit(0);
            });
            closeTimer.setRepeats(false);
            closeTimer.start();
        });
    }

    // 飘字面板
    static class FloatingPanel extends JPanel {
        public FloatingPanel() {
            setOpaque(false); // 透明背景
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // 设置抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制所有飘字
            for (FloatingText text : floatingTexts) {
                text.update();
                text.draw(g2d);
            }
        }
    }

    // 飘字对象
    static class FloatingText {
        private String text;
        private float x, y;
        private float speed;
        private float angle;
        private Color color;
        private int fontSize;
        private int screenWidth, screenHeight;

        public FloatingText(int screenWidth, int screenHeight) {
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;

            // 随机文本内容
            String[] texts = {"臭宝天天开心", "刘雨熙", "天天开心", "Happy Every Day", "永远快乐",""};
            this.text = texts[random.nextInt(texts.length)];

            // 随机初始位置
            this.x = random.nextInt(screenWidth);
            this.y = random.nextInt(screenHeight);

            // 随机速度
            this.speed = 1 + random.nextFloat() * 3;

            // 随机角度
            this.angle = random.nextFloat() * 360;

            // 随机颜色
            Color[] colors = {
                    Color.RED, Color.PINK, Color.MAGENTA, Color.ORANGE,
                    Color.YELLOW, Color.CYAN, Color.GREEN, Color.WHITE
            };
            this.color = colors[random.nextInt(colors.length)];

            // 随机字体大小
            this.fontSize = 20 + random.nextInt(30);
        }

        public void update() {
            // 更新位置
            x += Math.cos(Math.toRadians(angle)) * speed;
            y += Math.sin(Math.toRadians(angle)) * speed;

            // 边界检测和反弹
            if (x < 0) {
                x = 0;
                angle = 180 - angle;
            } else if (x > screenWidth) {
                x = screenWidth;
                angle = 180 - angle;
            }

            if (y < 0) {
                y = 0;
                angle = -angle;
            } else if (y > screenHeight) {
                y = screenHeight;
                angle = -angle;
            }

            // 随机改变角度（模拟自然飘动）
            if (random.nextInt(100) < 5) {
                angle += (random.nextFloat() - 0.5f) * 30;
            }
        }

        public void draw(Graphics2D g2d) {
            // 设置字体
            Font font = new Font("微软雅黑", Font.BOLD, fontSize);
            g2d.setFont(font);

            // 设置颜色（带透明度）
            Color textColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
            g2d.setColor(textColor);

            // 绘制文字
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, x - textWidth / 2, y);

            // 绘制文字阴影（增强视觉效果）
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.drawString(text, x - textWidth / 2 + 2, y + 2);
        }
    }
}