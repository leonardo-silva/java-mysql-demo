/*
 * Tela de exemplo para cadastro de livros de uma biblioteca.
 */
package biblio.ui;

import biblio.control.DBUtilities;
import biblio.db.ResultSetTableModel;
import biblio.db.Autor;
import java.awt.Color;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;

/**
 *
 * @author leonardosilva
 */
public class FrmLivro extends javax.swing.JFrame {
    private byte estado=EDICAO;
    private JComboBox cbbAutor = new JComboBox();
    
    private static final byte INCLUSAO = 0;
    private static final byte ALTERACAO = 1;
    private static final byte EDICAO = 2;
    private static final byte CONSULTA = 3;
    
    private static final String TOOLTIP_TBL_AUTOR = "Selecione um autor e pressione 'delete' para excluir um autor";

    /**
     * Creates new form FrmLivro
     */
    public FrmLivro() {
        initComponents();

        tblAutor.setDefaultRenderer(Integer.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                c.setForeground(row>1 ? Color.BLUE : Color.RED);
                return c;
            }
        });        
        

        try {
            this.loadRecords(0);
        } catch (SQLException ex) {
            System.out.println("The following error has occured: " + ex.getMessage());
        }
        // É importante só habilitar os controles após chamar this.loadRecords(0);
        this.enableButtons(estado);
        // Alterando largura da 1a coluna da tabela de autores
        //tblAutor.getColumnModel().getColumn(0).setPreferredWidth(107);
        tblAutor.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        // Adiciona eventos para o combobox de autores
        //addComboListeners();
        loadComboAuthors();
    }
    
    private void addComboListeners() {
        cbbAutor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (tblAutor.getSelectedRow() >= 0) {
                    JComboBox comboBox = (JComboBox) evt.getSource();
                    Autor author = (Autor) comboBox.getSelectedItem();
                    tblAutor.setValueAt(author.getCod_autor(), tblAutor.getSelectedRow(), 0);
                    tblAutor.setValueAt(author.getNome_autor(), tblAutor.getSelectedRow(), 1);
                }    
            }
        });
    }

    private void loadRecords(int idIndex) throws SQLException {

        String sql_stmt = "SELECT CAST(l.cod_livro as SIGNED) as 'Codigo', CONCAT(l.titulo_livro,'') as 'Titulo' ";
        //sql_stmt += ", CONCAT(a.nome_autor,'') as 'Autor' ";
        sql_stmt += "FROM Livro l";
        //sql_stmt += "  LEFT JOIN Autor_Livro al ON l.cod_livro = al.cod_livro";
        //sql_stmt += "  LEFT JOIN Autor a ON al.cod_autor = a.cod_autor";
        
        ResultSetTableModel tableModel = new ResultSetTableModel(sql_stmt);

        tblLivro.setModel(tableModel);

        tblLivro.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            try {
                if (tblLivro.getSelectedRow() >= 0) {
                    Object cod_livro = tblLivro.getValueAt(tblLivro.getSelectedRow(), 0);
                    Object titulo_livro = tblLivro.getValueAt(tblLivro.getSelectedRow(), 1);

                    tfCodLivro.setText(cod_livro.toString());
                    tfTituloLivro.setText(titulo_livro.toString());
                    
                    loadRecordsAuthors(cod_livro.toString());
                }
            } catch (Exception ex) {
                biblio.control.FrameUtil.showMessage(this, "Erro ao ler tabela de livros: " + ex.getMessage(), "Erro");
            }
        });

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblLivro.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        // Posicionando
        if (idIndex >= 0 && tblLivro.getRowCount() > 0) {
           tblLivro.setRowSelectionInterval(idIndex, idIndex);
        }
    }    

    private void loadRecordsAuthors(String codLivro) {
        String sql_stmt = "SELECT CAST(a.cod_autor as SIGNED) as 'Codigo', CONCAT(a.nome_autor,'') as 'Autor' ";
        sql_stmt += "FROM Autor a";
        sql_stmt += "  LEFT JOIN Autor_Livro al ON a.cod_autor = al.cod_autor ";
        sql_stmt += "WHERE al.cod_livro = " + codLivro;
        
        // Limpa JTable de autores
        biblio.control.FrameUtil.removeAllJTableRows((DefaultTableModel)tblAutor.getModel());

        DBUtilities dbUtilities = new DBUtilities();

        try {
            ResultSet rs1 = dbUtilities.executeSQLQuery(sql_stmt);
            // Carrega combo
            while(rs1.next()) {
                ((DefaultTableModel)tblAutor.getModel()).addRow(new Object[]{rs1.getString(1),rs1.getString(2)}); 
            }
        } catch (SQLException ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro ao ler dados dos autores: " + ex.getMessage(), "Erro");
        }

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblAutor.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
    }    
