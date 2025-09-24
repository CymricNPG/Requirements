package net.npg.requirements.ui;

import javax.swing.*;
import java.awt.event.*;

public class RequirementDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idTextField;
    private JTextField summaryTextField;
    private JComboBox statusComboBox;
    private JComboBox priorityComboBox;
    private JComboBox responsibleComboBox;
    private JComboBox typeComboBox;
    private JList labelsList;
    private JTextField createdTextField;
    private JLabel idLabel;
    private JLabel summaryLabel;
    private JLabel statusLabel;
    private JLabel priorityLabel;
    private JLabel responsibleLabel;
    private JLabel typeLabel;
    private JLabel labelsLabel;
    private JLabel createdLabel;

    public RequirementDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}