package featurecat.lizzie.gui;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Math.min;

import featurecat.lizzie.Config;
import featurecat.lizzie.Lizzie;
import featurecat.lizzie.rules.Board;
import featurecat.lizzie.rules.BoardHistoryNode;
import featurecat.lizzie.rules.NodeInfo;
import featurecat.lizzie.rules.Stone;
import featurecat.lizzie.util.Utils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import org.json.JSONArray;

@SuppressWarnings("serial")
public class MovelistFrame extends JFrame {
  public static Config config;
  public TableModel dataModel;

  public final DecimalFormat FORMAT_PERCENT = new DecimalFormat("#0.00");
  public final DecimalFormat FORMAT_INT = new DecimalFormat("#0");
  private BufferedImage cachedImage;
  private BufferedImage cachedImageMin;
  private int DOT_RADIUS = 3;
  private int[] origParams = {0, 0, 0, 0};
  private int[] params = {0, 0, 0, 0, 0};
  private double maxcoreMean = 30.0;
  public JPanel tablepanel;
  JPanel selectpanel = new JPanel();
  private String oriTitle = "";

  public JTabbedPane topPanel;
  JTabbedPane bottomPanel;
  int selectedIndex = Lizzie.config.movelistSelectedIndex;
  int selectedIndexTop = Lizzie.config.movelistSelectedIndexTop;

  JPanel matchPanel;
  JPanel matchPanelmin;
  JPanel matchPanelAll;

  JPanel mistakePanelAll;
  JPanel matchGraphAll;
  JPanel matchHistogramAll;
  JPanel scoreDiffGraphAll;
  JPanel winrateDiffStatics;
  JPanel scoreDiffStatics;
  JPanel bigMistakePanel;
  JPanel bigScoreMistakePanel;
  SetMatchAiPara setMatchAiPara;
  public boolean isShowingWinrateGraph = Lizzie.config.isShowingWinrateGraph;
  boolean topCurNode = Lizzie.config.moveListTopCurNode;
  JButton detail;
  JButton hideMove;
  JTextField suggestionMoves;
  JTextField percentPlayouts;
  JTextField firstMove;
  JTextField lastMove;
  JTextField winrateDiffRange1;
  JTextField winrateDiffRange2;

  JLabel lblDiffConfig1;
  JLabel lblDiffConfig2;
  JLabel lblMatchConfig1;
  JButton settings;
  JLabel lblMatchConfig2;
  JLabel lblMatchConfig3;
  JLabel lblMatchConfig4;
  JLabel lblMatchConfig5;

  JButton showGraph;

  ImageIcon iconUp;
  ImageIcon iconDown;
  ImageIcon iconSettings;
  public JScrollPane scrollpane;
  int refreshCount = 0;
  public JTable table;

  private JPanel minTablePanel;
  private TableModel minDataModel1;
  private TableModel minDataModel2;
  private JTable minTable1;
  private JTable minTable2;

  private JPanel tablePanelMin1;
  private JPanel tablePanelMin2;
  public JScrollPane minScrollpane1;
  public JScrollPane minScrollpane2;
  public JLabel checkBlackFilter;
  public JLabel checkWhiteFilter;
  Font headFont;
  Font winrateFont;
  // JFrame jf;
  Timer timer;
  public boolean showGra = Lizzie.config.showMoveListGraph;
  public int sortnum = 2;
  public int selectedorder = -1;
  private boolean issorted = false;
  private boolean isOriginOrder = true;

  JSpinner dropwinratechooser = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
  JSpinner dropScoreMeanChooser = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
  JSpinner playoutschooser = new JSpinner(new SpinnerNumberModel(100, 0, 999999, 100));
  JCheckBox checkBlack = new JCheckBox();
  JCheckBox checkWhite = new JCheckBox();
  public JComboBox<String> showBranch;
  int analyzed;
  int analyzedB = 0;
  int analyzedW = 0;
  double blackMatchValue = 0;
  double whiteMatchValue = 0;
  // public int matchTemp=2;

  boolean isKatago = false;

  private int parse1Move = Lizzie.config.parse1Move;
  private int parse2Move = Lizzie.config.parse2Move;

  JLabel lblParse1Move;
  JLabel lblParse2Move;
  JTextField txtParse1Move;
  JTextField txtParse2Move;

  private double parse1BlackValue = 0;
  private int parse1BlackAnalyzed = 0;
  private double parse1BlackWinrateDiff = 0;
  private double parse1BlackScoreDiff = 0;

  private double parse2BlackValue = 0;
  private int parse2BlackAnalyzed = 0;
  private double parse2BlackWinrateDiff = 0;
  private double parse2BlackScoreDiff = 0;

  private double parse3BlackValue = 0;
  private int parse3BlackAnalyzed = 0;
  private double parse3BlackWinrateDiff = 0;
  private double parse3BlackScoreDiff = 0;

  private double parse1WhiteValue = 0;
  private int parse1WhiteAnalyzed = 0;
  private double parse1WhiteWinrateDiff = 0;
  private double parse1WhiteScoreDiff = 0;

  private double parse2WhiteValue = 0;
  private int parse2WhiteAnalyzed = 0;
  private double parse2WhiteWinrateDiff = 0;
  private double parse2WhiteScoreDiff = 0;

  private double parse3WhiteValue = 0;
  private int parse3WhiteAnalyzed = 0;
  private double parse3WhiteWinrateDiff = 0;
  private double parse3WhiteScoreDiff = 0;
  boolean isMainEngine = true;

  private int parse1BlackWinrateMiss1 = 0;
  private int parse1BlackWinrateMiss2 = 0;
  private int parse1BlackWinrateMiss3 = 0;
  private int parse2BlackWinrateMiss1 = 0;
  private int parse2BlackWinrateMiss2 = 0;
  private int parse2BlackWinrateMiss3 = 0;
  private int parse3BlackWinrateMiss1 = 0;
  private int parse3BlackWinrateMiss2 = 0;
  private int parse3BlackWinrateMiss3 = 0;

  private int parse1WhiteWinrateMiss1 = 0;
  private int parse1WhiteWinrateMiss2 = 0;
  private int parse1WhiteWinrateMiss3 = 0;
  private int parse2WhiteWinrateMiss1 = 0;
  private int parse2WhiteWinrateMiss2 = 0;
  private int parse2WhiteWinrateMiss3 = 0;
  private int parse3WhiteWinrateMiss1 = 0;
  private int parse3WhiteWinrateMiss2 = 0;
  private int parse3WhiteWinrateMiss3 = 0;

  private int parse1BlackScoreMiss1 = 0;
  private int parse1BlackScoreMiss2 = 0;
  private int parse1BlackScoreMiss3 = 0;
  private int parse2BlackScoreMiss1 = 0;
  private int parse2BlackScoreMiss2 = 0;
  private int parse2BlackScoreMiss3 = 0;
  private int parse3BlackScoreMiss1 = 0;
  private int parse3BlackScoreMiss2 = 0;
  private int parse3BlackScoreMiss3 = 0;

  private int parse1WhiteScoreMiss1 = 0;
  private int parse1WhiteScoreMiss2 = 0;
  private int parse1WhiteScoreMiss3 = 0;
  private int parse2WhiteScoreMiss1 = 0;
  private int parse2WhiteScoreMiss2 = 0;
  private int parse2WhiteScoreMiss3 = 0;
  private int parse3WhiteScoreMiss1 = 0;
  private int parse3WhiteScoreMiss2 = 0;
  private int parse3WhiteScoreMiss3 = 0;
  private BoardHistoryNode curMouseOverNode;

  private List<bigMistakeInfo> BigMistakeList = new ArrayList<bigMistakeInfo>();
  private int mouseOverBigMistakeIndex = -1; // 0-9

