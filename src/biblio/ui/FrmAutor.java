/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblio.ui;

import java.sql.*;
import biblio.control.DBUtilities;
import biblio.db.ResultSetTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author leonardosilva
 */
public class FrmAutor extends javax.swing.JFrame {
    private byte estado=EDICAO;
    
    private static final byte INCLUSAO = 0;
    private static final byte ALTERACAO = 1;
    private static final byte EDICAO = 2;
    private static final byte CONSULTA = 3;
    
    /**
     * Creates new form FrmAutor
     */
    public FrmAutor() {
        initComponents();
        this.enableButtons(estado);
        try {
            this.loadRecords(0);
        } catch (SQLException ex) {
            System.out.println("The following error has occured: " + ex.getMessage());
        }
    }
    
    private void enableButtons(byte state) {
        switch(state) {
            case INCLUSAO: {
                btnIncluir.setEnabled(false);
                btnAlterar.setEnabled(false);
                btnExcluir.setEnabled(false);
                btnConsultar.setEnabled(false);
                btnSalva.setEnabled(true);
                btnCancela.setEnabled(true);
                btnEfetuarBusca.setVisible(false);
                tfCodAutor.setEnabled(false);
                tfNomeAutor.setEditable(true);
                ftfDatNasc.setEditable(true);
                tblAutor.setEnabled(false);
                break;
            }
            case ALTERACAO: {
                btnIncluir.setEnabled(false);
                btnAlterar.setEnabled(false);
                btnExcluir.setEnabled(false);
                btnConsultar.setEnabled(false);
                btnSalva.setEnabled(true);
                btnCancela.setEnabled(true);
                btnEfetuarBusca.setVisible(false);
                tfCodAutor.setEnabled(false);
                tfNomeAutor.setEditable(true);
                tfNomeAutor.requestFocus();
                ftfDatNasc.setEditable(true);
                tblAutor.setEnabled(false);
                break;
            }
            case EDICAO: {
                btnIncluir.setEnabled(true);
                btnAlterar.setEnabled(true);
                btnExcluir.setEnabled(true);
                btnConsultar.setEnabled(true);
                btnSalva.setEnabled(false);
                btnCancela.setEnabled(false);
                btnEfetuarBusca.setVisible(false);
                tfCodAutor.setEnabled(false);
                tfNomeAutor.setEditable(false);
                ftfDatNasc.setEditable(false);
                tblAutor.setEnabled(true);
                tblAutor.requestFocus();
                break;
            }
            case CONSULTA: {
                btnIncluir.setEnabled(false);
                btnAlterar.setEnabled(false);
                btnExcluir.setEnabled(false);
                btnConsultar.setEnabled(false);
                btnSalva.setEnabled(false);
                btnCancela.setEnabled(true);
                btnEfetuarBusca.setVisible(true);
                tfCodAutor.setEnabled(true);
                tfNomeAutor.setEditable(true);
                ftfDatNasc.setEditable(false);
                tblAutor.setEnabled(false);
                tblAutor.requestFocus();
                break;
            }
        }
    }

    private byte getEstado() {
        return this.estado;
    }
    
    private void setEstado(byte estado) {
        this.estado = estado;
        this.enableButtons(estado);
    }
    
    private void clearInputBoxes() {
        tfCodAutor.setText("");
        tfNomeAutor.setText("");
        if (this.getEstado() == INCLUSAO) {
           ftfDatNasc.setValue(new Date());
        } else {
           ftfDatNasc.setText(""); 
        }
    }

    private String getDatNascAsString() {
        // Converte a data de nascimento no formato dd/MM/yyyy
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date enteredDate = (Date)ftfDatNasc.getValue();
        String reportDate = df.format(enteredDate) + " 00:00:00";
        return reportDate;
    }
    
    private int addNew() {
        ResultSet result;
        int lastInsertedID=-1;
        String sql_stmt = "INSERT INTO Autor (nome_autor, dat_nasc_autor)";
        sql_stmt += "VALUES('" + tfNomeAutor.getText() + "'";
        sql_stmt += ", '" + getDatNascAsString() + "')";

        DBUtilities dbUtilities = new DBUtilities();

        try {
            dbUtilities.executeSQLStatement(sql_stmt);
 
            result = dbUtilities.executeSQLQuery("SELECT LAST_INSERT_ID()");
            if (result.next()) {
                lastInsertedID = result.getInt(1);
            }            
        } catch (SQLException ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro ao acessar o BD: " + ex.getMessage(), "Erro");
        }
        
        return lastInsertedID;
    }

    private void updateRecord() {
        String sql_stmt = "UPDATE autor SET nome_autor = '" + tfNomeAutor.getText() + "'";
        sql_stmt += ", dat_nasc_autor = '" + getDatNascAsString() + "'";
        sql_stmt += " WHERE cod_autor = " + tfCodAutor.getText();

        DBUtilities dbUtilities = new DBUtilities();

        try {
            dbUtilities.executeSQLStatement(sql_stmt);
        } catch (SQLException ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro de acesso ao BD: " + ex.getMessage(), "Erro");
        }
    }

