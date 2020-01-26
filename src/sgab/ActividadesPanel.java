/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgab;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FranciscoHernandez
 */
public class ActividadesPanel extends javax.swing.JPanel {

    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    private String idActividad;
    private String nombreA;
    private int costoA;
    private int capacidadU;
    private String fechaInicio;
    private String fechaFin;
    private String[] botones = {"Si", "No"};
    private String diaC;
    private String horaC;
    private int fila;
    private int filaClase;
    Validar val = new Validar();
    private static String user = "";
    private static String password = "";
    private String fechaAuxR="";
    private String fechaAuxH="";

    /**
     * Creates new form ActividadesPanel
     */
    public ActividadesPanel(String user, String password) {
        initComponents();
        this.user = user;
        this.password = password;
        lunesModTxt.setEditable(false);
        martesModTxt.setEditable(false);
        miercolesModTxt.setEditable(false);
        juevesModTxt.setEditable(false);
        viernesModTxt.setEditable(false);
        lunesRegTxt.setEditable(false);
        martesRegTxt.setEditable(false);
        miercolesRegTxt.setEditable(false);
        juevesRegTxt.setEditable(false);
        viernesRegTxt.setEditable(false);
        lunesModBox.setEnabled(false);
        martesModBox.setEnabled(false);
        miercolesModBox.setEnabled(false);
        juevesModBox.setEnabled(false);
        viernesModBox.setEnabled(false);

        eliminarBtnL.setBackground(Color.RED);
        eliminarBtnL.setForeground(Color.WHITE);
        eliminarClaBtn.setBackground(Color.RED);
        eliminarClaBtn.setForeground(Color.WHITE);

        modificarBtn.setBackground(new Color(32, 33, 79));
        modificarBtn.setForeground(Color.WHITE);
        modificarClaBtn.setBackground(new Color(32, 33, 79));
        modificarClaBtn.setForeground(Color.WHITE);

        tablaConGen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaConClases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        idActModTxt.setEditable(false);
        capacidadModTxt.setEditable(false);
        costoTotalModTxt.setEditable(false);
        idActRegTxt.setEditable(false);
        generarID();
        limpiarTablaClases();
        limpiarTablaConsulta();
        consultaGeneral();
        consultaClases();
        //FechaDelDia.setVisible(false);
    }

