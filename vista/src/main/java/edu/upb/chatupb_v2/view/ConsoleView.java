package edu.upb.chatupb_v2.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import edu.upb.chatupb_v2.controller.ContactController;
import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.model.entities.*;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.controller.interfaces.IChatView;

import java.io.IOException;
import java.util.List;

public class ConsoleView implements IChatView {
    private final String MI_ID = "83768ab5-4019-4cfa-a59e-f09e9d842fea";
    private SocketClient socketClient;
    private final ContactController contactController;

    private Contact contactoActual = null;

    // Componentes de Lanterna
    private MultiWindowTextGUI gui;
    private BasicWindow mainWindow;
    private Label statusLabel;
    private TextBox chatHistoryBox;
    private TextBox inputBox;

    public ConsoleView() throws IOException {
        Mediador.getInstance().setChatView(this);
        this.contactController = new ContactController(this);
    }

    public static void main(String[] args) {
        try {
            ConsoleView console = new ConsoleView();
            console.iniciarGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarGUI() throws IOException {
        // 1. Configurar la Terminal y la Pantalla
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        factory.setTerminalEmulatorTitle("Chat UPB - Consola"); // <-- Aquí pones tu título
        //factory.setForceTextTerminal(true);
        Terminal terminal = factory.createTerminal();

        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // 2. Crear el gestor de ventanas (Fondo azul oscuro)
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));

        // 3. Crear la ventana principal y su diseño (BorderLayout)
        mainWindow = new BasicWindow("Chat UPB CLI - TUI Mode");
        Panel mainPanel = new Panel(new BorderLayout());

        // --- PARTE SUPERIOR: Estado ---
        statusLabel = new Label("Estado: Desconectado");
        mainPanel.addComponent(statusLabel, BorderLayout.Location.TOP);

        // --- PARTE CENTRAL: Historial de Chat ---
        chatHistoryBox = new TextBox(new TerminalSize(50, 15), TextBox.Style.MULTI_LINE);
        chatHistoryBox.setReadOnly(true); // El usuario no puede borrar el historial
        mainPanel.addComponent(chatHistoryBox, BorderLayout.Location.CENTER);

        // --- PARTE INFERIOR: Entrada de texto y botón Enviar ---
        Panel inputPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        inputBox = new TextBox(new TerminalSize(40, 1));
        Button sendButton = new Button("Enviar", this::enviarMensajeChat);

        inputPanel.addComponent(inputBox);
        inputPanel.addComponent(sendButton);
        mainPanel.addComponent(inputPanel, BorderLayout.Location.BOTTOM);

        // --- PARTE DERECHA: Menú de Acciones ---
        ActionListBox menu = new ActionListBox();
        menu.addItem("1. Ver Contactos", () -> {
            try {
                contactController.loadContacts();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        menu.addItem("2. Conectar a IP", this::modalEnviarInvitacion);
        menu.addItem("3. Salir", () -> mainWindow.close());
        mainPanel.addComponent(menu, BorderLayout.Location.RIGHT);

        // 4. Acoplar y mostrar
        mainWindow.setComponent(mainPanel);

        // Esto bloquea el hilo principal y gestiona los eventos hasta que se cierre la ventana
        gui.addWindowAndWait(mainWindow);

        // 5. Limpieza al salir
        if (socketClient != null) {
            socketClient.close();
        }
        screen.stopScreen();
    }

    private void modalEnviarInvitacion() {
        // Lanza un pop-up nativo de consola para pedir la IP
        String ipDestino = TextInputDialog.showDialog(gui, "Nueva Conexión", "Ingrese la IP a conectarse:", "");

        if (ipDestino != null && !ipDestino.trim().isEmpty()) {
            try {
                this.socketClient = new SocketClient(ipDestino);
                this.socketClient.start();
                this.socketClient.addListener(Mediador.getInstance());

                Invitacion nuevaInvitacion = new Invitacion(MI_ID, "Profe");
                this.socketClient.send(nuevaInvitacion);

                MessageDialog.showMessageDialog(gui, "Éxito", "Invitación enviada a " + ipDestino + "\nEsperando respuesta...");
            } catch (IOException e) {
                MessageDialog.showMessageDialog(gui, "Error", "No se pudo conectar: " + e.getMessage());
            }
        }
    }

    private void enviarMensajeChat() {
        if (contactoActual == null) {
            MessageDialog.showMessageDialog(gui, "Atención", "Aún no estás conectado con nadie. Usa la opción 'Conectar a IP'.");
            return;
        }

        String texto = inputBox.getText();
        if (texto.trim().isEmpty()) return;

        TextMessage message = new TextMessage(MI_ID, texto);
        agregarAlHistorial("Yo: " + texto); // Mostramos visualmente lo que enviamos

        try {
            socketClient.send(message);
            inputBox.setText(""); // Limpiamos la caja de texto tras enviar
            inputBox.takeFocus(); // Devolvemos el cursor a la caja de texto
        } catch (IOException e) {
            MessageDialog.showMessageDialog(gui, "Error", "Fallo al enviar el mensaje.");
        }
    }

    private void agregarAlHistorial(String linea) {
        String textoActual = chatHistoryBox.getText();
        if (textoActual.isEmpty()) {
            chatHistoryBox.setText(linea);
        } else {
            chatHistoryBox.setText(textoActual + "\n" + linea);
        }
        // Lanterna maneja el scroll automático hacia abajo si sobrepasa el TerminalSize
    }

    @Override
    public void onLoadData(List<Contact> contacts) {
        StringBuilder sb = new StringBuilder();
        if (contacts.isEmpty()) {
            sb.append("No tienes contactos agregados.");
        } else {
            contacts.forEach(c -> sb.append(c.toString()).append("\n"));
        }
        // Mostramos los contactos en un pop-up central
        MessageDialog.showMessageDialog(gui, "Lista de Contactos", sb.toString());
    }

    @Override
    public void onMessage(AbstractMessage mensajeRecibido) {
        if (mensajeRecibido instanceof Aceptar aceptar) {
            contactoActual = new Contact(aceptar.getIdUsuario(), aceptar.getNombre(), aceptar.getIp(), true);
            statusLabel.setText("Estado: 🟢 Conectado con " + contactoActual.getName());
            agregarAlHistorial("🔔 INVITACIÓN ACEPTADA! Ya puedes chatear.");
            return;
        }

        if (mensajeRecibido instanceof TextMessage message) {
            agregarAlHistorial(message.toString());
        }
    }
}
