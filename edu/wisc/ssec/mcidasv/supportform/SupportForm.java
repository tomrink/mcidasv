/*
 * $Id$
 *
 * This file is part of McIDAS-V
 *
 * Copyright 2007-2012
 * Space Science and Engineering Center (SSEC)
 * University of Wisconsin - Madison
 * 1225 W. Dayton Street, Madison, WI 53706, USA
 * http://www.ssec.wisc.edu/mcidas
 * 
 * All Rights Reserved
 * 
 * McIDAS-V is built on Unidata's IDV and SSEC's VisAD libraries, and
 * some McIDAS-V source code is based on IDV and VisAD source code.  
 * 
 * McIDAS-V is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * McIDAS-V is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */
package edu.wisc.ssec.mcidasv.supportform;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import ucar.unidata.idv.IdvObjectStore;
import ucar.unidata.idv.IntegratedDataViewer;
import ucar.unidata.idv.ui.IdvUIManager;

import edu.wisc.ssec.mcidasv.util.CollectionHelpers;
import edu.wisc.ssec.mcidasv.util.BackgroundTask;
import edu.wisc.ssec.mcidasv.util.Contract;

public class SupportForm extends javax.swing.JFrame {

    private static final String HELP_ID = "idv.tools.supportrequestform";

    private static ExecutorService exec = Executors.newCachedThreadPool();

    private final IdvObjectStore store;

    private final StateCollector collector;

    private final CancelListener listener = new CancelListener();

    public SupportForm(IdvObjectStore store, StateCollector collector) {
        this.store = Contract.notNull(store);
        this.collector = Contract.notNull(collector);
        unpersistInput();
        initComponents();
        otherDoFocusThingNow();
    }

    private void persistInput() {
        store.put(IdvUIManager.PROP_HELP_NAME, getUser());
        store.put(IdvUIManager.PROP_HELP_EMAIL, getEmail());
        store.put(IdvUIManager.PROP_HELP_ORG, getOrganization());
        store.put("mcv.supportreq.cc", getSendCopy());
        store.put("mcv.supportreq.bundle", getSendBundle());
        store.save();
    }