    public void registrarActividad() {
        String sql = "INSERT INTO Actividades (IdActividad,NombreA,CostoTotalA,CapacidadUsuarios,FechaInicioA,FechaFinA) VALUES (?,?,?,?,?,?)";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaI = sdf.format(fechaInicioCalReg.getDate());
            String fechaF = sdf.format(fechaFinCalReg.getDate());

            pstm.setString(1, idActRegTxt.getText());
            pstm.setString(2, nombreRegTxt.getText());
            pstm.setInt(3, Integer.parseInt(costoRegTxt.getText()));
            pstm.setInt(4, Integer.parseInt(capacidadRegSpinner.getValue().toString()));
            pstm.setString(5, fechaI);
            pstm.setString(6, fechaF);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Actividad registrada");
                if (lunesCheckBox.isSelected()) {
                    registrarClase("Lunes", lunesRegTxt.getText());
                }
                if (martesCheckBox.isSelected()) {
                    registrarClase("Martes", martesRegTxt.getText());
                }
                if (miercolesCheckBox.isSelected()) {
                    registrarClase("Miercoles", miercolesRegTxt.getText());
                }
                if (juevesCheckBox.isSelected()) {
                    registrarClase("Jueves", juevesRegTxt.getText());
                }
                if (viernesCheckBox.isSelected()) {
                    registrarClase("Viernes", viernesRegTxt.getText());
                }
                limpiarTxt();
                generarID();
                limpiarTablaClases();
                limpiarTablaConsulta();
                consultaGeneral();
                consultaClases();
            }
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void registrarClase(String dia, String hora) {
        String sql = "INSERT INTO Clases (IdActividad,DiaC,HoraC) VALUES (?,?,?)";
        con = UConnection.getConnection(user, password);

        try {
            pstm = con.prepareStatement(sql);

            pstm.setString(1, idActRegTxt.getText());
            pstm.setString(2, dia);
            pstm.setString(3, hora);

            int rtdo = pstm.executeUpdate();
            pstm.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }

    public void consultaGeneral() {
        String nombre = actividadConGenTxt.getText();
        String sql = "SELECT * FROM Actividades WHERE NombreA like '" + nombre + "%'";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            fila = 0;
            int col = 0;
            rs = pstm.executeQuery();

            while (rs.next()) {
                tablaConGen.setValueAt(rs.getString("IdActividad"), fila, col);
                col++;
                tablaConGen.setValueAt(rs.getString("NombreA"), fila, col);
                col++;
                tablaConGen.setValueAt(rs.getString("CostoTotalA"), fila, col);
                col++;
                tablaConGen.setValueAt(rs.getString("CapacidadUsuarios"), fila, col);
                col++;
                tablaConGen.setValueAt(rs.getString("FechaInicioA"), fila, col);
                col++;
                tablaConGen.setValueAt(rs.getString("FechaFinA"), fila, col);
                col++;
                col = 0;
                if (fila == tablaConGen.getRowCount() - 1) {
                    break;
                }
                fila++;
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaClases() {
        String nombre = actividadConClasesTxt.getText();
        //if (!nombre.equals("")) {
        String sql = "SELECT * FROM horario where NombreA like '" + nombre + "%' ORDER BY IdActividad";

        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            filaClase = -1;
            rs = pstm.executeQuery();

            String idAct = "";
            String idActAux = "";
            while (rs.next()) {
                String nom = "";
                String aux = "";
                String lunes = "-";
                String martes = "-";
                String miercoles = "-";
                String jueves = "-";
                String viernes = "-";

                idAct = rs.getString("IdActividad");
                if (!idAct.equals(idActAux)) {
                    filaClase++;
                }
                idActAux = idAct;
                tablaConClases.setValueAt(idAct, filaClase, 0);

                nom = rs.getString("NombreA");
                tablaConClases.setValueAt(nom, filaClase, 1);

                aux = rs.getString("DiaC");
                if (aux.equals("Lunes")) {
                    lunes = rs.getString("HoraC");
                    tablaConClases.setValueAt(lunes, filaClase, 2);
                } else if (aux.equals("Martes")) {
                    martes = rs.getString("HoraC");
                    tablaConClases.setValueAt(martes, filaClase, 3);
                } else if (aux.equals("Miercoles")) {
                    miercoles = rs.getString("HoraC");
                    tablaConClases.setValueAt(miercoles, filaClase, 4);
                } else if (aux.equals("Jueves")) {
                    jueves = rs.getString("HoraC");
                    tablaConClases.setValueAt(jueves, filaClase, 5);
                } else if (aux.equals("Viernes")) {
                    viernes = rs.getString("HoraC");
                    tablaConClases.setValueAt(viernes, filaClase, 6);
                }

                if (filaClase == tablaConClases.getRowCount() - 1) {
                    break;
                }
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

        //}
    }

    public void consultaIndividual(String idAux) {
        String sql = "SELECT * FROM Actividades WHERE IdActividad=?";
        con = UConnection.getConnection(user, password);
        limpiarVariables();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, idAux);

            rs = pstm.executeQuery();
            while (rs.next()) {
                idActividad = rs.getString("IdActividad");
                nombreA = rs.getString("NombreA");
                costoA = rs.getInt("CostoTotalA");
                capacidadU = rs.getInt("CapacidadUsuarios");
                fechaInicio = rs.getString("FechaInicioA");
                fechaFin = rs.getString("FechaFinA");
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void consultaIndividualClases(String idAux) {
        String sql = "SELECT * FROM Clases WHERE IdActividad=?";
        con = UConnection.getConnection(user, password);
        limpiarVariablesClases();
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, idAux);

            rs = pstm.executeQuery();
            while (rs.next()) {
                idActividad = rs.getString("IdActividad");
                diaC = rs.getString("DiaC");
                horaC = rs.getString("HoraC");

                if (diaC.equals("Lunes")) {
                    lunesModTxt.setText(horaC);
                    //lunesModTxt.setEditable(true);
                    lunesModBox.setEnabled(true);
                } else if (diaC.equals("Martes")) {
                    martesModTxt.setText(horaC);
                    //martesModTxt.setEditable(true);
                    martesModBox.setEnabled(true);
                } else if (diaC.equals("Miercoles")) {
                    miercolesModTxt.setText(horaC);
                    //miercolesModTxt.setEditable(true);
                    miercolesModBox.setEnabled(true);
                } else if (diaC.equals("Jueves")) {
                    juevesModTxt.setText(horaC);
                    //juevesModTxt.setEditable(true);
                    juevesModBox.setEnabled(true);
                } else if (diaC.equals("Viernes")) {
                    viernesModTxt.setText(horaC);
                    //viernesModTxt.setEditable(true);
                    viernesModBox.setEnabled(true);
                }
            }
            pstm.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void modificarActividad() {
        String sql = "UPDATE Actividades SET NombreA=?,FechaInicioA=?,FechaFinA=? WHERE IdActividad=?";
        con = UConnection.getConnection(user, password);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaI = sdf.format(fechaInicioCalMod.getDate());
            String fechaF = sdf.format(fechaFinCalMod.getDate());

            pstm = con.prepareStatement(sql);
            pstm.setString(1, nombreModTxt.getText());
            pstm.setString(2, fechaI);
            pstm.setString(3, fechaF);
            pstm.setString(4, idActModTxt.getText());

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Actividad actualizada");
                limpiarTxtActualizar();
            }
            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void modificarClases() {
        String sql = "UPDATE Clases SET HoraC=? WHERE IdActividad=? && DiaC=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            if (lunesModTxt.isEditable()) {
                pstm.setString(1, lunesModTxt.getText());
                pstm.setString(2, idActModTxt.getText());
                pstm.setString(3, "Lunes");
                int rtdo = pstm.executeUpdate();
                if (rtdo == 1) {
                    JOptionPane.showMessageDialog(null, "Clase actualizada");
                    lunesModTxt.setEditable(false);
                    lunesModTxt.setText("");
                }
            }
            if (martesModTxt.isEditable()) {
                pstm.setString(1, martesModTxt.getText());
                pstm.setString(2, idActModTxt.getText());
                pstm.setString(3, "Martes");
                int rtdo = pstm.executeUpdate();
                if (rtdo == 1) {
                    JOptionPane.showMessageDialog(null, "Clase actualizada");
                    martesModTxt.setEditable(false);
                    martesModTxt.setText("");
                }
            }
            if (miercolesModTxt.isEditable()) {
                pstm.setString(1, miercolesModTxt.getText());
                pstm.setString(2, idActModTxt.getText());
                pstm.setString(3, "Miercoles");
                int rtdo = pstm.executeUpdate();
                if (rtdo == 1) {
                    JOptionPane.showMessageDialog(null, "Clase actualizada");
                    miercolesModTxt.setEditable(false);
                    miercolesModTxt.setText("");
                }
            }
            if (juevesModTxt.isEditable()) {
                pstm.setString(1, juevesModTxt.getText());
                pstm.setString(2, idActModTxt.getText());
                pstm.setString(3, "Jueves");
                int rtdo = pstm.executeUpdate();
                if (rtdo == 1) {
                    JOptionPane.showMessageDialog(null, "Clase actualizada");
                    juevesModTxt.setEditable(false);
                    juevesModTxt.setText("");
                }
            }
            if (viernesModTxt.isEditable()) {
                pstm.setString(1, viernesModTxt.getText());
                pstm.setString(2, idActModTxt.getText());
                pstm.setString(3, "Viernes");
                int rtdo = pstm.executeUpdate();
                if (rtdo == 1) {
                    JOptionPane.showMessageDialog(null, "Clase actualizada");
                    viernesModTxt.setEditable(false);
                    viernesModTxt.setText("");
                }
            }

            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void eliminarActividad(String id, int row) {
        String sql = "DELETE FROM Actividades WHERE IdActividad=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            int rtdo = pstm.executeUpdate();
            if (rtdo == 1) {
                JOptionPane.showMessageDialog(null, "Actividad eliminada");
                DefaultTableModel m = (DefaultTableModel) tablaConGen.getModel();
                m.removeRow(row);
                m.addRow(new String[8]);
            }

            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void eliminarClases(String id) {
        String sql = "DELETE FROM Clases WHERE IdActividad=?";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            int rtdo = pstm.executeUpdate();

            pstm.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public Date convertirFecha(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void limpiarTxtRadio() {
        lunesModTxt.setText("");
        martesModTxt.setText("");
        miercolesModTxt.setText("");
        juevesModTxt.setText("");
        viernesModTxt.setText("");
        lunesModBox.setEnabled(false);
        martesModBox.setEnabled(false);
        miercolesModBox.setEnabled(false);
        juevesModBox.setEnabled(false);
        viernesModBox.setEnabled(false);
        lunesCheckBox.setSelected(false);
        martesModBox.setSelected(false);
        miercolesModBox.setSelected(false);
        juevesModBox.setSelected(false);
        viernesModBox.setSelected(false);
    }

    public void limpiarTablaConsulta() {
        for (int x = 0; x < 6; x++) {
            for (int i = 0; i < fila; i++) {
                tablaConGen.setValueAt(null, i, x);
            }
        }
    }

    public void limpiarTablaClases() {
        for (int x = 0; x < 7; x++) {
            for (int i = 0; i < filaClase + 1; i++) {
                tablaConClases.setValueAt(null, i, x);
            }
        }
    }

    public void limpiarVariables() {
        idActividad = "";
        costoA = 0;
        capacidadU = 0;
        fechaFin = "";
        fechaInicio = "";
        nombreA = "";
    }

    public void limpiarTxt() {
        idActRegTxt.setText("");
        nombreRegTxt.setText("");
        costoRegTxt.setText("");
        capacidadRegSpinner.setValue(1);
        fechaInicioCalReg.setDate(null);
        fechaFinCalReg.setDate(null);
        lunesCheckBox.setSelected(false);
        martesCheckBox.setSelected(false);
        miercolesCheckBox.setSelected(false);
        juevesCheckBox.setSelected(false);
        viernesCheckBox.setSelected(false);
        horaInicialBoxReg.setSelectedIndex(0);
        horaFinalBoxReg.setSelectedIndex(0);
        minutosInicialBoxReg.setSelectedIndex(0);
        minutosFinalBoxReg.setSelectedIndex(0);
        lunesRegTxt.setText("");
        martesRegTxt.setText("");
        miercolesRegTxt.setText("");
        juevesRegTxt.setText("");
        viernesRegTxt.setText("");
    }

    public void limpiarTxtActualizar() {
        idActModTxt.setText("");
        costoTotalModTxt.setText("");
        nombreModTxt.setText("");
        capacidadModTxt.setText("");
        lunesModTxt.setText("");
        martesModTxt.setText("");
        miercolesModTxt.setText("");
        juevesModTxt.setText("");
        viernesModTxt.setText("");
        lunesModBox.setSelected(false);
        martesModBox.setSelected(false);
        miercolesModBox.setSelected(false);
        juevesModBox.setSelected(false);
        viernesModBox.setSelected(false);
        horaFModBox.setSelectedIndex(0);
        horaIModBox.setSelectedIndex(0);
        minutosFModBox.setSelectedIndex(0);
        minutosIModBox.setSelectedIndex(0);
        fechaFinCalMod.setDate(null);
        fechaInicioCalMod.setDate(null);
    }

    public void limpiarVariablesClases() {
        diaC = "";
        horaC = "";
    }

    public String getHora() {
        String hora = "";
        hora = horaInicialBoxReg.getSelectedItem().toString() + ":" + minutosInicialBoxReg.getSelectedItem().toString() + "-" + horaFinalBoxReg.getSelectedItem().toString() + ":" + minutosFinalBoxReg.getSelectedItem().toString();
        return hora;
    }

    public String getHoraMod() {
        String hora = "";
        hora = horaIModBox.getSelectedItem().toString() + ":" + minutosIModBox.getSelectedItem().toString() + "-" + horaFModBox.getSelectedItem().toString() + ":" + minutosFModBox.getSelectedItem().toString();
        return hora;
    }

    public boolean camposRegistrar() {
        boolean campos = false;
        if (idActRegTxt.getText().length() > 0 && nombreRegTxt.getText().length() > 0 && fechaInicioCalReg.getDate() != null
                && fechaFinCalReg.getDate() != null && costoRegTxt.getText().length() > 0) {
            campos = true;
        }
        return campos;
    }

    public boolean camposModificar() {
        boolean campos = false;
        if (nombreModTxt.getText().length() > 0 && fechaInicioCalMod.getDate() != null && fechaFinCalMod.getDate() != null) {
            campos = true;
        }
        return campos;
    }

    public String ultimoID() {
        String sql = "SELECT MAX(IdActividad) as ID FROM Actividades";
        String rtn = "";
        con = UConnection.getConnection(user, password);
        try {
            pstm = con.prepareStatement(sql);

            rs = pstm.executeQuery();
            while (rs.next()) {
                rtn = rs.getString("ID");

            }
            pstm.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return rtn;
    }

    public void generarID() {
        String ultimo = ultimoID();
        if (ultimo == null) {
            ultimo = "A0001";
        } else {
            String idInicio = "";
            ultimo = ultimo.substring(1);
            int digitos = Integer.parseInt(ultimo);
            digitos++;
            if (Integer.toString(digitos).length() == 1) {
                idInicio = "000";
            }
            if (Integer.toString(digitos).length() == 2) {
                idInicio = "00";
            }
            if (Integer.toString(digitos).length() == 3) {
                idInicio = "0";
            }
            ultimo = "A" + idInicio + Integer.toString(digitos);
        }
        idActRegTxt.setText(ultimo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        actividadesTabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        idActRegTxt = new javax.swing.JTextField();
        nombreRegTxt = new javax.swing.JTextField();
        costoRegTxt = new javax.swing.JTextField();
        regBtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        lunesCheckBox = new javax.swing.JCheckBox();
        martesCheckBox = new javax.swing.JCheckBox();
        miercolesCheckBox = new javax.swing.JCheckBox();
        juevesCheckBox = new javax.swing.JCheckBox();
        viernesCheckBox = new javax.swing.JCheckBox();
        lunesRegTxt = new javax.swing.JTextField();
        martesRegTxt = new javax.swing.JTextField();
        miercolesRegTxt = new javax.swing.JTextField();
        juevesRegTxt = new javax.swing.JTextField();
        viernesRegTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        horaInicialBoxReg = new javax.swing.JComboBox<>();
        minutosInicialBoxReg = new javax.swing.JComboBox<>();
        horaFinalBoxReg = new javax.swing.JComboBox<>();
        minutosFinalBoxReg = new javax.swing.JComboBox<>();
        fechaInicioCalReg = new com.toedter.calendar.JDateChooser();
        fechaFinCalReg = new com.toedter.calendar.JDateChooser();
        capacidadRegSpinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        FechaDelDia = new rojeru_san.RSLabelFecha();
        consultarTab = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        actividadConGenTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaConGen = new javax.swing.JTable();
        eliminarBtnL = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        actividadConClasesTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaConClases = new javax.swing.JTable();
        modificarClaBtn = new javax.swing.JButton();
        eliminarClaBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        idActModTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        nombreModTxt = new javax.swing.JTextField();
        costoTotalModTxt = new javax.swing.JTextField();
        actualizarBtn = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lunesModTxt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        martesModTxt = new javax.swing.JTextField();
        miercolesModTxt = new javax.swing.JTextField();
        juevesModTxt = new javax.swing.JTextField();
        viernesModTxt = new javax.swing.JTextField();
        fechaFinCalMod = new com.toedter.calendar.JDateChooser();
        fechaInicioCalMod = new com.toedter.calendar.JDateChooser();
        horaIModBox = new javax.swing.JComboBox<>();
        minutosIModBox = new javax.swing.JComboBox<>();
        horaFModBox = new javax.swing.JComboBox<>();
        minutosFModBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        lunesModBox = new javax.swing.JCheckBox();
        martesModBox = new javax.swing.JCheckBox();
        miercolesModBox = new javax.swing.JCheckBox();
        juevesModBox = new javax.swing.JCheckBox();
        viernesModBox = new javax.swing.JCheckBox();
        capacidadModTxt = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1069, 619));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ACTIVIDADES");

        jLabel2.setText("ID Actividad:");

        jLabel3.setText("Nombre:");

        jLabel4.setText("Costo Total: $");

        jLabel5.setText("Capacidad:");

        jLabel6.setText("Fecha de inicio:");

        jLabel7.setText("Fecha de fin:");

        idActRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idActRegTxtKeyTyped(evt);
            }
        });

        nombreRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreRegTxtKeyTyped(evt);
            }
        });

        costoRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                costoRegTxtKeyTyped(evt);
            }
        });

