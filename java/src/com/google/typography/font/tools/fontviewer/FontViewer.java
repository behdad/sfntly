package com.google.typography.font.tools.fontviewer;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;

import java.awt.Container;
import java.awt.Dimension;
import java.io.FileInputStream;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * Shows the hierarchy of some of the tables of a font.
 */
public class FontViewer {

  private final JFrame frame;
  private final JScrollPane mainScrollPane;

  FontViewer(Font font) {
    JScrollPane fontPane = createFontTree(font);
    this.mainScrollPane = createMainPane();
    this.frame = createFrame(fontPane, this.mainScrollPane);
  }

  private JScrollPane createFontTree(Font font) {
    TreeModel model = new DefaultTreeModel(new FontNode(font));
    JTree fontTree = new JTree(model);
    fontTree.setBorder(new EmptyBorder(3, 3, 3, 3));
    fontTree.addTreeSelectionListener(new TreeSelectionListener() {
      @Override
      public void valueChanged(TreeSelectionEvent e) {
        render((AbstractNode) e.getPath().getLastPathComponent());
      }
    });

    JScrollPane fontPane = new JScrollPane(fontTree);
    fontPane.setPreferredSize(new Dimension(300, 500));
    return fontPane;
  }

  private static JScrollPane createMainPane() {
    JScrollPane pane = new JScrollPane();
    pane.add(new JTextArea());
    pane.setPreferredSize(new Dimension(500, 500));
    return pane;
  }

  private JFrame createFrame(JScrollPane fontPane, JScrollPane mainPane) {
    JFrame frame = new JFrame("Font Viewer");
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    Container content = frame.getContentPane();
    JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, fontPane, mainPane);
    content.add(pane);
    frame.pack();
    frame.setLocationRelativeTo(null);
    return frame;
  }

  private void render(AbstractNode node) {
    JComponent mainComponent = node.render();
    mainComponent.setBorder(new EmptyBorder(3, 3, 3, 3));
    this.mainScrollPane.setViewportView(mainComponent);
    this.mainScrollPane.revalidate();
    this.mainScrollPane.repaint();
  }

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    Font font = FontFactory.getInstance().loadFonts(new FileInputStream(args[0]))[0];
    FontViewer viewer = new FontViewer(font);
    viewer.frame.setVisible(true);
  }

}