    private void unpersistInput() {
        userField.setText(store.get(IdvUIManager.PROP_HELP_NAME, ""));
        emailField.setText(store.get(IdvUIManager.PROP_HELP_EMAIL, ""));
        organizationField.setText(store.get(IdvUIManager.PROP_HELP_ORG, ""));
        ccCheckBox.setSelected(store.get("mcv.supportreq.cc", true));
        bundleCheckBox.setSelected(store.get("mcv.supportreq.bundle", false));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Request McIDAS-V Support");

        userLabel.setText("Name:");
        userLabel.setToolTipText("");

        userField.setToolTipText("");
        userField.setName("user"); // NOI18N

        emailLabel.setText("Your Email:");

        emailField.setName("email"); // NOI18N

        organizationLabel.setText("Organization:");

        organizationField.setName("organization"); // NOI18N

        subjectLabel.setText("Subject:");

        subjectField.setName("subject"); // NOI18N

        descriptiveLabel.setText("Please provide a thorough description of the problem you encountered.");

        descriptionLabel.setText("Description:");

        descriptionArea.setColumns(20);
        descriptionArea.setRows(5);
        descriptionArea.setName("description"); // NOI18N
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        descriptionScroller.setViewportView(descriptionArea);

        attachmentOneField.setName("attachment1"); // NOI18N
        attachmentOneField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                attachmentOneFieldMouseClicked(evt);
            }
        });

        attachmentOneLabel.setText("Attachment 1:");

        attachmentTwoLabel.setText("Attachment 2:");

        attachmentOneButton.setText("Browse...");
        attachmentOneButton.setToolTipText("Allows you to locate and attach a file relevant to your request.");
        attachmentOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachmentOneButtonActionPerformed(evt);
            }
        });

        attachmentTwoField.setName("attachment2"); // NOI18N
        attachmentTwoField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                attachmentTwoFieldMouseClicked(evt);
            }
        });

        attachmentTwoButton.setText("Browse...");
        attachmentTwoButton.setToolTipText("Allows you to locate and attach a file relevant to your request.");
        attachmentTwoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachmentTwoButtonActionPerformed(evt);
            }
        });

        bundleCheckBox.setText("Include current state as a bundle");
        bundleCheckBox.setName("sendstate"); // NOI18N

        ccCheckBox.setText("Send copy of support request to me");
        ccCheckBox.setToolTipText("Adds your email address to the CC field of the support request.");
        ccCheckBox.setName("ccrequest"); // NOI18N

        sendButton.setText("Send Request");
        sendButton.setToolTipText("Sends your support request to the McIDAS Help Desk.");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendRequest(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Closes this window and does not submit a support request.");
        cancelButton.addActionListener(listener);

        helpButton.setText("Help");
        helpButton.setToolTipText("Display the help page for the McIDAS-V support request form.");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ucar.unidata.ui.Help.getDefaultHelp().gotoTarget(HELP_ID);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(userLabel)
                            .addComponent(attachmentTwoLabel)
                            .addComponent(descriptionLabel)
                            .addComponent(subjectLabel)
                            .addComponent(organizationLabel)
                            .addComponent(emailLabel)
                            .addComponent(attachmentOneLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(descriptionScroller, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                            .addComponent(ccCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bundleCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                            .addComponent(organizationField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                            .addComponent(subjectField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                            .addComponent(descriptiveLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(attachmentTwoField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                                    .addComponent(attachmentOneField, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(attachmentTwoButton)
                                    .addComponent(attachmentOneButton)))
                            .addComponent(userField, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(helpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLabel)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(organizationLabel)
                    .addComponent(organizationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectLabel)
                    .addComponent(subjectField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptiveLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(attachmentOneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(attachmentOneLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(attachmentTwoLabel)
                            .addComponent(attachmentTwoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(attachmentTwoButton)))
                    .addComponent(attachmentOneButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bundleCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ccCheckBox)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(cancelButton)
                    .addComponent(helpButton)))
        );

        List<JComponent> order = CollectionHelpers.list((JComponent)userField, 
            emailField, organizationField, subjectField, descriptionArea, 
            attachmentOneButton, attachmentOneField, attachmentTwoButton, 
            attachmentTwoField, bundleCheckBox, ccCheckBox, sendButton, 
            cancelButton, helpButton);

        SupportFormTraversalPolicy traversal = 
            new SupportFormTraversalPolicy(order);
        setFocusTraversalPolicy(traversal);

        pack();
    }// </editor-fold>

    private void attachmentOneButtonActionPerformed(java.awt.event.ActionEvent evt) {
        attachFileToField(attachmentOneField);
    }

    private void attachmentTwoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        attachFileToField(attachmentTwoField);
    }

    private void attachmentOneFieldMouseClicked(java.awt.event.MouseEvent evt) {
        if (attachmentOneField.getText().length() == 0) {
            attachFileToField(attachmentOneField);
        }
    }

    private void attachmentTwoFieldMouseClicked(java.awt.event.MouseEvent evt) {
        if (attachmentTwoField.getText().length() == 0) {
            attachFileToField(attachmentTwoField);
        }
    }

    private static void attachFileToField(final JTextField field) {
        String current = field.getText();
        JFileChooser jfc = new JFileChooser(current);
        if (jfc.showOpenDialog(field) == JFileChooser.APPROVE_OPTION) {
            field.setText(jfc.getSelectedFile().toString());
        }
    }

    /**
     * Checks to see if there is <i>anything</i> in the name, email, subject,
     * and description.
     * 
     * @return {@code true} if all of the required fields have some sort of 
     * input, {@code false} otherwise.
     */
    private boolean validInput() {
        if (userField.getText().length() == 0) {
            return false;
        }
        if (emailField.getText().length() == 0) {
            return false;
        }
        if (subjectField.getText().length() == 0) {
            return false;
        }
        if (descriptionArea.getText().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Due to some fields persisting user input between McIDAS-V sessions we
     * set the focus to be on the first of these fields <i>lacking</i> input.
     */
    private void otherDoFocusThingNow() {
        List<JTextComponent> comps = CollectionHelpers.list(userField, 
            emailField, organizationField, subjectField, descriptionArea);

        for (JTextComponent comp : comps) {
            if (comp.getText().length() == 0) {
                comp.requestFocus(true);
                break;
            }
        }
    }

    /**
     * Returns whatever currently lives in {@link #emailField}.
     * 
     * @return User's email address.
     */
    public String getEmail() {
        return emailField.getText();
    }

    /**
     * Returns whatever occupies {@link #userField}.
     * 
     * @return User's name.
     */
    public String getUser() {
        return userField.getText();
    }

    /**
     * Returns whatever resides in {@link #subjectField}.
     * 
     * @return Subject of the support request.
     */
    public String getSubject() {
        return subjectField.getText();
    }

    /**
     * Returns whatever has commandeered {@link #organizationField}.
     * 
     * @return Organization to which the user belongs.
     */
    public String getOrganization() {
        return organizationField.getText();
    }

    /**
     * Returns whatever is ensconced inside {@link #descriptionArea}.
     * 
     * @return Body of the user's email.
     */
    public String getDescription() {
        return descriptionArea.getText();
    }

    /**
     * Checks whether or not the user has attached a file in the 
     * {@literal "first file"} slot.
     * 
     * @return {@code true} if there's a file, {@code false} otherwise.
     */
    public boolean hasAttachmentOne() {
        return new File(attachmentOneField.getText()).exists();
    }

    /**
     * Checks whether or not the user has attached a file in the 
     * {@literal "second file"} slot.
     * 
     * @return {@code true} if there's a file, {@code false} otherwise.
     */
    public boolean hasAttachmentTwo() {
        return new File(attachmentTwoField.getText()).exists();
    }

    /**
     * Returns whatever file path has monopolized {@link #attachmentOneField}.
     * 
     * @return Path to the first file attachment, or a blank string if no file
     * has been selected.
     */
    public String getAttachmentOne() {
        return attachmentOneField.getText();
    }

    /**
     * Returns whatever file path has appeared within 
     * {@link #attachmentTwoField}.
     * 
     * @return Path to the second file attachment, or a blank string if no 
     * file has been selected.
     */
    public String getAttachmentTwo() {
        return attachmentTwoField.getText();
    }

    // TODO: javadocs!
    public boolean getSendCopy() {
        return ccCheckBox.isSelected();
    }

    public boolean getSendBundle() {
        return bundleCheckBox.isSelected();
    }

    public byte[] getExtraState() {
        return collector.getContents();
    }

    public String getExtraStateName() {
        return collector.getExtraAttachmentName();
    }

    public boolean canBundleState() {
        return collector.canBundleState();
    }

    public byte[] getBundledState() {
        return collector.getBundledState();
    }

    public String getBundledStateName() {
        return collector.getBundleAttachmentName();
    }

    public boolean canSendLog() {
        String path = collector.getLogPath();
        if (path == null || path.length() == 0) {
            return false;
        }
        return new File(path).exists();
    }

    public String getLogPath() {
        return collector.getLogPath();
    }

    // TODO: dialogs are bad news bears.
    public void showSuccess() {
        JOptionPane.showMessageDialog(this, "Support request sent successfully.", "Success", JOptionPane.DEFAULT_OPTION);
    }

    // TODO: dialogs are bad news hares.
    public void showFailure(final String reason) {
        String msg = "";
        if (reason == null || reason.length() == 0) {
            msg = "Error sending request, could not determine cause.";
        } else {
            msg = "Error sending request:\n"+reason;
        }
        JOptionPane.showMessageDialog(this, msg, "Problem sending support request", JOptionPane.ERROR_MESSAGE);
    }

    private class CancelListener implements ActionListener {
        BackgroundTask<?> task;
        public void actionPerformed(ActionEvent e) {
            if (task != null) {
                task.cancel(true);
            } 
            setVisible(false);
            dispose();
        }
    }

    private void showInvalidInputs() {
        // how to display these?
        JOptionPane.showMessageDialog(this, "You must provide at least your name, email address, subject, and description.", "Missing required input", JOptionPane.ERROR_MESSAGE);
    }

    private void sendRequest(java.awt.event.ActionEvent evt) {
        // check input validity
        if (!validInput()) {
            showInvalidInputs();
            return;
        }

        // persist things that need it.
        persistInput();

        // create a background thread
        listener.task = new Submitter(this);

        // send the worker thread to the mines
        exec.execute(listener.task);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    IntegratedDataViewer idv = new IntegratedDataViewer();
                    new SupportForm(idv.getStore(), new SimpleStateCollector()).setVisible(true);
                } catch (Exception e) {
                    System.err.println("Couldn't create an IDV object: "+e.getMessage());
                }
            }
        });
    }

    /* (non-javadoc)
     * simplistic traversal policy, merely traverse components according to 
     * their order in the list you used when creating the policy.
     */
    public static class SupportFormTraversalPolicy extends FocusTraversalPolicy {
        private final List<JComponent> ordering = CollectionHelpers.arrList();

        public SupportFormTraversalPolicy(List<JComponent> ordering) {
            this.ordering.addAll(ordering);
        }

        public JComponent getComponentAfter(Container focusCycleRoot, 
            Component aComponent) 
        {
            int idx = (ordering.indexOf(aComponent) + 1) % ordering.size();
            return ordering.get(idx);
        }

        public JComponent getComponentBefore(Container focusCycleRoot, 
            Component aComponent) 
        {
            int idx = ordering.indexOf(aComponent) - 1;
            if (idx < 0) {
                idx = ordering.size() - 1;
            }
            return ordering.get(idx);
        }

        public JComponent getDefaultComponent(Container focusCycleRoot) {
            return ordering.get(0);
        }

        public JComponent getLastComponent(Container focusCycleRoot) {
            return ordering.get(ordering.size() - 1);
        }

        public JComponent getFirstComponent(Container focusCycleRoot) {
            return ordering.get(0);
        }
    }

    private JLabel userLabel = new JLabel();
    private JTextField userField = new JTextField();
    private JLabel emailLabel = new JLabel();
    private JTextField emailField = new JTextField();
    private JLabel organizationLabel = new JLabel();
    private JTextField organizationField = new JTextField();
    private JLabel subjectLabel = new JLabel();
    private JTextField subjectField = new JTextField();
    private JLabel descriptiveLabel = new JLabel();
    private JLabel descriptionLabel = new JLabel();
    private JScrollPane descriptionScroller = new JScrollPane();
    private JTextArea descriptionArea = new JTextArea();
    private JTextField attachmentOneField = new JTextField();
    private JLabel attachmentOneLabel = new JLabel();
    private JLabel attachmentTwoLabel = new JLabel();
    private JButton attachmentOneButton = new JButton();
    private JTextField attachmentTwoField = new JTextField();
    private JButton attachmentTwoButton = new JButton();
    private JCheckBox bundleCheckBox = new JCheckBox();
    private JCheckBox ccCheckBox = new JCheckBox();
    private JButton sendButton = new JButton();
    private JButton cancelButton = new JButton();
    private JButton helpButton = new JButton();
}