        regBtn.setText("Guardar");
        regBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regBtnActionPerformed(evt);
            }
        });

        jLabel8.setText("Clases:");

        lunesCheckBox.setText("Lunes");
        lunesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lunesCheckBoxItemStateChanged(evt);
            }
        });

        martesCheckBox.setText("Martes");
        martesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                martesCheckBoxItemStateChanged(evt);
            }
        });

        miercolesCheckBox.setText("Miercoles");
        miercolesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                miercolesCheckBoxItemStateChanged(evt);
            }
        });

        juevesCheckBox.setText("Jueves");
        juevesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                juevesCheckBoxItemStateChanged(evt);
            }
        });

        viernesCheckBox.setText("Viernes");
        viernesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viernesCheckBoxItemStateChanged(evt);
            }
        });

        lunesRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lunesRegTxtKeyTyped(evt);
            }
        });

        martesRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                martesRegTxtKeyTyped(evt);
            }
        });

        miercolesRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                miercolesRegTxtKeyTyped(evt);
            }
        });

        juevesRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                juevesRegTxtKeyTyped(evt);
            }
        });

        viernesRegTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                viernesRegTxtKeyTyped(evt);
            }
        });

        jLabel13.setText("Día");

        jLabel31.setText("ejemplo: 09:00-13:00");

        horaInicialBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

        minutosInicialBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        horaFinalBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

        minutosFinalBoxReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        capacidadRegSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 35, 1));
        capacidadRegSpinner.setValue(1);

        jLabel9.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel9.setText("-");

        FechaDelDia.setForeground(new java.awt.Color(0, 0, 0));
        FechaDelDia.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(idActRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(130, 130, 130))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(nombreRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(costoRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(capacidadRegSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(fechaInicioCalReg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(fechaFinCalReg, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(85, 85, 85)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lunesCheckBox)
                                    .addComponent(martesCheckBox)
                                    .addComponent(miercolesCheckBox)
                                    .addComponent(juevesCheckBox)
                                    .addComponent(viernesCheckBox))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(viernesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lunesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(martesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(miercolesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(juevesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel31)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel13)
                                            .addComponent(horaInicialBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(minutosInicialBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(horaFinalBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minutosFinalBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(445, 445, 445)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 315, Short.MAX_VALUE)
                .addComponent(FechaDelDia, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(344, 344, 344)
                .addComponent(regBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(idActRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nombreRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(16, 16, 16)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(costoRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(capacidadRegSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(fechaInicioCalReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(fechaFinCalReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(horaInicialBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(minutosInicialBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(horaFinalBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(minutosFinalBoxReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lunesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lunesCheckBox))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(martesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(martesCheckBox))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(miercolesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(miercolesCheckBox))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(juevesCheckBox)
                                    .addComponent(juevesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(viernesCheckBox)
                                    .addComponent(viernesRegTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(FechaDelDia, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53)
                .addComponent(regBtn)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        actividadesTabbed.addTab("Registrar", jPanel1);

        jLabel11.setText("Actividad:");

        actividadConGenTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                actividadConGenTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                actividadConGenTxtKeyTyped(evt);
            }
        });

        tablaConGen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Actividad", "Nombre", "Costo total", "Capacidad", "Fecha inicio", "Fecha fin"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaConGen.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaConGen);

        eliminarBtnL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/trash_can.png"))); // NOI18N
        eliminarBtnL.setText("Eliminar");
        eliminarBtnL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBtnLActionPerformed(evt);
            }
        });

        modificarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/pencil.png"))); // NOI18N
        modificarBtn.setText("Modificar");
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 925, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actividadConGenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(modificarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminarBtnL)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(actividadConGenTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eliminarBtnL)
                    .addComponent(modificarBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        consultarTab.addTab("Actividades", jPanel5);

        jLabel12.setText("Actividad:");

        actividadConClasesTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                actividadConClasesTxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                actividadConClasesTxtKeyTyped(evt);
            }
        });

        tablaConClases.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id Actividad", "Nombre", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaConClases.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tablaConClases);

        modificarClaBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/pencil.png"))); // NOI18N
        modificarClaBtn.setText("Modificar");
        modificarClaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarClaBtnActionPerformed(evt);
            }
        });

        eliminarClaBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgab/img/trash_can.png"))); // NOI18N
        eliminarClaBtn.setText("Eliminar");
        eliminarClaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarClaBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(actividadConClasesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 445, Short.MAX_VALUE)
                        .addComponent(modificarClaBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(eliminarClaBtn)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(actividadConClasesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(eliminarClaBtn)
                        .addComponent(modificarClaBtn)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        consultarTab.addTab("Clases", jPanel2);

        actividadesTabbed.addTab("Consultar", consultarTab);

        jLabel14.setText("ID Actividad:");

        idActModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idActModTxtKeyTyped(evt);
            }
        });

        jLabel15.setText("Nombre:");

        jLabel16.setText("Costo total: $");

        jLabel17.setText("Capacidad:");

        jLabel18.setText("Fecha de inicio:");

        jLabel19.setText("Fecha de fin:");

        nombreModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreModTxtKeyTyped(evt);
            }
        });

        costoTotalModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                costoTotalModTxtKeyTyped(evt);
            }
        });

        actualizarBtn.setText("Actualizar");
        actualizarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarBtnActionPerformed(evt);
            }
        });

        jLabel20.setText("Clases:");

        jLabel21.setText("Día");

        jLabel22.setText("Hora");

        lunesModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lunesModTxtKeyTyped(evt);
            }
        });

        jLabel23.setText("Lunes");

        jLabel24.setText("Martes");

        jLabel25.setText("Miercoles");

        jLabel26.setText("Jueves");

        jLabel27.setText("Viernes");

        martesModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                martesModTxtKeyTyped(evt);
            }
        });

        miercolesModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                miercolesModTxtKeyTyped(evt);
            }
        });

        juevesModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                juevesModTxtKeyTyped(evt);
            }
        });

        viernesModTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                viernesModTxtKeyTyped(evt);
            }
        });

        horaIModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

        minutosIModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        horaFModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

        minutosFModBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        jLabel10.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel10.setText("-");

        lunesModBox.setText("Lunes");
        lunesModBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lunesModBoxItemStateChanged(evt);
            }
        });

        martesModBox.setText("Martes");
        martesModBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                martesModBoxItemStateChanged(evt);
            }
        });
        martesModBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                martesModBoxActionPerformed(evt);
            }
        });

        miercolesModBox.setText("Miercoles");
        miercolesModBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                miercolesModBoxItemStateChanged(evt);
            }
        });

        juevesModBox.setText("Jueves");
        juevesModBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                juevesModBoxItemStateChanged(evt);
            }
        });

        viernesModBox.setText("Viernes");
        viernesModBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viernesModBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(idActModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nombreModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(capacidadModTxt)
                                                .addComponent(costoTotalModTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(fechaInicioCalMod, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(119, 119, 119))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(fechaFinCalMod, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(188, 188, 188))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(96, 96, 96))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(361, 361, 361)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(viernesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(juevesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(viernesModBox)
                            .addComponent(juevesModBox))
                        .addGap(0, 237, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel25))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(miercolesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(miercolesModBox))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(martesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(martesModBox))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lunesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lunesModBox))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel20))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel21)
                                .addGap(83, 83, 83)
                                .addComponent(jLabel22)
                                .addGap(48, 48, 48)
                                .addComponent(horaIModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minutosIModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(horaFModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minutosFModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(198, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(378, 378, 378)
                .addComponent(actualizarBtn)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(horaIModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minutosIModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(horaFModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minutosFModBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lunesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)))
                    .addComponent(lunesModBox, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(martesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(martesModBox))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(miercolesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(miercolesModBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(juevesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(juevesModBox))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(viernesModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viernesModBox))
                .addGap(33, 33, 33)
                .addComponent(actualizarBtn)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(idActModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(nombreModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(costoTotalModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(capacidadModTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addComponent(fechaFinCalMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaInicioCalMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(6, 6, 6))))
        );

        actividadesTabbed.addTab("Modificar", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actividadesTabbed)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actividadesTabbed))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void viernesModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viernesModTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || viernesModTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_viernesModTxtKeyTyped

    private void juevesModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_juevesModTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || juevesModTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_juevesModTxtKeyTyped

    private void miercolesModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_miercolesModTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || miercolesModTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_miercolesModTxtKeyTyped

    private void martesModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_martesModTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || martesModTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_martesModTxtKeyTyped

    private void lunesModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lunesModTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || lunesModTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_lunesModTxtKeyTyped

    private void actualizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarBtnActionPerformed
        if (camposModificar()) {
            if (fechaInicioCalMod.getDate().compareTo(fechaFinCalMod.getDate()) < 0 && (convertirFecha(fechaAuxR).compareTo(fechaInicioCalMod.getDate())<=0 || convertirFecha(fechaAuxH).compareTo(fechaInicioCalMod.getDate())<0)) {
                modificarActividad();
                modificarClases();
                limpiarTablaClases();
                limpiarTablaConsulta();
                consultaGeneral();
                consultaClases();
            } else {
                JOptionPane.showMessageDialog(null, "Las fechas estan mal");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }


    }//GEN-LAST:event_actualizarBtnActionPerformed

    private void costoTotalModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costoTotalModTxtKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_costoTotalModTxtKeyTyped

    private void nombreModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreModTxtKeyTyped
        String nombre = actividadConClasesTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || nombreModTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_nombreModTxtKeyTyped

    private void idActModTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idActModTxtKeyTyped
        if (val.id(evt.getKeyChar()) || idActModTxt.getText().length() == 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idActModTxtKeyTyped

    private void actividadConClasesTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadConClasesTxtKeyTyped
        String nombre = actividadConClasesTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || actividadConClasesTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_actividadConClasesTxtKeyTyped

    private void actividadConClasesTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadConClasesTxtKeyReleased
        limpiarTablaClases();
        consultaClases();
    }//GEN-LAST:event_actividadConClasesTxtKeyReleased

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        if (tablaConGen.getSelectedRow() != -1) {
            try {
                int f = tablaConGen.getSelectedRow();
                String id = tablaConGen.getValueAt(f, 0).toString();
                limpiarTxtRadio();
                consultaIndividualClases(id);
                consultaIndividual(id);
                idActModTxt.setText(idActividad);
                idActModTxt.setEditable(false);
                nombreModTxt.setText(nombreA);
                costoTotalModTxt.setText(Integer.toString(costoA));
                
                fechaInicioCalMod.setDate(convertirFecha(fechaInicio));
                fechaAuxR=fechaInicio;
                fechaAuxH=FechaDelDia.getFecha();
                //JOptionPane.showMessageDialog(null, fechaAuxH + "  " + fechaAuxR);
                fechaFinCalMod.setDate(convertirFecha(fechaFin));
                capacidadModTxt.setText(Integer.toString(capacidadU));
                //JOptionPane.showMessageDialog(null, "Realice los cambios en el apartado de modificar");
                actividadesTabbed.setSelectedIndex(2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_modificarBtnActionPerformed

    private void eliminarBtnLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnLActionPerformed
        if (tablaConGen.getSelectedRow() != -1) {
            try {
                int f = tablaConGen.getSelectedRow();
                String id = tablaConGen.getValueAt(f, 0).toString();
                int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar el registro?\nTambien se eliminaran las \nclases asociadas", null, 0, 0, null, botones, this);
                if (eleccion == JOptionPane.YES_OPTION) {
                    eliminarClases(id);
                    eliminarActividad(id, f);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_eliminarBtnLActionPerformed

    private void actividadConGenTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadConGenTxtKeyTyped
        String nombre = actividadConGenTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || actividadConGenTxt.getText().length() == 40) {
            evt.consume();
        }
    }//GEN-LAST:event_actividadConGenTxtKeyTyped

    private void actividadConGenTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actividadConGenTxtKeyReleased
        limpiarTablaConsulta();
        //if (!actividadConGenTxt.getText().equals("")) {
        consultaGeneral();
        //}
    }//GEN-LAST:event_actividadConGenTxtKeyReleased

    private void viernesRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viernesRegTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || viernesRegTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_viernesRegTxtKeyTyped

    private void juevesRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_juevesRegTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || juevesRegTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_juevesRegTxtKeyTyped

    private void miercolesRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_miercolesRegTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || miercolesRegTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_miercolesRegTxtKeyTyped

    private void martesRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_martesRegTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || martesRegTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_martesRegTxtKeyTyped

    private void lunesRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lunesRegTxtKeyTyped
        if (val.horaCompuesta(evt.getKeyChar()) || lunesRegTxt.getText().length() == 11) {
            evt.consume();
        }
    }//GEN-LAST:event_lunesRegTxtKeyTyped

    private void viernesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_viernesCheckBoxItemStateChanged
        if (viernesCheckBox.isSelected()) {
            int horaI = Integer.parseInt(horaInicialBoxReg.getSelectedItem().toString() + minutosInicialBoxReg.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFinalBoxReg.getSelectedItem().toString() + minutosFinalBoxReg.getSelectedItem().toString());
            if (horaI < horaF) {
                viernesRegTxt.setText(getHora());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }

        } else {
            viernesRegTxt.setText("");
        }
    }//GEN-LAST:event_viernesCheckBoxItemStateChanged

    private void juevesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_juevesCheckBoxItemStateChanged
        if (juevesCheckBox.isSelected()) {
            int horaI = Integer.parseInt(horaInicialBoxReg.getSelectedItem().toString() + minutosInicialBoxReg.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFinalBoxReg.getSelectedItem().toString() + minutosFinalBoxReg.getSelectedItem().toString());
            if (horaI < horaF) {
                juevesRegTxt.setText(getHora());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }

        } else {
            juevesRegTxt.setText("");
        }
    }//GEN-LAST:event_juevesCheckBoxItemStateChanged

    private void miercolesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_miercolesCheckBoxItemStateChanged
        if (miercolesCheckBox.isSelected()) {
            int horaI = Integer.parseInt(horaInicialBoxReg.getSelectedItem().toString() + minutosInicialBoxReg.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFinalBoxReg.getSelectedItem().toString() + minutosFinalBoxReg.getSelectedItem().toString());
            if (horaI < horaF) {
                miercolesRegTxt.setText(getHora());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }

        } else {
            miercolesRegTxt.setText("");
        }
    }//GEN-LAST:event_miercolesCheckBoxItemStateChanged

    private void martesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_martesCheckBoxItemStateChanged
        if (martesCheckBox.isSelected()) {
            int horaI = Integer.parseInt(horaInicialBoxReg.getSelectedItem().toString() + minutosInicialBoxReg.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFinalBoxReg.getSelectedItem().toString() + minutosFinalBoxReg.getSelectedItem().toString());
            if (horaI < horaF) {
                martesRegTxt.setText(getHora());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }

        } else {
            martesRegTxt.setText("");
        }
    }//GEN-LAST:event_martesCheckBoxItemStateChanged

    private void lunesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lunesCheckBoxItemStateChanged
        if (lunesCheckBox.isSelected()) {
            int horaI = Integer.parseInt(horaInicialBoxReg.getSelectedItem().toString() + minutosInicialBoxReg.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFinalBoxReg.getSelectedItem().toString() + minutosFinalBoxReg.getSelectedItem().toString());
            if (horaI < horaF) {
                lunesRegTxt.setText(getHora());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }

        } else {
            lunesRegTxt.setText("");
        }
    }//GEN-LAST:event_lunesCheckBoxItemStateChanged

    private void regBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regBtnActionPerformed
        if (camposRegistrar()) {
            if (val.validarEnvioID(idActRegTxt.getText())) {
                if (fechaInicioCalReg.getDate().compareTo(fechaFinCalReg.getDate()) < 0 && convertirFecha(FechaDelDia.getFecha()).compareTo(fechaInicioCalReg.getDate())<0) {
                    if (!lunesCheckBox.isSelected() && !martesCheckBox.isSelected() && !miercolesCheckBox.isSelected() && !juevesCheckBox.isSelected() && !viernesCheckBox.isSelected()) {
                        JOptionPane.showMessageDialog(null, "No agrego ninguna clase");
                    } else {
                        registrarActividad();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Las fechas están mal");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El ID debe contener 2 letras mayúsculas y 3 digitos");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Faltan campos por completar");
        }

    }//GEN-LAST:event_regBtnActionPerformed

    private void costoRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costoRegTxtKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) || costoRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_costoRegTxtKeyTyped

    private void nombreRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreRegTxtKeyTyped
        String nombre = nombreRegTxt.getText().concat(Character.toString(evt.getKeyChar()));
        if (!val.validarNombreNum(nombre) || nombreRegTxt.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_nombreRegTxtKeyTyped

    private void idActRegTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idActRegTxtKeyTyped
        if (val.id(evt.getKeyChar()) || idActRegTxt.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_idActRegTxtKeyTyped

    private void lunesModBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lunesModBoxItemStateChanged
        if (lunesModBox.isSelected()) {
            int horaI = Integer.parseInt(horaIModBox.getSelectedItem().toString() + minutosIModBox.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFModBox.getSelectedItem().toString() + minutosFModBox.getSelectedItem().toString());
            if (horaI < horaF) {
                lunesModTxt.setText(getHoraMod());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }
        }
    }//GEN-LAST:event_lunesModBoxItemStateChanged

    private void martesModBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_martesModBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_martesModBoxActionPerformed

    private void martesModBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_martesModBoxItemStateChanged
        if (martesModBox.isSelected()) {
            int horaI = Integer.parseInt(horaIModBox.getSelectedItem().toString() + minutosIModBox.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFModBox.getSelectedItem().toString() + minutosFModBox.getSelectedItem().toString());
            if (horaI < horaF) {
                martesModTxt.setText(getHoraMod());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }
        }
    }//GEN-LAST:event_martesModBoxItemStateChanged

    private void miercolesModBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_miercolesModBoxItemStateChanged
        if (miercolesModBox.isSelected()) {
            int horaI = Integer.parseInt(horaIModBox.getSelectedItem().toString() + minutosIModBox.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFModBox.getSelectedItem().toString() + minutosFModBox.getSelectedItem().toString());
            if (horaI < horaF) {
                miercolesModTxt.setText(getHoraMod());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }
        }
    }//GEN-LAST:event_miercolesModBoxItemStateChanged

    private void juevesModBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_juevesModBoxItemStateChanged
        if (juevesModBox.isSelected()) {
            int horaI = Integer.parseInt(horaIModBox.getSelectedItem().toString() + minutosIModBox.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFModBox.getSelectedItem().toString() + minutosFModBox.getSelectedItem().toString());
            if (horaI < horaF) {
                juevesModTxt.setText(getHoraMod());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }
        }
    }//GEN-LAST:event_juevesModBoxItemStateChanged

    private void viernesModBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_viernesModBoxItemStateChanged
        if (viernesModBox.isSelected()) {
            int horaI = Integer.parseInt(horaIModBox.getSelectedItem().toString() + minutosIModBox.getSelectedItem().toString());
            int horaF = Integer.parseInt(horaFModBox.getSelectedItem().toString() + minutosFModBox.getSelectedItem().toString());
            if (horaI < horaF) {
                viernesModTxt.setText(getHoraMod());
            } else {
                JOptionPane.showMessageDialog(null, "Las horas están mal");
            }
        }
    }//GEN-LAST:event_viernesModBoxItemStateChanged

    private void modificarClaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarClaBtnActionPerformed
        if (tablaConClases.getSelectedRow() != -1) {
            try {
                int f = tablaConClases.getSelectedRow();
                String id = tablaConClases.getValueAt(f, 0).toString();
                limpiarTxtRadio();
                consultaIndividualClases(id);
                consultaIndividual(id);
                idActModTxt.setText(idActividad);
                idActModTxt.setEditable(false);
                nombreModTxt.setText(nombreA);
                costoTotalModTxt.setText(Integer.toString(costoA));
                fechaInicioCalMod.setDate(convertirFecha(fechaInicio));
                fechaFinCalMod.setDate(convertirFecha(fechaFin));
                capacidadModTxt.setText(Integer.toString(capacidadU));
                //JOptionPane.showMessageDialog(null, "Realice los cambios en el apartado de modificar");
                actividadesTabbed.setSelectedIndex(2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_modificarClaBtnActionPerformed

    private void eliminarClaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarClaBtnActionPerformed
        if (tablaConClases.getSelectedRow() != -1) {
            try {
                int f = tablaConClases.getSelectedRow();
                String id = tablaConClases.getValueAt(f, 0).toString();
                int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar el registro?\nTambien se eliminaran las \nclases asociadas", null, 0, 0, null, botones, this);
                if (eleccion == JOptionPane.YES_OPTION) {
                    eliminarClases(id);
                    eliminarActividad(id, f);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "La fila seleccionada esta vacía");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_eliminarClaBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.RSLabelFecha FechaDelDia;
    private javax.swing.JTextField actividadConClasesTxt;
    private javax.swing.JTextField actividadConGenTxt;
    private javax.swing.JTabbedPane actividadesTabbed;
    private javax.swing.JButton actualizarBtn;
    private javax.swing.JTextField capacidadModTxt;
    private javax.swing.JSpinner capacidadRegSpinner;
    private javax.swing.JTabbedPane consultarTab;
    private javax.swing.JTextField costoRegTxt;
    private javax.swing.JTextField costoTotalModTxt;
    private javax.swing.JButton eliminarBtnL;
    private javax.swing.JButton eliminarClaBtn;
    private com.toedter.calendar.JDateChooser fechaFinCalMod;
    private com.toedter.calendar.JDateChooser fechaFinCalReg;
    private com.toedter.calendar.JDateChooser fechaInicioCalMod;
    private com.toedter.calendar.JDateChooser fechaInicioCalReg;
    private javax.swing.JComboBox<String> horaFModBox;
    private javax.swing.JComboBox<String> horaFinalBoxReg;
    private javax.swing.JComboBox<String> horaIModBox;
    private javax.swing.JComboBox<String> horaInicialBoxReg;
    private javax.swing.JTextField idActModTxt;
    private javax.swing.JTextField idActRegTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox juevesCheckBox;
    private javax.swing.JCheckBox juevesModBox;
    private javax.swing.JTextField juevesModTxt;
    private javax.swing.JTextField juevesRegTxt;
    private javax.swing.JCheckBox lunesCheckBox;
    private javax.swing.JCheckBox lunesModBox;
    private javax.swing.JTextField lunesModTxt;
    private javax.swing.JTextField lunesRegTxt;
    private javax.swing.JCheckBox martesCheckBox;
    private javax.swing.JCheckBox martesModBox;
    private javax.swing.JTextField martesModTxt;
    private javax.swing.JTextField martesRegTxt;
    private javax.swing.JCheckBox miercolesCheckBox;
    private javax.swing.JCheckBox miercolesModBox;
    private javax.swing.JTextField miercolesModTxt;
    private javax.swing.JTextField miercolesRegTxt;
    private javax.swing.JComboBox<String> minutosFModBox;
    private javax.swing.JComboBox<String> minutosFinalBoxReg;
    private javax.swing.JComboBox<String> minutosIModBox;
    private javax.swing.JComboBox<String> minutosInicialBoxReg;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JButton modificarClaBtn;
    private javax.swing.JTextField nombreModTxt;
    private javax.swing.JTextField nombreRegTxt;
    private javax.swing.JButton regBtn;
    private javax.swing.JTable tablaConClases;
    private javax.swing.JTable tablaConGen;
    private javax.swing.JCheckBox viernesCheckBox;
    private javax.swing.JCheckBox viernesModBox;
    private javax.swing.JTextField viernesModTxt;
    private javax.swing.JTextField viernesRegTxt;
    // End of variables declaration//GEN-END:variables
}