    private void deleteRecord() throws SQLException {
        String sql_stmt = "DELETE FROM autor WHERE cod_autor = '" + tfCodAutor.getText() + "'";

        DBUtilities dbUtilities = new DBUtilities();

        dbUtilities.executeSQLStatement(sql_stmt);
    }

    private void loadRecords(int idIndex) throws SQLException {

        String sql_stmt = "SELECT CAST(cod_autor as SIGNED) as 'Codigo', CONCAT(nome_autor,'') as 'Nome', ";
        //sql_stmt += "dat_nasc_autor FROM autor;";
        sql_stmt += "DATE_FORMAT(dat_nasc_autor,'%d/%m/%Y') as Nascimento FROM autor;";
        
        ResultSetTableModel tableModel = new ResultSetTableModel(sql_stmt);

        tblAutor.setModel(tableModel);

        tblAutor.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            try {
                if (tblAutor.getSelectedRow() >= 0) {
                    Object cod_autor = tblAutor.getValueAt(tblAutor.getSelectedRow(), 0);
                    Object nome_autor = tblAutor.getValueAt(tblAutor.getSelectedRow(), 1);
                    Object dat_nasc_autor = tblAutor.getValueAt(tblAutor.getSelectedRow(), 2);

                    tfCodAutor.setText(cod_autor.toString());
                    tfNomeAutor.setText(nome_autor.toString());
                    if (dat_nasc_autor == null)
                        ftfDatNasc.setText("");
                    else
                        ftfDatNasc.setText(dat_nasc_autor.toString());
                }
            } catch (Exception ex) {
                biblio.control.FrameUtil.showMessage(this, "Erro ao ler tabela de autores: " + ex.getMessage(), "Erro");
            }
        });

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblAutor.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        // Posicionando
        if (idIndex >= 0) {
           tblAutor.setRowSelectionInterval(idIndex, idIndex);
        }
    }    

    private int getRowByValue(Object value) {
        for (int i = tblAutor.getRowCount() - 1; i >= 0; --i) {
            if (tblAutor.getValueAt(i, 0).equals(value)) {
                // what if value is not unique?
                return i;
            }
        }
        return -1;
    }
    
    private void updateFields() {
        Object cod_autor = tblAutor.getValueAt(tblAutor.getSelectedRow(), 0);
        Object nome_autor = tblAutor.getValueAt(tblAutor.getSelectedRow(), 1);

        tfCodAutor.setText(cod_autor.toString());
        tfNomeAutor.setText(nome_autor.toString());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblAutor = new javax.swing.JTable();
        pnlData = new javax.swing.JPanel();
        lblID = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        tfCodAutor = new javax.swing.JTextField();
        tfNomeAutor = new javax.swing.JTextField();
        btnEfetuarBusca = new javax.swing.JButton();
        lblDatNasc = new javax.swing.JLabel();
        ftfDatNasc = new javax.swing.JFormattedTextField();
        pnlButtons = new javax.swing.JPanel();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnSalva = new javax.swing.JButton();
        btnCancela = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cadastro de Autores");
        setAlwaysOnTop(true);

        tblAutor.setAutoCreateRowSorter(true);
        tblAutor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Codigo", "Nome", "Nascimento"
            }
        ));
        tblAutor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblAutor.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAutor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAutorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAutor);

        pnlData.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados dos Autores"));

        lblID.setText("Código: ");

        lblNome.setText("Nome: ");

        tfCodAutor.setEnabled(false);
        tfCodAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfCodAutorActionPerformed(evt);
            }
        });

        btnEfetuarBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/search_button24x24.png"))); // NOI18N
        btnEfetuarBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEfetuarBuscaActionPerformed(evt);
            }
        });

        lblDatNasc.setText("Data Nascimento: ");
        lblDatNasc.setToolTipText("");

        ftfDatNasc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("d/M/yyyy"))));
        ftfDatNasc.setToolTipText("Data de nascimento no formato dd/mm/aaaa. Informe também as barras ao digitar.");

        javax.swing.GroupLayout pnlDataLayout = new javax.swing.GroupLayout(pnlData);
        pnlData.setLayout(pnlDataLayout);
        pnlDataLayout.setHorizontalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNome)
                            .addComponent(lblID))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfNomeAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlDataLayout.createSequentialGroup()
                                .addComponent(tfCodAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(btnEfetuarBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addComponent(lblDatNasc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ftfDatNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDataLayout.setVerticalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEfetuarBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblID)
                        .addComponent(tfCodAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(tfNomeAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDatNasc)
                    .addComponent(ftfDatNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        btnIncluir.setText("Incluir");
        btnIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncluirActionPerformed(evt);
            }
        });

        btnAlterar.setText("Alterar");
        btnAlterar.setToolTipText("");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setText("Excluir");
        btnExcluir.setToolTipText("");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnSalva.setText("Salva");
        btnSalva.setToolTipText("");
        btnSalva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvaActionPerformed(evt);
            }
        });

        btnCancela.setText("Cancela");
        btnCancela.setToolTipText("");
        btnCancela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIncluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addComponent(btnSalva)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancela)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIncluir)
                    .addComponent(btnAlterar)
                    .addComponent(btnExcluir)
                    .addComponent(btnConsultar)
                    .addComponent(btnSalva)
                    .addComponent(btnCancela))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1)
                .addGap(6, 6, 6))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfCodAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCodAutorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfCodAutorActionPerformed

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        this.setEstado(INCLUSAO);

        clearInputBoxes();

        tfNomeAutor.requestFocus();
    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblAutor.getSelectedRow() < 0) {
           biblio.control.FrameUtil.showMessage(this, "Selecione um registro primeiro", "Erro");
        } else {
            this.setEstado(ALTERACAO);
        }    
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblAutor.getSelectedRow() < 0) {
           biblio.control.FrameUtil.showMessage(this, "Selecione um registro primeiro", "Erro");
        } else {
            int dialogResult;
            Object[] options = { "Confirmar", "Cancelar" };
            dialogResult = JOptionPane.showOptionDialog(this, "Tem certeza que deseja excluir este registro?", "Informação", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            if (dialogResult == JOptionPane.YES_OPTION) {
                try {
                    //this.setEstado(ALTERACAO);

                    deleteRecord();

                    loadRecords(0);
                    
                    tblAutor.requestFocus();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }    
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        this.setEstado(EDICAO);
        clearInputBoxes();
        updateFields();
    }//GEN-LAST:event_btnCancelaActionPerformed

    private void btnSalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvaActionPerformed
        //int dialogResult = JOptionPane.showConfirmDialog(null, "Confirma a alteração deste registro?", "Confirmação de alteração", JOptionPane.YES_NO_OPTION);
        int dialogResult;
        long idNumber;
        Object[] options = { "Confirmar", "Cancelar" };
        dialogResult = JOptionPane.showOptionDialog(this, "Confirma a gravação deste registro?", "Informação", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                if (this.getEstado() == INCLUSAO) {
                    idNumber = addNew();
                } else {
                    updateRecord();
                    idNumber = Integer.parseInt(tfCodAutor.getText());
                }
                loadRecords(-1);
                // Posicionando
                int index = this.getRowByValue(idNumber);
                if (index >= 0) {
                   tblAutor.setRowSelectionInterval(index, index);
                }

                this.setEstado(EDICAO);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            tfNomeAutor.requestFocus();
        }
    }//GEN-LAST:event_btnSalvaActionPerformed

    private void tblAutorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAutorMouseClicked
    }//GEN-LAST:event_tblAutorMouseClicked

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        this.setEstado(CONSULTA);
        this.clearInputBoxes();
        tfCodAutor.requestFocus();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnEfetuarBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEfetuarBuscaActionPerformed
        ResultSet result;
        int locatedAuthorIndex;
        String sql_stmt = "SELECT cod_autor FROM Autor ";
        // Completa o comando dependendo dos campos preenchidos
        if (tfCodAutor.getText().isEmpty() && tfNomeAutor.getText().isEmpty()) {
            biblio.control.FrameUtil.showMessage(this, "Informe o código ou o nome antes de realizar busca", "Erro");
        } else if (tfCodAutor.getText().isEmpty()) {
            sql_stmt += "WHERE nome_autor LIKE '%" + tfNomeAutor.getText() + "%'";
        } else if (tfNomeAutor.getText().isEmpty()) {
            sql_stmt += "WHERE cod_autor  = " + tfCodAutor.getText();
        } else {
            sql_stmt += "WHERE cod_autor  = " + tfCodAutor.getText();
            sql_stmt += "  AND nome_autor LIKE '%" + tfNomeAutor.getText() + "%'";
        }

        DBUtilities dbUtilities = new DBUtilities();

        try {
            result = dbUtilities.executeSQLQuery(sql_stmt);
            if (result.next()) {
                // Posiciona no autor que foi localizado
                locatedAuthorIndex = biblio.control.FrameUtil.getJTableRowByValue(tblAutor,result.getLong(1),0);
                loadRecords(locatedAuthorIndex);
                if (locatedAuthorIndex >= 0) {
                   tblAutor.setRowSelectionInterval(locatedAuthorIndex, locatedAuthorIndex);
                }
                // Volta estado para edição
                this.setEstado(EDICAO);
            } else {
                biblio.control.FrameUtil.showMessage(this, "Nenhum registro encontrado", "Mensagem");
            }           
        } catch (SQLException ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro ao acessar o BD: " + ex.getMessage(), "Erro");
        }
    }//GEN-LAST:event_btnEfetuarBuscaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancela;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnEfetuarBusca;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnSalva;
    private javax.swing.JFormattedTextField ftfDatNasc;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDatNasc;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblNome;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlData;
    private javax.swing.JTable tblAutor;
    private javax.swing.JTextField tfCodAutor;
    private javax.swing.JTextField tfNomeAutor;
    // End of variables declaration//GEN-END:variables
}