/*
    private void loadRecordsAuthors(String codLivro) {
        String sql_stmt = "SELECT CAST(a.cod_autor as SIGNED) as 'Codigo', CONCAT(a.nome_autor,'') as 'Autor' ";
        sql_stmt += "FROM Autor a";
        sql_stmt += "  LEFT JOIN Autor_Livro al ON a.cod_autor = al.cod_autor ";
        sql_stmt += "WHERE al.cod_livro = " + codLivro;
        
        try {
            ResultSetTableModel tableModel = new ResultSetTableModel(sql_stmt);
            tblAutor.setModel(tableModel);
        } catch (Exception ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro ao ler tabela de livros: " + ex.getMessage(), "Erro");
        }

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblAutor.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
    }    
*/
    private void enableButtons(byte state) {
        switch(state) {
            case INCLUSAO: {
                btnAdd.setEnabled(false);
                lblCodigo.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnSearch.setEnabled(false);
                btnFirst.setEnabled(false);
                btnPrevious.setEnabled(false);
                btnNext.setEnabled(false);
                btnLast.setEnabled(false);
                btnConfirma.setEnabled(true);
                btnCancela.setEnabled(true);
                btnAddAutores.setVisible(true);
                tblAutor.setToolTipText(TOOLTIP_TBL_AUTOR);
                cbbAutores.setVisible(true);
                lblAutores.setEnabled(true);
                lblCodigo.setEnabled(false);
                tfCodLivro.setEnabled(false);
                tfTituloLivro.setEditable(true);
                tblAutor.setEnabled(true);
                tblLivro.setEnabled(false);
                break;
            }
            case ALTERACAO: {
                btnAdd.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnSearch.setEnabled(false);
                btnFirst.setEnabled(false);
                btnPrevious.setEnabled(false);
                btnNext.setEnabled(false);
                btnLast.setEnabled(false);
                btnConfirma.setEnabled(true);
                btnCancela.setEnabled(true);
                btnAddAutores.setVisible(true);
                tblAutor.setToolTipText(TOOLTIP_TBL_AUTOR);
                cbbAutores.setVisible(true);
                lblAutores.setEnabled(true);
                lblCodigo.setEnabled(false);
                tfCodLivro.setEnabled(false);
                tfTituloLivro.setEditable(true);
                tfTituloLivro.requestFocus();
                tblAutor.setEnabled(false);
                tblLivro.setEnabled(false);
                break;
            }
            case EDICAO: {
                btnAdd.setEnabled(true);
                btnEdit.setEnabled(tblLivro.getRowCount()>0);
                btnDelete.setEnabled(tblLivro.getRowCount()>0);
                btnSearch.setEnabled(tblLivro.getRowCount()>0);
                btnFirst.setEnabled(tblLivro.getRowCount()>1);
                btnPrevious.setEnabled(tblLivro.getRowCount()>1);
                btnNext.setEnabled(tblLivro.getRowCount()>1);
                btnLast.setEnabled(tblLivro.getRowCount()>1);
                btnConfirma.setEnabled(false);
                btnCancela.setEnabled(false);
                btnAddAutores.setVisible(false);
                tblAutor.setToolTipText("");
                cbbAutores.setVisible(false);
                lblAutores.setEnabled(false);
                lblCodigo.setEnabled(tblLivro.getRowCount()>0);
                tfCodLivro.setEnabled(false);
                tfTituloLivro.setEditable(false);
                tblAutor.setEnabled(false);
                tblLivro.setEnabled(true);
                //tblAutor.requestFocus();
                break;
            }
            case CONSULTA: {
                btnAdd.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnSearch.setEnabled(false);
                btnFirst.setEnabled(false);
                btnPrevious.setEnabled(false);
                btnNext.setEnabled(false);
                btnLast.setEnabled(false);
                btnConfirma.setEnabled(false);
                btnCancela.setEnabled(true);
                btnAddAutores.setVisible(false);
                tblAutor.setToolTipText("");
                cbbAutores.setVisible(false);
                lblAutores.setEnabled(false);
                lblCodigo.setEnabled(true);
                tfCodLivro.setEnabled(true);
                tfTituloLivro.setEditable(true);
                tblAutor.setEnabled(false);
                tblLivro.setEnabled(true);
                //tblLivro.requestFocus();
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
        // Carrega combo com os autores
        loadComboAuthors();
    }
    
    private void loadComboAuthors() {
        cbbAutores.removeAllItems();
        cbbAutores.addItem("- - -");

        String query1 = "SELECT cod_autor, nome_autor ";
        query1 += "FROM Autor ORDER BY nome_autor";

        DBUtilities dbUtilities = new DBUtilities();

        try {
            ResultSet rs1 = dbUtilities.executeSQLQuery(query1);
            // Carrega combo
            while(rs1.next()) {
                cbbAutores.addItem(new Autor(rs1.getInt(1),rs1.getString(2)));
            }
        } catch (SQLException ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro ao ler dados dos autores: " + ex.getMessage(), "Erro");
        }
        // Habilita célula na tabela de autores
        //tblAutor.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbbAutor));
    }

    private int addNew() {
        ResultSet result;
        int lastInsertedID=-1;
        String beginTran_stmt = "START TRANSACTION;";
        String sql_stmt1 = "INSERT INTO Livro (titulo_livro)";
        sql_stmt1 += "VALUES('" + tfTituloLivro.getText() + "');";
        String sql_stmt2;
        String commitTran_stmt = "COMMIT;";
        String rollbackTran_stmt = "ROLLBACK;";

        DBUtilities dbUtilities = new DBUtilities();

        try {
            dbUtilities.executeSQLStatement(beginTran_stmt);
            try {
                dbUtilities.executeSQLStatement(sql_stmt1);
                result = dbUtilities.executeSQLQuery("SELECT LAST_INSERT_ID()");
                if (result.next()) {
                    lastInsertedID = result.getInt(1);
                }    
                // Inclui os autores do livro em Autor_Livro
                for (int i = 0; i < tblAutor.getRowCount(); i++) {
                    sql_stmt2 = "INSERT INTO Autor_Livro (cod_livro, cod_autor) ";
                    sql_stmt2 += "VALUES('" + lastInsertedID + "', ";
                    sql_stmt2 += tblAutor.getValueAt(i, 0).toString() + ");";
                    dbUtilities.executeSQLStatement(sql_stmt2);
                }

                dbUtilities.executeSQLStatement(commitTran_stmt);
            } catch (SQLException ex) {
                dbUtilities.executeSQLStatement(rollbackTran_stmt);
                biblio.control.FrameUtil.showMessage(this, "Erro ao atualizar dados dos livros: " + ex.getMessage(), "Erro");
            }
        } catch (SQLException ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro ao iniciar transacao no BD: " + ex.getMessage(), "Erro");
        }
        
        return lastInsertedID;
    }

    private void updateRecord() {
        String sql_stmt = "UPDATE livro SET titulo_livro = '" + tfTituloLivro.getText() + "'";
        //sql_stmt += ", dat_nasc_autor = '" + getDatNascAsString() + "'";
        //sql_stmt += " WHERE cod_autor = " + tfCodAutor.getText();

        DBUtilities dbUtilities = new DBUtilities();

        try {
            dbUtilities.executeSQLStatement(sql_stmt);
        } catch (SQLException ex) {
            biblio.control.FrameUtil.showMessage(this, "Erro de acesso ao BD: " + ex.getMessage(), "Erro");
        }
    }

    private void deleteRecord() throws SQLException {
        String sql_stmt = "DELETE FROM livro WHERE cod_livro = '" + tfCodLivro.getText() + "'";

        DBUtilities dbUtilities = new DBUtilities();

        dbUtilities.executeSQLStatement(sql_stmt);
    }

    private void clearInputBoxes() {
        tfCodLivro.setText("");
        tfTituloLivro.setText("");
        // Limpa tabela com os autores
        biblio.control.FrameUtil.removeAllJTableRows((DefaultTableModel)tblAutor.getModel());
        //loadRecordsAuthors("-1");
    }
    
    private void updateFields() {
        Object cod_livro = tblLivro.getValueAt(tblLivro.getSelectedRow(), 0);
        Object titulo_livro = tblLivro.getValueAt(tblLivro.getSelectedRow(), 1);

        tfCodLivro.setText(cod_livro.toString());
        tfTituloLivro.setText(titulo_livro.toString());
        this.loadRecordsAuthors(cod_livro.toString());
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlMain = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAutor = new javax.swing.JTable();
        lblCodigo = new javax.swing.JLabel();
        tfCodLivro = new javax.swing.JTextField();
        tfTituloLivro = new javax.swing.JTextField();
        lblTitulo = new javax.swing.JLabel();
        tlbCrud = new javax.swing.JToolBar();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnConfirma = new javax.swing.JButton();
        btnCancela = new javax.swing.JButton();
        lblAutores = new javax.swing.JLabel();
        cbbAutores = new javax.swing.JComboBox();
        btnAddAutores = new javax.swing.JButton();
        pnlGrid = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLivro = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBorder(javax.swing.BorderFactory.createTitledBorder("Edição de Livros"));

        tblAutor.setAutoCreateRowSorter(true);
        tblAutor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAutor.setToolTipText("Selecione um autor e pressione 'delete' para excluir um autor");
        tblAutor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblAutor.setCellSelectionEnabled(true);
        tblAutor.setShowGrid(false);
        tblAutor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblAutorKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblAutor);

        lblCodigo.setText("Código: ");

        lblTitulo.setText("Título: ");

        tlbCrud.setRollover(true);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/add_126583_24x24.png"))); // NOI18N
        btnAdd.setToolTipText("Abre novo registro para edição");
        btnAdd.setFocusable(false);
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setMaximumSize(new java.awt.Dimension(56, 28));
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        tlbCrud.add(btnAdd);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/remove-186389_24x24.png"))); // NOI18N
        btnDelete.setToolTipText("Exclui o registro atual");
        btnDelete.setFocusable(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setMaximumSize(new java.awt.Dimension(56, 28));
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbCrud.add(btnDelete);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/edit_103539_24x24.png"))); // NOI18N
        btnEdit.setToolTipText("Permite alterar o registro atual");
        btnEdit.setFocusable(false);
        btnEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEdit.setMaximumSize(new java.awt.Dimension(56, 28));
        btnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbCrud.add(btnEdit);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/search_2362208_24x24.png"))); // NOI18N
        btnSearch.setToolTipText("Permite procurar por um registro específico");
        btnSearch.setFocusable(false);
        btnSearch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSearch.setMaximumSize(new java.awt.Dimension(56, 28));
        btnSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbCrud.add(btnSearch);

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/first_118674_24x24.png"))); // NOI18N
        btnFirst.setToolTipText("Vai para o primeiro registro");
        btnFirst.setFocusable(false);
        btnFirst.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFirst.setMaximumSize(new java.awt.Dimension(56, 28));
        btnFirst.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbCrud.add(btnFirst);

        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/backward_49836_24x24.png"))); // NOI18N
        btnPrevious.setToolTipText("Volta um registro");
        btnPrevious.setFocusable(false);
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setMaximumSize(new java.awt.Dimension(56, 28));
        btnPrevious.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbCrud.add(btnPrevious);

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/forward_126569_24x24.png"))); // NOI18N
        btnNext.setToolTipText("Avança um registro");
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setMaximumSize(new java.awt.Dimension(56, 28));
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbCrud.add(btnNext);

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/last_118654_24x24.png"))); // NOI18N
        btnLast.setToolTipText("Vai para o último registro");
        btnLast.setFocusable(false);
        btnLast.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLast.setMaximumSize(new java.awt.Dimension(56, 28));
        btnLast.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbCrud.add(btnLast);

        btnConfirma.setText("Confirmar");
        btnConfirma.setToolTipText("");
        btnConfirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmaActionPerformed(evt);
            }
        });

        btnCancela.setText("Cancelar");
        btnCancela.setToolTipText("");
        btnCancela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelaActionPerformed(evt);
            }
        });

        lblAutores.setText("Autores:");

        cbbAutores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbAutoresActionPerformed(evt);
            }
        });

        btnAddAutores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biblio/ui/downarrow_20x24.png"))); // NOI18N
        btnAddAutores.setText("Incluir Autor");
        btnAddAutores.setToolTipText("");
        btnAddAutores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAutoresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCodigo)
                                    .addComponent(lblTitulo))
                                .addGap(18, 18, 18)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfTituloLivro)
                                    .addGroup(pnlMainLayout.createSequentialGroup()
                                        .addComponent(tfCodLivro, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(64, 353, Short.MAX_VALUE))))))
                    .addComponent(tlbCrud, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(btnConfirma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancela)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblAutores)
                        .addGap(18, 18, 18)
                        .addComponent(cbbAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddAutores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addComponent(tlbCrud, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo)
                    .addComponent(tfCodLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTituloLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitulo))
                .addGap(19, 19, 19)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAutores)
                    .addComponent(cbbAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddAutores))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirma)
                    .addComponent(btnCancela))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dados", pnlMain);

        pnlGrid.setLayout(new java.awt.GridLayout(1, 0));

        tblLivro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Título"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblLivro);

        pnlGrid.add(jScrollPane2);

        jTabbedPane1.addTab("Grid", pnlGrid);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmaActionPerformed
        long idNumber;

        if (biblio.control.FrameUtil.showOptionDialog(this, "Confirma a gravação deste registro?")) {
            
            try {
                if (this.getEstado() == INCLUSAO) {
                    idNumber = addNew();
                } else {
                    updateRecord();
                    idNumber = Integer.parseInt(tfCodLivro.getText());
                }
                loadRecords(-1);
                // Posicionando
                int index = biblio.control.FrameUtil.getJTableRowByValue(tblLivro, idNumber, 0);
                if (index >= 0) {
                    tblLivro.setRowSelectionInterval(index, index);
                }

                this.setEstado(EDICAO);

            } catch (SQLException ex) {
                biblio.control.FrameUtil.showMessage(this, "Erro de acesso ao BD: " + ex.getMessage(), "Erro");
            }
        } else {
            tfTituloLivro.requestFocus();
        }
    }//GEN-LAST:event_btnConfirmaActionPerformed
    
    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        this.setEstado(EDICAO);
        clearInputBoxes();
        updateFields();
    }//GEN-LAST:event_btnCancelaActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        this.setEstado(INCLUSAO);

        clearInputBoxes();

        tfTituloLivro.requestFocus();
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblAutorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAutorKeyPressed
        //if ((tblAutor.getRowCount() == 0) || (! tblAutor.getValueAt(tblAutor.getSelectedRow(), tblAutor.getSelectedColumn()).toString().isEmpty()))
        //    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        //        ((DefaultTableModel)tblAutor.getModel()).addRow(new Object[]{""}); 
            //} else 
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
                ((DefaultTableModel)tblAutor.getModel()).removeRow(tblAutor.getSelectedRow());
            }
    }//GEN-LAST:event_tblAutorKeyPressed

    private void btnAddAutoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAutoresActionPerformed
        if (cbbAutores.getSelectedIndex() > 0) {
            Autor author = (Autor) cbbAutores.getSelectedItem();
            if (biblio.control.FrameUtil.getJTableRowByValue(tblAutor, author.getCod_autor(), 0) >= 0)
                biblio.control.FrameUtil.showMessage(this, "Este autor já está incluído!", "Erro");
            else
                ((DefaultTableModel)tblAutor.getModel()).addRow(new Object[]{author.getCod_autor(), author.getNome_autor()});
        }    

    }//GEN-LAST:event_btnAddAutoresActionPerformed

    private void cbbAutoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbAutoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbAutoresActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddAutores;
    private javax.swing.JButton btnCancela;
    private javax.swing.JButton btnConfirma;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cbbAutores;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAutores;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlGrid;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JTable tblAutor;
    private javax.swing.JTable tblLivro;
    private javax.swing.JTextField tfCodLivro;
    private javax.swing.JTextField tfTituloLivro;
    private javax.swing.JToolBar tlbCrud;
    // End of variables declaration//GEN-END:variables
}
