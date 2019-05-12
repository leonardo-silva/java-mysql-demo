/*
 * Classe utilitária para manipulação de frames.
 */
package biblio.control;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author leonardosilva
 */
public final class FrameUtil {
    public static void showMessage(Component parent, String msg, String msgType) {
        if (msgType.equals("Erro"))
             JOptionPane.showMessageDialog(parent, msg, "Erro", JOptionPane.ERROR_MESSAGE);
        if (msgType.equals("Mensagem"))
             JOptionPane.showMessageDialog(parent, msg, "Mensagem", JOptionPane.INFORMATION_MESSAGE);

    }
    
    public static boolean showOptionDialog(Component parent, String msg) {
        int dialogResult;
        Object[] options = { "Confirmar", "Cancelar" };
        dialogResult = JOptionPane.showOptionDialog(parent, msg, "Opção", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        return (dialogResult == JOptionPane.YES_OPTION);
    }

    public static void removeAllJTableRows(DefaultTableModel dm) {
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }        
    }
    
    public static int getJTableRowByValue(javax.swing.JTable objTable, Object value, int column) {
        for (int i = objTable.getRowCount() - 1; i >= 0; --i) {
            if (objTable.getValueAt(i, column).equals(value)) {
                // what if value is not unique?
                return i;
            }
        }
        return -1;
    }
    

}