  public MovelistFrame(int engineIndex) {
    // super(new BorderLayout());
    if (engineIndex == 2) isMainEngine = false;
    setLayout(new BorderLayout());
    oriTitle =
        Lizzie.resourceBundle.getString("Movelistframe.title")
            + (Lizzie.frame.extraMode == 2
                ? (isMainEngine
                    ? Lizzie.resourceBundle.getString("Movelistframe.titleMain")
                    : Lizzie.resourceBundle.getString("Movelistframe.titleSub"))
                : "")
            + Lizzie.resourceBundle.getString("Movelistframe.titleHint");
    setTitle(oriTitle);

    addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            Lizzie.frame.toggleBadMoves();
          }
        });
    //
    //     final MovelistFrame newContentPane = new MovelistFrame();
    //     newContentPane.setOpaque(true); // content panes must be opaque
    //     setContentPane(newContentPane);
    //     // Display the window.
    //     // jf.setSize(521, 320);

    boolean persisted = Lizzie.config.persistedUi != null;
    if (persisted
        && Lizzie.config.persistedUi.optJSONArray("badmoves-list-position") != null
        && Lizzie.config.persistedUi.optJSONArray("badmoves-list-position").length() >= 5) {
      JSONArray pos = Lizzie.config.persistedUi.getJSONArray("badmoves-list-position");
      if (isMainEngine) setBounds(pos.getInt(1), pos.getInt(2), pos.getInt(3), pos.getInt(4));
      else setBounds(pos.getInt(1) + pos.getInt(3), pos.getInt(2), pos.getInt(3), pos.getInt(4));
      Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
      int width = (int) screensize.getWidth();
      int height = (int) screensize.getHeight();
      if (pos.getInt(0) >= width || pos.getInt(1) >= height) setLocation(0, 0);
    } else {
      if (isMainEngine) setBounds(-9, 0, Lizzie.config.isChinese ? 746 : 880, 487);
      else setBounds(737, 0, Lizzie.config.isChinese ? 746 : 880, 487);
    }
    // setMinimumSize(new Dimension(700, 300));
    try {
      setIconImage(ImageIO.read(MovelistFrame.class.getResourceAsStream("/assets/logo.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    setAlwaysOnTop(Lizzie.config.badmovesalwaysontop || Lizzie.frame.isAlwaysOnTop());
    setTopTitle();
    if (isMainEngine) {
      if (Lizzie.leelaz.isKatago
          || Lizzie.leelaz.isSai
          || Lizzie.board.isContainsKataData()
          || (Lizzie.board.isPkBoard
                  && (Lizzie.engineManager.isEngineGame
                      && (Lizzie.engineManager.engineList.get(
                                  Lizzie.engineManager.engineGameInfo.blackEngineIndex)
                              .isKatago
                          || Lizzie.engineManager.engineList.get(
                                  Lizzie.engineManager.engineGameInfo.whiteEngineIndex)
                              .isKatago))
              || (Lizzie.board.isPkBoardKataB || Lizzie.board.isPkBoardKataW))) isKatago = true;
    } else {
      if (Lizzie.leelaz2 != null && (Lizzie.leelaz2.isKatago || Lizzie.leelaz2.isSai)
          || Lizzie.board.isContainsKataData2()) isKatago = true;
    }
    dataModel = getTableModel();
    table = new JTable(dataModel);

    winrateFont =
        new Font("Microsoft YaHei", Font.PLAIN, Math.max(Lizzie.config.frameFontSize, 14));
    headFont = new Font("Microsoft YaHei", Font.PLAIN, Math.max(Lizzie.config.frameFontSize, 13));

    table.getTableHeader().setFont(headFont);
    table.getTableHeader().setReorderingAllowed(false);
    table.setFont(winrateFont);
    DefaultTableCellRenderer tcr = new ColorTableCellRenderer();
    tcr.setHorizontalAlignment(JLabel.CENTER);
    table.setDefaultRenderer(Object.class, tcr);
    table.setRowHeight(Lizzie.config.menuHeight);
    tablepanel = new JPanel(new BorderLayout());

    // this.add(selectpanel, BorderLayout.SOUTH);

    bottomPanel = new JTabbedPane(JTabbedPane.TOP);
    topPanel = new JTabbedPane(JTabbedPane.TOP);

    DefaultTableCellRenderer r = new ColorTableCellRenderer2();
    r.setHorizontalAlignment(JLabel.CENTER);

    minDataModel1 = getTableModelMin1();
    minDataModel2 = getTableModelMin2();

    minTable1 = new JTable(minDataModel1);
    minTable2 = new JTable(minDataModel2);
    minTable1.setFillsViewportHeight(true);
    minTable2.setFillsViewportHeight(true);

    minTable1.getTableHeader().setFont(headFont);
    minTable1.setFont(winrateFont);
    minTable1.setRowHeight(Lizzie.config.menuHeight);

    minTable2.getTableHeader().setFont(headFont);
    minTable2.setFont(winrateFont);
    minTable2.setRowHeight(Lizzie.config.menuHeight);

    minTable1.setDefaultRenderer(Object.class, r);

    minTable2.setDefaultRenderer(Object.class, r);

    scrollpane = new JScrollPane(table);
    minScrollpane1 = new JScrollPane(minTable1);
    minScrollpane2 = new JScrollPane(minTable2);
    tablePanelMin1 = new JPanel(new BorderLayout());
    tablePanelMin2 = new JPanel(new BorderLayout());
    tablePanelMin1.add(minScrollpane1);
    tablePanelMin2.add(minScrollpane2);
    tablepanel.add(scrollpane);

    minTablePanel = new JPanel(new GridLayout(1, 2));
    minTablePanel.add(tablePanelMin1); // , BorderLayout.WEST, 0);
    minTablePanel.add(tablePanelMin2); // , BorderLayout.EAST, 0);

    topPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.topPanle.simpleList"), minTablePanel);
    topPanel.setFont(new Font("", Font.PLAIN, Config.frameFontSize));
    topPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.topPanle.detailList"), tablepanel);
    topPanel.setSelectedIndex(selectedIndexTop);

    if (selectedIndexTop == 1) sortnum = 3;
    add(topPanel, BorderLayout.CENTER);

    matchPanel =
        new JPanel(true) {
          @Override
          protected void paintComponent(Graphics g) {
            // super.paintComponent(g);
            cachedImage =
                new BufferedImage(matchPanel.getWidth(), matchPanel.getHeight(), TYPE_INT_ARGB);
            Graphics2D g0 = (Graphics2D) cachedImage.getGraphics();
            // g0.setRenderingHint(RenderingHints.KEY_RENDERING,
            // RenderingHints.VALUE_RENDER_QUALITY);
            g0.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            draw(g0, 0, 0, matchPanel.getWidth() - 5, matchPanel.getHeight());
            if (Lizzie.config.isScaled) {
              Graphics2D g1 = (Graphics2D) g;
              g1.setRenderingHint(
                  RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
              g1.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
              g1.drawImage(cachedImage, 0, 0, null);
            } else g.drawImage(cachedImage, 0, 0, null);
            g0.dispose();
          }
        };
    matchPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 4));

    matchPanelmin =
        new JPanel(true) {
          @Override
          protected void paintComponent(Graphics g) {
            //   super.paintComponent(g);
            cachedImageMin =
                new BufferedImage(
                    matchPanelmin.getWidth(), matchPanelmin.getHeight(), TYPE_INT_ARGB);
            Graphics2D g0 = (Graphics2D) cachedImageMin.getGraphics();
            // g0.setRenderingHint(RenderingHints.KEY_RENDERING,
            // RenderingHints.VALUE_RENDER_QUALITY);
            g0.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawMin(g0, 0, 0, matchPanelmin.getWidth(), matchPanelmin.getHeight());
            if (Lizzie.config.isScaled) {
              Graphics2D g1 = (Graphics2D) g;
              g1.setRenderingHint(
                  RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
              g1.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
              g1.drawImage(cachedImageMin, 0, 0, null);
            } else g.drawImage(cachedImageMin, 0, 0, null);
            g0.dispose();
          }
        };
    matchPanelmin.setPreferredSize(new Dimension(getWidth(), 60));

    matchPanelAll = new JPanel();
    matchPanelAll.setLayout(new BorderLayout());

    addComponentListener(
        new ComponentAdapter() {
          public void componentResized(ComponentEvent e) {
            reSetLoc();
          }
        });

    addWindowStateListener(
        new WindowStateListener() {
          public void windowStateChanged(WindowEvent state) {
            if (true) {
              bottomPanel.setPreferredSize(
                  new Dimension(getWidth(), isShowingWinrateGraph ? getHeight() / 4 + 123 : 125));
            }
            matchPanelmin.setPreferredSize(new Dimension(getWidth(), 60));
            resetTop(true);
          }
        });
    // this.add(matchPanelAll, BorderLayout.SOUTH);

    mistakePanelAll = new JPanel();
    matchGraphAll = new JPanel();
    scoreDiffGraphAll = new JPanel();
    matchHistogramAll = new JPanel();
    winrateDiffStatics = new JPanel();
    scoreDiffStatics = new JPanel();
    bigMistakePanel = new JPanel();
    bigScoreMistakePanel = new JPanel();

    bottomPanel.setFont(new Font("", Font.PLAIN, Config.frameFontSize));
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.winrateMatch"), matchPanelAll); // 胜率吻合图
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.stageScore"), matchHistogramAll);
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.aiScore"), matchGraphAll); // AI Score AI评分曲线
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.winrateDifference"), mistakePanelAll);
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.scoreDifference"), scoreDiffGraphAll);
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.winrateStatistics"), winrateDiffStatics);
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.scoreStatistics"), scoreDiffStatics);
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.winrateBigMistake"), bigMistakePanel);
    bottomPanel.addTab(
        Lizzie.resourceBundle.getString("Movelistframe.scoreBigMistake"), bigScoreMistakePanel);

    matchPanelAll.setLayout(new BorderLayout());
    matchGraphAll.setLayout(new BorderLayout());
    mistakePanelAll.setLayout(new BorderLayout());
    scoreDiffGraphAll.setLayout(new BorderLayout());
    matchHistogramAll.setLayout(new BorderLayout());
    winrateDiffStatics.setLayout(new BorderLayout());
    scoreDiffStatics.setLayout(new BorderLayout());
    bigMistakePanel.setLayout(new BorderLayout());
    bigScoreMistakePanel.setLayout(new BorderLayout());

    add(bottomPanel, BorderLayout.SOUTH);
    int curIndex = 0;
    switch (selectedIndex) {
      case 0:
        curIndex = 0;
        matchPanelAll.add(selectpanel, BorderLayout.NORTH);
        matchPanelAll.add(matchPanelmin, BorderLayout.SOUTH);
        matchPanelAll.add(matchPanel, BorderLayout.CENTER);
        break;
      case 1:
        curIndex = 2;
        matchGraphAll.add(selectpanel, BorderLayout.NORTH);
        matchGraphAll.add(matchPanelmin, BorderLayout.SOUTH);
        matchGraphAll.add(matchPanel, BorderLayout.CENTER);
        break;
      case 2:
        curIndex = 3;
        mistakePanelAll.add(selectpanel, BorderLayout.NORTH);
        mistakePanelAll.add(matchPanelmin, BorderLayout.SOUTH);
        mistakePanelAll.add(matchPanel, BorderLayout.CENTER);
        break;
      case 3:
        curIndex = 4;
        scoreDiffGraphAll.add(selectpanel, BorderLayout.NORTH);
        scoreDiffGraphAll.add(matchPanelmin, BorderLayout.SOUTH);
        scoreDiffGraphAll.add(matchPanel, BorderLayout.CENTER);
        break;
      case 4:
        curIndex = 1;
        matchHistogramAll.add(selectpanel, BorderLayout.NORTH);
        matchHistogramAll.add(matchPanelmin, BorderLayout.SOUTH);
        matchHistogramAll.add(matchPanel, BorderLayout.CENTER);
        break;
      case 5:
        curIndex = 5;
        winrateDiffStatics.add(selectpanel, BorderLayout.NORTH);
        winrateDiffStatics.add(matchPanelmin, BorderLayout.SOUTH);
        winrateDiffStatics.add(matchPanel, BorderLayout.CENTER);
        break;
      case 6:
        curIndex = 6;
        scoreDiffStatics.add(selectpanel, BorderLayout.NORTH);
        scoreDiffStatics.add(matchPanelmin, BorderLayout.SOUTH);
        scoreDiffStatics.add(matchPanel, BorderLayout.CENTER);
        break;
      case 7:
        curIndex = 7;
        bigMistakePanel.add(selectpanel, BorderLayout.NORTH);
        // bigMistakePanel.add(matchPanelmin, BorderLayout.SOUTH);
        bigMistakePanel.add(matchPanel, BorderLayout.CENTER);
        break;
      case 8:
        curIndex = 8;
        bigScoreMistakePanel.add(selectpanel, BorderLayout.NORTH);
        // bigMistakePanel.add(matchPanelmin, BorderLayout.SOUTH);
        bigScoreMistakePanel.add(matchPanel, BorderLayout.CENTER);
        break;
    }

    bottomPanel.setSelectedIndex(curIndex);
    matchPanelmin.setLayout(null);
    detail = new JButton("");
    hideMove = new JButton("");
    hideMove.setPreferredSize(new Dimension(20, 20));
    iconUp = new ImageIcon();
    try {
      iconUp.setImage(ImageIO.read(AnalysisFrame.class.getResourceAsStream("/assets/up.png")));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    iconDown = new ImageIcon();
    try {
      iconDown.setImage(ImageIO.read(AnalysisFrame.class.getResourceAsStream("/assets/down.png")));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    iconSettings = new ImageIcon();
    try {
      iconSettings.setImage(
          ImageIO.read(AnalysisFrame.class.getResourceAsStream("/assets/settings.png")));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (isShowingWinrateGraph) detail.setIcon(iconDown);
    else detail.setIcon(iconUp);
    if (Lizzie.config.isShowingMoveList) hideMove.setIcon(iconUp);
    else hideMove.setIcon(iconDown);
    hideMove.setFocusable(false);
    hideMove.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Lizzie.config.isShowingMoveList = !Lizzie.config.isShowingMoveList;
            if (Lizzie.config.isShowingMoveList) hideMove.setIcon(iconUp);
            else hideMove.setIcon(iconDown);
            reSetLoc();
            Lizzie.config.uiConfig.put("show-movelist-matchai", Lizzie.config.isShowingMoveList);
          }
        });
    detail.setFocusable(false);
    detail.setBounds(0, 1, 20, 20);
    detail.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (isShowingWinrateGraph) {
              switch (selectedIndex) {
                case 0:
                  matchPanelAll.remove(matchPanel);
                  break;
                case 1:
                  matchGraphAll.remove(matchPanel);
                  break;
                case 2:
                  mistakePanelAll.remove(matchPanel);
                  break;
                case 3:
                  scoreDiffGraphAll.remove(matchPanel);
                case 4:
                  matchHistogramAll.remove(matchPanel);
                  break;
                case 5:
                  winrateDiffStatics.remove(matchPanel);
                  break;
                case 6:
                  scoreDiffStatics.remove(matchPanel);
                  break;
                case 7:
                  bigMistakePanel.remove(matchPanel);
                  break;
                case 8:
                  bigScoreMistakePanel.remove(matchPanel);
                  break;
              }

              detail.setIcon(iconUp);

            } else {
              switch (selectedIndex) {
                case 0:
                  matchPanelAll.add(matchPanel);
                  break;
                case 1:
                  matchGraphAll.add(matchPanel);
                  break;
                case 2:
                  mistakePanelAll.add(matchPanel);
                  break;
                case 3:
                  scoreDiffGraphAll.add(matchPanel);
                case 4:
                  matchHistogramAll.add(matchPanel);
                  break;
                case 5:
                  winrateDiffStatics.add(matchPanel);
                  break;
                case 6:
                  scoreDiffStatics.add(matchPanel);
                  break;
                case 7:
                  bigMistakePanel.add(matchPanel);
                  break;
                case 8:
                  bigScoreMistakePanel.add(matchPanel);
                  break;
              }
              //  matchPanelAll.add(matchPanel, BorderLayout.CENTER);
              detail.setIcon(iconDown);
            }
            isShowingWinrateGraph = !isShowingWinrateGraph;
            // if (true) {
            bottomPanel.setPreferredSize(
                new Dimension(getWidth(), isShowingWinrateGraph ? getHeight() / 4 + 123 : 125));
            //    }

            validate();
            Lizzie.config.uiConfig.put("show-winrate-matchai", isShowingWinrateGraph);
          }
        });

    //    JLabel lblDetail = new JLabel("吻合图:");
    //    lblDetail.setBounds(5, 0, 45, 20);
    //    matchPanelmin.add(lblDetail);

    settings = new JButton(iconSettings);
    settings.setFocusable(false);
    settings.setBounds(0, 20, 20, 20);
    settings.setToolTipText(Lizzie.resourceBundle.getString("Movelistframe.settingsToolTip"));
    settings.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (setMatchAiPara == null) setMatchAiPara = new SetMatchAiPara();
            setMatchAiPara.setVisible(true);
          }
        });
    lblMatchConfig1 =
        new JLabel(
            Lizzie.resourceBundle.getString("Movelistframe.lblMatchConfig1")); // ("吻合率条件: 前");

    suggestionMoves = new JTextField();
    suggestionMoves.setText(Lizzie.board.matchAiMoves + "");

    suggestionMoves.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            suggestionMoves.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    lblMatchConfig2 =
        new JLabel(
            Lizzie.resourceBundle.getString("Movelistframe.lblMatchConfig2")); // ("选点,且计算量不低于最高值");

    percentPlayouts = new JTextField();
    percentPlayouts.setText(Lizzie.board.matchAiPercentsPlayouts + "");

    percentPlayouts.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            percentPlayouts.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    lblMatchConfig3 = new JLabel("%");
    lblMatchConfig4 =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblMatchConfig4")); // ("手数筛选:");

    firstMove = new JTextField();
    firstMove.setText(Lizzie.board.matchAiFirstMove > 0 ? Lizzie.board.matchAiFirstMove + "" : "");

    firstMove.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            firstMove.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    lblMatchConfig5 =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblMatchConfig5")); // ("至");

    lastMove = new JTextField();
    lastMove.setText(Lizzie.board.matchAiLastMove < 1000 ? Lizzie.board.matchAiLastMove + "" : "");

    lastMove.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            lastMove.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    Document dt4 = lastMove.getDocument();
    dt4.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            boolean error = false;
            int lastMoves = 1000;
            try {
              lastMoves = FORMAT_INT.parse(lastMove.getText()).intValue();
            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              Runnable runnable =
                  new Runnable() {
                    public void run() {
                      lastMove.setText("");
                    }
                  };
              Thread thread = new Thread(runnable);
              thread.start();
            }
            applyChange(
                Lizzie.board.matchAiMoves,
                Lizzie.board.matchAiPercentsPlayouts,
                Lizzie.board.matchAiFirstMove,
                lastMoves);
          }

          public void removeUpdate(DocumentEvent e) {
            boolean error = false;
            int lastMoves = 1000;
            try {
              lastMoves = FORMAT_INT.parse(lastMove.getText()).intValue();
            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              Runnable runnable =
                  new Runnable() {
                    public void run() {
                      lastMove.setText("");
                    }
                  };
              Thread thread = new Thread(runnable);
              thread.start();
            }
            applyChange(
                Lizzie.board.matchAiMoves,
                Lizzie.board.matchAiPercentsPlayouts,
                Lizzie.board.matchAiFirstMove,
                lastMoves);
          }

          public void changedUpdate(DocumentEvent e) {
            boolean error = false;
            int lastMoves = 1000;
            try {
              lastMoves = FORMAT_INT.parse(lastMove.getText()).intValue();
            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              Runnable runnable =
                  new Runnable() {
                    public void run() {
                      lastMove.setText("");
                    }
                  };
              Thread thread = new Thread(runnable);
              thread.start();
            }
            applyChange(
                Lizzie.board.matchAiMoves,
                Lizzie.board.matchAiPercentsPlayouts,
                Lizzie.board.matchAiFirstMove,
                lastMoves);
          }
        });

    Document dt3 = firstMove.getDocument();
    dt3.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            int firstMoves = -1;
            boolean error = false;
            try {
              firstMoves = FORMAT_INT.parse(firstMove.getText()).intValue();
            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              Runnable runnable =
                  new Runnable() {
                    public void run() {
                      firstMove.setText("");
                    }
                  };
              Thread thread = new Thread(runnable);
              thread.start();
            }
            applyChange(
                Lizzie.board.matchAiMoves,
                Lizzie.board.matchAiPercentsPlayouts,
                firstMoves,
                Lizzie.board.matchAiLastMove);
          }

          public void removeUpdate(DocumentEvent e) {
            int firstMoves = -1;
            boolean error = false;
            try {
              firstMoves = FORMAT_INT.parse(firstMove.getText()).intValue();
            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              Runnable runnable =
                  new Runnable() {
                    public void run() {
                      firstMove.setText("");
                    }
                  };
              Thread thread = new Thread(runnable);
              thread.start();
            }
            applyChange(
                Lizzie.board.matchAiMoves,
                Lizzie.board.matchAiPercentsPlayouts,
                firstMoves,
                Lizzie.board.matchAiLastMove);
          }

          public void changedUpdate(DocumentEvent e) {
            int firstMoves = -1;
            boolean error = false;
            try {
              firstMoves = FORMAT_INT.parse(firstMove.getText()).intValue();
            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              Runnable runnable =
                  new Runnable() {
                    public void run() {
                      firstMove.setText("");
                    }
                  };
              Thread thread = new Thread(runnable);
              thread.start();
            }
            applyChange(
                Lizzie.board.matchAiMoves,
                Lizzie.board.matchAiPercentsPlayouts,
                firstMoves,
                Lizzie.board.matchAiLastMove);
          }
        });

    Document dt = suggestionMoves.getDocument();
    dt.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            boolean error = false;
            int moves = Lizzie.board.matchAiMoves;
            try {
              moves = FORMAT_INT.parse(suggestionMoves.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (moves < 1) error = true;
            if (error) {
              suggestionMoves.setBackground(Color.RED);
              return;
            } else {
              suggestionMoves.setBackground(Color.WHITE);
              applyChange(
                  moves,
                  Lizzie.board.matchAiPercentsPlayouts,
                  Lizzie.board.matchAiFirstMove,
                  Lizzie.board.matchAiLastMove);
            }
          }

          public void removeUpdate(DocumentEvent e) {
            boolean error = false;
            int moves = Lizzie.board.matchAiMoves;
            try {
              moves = FORMAT_INT.parse(suggestionMoves.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (moves < 1) error = true;
            if (error) {
              suggestionMoves.setBackground(Color.RED);
              return;
            } else {
              suggestionMoves.setBackground(Color.WHITE);
              applyChange(
                  moves,
                  Lizzie.board.matchAiPercentsPlayouts,
                  Lizzie.board.matchAiFirstMove,
                  Lizzie.board.matchAiLastMove);
            }
          }

          public void changedUpdate(DocumentEvent e) {
            boolean error = false;
            int moves = Lizzie.board.matchAiMoves;
            try {
              moves = FORMAT_INT.parse(suggestionMoves.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (moves < 1) error = true;
            if (error) {
              suggestionMoves.setBackground(Color.RED);
              return;
            } else {
              suggestionMoves.setBackground(Color.WHITE);
              applyChange(
                  moves,
                  Lizzie.board.matchAiPercentsPlayouts,
                  Lizzie.board.matchAiFirstMove,
                  Lizzie.board.matchAiLastMove);
            }
          }
        });

    Document dt2 = percentPlayouts.getDocument();
    dt2.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            boolean error = false;
            double percent = Lizzie.board.matchAiPercentsPlayouts;
            try {
              percent = FORMAT_PERCENT.parse(percentPlayouts.getText()).doubleValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (percent > 100 || percent < 0) error = true;
            if (error) {
              percentPlayouts.setBackground(Color.RED);
              return;
            } else {
              percentPlayouts.setBackground(Color.WHITE);
              applyChange(
                  Lizzie.board.matchAiMoves,
                  percent,
                  Lizzie.board.matchAiFirstMove,
                  Lizzie.board.matchAiLastMove);
            }
          }

          public void removeUpdate(DocumentEvent e) {
            boolean error = false;
            double percent = Lizzie.board.matchAiPercentsPlayouts;
            try {
              percent = FORMAT_PERCENT.parse(percentPlayouts.getText()).doubleValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (percent > 100 || percent < 0) error = true;
            if (error) {
              percentPlayouts.setBackground(Color.RED);
              return;
            } else {
              percentPlayouts.setBackground(Color.WHITE);
              applyChange(
                  Lizzie.board.matchAiMoves,
                  percent,
                  Lizzie.board.matchAiFirstMove,
                  Lizzie.board.matchAiLastMove);
            }
          }

          public void changedUpdate(DocumentEvent e) {
            boolean error = false;
            double percent = Lizzie.board.matchAiPercentsPlayouts;
            if (percent > 100 || percent < 0) error = true;
            try {
              percent = FORMAT_PERCENT.parse(percentPlayouts.getText()).doubleValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              percentPlayouts.setBackground(Color.RED);
              return;
            } else {
              percentPlayouts.setBackground(Color.WHITE);
              applyChange(
                  Lizzie.board.matchAiMoves,
                  percent,
                  Lizzie.board.matchAiFirstMove,
                  Lizzie.board.matchAiLastMove);
            }
          }
        });
    if (Lizzie.config.isChinese) {
      lblMatchConfig1.setBounds(5, 40, 166, 20);
      suggestionMoves.setBounds(84, 43, 25, 16);
      lblMatchConfig2.setBounds(110, 40, 250, 20);
      percentPlayouts.setBounds(258, 43, 40, 16);
      lblMatchConfig3.setBounds(299, 40, 15, 20);
      lblMatchConfig4.setBounds(318, 40, 100, 20);
      firstMove.setBounds(373, 43, 35, 16);
      lblMatchConfig5.setBounds(410, 40, 15, 20);
      lastMove.setBounds(423, 43, 35, 16);
    } else {
      lblMatchConfig1.setBounds(5, 40, 166, 20);
      suggestionMoves.setBounds(123, 43, 25, 16);
      lblMatchConfig2.setBounds(150, 40, 250, 20);
      percentPlayouts.setBounds(373, 43, 40, 16);
      lblMatchConfig3.setBounds(415, 40, 15, 20);
      lblMatchConfig4.setBounds(435, 40, 100, 20);
      firstMove.setBounds(499, 43, 35, 16);
      lblMatchConfig5.setBounds(536, 40, 15, 20);
      lastMove.setBounds(548, 43, 35, 16);
    }

    lblDiffConfig1 =
        new JLabel(
            Lizzie.resourceBundle.getString("Movelistframe.lblDiffConfig1")); // ("胜率波动阈值:  第一阈值:");
    lblDiffConfig2 =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblDiffConfig2")); // ("第二阈值:");
    winrateDiffRange1 = new JTextField();
    winrateDiffRange2 = new JTextField();
    if (Lizzie.config.isChinese) {
      lblDiffConfig1.setBounds(5, 40, 235, 20);
      winrateDiffRange1.setBounds(140, 43, 25, 16);
      lblDiffConfig2.setBounds(170, 40, 155, 20);
      winrateDiffRange2.setBounds(224, 43, 25, 16);
    } else {
      lblDiffConfig1.setBounds(5, 40, 235, 20);
      winrateDiffRange1.setBounds(200, 43, 25, 16);
      lblDiffConfig2.setBounds(230, 40, 155, 20);
      winrateDiffRange2.setBounds(279, 43, 25, 16);
    }

    winrateDiffRange1.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            winrateDiffRange1.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    winrateDiffRange2.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            winrateDiffRange2.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    Document dtRange1 = winrateDiffRange1.getDocument();
    dtRange1.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            boolean error = false;
            int range1 = Lizzie.config.winrateDiffRange1;
            try {
              range1 = FORMAT_INT.parse(winrateDiffRange1.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              winrateDiffRange1.setBackground(Color.RED);
              return;
            } else {
              winrateDiffRange1.setBackground(Color.WHITE);
              if (selectedIndex == 3 || selectedIndex == 6) {
                Lizzie.config.scoreDiffRange1 = range1;
                Lizzie.config.uiConfig.put("score-diff-range1", Lizzie.config.scoreDiffRange1);
              }
              if (selectedIndex == 2 || selectedIndex == 5) {
                Lizzie.config.winrateDiffRange1 = range1;
                Lizzie.config.uiConfig.put("winrate-diff-range1", Lizzie.config.winrateDiffRange1);
              }
            }
          }

          public void removeUpdate(DocumentEvent e) {
            boolean error = false;
            int range1 = Lizzie.config.winrateDiffRange1;
            try {
              range1 = FORMAT_INT.parse(winrateDiffRange1.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              winrateDiffRange1.setBackground(Color.RED);
              return;
            } else {
              winrateDiffRange1.setBackground(Color.WHITE);
              if (selectedIndex == 3 || selectedIndex == 6) {
                Lizzie.config.scoreDiffRange1 = range1;
                Lizzie.config.uiConfig.put("score-diff-range1", Lizzie.config.scoreDiffRange1);
              }
              if (selectedIndex == 2 || selectedIndex == 5) {
                Lizzie.config.winrateDiffRange1 = range1;
                Lizzie.config.uiConfig.put("winrate-diff-range1", Lizzie.config.winrateDiffRange1);
              }
            }
          }

          public void changedUpdate(DocumentEvent e) {
            boolean error = false;
            int range1 = Lizzie.config.winrateDiffRange1;
            try {
              range1 = FORMAT_INT.parse(winrateDiffRange1.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              winrateDiffRange1.setBackground(Color.RED);
              return;
            } else {
              winrateDiffRange1.setBackground(Color.WHITE);
              if (selectedIndex == 3 || selectedIndex == 6) {
                Lizzie.config.scoreDiffRange1 = range1;
                Lizzie.config.uiConfig.put("score-diff-range1", Lizzie.config.scoreDiffRange1);
              }
              if (selectedIndex == 2 || selectedIndex == 5) {
                Lizzie.config.winrateDiffRange1 = range1;
                Lizzie.config.uiConfig.put("winrate-diff-range1", Lizzie.config.winrateDiffRange1);
              }
            }
          }
        });

    Document dtRange2 = winrateDiffRange2.getDocument();
    dtRange2.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            boolean error = false;
            int range2 = Lizzie.config.winrateDiffRange2;
            try {
              range2 = FORMAT_INT.parse(winrateDiffRange2.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              winrateDiffRange2.setBackground(Color.RED);
              return;
            } else {
              winrateDiffRange2.setBackground(Color.WHITE);
              if (selectedIndex == 3 || selectedIndex == 6) {
                Lizzie.config.scoreDiffRange2 = range2;
                Lizzie.config.uiConfig.put("score-diff-range2", Lizzie.config.scoreDiffRange2);
              }
              if (selectedIndex == 2 || selectedIndex == 5) {
                Lizzie.config.winrateDiffRange2 = range2;
                Lizzie.config.uiConfig.put("winrate-diff-range2", Lizzie.config.winrateDiffRange2);
              }
            }
          }

          public void removeUpdate(DocumentEvent e) {
            boolean error = false;
            int range2 = Lizzie.config.winrateDiffRange2;
            try {
              range2 = FORMAT_INT.parse(winrateDiffRange2.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              winrateDiffRange2.setBackground(Color.RED);
              return;
            } else {
              winrateDiffRange2.setBackground(Color.WHITE);
              if (selectedIndex == 3 || selectedIndex == 6) {
                Lizzie.config.scoreDiffRange2 = range2;
                Lizzie.config.uiConfig.put("score-diff-range2", Lizzie.config.scoreDiffRange2);
              }
              if (selectedIndex == 2 || selectedIndex == 5) {
                Lizzie.config.winrateDiffRange2 = range2;
                Lizzie.config.uiConfig.put("winrate-diff-range2", Lizzie.config.winrateDiffRange2);
              }
            }
          }

          public void changedUpdate(DocumentEvent e) {
            boolean error = false;
            int range2 = Lizzie.config.winrateDiffRange2;
            try {
              range2 = FORMAT_INT.parse(winrateDiffRange2.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              winrateDiffRange2.setBackground(Color.RED);
              return;
            } else {
              winrateDiffRange2.setBackground(Color.WHITE);
              if (selectedIndex == 3 || selectedIndex == 6) {
                Lizzie.config.scoreDiffRange2 = range2;
                Lizzie.config.uiConfig.put("score-diff-range2", Lizzie.config.scoreDiffRange2);
              }
              if (selectedIndex == 2 || selectedIndex == 5) {
                Lizzie.config.winrateDiffRange2 = range2;
                Lizzie.config.uiConfig.put("winrate-diff-range2", Lizzie.config.winrateDiffRange2);
              }
            }
          }
        });

    lblParse1Move =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblParse1Move")); // ("布局结束手数:");
    lblParse2Move =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblParse2Move")); // ("中盘结束手数:");
    txtParse1Move = new JTextField();
    txtParse2Move = new JTextField();

    txtParse1Move.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            txtParse1Move.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    txtParse2Move.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            // 获得焦点执行的代码
            txtParse2Move.selectAll();
          }

          @Override
          public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub

          }
        });

    Document dtParse1Move = txtParse1Move.getDocument();
    dtParse1Move.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            boolean error = false;
            int curParse1Move = Lizzie.config.parse1Move;
            try {
              curParse1Move = FORMAT_INT.parse(txtParse1Move.getText()).intValue();
            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              txtParse1Move.setBackground(Color.RED);
              return;
            } else {
              txtParse1Move.setBackground(Color.WHITE);
              Lizzie.config.parse1Move = curParse1Move;
              parse1Move = curParse1Move;
              Lizzie.config.uiConfig.put("parse1-Move", Lizzie.config.parse1Move);
              repaint();
            }
          }

          public void removeUpdate(DocumentEvent e) {
            boolean error = false;
            int curParse1Move = Lizzie.config.parse1Move;
            try {
              curParse1Move = FORMAT_INT.parse(txtParse1Move.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              txtParse1Move.setBackground(Color.RED);
              return;
            } else {
              txtParse1Move.setBackground(Color.WHITE);
              Lizzie.config.parse1Move = curParse1Move;
              parse1Move = curParse1Move;
              Lizzie.config.uiConfig.put("parse1-Move", Lizzie.config.parse1Move);
              repaint();
            }
          }

          public void changedUpdate(DocumentEvent e) {
            boolean error = false;
            int curParse1Move = Lizzie.config.parse1Move;
            try {
              curParse1Move = FORMAT_INT.parse(txtParse1Move.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              txtParse1Move.setBackground(Color.RED);
              return;
            } else {
              txtParse1Move.setBackground(Color.WHITE);
              Lizzie.config.parse1Move = curParse1Move;
              parse1Move = curParse1Move;
              Lizzie.config.uiConfig.put("parse1-Move", Lizzie.config.parse1Move);
              repaint();
            }
          }
        });

    Document dtParse2Move = txtParse2Move.getDocument();
    dtParse2Move.addDocumentListener(
        new DocumentListener() {
          public void insertUpdate(DocumentEvent e) {
            boolean error = false;
            int curParse2Move = Lizzie.config.parse2Move;
            try {
              curParse2Move = FORMAT_INT.parse(txtParse2Move.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              txtParse2Move.setBackground(Color.RED);
              return;
            } else {
              txtParse2Move.setBackground(Color.WHITE);
              Lizzie.config.parse2Move = curParse2Move;
              parse2Move = curParse2Move;
              Lizzie.config.uiConfig.put("parse2-Move", Lizzie.config.parse2Move);
              repaint();
            }
          }

          public void removeUpdate(DocumentEvent e) {
            boolean error = false;
            int curParse2Move = Lizzie.config.parse2Move;
            try {
              curParse2Move = FORMAT_INT.parse(txtParse2Move.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              txtParse2Move.setBackground(Color.RED);
              return;
            } else {
              txtParse2Move.setBackground(Color.WHITE);
              Lizzie.config.parse2Move = curParse2Move;
              parse2Move = curParse2Move;
              Lizzie.config.uiConfig.put("parse2-Move", Lizzie.config.parse2Move);
              repaint();
            }
          }

          public void changedUpdate(DocumentEvent e) {
            boolean error = false;
            int curParse2Move = Lizzie.config.parse2Move;
            try {
              curParse2Move = FORMAT_INT.parse(txtParse2Move.getText()).intValue();

            } catch (ParseException s) {
              // TODO Auto-generated catch block
              error = true;
            }
            if (error) {
              txtParse2Move.setBackground(Color.RED);
              return;
            } else {
              txtParse2Move.setBackground(Color.WHITE);
              Lizzie.config.parse2Move = curParse2Move;
              parse2Move = curParse2Move;
              Lizzie.config.uiConfig.put("parse2-Move", Lizzie.config.parse2Move);
              repaint();
            }
          }
        });

    setPanel();

    ChangeListener changeListener =
        new ChangeListener() {
          public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            switch (index) {
              case 0:
                selectedIndex = 0;
                matchPanelAll.add(selectpanel, BorderLayout.NORTH);
                matchPanelAll.add(matchPanelmin, BorderLayout.SOUTH);
                matchPanelAll.add(matchPanel, BorderLayout.CENTER);
                break;
              case 2:
                selectedIndex = 1;
                matchGraphAll.add(selectpanel, BorderLayout.NORTH);
                matchGraphAll.add(matchPanelmin, BorderLayout.SOUTH);
                matchGraphAll.add(matchPanel, BorderLayout.CENTER);
                break;
              case 1:
                selectedIndex = 4;
                matchHistogramAll.add(selectpanel, BorderLayout.NORTH);
                matchHistogramAll.add(matchPanelmin, BorderLayout.SOUTH);
                matchHistogramAll.add(matchPanel, BorderLayout.CENTER);
                break;
              case 3:
                selectedIndex = 2;
                mistakePanelAll.add(selectpanel, BorderLayout.NORTH);
                mistakePanelAll.add(matchPanelmin, BorderLayout.SOUTH);
                mistakePanelAll.add(matchPanel, BorderLayout.CENTER);
                break;
              case 4:
                selectedIndex = 3;
                scoreDiffGraphAll.add(selectpanel, BorderLayout.NORTH);
                scoreDiffGraphAll.add(matchPanelmin, BorderLayout.SOUTH);
                scoreDiffGraphAll.add(matchPanel, BorderLayout.CENTER);
                break;
              case 5:
                selectedIndex = 5;
                winrateDiffStatics.add(selectpanel, BorderLayout.NORTH);
                winrateDiffStatics.add(matchPanelmin, BorderLayout.SOUTH);
                winrateDiffStatics.add(matchPanel, BorderLayout.CENTER);
                break;
              case 6:
                selectedIndex = 6;
                scoreDiffStatics.add(selectpanel, BorderLayout.NORTH);
                scoreDiffStatics.add(matchPanelmin, BorderLayout.SOUTH);
                scoreDiffStatics.add(matchPanel, BorderLayout.CENTER);
                break;
              case 7:
                selectedIndex = 7;
                bigMistakePanel.add(selectpanel, BorderLayout.NORTH);
                // bigMistakePanel.add(matchPanelmin, BorderLayout.SOUTH);
                bigMistakePanel.add(matchPanel, BorderLayout.CENTER);
                break;
              case 8:
                selectedIndex = 8;
                bigScoreMistakePanel.add(selectpanel, BorderLayout.NORTH);
                bigScoreMistakePanel.add(matchPanel, BorderLayout.CENTER);
                break;
            }
            Lizzie.config.movelistSelectedIndex = selectedIndex;
            Lizzie.config.uiConfig.put(
                "movelist-selected-index", Lizzie.config.movelistSelectedIndex);
            matchPanelmin.removeAll();
            setPanel();
          }
        };
    bottomPanel.addChangeListener(changeListener);

    ChangeListener changeListener2 =
        new ChangeListener() {
          public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            switch (index) {
              case 0:
                if (selectedIndexTop == 1) {
                  sortnum = 2;
                }
                selectedIndexTop = 0;
                break;
              case 1:
                if (selectedIndexTop == 0) {
                  sortnum = 3;
                }
                selectedIndexTop = 1;
                break;
            }
            Lizzie.config.movelistSelectedIndexTop = selectedIndexTop;
            Lizzie.config.uiConfig.put(
                "movelist-selected-indextop", Lizzie.config.movelistSelectedIndexTop);
          }
        };
    topPanel.addChangeListener(changeListener2);

    timer =
        new Timer(
            Lizzie.config.analyzeUpdateIntervalCentisec * 20,
            new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                refreshCount = refreshCount + 1;
                if (refreshCount > 9) {
                  refreshCount = 0;
                  if (!Lizzie.engineManager.isEmpty) {
                    Lizzie.board.updateMovelist(Lizzie.board.getHistory().getCurrentHistoryNode());
                  }
                }
                bottomPanel.repaint();
                minTable1.revalidate();
                minTable1.repaint();
                minTable2.revalidate();
                minTable2.repaint();
                table.revalidate();
                table.repaint();
                //                Lizzie.frame.movelistframe.scrollpane.repaint();
                //                Lizzie.frame.movelistframe.minScrollpane1.repaint();
                //                Lizzie.frame.movelistframe.minScrollpane2.repaint();
              }
            });
    timer.start();

    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setFillsViewportHeight(true);
    table.getColumnModel().getColumn(0).setPreferredWidth(52);
    table.getColumnModel().getColumn(1).setPreferredWidth(50);
    table.getColumnModel().getColumn(2).setPreferredWidth(57);
    table.getColumnModel().getColumn(3).setPreferredWidth(72);
    table.getColumnModel().getColumn(4).setPreferredWidth(77);
    table.getColumnModel().getColumn(5).setPreferredWidth(74);
    table.getColumnModel().getColumn(6).setPreferredWidth(76);
    table.getColumnModel().getColumn(7).setPreferredWidth(71);
    table.getColumnModel().getColumn(8).setPreferredWidth(40);
    if (persisted && Lizzie.config.persistedUi.optJSONArray("badmoves-list-position") != null) {
      JSONArray pos = Lizzie.config.persistedUi.getJSONArray("badmoves-list-position");
      if (table.getColumnCount() == 11
          && Lizzie.config.persistedUi.optJSONArray("badmoves-list-position").length() == 16) {
        table.getColumnModel().getColumn(0).setPreferredWidth(pos.getInt(5));
        table.getColumnModel().getColumn(1).setPreferredWidth(pos.getInt(6));
        table.getColumnModel().getColumn(2).setPreferredWidth(pos.getInt(7));
        table.getColumnModel().getColumn(3).setPreferredWidth(pos.getInt(8));
        table.getColumnModel().getColumn(4).setPreferredWidth(pos.getInt(9));
        table.getColumnModel().getColumn(5).setPreferredWidth(pos.getInt(10));
        table.getColumnModel().getColumn(6).setPreferredWidth(pos.getInt(11));
        table.getColumnModel().getColumn(7).setPreferredWidth(pos.getInt(12));
        table.getColumnModel().getColumn(8).setPreferredWidth(pos.getInt(13));
        table.getColumnModel().getColumn(9).setPreferredWidth(pos.getInt(14));
        table.getColumnModel().getColumn(10).setPreferredWidth(pos.getInt(15));
        //  sortnum = pos.getInt(0);
      } else if (Lizzie.config.persistedUi.optJSONArray("badmoves-list-position").length() >= 14) {
        table.getColumnModel().getColumn(0).setPreferredWidth(pos.getInt(5));
        table.getColumnModel().getColumn(1).setPreferredWidth(pos.getInt(6));
        table.getColumnModel().getColumn(2).setPreferredWidth(pos.getInt(7));
        table.getColumnModel().getColumn(3).setPreferredWidth(pos.getInt(8));
        table.getColumnModel().getColumn(4).setPreferredWidth(pos.getInt(9));
        table.getColumnModel().getColumn(5).setPreferredWidth(pos.getInt(10));
        table.getColumnModel().getColumn(6).setPreferredWidth(pos.getInt(11));
        table.getColumnModel().getColumn(7).setPreferredWidth(pos.getInt(12));
        table.getColumnModel().getColumn(8).setPreferredWidth(pos.getInt(13));
        // sortnum = pos.getInt(0);
      }
    }
    //    if (!isKatago
    //        && sortnum == 4
    //        && Lizzie.config.persistedUi.optJSONArray("badmoves-list-position").length() == 16) {
    //      //sortnum = 3;
    //    }
    //    if (!true) {
    //      if (isKatago) {
    //        hideColumn(table, 10);
    //      } else {
    //        hideColumn(table, 8);
    //      }
    //    }
    // table.setDefaultRenderer(Object.class, new TableStyle());
    ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
        .setHorizontalAlignment(JLabel.CENTER);
    JTableHeader header = table.getTableHeader();
    JTableHeader headerMint1 = minTable1.getTableHeader();
    JTableHeader headerMint2 = minTable2.getTableHeader();
    minTable1.getTableHeader().setReorderingAllowed(false);
    minTable2.getTableHeader().setReorderingAllowed(false);

    TableCellRenderer hr = headerMint1.getDefaultRenderer();
    ((JLabel) hr).setHorizontalAlignment(JLabel.CENTER);
    headerMint1.setDefaultRenderer(hr);
    headerMint2.setDefaultRenderer(hr);

    dropwinratechooser.setValue(Lizzie.config.limitbadmoves);
    dropScoreMeanChooser.setValue(Lizzie.config.limitbadMeanmoves);
    playoutschooser.setValue(Lizzie.config.limitbadplayouts);
    checkBlack.setSelected(true);
    checkWhite.setSelected(true);

    checkBlack.addItemListener(
        new ItemListener() {
          @Override
          public void itemStateChanged(ItemEvent e) {
            repaint();
          }
        });

    checkWhite.addItemListener(
        new ItemListener() {
          @Override
          public void itemStateChanged(ItemEvent e) {
            repaint();
          }
        });

    checkBlackFilter =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.checkBlackFilter")); // ("筛选: 黑");
    checkWhiteFilter =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.checkWhiteFilter")); // ("白");
    JLabel lblDropWinrate =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblDropWinrate")); // ("胜率波动");
    JLabel lblDropScore =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblDropScore")); // ("目差波动");
    JLabel lblPlayouts =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblPlayouts")); // ("前后计算量");
    JLabel lblShowBranch =
        new JLabel(Lizzie.resourceBundle.getString("Movelistframe.lblShowBranch")); // ("分支");
    showBranch = new JComboBox<String>();
    showBranch.addItem(
        Lizzie.resourceBundle.getString("Movelistframe.lblShowBranchItemMain")); // ("主分支");
    showBranch.addItem(
        Lizzie.resourceBundle.getString("Movelistframe.lblShowBranchItemCurrent")); // ("当前分支");
    showBranch.setSelectedIndex(Lizzie.config.moveListSelectedBranch);
    showBranch.setFocusable(false);

    showBranch.addItemListener(
        new ItemListener() {
          public void itemStateChanged(final ItemEvent e) {
            Lizzie.config.moveListSelectedBranch = showBranch.getSelectedIndex();
            Lizzie.config.uiConfig.put(
                "moveList-selected-branch", Lizzie.config.moveListSelectedBranch);
            table.revalidate();
            minTable1.revalidate();
            minTable2.revalidate();
          }
        });

    JCheckBox chkTopCurrentMove =
        new JCheckBox(
            Lizzie.resourceBundle.getString("Movelistframe.chkTopCurrentMove")); // ("置顶当前手");
    chkTopCurrentMove.setFocusable(false);
    chkTopCurrentMove.setSelected(this.topCurNode);
    chkTopCurrentMove.setMargin(new Insets(3, 2, 3, 2));
    chkTopCurrentMove.addItemListener(
        new ItemListener() {
          @Override
          public void itemStateChanged(ItemEvent e) {
            topCurNode = chkTopCurrentMove.isSelected();
            Lizzie.config.moveListTopCurNode = chkTopCurrentMove.isSelected();
            Lizzie.config.uiConfig.put("movelist-top-curnode", Lizzie.config.moveListTopCurNode);
            tablepanel.repaint();
          }
        });

    showGraph =
        new JButton(Lizzie.resourceBundle.getString("Movelistframe.showGraph")); // ("显示图表");
    showGraph.setMargin(new Insets(3, 2, 3, 2));
    if (this.showGra) {
      showGraph.setText(Lizzie.resourceBundle.getString("Movelistframe.hideGraph")); // ("关闭图表");
    } else {
      remove(bottomPanel);
      remove(selectpanel);
      add(selectpanel, BorderLayout.SOUTH);
      validate();
    }
    showGraph.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (showGra) {
              remove(bottomPanel);
              remove(selectpanel);
              add(selectpanel, BorderLayout.SOUTH);
              validate();
            } else {
              remove(selectpanel);
              remove(bottomPanel);
              matchPanelAll.add(selectpanel, BorderLayout.NORTH);
              matchGraphAll.add(selectpanel, BorderLayout.NORTH);
              mistakePanelAll.add(selectpanel, BorderLayout.NORTH);
              scoreDiffGraphAll.add(selectpanel, BorderLayout.NORTH);
              matchHistogramAll.add(selectpanel, BorderLayout.NORTH);
              add(bottomPanel, BorderLayout.SOUTH);
              switch (selectedIndex) {
                case 0:
                  selectedIndex = 0;
                  matchPanelAll.removeAll();
                  matchPanelAll.add(selectpanel, BorderLayout.NORTH);
                  matchPanelAll.add(matchPanelmin, BorderLayout.SOUTH);
                  matchPanelAll.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 1:
                  selectedIndex = 1;
                  matchGraphAll.removeAll();
                  matchGraphAll.add(selectpanel, BorderLayout.NORTH);
                  matchGraphAll.add(matchPanelmin, BorderLayout.SOUTH);
                  matchGraphAll.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 2:
                  selectedIndex = 2;
                  mistakePanelAll.removeAll();
                  mistakePanelAll.add(selectpanel, BorderLayout.NORTH);
                  mistakePanelAll.add(matchPanelmin, BorderLayout.SOUTH);
                  mistakePanelAll.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 3:
                  selectedIndex = 3;
                  scoreDiffGraphAll.removeAll();
                  scoreDiffGraphAll.add(selectpanel, BorderLayout.NORTH);
                  scoreDiffGraphAll.add(matchPanelmin, BorderLayout.SOUTH);
                  scoreDiffGraphAll.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 4:
                  selectedIndex = 4;
                  matchHistogramAll.removeAll();
                  matchHistogramAll.add(selectpanel, BorderLayout.NORTH);
                  matchHistogramAll.add(matchPanelmin, BorderLayout.SOUTH);
                  matchHistogramAll.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 5:
                  selectedIndex = 5;
                  winrateDiffStatics.removeAll();
                  winrateDiffStatics.add(selectpanel, BorderLayout.NORTH);
                  winrateDiffStatics.add(matchPanelmin, BorderLayout.SOUTH);
                  winrateDiffStatics.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 6:
                  selectedIndex = 6;
                  scoreDiffStatics.removeAll();
                  scoreDiffStatics.add(selectpanel, BorderLayout.NORTH);
                  scoreDiffStatics.add(matchPanelmin, BorderLayout.SOUTH);
                  scoreDiffStatics.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 7:
                  selectedIndex = 7;
                  bigMistakePanel.removeAll();
                  bigMistakePanel.add(selectpanel, BorderLayout.NORTH);
                  // bigMistakePanel.add(matchPanelmin, BorderLayout.SOUTH);
                  bigMistakePanel.add(matchPanel, BorderLayout.CENTER);
                  break;
                case 8:
                  selectedIndex = 8;
                  bigScoreMistakePanel.removeAll();
                  bigScoreMistakePanel.add(selectpanel, BorderLayout.NORTH);
                  bigScoreMistakePanel.add(matchPanel, BorderLayout.CENTER);
                  break;
              }
              repaint();
              validate();
            }
            showGra = !showGra;
            if (showGra)
              showGraph.setText(
                  Lizzie.resourceBundle.getString("Movelistframe.hideGraph")); // ("关闭图表");
            else
              showGraph.setText(
                  Lizzie.resourceBundle.getString("Movelistframe.showGraph")); // ("显示图表");

            Lizzie.config.showMoveListGraph = showGra;
            Lizzie.config.uiConfig.put("show-movelist-graph", Lizzie.config.showMoveListGraph);
          }
        });
    selectpanel.add(hideMove);
    selectpanel.add(checkBlackFilter);
    selectpanel.add(checkBlack);
    selectpanel.add(checkWhiteFilter);
    selectpanel.add(checkWhite);
    selectpanel.add(lblShowBranch);
    selectpanel.add(showBranch);
    selectpanel.add(lblDropWinrate);
    selectpanel.add(dropwinratechooser);
    selectpanel.add(lblDropScore);
    selectpanel.add(dropScoreMeanChooser);
    selectpanel.add(lblPlayouts);
    selectpanel.add(playoutschooser);
    if (true) selectpanel.add(showGraph);
    selectpanel.add(chkTopCurrentMove);
    playoutschooser.setPreferredSize(new Dimension(55, 23));
    dropwinratechooser.setPreferredSize(new Dimension(35, 23));
    dropScoreMeanChooser.setPreferredSize(new Dimension(35, 23));

    if (Lizzie.board.isPkBoard) {
      if (isKatago)
        table
            .getColumnModel()
            .getColumn(6)
            .setHeaderValue(
                Lizzie.resourceBundle.getString(
                    "Movelistframe.columnHead.PreivousWinRate")); // ("前一手胜率");
      else
        table
            .getColumnModel()
            .getColumn(5)
            .setHeaderValue(
                Lizzie.resourceBundle.getString(
                    "Movelistframe.columnHead.PreivousWinRate")); // ("前一手胜率");
      //  checkBlacktxt.setText("白:");
      //   checkWhitetxt.setText("黑:");
    }
    // else {
    // if (isKatago) {table.getColumnModel().getColumn(6).setHeaderValue("AI胜率");
    //  table.getColumnModel().getColumn(5).setHeaderValue("此手胜率");
    // }
    //  else table.getColumnModel().getColumn(5).setHeaderValue("AI胜率");
    //   checkBlacktxt.setText("黑:");
    // checkWhitetxt.setText("白:");
    // }

    JFontPopupMenu exportMin1 = new JFontPopupMenu();
    final JMenuItem exportMenu =
        new JFontMenuItem(Lizzie.resourceBundle.getString("JTabel.export"));
    exportMin1.add(exportMenu);
    exportMenu.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              JFileChooser chooser = new JFileChooser();
              FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.xls)", "xls");
              chooser.setFileFilter(filter);
              int option = chooser.showSaveDialog(Lizzie.frame);
              if (option == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String fname = chooser.getName(file);
                if (fname.indexOf(".xlsx") == -1) {
                  Utils.exportTable(
                      minTable1, chooser.getCurrentDirectory() + Utils.pwd + fname + ".xls");
                }
              }

            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
          }
        });

    JFontPopupMenu exportMin2 = new JFontPopupMenu();
    final JMenuItem exportMenu2 =
        new JFontMenuItem(Lizzie.resourceBundle.getString("JTabel.export"));
    exportMin2.add(exportMenu2);
    exportMenu2.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              JFileChooser chooser = new JFileChooser();
              FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.xls)", "xls");
              chooser.setFileFilter(filter);
              int option = chooser.showSaveDialog(Lizzie.frame);
              if (option == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String fname = chooser.getName(file);
                if (fname.indexOf(".xlsx") == -1) {
                  Utils.exportTable(
                      minTable2, chooser.getCurrentDirectory() + Utils.pwd + fname + ".xls");
                }
              }

            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
          }
        });

    JFontPopupMenu exportFull = new JFontPopupMenu();
    final JMenuItem exportMenuFull =
        new JFontMenuItem(Lizzie.resourceBundle.getString("JTabel.export"));
    exportFull.add(exportMenuFull);
    exportMenuFull.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              JFileChooser chooser = new JFileChooser();
              FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.xls)", "xls");
              chooser.setFileFilter(filter);
              int option = chooser.showSaveDialog(Lizzie.frame);
              if (option == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String fname = chooser.getName(file);
                if (fname.indexOf(".xlsx") == -1) {
                  Utils.exportTable(
                      table, chooser.getCurrentDirectory() + Utils.pwd + fname + ".xls");
                }
              }

            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
          }
        });

    playoutschooser.addChangeListener(
        new ChangeListener() {

          public void stateChanged(ChangeEvent e) {
            Lizzie.config.limitbadplayouts = (int) playoutschooser.getValue();
            Lizzie.config.uiConfig.putOpt("badmoves-playouts-limits", playoutschooser.getValue());
          }
        });

    dropwinratechooser.addChangeListener(
        new ChangeListener() {

          public void stateChanged(ChangeEvent e) {
            Lizzie.config.limitbadmoves = (int) dropwinratechooser.getValue();
            Lizzie.config.uiConfig.putOpt("badmoves-winrate-limits", dropwinratechooser.getValue());
          }
        });
    dropScoreMeanChooser.addChangeListener(
        new ChangeListener() {

          public void stateChanged(ChangeEvent e) {
            Lizzie.config.limitbadMeanmoves = (int) dropScoreMeanChooser.getValue();
            Lizzie.config.uiConfig.putOpt(
                "badmoves-scoremean-limits", dropScoreMeanChooser.getValue());
          }
        });
    table.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
              exportFull.show(table, e.getX(), e.getY());
              return;
            }
            int row = table.rowAtPoint(e.getPoint());
            int col = table.columnAtPoint(e.getPoint());
            if (row >= 0 && col >= 0) {
              try {
                handleTableDoubleClick(row, col);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
          }
        });
    bottomPanel.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Y) {
              Lizzie.frame.toggleBadMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_U) {
              Lizzie.frame.toggleBestMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {

              Lizzie.frame.togglePonderMannul();
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
              togglealwaysontop();
            }
          }
        });

    table.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Y) {
              Lizzie.frame.toggleBadMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_U) {
              Lizzie.frame.toggleBestMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
              Lizzie.frame.togglePonderMannul();
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
              togglealwaysontop();
            }
          }
        });

    minTable1.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
              exportMin1.show(minTable1, e.getX(), e.getY());
              return;
            }
            int row = minTable1.rowAtPoint(e.getPoint());
            int col = minTable1.columnAtPoint(e.getPoint());
            if (row >= 0 && col >= 0) {
              try {
                handleTableDoubleClickMin1(row, col);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
          }
        });
    minTable1.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Y) {
              Lizzie.frame.toggleBadMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_U) {
              Lizzie.frame.toggleBestMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
              Lizzie.frame.togglePonderMannul();
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
              togglealwaysontop();
            }
          }
        });

    minTable2.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
              exportMin2.show(minTable2, e.getX(), e.getY());
              return;
            }
            int row = minTable2.rowAtPoint(e.getPoint());
            int col = minTable2.columnAtPoint(e.getPoint());
            if (row >= 0 && col >= 0) {
              try {
                handleTableDoubleClickMin2(row, col);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
          }
        });
    minTable2.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Y) {
              Lizzie.frame.toggleBadMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_U) {
              Lizzie.frame.toggleBestMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
              Lizzie.frame.togglePonderMannul();
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
              togglealwaysontop();
            }
          }
        });

    this.setFocusable(true);
    this.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Y) {
              Lizzie.frame.toggleBadMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_O) {
              Lizzie.frame.openFileAll();
            }
            if (e.getKeyCode() == KeyEvent.VK_U) {
              Lizzie.frame.toggleBestMoves();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
              if (Lizzie.frame.isPlayingAgainstLeelaz) {
                Lizzie.frame.isPlayingAgainstLeelaz = false;
                Lizzie.leelaz.isThinking = false;
              }
              Lizzie.leelaz.togglePonder();
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) {
              togglealwaysontop();
            }
          }
        });
    matchPanelmin.addMouseListener(
        new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            setTxtUnfocuse();
          }
        });
    matchPanel.addMouseListener(
        new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (selectedIndex >= 7) {
              int index = getMouseOverBigMistakeIndex(e.getX());
              if (BigMistakeList.size() > index) {
                Lizzie.board.goToMoveNumberBeyondBranch(BigMistakeList.get(index).moveNumber - 1);
                Lizzie.frame.clickbadmove = BigMistakeList.get(index).coords;
                // mouseOverBigMistakeIndex = -1;
              }
            } else {
              if (Lizzie.board.isPkBoard || selectedIndex >= 4) return;
              int moveNumber = moveNumber(e.getX(), e.getY());
              Lizzie.board.goToMoveNumberBeyondBranch(moveNumber);
            }
            repaint();
          }
        });

    matchPanel.addMouseListener(
        new MouseAdapter() {
          public void mouseExited(MouseEvent e) {
            if (curMouseOverNode != null) {
              curMouseOverNode = null;
              repaint();
            }
            mouseOverBigMistakeIndex = -1;
          }
        });

    matchPanel.addMouseMotionListener(
        new MouseAdapter() {
          @Override
          public void mouseMoved(MouseEvent e) {
            if (selectedIndex >= 7) {
              mouseOverBigMistakeIndex = getMouseOverBigMistakeIndex(e.getX());
            }
            if (selectedIndex == 0) {
              int moveNumber = moveNumber(e.getX(), e.getY());
              boolean noRefresh = false;
              if (moveNumber >= 0) {
                BoardHistoryNode curNode = Lizzie.board.getHistory().getCurrentHistoryNode();
                BoardHistoryNode mouseOverNode = curNode;
                int curMoveNumber = mouseOverNode.getData().moveNumber;
                if (curMoveNumber > moveNumber) {
                  for (int i = 0; i < curMoveNumber - moveNumber; i++) {
                    if (mouseOverNode.previous().isPresent())
                      mouseOverNode = mouseOverNode.previous().get();
                    else {
                      noRefresh = true;
                      break;
                    }
                  }
                } else if (curMoveNumber < moveNumber) {
                  for (int i = 0; i < moveNumber - curMoveNumber; i++) {
                    if (mouseOverNode.next().isPresent())
                      mouseOverNode = mouseOverNode.next().get();
                    else {
                      noRefresh = true;
                      break;
                    }
                  }
                }
                curMouseOverNode = mouseOverNode;
                if (!(curMouseOverNode == curNode && noRefresh)) repaint();
              }
            }
          }
        });

    matchPanel.addMouseMotionListener(
        new MouseAdapter() {
          public void mouseDragged(MouseEvent e) {
            if (Lizzie.board.isPkBoard || selectedIndex >= 4) return;
            int moveNumber = moveNumber(e.getX(), e.getY());
            Lizzie.board.goToMoveNumberBeyondBranch(moveNumber);
            repaint();
          }
        });

    matchPanel.addMouseWheelListener(
        new MouseAdapter() {
          public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() > 0) {
              Lizzie.board.nextMove(true);
              repaint();
            } else if (e.getWheelRotation() < 0) {
              Lizzie.board.previousMove(true);
              repaint();
            }
          }
        });

    header.addMouseListener(
        new MouseAdapter() {
          public void mouseReleased(MouseEvent e) {
            int pick = header.columnAtPoint(e.getPoint());
            sortnum = pick;
            if (sortnum == 3 || (isKatago && sortnum == 4)) {
              if (isOriginOrder) {
                isOriginOrder = false;
                issorted = false;
              } else if (!issorted) issorted = true;
              else {
                isOriginOrder = true;
                issorted = false;
              }
            } else {
              issorted = !issorted;
            }
            table.repaint();
          }
        });
    headerMint1.addMouseListener(
        new MouseAdapter() {
          public void mouseReleased(MouseEvent e) {
            int pick = headerMint1.columnAtPoint(e.getPoint());
            sortnum = pick;
            if (sortnum == 2 || (isKatago && sortnum == 3)) {
              if (isOriginOrder) {
                isOriginOrder = false;
                issorted = false;
              } else if (!issorted) issorted = true;
              else {
                isOriginOrder = true;
                issorted = false;
              }
            } else {
              issorted = !issorted;
            }
            minTable1.repaint();
          }
        });
    headerMint2.addMouseListener(
        new MouseAdapter() {
          public void mouseReleased(MouseEvent e) {
            int pick = headerMint2.columnAtPoint(e.getPoint());
            sortnum = pick;
            if (sortnum == 2 || (isKatago && sortnum == 3)) {
              if (isOriginOrder) {
                isOriginOrder = false;
                issorted = false;
              } else if (!issorted) issorted = true;
              else {
                isOriginOrder = true;
                issorted = false;
              }
            } else {
              issorted = !issorted;
            }
            minTable2.repaint();
          }
        });
  }

  protected void reSetLoc() {
    // TODO Auto-generated method stub
    if (Lizzie.config.isShowingMoveList) {
      topPanel.setVisible(true);
      bottomPanel.setPreferredSize(
          new Dimension(getWidth(), isShowingWinrateGraph ? getHeight() / 4 + 123 : 125));
    } else {
      bottomPanel.setPreferredSize(new Dimension(getWidth(), getHeight() - 10));
      topPanel.setVisible(false);
    }
    matchPanelmin.setPreferredSize(new Dimension(getWidth(), 60));
    resetTop(false);
  }

  private void hideColumn(JTable table, int index) {

    TableColumn tc = table.getColumnModel().getColumn(index);
    tc.setMaxWidth(0);
    tc.setPreferredWidth(0);
    tc.setMinWidth(0);
    tc.setWidth(0);

    table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
    table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
  }

  private double convertcoreMean(double coreMean) {

    if (coreMean > maxcoreMean) return maxcoreMean;
    if (coreMean < 0 && Math.abs(coreMean) > maxcoreMean) return -maxcoreMean;
    return coreMean;
  }

  private double convertWinrate(double winrate) {
    return winrate;
  }

  private void resetTop(boolean isStatChanged) {
    if (isStatChanged) {
      minScrollpane1.setPreferredSize(new Dimension(getWidth() / 2 - 5, topPanel.getHeight()));
      minScrollpane2.setPreferredSize(new Dimension(getWidth() / 2, topPanel.getHeight()));
    } else {
      minScrollpane1.setPreferredSize(
          new Dimension(topPanel.getWidth() / 2 - 5, topPanel.getHeight()));
      minScrollpane2.setPreferredSize(new Dimension(topPanel.getWidth() / 2, topPanel.getHeight()));
    }
  }

  public void drawMin(Graphics2D g, int posx, int posy, int width, int height) {
    int blackMatch = 0;
    int blackMoves = 0;
    int whiteMatch = 0;
    int whiteMoves = 0;
    int all = 0;
    int analyzed = 0;
    // double blackValue = 0;
    // double whiteValue = 0;
    double blackTrueValue = 0;
    double whiteTrueValue = 0;
    int analyzedBlack = 0;
    int analyzedWhite = 0;

    double winratediffBlack = 0;
    double winratediffWhite = 0;
    double winratediffVarianceBlack = 0;
    double winratediffVarianceWhite = 0;

    int range1Black = 0;
    int range2Black = 0;
    int range1White = 0;
    int range2White = 0;

    double winratediffBlack1 = 0;
    double winratediffWhite1 = 0;
    double winratediffVarianceBlack1 = 0;
    double winratediffVarianceWhite1 = 0;

    double winratediffBlack2 = 0;
    double winratediffWhite2 = 0;
    double winratediffVarianceBlack2 = 0;
    double winratediffVarianceWhite2 = 0;

    int analyzedBlackS = 0;
    int analyzedWhiteS = 0;

    double scorediffBlack = 0;
    double scorediffWhite = 0;
    double scorediffVarianceBlack = 0;
    double scorediffVarianceWhite = 0;

    int range1BlackS = 0;
    int range2BlackS = 0;
    int range1WhiteS = 0;
    int range2WhiteS = 0;

    double scorediffBlack1 = 0;
    double scorediffWhite1 = 0;
    double scorediffVarianceBlack1 = 0;
    double scorediffVarianceWhite1 = 0;

    double scorediffBlack2 = 0;
    double scorediffWhite2 = 0;
    double scorediffVarianceBlack2 = 0;
    double scorediffVarianceWhite2 = 0;

    if (selectedIndex == 4) {
      parse1BlackValue = 0;
      parse1BlackAnalyzed = 0;
      parse1BlackWinrateDiff = 0;
      parse1BlackScoreDiff = 0;

      parse2BlackValue = 0;
      parse2BlackAnalyzed = 0;
      parse2BlackWinrateDiff = 0;
      parse2BlackScoreDiff = 0;

      parse3BlackValue = 0;
      parse3BlackAnalyzed = 0;
      parse3BlackWinrateDiff = 0;
      parse3BlackScoreDiff = 0;

      parse1WhiteValue = 0;
      parse1WhiteAnalyzed = 0;
      parse1WhiteWinrateDiff = 0;
      parse1WhiteScoreDiff = 0;

      parse2WhiteValue = 0;
      parse2WhiteAnalyzed = 0;
      parse2WhiteWinrateDiff = 0;
      parse2WhiteScoreDiff = 0;

      parse3WhiteValue = 0;
      parse3WhiteAnalyzed = 0;
      parse3WhiteWinrateDiff = 0;
      parse3WhiteScoreDiff = 0;
    }
    if (selectedIndex == 5) {
      parse1BlackWinrateMiss1 = 0;
      parse1BlackWinrateMiss2 = 0;
      parse1BlackWinrateMiss3 = 0;
      parse2BlackWinrateMiss1 = 0;
      parse2BlackWinrateMiss2 = 0;
      parse2BlackWinrateMiss3 = 0;
      parse3BlackWinrateMiss1 = 0;
      parse3BlackWinrateMiss2 = 0;
      parse3BlackWinrateMiss3 = 0;

      parse1WhiteWinrateMiss1 = 0;
      parse1WhiteWinrateMiss2 = 0;
      parse1WhiteWinrateMiss3 = 0;
      parse2WhiteWinrateMiss1 = 0;
      parse2WhiteWinrateMiss2 = 0;
      parse2WhiteWinrateMiss3 = 0;
      parse3WhiteWinrateMiss1 = 0;
      parse3WhiteWinrateMiss2 = 0;
      parse3WhiteWinrateMiss3 = 0;
    }

    if (selectedIndex == 6) {
      parse1BlackScoreMiss1 = 0;
      parse1BlackScoreMiss2 = 0;
      parse1BlackScoreMiss3 = 0;
      parse2BlackScoreMiss1 = 0;
      parse2BlackScoreMiss2 = 0;
      parse2BlackScoreMiss3 = 0;
      parse3BlackScoreMiss1 = 0;
      parse3BlackScoreMiss2 = 0;
      parse3BlackScoreMiss3 = 0;

      parse1WhiteScoreMiss1 = 0;
      parse1WhiteScoreMiss2 = 0;
      parse1WhiteScoreMiss3 = 0;
      parse2WhiteScoreMiss1 = 0;
      parse2WhiteScoreMiss2 = 0;
      parse2WhiteScoreMiss3 = 0;
      parse3WhiteScoreMiss1 = 0;
      parse3WhiteScoreMiss2 = 0;
      parse3WhiteScoreMiss3 = 0;
    }

    BoardHistoryNode node = Lizzie.board.getHistory().getCurrentHistoryNode();
    if (showBranch.getSelectedIndex() == 0 && !node.isMainTrunk()) {
      node = Lizzie.board.getHistory().getMainEnd();
    }
    while (node.next().isPresent()) {
      node = node.next().get();
    }
    all = node.getData().moveNumber;
    // selectedIndex == 4
    while (node.previous().isPresent()) {
      node = node.previous().get();
      NodeInfo nodeInfo =
          showBranch.getSelectedIndex() == 0
              ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
              : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
      if (selectedIndex > 3
          || (node.getData().moveNumber <= Lizzie.board.matchAiLastMove
              && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove)) {
        if (selectedIndex == 2) {
          if (nodeInfo.analyzed) {
            if (nodeInfo.isBlack) {
              winratediffBlack = winratediffBlack + Math.abs(nodeInfo.diffWinrate);
              analyzedBlack = analyzedBlack + 1;
              winratediffVarianceBlack =
                  winratediffVarianceBlack + Math.pow(nodeInfo.diffWinrate, 2);
              if (Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                range1Black = range1Black + 1;
                winratediffBlack1 = winratediffBlack1 + Math.abs(nodeInfo.diffWinrate);
                winratediffVarianceBlack1 =
                    winratediffVarianceBlack1 + Math.pow(nodeInfo.diffWinrate, 2);
              }
              if (Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange2) {
                range2Black = range2Black + 1;
                winratediffBlack2 = winratediffBlack2 + Math.abs(nodeInfo.diffWinrate);
                winratediffVarianceBlack2 =
                    winratediffVarianceBlack2 + Math.pow(nodeInfo.diffWinrate, 2);
              }
            } else {
              winratediffWhite = winratediffWhite + Math.abs(nodeInfo.diffWinrate);
              analyzedWhite = analyzedWhite + 1;
              winratediffVarianceWhite =
                  winratediffVarianceWhite + Math.pow(nodeInfo.diffWinrate, 2);
              if (Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                range1White = range1White + 1;
                winratediffWhite1 = winratediffWhite1 + Math.abs(nodeInfo.diffWinrate);
                winratediffVarianceWhite1 =
                    winratediffVarianceWhite1 + Math.pow(nodeInfo.diffWinrate, 2);
              }
              if (Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange2) {
                range2White = range2White + 1;
                winratediffWhite2 = winratediffWhite2 + Math.abs(nodeInfo.diffWinrate);
                winratediffVarianceWhite2 =
                    winratediffVarianceWhite2 + Math.pow(nodeInfo.diffWinrate, 2);
              }
            }
          }
        } else if (selectedIndex == 3) {
          if (nodeInfo.analyzed) {
            if (nodeInfo.isBlack) {
              scorediffBlack = scorediffBlack + Math.abs(nodeInfo.scoreMeanDiff);
              analyzedBlackS = analyzedBlackS + 1;
              scorediffVarianceBlack = scorediffVarianceBlack + Math.pow(nodeInfo.scoreMeanDiff, 2);
              if (Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                range1BlackS = range1BlackS + 1;
                scorediffBlack1 = scorediffBlack1 + Math.abs(nodeInfo.scoreMeanDiff);
                scorediffVarianceBlack1 =
                    scorediffVarianceBlack1 + Math.pow(nodeInfo.scoreMeanDiff, 2);
              }
              if (Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange2) {
                range2BlackS = range2BlackS + 1;
                scorediffBlack2 = scorediffBlack2 + Math.abs(nodeInfo.scoreMeanDiff);
                scorediffVarianceBlack2 =
                    scorediffVarianceBlack2 + Math.pow(nodeInfo.scoreMeanDiff, 2);
              }
            } else {
              scorediffWhite = scorediffWhite + Math.abs(nodeInfo.scoreMeanDiff);
              analyzedWhiteS = analyzedWhiteS + 1;
              scorediffVarianceWhite = scorediffVarianceWhite + Math.pow(nodeInfo.scoreMeanDiff, 2);
              if (Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                range1WhiteS = range1WhiteS + 1;
                scorediffWhite1 = scorediffWhite1 + Math.abs(nodeInfo.scoreMeanDiff);
                scorediffVarianceWhite1 =
                    scorediffVarianceWhite1 + Math.pow(nodeInfo.scoreMeanDiff, 2);
              }
              if (Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange2) {
                range2WhiteS = range2WhiteS + 1;
                scorediffWhite2 = scorediffWhite2 + Math.abs(nodeInfo.scoreMeanDiff);
                scorediffVarianceWhite2 =
                    scorediffVarianceWhite2 + Math.pow(nodeInfo.scoreMeanDiff, 2);
              }
            }
          }
        } else if (selectedIndex == 4) {
          if (nodeInfo.analyzed) {
            if (nodeInfo.isBlack) {
              if (nodeInfo.moveNum <= parse1Move) {
                parse1BlackAnalyzed++;
                parse1BlackValue += nodeInfo.percentsMatch;
                // Math.pow(nodeInfo.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature);
                parse1BlackWinrateDiff += Math.abs(nodeInfo.diffWinrate);
                parse1BlackScoreDiff += Math.abs(nodeInfo.scoreMeanDiff);
              } else if (nodeInfo.moveNum <= parse2Move) {
                parse2BlackAnalyzed++;
                parse2BlackValue += nodeInfo.percentsMatch;
                //   Math.pow(nodeInfo.percentsMatch, (double) 1 /
                // Lizzie.config.matchAiTemperature);
                parse2BlackWinrateDiff += Math.abs(nodeInfo.diffWinrate);
                parse2BlackScoreDiff += Math.abs(nodeInfo.scoreMeanDiff);
              } else {
                parse3BlackAnalyzed++;
                parse3BlackValue += nodeInfo.percentsMatch;
                //  Math.pow(nodeInfo.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature);
                parse3BlackWinrateDiff += Math.abs(nodeInfo.diffWinrate);
                parse3BlackScoreDiff += Math.abs(nodeInfo.scoreMeanDiff);
              }
            } else {
              if (nodeInfo.moveNum <= parse1Move) {
                parse1WhiteAnalyzed++;
                parse1WhiteValue += nodeInfo.percentsMatch;
                //  Math.pow(nodeInfo.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature);
                parse1WhiteWinrateDiff += Math.abs(nodeInfo.diffWinrate);
                parse1WhiteScoreDiff += Math.abs(nodeInfo.scoreMeanDiff);
              } else if (nodeInfo.moveNum <= parse2Move) {
                parse2WhiteAnalyzed++;
                parse2WhiteValue += nodeInfo.percentsMatch;
                //  Math.pow(nodeInfo.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature);
                parse2WhiteWinrateDiff += Math.abs(nodeInfo.diffWinrate);
                parse2WhiteScoreDiff += Math.abs(nodeInfo.scoreMeanDiff);
              } else {
                parse3WhiteAnalyzed++;
                parse3WhiteValue += nodeInfo.percentsMatch;
                //  Math.pow(nodeInfo.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature);
                parse3WhiteWinrateDiff += Math.abs(nodeInfo.diffWinrate);
                parse3WhiteScoreDiff += Math.abs(nodeInfo.scoreMeanDiff);
              }
            }
          }
        } else if (selectedIndex == 5) {
          // nodeInfo

          if (nodeInfo.analyzed) {
            if (nodeInfo.isBlack) {
              if (nodeInfo.moveNum <= parse1Move) {
                if (Math.abs(nodeInfo.diffWinrate) < Lizzie.config.winrateDiffRange1) {
                  parse1BlackWinrateMiss1++;
                }
                if (Math.abs(nodeInfo.diffWinrate) <= Lizzie.config.winrateDiffRange2
                    && Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                  parse1BlackWinrateMiss2++;
                }
                if (Math.abs(nodeInfo.diffWinrate) > Lizzie.config.winrateDiffRange2) {
                  parse1BlackWinrateMiss3++;
                }
              } else if (nodeInfo.moveNum <= parse2Move) {
                if (Math.abs(nodeInfo.diffWinrate) < Lizzie.config.winrateDiffRange1) {
                  parse2BlackWinrateMiss1++;
                }
                if (Math.abs(nodeInfo.diffWinrate) <= Lizzie.config.winrateDiffRange2
                    && Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                  parse2BlackWinrateMiss2++;
                }
                if (Math.abs(nodeInfo.diffWinrate) > Lizzie.config.winrateDiffRange2) {
                  parse2BlackWinrateMiss3++;
                }
              } else {
                if (Math.abs(nodeInfo.diffWinrate) < Lizzie.config.winrateDiffRange1) {
                  parse3BlackWinrateMiss1++;
                }
                if (Math.abs(nodeInfo.diffWinrate) <= Lizzie.config.winrateDiffRange2
                    && Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                  parse3BlackWinrateMiss2++;
                }
                if (Math.abs(nodeInfo.diffWinrate) > Lizzie.config.winrateDiffRange2) {
                  parse3BlackWinrateMiss3++;
                }
              }
            } else {
              if (nodeInfo.moveNum <= parse1Move) {
                if (Math.abs(nodeInfo.diffWinrate) < Lizzie.config.winrateDiffRange1) {
                  parse1WhiteWinrateMiss1++;
                }
                if (Math.abs(nodeInfo.diffWinrate) <= Lizzie.config.winrateDiffRange2
                    && Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                  parse1WhiteWinrateMiss2++;
                }
                if (Math.abs(nodeInfo.diffWinrate) > Lizzie.config.winrateDiffRange2) {
                  parse1WhiteWinrateMiss3++;
                }
              } else if (nodeInfo.moveNum <= parse2Move) {
                if (Math.abs(nodeInfo.diffWinrate) < Lizzie.config.winrateDiffRange1) {
                  parse2WhiteWinrateMiss1++;
                }
                if (Math.abs(nodeInfo.diffWinrate) <= Lizzie.config.winrateDiffRange2
                    && Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                  parse2WhiteWinrateMiss2++;
                }
                if (Math.abs(nodeInfo.diffWinrate) > Lizzie.config.winrateDiffRange2) {
                  parse2WhiteWinrateMiss3++;
                }
              } else {
                if (Math.abs(nodeInfo.diffWinrate) < Lizzie.config.winrateDiffRange1) {
                  parse3WhiteWinrateMiss1++;
                }
                if (Math.abs(nodeInfo.diffWinrate) <= Lizzie.config.winrateDiffRange2
                    && Math.abs(nodeInfo.diffWinrate) >= Lizzie.config.winrateDiffRange1) {
                  parse3WhiteWinrateMiss2++;
                }
                if (Math.abs(nodeInfo.diffWinrate) > Lizzie.config.winrateDiffRange2) {
                  parse3WhiteWinrateMiss3++;
                }
              }
            }
          }
        } else if (selectedIndex == 6) {
          if (nodeInfo.analyzed) {
            if (nodeInfo.isBlack) {
              if (nodeInfo.moveNum <= parse1Move) {
                if (Math.abs(nodeInfo.scoreMeanDiff) < Lizzie.config.scoreDiffRange1) {
                  parse1BlackScoreMiss1++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) <= Lizzie.config.scoreDiffRange2
                    && Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                  parse1BlackScoreMiss2++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) > Lizzie.config.scoreDiffRange2) {
                  parse1BlackScoreMiss3++;
                }
              } else if (nodeInfo.moveNum <= parse2Move) {
                if (Math.abs(nodeInfo.scoreMeanDiff) < Lizzie.config.scoreDiffRange1) {
                  parse2BlackScoreMiss1++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) <= Lizzie.config.scoreDiffRange2
                    && Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                  parse2BlackScoreMiss2++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) > Lizzie.config.scoreDiffRange2) {
                  parse2BlackScoreMiss3++;
                }
              } else {
                if (Math.abs(nodeInfo.scoreMeanDiff) < Lizzie.config.scoreDiffRange1) {
                  parse3BlackScoreMiss1++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) <= Lizzie.config.scoreDiffRange2
                    && Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                  parse3BlackScoreMiss2++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) > Lizzie.config.scoreDiffRange2) {
                  parse3BlackScoreMiss3++;
                }
              }
            } else {
              if (nodeInfo.moveNum <= parse1Move) {
                if (Math.abs(nodeInfo.scoreMeanDiff) < Lizzie.config.scoreDiffRange1) {
                  parse1WhiteScoreMiss1++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) <= Lizzie.config.scoreDiffRange2
                    && Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                  parse1WhiteScoreMiss2++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) > Lizzie.config.scoreDiffRange2) {
                  parse1WhiteScoreMiss3++;
                }
              } else if (nodeInfo.moveNum <= parse2Move) {
                if (Math.abs(nodeInfo.scoreMeanDiff) < Lizzie.config.scoreDiffRange1) {
                  parse2WhiteScoreMiss1++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) <= Lizzie.config.scoreDiffRange2
                    && Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                  parse2WhiteScoreMiss2++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) > Lizzie.config.scoreDiffRange2) {
                  parse2WhiteScoreMiss3++;
                }
              } else {
                if (Math.abs(nodeInfo.scoreMeanDiff) < Lizzie.config.scoreDiffRange1) {
                  parse3WhiteScoreMiss1++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) <= Lizzie.config.scoreDiffRange2
                    && Math.abs(nodeInfo.scoreMeanDiff) >= Lizzie.config.scoreDiffRange1) {
                  parse3WhiteScoreMiss2++;
                }
                if (Math.abs(nodeInfo.scoreMeanDiff) > Lizzie.config.scoreDiffRange2) {
                  parse3WhiteScoreMiss3++;
                }
              }
            }
          }

        } else {
          if (nodeInfo.analyzed) {
            if (nodeInfo.isBlack) {
              //              blackValue =
              //                  blackValue
              //                      + Math.pow(
              //                          nodeInfo.percentsMatch, (double) 1 /
              // Lizzie.config.matchAiTemperature);
              blackTrueValue = blackTrueValue + nodeInfo.percentsMatch;
              winratediffBlack = winratediffBlack + Math.abs(nodeInfo.diffWinrate);
              analyzedBlack = analyzedBlack + 1;
            } else {
              //              whiteValue =
              //                  whiteValue
              //                      + Math.pow(
              //                          nodeInfo.percentsMatch, (double) 1 /
              // Lizzie.config.matchAiTemperature);
              whiteTrueValue = whiteTrueValue + nodeInfo.percentsMatch;
              winratediffWhite = winratediffWhite + Math.abs(nodeInfo.diffWinrate);
              analyzedWhite = analyzedWhite + 1;
            }

            analyzed = analyzed + 1;
            if (nodeInfo.isBlack) {
              blackMoves = blackMoves + 1;

              if (nodeInfo.isMatchAi) blackMatch = blackMatch + 1;
            }
            if (!nodeInfo.isBlack) {
              whiteMoves = whiteMoves + 1;

              if (nodeInfo.isMatchAi) whiteMatch = whiteMatch + 1;
            }
          }
        }
      }
    }
    g.setColor(Color.BLACK);
    g.setFont(new Font("", Font.PLAIN, 13));
    if (selectedIndex == 2) {
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.blackAnalyzed")
              + analyzedBlack
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", winratediffBlack / analyzedBlack)
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndStdev")
              + String.format("%.1f", winratediffVarianceBlack / analyzedBlack)
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.winrateDiffRange1
              + "%："
              + range1Black
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", winratediffBlack1 / range1Black)
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndStdev")
              + String.format("%.1f", winratediffVarianceBlack1 / range1Black)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets")
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.winrateDiffRange2
              + "%："
              + range2Black
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", winratediffBlack2 / range2Black)
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndStdev")
              + String.format("%.1f", winratediffVarianceBlack2 / range2Black)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets"),
          5,
          15);

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.whiteAnalyzed")
              + analyzedWhite
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", winratediffWhite / analyzedWhite)
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndStdev")
              + String.format("%.1f", winratediffVarianceWhite / analyzedWhite)
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.winrateDiffRange1
              + "%："
              + range1White
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", winratediffWhite1 / range1White)
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndStdev")
              + String.format("%.1f", winratediffVarianceWhite1 / range1White)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets")
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.winrateDiffRange2
              + "%："
              + range2White
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", winratediffWhite2 / range2White)
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndStdev")
              + String.format("%.1f", winratediffVarianceWhite2 / range2White)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets"),
          5,
          35);
    } else if (selectedIndex == 3) {
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.blackAnalyzed")
              + analyzedBlackS
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", scorediffBlack / analyzedBlackS)
              + Lizzie.resourceBundle.getString("Movelistframe.scoreAndStdev")
              + String.format("%.1f", scorediffVarianceBlack / analyzedBlackS)
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.scorePoints")
              + range1BlackS
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", scorediffBlack1 / range1BlackS)
              + Lizzie.resourceBundle.getString("Movelistframe.stdev")
              + String.format("%.1f", scorediffVarianceBlack1 / range1BlackS)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets")
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.scorePoints")
              + range2BlackS
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", scorediffBlack2 / range2BlackS)
              + Lizzie.resourceBundle.getString("Movelistframe.stdev")
              + String.format("%.1f", scorediffVarianceBlack2 / range2BlackS)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets"),
          5,
          15);

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.whiteAnalyzed")
              + analyzedWhiteS
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", scorediffWhite / analyzedWhiteS)
              + Lizzie.resourceBundle.getString("Movelistframe.scoreAndStdev")
              + String.format("%.1f", scorediffVarianceWhite / analyzedWhiteS)
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.scorePoints")
              + range1WhiteS
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", scorediffWhite1 / range1WhiteS)
              + Lizzie.resourceBundle.getString("Movelistframe.stdev")
              + String.format("%.1f", scorediffVarianceWhite1 / range1WhiteS)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets")
              + Lizzie.resourceBundle.getString("Movelistframe.above")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.scorePoints")
              + range2WhiteS
              + Lizzie.resourceBundle.getString("Movelistframe.movedAndAverage")
              + String.format("%.2f", scorediffWhite2 / range2WhiteS)
              + Lizzie.resourceBundle.getString("Movelistframe.stdev")
              + String.format("%.1f", scorediffVarianceWhite2 / range2WhiteS)
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets"),
          5,
          35);
    } else if (selectedIndex == 0) {
      String percentBlack =
          String.format("%.1f", ((float) blackMatch / (blackMoves > 0 ? blackMoves : 1)) * 100)
              + "%";
      String percentWhite =
          String.format("%.1f", ((float) whiteMatch / (whiteMoves > 0 ? whiteMoves : 1)) * 100)
              + "%";
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.blackMatch")
              + blackMatch
              + "/"
              + blackMoves
              + " "
              + percentBlack
              + Lizzie.resourceBundle.getString("Movelistframe.AIscore")
              + String.format("%.2f", blackTrueValue * 100 / analyzedBlack)
              + Lizzie.resourceBundle.getString("Movelistframe.avgDifference")
              + String.format("%.2f", winratediffBlack / analyzedBlack)
              + "%",
          25,
          15);
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.whiteMatch")
              + whiteMatch
              + "/"
              + whiteMoves
              + " "
              + percentWhite
              + Lizzie.resourceBundle.getString("Movelistframe.AIscore")
              + String.format("%.2f", whiteTrueValue * 100 / analyzedWhite)
              + Lizzie.resourceBundle.getString("Movelistframe.avgDifference")
              + String.format("%.2f", winratediffWhite / analyzedWhite)
              + "%",
          25,
          35);
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.allmoves")
              + all
              + Lizzie.resourceBundle.getString("Movelistframe.analyzed")
              + analyzed,
          391,
          15);

      if (Lizzie.config.isChinese) {
        g.setColor(new Color(0, 0, 255, 100));
        g.fillRect(400, 23, 20, 14);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.blackMatchLabel"), 420, 35);
        g.setColor(new Color(0, 255, 0, 100));
        g.fillRect(460, 23, 20, 14);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.whiteMatchLabel"), 480, 35);
        g.setColor(Color.CYAN);
        g.setStroke(new BasicStroke(3));
        g.drawLine(522, 30, 540, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.analyzedLabel"), 545, 35);
        g.setColor(Color.ORANGE);
        g.setStroke(new BasicStroke(3));
        g.drawLine(588, 30, 606, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.unanalyzedLabel"), 611, 35);
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(1));
        g.drawLine(654, 30, 672, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.scoreLeadLabel"), 677, 35);
      } else {
        g.setColor(new Color(0, 0, 255, 100));
        g.fillRect(400, 23, 20, 14);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.blackMatchLabel"), 421, 35);
        g.setColor(new Color(0, 255, 0, 100));
        g.fillRect(492, 23, 20, 14);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.whiteMatchLabel"), 514, 35);
        g.setColor(Color.CYAN);
        g.setStroke(new BasicStroke(3));
        g.drawLine(588, 30, 606, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.analyzedLabel"), 611, 35);
        g.setColor(Color.ORANGE);
        g.setStroke(new BasicStroke(3));
        g.drawLine(668, 30, 686, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.unanalyzedLabel"), 691, 35);
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(1));
        g.drawLine(761, 30, 779, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.scoreLeadLabel"), 784, 35);
      }
    } else if (selectedIndex == 1) {
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.blackAIScore")
              + String.format("%.2f", blackTrueValue * 100 / analyzedBlack),
          // + Lizzie.resourceBundle.getString("Movelistframe.accordingTo")
          //  + String.format("%.2f", blackTrueValue * 100 / analyzedBlack)
          //              + Lizzie.resourceBundle.getString("Movelistframe.matchTemperature")
          //              + String.format("%.1f", Lizzie.config.matchAiTemperature)
          //    + ")",
          25,
          15);
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.whiteAIScore")
              + String.format("%.2f", whiteTrueValue * 100 / analyzedWhite),
          //    + Lizzie.resourceBundle.getString("Movelistframe.accordingTo")
          //     + String.format("%.2f", whiteValue * 100 / analyzedWhite)
          //              + Lizzie.resourceBundle.getString("Movelistframe.matchTemperature")
          //              + String.format("%.1f", Lizzie.config.matchAiTemperature)
          //       + ")",
          //  + Lizzie.resourceBundle.getString("Movelistframe.hintLeftQuestionMark"),
          25,
          35);
      if (Lizzie.config.isChinese) {
        g.drawString(
            Lizzie.resourceBundle.getString("Movelistframe.allmoves")
                + all
                + Lizzie.resourceBundle.getString("Movelistframe.analyzed")
                + analyzed,
            160,
            15);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawLine(172, 33, 190, 33);
        g.setColor(Color.LIGHT_GRAY);
        g.setStroke(new BasicStroke(3));
        g.drawLine(172, 27, 190, 27);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.analyzedLabel"), 195, 35);
        g.setColor(Color.ORANGE);
        g.setStroke(new BasicStroke(3));
        g.drawLine(238, 30, 256, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.unanalyzedLabel"), 261, 35);
      } else {
        g.drawString(
            Lizzie.resourceBundle.getString("Movelistframe.allmoves")
                + all
                + Lizzie.resourceBundle.getString("Movelistframe.analyzed")
                + analyzed,
            175,
            15);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawLine(182, 33, 200, 33);
        g.setColor(Color.LIGHT_GRAY);
        g.setStroke(new BasicStroke(3));
        g.drawLine(182, 27, 200, 27);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.analyzedLabel"), 205, 35);
        g.setColor(Color.ORANGE);
        g.setStroke(new BasicStroke(3));
        g.drawLine(268, 30, 280, 30);
        g.setColor(Color.BLACK);
        g.drawString(Lizzie.resourceBundle.getString("Movelistframe.unanalyzedLabel"), 285, 35);
      }
    } else if (selectedIndex == 4) {
      g.setFont(new Font("", Font.PLAIN, 12));
      double allBlackWinrateDiff =
          parse1BlackWinrateDiff + parse2BlackWinrateDiff + parse3BlackWinrateDiff;
      double allBlackScoreDiff = parse1BlackScoreDiff + parse2BlackScoreDiff + parse3BlackScoreDiff;
      int allBlakAnalyzed = parse1BlackAnalyzed + parse2BlackAnalyzed + parse3BlackAnalyzed;

      double allWhiteWinrateDiff =
          parse1WhiteWinrateDiff + parse2WhiteWinrateDiff + parse3WhiteWinrateDiff;
      double allWhiteScoreDiff = parse1WhiteScoreDiff + parse2WhiteScoreDiff + parse3WhiteScoreDiff;
      int allWhiteAnalyzed = parse1WhiteAnalyzed + parse2WhiteAnalyzed + parse3WhiteAnalyzed;
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.blackDifference")
              + Lizzie.resourceBundle.getString("Movelistframe.allWinrate")
              + (allBlakAnalyzed > 0
                  ? String.format("%.1f", allBlackWinrateDiff / allBlakAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (allBlakAnalyzed > 0
                  ? String.format("%.1f", allBlackScoreDiff / allBlakAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.openAndWinrate")
              + (parse1BlackAnalyzed > 0
                  ? String.format("%.1f", parse1BlackWinrateDiff / parse1BlackAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (parse1BlackAnalyzed > 0
                  ? String.format("%.1f", parse1BlackScoreDiff / parse1BlackAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.middleAndWinrate")
              + (parse2BlackAnalyzed > 0
                  ? String.format("%.1f", parse2BlackWinrateDiff / parse2BlackAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (parse2BlackAnalyzed > 0
                  ? String.format("%.1f", parse2BlackScoreDiff / parse2BlackAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.endAndWinrate")
              + (parse3BlackAnalyzed > 0
                  ? String.format("%.1f", parse3BlackWinrateDiff / parse3BlackAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (parse3BlackAnalyzed > 0
                  ? String.format("%.1f", parse3BlackScoreDiff / parse3BlackAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets"),
          25,
          15);
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.whiteDifference")
              + Lizzie.resourceBundle.getString("Movelistframe.allWinrate")
              + (allWhiteAnalyzed > 0
                  ? String.format("%.1f", allWhiteWinrateDiff / allWhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (allWhiteAnalyzed > 0
                  ? String.format("%.1f", allWhiteScoreDiff / allWhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.openAndWinrate")
              + (parse1WhiteAnalyzed > 0
                  ? String.format("%.1f", parse1WhiteWinrateDiff / parse1WhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (parse1WhiteAnalyzed > 0
                  ? String.format("%.1f", parse1WhiteScoreDiff / parse1WhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.middleAndWinrate")
              + (parse2WhiteAnalyzed > 0
                  ? String.format("%.1f", parse2WhiteWinrateDiff / parse2WhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (parse2WhiteAnalyzed > 0
                  ? String.format("%.1f", parse2WhiteScoreDiff / parse2WhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.endAndWinrate")
              + (parse3WhiteAnalyzed > 0
                  ? String.format("%.1f", parse3WhiteWinrateDiff / parse3WhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.percentAndScore")
              + (parse3WhiteAnalyzed > 0
                  ? String.format("%.1f", parse3WhiteScoreDiff / parse3WhiteAnalyzed)
                  : "0")
              + Lizzie.resourceBundle.getString("Movelistframe.rightBrackets"),
          25,
          35);
    } else if (selectedIndex == 5) {
      g.setFont(new Font("", Font.PLAIN, 12));
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.blackOpen")
              + Lizzie.config.winrateDiffRange1
              + "% "
              + parse1BlackWinrateMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.winrateDiffRange1
              + "-"
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse1BlackWinrateMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse1BlackWinrateMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndMiddle")
              + Lizzie.config.winrateDiffRange1
              + "% "
              + parse2BlackWinrateMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.winrateDiffRange1
              + "-"
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse2BlackWinrateMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse2BlackWinrateMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndEnd")
              + Lizzie.config.winrateDiffRange1
              + "% "
              + parse3BlackWinrateMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.winrateDiffRange1
              + "-"
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse3BlackWinrateMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse3BlackWinrateMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndRightBrackets"),
          5,
          15);

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.whiteOpen")
              + Lizzie.config.winrateDiffRange1
              + "% "
              + parse1WhiteWinrateMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.winrateDiffRange1
              + "-"
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse1WhiteWinrateMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse1WhiteWinrateMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndMiddle")
              + Lizzie.config.winrateDiffRange1
              + "% "
              + parse2WhiteWinrateMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.winrateDiffRange1
              + "-"
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse2WhiteWinrateMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse2WhiteWinrateMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndEnd")
              + Lizzie.config.winrateDiffRange1
              + "% "
              + parse3WhiteWinrateMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.winrateDiffRange1
              + "-"
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse3WhiteWinrateMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.winrateDiffRange2
              + "% "
              + parse3WhiteWinrateMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndRightBrackets"),
          5,
          35);
    } else if (selectedIndex == 6) {
      g.setFont(new Font("", Font.PLAIN, 12));
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.blackOpen")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse1BlackScoreMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.scoreDiffRange1
              + "-"
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse1BlackScoreMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse1BlackScoreMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndMiddle")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse2BlackScoreMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.scoreDiffRange1
              + "-"
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse2BlackScoreMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse2BlackScoreMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndEnd")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse3BlackScoreMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.scoreDiffRange1
              + "-"
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse3BlackScoreMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse3BlackScoreMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndRightBrackets"),
          5,
          15);

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.whiteOpen")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse1WhiteScoreMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.scoreDiffRange1
              + "-"
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse1WhiteScoreMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse1WhiteScoreMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndMiddle")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse2WhiteScoreMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.scoreDiffRange1
              + "-"
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse2WhiteScoreMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse2WhiteScoreMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndEnd")
              + Lizzie.config.scoreDiffRange1
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse3WhiteScoreMiss1
              + Lizzie.resourceBundle.getString("Movelistframe.moves")
              + Lizzie.config.scoreDiffRange1
              + "-"
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse3WhiteScoreMiss2
              + Lizzie.resourceBundle.getString("Movelistframe.movesAnd>")
              + Lizzie.config.scoreDiffRange2
              + Lizzie.resourceBundle.getString("Movelistframe.pts")
              + parse3WhiteScoreMiss3
              + Lizzie.resourceBundle.getString("Movelistframe.movesAndRightBrackets"),
          5,
          35);
    }
  }

  private int getMouseOverBigMistakeIndex(int y) {
    int width = params[2];
    return (y / (width / 10));
  }

  private int moveNumber(int x, int y) {
    int origPosx = origParams[0];
    int origPosy = origParams[1];
    int origWidth = origParams[2];
    int origHeight = origParams[3];
    int posx = params[0];
    //  int posy = params[1];
    int width = params[2];
    //  int height = params[3];
    int numMoves = params[4];
    if (origPosx <= x && x < origPosx + origWidth && origPosy <= y && y < origPosy + origHeight) {
      // x == posx + (movenum * width / numMoves) ==> movenum = ...
      int movenum;
      // movenum == moveNumber - 1 ==> moveNumber = ...
      if (selectedIndex == 2 || selectedIndex == 3) {
        movenum = Math.round((x - posx) * (numMoves - 1) / (float) width);
        return movenum
            + ((Lizzie.board.matchAiFirstMove) > 0 ? (Lizzie.board.matchAiFirstMove) : 1);
      } else {
        movenum = Math.round((x - posx) * numMoves / (float) width);
        return movenum + 1;
      }
    } else {
      return -1;
    }
  }

  private Font makeFont(Font fontBase, int style) {
    Font font = fontBase.deriveFont(style, 100);
    Map<TextAttribute, Object> atts = new HashMap<>();
    atts.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
    return font.deriveFont(atts);
  }

  private void drawString(
      Graphics2D g,
      int x,
      int y,
      Font fontBase,
      int style,
      String string,
      float maximumFontHeight,
      double maximumFontWidth,
      int aboveOrBelow) {

    Font font = makeFont(fontBase, style);

    // set maximum size of font
    FontMetrics fm = g.getFontMetrics(font);
    font = font.deriveFont((float) (font.getSize2D() * maximumFontWidth / fm.stringWidth(string)));
    font = font.deriveFont(min(maximumFontHeight, font.getSize()));
    //    if(font.getSize()<15)
    //    	font=new Font(font.getName(),Font.BOLD,font.getSize());
    g.setFont(font);
    fm = g.getFontMetrics(font);
    int height = fm.getAscent() - fm.getDescent();
    int verticalOffset;
    if (aboveOrBelow == -1) {
      verticalOffset = height / 2;
    } else if (aboveOrBelow == 1) {
      verticalOffset = -height / 2;
    } else {
      verticalOffset = 0;
    }

    // bounding box for debugging
    // g.drawRect(x-(int)maximumFontWidth/2, y - height/2 + verticalOffset,
    // (int)maximumFontWidth,
    // height+verticalOffset );
    g.drawString(string, x - fm.stringWidth(string) / 2, y + height / 2 + verticalOffset);
  }

  private void drawString(
      Graphics2D g,
      int x,
      int y,
      Font fontBase,
      String string,
      float maximumFontHeight,
      double maximumFontWidth) {
    drawString(g, x, y, fontBase, Font.PLAIN, string, maximumFontHeight, maximumFontWidth, 0);
  }

  private double getMatchValue(BoardHistoryNode node) {
    boolean isBlack = node.getData().blackToPlay;
    double blackValue = 0;
    double whiteValue = 0;
    int analyzedBlack = 0;
    int analyzedWhite = 0;
    while (node.previous().isPresent()) {
      node = node.previous().get();
      NodeInfo nodeInfo =
          showBranch.getSelectedIndex() == 0
              ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
              : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
      if (node.getData().moveNumber <= Lizzie.board.matchAiLastMove
          && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove) {
        if (nodeInfo.analyzed) {
          if (node.getData().blackToPlay) {
            blackValue = blackValue + nodeInfo.percentsMatch;
            //                    + Math.pow(
            //                        nodeInfo.percentsMatch, (double) 1 /
            // Lizzie.config.matchAiTemperature);
            analyzedBlack = analyzedBlack + 1;
          } else {
            whiteValue = whiteValue + nodeInfo.percentsMatch;
            //                    + Math.pow(
            //                        nodeInfo.percentsMatch, (double) 1 /
            // Lizzie.config.matchAiTemperature);
            analyzedWhite = analyzedWhite + 1;
          }
        }
      }
    }

    analyzedB = analyzedBlack;
    analyzedW = analyzedWhite;
    blackMatchValue = blackValue * 100 / analyzedBlack;
    whiteMatchValue = whiteValue * 100 / analyzedWhite;
    if (isBlack) {
      analyzed = analyzedBlack;
      return blackValue * 100 / analyzedBlack;
    } else {
      analyzed = analyzedWhite;
      return whiteValue * 100 / analyzedWhite;
    }
  }

  public void draw(Graphics2D g, int posx, int posy, int width, int height) {
    BoardHistoryNode curMove = Lizzie.board.getHistory().getCurrentHistoryNode();
    BoardHistoryNode node = curMove;
    BoardHistoryNode lastMove = Lizzie.board.getHistory().getEnd();
    // maxcoreMean = 30.0;

    // draw background rectangle
    //    final Paint gradient =
    //        new GradientPaint(
    //            new Point2D.Float(posx, posy),
    //            new Color(120, 120, 120, 180),
    //            new Point2D.Float(posx, posy + height),
    //            new Color(185, 185, 185, 185));

    // Paint original = g.getPaint();
    // g.setPaint(gradient);

    g.setColor(new Color(150, 150, 150));

    if (selectedIndex != 0) g.fillRect(posx, posy, width + 5, height);
    else g.fillRect(posx, posy, width + 2, height);
    if (Lizzie.board.isPkBoard) {
      g.setColor(new Color(0, 0, 0, 180));
      drawString(
          g,
          posx + width / 2,
          posy + height / 2,
          headFont,
          Lizzie.resourceBundle.getString("Movelistframe.engineGameHint"), // "引擎对局需自动分析后才可显示图表",
          (float) (height * 0.6),
          width * 0.6);
      return;
    }

    // draw border
    int strokeRadius = 1;
    g.setStroke(new BasicStroke(strokeRadius == 1 ? strokeRadius : 2 * strokeRadius));
    //  g.setPaint(borderGradient);
    //    if (Lizzie.config.showBorder) {
    //      g.drawRect(
    //          posx + strokeRadius,
    //          posy + strokeRadius,
    //          width - 2 * strokeRadius,
    //          height - 2 * strokeRadius);
    //    } else {
    g.drawLine(
        posx + strokeRadius, posy + strokeRadius, posx - strokeRadius + width, posy + strokeRadius);
    //   }

    // g.setPaint(original);

    // record parameters (before resizing) for calculating moveNumber
    origParams[0] = posx;
    origParams[1] = posy;
    origParams[2] = width;
    origParams[3] = height;

    // resize the box now so it's inside the border
    if (selectedIndex < 7) {
      if (selectedIndex == 2 || selectedIndex == 3) posx += 23;
      else if (selectedIndex < 4) posx += 15;
      if (selectedIndex == 2 || selectedIndex == 3) posy += 2 * strokeRadius;
      else posy += 8 * strokeRadius;
      width -= 4 * strokeRadius;
      if (selectedIndex == 2 || selectedIndex == 3) height -= 15;
      else if (selectedIndex >= 4) {
        height -= 10 * strokeRadius;
      } else height -= 16 * strokeRadius;
      if (selectedIndex == 2 || selectedIndex == 3) {
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.WHITE);
        g.drawLine(0, posy, origParams[2] + 5, posy);
        g.drawLine(0, posy + height, origParams[2] + 5, posy + height);
      }
    }
    // draw lines marking 50% 60% 70% etc.
    Stroke dashed =
        new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {4}, 0);

    g.setColor(Color.white);
    g.setStroke(dashed);
    int winRateGridLines = 3;
    if (selectedIndex == 2 || selectedIndex == 3) {
      winRateGridLines = 7;
    }
    if (selectedIndex == 4) {
      winRateGridLines = 4;
    }
    if (selectedIndex > 4) {
      winRateGridLines = 5;
    }
    if (selectedIndex >= 7) winRateGridLines = 3;
    //    int midline = 0;
    //    int midy = 0;
    //    if (Lizzie.config.showBlunderBar) {
    // int       midline = (int) Math.ceil(winRateGridLines / 2.0);
    //      midy = posy + height / 2;
    //    }
    g.setFont(new Font("", Font.BOLD, 10));
    for (int i = 1; i <= winRateGridLines; i++) {
      double percent = i * 100.0 / (winRateGridLines + 1);
      int y = posy + height - (int) (height * percent / 100);

      if (selectedIndex == 2 || selectedIndex == 3) {
        switch (i) {
          case 1:
            g.drawString("+20", 3, y + 3);
            break;
          case 2:
            g.drawString("+5", 3, y + 3);
            break;
          case 3:
            g.setStroke(new BasicStroke(1));
            g.drawString("0", 3, y + 3);
            break;
          case 4:
            g.drawString("-5", 3, y + 3);
            break;
          case 5:
            g.drawString("-10", 3, y + 3);
            break;
          case 6:
            g.drawString("-20", 3, y + 3);
            break;
          case 7:
            g.drawString("-50", 3, y + 3);
            break;
        }
        g.drawLine(posx, y, posx + width - 3, y);
        g.setStroke(dashed);
      } else if (selectedIndex == 4) {

        if (i == 1) {
          g.setStroke(new BasicStroke(1));
          g.drawLine(0, y, posx + width, y);
          g.setStroke(dashed);
        } else {
          g.setColor(Color.LIGHT_GRAY);
          g.drawLine(posx + 17, y, posx + width - 25, y);
          g.setColor(Color.WHITE);
          g.drawString(100 * (i - 1) / 4 + "", 3, y + 3);
        }
      } else if (selectedIndex > 4 && selectedIndex < 7) {
        if (i == 1) {
        } else {
          if (i == 2) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(1));
            g.drawLine(0, y, posx + width, y);
            g.setStroke(dashed);
            g.setColor(Color.LIGHT_GRAY);
          } else {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(posx, y, posx + width - 25, y);
          }
        }
      } else {
        if (selectedIndex >= 7) {
          g.setColor(Color.GRAY);
          g.drawLine(posx, y, posx + width, y);
        } else g.drawLine(posx, y, posx + width - 25, y);
        if (selectedIndex < 5)
          g.drawString(
              (selectedIndex == 1 ? 80 : 100) * i / 4 + (selectedIndex == 1 ? 20 : 0) + "",
              3,
              y + 3);
      }
    }
    if (selectedIndex == 5) {
      g.setFont(new Font(Lizzie.config.fontName, Font.BOLD, 12));
      g.setColor(Color.BLACK);
      double percent = 2 * 100.0 / (winRateGridLines + 1);
      int y = posy + height - (int) (height * percent / 100);
      int y2 = posy + height - (int) (height * (100.0 / (winRateGridLines + 1)) / 100);

      int parse1maxHeight = parse1BlackWinrateMiss1;
      parse1maxHeight = Math.max(parse1maxHeight, parse1BlackWinrateMiss2);
      parse1maxHeight = Math.max(parse1maxHeight, parse1BlackWinrateMiss3);
      parse1maxHeight = Math.max(parse1maxHeight, parse1WhiteWinrateMiss1);
      parse1maxHeight = Math.max(parse1maxHeight, parse1WhiteWinrateMiss2);
      parse1maxHeight = Math.max(parse1maxHeight, parse1WhiteWinrateMiss3);

      int parse2maxHeight = parse2BlackWinrateMiss1;
      parse2maxHeight = Math.max(parse2maxHeight, parse2BlackWinrateMiss2);
      parse2maxHeight = Math.max(parse2maxHeight, parse2BlackWinrateMiss3);
      parse2maxHeight = Math.max(parse2maxHeight, parse2WhiteWinrateMiss1);
      parse2maxHeight = Math.max(parse2maxHeight, parse2WhiteWinrateMiss2);
      parse2maxHeight = Math.max(parse2maxHeight, parse2WhiteWinrateMiss3);

      int parse3maxHeight = parse3BlackWinrateMiss1;
      parse3maxHeight = Math.max(parse3maxHeight, parse3BlackWinrateMiss2);
      parse3maxHeight = Math.max(parse3maxHeight, parse3BlackWinrateMiss3);
      parse3maxHeight = Math.max(parse3maxHeight, parse3WhiteWinrateMiss1);
      parse3maxHeight = Math.max(parse3maxHeight, parse3WhiteWinrateMiss2);
      parse3maxHeight = Math.max(parse3maxHeight, parse3WhiteWinrateMiss3);

      int allBlackWinrateMiss1 =
          parse1BlackWinrateMiss1 + parse2BlackWinrateMiss1 + parse3BlackWinrateMiss1;
      int allBlackWinrateMiss2 =
          parse1BlackWinrateMiss2 + parse2BlackWinrateMiss2 + parse3BlackWinrateMiss2;
      int allBlackWinrateMiss3 =
          parse1BlackWinrateMiss3 + parse2BlackWinrateMiss3 + parse3BlackWinrateMiss3;

      int allWhiteWinrateMiss1 =
          parse1WhiteWinrateMiss1 + parse2WhiteWinrateMiss1 + parse3WhiteWinrateMiss1;
      int allWhiteWinrateMiss2 =
          parse1WhiteWinrateMiss2 + parse2WhiteWinrateMiss2 + parse3WhiteWinrateMiss2;
      int allWhiteWinrateMiss3 =
          parse1WhiteWinrateMiss3 + parse2WhiteWinrateMiss3 + parse3WhiteWinrateMiss3;

      int allMaxHeight = allBlackWinrateMiss1;
      allMaxHeight = Math.max(allMaxHeight, allBlackWinrateMiss2);
      allMaxHeight = Math.max(allMaxHeight, allBlackWinrateMiss3);
      allMaxHeight = Math.max(allMaxHeight, allWhiteWinrateMiss1);
      allMaxHeight = Math.max(allMaxHeight, allWhiteWinrateMiss2);
      allMaxHeight = Math.max(allMaxHeight, allWhiteWinrateMiss3);

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.all"),
          (int)
              (posx
                  + 9 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(Lizzie.resourceBundle.getString("Movelistframe.all"))
                      / 2),
          y2 + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.open") + "(1-" + parse1Move + ")",
          (int)
              (posx
                  + 27 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.open")
                                  + "(1-"
                                  + parse1Move
                                  + ")")
                      / 2),
          y2 + g.getFontMetrics().getHeight());
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.middle")
              + "("
              + (parse1Move + 1)
              + "-"
              + parse2Move
              + ")",
          (int)
              (posx
                  + 45 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.middle")
                                  + "("
                                  + (parse1Move + 1)
                                  + "-"
                                  + parse2Move
                                  + ")")
                      / 2),
          y2 + g.getFontMetrics().getHeight());
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.end")
              + "("
              + (parse2Move + 1)
              + Lizzie.resourceBundle.getString("Movelistframe.toEnd")
              + ")",
          (int)
              (posx
                  + 63 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.end")
                                  + "("
                                  + (parse2Move + 1)
                                  + Lizzie.resourceBundle.getString("Movelistframe.toEnd")
                                  + ")")
                      / 2),
          y2 + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.winrateDiffRange1 + "%",
          (int)
              (posx
                  + 4 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.winrateDiffRange1 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.winrateDiffRange1 + "-" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 9 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.winrateDiffRange1
                                  + "-"
                                  + Lizzie.config.winrateDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 14 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.winrateDiffRange2 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.winrateDiffRange1 + "%",
          (int)
              (posx
                  + 22 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.winrateDiffRange1 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.winrateDiffRange1 + "-" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 27 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.winrateDiffRange1
                                  + "-"
                                  + Lizzie.config.winrateDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 32 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.winrateDiffRange2 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.winrateDiffRange1 + "%",
          (int)
              (posx
                  + 58 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.winrateDiffRange1 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.winrateDiffRange1 + "-" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 63 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.winrateDiffRange1
                                  + "-"
                                  + Lizzie.config.winrateDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 68 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.winrateDiffRange2 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.winrateDiffRange1 + "%",
          (int)
              (posx
                  + 40 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.winrateDiffRange1 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.winrateDiffRange1 + "-" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 45 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.winrateDiffRange1
                                  + "-"
                                  + Lizzie.config.winrateDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.winrateDiffRange2 + "%",
          (int)
              (posx
                  + 50 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.winrateDiffRange2 + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.fillRect(
          posx + 2 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allBlackWinrateMiss1 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allBlackWinrateMiss1 / allMaxHeight)));

      g.drawString(
          allBlackWinrateMiss1 + "",
          (int)
              (posx
                  + 3 * width / 72
                  - g.getFontMetrics().stringWidth(allBlackWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allBlackWinrateMiss1 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 7 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allBlackWinrateMiss2 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allBlackWinrateMiss2 / allMaxHeight)));

      g.drawString(
          allBlackWinrateMiss2 + "",
          (int)
              (posx
                  + 8 * width / 72
                  - g.getFontMetrics().stringWidth(allBlackWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allBlackWinrateMiss2 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 12 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allBlackWinrateMiss3 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allBlackWinrateMiss3 / allMaxHeight)));

      g.drawString(
          allBlackWinrateMiss3 + "",
          (int)
              (posx
                  + 13 * width / 72
                  - g.getFontMetrics().stringWidth(allBlackWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allBlackWinrateMiss3 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 20 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss1 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss1 / parse1maxHeight)));

      g.drawString(
          parse1BlackWinrateMiss1 + "",
          (int)
              (posx
                  + 21 * width / 72
                  - g.getFontMetrics().stringWidth(parse1BlackWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss1 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 25 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss2 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss2 / parse1maxHeight)));

      g.drawString(
          parse1BlackWinrateMiss2 + "",
          (int)
              (posx
                  + 26 * width / 72
                  - g.getFontMetrics().stringWidth(parse1BlackWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss2 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 30 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss3 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss3 / parse1maxHeight)));

      g.drawString(
          parse1BlackWinrateMiss3 + "",
          (int)
              (posx
                  + 31 * width / 72
                  - g.getFontMetrics().stringWidth(parse1BlackWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1BlackWinrateMiss3 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 38 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss1 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss1 / parse2maxHeight)));

      g.drawString(
          parse2BlackWinrateMiss1 + "",
          (int)
              (posx
                  + 39 * width / 72
                  - g.getFontMetrics().stringWidth(parse2BlackWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss1 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 43 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss2 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss2 / parse2maxHeight)));

      g.drawString(
          parse2BlackWinrateMiss2 + "",
          (int)
              (posx
                  + 44 * width / 72
                  - g.getFontMetrics().stringWidth(parse2BlackWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss2 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 48 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss3 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss3 / parse2maxHeight)));

      g.drawString(
          parse2BlackWinrateMiss3 + "",
          (int)
              (posx
                  + 49 * width / 72
                  - g.getFontMetrics().stringWidth(parse2BlackWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2BlackWinrateMiss3 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 56 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss1 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss1 / parse3maxHeight)));

      g.drawString(
          parse3BlackWinrateMiss1 + "",
          (int)
              (posx
                  + 57 * width / 72
                  - g.getFontMetrics().stringWidth(parse3BlackWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss1 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 61 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss2 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss2 / parse3maxHeight)));

      g.drawString(
          parse3BlackWinrateMiss2 + "",
          (int)
              (posx
                  + 62 * width / 72
                  - g.getFontMetrics().stringWidth(parse3BlackWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss2 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 66 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss3 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss3 / parse3maxHeight)));

      g.drawString(
          parse3BlackWinrateMiss3 + "",
          (int)
              (posx
                  + 67 * width / 72
                  - g.getFontMetrics().stringWidth(parse3BlackWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3BlackWinrateMiss3 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.setColor(Color.WHITE);

      g.fillRect(
          posx + 4 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss1 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss1 / allMaxHeight)));

      g.drawString(
          allWhiteWinrateMiss1 + "",
          (int)
              (posx
                  + 5 * width / 72
                  - g.getFontMetrics().stringWidth(allWhiteWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss1 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 9 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss2 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss2 / allMaxHeight)));

      g.drawString(
          allWhiteWinrateMiss2 + "",
          (int)
              (posx
                  + 10 * width / 72
                  - g.getFontMetrics().stringWidth(allWhiteWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss2 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 14 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss3 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss3 / allMaxHeight)));

      g.drawString(
          allWhiteWinrateMiss3 + "",
          (int)
              (posx
                  + 15 * width / 72
                  - g.getFontMetrics().stringWidth(allWhiteWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allWhiteWinrateMiss3 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 22 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss1 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss1 / parse1maxHeight)));

      g.drawString(
          parse1WhiteWinrateMiss1 + "",
          (int)
              (posx
                  + 23 * width / 72
                  - g.getFontMetrics().stringWidth(parse1WhiteWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss1 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 27 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss2 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss2 / parse1maxHeight)));

      g.drawString(
          parse1WhiteWinrateMiss2 + "",
          (int)
              (posx
                  + 28 * width / 72
                  - g.getFontMetrics().stringWidth(parse1WhiteWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss2 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 32 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss3 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss3 / parse1maxHeight)));

      g.drawString(
          parse1WhiteWinrateMiss3 + "",
          (int)
              (posx
                  + 33 * width / 72
                  - g.getFontMetrics().stringWidth(parse1WhiteWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1WhiteWinrateMiss3 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 40 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss1 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss1 / parse2maxHeight)));

      g.drawString(
          parse2WhiteWinrateMiss1 + "",
          (int)
              (posx
                  + 41 * width / 72
                  - g.getFontMetrics().stringWidth(parse2WhiteWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss1 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 45 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss2 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss2 / parse2maxHeight)));

      g.drawString(
          parse2WhiteWinrateMiss2 + "",
          (int)
              (posx
                  + 46 * width / 72
                  - g.getFontMetrics().stringWidth(parse2WhiteWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss2 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 50 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss3 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss3 / parse2maxHeight)));

      g.drawString(
          parse2WhiteWinrateMiss3 + "",
          (int)
              (posx
                  + 51 * width / 72
                  - g.getFontMetrics().stringWidth(parse2WhiteWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2WhiteWinrateMiss3 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 58 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss1 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss1 / parse3maxHeight)));

      g.drawString(
          parse3WhiteWinrateMiss1 + "",
          (int)
              (posx
                  + 59 * width / 72
                  - g.getFontMetrics().stringWidth(parse3WhiteWinrateMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss1 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 63 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss2 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss2 / parse3maxHeight)));

      g.drawString(
          parse3WhiteWinrateMiss2 + "",
          (int)
              (posx
                  + 64 * width / 72
                  - g.getFontMetrics().stringWidth(parse3WhiteWinrateMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss2 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 68 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss3 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss3 / parse3maxHeight)));

      g.drawString(
          parse3WhiteWinrateMiss3 + "",
          (int)
              (posx
                  + 69 * width / 72
                  - g.getFontMetrics().stringWidth(parse3WhiteWinrateMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3WhiteWinrateMiss3 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);
    }
    if (selectedIndex == 6) {
      // 待完成
      g.setFont(new Font(Lizzie.config.fontName, Font.BOLD, 12));
      g.setColor(Color.BLACK);
      double percent = 2 * 100.0 / (winRateGridLines + 1);
      int y = posy + height - (int) (height * percent / 100);
      int y2 = posy + height - (int) (height * (100.0 / (winRateGridLines + 1)) / 100);

      int parse1maxHeight = parse1BlackScoreMiss1;
      parse1maxHeight = Math.max(parse1maxHeight, parse1BlackScoreMiss2);
      parse1maxHeight = Math.max(parse1maxHeight, parse1BlackScoreMiss3);
      parse1maxHeight = Math.max(parse1maxHeight, parse1WhiteScoreMiss1);
      parse1maxHeight = Math.max(parse1maxHeight, parse1WhiteScoreMiss2);
      parse1maxHeight = Math.max(parse1maxHeight, parse1WhiteScoreMiss3);

      int parse2maxHeight = parse2BlackScoreMiss1;
      parse2maxHeight = Math.max(parse2maxHeight, parse2BlackScoreMiss2);
      parse2maxHeight = Math.max(parse2maxHeight, parse2BlackScoreMiss3);
      parse2maxHeight = Math.max(parse2maxHeight, parse2WhiteScoreMiss1);
      parse2maxHeight = Math.max(parse2maxHeight, parse2WhiteScoreMiss2);
      parse2maxHeight = Math.max(parse2maxHeight, parse2WhiteScoreMiss3);

      int parse3maxHeight = parse3BlackScoreMiss1;
      parse3maxHeight = Math.max(parse3maxHeight, parse3BlackScoreMiss2);
      parse3maxHeight = Math.max(parse3maxHeight, parse3BlackScoreMiss3);
      parse3maxHeight = Math.max(parse3maxHeight, parse3WhiteScoreMiss1);
      parse3maxHeight = Math.max(parse3maxHeight, parse3WhiteScoreMiss2);
      parse3maxHeight = Math.max(parse3maxHeight, parse3WhiteScoreMiss3);

      int allBlackScoreMiss1 =
          parse1BlackScoreMiss1 + parse2BlackScoreMiss1 + parse3BlackScoreMiss1;
      int allBlackScoreMiss2 =
          parse1BlackScoreMiss2 + parse2BlackScoreMiss2 + parse3BlackScoreMiss2;
      int allBlackScoreMiss3 =
          parse1BlackScoreMiss3 + parse2BlackScoreMiss3 + parse3BlackScoreMiss3;

      int allWhiteScoreMiss1 =
          parse1WhiteScoreMiss1 + parse2WhiteScoreMiss1 + parse3WhiteScoreMiss1;
      int allWhiteScoreMiss2 =
          parse1WhiteScoreMiss2 + parse2WhiteScoreMiss2 + parse3WhiteScoreMiss2;
      int allWhiteScoreMiss3 =
          parse1WhiteScoreMiss3 + parse2WhiteScoreMiss3 + parse3WhiteScoreMiss3;

      int allMaxHeight = allBlackScoreMiss1;
      allMaxHeight = Math.max(allMaxHeight, allBlackScoreMiss2);
      allMaxHeight = Math.max(allMaxHeight, allBlackScoreMiss3);
      allMaxHeight = Math.max(allMaxHeight, allWhiteScoreMiss1);
      allMaxHeight = Math.max(allMaxHeight, allWhiteScoreMiss2);
      allMaxHeight = Math.max(allMaxHeight, allWhiteScoreMiss3);

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.all"),
          (int)
              (posx
                  + 9 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(Lizzie.resourceBundle.getString("Movelistframe.all"))
                      / 2),
          y2 + g.getFontMetrics().getHeight());
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.open") + "(1-" + parse1Move + ")",
          (int)
              (posx
                  + 27 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.open")
                                  + "(1-"
                                  + parse1Move
                                  + ")")
                      / 2),
          y2 + g.getFontMetrics().getHeight());
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.middle")
              + "("
              + (parse1Move + 1)
              + "-"
              + parse2Move
              + ")",
          (int)
              (posx
                  + 45 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.middle")
                                  + "("
                                  + (parse1Move + 1)
                                  + "-"
                                  + parse2Move
                                  + ")")
                      / 2),
          y2 + g.getFontMetrics().getHeight());
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.end")
              + "("
              + (parse2Move + 1)
              + Lizzie.resourceBundle.getString("Movelistframe.toEnd")
              + ")",
          (int)
              (posx
                  + 63 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.end")
                                  + "("
                                  + (parse2Move + 1)
                                  + Lizzie.resourceBundle.getString("Movelistframe.toEnd")
                                  + ")")
                      / 2),
          y2 + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.scoreDiffRange1 + "%",
          (int)
              (posx
                  + 4 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.scoreDiffRange1 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.scoreDiffRange1 + "-" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 9 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.scoreDiffRange1
                                  + "-"
                                  + Lizzie.config.scoreDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 14 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.scoreDiffRange2 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.scoreDiffRange1 + "%",
          (int)
              (posx
                  + 22 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.scoreDiffRange1 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.scoreDiffRange1 + "-" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 27 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.scoreDiffRange1
                                  + "-"
                                  + Lizzie.config.scoreDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 32 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.scoreDiffRange2 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.scoreDiffRange1 + "%",
          (int)
              (posx
                  + 58 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.scoreDiffRange1 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.scoreDiffRange1 + "-" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 63 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.scoreDiffRange1
                                  + "-"
                                  + Lizzie.config.scoreDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 68 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.scoreDiffRange2 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          "<" + Lizzie.config.scoreDiffRange1 + "%",
          (int)
              (posx
                  + 40 * width / 72
                  - g.getFontMetrics().stringWidth("<" + Lizzie.config.scoreDiffRange1 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          Lizzie.config.scoreDiffRange1 + "-" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 45 * width / 72
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.config.scoreDiffRange1
                                  + "-"
                                  + Lizzie.config.scoreDiffRange2
                                  + "%")
                      / 2),
          y + g.getFontMetrics().getHeight());

      g.drawString(
          ">" + Lizzie.config.scoreDiffRange2 + "%",
          (int)
              (posx
                  + 50 * width / 72
                  - g.getFontMetrics().stringWidth(">" + Lizzie.config.scoreDiffRange2 + "%") / 2),
          y + g.getFontMetrics().getHeight());

      g.fillRect(
          posx + 2 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allBlackScoreMiss1 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allBlackScoreMiss1 / allMaxHeight)));

      g.drawString(
          allBlackScoreMiss1 + "",
          (int)
              (posx + 3 * width / 72 - g.getFontMetrics().stringWidth(allBlackScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allBlackScoreMiss1 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 7 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allBlackScoreMiss2 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allBlackScoreMiss2 / allMaxHeight)));

      g.drawString(
          allBlackScoreMiss2 + "",
          (int)
              (posx + 8 * width / 72 - g.getFontMetrics().stringWidth(allBlackScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allBlackScoreMiss2 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 12 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allBlackScoreMiss3 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allBlackScoreMiss3 / allMaxHeight)));

      g.drawString(
          allBlackScoreMiss3 + "",
          (int)
              (posx
                  + 13 * width / 72
                  - g.getFontMetrics().stringWidth(allBlackScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allBlackScoreMiss3 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 20 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss1 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss1 / parse1maxHeight)));

      g.drawString(
          parse1BlackScoreMiss1 + "",
          (int)
              (posx
                  + 21 * width / 72
                  - g.getFontMetrics().stringWidth(parse1BlackScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss1 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 25 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss2 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss2 / parse1maxHeight)));

      g.drawString(
          parse1BlackScoreMiss2 + "",
          (int)
              (posx
                  + 26 * width / 72
                  - g.getFontMetrics().stringWidth(parse1BlackScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss2 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 30 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss3 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss3 / parse1maxHeight)));

      g.drawString(
          parse1BlackScoreMiss3 + "",
          (int)
              (posx
                  + 31 * width / 72
                  - g.getFontMetrics().stringWidth(parse1BlackScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1BlackScoreMiss3 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 38 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss1 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss1 / parse2maxHeight)));

      g.drawString(
          parse2BlackScoreMiss1 + "",
          (int)
              (posx
                  + 39 * width / 72
                  - g.getFontMetrics().stringWidth(parse2BlackScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss1 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 43 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss2 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss2 / parse2maxHeight)));

      g.drawString(
          parse2BlackScoreMiss2 + "",
          (int)
              (posx
                  + 44 * width / 72
                  - g.getFontMetrics().stringWidth(parse2BlackScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss2 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 48 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss3 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss3 / parse2maxHeight)));

      g.drawString(
          parse2BlackScoreMiss3 + "",
          (int)
              (posx
                  + 49 * width / 72
                  - g.getFontMetrics().stringWidth(parse2BlackScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2BlackScoreMiss3 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 56 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss1 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss1 / parse3maxHeight)));

      g.drawString(
          parse3BlackScoreMiss1 + "",
          (int)
              (posx
                  + 57 * width / 72
                  - g.getFontMetrics().stringWidth(parse3BlackScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss1 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 61 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss2 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss2 / parse3maxHeight)));

      g.drawString(
          parse3BlackScoreMiss2 + "",
          (int)
              (posx
                  + 62 * width / 72
                  - g.getFontMetrics().stringWidth(parse3BlackScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss2 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 66 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss3 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss3 / parse3maxHeight)));

      g.drawString(
          parse3BlackScoreMiss3 + "",
          (int)
              (posx
                  + 67 * width / 72
                  - g.getFontMetrics().stringWidth(parse3BlackScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3BlackScoreMiss3 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.setColor(Color.WHITE);

      g.fillRect(
          posx + 4 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allWhiteScoreMiss1 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allWhiteScoreMiss1 / allMaxHeight)));

      g.drawString(
          allWhiteScoreMiss1 + "",
          (int)
              (posx + 5 * width / 72 - g.getFontMetrics().stringWidth(allWhiteScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allWhiteScoreMiss1 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 9 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allWhiteScoreMiss2 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allWhiteScoreMiss2 / allMaxHeight)));

      g.drawString(
          allWhiteScoreMiss2 + "",
          (int)
              (posx
                  + 10 * width / 72
                  - g.getFontMetrics().stringWidth(allWhiteScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allWhiteScoreMiss2 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 14 * width / 72,
          y - (int) (height * 4 / 7 * ((double) allWhiteScoreMiss3 / allMaxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) allWhiteScoreMiss3 / allMaxHeight)));

      g.drawString(
          allWhiteScoreMiss3 + "",
          (int)
              (posx
                  + 15 * width / 72
                  - g.getFontMetrics().stringWidth(allWhiteScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) allWhiteScoreMiss3 / allMaxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 22 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss1 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss1 / parse1maxHeight)));

      g.drawString(
          parse1WhiteScoreMiss1 + "",
          (int)
              (posx
                  + 23 * width / 72
                  - g.getFontMetrics().stringWidth(parse1WhiteScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss1 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 27 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss2 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss2 / parse1maxHeight)));

      g.drawString(
          parse1WhiteScoreMiss2 + "",
          (int)
              (posx
                  + 28 * width / 72
                  - g.getFontMetrics().stringWidth(parse1WhiteScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss2 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 32 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss3 / parse1maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss3 / parse1maxHeight)));

      g.drawString(
          parse1WhiteScoreMiss3 + "",
          (int)
              (posx
                  + 33 * width / 72
                  - g.getFontMetrics().stringWidth(parse1WhiteScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse1WhiteScoreMiss3 / parse1maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 40 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss1 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss1 / parse2maxHeight)));

      g.drawString(
          parse2WhiteScoreMiss1 + "",
          (int)
              (posx
                  + 41 * width / 72
                  - g.getFontMetrics().stringWidth(parse2WhiteScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss1 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 45 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss2 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss2 / parse2maxHeight)));

      g.drawString(
          parse2WhiteScoreMiss2 + "",
          (int)
              (posx
                  + 46 * width / 72
                  - g.getFontMetrics().stringWidth(parse2WhiteScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss2 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 50 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss3 / parse2maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss3 / parse2maxHeight)));

      g.drawString(
          parse2WhiteScoreMiss3 + "",
          (int)
              (posx
                  + 51 * width / 72
                  - g.getFontMetrics().stringWidth(parse2WhiteScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse2WhiteScoreMiss3 / parse2maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 58 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss1 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss1 / parse3maxHeight)));

      g.drawString(
          parse3WhiteScoreMiss1 + "",
          (int)
              (posx
                  + 59 * width / 72
                  - g.getFontMetrics().stringWidth(parse3WhiteScoreMiss1 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss1 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 63 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss2 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss2 / parse3maxHeight)));

      g.drawString(
          parse3WhiteScoreMiss2 + "",
          (int)
              (posx
                  + 64 * width / 72
                  - g.getFontMetrics().stringWidth(parse3WhiteScoreMiss2 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss2 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);

      g.fillRect(
          posx + 68 * width / 72,
          y - (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss3 / parse3maxHeight)),
          width / 36,
          (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss3 / parse3maxHeight)));

      g.drawString(
          parse3WhiteScoreMiss3 + "",
          (int)
              (posx
                  + 69 * width / 72
                  - g.getFontMetrics().stringWidth(parse3WhiteScoreMiss3 + "") / 2),
          y
              - (int) (height * 4 / 7 * ((double) parse3WhiteScoreMiss3 / parse3maxHeight))
              - g.getFontMetrics().getHeight() / 4);
    }

    if (selectedIndex == 4) {
      double percentBlackParse1 =
          parse1BlackAnalyzed > 0 ? parse1BlackValue / parse1BlackAnalyzed : 0;
      double percentBlackParse2 =
          parse2BlackAnalyzed > 0 ? parse2BlackValue / parse2BlackAnalyzed : 0;
      double percentBlackParse3 =
          parse3BlackAnalyzed > 0 ? parse3BlackValue / parse3BlackAnalyzed : 0;
      int blackAllAnalyzed = parse1BlackAnalyzed + parse2BlackAnalyzed + parse3BlackAnalyzed;
      double percentBlackAll =
          blackAllAnalyzed > 0
              ? (parse1BlackValue + parse2BlackValue + parse3BlackValue) / blackAllAnalyzed
              : 0;

      double percentWhiteParse1 =
          parse1WhiteAnalyzed > 0 ? parse1WhiteValue / parse1WhiteAnalyzed : 0;
      double percentWhiteParse2 =
          parse2WhiteAnalyzed > 0 ? parse2WhiteValue / parse2WhiteAnalyzed : 0;
      double percentWhiteParse3 =
          parse3WhiteAnalyzed > 0 ? parse3WhiteValue / parse3WhiteAnalyzed : 0;

      int whiteAllAnalyzed = parse1WhiteAnalyzed + parse2WhiteAnalyzed + parse3WhiteAnalyzed;
      double percentWhiteAll =
          whiteAllAnalyzed > 0
              ? (parse1WhiteValue + parse2WhiteValue + parse3WhiteValue) / whiteAllAnalyzed
              : 0;

      g.setFont(new Font(Lizzie.config.fontName, Font.BOLD, 12));
      g.setColor(Color.BLACK);
      //  posx = width / 14;

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.all"),
          (int)
              (posx
                  + 2 * width / 16
                  - g.getFontMetrics()
                          .stringWidth(Lizzie.resourceBundle.getString("Movelistframe.all"))
                      / 2),
          posy + height * 4 / 5 + 15);

      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.open") + "(1-" + parse1Move + ")",
          (int)
              (posx
                  + 6 * width / 16
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.open")
                                  + "(1-"
                                  + parse1Move
                                  + ")")
                      / 2),
          posy + height * 4 / 5 + 15);
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.middle")
              + "("
              + (parse1Move + 1)
              + "-"
              + parse2Move
              + ")",
          (int)
              (posx
                  + 10 * width / 16
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.middle")
                                  + "("
                                  + (parse1Move + 1)
                                  + "-"
                                  + parse2Move
                                  + ")")
                      / 2),
          posy + height * 4 / 5 + 15);
      g.drawString(
          Lizzie.resourceBundle.getString("Movelistframe.end")
              + "("
              + (parse2Move + 1)
              + Lizzie.resourceBundle.getString("Movelistframe.toEnd")
              + ")",
          (int)
              (posx
                  + 14 * width / 16
                  - g.getFontMetrics()
                          .stringWidth(
                              Lizzie.resourceBundle.getString("Movelistframe.end")
                                  + "("
                                  + (parse2Move + 1)
                                  + Lizzie.resourceBundle.getString("Movelistframe.toEnd")
                                  + ")")
                      / 2),
          posy + height * 4 / 5 + 15);

      if (blackAllAnalyzed > 0) {
        g.fillRect(
            posx + width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackAll > 0.75
                        ? ((percentBlackAll - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackAll * height * 4 / 5),
            width / 16,
            (int)
                (percentBlackAll > 0.75
                    ? ((percentBlackAll - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentBlackAll * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentBlackAll * 100),
            (int)
                (posx
                    + 1.5 * width / 16
                    - g.getFontMetrics().stringWidth(String.format("%.1f", percentBlackAll * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackAll > 0.75
                        ? ((percentBlackAll - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackAll * height * 4 / 5)
                - 3);
      }

      if (parse1BlackAnalyzed > 0) {
        g.fillRect(
            posx + 5 * width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackParse1 > 0.75
                        ? ((percentBlackParse1 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackParse1 * height * 4 / 5),
            width / 16,
            (int)
                (percentBlackParse1 > 0.75
                    ? ((percentBlackParse1 - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentBlackParse1 * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentBlackParse1 * 100),
            (int)
                (posx
                    + 5.5 * width / 16
                    - g.getFontMetrics()
                            .stringWidth(String.format("%.1f", percentBlackParse1 * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackParse1 > 0.75
                        ? ((percentBlackParse1 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackParse1 * height * 4 / 5)
                - 3);
      }

      if (parse2BlackAnalyzed > 0) {
        g.fillRect(
            posx + 9 * width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackParse2 > 0.75
                        ? ((percentBlackParse2 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackParse2 * height * 4 / 5),
            width / 16,
            (int)
                (percentBlackParse2 > 0.75
                    ? ((percentBlackParse2 - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentBlackParse2 * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentBlackParse2 * 100),
            (int)
                (posx
                    + 9.5 * width / 16
                    - g.getFontMetrics()
                            .stringWidth(String.format("%.1f", percentBlackParse2 * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackParse2 > 0.75
                        ? ((percentBlackParse2 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackParse2 * height * 4 / 5)
                - 3);
      }

      if (parse3BlackAnalyzed > 0) {
        g.fillRect(
            posx + 13 * width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackParse3 > 0.75
                        ? ((percentBlackParse3 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackParse3 * height * 4 / 5),
            width / 16,
            (int)
                (percentBlackParse3 > 0.75
                    ? ((percentBlackParse3 - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentBlackParse3 * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentBlackParse3 * 100),
            (int)
                (posx
                    + 13.5 * width / 16
                    - g.getFontMetrics()
                            .stringWidth(String.format("%.1f", percentBlackParse3 * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentBlackParse3 > 0.75
                        ? ((percentBlackParse3 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentBlackParse3 * height * 4 / 5)
                - 3);
      }

      g.setColor(Color.WHITE);

      if (whiteAllAnalyzed > 0) {
        g.fillRect(
            posx + 2 * width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteAll > 0.75
                        ? ((percentWhiteAll - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteAll * height * 4 / 5),
            width / 16,
            (int)
                (percentWhiteAll > 0.75
                    ? ((percentWhiteAll - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentWhiteAll * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentWhiteAll * 100),
            (int)
                (posx
                    + 2.5 * width / 16
                    - g.getFontMetrics().stringWidth(String.format("%.1f", percentWhiteAll * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteAll > 0.75
                        ? ((percentWhiteAll - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteAll * height * 4 / 5)
                - 3);
      }

      if (parse1WhiteAnalyzed > 0) {
        g.fillRect(
            posx + 6 * width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteParse1 > 0.75
                        ? ((percentWhiteParse1 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteParse1 * height * 4 / 5),
            width / 16,
            (int)
                (percentWhiteParse1 > 0.75
                    ? ((percentWhiteParse1 - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentWhiteParse1 * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentWhiteParse1 * 100),
            (int)
                (posx
                    + 6.5 * width / 16
                    - g.getFontMetrics()
                            .stringWidth(String.format("%.1f", percentWhiteParse1 * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteParse1 > 0.75
                        ? ((percentWhiteParse1 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteParse1 * height * 4 / 5)
                - 3);
      }

      if (parse2WhiteAnalyzed > 0) {
        g.fillRect(
            posx + 10 * width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteParse2 > 0.75
                        ? ((percentWhiteParse2 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteParse2 * height * 4 / 5),
            width / 16,
            (int)
                (percentWhiteParse2 > 0.75
                    ? ((percentWhiteParse2 - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentWhiteParse2 * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentWhiteParse2 * 100),
            (int)
                (posx
                    + 10.5 * width / 16
                    - g.getFontMetrics()
                            .stringWidth(String.format("%.1f", percentWhiteParse2 * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteParse2 > 0.75
                        ? ((percentWhiteParse2 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteParse2 * height * 4 / 5)
                - 3);
      }
      if (parse3WhiteAnalyzed > 0) {
        g.fillRect(
            posx + 14 * width / 16,
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteParse3 > 0.75
                        ? ((percentWhiteParse3 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteParse3 * height * 4 / 5),
            width / 16,
            (int)
                (percentWhiteParse3 > 0.75
                    ? ((percentWhiteParse3 - 0.75) * height * 4 / 7 + height * 3 / 5)
                    : percentWhiteParse3 * height * 4 / 5));
        g.drawString(
            String.format("%.1f", percentWhiteParse3 * 100),
            (int)
                (posx
                    + 14.5 * width / 16
                    - g.getFontMetrics()
                            .stringWidth(String.format("%.1f", percentWhiteParse3 * 100))
                        / 2),
            posy
                + height * 4 / 5
                - (int)
                    (percentWhiteParse3 > 0.75
                        ? ((percentWhiteParse3 - 0.75) * height * 4 / 7 + height * 3 / 5)
                        : percentWhiteParse3 * height * 4 / 5)
                - 3);
      }

      return;
    }

    g.setStroke(new BasicStroke(2));

    // Optional<BoardHistoryNode> topOfVariation = Optional.empty();
    int numMoves = 0;
    if (selectedIndex != 2 && selectedIndex != 3) numMoves = 50;
    int trueNumMoves = 0;
    if (showBranch.getSelectedIndex() == 0 && !curMove.isMainTrunk()) {
      // We're in a variation, need to draw both main trunk and variation
      // Find top of variation
      node = Lizzie.board.getHistory().getMainEnd();
      curMove = Lizzie.board.getHistory().getMainEnd();
      // topOfVariation = Optional.of(top);
      // Find depth of main trunk, need this for plot scaling
      //   numMoves = top.getDepth() + top.getData().moveNumber - 1;
      // g.setStroke(dashed);
    }

    // Go to end of variation and work our way backwards to the root
    if (selectedIndex == 2 || selectedIndex == 3) {
      if (!node.next().isPresent() && node.previous().isPresent()) {
        node = node.previous().get();
      }
      while (node.next().isPresent()
          && node.next().get().next().isPresent()
          && node.getData().moveNumber <= (Lizzie.board.matchAiLastMove + 1)) {
        node = node.next().get();
      }
      while (node.previous().isPresent()
          && node.getData().moveNumber > (Lizzie.board.matchAiLastMove + 1)) {
        node = node.previous().get();
      }

      trueNumMoves =
          node.getData().moveNumber
              - ((Lizzie.board.matchAiFirstMove - 1) > 0 ? (Lizzie.board.matchAiFirstMove - 1) : 0);
    } else {
      while (node.next().isPresent()) {
        node = node.next().get();
      }

      trueNumMoves = node.getData().moveNumber - 1;
    }
    if (numMoves < trueNumMoves) {
      numMoves = trueNumMoves;
    }

    boolean lastNodeAnalyzed =
        isMainEngine ? node.getData().getPlayouts() > 0 : node.getData().getPlayouts2() > 0;
    int lastNodeMove = node.getData().moveNumber - 1;
    if (trueNumMoves < 1) return;
    BoardHistoryNode firstNode = Lizzie.board.getHistory().getStart().next().get();
    boolean firstNodeAnalyzed =
        isMainEngine
            ? firstNode.getData().getPlayouts() > 0
            : firstNode.getData().getPlayouts2() > 0;
    // Plot
    if (selectedIndex == 0) width = width - 30;
    else width = width - 35; // Leave some space after last move
    double lastWr = 50;
    // double lastWr2 = 50;
    boolean lastNodeOk = false;
    // boolean inFirstPath = true;
    int movenum = node.getData().moveNumber - 1;
    if (selectedIndex == 2 || selectedIndex == 3)
      movenum =
          movenum
              - ((Lizzie.board.matchAiFirstMove - 1) > 0 ? (Lizzie.board.matchAiFirstMove - 1) : 0);
    int lastOkMove = -1;
    int lastOkMoveSave = -1;
    double lastWrSave = 50;
    double lastMatchValueB = 0;
    double lastMatchValueW = 0;

    double cwr = -1;
    String moveNumString = "";
    int cmovenum = -1;
    double cScore = -500;
    int mmovenum = -1;
    double mwr = -1;
    double mScore = -500;

    int cmovenumCase2 = -1;
    double cdiffwinCase2 = 0;
    double cConvertHeight = 0;
    if (selectedIndex == 7) {
      List<bigMistakeInfo> blackBigMistakeList = new ArrayList<bigMistakeInfo>();
      List<bigMistakeInfo> whiteBigMistakeList = new ArrayList<bigMistakeInfo>();
      List<bigMistakeInfo> AllBigMistakeList = new ArrayList<bigMistakeInfo>();
      while (node.previous().isPresent()
          && (node.getData().moveNumber + 2 > Lizzie.board.matchAiFirstMove)) {
        NodeInfo nodeInfo =
            showBranch.getSelectedIndex() == 0
                ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
                : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
        if (nodeInfo.analyzed) {
          bigMistakeInfo newBigMistakeInfo = new bigMistakeInfo();
          newBigMistakeInfo.moveNumber = nodeInfo.moveNum;
          // Double winRate = isMainEngine ? node.getData().winrate : node.getData().winrate2;
          newBigMistakeInfo.currentMoveWinRate = nodeInfo.winrate;
          newBigMistakeInfo.isBlack = nodeInfo.isBlack;
          newBigMistakeInfo.diffWinrate = nodeInfo.diffWinrate;
          newBigMistakeInfo.coords = nodeInfo.coords;
          if (nodeInfo.isBlack) {
            blackBigMistakeList.add(newBigMistakeInfo);
          } else {
            whiteBigMistakeList.add(newBigMistakeInfo);
          }
        }
        node = node.previous().get();
      }
      Collections.sort(
          blackBigMistakeList,
          new Comparator<bigMistakeInfo>() {
            @Override
            public int compare(bigMistakeInfo s1, bigMistakeInfo s2) {
              if (s1.diffWinrate > s2.diffWinrate) return 1;
              else return -1;
            }
          });
      Collections.sort(
          whiteBigMistakeList,
          new Comparator<bigMistakeInfo>() {
            @Override
            public int compare(bigMistakeInfo s1, bigMistakeInfo s2) {
              if (s1.diffWinrate > s2.diffWinrate) return 1;
              else return -1;
            }
          });
      if (blackBigMistakeList.size() > 5) blackBigMistakeList = blackBigMistakeList.subList(0, 5);
      if (whiteBigMistakeList.size() > 5) whiteBigMistakeList = whiteBigMistakeList.subList(0, 5);
      AllBigMistakeList.addAll(blackBigMistakeList);
      AllBigMistakeList.addAll(whiteBigMistakeList);
      Collections.sort(
          AllBigMistakeList,
          new Comparator<bigMistakeInfo>() {
            @Override
            public int compare(bigMistakeInfo s1, bigMistakeInfo s2) {
              if (s1.moveNumber > s2.moveNumber) return 1;
              else return -1;
            }
          });
      BigMistakeList = AllBigMistakeList;

      FontMetrics fm = g.getFontMetrics(new Font("", Font.PLAIN, Config.frameFontSize - 2));
      g.setColor(Color.WHITE);
      int heightFont = fm.getAscent() - fm.getDescent();
      g.setStroke(new BasicStroke(1));
      g.drawLine(0, height - heightFont - 10, width, height - heightFont - 10);
      for (int i = 0; i < BigMistakeList.size(); i++)
        drawOneBigMistakePoint(g, width, height, BigMistakeList.get(i), i, false);
    } else if (selectedIndex == 8) {
      List<bigMistakeInfo> blackBigMistakeList = new ArrayList<bigMistakeInfo>();
      List<bigMistakeInfo> whiteBigMistakeList = new ArrayList<bigMistakeInfo>();
      List<bigMistakeInfo> AllBigMistakeList = new ArrayList<bigMistakeInfo>();
      while (node.previous().isPresent()
          && (node.getData().moveNumber + 2 > Lizzie.board.matchAiFirstMove)) {
        NodeInfo nodeInfo =
            showBranch.getSelectedIndex() == 0
                ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
                : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
        if (nodeInfo.analyzed) {
          bigMistakeInfo newBigMistakeInfo = new bigMistakeInfo();
          newBigMistakeInfo.moveNumber = nodeInfo.moveNum;
          // Double winRate = isMainEngine ? node.getData().winrate : node.getData().winrate2;
          newBigMistakeInfo.currentMoveWinRate = nodeInfo.winrate;
          newBigMistakeInfo.isBlack = nodeInfo.isBlack;
          newBigMistakeInfo.diffWinrate = nodeInfo.scoreMeanDiff;
          newBigMistakeInfo.coords = nodeInfo.coords;
          if (nodeInfo.isBlack) {
            blackBigMistakeList.add(newBigMistakeInfo);
          } else {
            whiteBigMistakeList.add(newBigMistakeInfo);
          }
        }
        node = node.previous().get();
      }
      Collections.sort(
          blackBigMistakeList,
          new Comparator<bigMistakeInfo>() {
            @Override
            public int compare(bigMistakeInfo s1, bigMistakeInfo s2) {
              if (s1.diffWinrate > s2.diffWinrate) return 1;
              else return -1;
            }
          });
      Collections.sort(
          whiteBigMistakeList,
          new Comparator<bigMistakeInfo>() {
            @Override
            public int compare(bigMistakeInfo s1, bigMistakeInfo s2) {
              if (s1.diffWinrate > s2.diffWinrate) return 1;
              else return -1;
            }
          });
      if (blackBigMistakeList.size() > 5) blackBigMistakeList = blackBigMistakeList.subList(0, 5);
      if (whiteBigMistakeList.size() > 5) whiteBigMistakeList = whiteBigMistakeList.subList(0, 5);
      AllBigMistakeList.addAll(blackBigMistakeList);
      AllBigMistakeList.addAll(whiteBigMistakeList);
      Collections.sort(
          AllBigMistakeList,
          new Comparator<bigMistakeInfo>() {
            @Override
            public int compare(bigMistakeInfo s1, bigMistakeInfo s2) {
              if (s1.moveNumber > s2.moveNumber) return 1;
              else return -1;
            }
          });
      BigMistakeList = AllBigMistakeList;

      FontMetrics fm = g.getFontMetrics(new Font("", Font.PLAIN, Config.frameFontSize - 2));
      g.setColor(Color.WHITE);
      int heightFont = fm.getAscent() - fm.getDescent();
      g.setStroke(new BasicStroke(1));
      g.drawLine(0, height - heightFont - 10, width, height - heightFont - 10);
      for (int i = 0; i < BigMistakeList.size(); i++)
        drawOneBigMistakePoint(g, width, height, BigMistakeList.get(i), i, true);
    } else {
      while (node.previous().isPresent()
          && (selectedIndex != 2 && selectedIndex != 3
              || ((selectedIndex == 2 || selectedIndex == 3)
                  && ((node.getData().moveNumber + 2) > Lizzie.board.matchAiFirstMove)))) {
        double wr = isMainEngine ? node.getData().winrate : node.getData().winrate2;
        boolean firstOk = true;
        int playouts = isMainEngine ? node.getData().getPlayouts() : node.getData().getPlayouts2();
        switch (selectedIndex) {
          case 0:
            if (playouts > 0) {
              if (wr < 0) {
                wr = 100 - lastWr;
              } else if (!node.getData().blackToPlay) {
                wr = 100 - wr;
              }
              NodeInfo nodeInfo =
                  showBranch.getSelectedIndex() == 0
                      ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
                      : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
              if (lastOkMove > 0) {
                if (node.getData().moveNumber <= Lizzie.board.matchAiLastMove
                    && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove) {
                  if (nodeInfo.isMatchAi) {
                    int lostMoves = lastOkMove - movenum;
                    if (checkBlack.isSelected()
                        && (!node.getData().lastMoveColor.equals(Stone.BLACK)
                            || (node.getData().lastMoveColor.equals(Stone.BLACK)
                                && nodeInfo.isMatchAi))) {
                      g.setColor(new Color(0, 0, 255, 100));
                      if (lostMoves == 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy + height - (int) (convertWinrate(lastWr) * height / 100),
                          origParams[3],
                          origParams[3],
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      } else if (lostMoves > 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy
                              + height
                              - (int) (convertWinrate(lastWr) * height / 100)
                              + ((int) (convertWinrate(lastWr) * height / 100)
                                      - (int) (convertWinrate(wr) * height / 100))
                                  * (lostMoves - 1)
                                  / lostMoves,
                          origParams[3],
                          origParams[3],
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      }
                    }
                    if (checkWhite.isSelected()
                        && (node.getData().lastMoveColor.equals(Stone.BLACK)
                            || (!node.getData().lastMoveColor.equals(Stone.BLACK)
                                && nodeInfo.isMatchAi))) {
                      g.setColor(new Color(0, 255, 0, 100));
                      if (lostMoves == 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy + height - (int) (convertWinrate(lastWr) * height / 100),
                          0,
                          0,
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      } else if (lostMoves > 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy
                              + height
                              - (int) (convertWinrate(lastWr) * height / 100)
                              + ((int) (convertWinrate(lastWr) * height / 100)
                                      - (int) (convertWinrate(wr) * height / 100))
                                  * (lostMoves - 1)
                                  / lostMoves,
                          0,
                          0,
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      }
                    }
                  } else if (showBranch.getSelectedIndex() == 0
                      ? node.previous().get().nodeInfoMain.isMatchAi
                      : node.previous().get().nodeInfo.isMatchAi) {
                    int lostMoves = lastOkMove - movenum;
                    if (checkBlack.isSelected()
                        && (node.getData().lastMoveColor.equals(Stone.BLACK))) {
                      g.setColor(new Color(0, 0, 255, 100));
                      if (lostMoves == 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy + height - (int) (convertWinrate(lastWr) * height / 100),
                          origParams[3],
                          origParams[3],
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      } else if (lostMoves > 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy
                              + height
                              - (int) (convertWinrate(lastWr) * height / 100)
                              + ((int) (convertWinrate(lastWr) * height / 100)
                                      - (int) (convertWinrate(wr) * height / 100))
                                  * (lostMoves - 1)
                                  / lostMoves,
                          origParams[3],
                          origParams[3],
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      }
                    }
                    if (checkWhite.isSelected()
                        && (!node.getData().lastMoveColor.equals(Stone.BLACK))) {
                      g.setColor(new Color(0, 255, 0, 100));
                      if (lostMoves == 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy + height - (int) (convertWinrate(lastWr) * height / 100),
                          0,
                          0,
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      } else if (lostMoves > 1) {
                        int[] xPoints = {
                          posx + ((movenum + 1) * width / numMoves),
                          posx + ((movenum + 1) * width / numMoves),
                          posx + (movenum * width / numMoves),
                          posx + (movenum * width / numMoves)
                        };
                        int[] yPoints = {
                          posy
                              + height
                              - (int) (convertWinrate(lastWr) * height / 100)
                              + ((int) (convertWinrate(lastWr) * height / 100)
                                      - (int) (convertWinrate(wr) * height / 100))
                                  * (lostMoves - 1)
                                  / lostMoves,
                          0,
                          0,
                          posy + height - (int) (convertWinrate(wr) * height / 100)
                        };
                        g.fillPolygon(xPoints, yPoints, 4);
                      }
                    }
                  }
                }

                if (lastNodeOk) {
                  if (node.getData().moveNumber <= Lizzie.board.matchAiLastMove
                      && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove)
                    g.setColor(Color.CYAN);
                  else g.setColor(Color.ORANGE);
                  g.drawLine(
                      posx + (lastOkMove * width / numMoves),
                      posy + height - (int) (convertWinrate(lastWr) * height / 100),
                      posx + (movenum * width / numMoves),
                      posy + height - (int) (convertWinrate(wr) * height / 100));
                } else {
                  g.setColor(Color.ORANGE);
                  int lostMoves = lastOkMove - movenum;
                  if (Math.abs(movenum - lastOkMove) < 35)
                    g.drawLine(
                        posx + (lastOkMove * width / numMoves),
                        posy + height - (int) (convertWinrate(lastWr) * height / 100),
                        posx + ((movenum + 1) * width / numMoves),
                        posy + height - (int) ((wr - (wr - lastWr) / lostMoves) * height / 100));
                  if (node.getData().moveNumber <= Lizzie.board.matchAiLastMove
                      && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove)
                    g.setColor(Color.CYAN);
                  else g.setColor(Color.ORANGE);
                  g.drawLine(
                      posx + ((movenum + 1) * width / numMoves),
                      posy
                          + height
                          - (int) (convertWinrate(lastWr) * height / 100)
                          + ((int) (convertWinrate(lastWr) * height / 100)
                                  - (int) (convertWinrate(wr) * height / 100))
                              * (lostMoves - 1)
                              / lostMoves,
                      posx + (movenum * width / numMoves),
                      posy + height - (int) (convertWinrate(wr) * height / 100));
                }

              } else if (firstOk) {
                firstOk = false;
                if (!lastNodeAnalyzed) {
                  if (node.getData().moveNumber <= Lizzie.board.matchAiLastMove
                      && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove) {
                    if (node.nodeInfo.isMatchAi) {

                      int lostMoves = lastNodeMove - movenum;
                      if (checkBlack.isSelected()
                          && (!node.getData().lastMoveColor.equals(Stone.BLACK)
                              || (node.getData().lastMoveColor.equals(Stone.BLACK)
                                  && node.previous().get().nodeInfo.isMatchAi)
                              || node.getData().moveNumber == 1)) {
                        g.setColor(new Color(0, 0, 255, 100));
                        if (lostMoves == 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy + height - (int) (convertWinrate(wr) * height / 100),
                            origParams[3],
                            origParams[3],
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        } else if (lostMoves > 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy + height - (int) (convertWinrate(wr) * height / 100),
                            origParams[3],
                            origParams[3],
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        }
                      }
                      if (checkWhite.isSelected()
                          && (node.getData().lastMoveColor.equals(Stone.BLACK)
                              || (!node.getData().lastMoveColor.equals(Stone.BLACK)
                                  && node.previous().get().nodeInfo.isMatchAi))) {
                        g.setColor(new Color(0, 255, 0, 100));
                        if (lostMoves == 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy + height - (int) (convertWinrate(wr) * height / 100),
                            0,
                            0,
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        } else if (lostMoves > 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy + height - (int) (convertWinrate(wr) * height / 100),
                            0,
                            0,
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        }
                      }
                    } else if (node.previous().get().nodeInfo.isMatchAi) {
                      int lostMoves = lastOkMove - movenum;
                      if (checkBlack.isSelected()
                          && (node.getData().lastMoveColor.equals(Stone.BLACK)
                              || node.getData().moveNumber == 1)) {
                        g.setColor(new Color(0, 0, 255, 120));
                        if (lostMoves == 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy + height - (int) (convertWinrate(lastWr) * height / 100),
                            origParams[3],
                            origParams[3],
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        } else if (lostMoves > 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy
                                + height
                                - (int) (convertWinrate(lastWr) * height / 100)
                                + ((int) (convertWinrate(lastWr) * height / 100)
                                        - (int) (convertWinrate(wr) * height / 100))
                                    * (lostMoves - 1)
                                    / lostMoves,
                            origParams[3],
                            origParams[3],
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        }
                      }
                      if (checkWhite.isSelected()
                          && (!node.getData().lastMoveColor.equals(Stone.BLACK))) {
                        g.setColor(new Color(0, 255, 0, 120));
                        if (lostMoves == 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy + height - (int) (convertWinrate(lastWr) * height / 100),
                            0,
                            0,
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        } else if (lostMoves > 1) {
                          int[] xPoints = {
                            posx + ((movenum + 1) * width / numMoves),
                            posx + ((movenum + 1) * width / numMoves),
                            posx + (movenum * width / numMoves),
                            posx + (movenum * width / numMoves)
                          };
                          int[] yPoints = {
                            posy
                                + height
                                - (int) (convertWinrate(lastWr) * height / 100)
                                + ((int) (convertWinrate(lastWr) * height / 100)
                                        - (int) (convertWinrate(wr) * height / 100))
                                    * (lostMoves - 1)
                                    / lostMoves,
                            0,
                            0,
                            posy + height - (int) (convertWinrate(wr) * height / 100)
                          };
                          g.fillPolygon(xPoints, yPoints, 4);
                        }
                      }
                    }
                  }
                  g.setColor(Color.ORANGE);
                  g.drawLine(
                      posx + (lastNodeMove * width / numMoves),
                      posy + height - (int) (convertWinrate(wr) * height / 100),
                      posx + ((movenum + 1) * width / numMoves),
                      posy + height - (int) (convertWinrate(wr) * height / 100));
                  if (node.getData().moveNumber <= Lizzie.board.matchAiLastMove
                      && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove)
                    g.setColor(Color.CYAN);
                  else g.setColor(Color.ORANGE);
                  int lostMoves = lastOkMove - movenum;
                  g.drawLine(
                      posx + ((movenum + 1) * width / numMoves),
                      posy + height - (int) (convertWinrate(wr) * height / 100),
                      posx + (movenum * width / numMoves),
                      posy + height - (int) (convertWinrate(wr) * height / 100));
                  cwr = wr;
                  cmovenum = -1;
                }
              }

              if (node == curMove) {
                cwr = wr;
                cmovenum = movenum;
              } else if (node == curMouseOverNode) {
                mwr = wr;
                mmovenum = movenum;
              }
              lastWr = wr;
              lastNodeOk = true;
              // Check if we were in a variation and has reached the main trunk
              //        if (topOfVariation.isPresent() && topOfVariation.get() == node) {
              //          // Reached top of variation, go to end of main trunk before continuing
              //          while (node.next().isPresent()) {
              //            node = node.next().get();
              //          }
              //          movenum = node.getData().moveNumber - 1;
              //          lastWr = node.getData().winrate;
              //          if (!node.getData().blackToPlay) lastWr = 100 - lastWr;
              //          g.setStroke(new BasicStroke(3));
              //          topOfVariation = Optional.empty();
              //          if (node.getData().getPlayouts() == 0) {
              //            lastNodeOk = false;
              //          }
              //          inFirstPath = false;
              //        }
              lastOkMove = lastNodeOk ? movenum : -1;
            } else {
              lastNodeOk = false;
            }
            if (lastOkMove > 0 && !firstNodeAnalyzed) {
              lastOkMoveSave = lastOkMove;
              lastWrSave = lastWr;
            }
            break;
          case 1:
            //  g.setStroke(new BasicStroke(1));

            double matchValue = getMatchValue(node);
            NodeInfo nodeinfo =
                showBranch.getSelectedIndex() == 0
                    ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
                    : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
            double lastMatchValue = 0;
            if (analyzed >= 1) {
              if ((node.getData().moveNumber <= Lizzie.board.matchAiLastMove
                      && (node.getData().moveNumber + 1) > Lizzie.board.matchAiFirstMove)
                  && nodeinfo.analyzed) {
                if (node.getData().blackToPlay) {
                  g.setColor(new Color(0, 0, 0, 200));
                  lastMatchValue = lastMatchValueB;
                } else {
                  g.setColor(new Color(255, 255, 255, 200));
                  lastMatchValue = lastMatchValueW;
                }
              } else
                g.setColor(
                    new Color(
                        Color.ORANGE.getRed(),
                        Color.ORANGE.getGreen(),
                        Color.ORANGE.getBlue(),
                        200));
              if (node != lastMove) {
                g.drawLine(
                    posx + ((movenum + 2) * width / numMoves),
                    posy
                        + height
                        - Math.max(
                            ((int)
                                    ((lastMatchValue > 0 ? lastMatchValue : matchValue)
                                        * height
                                        / 100)
                                - 20),
                            0),
                    posx + (movenum * width / numMoves),
                    posy + height - Math.max(((int) (matchValue * height / 100) - 20), 0));
              }
            }
            if (!Double.isNaN(blackMatchValue)) lastMatchValueB = blackMatchValue;
            if (!Double.isNaN(whiteMatchValue)) lastMatchValueW = whiteMatchValue;

            if (node == curMove) {
              Stroke preStroke = g.getStroke();
              int x = posx + ((curMove.getData().moveNumber - 1) * width / numMoves);
              moveNumString = "" + curMove.getData().moveNumber;
              g.setStroke(dashed);
              g.setColor(Color.YELLOW);
              g.drawLine(x, 0, x, posy + origParams[3]);
              g.setColor(Color.BLACK);
              // g.setFont(new Font("", Font.BOLD, 10));
              g.drawString(moveNumString, x + 3, posy + this.origParams[3] - 10);
              g.setStroke(preStroke);
              g.setFont(new Font(Lizzie.config.fontName, Font.BOLD, 15));
              if (lastMatchValueB >= lastMatchValueW) {
                g.setColor(Color.BLACK);
                if (analyzedB >= 1)
                  g.drawString(
                      String.format("%.1f", lastMatchValueB),
                      posx + (movenum * width / numMoves) - 5,
                      posy
                          + height
                          - Math.max(
                              ((int) (Math.max(lastMatchValueB, lastMatchValueW) * height / 100)
                                      - 20)
                                  + 4,
                              0));
                g.setColor(Color.WHITE);
                if (analyzedW >= 1)
                  g.drawString(
                      String.format("%.1f", lastMatchValueW),
                      posx + (movenum * width / numMoves) - 5,
                      posy
                          + height
                          - Math.max(
                              ((int) (Math.min(lastMatchValueB, lastMatchValueW) * height / 100)
                                      - 20)
                                  - 16,
                              0));
              } else {
                g.setColor(Color.BLACK);
                if (analyzedB >= 1)
                  g.drawString(
                      String.format("%.1f", lastMatchValueB),
                      posx + (movenum * width / numMoves) - 5,
                      posy
                          + height
                          - Math.max(
                              ((int) (Math.min(lastMatchValueB, lastMatchValueW) * height / 100)
                                      - 20)
                                  - 16,
                              0));
                g.setColor(Color.WHITE);
                if (analyzedW >= 1)
                  g.drawString(
                      String.format("" + "%.1f", lastMatchValueW),
                      posx + (movenum * width / numMoves) - 5,
                      posy
                          + height
                          - Math.max(
                              ((int) (Math.max(lastMatchValueB, lastMatchValueW) * height / 100)
                                      - 20)
                                  + 4,
                              0));
              }
            }
            break;

          case 2:
            boolean draw = false;
            boolean drawSingle = checkBlack.isSelected() != checkWhite.isSelected();
            NodeInfo nodeinfoW =
                showBranch.getSelectedIndex() == 0
                    ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
                    : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
            double diffWinrate = nodeinfoW.diffWinrate;
            double convertHeight = 0;
            // if(Lizzie.board.isPkBoard)
            //		diffWinrate=-diffWinrate;
            if (nodeinfoW.analyzed && Math.abs(diffWinrate) > 0) {
              if (node.getData().blackToPlay && checkBlack.isSelected()) {
                g.setColor(Color.BLACK);
                draw = true;
              } else if (!node.getData().blackToPlay && checkWhite.isSelected()) {
                g.setColor(Color.WHITE);
                draw = true;
              }

              if (draw) {
                convertHeight = 0;
                if (diffWinrate < -50) {
                  convertHeight = -(4 / 8.0 + (Math.abs(diffWinrate) - 50) / (8 * 50.0)) * 100.0;
                } else if (diffWinrate < -20) {
                  convertHeight = -(3 / 8.0 + (Math.abs(diffWinrate) - 20) / (8 * 30.0)) * 100.0;
                } else if (diffWinrate < -10) {
                  convertHeight = -(2 / 8.0 + (Math.abs(diffWinrate) - 10) / (8 * 10.0)) * 100.0;
                } else if (diffWinrate < -5) {
                  convertHeight = -(1 / 8.0 + (Math.abs(diffWinrate) - 5) / (8 * 5.0)) * 100.0;
                } else if (diffWinrate < 0) {
                  convertHeight = -((Math.abs(diffWinrate)) / (8 * 5.0)) * 100.0;
                } else if (diffWinrate < 5) {
                  convertHeight = (Math.abs(diffWinrate)) / (8 * 5.0) * 100.0;
                } else if (diffWinrate < 20) {
                  convertHeight =
                      1 / 8.0 * 100 + (Math.abs(diffWinrate) - 5.0) / (8 * 15.0) * 100.0;
                } else {
                  convertHeight =
                      2 / 8.0 * 100 + (Math.abs(diffWinrate) - 20.0) / (8.0 * 80) * 100.0;
                }
                if (diffWinrate > 0)
                  g.fillRect(
                      posx + (int) ((movenum + (drawSingle ? 0.5 : 1)) * width / numMoves),
                      posy + height - (int) (height * 3 / 8),
                      (drawSingle ? 2 : 1) * width / numMoves - (drawSingle ? 1 : 0),
                      (int) ((convertHeight) * height / 100));
                else
                  g.fillRect(
                      posx + (int) ((movenum + (drawSingle ? 0.5 : 1)) * width / numMoves),
                      (int)
                          (posy + height - (int) (height * 3 / 8) + (convertHeight) * height / 100),
                      (drawSingle ? 2 : 1) * width / numMoves - (drawSingle ? 1 : 0),
                      (int) (Math.abs(convertHeight) * height / 100));
              }
            }
            if (curMove.previous().isPresent() && node == curMove.previous().get()) {
              int x = posx + (int) ((movenum + 1.5) * width / numMoves);
              g.setStroke(dashed);
              g.setColor(Color.YELLOW);
              g.drawLine(x, 0, x, posy + origParams[3]);
            }
            if (node.next().isPresent() && node.next().get() == curMove) {
              if (draw) {
                cmovenumCase2 = movenum;
                cdiffwinCase2 = diffWinrate;
                cConvertHeight = convertHeight;
              }
            }
            break;
          case 3:
            boolean draw3 = false;
            boolean drawSingle3 = checkBlack.isSelected() != checkWhite.isSelected();
            NodeInfo nodeinfoW3 =
                showBranch.getSelectedIndex() == 0
                    ? (isMainEngine ? node.nodeInfoMain : node.nodeInfoMain2)
                    : (isMainEngine ? node.nodeInfo : node.nodeInfo2);
            double diffScore = nodeinfoW3.scoreMeanDiff;
            double convertScoreHeight = 0;
            // if(Lizzie.board.isPkBoard)
            //		diffWinrate=-diffWinrate;
            if (nodeinfoW3.analyzed && Math.abs(diffScore) > 0) {
              if (node.getData().blackToPlay && checkBlack.isSelected()) {
                g.setColor(Color.BLACK);
                draw3 = true;
              } else if (!node.getData().blackToPlay && checkWhite.isSelected()) {
                g.setColor(Color.WHITE);
                draw3 = true;
              }
              if (draw3) {
                convertScoreHeight = 0;
                if (diffScore < -50) {
                  convertScoreHeight = -(4 / 8.0 + (Math.abs(diffScore) - 50) / (8 * 50.0)) * 100.0;
                } else if (diffScore < -20) {
                  convertScoreHeight = -(3 / 8.0 + (Math.abs(diffScore) - 20) / (8 * 30.0)) * 100.0;
                } else if (diffScore < -10) {
                  convertScoreHeight = -(2 / 8.0 + (Math.abs(diffScore) - 10) / (8 * 10.0)) * 100.0;
                } else if (diffScore < -5) {
                  convertScoreHeight = -(1 / 8.0 + (Math.abs(diffScore) - 5) / (8 * 5.0)) * 100.0;
                } else if (diffScore < 0) {
                  convertScoreHeight = -((Math.abs(diffScore)) / (8 * 5.0)) * 100.0;
                } else if (diffScore < 5) {
                  convertScoreHeight = (Math.abs(diffScore)) / (8 * 5.0) * 100.0;
                } else if (diffScore < 20) {
                  convertScoreHeight =
                      1 / 8.0 * 100 + (Math.abs(diffScore) - 5.0) / (8 * 15.0) * 100.0;
                } else {
                  convertScoreHeight =
                      2 / 8.0 * 100 + (Math.abs(diffScore) - 20.0) / (8.0 * 80) * 100.0;
                }
                if (convertScoreHeight > 100) convertScoreHeight = 100;
                if (convertScoreHeight < -100) convertScoreHeight = -100;
                if (diffScore > 0)
                  g.fillRect(
                      posx + (int) ((movenum + (drawSingle3 ? 0.5 : 1)) * width / numMoves),
                      posy + height - (int) (height * 3 / 8),
                      (drawSingle3 ? 2 : 1) * width / numMoves - (drawSingle3 ? 1 : 0),
                      (int) ((convertScoreHeight) * height / 100));
                else
                  g.fillRect(
                      posx + (int) ((movenum + (drawSingle3 ? 0.5 : 1)) * width / numMoves),
                      (int)
                          (posy
                              + height
                              - (int) (height * 3 / 8)
                              + (convertScoreHeight) * height / 100),
                      (drawSingle3 ? 2 : 1) * width / numMoves - (drawSingle3 ? 1 : 0),
                      (int) (Math.abs(convertScoreHeight) * height / 100));
              }
            }
            if (curMove.previous().isPresent() && node == curMove.previous().get()) {
              int x = posx + (int) ((movenum + 1.5) * width / numMoves);
              g.setStroke(dashed);
              g.setColor(Color.YELLOW);
              g.drawLine(x, 0, x, posy + origParams[3]);
            }
            if (node.next().isPresent() && node.next().get() == curMove) {
              if (draw3) {
                cmovenumCase2 = movenum;
                cdiffwinCase2 = diffScore;
                cConvertHeight = diffScore;
              }
            }
            break;
        }

        //
        //      g.drawLine(
        //              posx + ((movenum + 2) * width / numMoves),
        //              posy
        //                  + height
        //                  - Math.max(
        //                      ((int) ((lastMatchValue > 0 ? lastMatchValue : matchValue) * height
        // /
        // 100)
        //                          - 20),
        //                      0),
        //              posx + (movenum * width / numMoves),
        //              posy + height - Math.max(((int) (matchValue * height / 100) - 20), 0));
        //
        node = node.previous().get();
        movenum--;
      }
      g.setColor(Color.WHITE);
      g.setFont(new Font(Lizzie.config.uiFontName, Font.BOLD, 10));
      if (selectedIndex == 2 || selectedIndex == 3) {
        if (numMoves <= 100) {
          for (int i = 1; i <= (numMoves / 10); i++)
            g.drawString(
                (Lizzie.board.matchAiFirstMove > 0 ? Lizzie.board.matchAiFirstMove : 0)
                    + i * 10
                    + "",
                posx + (i * 10 - 1) * width / numMoves + 3,
                posy + this.origParams[3] - 5);
        } else {
          for (int i = 1; i <= (numMoves / 20); i++)
            g.drawString(
                (Lizzie.board.matchAiFirstMove > 0 ? Lizzie.board.matchAiFirstMove : 0)
                    + i * 20
                    + "",
                posx + (i * 20 - 1) * width / numMoves + 3,
                posy + this.origParams[3] - 5);
        }
        int x =
            posx
                + ((curMove.getData().moveNumber
                        - 1
                        - ((Lizzie.board.matchAiFirstMove - 1) > 0
                            ? (Lizzie.board.matchAiFirstMove)
                            : 0))
                    * width
                    / numMoves);

        moveNumString = "" + curMove.getData().moveNumber;
        g.setColor(Color.BLACK);
        g.setFont(new Font("", Font.BOLD, 10));
        g.drawString(moveNumString, x + 3, posy + this.origParams[3] - 5);
        if (cmovenumCase2 > 0) {

          g.setFont(new Font(Lizzie.config.fontName, Font.BOLD, 13));
          int mw = g.getFontMetrics().stringWidth(String.format("%.1f", cdiffwinCase2));
          // int mh = g.getFontMetrics().getHeight();

          // if(cmovenumCase2<numMoves/2)
          // {
          g.setColor(Color.RED);
          if (cdiffwinCase2 <= 0) {
            g.drawString(
                String.format("%.1f", cdiffwinCase2),
                posx + (int) ((cmovenumCase2 + 1.5) * width / numMoves - mw / 2),
                (int) (posy + height + 13 - (int) (height * 3 / 8)));
          } else {
            {
              g.drawString(
                  "+" + String.format("%.1f", cdiffwinCase2),
                  posx + (int) ((cmovenumCase2 + 1.5) * width / numMoves - mw / 2),
                  (int)
                      (posy
                          + height
                          + 13
                          + cConvertHeight / 100 * height
                          - (int) (height * 3 / 8)));
            }
          }
        }
      } else if (selectedIndex < 4) {
        if (numMoves <= 100) {
          for (int i = 1; i <= (numMoves / 10); i++)
            g.drawString(
                i * 10 + "",
                posx + (i * 10 - 1) * width / numMoves + 3,
                posy + this.origParams[3] - 10);
        } else {
          for (int i = 1; i <= (numMoves / 20); i++)
            g.drawString(
                i * 20 + "",
                posx + (i * 20 - 1) * width / numMoves + 3,
                posy + this.origParams[3] - 10);
        }
      }
      if (selectedIndex == 0) {
        Stroke preStroke = g.getStroke();
        int x = posx + ((curMove.getData().moveNumber - 1) * width / numMoves);
        moveNumString = "" + curMove.getData().moveNumber;
        g.setStroke(dashed);
        g.setColor(Color.YELLOW);
        g.drawLine(x, 0, x, posy + origParams[3]);
        g.setColor(Color.BLACK);
        g.drawString(moveNumString, x + 3, posy + this.origParams[3] - 10);
        //

        if (mmovenum > 0) {
          x = posx + ((curMouseOverNode.getData().moveNumber - 1) * width / numMoves);
          moveNumString = "" + curMouseOverNode.getData().moveNumber;
          g.setStroke(dashed);
          g.setColor(Color.WHITE);
          g.drawLine(x, 0, x, posy + origParams[3]);
          g.setColor(Color.BLACK);
          g.drawString(moveNumString, x + 3, posy + this.origParams[3] - 10);
        }
        //
        g.setStroke(preStroke);
        if (lastOkMoveSave > 0 && !firstNodeAnalyzed) {
          g.setColor(Color.ORANGE);
          g.drawLine(
              posx,
              posy + height - (int) (50 * height / 100),
              posx + ((lastOkMoveSave) * width / numMoves),
              posy + height - (int) (convertWinrate(lastWrSave) * height / 100));
        }

        node = curMove;
        while (node.next().isPresent()) {
          node = node.next().get();
        }
        if (numMoves < node.getData().moveNumber - 1) {
          numMoves = node.getData().moveNumber - 1;
        }

        if (numMoves < 1) return;
        lastOkMove = -1;
        movenum = node.getData().moveNumber - 1;

        if (isKatago) {
          double lastscoreMean = -500;
          //   int curmovenum = -1;
          //   double drawcurscoreMean = 0;
          while (node.previous().isPresent()) {
            if (!node.getData().bestMoves.isEmpty()) {

              double curscoreMean =
                  isMainEngine ? node.getData().scoreMean : node.getData().scoreMean2;

              if (!node.getData().blackToPlay) {
                curscoreMean = -curscoreMean;
              }
              if (Lizzie.config.scoreMeanWinrateGraphBoard)
                curscoreMean = curscoreMean + Lizzie.board.getHistory().getGameInfo().getKomi();
              if (Math.abs(curscoreMean) > maxcoreMean) maxcoreMean = Math.abs(curscoreMean);

              if (node == curMove) {
                cScore = curscoreMean;
              } else if (node == curMouseOverNode) {
                mScore = curscoreMean;
              }
              if (lastOkMove > 0 && Math.abs(movenum - lastOkMove) < 25) {

                if (lastscoreMean > -500) {
                  // Color lineColor = g.getColor();
                  Stroke previousStroke = g.getStroke();
                  g.setColor(Color.RED);
                  if (!node.isMainTrunk()) {
                    g.setStroke(dashed);
                  } else g.setStroke(new BasicStroke(1));
                  g.drawLine(
                      posx + (lastOkMove * width / numMoves),
                      posy
                          + height / 2
                          - (int) (convertcoreMean(lastscoreMean) * height / 2 / maxcoreMean),
                      posx + (movenum * width / numMoves),
                      posy
                          + height / 2
                          - (int) (convertcoreMean(curscoreMean) * height / 2 / maxcoreMean));
                  g.setStroke(previousStroke);
                }
              }

              lastscoreMean = curscoreMean;
              lastOkMove = movenum;
            }

            node = node.previous().get();
            movenum--;
          }
        }
        int mPosX = -1;
        if (mmovenum > 0)
          mPosX =
              posx
                  + (mmovenum * width / numMoves)
                  - (isKatago || Lizzie.board.isKataBoard ? 9 : 3) * DOT_RADIUS;
        if (cmovenum > 0) {
          Font f = new Font(Lizzie.config.uiFontName, Font.BOLD, 16);
          g.setFont(f);
          String wrString = String.format("%.1f", cwr);
          int strWidth = g.getFontMetrics().stringWidth(wrString);
          int cPosX =
              posx
                  + (cmovenum * width / numMoves)
                  - (isKatago || Lizzie.board.isKataBoard ? 9 : 3) * DOT_RADIUS;
          if (Math.abs(cPosX - mPosX) > strWidth) {
            g.setColor(Color.BLACK);
            if (cmovenum > 0)
              if (cwr > 50)
                g.drawString(
                    wrString,
                    cPosX,
                    posy + (height - (int) (convertWinrate(cwr) * height / 100)) + 6 * DOT_RADIUS);
              else
                g.drawString(
                    wrString,
                    cPosX,
                    posy + (height - (int) (convertWinrate(cwr) * height / 100)) - 2 * DOT_RADIUS);

            g.setColor(Color.MAGENTA);
            g.fillOval(
                posx + (cmovenum * width / numMoves) - DOT_RADIUS,
                posy + height - (int) (cwr * height / 100) - DOT_RADIUS,
                DOT_RADIUS * 2,
                DOT_RADIUS * 2);

            if (cScore > -500) {
              g.setColor(Color.WHITE);
              g.setFont(new Font(Lizzie.config.uiFontName, Font.BOLD, 13));
              if (numMoves - cmovenum < 2) {
                g.drawString(
                    String.format("%.1f", cScore),
                    posx + (cmovenum * width / numMoves) - 13,
                    posy + height / 2 - (int) (convertcoreMean(cScore) * height / 2 / maxcoreMean));
              } else
                g.drawString(
                    String.format("%.1f", cScore),
                    posx + (cmovenum * width / numMoves),
                    posy + height / 2 - (int) (convertcoreMean(cScore) * height / 2 / maxcoreMean));
            }
          }
        }
        if (mmovenum > 0) {
          Font f = new Font(Lizzie.config.uiFontName, Font.BOLD, 16);
          g.setFont(f);
          g.setColor(Color.BLACK);
          if (mmovenum > 0)
            if (mwr > 50)
              g.drawString(
                  String.format("%.1f", mwr),
                  mPosX,
                  posy + (height - (int) (convertWinrate(mwr) * height / 100)) + 6 * DOT_RADIUS);
            else
              g.drawString(
                  String.format("%.1f", mwr),
                  mPosX,
                  posy + (height - (int) (convertWinrate(mwr) * height / 100)) - 2 * DOT_RADIUS);
          g.setColor(Color.YELLOW);
          g.fillOval(
              posx + (mmovenum * width / numMoves) - DOT_RADIUS,
              posy + height - (int) (mwr * height / 100) - DOT_RADIUS,
              DOT_RADIUS * 2,
              DOT_RADIUS * 2);

          if (mScore > -500) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Lizzie.config.uiFontName, Font.BOLD, 13));
            if (numMoves - mmovenum < 2) {
              g.drawString(
                  String.format("%.1f", mScore),
                  posx + (mmovenum * width / numMoves) - 13,
                  posy + height / 2 - (int) (convertcoreMean(mScore) * height / 2 / maxcoreMean));
            } else
              g.drawString(
                  String.format("%.1f", mScore),
                  posx + (mmovenum * width / numMoves),
                  posy + height / 2 - (int) (convertcoreMean(mScore) * height / 2 / maxcoreMean));
          }
        }
      }
    }
    params[0] = posx;
    params[1] = posy;
    params[2] = width;
    params[3] = height;
    params[4] = numMoves;
  }

  private void drawOneBigMistakePoint(
      Graphics2D g,
      int width,
      int height,
      bigMistakeInfo bigMistakeInfo,
      int index,
      boolean isScore) {
    // TODO Auto-generated method stub
    if (index == mouseOverBigMistakeIndex) {
      g.setColor(Color.YELLOW);
      g.setStroke(
          new BasicStroke(
              1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {5}, 0));
      g.drawLine(width / 20 + index * width / 10, 0, width / 20 + index * width / 10, height);
    }
    if (bigMistakeInfo.isBlack) g.setColor(Color.BLACK);
    else g.setColor(Color.WHITE);
    g.fillOval(
        width / 20 + index * width / 10 - height / 26,
        (int) (height * 0.15 + height * 0.7 * (100 - bigMistakeInfo.currentMoveWinRate) / 100.0)
            - height / 26,
        height / 13,
        height / 13);
    int[] xPoints = {
      width / 20 + index * width / 10 - height / 40,
      width / 20 + index * width / 10 - height / 55,
      width / 20 + index * width / 10 - height / 10
    };
    int[] yPoints = {
      (int) (height * 0.15 + height * 0.7 * (100 - bigMistakeInfo.currentMoveWinRate) / 100),
      (int) (height * 0.15 + height * 0.7 * (100 - bigMistakeInfo.currentMoveWinRate) / 100)
          - height / 30,
      (int) (height * 0.15 + height * 0.7 * (100 - bigMistakeInfo.currentMoveWinRate) / 100)
          - height / 10
    };
    g.fillPolygon(xPoints, yPoints, 3);
    g.setFont(new Font("", Font.PLAIN, Config.frameFontSize - 2));
    FontMetrics fm = g.getFontMetrics(new Font("", Font.PLAIN, Config.frameFontSize - 2));
    int widthFont = fm.stringWidth(bigMistakeInfo.moveNumber + "");
    g.drawString(
        bigMistakeInfo.moveNumber + "",
        width / 20 + index * width / 10 - widthFont / 2,
        height - 5);
    //
    g.setFont(new Font("", Font.PLAIN, Config.frameFontSize));
    g.drawString(
        String.format("%.1f", bigMistakeInfo.diffWinrate) + (isScore ? "" : "%"),
        width / 20 + index * width / 10 + height / 13,
        (int) (height * 0.15 + height * 0.7 * (100 - bigMistakeInfo.currentMoveWinRate) / 100.0));
  }

  class TableStyle extends DefaultTableCellRenderer {
    public TableStyle() {
      setHorizontalAlignment(CENTER);
    }
  }

  class ColorTableCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      // if(row%2 == 0){
      // if(row%2 == 0){
      if (topCurNode
          && row == 0
          && Lizzie.board.getHistory().getCurrentHistoryNode().previous().isPresent()
          && (showBranch.getSelectedIndex() == 1
              || (showBranch.getSelectedIndex() == 0
                  && Lizzie.board.getHistory().getCurrentHistoryNode().isMainTrunk()))
          && Lizzie.board.getHistory().getCurrentHistoryNode().previous().get().nodeInfo.analyzed) {
        setBackground(new Color(220, 220, 220));
        setForeground(new Color(0, 0, 0));
      } else if (Lizzie.board.convertNameToCoordinates(table.getValueAt(row, 2).toString())[0]
              == Lizzie.frame.clickbadmove[0]
          && Lizzie.board.convertNameToCoordinates(table.getValueAt(row, 2).toString())[1]
              == Lizzie.frame.clickbadmove[1]) {
        setBackground(new Color(238, 221, 130));
      } else setBackground(Color.WHITE);
      double diffWinrate =
          -Float.parseFloat(
              table
                  .getValueAt(row, 3)
                  .toString()
                  .substring(0, table.getValueAt(row, 3).toString().length() - 1));
      if (isKatago) {
        double scoreDiff =
            -Float.parseFloat(
                table
                    .getValueAt(row, 4)
                    .toString()
                    .substring(0, table.getValueAt(row, 4).toString().length() - 1));
        if (column == 4) {
          if (scoreDiff < 0) setForeground(Color.GREEN.darker());
          else if (scoreDiff >= 3 && scoreDiff <= 5) setForeground(Color.BLUE);
          else if (scoreDiff > 5) setForeground(new Color(220, 0, 0));
          else setForeground(Color.BLACK);
        } else if (column == 3) {
          if (diffWinrate < 0) setForeground(Color.GREEN.darker());
          else if (diffWinrate >= 5 && diffWinrate <= 20) setForeground(Color.BLUE);
          else if (diffWinrate > 20) setForeground(new Color(220, 0, 0));
        } else setForeground(Color.BLACK);
      } else {
        if (column == 3) {
          if (diffWinrate < 0) setForeground(Color.GREEN.darker());
          else if (diffWinrate >= 5 && diffWinrate <= 20) setForeground(Color.BLUE);
          else if (diffWinrate > 20) setForeground(new Color(220, 0, 0));
        } else setForeground(Color.BLACK);
      }
      return super.getTableCellRendererComponent(table, value, false, false, row, column);
    }
  }

  class ColorTableCellRenderer2 extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      if (Lizzie.board.convertNameToCoordinates(table.getValueAt(row, 1).toString())[0]
              == Lizzie.frame.clickbadmove[0]
          && Lizzie.board.convertNameToCoordinates(table.getValueAt(row, 1).toString())[1]
              == Lizzie.frame.clickbadmove[1]) {
        setBackground(new Color(238, 221, 130));
      } else setBackground(Color.WHITE);
      double diffWinrate =
          -Float.parseFloat(
              table
                  .getValueAt(row, 2)
                  .toString()
                  .substring(0, table.getValueAt(row, 2).toString().length() - 1));
      if (isKatago) {
        double scoreDiff =
            -Float.parseFloat(
                table
                    .getValueAt(row, 3)
                    .toString()
                    .substring(0, table.getValueAt(row, 3).toString().length() - 1));
        if (column == 3) {
          if (scoreDiff < 0) setForeground(Color.GREEN.darker());
          else if (scoreDiff >= 3 && scoreDiff <= 5) setForeground(Color.BLUE);
          else if (scoreDiff > 5) setForeground(new Color(220, 0, 0));
          else setForeground(Color.BLACK);
        } else if (column == 2) {
          if (diffWinrate < 0) setForeground(Color.GREEN.darker());
          else if (diffWinrate >= 5 && diffWinrate <= 20) setForeground(Color.BLUE);
          else if (diffWinrate > 20) setForeground(new Color(220, 0, 0));
        } else setForeground(Color.BLACK);
      } else {
        if (column == 2) {
          if (diffWinrate < 0) setForeground(Color.GREEN.darker());
          else if (diffWinrate >= 5 && diffWinrate <= 20) setForeground(Color.BLUE);
          else if (diffWinrate > 20) setForeground(new Color(220, 0, 0));
        } else setForeground(Color.BLACK);
      }
      return super.getTableCellRendererComponent(table, value, false, false, row, column);
    }
  }

  private void applyChange(
      int suggestionsMoves, double percentPlayouts, int moveFirst, int moveLast) {
    Lizzie.board.clearNodeInfo(Lizzie.board.getHistory().getStart());
    Lizzie.board.matchAiMoves = suggestionsMoves;
    Lizzie.board.matchAiPercentsPlayouts = percentPlayouts;

    Lizzie.board.matchAiFirstMove = moveFirst;
    Lizzie.board.matchAiLastMove = moveLast;

    Lizzie.config.matchAiMoves = suggestionsMoves;
    Lizzie.config.matchAiPercentsPlayouts = percentPlayouts;
    Lizzie.config.uiConfig.put("match-ai-moves", Lizzie.config.matchAiMoves);
    Lizzie.config.uiConfig.put("match-ai-percents-playouts", Lizzie.config.matchAiPercentsPlayouts);

    Lizzie.config.matchAiFirstMove = moveFirst;
    Lizzie.config.matchAiLastMove = moveLast;
    Lizzie.config.uiConfig.put("match-ai-firstmove", Lizzie.config.matchAiFirstMove);
    Lizzie.config.uiConfig.put("match-ai-lastmove", Lizzie.config.matchAiLastMove);
    Lizzie.board.setMovelistAll();
    Lizzie.frame.refresh();
  }

  private void setPanel() {
    if (selectedIndex == 4) {
      matchPanelmin.add(detail);
      matchPanelmin.add(settings);
      if (Lizzie.config.isChinese) {
        lblParse1Move.setBounds(5, 40, 180, 20);
        txtParse1Move.setBounds(81, 43, 35, 16);
        lblParse2Move.setBounds(120, 40, 180, 20);
        txtParse2Move.setBounds(196, 43, 35, 16);
      } else {
        lblParse1Move.setBounds(5, 40, 180, 20);
        txtParse1Move.setBounds(116, 43, 35, 16);
        lblParse2Move.setBounds(155, 40, 180, 20);
        txtParse2Move.setBounds(255, 43, 35, 16);
      }
      txtParse1Move.setText(Lizzie.config.parse1Move + "");
      txtParse2Move.setText(Lizzie.config.parse2Move + "");
      matchPanelmin.add(lblParse1Move);
      matchPanelmin.add(txtParse1Move);
      matchPanelmin.add(lblParse2Move);
      matchPanelmin.add(txtParse2Move);
    } else {
      if (selectedIndex == 2 || selectedIndex == 3 || selectedIndex == 5 || selectedIndex == 6) {
        matchPanelmin.add(lblDiffConfig1);
        if (selectedIndex == 3 || selectedIndex == 6) {
          lblDiffConfig1.setText(
              Lizzie.resourceBundle.getString("Movelistframe.lblDiffConfigScore"));
          winrateDiffRange1.setText(Lizzie.config.scoreDiffRange1 + "");
          winrateDiffRange2.setText(Lizzie.config.scoreDiffRange2 + "");
        }
        if (selectedIndex == 2 || selectedIndex == 5) {
          lblDiffConfig1.setText(Lizzie.resourceBundle.getString("Movelistframe.lblDiffConfig1"));
          winrateDiffRange1.setText(Lizzie.config.winrateDiffRange1 + "");
          winrateDiffRange2.setText(Lizzie.config.winrateDiffRange2 + "");
        }
        matchPanelmin.add(lblDiffConfig2);
        matchPanelmin.add(winrateDiffRange1);
        matchPanelmin.add(winrateDiffRange2);
      } else {
        matchPanelmin.add(lblMatchConfig1);
        matchPanelmin.add(detail);
        matchPanelmin.add(settings);
        matchPanelmin.add(lblMatchConfig2);
        matchPanelmin.add(suggestionMoves);
        matchPanelmin.add(percentPlayouts);
        matchPanelmin.add(lblMatchConfig3);
      }
      if (selectedIndex == 5 || selectedIndex == 6) {
        if (Lizzie.config.isChinese) {
          lblParse1Move.setBounds(305, 40, 180, 20);
          txtParse1Move.setBounds(381, 43, 35, 16);
          lblParse2Move.setBounds(420, 40, 180, 20);
          txtParse2Move.setBounds(496, 43, 35, 16);
        } else {
          lblParse1Move.setBounds(315, 40, 180, 20);
          txtParse1Move.setBounds(426, 43, 35, 16);
          lblParse2Move.setBounds(465, 40, 180, 20);
          txtParse2Move.setBounds(565, 43, 35, 16);
        }
        txtParse1Move.setText(Lizzie.config.parse1Move + "");
        txtParse2Move.setText(Lizzie.config.parse2Move + "");
        matchPanelmin.add(lblParse1Move);
        matchPanelmin.add(txtParse1Move);
        matchPanelmin.add(lblParse2Move);
        matchPanelmin.add(txtParse2Move);
      } else {
        matchPanelmin.add(lblMatchConfig4);
        matchPanelmin.add(firstMove);
        matchPanelmin.add(lblMatchConfig5);
        matchPanelmin.add(lastMove);
      }
    }
    if (selectedIndex == 0) {
      if (Lizzie.config.isChinese) {
        lblMatchConfig1.setBounds(5, 40, 166, 20);
        suggestionMoves.setBounds(84, 43, 25, 16);
        lblMatchConfig2.setBounds(110, 40, 250, 20);
        percentPlayouts.setBounds(258, 43, 40, 16);
        lblMatchConfig3.setBounds(299, 40, 15, 20);
        lblMatchConfig4.setBounds(318, 40, 100, 20);
        firstMove.setBounds(373, 43, 35, 16);
        lblMatchConfig5.setBounds(410, 40, 15, 20);
        lastMove.setBounds(423, 43, 35, 16);
      } else {
        lblMatchConfig1.setBounds(5, 40, 166, 20);
        suggestionMoves.setBounds(123, 43, 25, 16);
        lblMatchConfig2.setBounds(150, 40, 250, 20);
        percentPlayouts.setBounds(373, 43, 40, 16);
        lblMatchConfig3.setBounds(415, 40, 15, 20);
        lblMatchConfig4.setBounds(435, 40, 100, 20);
        firstMove.setBounds(499, 43, 35, 16);
        lblMatchConfig5.setBounds(536, 40, 15, 20);
        lastMove.setBounds(548, 43, 35, 16);
      }
    }
    if (selectedIndex == 1) {
      if (Lizzie.config.isChinese) {
        lblMatchConfig1.setBounds(0, 0, 0, 0);
        suggestionMoves.setBounds(0, 0, 0, 0);
        lblMatchConfig2.setBounds(0, 0, 0, 0);
        percentPlayouts.setBounds(0, 0, 0, 0);
        lblMatchConfig3.setBounds(0, 0, 0, 0);
        lblMatchConfig4.setBounds(5, 40, 100, 20);
        firstMove.setBounds(60, 43, 35, 16);
        lblMatchConfig5.setBounds(97, 40, 15, 20);
        lastMove.setBounds(110, 43, 35, 16);
      } else {
        lblMatchConfig1.setBounds(0, 0, 0, 0);
        suggestionMoves.setBounds(0, 0, 0, 0);
        lblMatchConfig2.setBounds(0, 0, 0, 0);
        percentPlayouts.setBounds(0, 0, 0, 0);
        lblMatchConfig3.setBounds(0, 0, 0, 0);
        lblMatchConfig4.setBounds(5, 40, 100, 20);
        firstMove.setBounds(69, 43, 35, 16);
        lblMatchConfig5.setBounds(106, 40, 15, 20);
        lastMove.setBounds(118, 43, 35, 16);
      }
    }
  }

  private void togglealwaysontop() {
    if (isAlwaysOnTop()) {
      setAlwaysOnTop(false);
      Lizzie.config.badmovesalwaysontop = false;
      Lizzie.config.uiConfig.put("badmoves-always-ontop", false);
    } else {
      setAlwaysOnTop(true);
      Lizzie.config.badmovesalwaysontop = true;
      Lizzie.config.uiConfig.put("badmoves-always-ontop", true);
      // if (Lizzie.frame.isAlwaysOnTop()) Lizzie.frame.toggleAlwaysOntop();
    }
    setTopTitle();
  }

  private void setTopTitle() {
    if (this.isAlwaysOnTop())
      setTitle(Lizzie.resourceBundle.getString("Lizzie.alwaysOnTopTitle") + oriTitle);
    else setTitle(oriTitle);
  }
  //  private void handleTableClick(int row, int col, int index) {
  //    if (selectedorder != row) {
  //      int[] coords = Lizzie.board.convertNameToCoordinates(table.getValueAt(row, 2).toString());
  //      Lizzie.frame.clickbadmove = coords;
  //      Lizzie.frame.boardRenderer.drawbadstone(coords[0], coords[1], Stone.BLACK);
  //      Lizzie.frame.refresh();
  //      selectedorder = row;
  //    } else {
  //      Lizzie.frame.clickbadmove = Lizzie.frame.outOfBoundCoordinate;
  //      Lizzie.frame.boardRenderer.removedrawmovestone();
  //      Lizzie.frame.refresh();
  //      selectedorder = -1;
  //      table.clearSelection();
  //    }
  //  }

  private void handleTableDoubleClick(int row, int col) {
    if (Lizzie.config.isAutoAna) return;
    table.repaint();
    int movenumber = Integer.parseInt(table.getValueAt(row, 1).toString());
    // Lizzie.board.goToMoveNumber(1);
    int[] coords = Lizzie.board.convertNameToCoordinates(table.getValueAt(row, 2).toString());
    int moveNumber = Lizzie.board.moveNumberByCoord(coords);
    if (this.showBranch.getSelectedIndex() == 0) {
      Lizzie.frame.moveToMainTrunk();
    }
    Lizzie.board.goToMoveNumber(movenumber - 1);
    Lizzie.frame.clickbadmove = coords;
    Lizzie.frame.refresh();
    //    if (Lizzie.board.getHistory().getCurrentHistoryNode().previous().isPresent()
    //            && (showBranch.getSelectedIndex() == 1
    //                || (showBranch.getSelectedIndex() == 0
    //                    && Lizzie.board.getHistory().getCurrentHistoryNode().isMainTrunk()))
    //            && Lizzie.board
    //                .getHistory()
    //                .getCurrentHistoryNode()
    //                .previous()
    //                .get()
    //                .nodeInfo
    //                .analyzed)
    //    selectedorder = row;
    //   else
    selectedorder = row + 1;
  }

  private void handleTableDoubleClickMin1(int row, int col) {
    if (Lizzie.config.isAutoAna) return;
    tablePanelMin1.repaint();
    int movenumber = Integer.parseInt(minTable1.getValueAt(row, 0).toString());
    // Lizzie.board.goToMoveNumber(1);
    int[] coords = Lizzie.board.convertNameToCoordinates(minTable1.getValueAt(row, 1).toString());
    int moveNumber = Lizzie.board.moveNumberByCoord(coords);
    if (this.showBranch.getSelectedIndex() == 0) {
      Lizzie.frame.moveToMainTrunk();
    }
    Lizzie.board.goToMoveNumber(movenumber - 1);
    Lizzie.frame.clickbadmove = coords;
    Lizzie.frame.repaint();
  }

  private void handleTableDoubleClickMin2(int row, int col) {
    if (Lizzie.config.isAutoAna) return;
    tablePanelMin2.repaint();
    int movenumber = Integer.parseInt(minTable2.getValueAt(row, 0).toString().trim());
    // Lizzie.board.goToMoveNumber(1);
    int[] coords = Lizzie.board.convertNameToCoordinates(minTable2.getValueAt(row, 1).toString());
    int moveNumber = Lizzie.board.moveNumberByCoord(coords);
    if (this.showBranch.getSelectedIndex() == 0) {
      Lizzie.frame.moveToMainTrunk();
    }
    Lizzie.board.goToMoveNumber(movenumber - 1);
    Lizzie.frame.clickbadmove = coords;
    Lizzie.frame.refresh();
  }

  private void setTxtUnfocuse() {
    if (percentPlayouts.isFocusOwner()) {
      firstMove.setFocusable(false);
      lastMove.setFocusable(false);
      txtParse1Move.setFocusable(false);
      txtParse2Move.setFocusable(false);
      suggestionMoves.setFocusable(false);
      percentPlayouts.setFocusable(false);
      percentPlayouts.setFocusable(true);
      suggestionMoves.setFocusable(true);
      txtParse1Move.setFocusable(true);
      txtParse2Move.setFocusable(true);
      firstMove.setFocusable(true);
      lastMove.setFocusable(true);
    }
    if (suggestionMoves.isFocusOwner()) {
      firstMove.setFocusable(false);
      lastMove.setFocusable(false);
      txtParse1Move.setFocusable(false);
      txtParse2Move.setFocusable(false);
      percentPlayouts.setFocusable(false);
      suggestionMoves.setFocusable(false);
      suggestionMoves.setFocusable(true);
      percentPlayouts.setFocusable(true);
      txtParse1Move.setFocusable(true);
      txtParse2Move.setFocusable(true);
      firstMove.setFocusable(true);
      lastMove.setFocusable(true);
    }
    if (firstMove.isFocusOwner()) {
      lastMove.setFocusable(false);
      percentPlayouts.setFocusable(false);
      txtParse1Move.setFocusable(false);
      txtParse2Move.setFocusable(false);
      suggestionMoves.setFocusable(false);
      firstMove.setFocusable(false);
      firstMove.setFocusable(true);
      suggestionMoves.setFocusable(true);
      txtParse1Move.setFocusable(true);
      txtParse2Move.setFocusable(true);
      percentPlayouts.setFocusable(true);
      lastMove.setFocusable(true);
    }
    if (lastMove.isFocusOwner()) {
      firstMove.setFocusable(false);
      percentPlayouts.setFocusable(false);
      txtParse1Move.setFocusable(false);
      txtParse2Move.setFocusable(false);
      suggestionMoves.setFocusable(false);
      lastMove.setFocusable(false);
      lastMove.setFocusable(true);
      txtParse1Move.setFocusable(true);
      txtParse2Move.setFocusable(true);
      suggestionMoves.setFocusable(true);
      percentPlayouts.setFocusable(true);
      firstMove.setFocusable(true);
    }
    if (txtParse1Move.isFocusOwner()) {
      firstMove.setFocusable(false);
      percentPlayouts.setFocusable(false);
      txtParse2Move.setFocusable(false);
      suggestionMoves.setFocusable(false);
      lastMove.setFocusable(false);
      txtParse1Move.setFocusable(false);
      txtParse1Move.setFocusable(true);
      lastMove.setFocusable(true);
      txtParse2Move.setFocusable(true);
      suggestionMoves.setFocusable(true);
      percentPlayouts.setFocusable(true);
      firstMove.setFocusable(true);
    }
    if (txtParse2Move.isFocusOwner()) {
      firstMove.setFocusable(false);
      percentPlayouts.setFocusable(false);
      txtParse1Move.setFocusable(false);
      suggestionMoves.setFocusable(false);
      lastMove.setFocusable(false);
      txtParse2Move.setFocusable(false);
      txtParse2Move.setFocusable(true);
      lastMove.setFocusable(true);
      txtParse1Move.setFocusable(true);
      suggestionMoves.setFocusable(true);
      percentPlayouts.setFocusable(true);
      firstMove.setFocusable(true);
    }
  }

  public AbstractTableModel getTableModel() {

    return new AbstractTableModel() {
      public int getColumnCount() {
        if (isKatago) return 11;
        else return 9;
      }

      public int getRowCount() {
        int row = 0;

        ArrayList<NodeInfo> data2 = new ArrayList<NodeInfo>();

        BoardHistoryNode lastNode =
            showBranch.getSelectedIndex() == 0
                ? Lizzie.board.getHistory().getMainEnd()
                : Lizzie.board.getHistory().getEnd();
        while (lastNode.previous().isPresent()) {
          NodeInfo nodeInfoThis =
              showBranch.getSelectedIndex() == 0
                  ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                  : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
          if (nodeInfoThis.analyzed)
            if (nodeInfoThis.isBlack && checkBlack.isSelected()
                || !nodeInfoThis.isBlack && checkWhite.isSelected())
              if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
                if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                    && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                  if (!isKatago
                      || Math.abs(nodeInfoThis.scoreMeanDiff)
                          >= (int) dropScoreMeanChooser.getValue()) row = row + 1;
          lastNode = lastNode.previous().get();
        }
        NodeInfo nodeInfoThis =
            showBranch.getSelectedIndex() == 0
                ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
        if (nodeInfoThis.analyzed)
          if (nodeInfoThis.isBlack && checkBlack.isSelected()
              || !nodeInfoThis.isBlack && checkWhite.isSelected())
            if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
              if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                  && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                if (!isKatago
                    || Math.abs(nodeInfoThis.scoreMeanDiff)
                        >= (int) dropScoreMeanChooser.getValue()) row = row + 1;
        //
        //
        //        ArrayList<Movelistwr> data2 = Lizzie.board.movelistwr;
        //        for (int i = 0; i < Lizzie.board.movelistwr.size(); i++) {
        //          Movelistwr mwr = Lizzie.board.movelistwr.get(i);
        //          if (!mwr.isdelete)
        //            if (mwr.isblack && checkBlack.isSelected() || !mwr.isblack &&
        // checkWhite.isSelected())
        //              if (Math.abs(mwr.diffwinrate) >= (int) dropwinratechooser.getValue())
        //                if (mwr.playouts >= (int) playoutschooser.getValue()
        //                    && mwr.previousplayouts >= (int) playoutschooser.getValue())
        //                  if (!isKatago
        //                      || Math.abs(mwr.scoreMeanDiff) >= (int)
        // dropScoreMeanChooser.getValue())
        //                    row = row + 1;
        //        }
        if (topCurNode
            && Lizzie.board.getHistory().getCurrentHistoryNode().previous().isPresent()
            && (showBranch.getSelectedIndex() == 1
                || (showBranch.getSelectedIndex() == 0
                    && Lizzie.board.getHistory().getCurrentHistoryNode().isMainTrunk()))
            && Lizzie.board.getHistory().getCurrentHistoryNode().previous().get().nodeInfo.analyzed)
          return row + 1;
        else return row;
      }

      public String getColumnName(int column) {
        // aaa
        if (isKatago) {
          if (column == 0) return Lizzie.resourceBundle.getString("Movelistframe.tableColumnColor");
          if (column == 1)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnMoveNum");
          if (column == 2)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnCoords");
          if (column == 3)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWinDiff");
          if (column == 4)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnScoreDiff");
          if (column == 5)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnThisWin");
          if (column == 6) return Lizzie.resourceBundle.getString("Movelistframe.tableColumnAiWin");
          if (column == 7)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnScoreBoard");
          if (column == 8)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnPlayouts");
          if (column == 9)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnNextPlayouts");
          if (column == 10)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnAiScore");
        } else {
          if (column == 0) return Lizzie.resourceBundle.getString("Movelistframe.tableColumnColor");
          if (column == 1)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnMoveNum");
          if (column == 2)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnCoords");
          if (column == 3)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWinDiff");
          if (column == 4)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnThisWin");
          if (column == 5) return Lizzie.resourceBundle.getString("Movelistframe.tableColumnAiWin");
          if (column == 6)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnPlayouts");
          if (column == 7)
            return Lizzie.resourceBundle.getString("Movelistframe.tableColumnNextPlayouts");
          if (column == 8)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnAiScore");
        }
        return "";
      }

      @SuppressWarnings("unchecked")
      public Object getValueAt(int row, int col) {
        ArrayList<NodeInfo> data2 = new ArrayList<NodeInfo>();
        BoardHistoryNode lastNode =
            showBranch.getSelectedIndex() == 0
                ? Lizzie.board.getHistory().getMainEnd()
                : Lizzie.board.getHistory().getEnd();
        while (lastNode.previous().isPresent()) {
          NodeInfo nodeInfoThis =
              showBranch.getSelectedIndex() == 0
                  ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                  : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
          if (nodeInfoThis.analyzed)
            if (nodeInfoThis.isBlack && checkBlack.isSelected()
                || !nodeInfoThis.isBlack && checkWhite.isSelected())
              if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
                if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                    && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                  if (!isKatago
                      || Math.abs(nodeInfoThis.scoreMeanDiff)
                          >= (int) dropScoreMeanChooser.getValue()) data2.add(nodeInfoThis);
          lastNode = lastNode.previous().get();
        }
        NodeInfo nodeInfoThis =
            showBranch.getSelectedIndex() == 0
                ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
        if (nodeInfoThis.analyzed)
          if (nodeInfoThis.isBlack && checkBlack.isSelected()
              || !nodeInfoThis.isBlack && checkWhite.isSelected())
            if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
              if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                  && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                if (!isKatago
                    || Math.abs(nodeInfoThis.scoreMeanDiff)
                        >= (int) dropScoreMeanChooser.getValue()) data2.add(nodeInfoThis);
        Collections.sort(
            data2,
            new Comparator<NodeInfo>() {

              @Override
              public int compare(NodeInfo s1, NodeInfo s2) {
                // 降序
                if (isKatago) {
                  if (!issorted) {
                    if (sortnum == 0) {
                      if (s2.isBlack) return 1;
                      if (!s2.isBlack) return -1;
                    }
                    if (sortnum == 1) {
                      if (s1.moveNum > s2.moveNum) return 1;
                      if (s1.moveNum < s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      return 1;
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return 1;
                        if (s1.diffWinrate > s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 4) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.scoreMeanDiff) < Math.abs(s2.scoreMeanDiff)) return 1;
                        if (Math.abs(s1.scoreMeanDiff) > Math.abs(s2.scoreMeanDiff)) return -1;
                      } else {
                        if (s1.scoreMeanDiff < s2.scoreMeanDiff) return 1;
                        if (s1.scoreMeanDiff > s2.scoreMeanDiff) return -1;
                      }
                    }
                    if (sortnum == 5) {
                      if (s1.winrate < s2.winrate) return 1;
                      if (s1.winrate > s2.winrate) return -1;
                    }
                    if (sortnum == 6) {
                      if (s1.winrate - s1.diffWinrate < s2.winrate - s2.diffWinrate) return 1;
                      if (s1.winrate - s1.diffWinrate > s2.winrate - s2.diffWinrate) return -1;
                    }
                    if (sortnum == 7) {
                      if (s1.scoreMeanBoard < s2.scoreMeanBoard) return 1;
                      if (s1.scoreMeanBoard > s2.scoreMeanBoard) return -1;
                    }
                    if (sortnum == 8) {
                      if (s1.previousPlayouts < s2.previousPlayouts) return 1;
                      if (s1.previousPlayouts > s2.previousPlayouts) return -1;
                    }
                    if (sortnum == 9) {
                      if (s1.playouts < s2.playouts) return 1;
                      if (s1.playouts > s2.playouts) return -1;
                    }
                    if (sortnum == 10) {
                      if (s1.percentsMatch < s2.percentsMatch) return 1;
                      if (s1.percentsMatch > s2.percentsMatch) return -1;
                    }

                  } else {
                    if (sortnum == 0) {
                      if (!s2.isBlack) return 1;
                      if (s2.isBlack) return -1;
                    }
                    if (sortnum == 1) {
                      if (s1.moveNum < s2.moveNum) return 1;
                      if (s1.moveNum > s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      return 1;
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate > s2.diffWinrate) return 1;
                        if (s1.diffWinrate < s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 4) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.scoreMeanDiff) > Math.abs(s2.scoreMeanDiff)) return 1;
                        if (Math.abs(s1.scoreMeanDiff) < Math.abs(s2.scoreMeanDiff)) return -1;
                      } else {
                        if (s1.scoreMeanDiff > s2.scoreMeanDiff) return 1;
                        if (s1.scoreMeanDiff < s2.scoreMeanDiff) return -1;
                      }
                    }
                    if (sortnum == 5) {
                      if (s1.winrate > s2.winrate) return 1;
                      if (s1.winrate < s2.winrate) return -1;
                    }
                    if (sortnum == 6) {
                      if (s1.winrate - s1.diffWinrate > s2.winrate - s2.diffWinrate) return 1;
                      if (s1.winrate - s1.diffWinrate < s2.winrate - s2.diffWinrate) return -1;
                    }
                    if (sortnum == 7) {
                      if (s1.scoreMeanBoard > s2.scoreMeanBoard) return 1;
                      if (s1.scoreMeanBoard < s2.scoreMeanBoard) return -1;
                    }
                    if (sortnum == 8) {
                      if (s1.previousPlayouts > s2.previousPlayouts) return 1;
                      if (s1.previousPlayouts < s2.previousPlayouts) return -1;
                    }
                    if (sortnum == 9) {
                      if (s1.playouts > s2.playouts) return 1;
                      if (s1.playouts < s2.playouts) return -1;
                    }
                    if (sortnum == 10) {
                      if (s1.percentsMatch > s2.percentsMatch) return 1;
                      if (s1.percentsMatch < s2.percentsMatch) return -1;
                    }
                  }
                  return 0;
                } else {
                  if (!issorted) {
                    if (sortnum == 0) {
                      if (s2.isBlack) return 1;
                      if (!s2.isBlack) return -1;
                    }
                    if (sortnum == 1) {
                      if (s1.moveNum > s2.moveNum) return 1;
                      if (s1.moveNum < s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      return 1;
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return 1;
                        if (s1.diffWinrate > s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 4) {
                      if (s1.winrate < s2.winrate) return 1;
                      if (s1.winrate > s2.winrate) return -1;
                    }
                    if (sortnum == 5) {
                      if (s1.winrate - s1.diffWinrate < s2.winrate - s2.diffWinrate) return 1;
                      if (s1.winrate - s1.diffWinrate > s2.winrate - s2.diffWinrate) return -1;
                    }
                    if (sortnum == 6) {
                      if (s1.previousPlayouts < s2.previousPlayouts) return 1;
                      if (s1.previousPlayouts > s2.previousPlayouts) return -1;
                    }
                    if (sortnum == 7) {
                      if (s1.playouts < s2.playouts) return 1;
                      if (s1.playouts > s2.playouts) return -1;
                    }
                    if (sortnum == 8) {
                      if (s1.percentsMatch < s2.percentsMatch) return 1;
                      if (s1.percentsMatch > s2.percentsMatch) return -1;
                    }

                  } else {
                    if (sortnum == 0) {
                      if (!s2.isBlack) return 1;
                      if (s2.isBlack) return -1;
                    }
                    if (sortnum == 1) {
                      if (s1.moveNum < s2.moveNum) return 1;
                      if (s1.moveNum > s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      return 1;
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate > s2.diffWinrate) return 1;
                        if (s1.diffWinrate < s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 4) {
                      if (s1.winrate > s2.winrate) return 1;
                      if (s1.winrate < s2.winrate) return -1;
                    }
                    if (sortnum == 5) {
                      if (s1.winrate - s1.diffWinrate > s2.winrate - s2.diffWinrate) return 1;
                      if (s1.winrate - s1.diffWinrate < s2.winrate - s2.diffWinrate) return -1;
                    }
                    if (sortnum == 6) {
                      if (s1.previousPlayouts > s2.previousPlayouts) return 1;
                      if (s1.previousPlayouts < s2.previousPlayouts) return -1;
                    }
                    if (sortnum == 7) {
                      if (s1.playouts > s2.playouts) return 1;
                      if (s1.playouts < s2.playouts) return -1;
                    }
                    if (sortnum == 8) {
                      if (s1.percentsMatch > s2.percentsMatch) return 1;
                      if (s1.percentsMatch < s2.percentsMatch) return -1;
                      //                      if (s2.previousPlayouts > 0
                      //                          && s2.moveNum <= (Lizzie.board.matchAiLastMove +
                      // 1)
                      //                          && s2.moveNum > Lizzie.board.matchAiFirstMove) {
                      //                        if (!s2.isMatchAi) {
                      //                          if (s1.previousPlayouts > 0
                      //                              && s1.moveNum <= (Lizzie.board.matchAiLastMove
                      // + 1)
                      //                              && s1.moveNum > Lizzie.board.matchAiFirstMove)
                      // {
                      //                            if (!s1.isMatchAi) return 0;
                      //                            else return 1;
                      //                          } else {
                      //                            return 1;
                      //                          }
                      //                        } else {
                      //                          if (s1.previousPlayouts > 0
                      //                              && s1.moveNum <= (Lizzie.board.matchAiLastMove
                      // + 1)
                      //                              && s1.moveNum > Lizzie.board.matchAiFirstMove)
                      // {
                      //                            if (!s1.isMatchAi) return -1;
                      //                            else return 0;
                      //                          } else {
                      //                            return 1;
                      //                          }
                      //                        }
                      //                      } else if (s1.previousPlayouts > 0
                      //                          && s1.moveNum <= (Lizzie.board.matchAiLastMove +
                      // 1)
                      //                          && s1.moveNum > Lizzie.board.matchAiFirstMove) {
                      //                        return -1;
                      //                      } else return 0;
                    }
                  }
                  return 0;
                }
              }
            });
        if (topCurNode
            && Lizzie.board.getHistory().getCurrentHistoryNode().previous().isPresent()
            && (showBranch.getSelectedIndex() == 1
                || (showBranch.getSelectedIndex() == 0
                    && Lizzie.board.getHistory().getCurrentHistoryNode().isMainTrunk()))
            && (isMainEngine
                ? Lizzie.board
                    .getHistory()
                    .getCurrentHistoryNode()
                    .previous()
                    .get()
                    .nodeInfo
                    .analyzed
                : Lizzie.board
                    .getHistory()
                    .getCurrentHistoryNode()
                    .previous()
                    .get()
                    .nodeInfo2
                    .analyzed)) {

          data2.add(
              0,
              showBranch.getSelectedIndex() == 0
                  ? (isMainEngine
                      ? Lizzie.board
                          .getHistory()
                          .getCurrentHistoryNode()
                          .previous()
                          .get()
                          .nodeInfoMain
                      : Lizzie.board
                          .getHistory()
                          .getCurrentHistoryNode()
                          .previous()
                          .get()
                          .nodeInfoMain2)
                  : (isMainEngine
                      ? Lizzie.board.getHistory().getCurrentHistoryNode().previous().get().nodeInfo
                      : Lizzie.board
                          .getHistory()
                          .getCurrentHistoryNode()
                          .previous()
                          .get()
                          .nodeInfo2));
        }
        // featurecat.lizzie.analysis.MoveDataSorter MoveDataSorter = new
        // MoveDataSorter(data2);
        // ArrayList sortedMoveData = MoveDataSorter.getSortedMoveDataByPolicy();

        NodeInfo data = data2.get(row);
        if (isKatago) {
          if (Lizzie.board.isPkBoard) {
            switch (col) {
              case 0:
                if (data.isBlack) return Lizzie.resourceBundle.getString("Movelistframe.black");
                return Lizzie.resourceBundle.getString("Movelistframe.white");
              case 1:
                return data.moveNum;
              case 2:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 3:
                return (data.diffWinrate < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate < 0 ? "↑" : "↓");
              case 4:
                return (data.scoreMeanDiff < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.scoreMeanDiff))
                    + (data.scoreMeanDiff < 0 ? "↑" : "↓");
              case 5:
                return String.format("%.2f", 100 - data.winrate);
              case 6:
                if (data.previousPlayouts > 0) {
                  return String.format("%.2f", 100 - (data.winrate - data.diffWinrate));
                } else {
                  return "";
                }
              case 7:
                return String.format("%.2f", data.scoreMeanBoard);
              case 8:
                return Lizzie.frame.getPlayoutsString(data.previousPlayouts);
              case 9:
                return Lizzie.frame.getPlayoutsString(data.playouts);
              case 10:
                return "-";
              default:
                return "";
            }
          } else {
            switch (col) {
              case 0:
                if (data.isBlack) return Lizzie.resourceBundle.getString("Movelistframe.black");
                return Lizzie.resourceBundle.getString("Movelistframe.white");
              case 1:
                return data.moveNum;
              case 2:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 3:
                return (data.diffWinrate > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate > 0 ? "↑" : "↓");
              case 4:
                return (data.scoreMeanDiff > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.scoreMeanDiff))
                    + (data.scoreMeanDiff > 0 ? "↑" : "↓");
              case 5:
                return String.format("%.2f", data.winrate);
              case 6:
                if (data.previousPlayouts > 0) {
                  return String.format("%.2f", data.winrate - data.diffWinrate);
                } else {
                  return "";
                }
              case 7:
                return String.format("%.2f", data.scoreMeanBoard);
              case 8:
                return Lizzie.frame.getPlayoutsString(data.previousPlayouts);
              case 9:
                return Lizzie.frame.getPlayoutsString(data.playouts);
              case 10:
                return String.format(
                    "%.1f",
                    // Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
                //                if (data.previousPlayouts > 0
                //                    && data.moveNum <= (Lizzie.board.matchAiLastMove + 1)
                //                    && data.moveNum > Lizzie.board.matchAiFirstMove)
                //                  return data.isMatchAi ? "是" : "否";
                //                else return "无";
              default:
                return "";
            }
          }
        } else {
          if (Lizzie.board.isPkBoard) {
            switch (col) {
              case 0:
                if (data.isBlack) return Lizzie.resourceBundle.getString("Movelistframe.black");
                return Lizzie.resourceBundle.getString("Movelistframe.white");
              case 1:
                return data.moveNum;
              case 2:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 3:
                return (data.diffWinrate < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate < 0 ? "↑" : "↓");
              case 4:
                return String.format("%.2f", 100 - data.winrate);
              case 5:
                if (data.previousPlayouts > 0) {
                  return String.format("%.2f", 100 - (data.winrate - data.diffWinrate));
                } else {
                  return "";
                }
              case 6:
                return Lizzie.frame.getPlayoutsString(data.previousPlayouts);
              case 7:
                return Lizzie.frame.getPlayoutsString(data.playouts);
              case 8:
                return "-";
              default:
                return "";
            }
          } else {
            switch (col) {
              case 0:
                if (data.isBlack) return Lizzie.resourceBundle.getString("Movelistframe.black");
                return Lizzie.resourceBundle.getString("Movelistframe.white");
              case 1:
                return data.moveNum;
              case 2:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 3:
                return (data.diffWinrate > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate > 0 ? "↑" : "↓");
              case 4:
                return String.format("%.2f", data.winrate);
              case 5:
                if (data.previousPlayouts > 0) {
                  return String.format("%.2f", data.winrate - data.diffWinrate);
                } else {
                  return "";
                }
              case 6:
                return Lizzie.frame.getPlayoutsString(data.previousPlayouts);
              case 7:
                return Lizzie.frame.getPlayoutsString(data.playouts);
              case 8:
                return String.format(
                    "%.1f",
                    // Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
                //                if (data.previousPlayouts > 0
                //                    && data.moveNum <= (Lizzie.board.matchAiLastMove + 1)
                //                    && data.moveNum > Lizzie.board.matchAiFirstMove)
                //                  return data.isMatchAi ? "是" : "否";
                //                else return "无";
              default:
                return "";
            }
          }
        }
      }
    };
  }

  public AbstractTableModel getTableModelMin1() {
    return new AbstractTableModel() {
      public int getColumnCount() {
        if (isKatago) return 5;
        else return 4;
      }

      public int getRowCount() {
        int row = 0;

        BoardHistoryNode lastNode =
            showBranch.getSelectedIndex() == 0
                ? Lizzie.board.getHistory().getMainEnd()
                : Lizzie.board.getHistory().getEnd();
        while (lastNode.previous().isPresent()) {
          NodeInfo nodeInfoThis =
              showBranch.getSelectedIndex() == 0
                  ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                  : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
          if (nodeInfoThis.analyzed)
            if (nodeInfoThis.isBlack)
              if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
                if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                    && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                  if (!isKatago
                      || Math.abs(nodeInfoThis.scoreMeanDiff)
                          >= (int) dropScoreMeanChooser.getValue()) row = row + 1;
          lastNode = lastNode.previous().get();
        }
        NodeInfo nodeInfoThis =
            showBranch.getSelectedIndex() == 0
                ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
        if (nodeInfoThis.analyzed)
          if (nodeInfoThis.isBlack)
            if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
              if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                  && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                if (!isKatago
                    || Math.abs(nodeInfoThis.scoreMeanDiff)
                        >= (int) dropScoreMeanChooser.getValue()) row = row + 1;
        return row;
      }

      public String getColumnName(int column) {
        if (isKatago) {
          if (column == 0)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnBlack");
          if (column == 1)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnCoords");
          if (column == 2)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWinDiff");
          if (column == 3)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnScoreDiff");
          if (column == 4)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnAiScore");
        } else {
          if (column == 0)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnBlack");
          if (column == 1)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnCoords");
          if (column == 2)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWinDiff");
          if (column == 3)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnAiScore");
        }
        return "";
      }

      public Object getValueAt(int row, int col) {
        ArrayList<NodeInfo> data2 = new ArrayList<NodeInfo>();
        BoardHistoryNode lastNode =
            showBranch.getSelectedIndex() == 0
                ? Lizzie.board.getHistory().getMainEnd()
                : Lizzie.board.getHistory().getEnd();
        while (lastNode.previous().isPresent()) {
          NodeInfo nodeInfoThis =
              showBranch.getSelectedIndex() == 0
                  ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                  : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
          if (nodeInfoThis.analyzed)
            if (nodeInfoThis.isBlack)
              if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
                if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                    && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                  if (!isKatago
                      || Math.abs(nodeInfoThis.scoreMeanDiff)
                          >= (int) dropScoreMeanChooser.getValue()) data2.add(nodeInfoThis);
          lastNode = lastNode.previous().get();
        }
        NodeInfo nodeInfoThis =
            showBranch.getSelectedIndex() == 0
                ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
        if (nodeInfoThis.analyzed)
          if (nodeInfoThis.isBlack)
            if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
              if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                  && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                if (!isKatago
                    || Math.abs(nodeInfoThis.scoreMeanDiff)
                        >= (int) dropScoreMeanChooser.getValue()) data2.add(nodeInfoThis);
        Collections.sort(
            data2,
            new Comparator<NodeInfo>() {
              @Override
              public int compare(NodeInfo s1, NodeInfo s2) {
                // 降序
                if (isKatago) {
                  if (!issorted) {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return 1;
                      if (s1.moveNum < s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return 1;
                        if (s1.diffWinrate > s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.scoreMeanDiff) < Math.abs(s2.scoreMeanDiff)) return 1;
                        if (Math.abs(s1.scoreMeanDiff) > Math.abs(s2.scoreMeanDiff)) return -1;
                      } else {
                        if (s1.scoreMeanDiff < s2.scoreMeanDiff) return 1;
                        if (s1.scoreMeanDiff > s2.scoreMeanDiff) return -1;
                      }
                    }
                    if (sortnum == 4) {
                      if (s1.percentsMatch < s2.percentsMatch) return 1;
                      if (s1.percentsMatch > s2.percentsMatch) return -1;
                    }
                  } else {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return -1;
                      if (s1.moveNum < s2.moveNum) return 1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return -1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return 1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return -1;
                        if (s1.diffWinrate > s2.diffWinrate) return 1;
                      }
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.scoreMeanDiff) < Math.abs(s2.scoreMeanDiff)) return -1;
                        if (Math.abs(s1.scoreMeanDiff) > Math.abs(s2.scoreMeanDiff)) return 1;
                      } else {
                        if (s1.scoreMeanDiff < s2.scoreMeanDiff) return -1;
                        if (s1.scoreMeanDiff > s2.scoreMeanDiff) return 1;
                      }
                    }
                    if (sortnum == 4) {
                      if (s1.percentsMatch < s2.percentsMatch) return -1;
                      if (s1.percentsMatch > s2.percentsMatch) return 1;
                    }
                  }
                  return 0;
                } else {
                  if (!issorted) {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return 1;
                      if (s1.moveNum < s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return 1;
                        if (s1.diffWinrate > s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 3) {
                      if (s1.percentsMatch < s2.percentsMatch) return 1;
                      if (s1.percentsMatch > s2.percentsMatch) return -1;
                    }
                  } else {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return -1;
                      if (s1.moveNum < s2.moveNum) return 1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return -1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return 1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return -1;
                        if (s1.diffWinrate > s2.diffWinrate) return 1;
                      }
                    }
                    if (sortnum == 3) {
                      if (s1.percentsMatch < s2.percentsMatch) return -1;
                      if (s1.percentsMatch > s2.percentsMatch) return 1;
                    }
                  }
                  return 0;
                }
              }
            });

        NodeInfo data = data2.get(row);
        if (isKatago) {
          if (Lizzie.board.isPkBoard) {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate < 0 ? "↑" : "↓");

              case 3:
                return (data.scoreMeanDiff < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.scoreMeanDiff))
                    + (data.scoreMeanDiff < 0 ? "↑" : "↓");
              case 4:
                return "-";
              default:
                return "";
            }
          } else {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate > 0 ? "↑" : "↓");
              case 3:
                return (data.scoreMeanDiff > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.scoreMeanDiff))
                    + (data.scoreMeanDiff > 0 ? "↑" : "↓");
              case 4:
                return String.format(
                    "%.1f",
                    // Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
              default:
                return "";
            }
          }
        } else {
          if (Lizzie.board.isPkBoard) {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate < 0 ? "↑" : "↓");
              case 3:
                return String.format(
                    "%.1f",
                    //   Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
              default:
                return "";
            }
          } else {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate > 0 ? "↑" : "↓");
              case 3:
                return String.format(
                    "%.1f",
                    // Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
              default:
                return "";
            }
          }
        }
      }
    };
  }

  public AbstractTableModel getTableModelMin2() {
    return new AbstractTableModel() {
      public int getColumnCount() {
        if (isKatago) return 5;
        else return 4;
      }

      public int getRowCount() {
        int row = 0;

        BoardHistoryNode lastNode =
            showBranch.getSelectedIndex() == 0
                ? Lizzie.board.getHistory().getMainEnd()
                : Lizzie.board.getHistory().getEnd();
        while (lastNode.previous().isPresent()) {
          NodeInfo nodeInfoThis =
              showBranch.getSelectedIndex() == 0
                  ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                  : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
          if (nodeInfoThis.analyzed)
            if (!nodeInfoThis.isBlack)
              if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
                if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                    && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                  if (!isKatago
                      || Math.abs(nodeInfoThis.scoreMeanDiff)
                          >= (int) dropScoreMeanChooser.getValue()) row = row + 1;
          lastNode = lastNode.previous().get();
        }
        NodeInfo nodeInfoThis =
            showBranch.getSelectedIndex() == 0
                ? (isMainEngine ? lastNode.nodeInfoMain : lastNode.nodeInfoMain2)
                : (isMainEngine ? lastNode.nodeInfo : lastNode.nodeInfo2);
        if (nodeInfoThis.analyzed)
          if (!nodeInfoThis.isBlack)
            if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
              if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                  && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                if (!isKatago
                    || Math.abs(nodeInfoThis.scoreMeanDiff)
                        >= (int) dropScoreMeanChooser.getValue()) row = row + 1;
        return row;
      }

      public String getColumnName(int column) {
        if (isKatago) {
          if (column == 0)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWhite");
          if (column == 1)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnCoords");
          if (column == 2)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWinDiff");
          if (column == 3)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnScoreDiff");
          if (column == 4)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnAiScore");
        } else {
          if (column == 0)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWhite");
          if (column == 1)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnCoords");
          if (column == 2)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnWinDiff");
          if (column == 3)
            return Lizzie.resourceBundle.getString("Movelistframe.minTableColumnAiScore");
        }
        return "";
      }

      public Object getValueAt(int row, int col) {
        ArrayList<NodeInfo> data2 = new ArrayList<NodeInfo>();
        BoardHistoryNode lastNode =
            showBranch.getSelectedIndex() == 0
                ? Lizzie.board.getHistory().getMainEnd()
                : Lizzie.board.getHistory().getEnd();
        while (lastNode.previous().isPresent()) {
          NodeInfo nodeInfoThis =
              showBranch.getSelectedIndex() == 0 ? lastNode.nodeInfoMain : lastNode.nodeInfo;
          if (nodeInfoThis.analyzed)
            if (!nodeInfoThis.isBlack)
              if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
                if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                    && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                  if (!isKatago
                      || Math.abs(nodeInfoThis.scoreMeanDiff)
                          >= (int) dropScoreMeanChooser.getValue()) data2.add(nodeInfoThis);
          lastNode = lastNode.previous().get();
        }
        NodeInfo nodeInfoThis =
            showBranch.getSelectedIndex() == 0 ? lastNode.nodeInfoMain : lastNode.nodeInfo;
        if (nodeInfoThis.analyzed)
          if (!nodeInfoThis.isBlack)
            if (Math.abs(nodeInfoThis.diffWinrate) >= (int) dropwinratechooser.getValue())
              if (nodeInfoThis.playouts >= (int) playoutschooser.getValue()
                  && nodeInfoThis.previousPlayouts >= (int) playoutschooser.getValue())
                if (!isKatago
                    || Math.abs(nodeInfoThis.scoreMeanDiff)
                        >= (int) dropScoreMeanChooser.getValue()) data2.add(nodeInfoThis);
        Collections.sort(
            data2,
            new Comparator<NodeInfo>() {
              @Override
              public int compare(NodeInfo s1, NodeInfo s2) {
                // 降序
                if (isKatago) {
                  if (!issorted) {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return 1;
                      if (s1.moveNum < s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return 1;
                        if (s1.diffWinrate > s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.scoreMeanDiff) < Math.abs(s2.scoreMeanDiff)) return 1;
                        if (Math.abs(s1.scoreMeanDiff) > Math.abs(s2.scoreMeanDiff)) return -1;
                      } else {
                        if (s1.scoreMeanDiff < s2.scoreMeanDiff) return 1;
                        if (s1.scoreMeanDiff > s2.scoreMeanDiff) return -1;
                      }
                    }
                    if (sortnum == 4) {
                      if (s1.percentsMatch < s2.percentsMatch) return 1;
                      if (s1.percentsMatch > s2.percentsMatch) return -1;
                    }
                  } else {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return -1;
                      if (s1.moveNum < s2.moveNum) return 1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return -1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return 1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return -1;
                        if (s1.diffWinrate > s2.diffWinrate) return 1;
                      }
                    }
                    if (sortnum == 3) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.scoreMeanDiff) < Math.abs(s2.scoreMeanDiff)) return -1;
                        if (Math.abs(s1.scoreMeanDiff) > Math.abs(s2.scoreMeanDiff)) return 1;
                      } else {
                        if (s1.scoreMeanDiff < s2.scoreMeanDiff) return -1;
                        if (s1.scoreMeanDiff > s2.scoreMeanDiff) return 1;
                      }
                    }
                    if (sortnum == 4) {
                      if (s1.percentsMatch < s2.percentsMatch) return -1;
                      if (s1.percentsMatch > s2.percentsMatch) return 1;
                    }
                  }
                  return 0;
                } else {
                  if (!issorted) {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return 1;
                      if (s1.moveNum < s2.moveNum) return -1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return 1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return -1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return 1;
                        if (s1.diffWinrate > s2.diffWinrate) return -1;
                      }
                    }
                    if (sortnum == 3) {
                      if (s1.percentsMatch < s2.percentsMatch) return 1;
                      if (s1.percentsMatch > s2.percentsMatch) return -1;
                    }
                  } else {
                    if (sortnum == 0) {
                      if (s1.moveNum > s2.moveNum) return -1;
                      if (s1.moveNum < s2.moveNum) return 1;
                    }
                    if (sortnum == 2) {
                      if (isOriginOrder) {
                        if (Math.abs(s1.diffWinrate) < Math.abs(s2.diffWinrate)) return -1;
                        if (Math.abs(s1.diffWinrate) > Math.abs(s2.diffWinrate)) return 1;
                      } else {
                        if (s1.diffWinrate < s2.diffWinrate) return -1;
                        if (s1.diffWinrate > s2.diffWinrate) return 1;
                      }
                    }
                    if (sortnum == 3) {
                      if (s1.percentsMatch < s2.percentsMatch) return -1;
                      if (s1.percentsMatch > s2.percentsMatch) return 1;
                    }
                  }
                  return 0;
                }
              }
            });
        NodeInfo data = data2.get(row);
        if (isKatago) {
          if (Lizzie.board.isPkBoard) {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate < 0 ? "↑" : "↓");
              case 3:
                return (data.scoreMeanDiff < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.scoreMeanDiff))
                    + (data.scoreMeanDiff < 0 ? "↑" : "↓");
              case 4:
                return "-";
              default:
                return "";
            }
          } else {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate > 0 ? "↑" : "↓");
              case 3:
                return (data.scoreMeanDiff > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.scoreMeanDiff))
                    + (data.scoreMeanDiff > 0 ? "↑" : "↓");
              case 4:
                return String.format(
                    "%.1f",
                    // Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
              default:
                return "";
            }
          }
        } else {
          if (Lizzie.board.isPkBoard) {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate < 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate < 0 ? "↑" : "↓");
              case 3:
                return String.format(
                    "%.1f",
                    // Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
              default:
                return "";
            }
          } else {
            switch (col) {
              case 0:
                return data.moveNum;
              case 1:
                return Board.convertCoordinatesToName(data.coords[0], data.coords[1]);
              case 2:
                return (data.diffWinrate > 0 ? "+" : "-")
                    + String.format("%.2f", Math.abs(data.diffWinrate))
                    + (data.diffWinrate > 0 ? "↑" : "↓");
              case 3:
                return String.format(
                    "%.1f",
                    // Math.pow(data.percentsMatch, (double) 1 / Lizzie.config.matchAiTemperature)
                    data.percentsMatch * 100);
              default:
                return "";
            }
          }
        }
      }
    };
  }
}

class bigMistakeInfo {
  boolean isBlack;
  int[] coords;
  int moveNumber;
  Double diffWinrate;
  Double currentMoveWinRate;
}
//  public  JFrame createBadmovesDialog() {
//    // Create and set up the window.
//    jf = new JFrame();
//    jf.setTitle(
//        "超级鹰眼"
//            + (Lizzie.frame.extraMode == 2 ? "(主)" : "")
//            + ","
//            + "黑["
//            + Lizzie.board.getHistory().getGameInfo().getPlayerBlack()
//            + "]白["
//            + Lizzie.board.getHistory().getGameInfo().getPlayerWhite()
//            + "]"
//            + ",B显示/关闭,点击列表跳转,Q切换总在最前");
//
//    jf.addWindowListener(
//        new WindowAdapter() {
//          public void windowClosing(WindowEvent e) {
//            Lizzie.frame.toggleBadMoves(true);
//          }
//        });
//
//    final MovelistFrame newContentPane = new MovelistFrame();
//    newContentPane.setOpaque(true); // content panes must be opaque
//    jf.setContentPane(newContentPane);
//    // Display the window.
//    // jf.setSize(521, 320);
//
//    boolean persisted = Lizzie.config.persistedUi != null;
//    if (persisted
//        && Lizzie.config.persistedUi.optJSONArray("badmoves-list-position") != null
//        && Lizzie.config.persistedUi.optJSONArray("badmoves-list-position").length() >= 5) {
//      JSONArray pos = Lizzie.config.persistedUi.getJSONArray("badmoves-list-position");
//      jf.setBounds(pos.getInt(1), pos.getInt(2), pos.getInt(3), pos.getInt(4));
//      Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
//      int width = (int) screensize.getWidth();
//      int height = (int) screensize.getHeight();
//      if (pos.getInt(0) >= width || pos.getInt(1) >= height) jf.setLocation(0, 0);
//    } else {
//      jf.setBounds(-9, 0, 746, 487);
//    }
//    try {
//      jf.setIconImage(ImageIO.read(MovelistFrame.class.getResourceAsStream("/assets/logo.png")));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    if (Lizzie.config.badmovesalwaysontop) jf.setAlwaysOnTop(true);
//    // jf.setResizable(false);
//    return jf;
//  }
// }
