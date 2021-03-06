/*
 * Tela de login para autenticação de usuários
 */
package biblio.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.Timer;

/**
 *
 * @author leonardosilva
 */
public class DlgLogin extends javax.swing.JDialog {
    
    private Timer timer;  // To control the progress bar

    /**
     * Creates new form dlgLogin
     */
    public DlgLogin(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        lblLogin = new javax.swing.JLabel();
        lblSenha = new javax.swing.JLabel();
        tfLogin = new javax.swing.JTextField();
        btnConfirmar = new javax.swing.JButton();
        btnCancela = new javax.swing.JButton();
        pfSenha = new javax.swing.JPasswordField();
        lblNote = new javax.swing.JLabel();
        pnlImage = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        prbLogin = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema de Gerenciamento da Biblioteca");
        setAlwaysOnTop(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Authentication"));
        mainPanel.setToolTipText("");

        lblLogin.setText("Username:");

        lblSenha.setText("Password:");

        btnConfirmar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/Unlock-icon.png"))); // NOI18N
        btnConfirmar.setText("Login");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        btnCancela.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCancela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/close-icon.png"))); // NOI18N
        btnCancela.setText("Cancel");
        btnCancela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelaActionPerformed(evt);
            }
        });

        pfSenha.setToolTipText("");

        lblNote.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        lblNote.setForeground(new java.awt.Color(255, 51, 51));
        lblNote.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNote.setText("NOTE: Unauthorized Access Is Not Allowed");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLogin)
                            .addComponent(lblSenha))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(btnConfirmar)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancela))
                            .addComponent(tfLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addComponent(pfSenha))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLogin)
                    .addComponent(tfLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSenha)
                    .addComponent(pfSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmar)
                    .addComponent(btnCancela))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNote)
                .addGap(40, 40, 40))
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnCancela, btnConfirmar});

        lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/login.png"))); // NOI18N

        javax.swing.GroupLayout pnlImageLayout = new javax.swing.GroupLayout(pnlImage);
        pnlImage.setLayout(pnlImageLayout);
        pnlImageLayout.setHorizontalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImageLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblImage)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlImageLayout.setVerticalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );

        prbLogin.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prbLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prbLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        biblio.control.Main.setUserLogin(tfLogin.getText());
        this.closeFrame();
        timer.start();  // For the progressBar
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        this.closeFrame();
    }//GEN-LAST:event_btnCancelaActionPerformed

    public class ProgressBarEvent implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int counter = prbLogin.getValue();
            if (counter < 100) {
                counter++;
                prbLogin.setValue(counter);
            } else {
                timer.stop();  // To stop the progressBar
            }
        }
        
    }
    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // Configure the speed and event controler of the progressbar 
        timer = new Timer(50, new ProgressBarEvent());
    }//GEN-LAST:event_formWindowOpened

    private void closeFrame() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); 
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancela;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField pfSenha;
    private javax.swing.JPanel pnlImage;
    private javax.swing.JProgressBar prbLogin;
    private javax.swing.JTextField tfLogin;
    // End of variables declaration//GEN-END:variables
}
