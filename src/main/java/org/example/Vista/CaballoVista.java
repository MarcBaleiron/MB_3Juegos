package org.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;

public class CaballoVista extends JPanel
{
    private int boardSize;
    private JPanel[][] squares;
    private DefaultListModel<String> moveListModel;
    private JList<String> moveList;
    private JButton returnButton;
    private JButton saveButton;

    public CaballoVista(int boardSize)
    {
        this.boardSize = boardSize;
        this.squares = new JPanel[boardSize][boardSize];

        setLayout(new BorderLayout());

        // Crear panel del tablero
        JPanel boardPanel = createBoardPanel();

        // Crear lista de movimientos
        moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        JScrollPane moveScrollPane = new JScrollPane(moveList);
        moveScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        moveScrollPane.setPreferredSize(new Dimension(200, 0));

        // Crear botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        returnButton = new JButton("Regresar a la Pantalla Inicial");
        saveButton = new JButton("Guardar en Base de Datos");
        buttonPanel.add(returnButton);
        buttonPanel.add(saveButton);

        // Contenedor principal
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainContainer.add(boardPanel, BorderLayout.CENTER);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        // Añadir componentes al panel principal
        add(mainContainer, BorderLayout.CENTER);
        add(moveScrollPane, BorderLayout.EAST);
    }

    private JPanel createBoardPanel()
    {
        JPanel boardPanel = new JPanel(new GridLayout(boardSize, boardSize))
        {
            @Override
            public Dimension getPreferredSize()
            {
                int size = Math.min(getParent().getWidth(), getParent().getHeight());
                return new Dimension(size, size);
            }

            @Override
            public void setBounds(int x, int y, int width, int height)
            {
                int size = Math.min(width, height);
                super.setBounds(x, y, size, size);
            }
        };

        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++)
            {
                JPanel square = new JPanel();
                square.setBorder(new LineBorder(Color.BLACK));
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                squares[row][col] = square;
                boardPanel.add(square);
            }
        }

        return boardPanel;
    }

    public void actualizarTablero(int[][] board)
    {
        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++)
            {
                squares[row][col].removeAll();
                squares[row][col].setLayout(new BorderLayout());
                if (board[row][col] != -1) {
                    JLabel label = new JLabel(String.valueOf(board[row][col]));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setVerticalAlignment(SwingConstants.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 18));
                    squares[row][col].add(label, BorderLayout.CENTER);
                }
                squares[row][col].revalidate();
                squares[row][col].repaint();
            }
        }
    }

    public void addMoveToList(String move)
    {
        moveListModel.addElement(move);
    }

    public JButton getBotonRegresar() { return returnButton; }
    public JButton getSaveButton() { return saveButton; }
}