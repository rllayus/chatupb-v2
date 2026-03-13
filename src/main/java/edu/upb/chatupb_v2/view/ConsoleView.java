package edu.upb.chatupb_v2.view;

import edu.upb.chatupb_v2.controller.ContactController;
import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.model.entities.*;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.view.interfaces.IChatView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements IChatView {
    private final String MI_ID="83768ab5-4019-4cfa-a59e-f09e9d842fea";
    private final List<TextMessage> historialChat = new ArrayList<>();
    private SocketClient socketClient;
    private final ContactController contactController;
    private final Scanner scanner;


    private Contact contactoActual = null;
    private boolean enSalaDeChat = false;

    public ConsoleView() throws IOException {
        Mediador.getInstance().setChatView(this);
        this.contactController = new ContactController(this);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        try {
            ConsoleView console = new ConsoleView();
            console.iniciarMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarMenu() throws Exception {
        boolean enEjecucion = true;
        System.out.println("=====================================");
        System.out.println("       BIENVENIDO AL CHAT CLI        ");
        System.out.println("=====================================");

        while (enEjecucion) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("Estado actual: " + (contactoActual != null ? "Conectado con " + contactoActual.getName() : "Desconectado"));
            System.out.println("1. Ver lista de Contactos");
            System.out.println("2. Conectar a una IP (Enviar Invitación)");
            System.out.println("3. Entrar a la Sala de Chat");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> this.contactController.loadContacts();
                case "2" -> enviarInvitacion();
                case "3" -> iniciarSalaDeChat();
                case "4" -> {
                    System.out.println("Cerrando sesión. ¡Hasta luego!");
                    enEjecucion = false;
                    if (socketClient != null) {
                        socketClient.close();
                    }
                }
                default -> System.out.println("❌ Opción no válida. Intenta de nuevo.");
            }
        }
        scanner.close();
    }

    private void enviarInvitacion() throws IOException {
        System.out.print("\nIngrese la IP a la que desea conectarse: ");
        String ipDestino = scanner.nextLine();

        System.out.println("Enviando invitación a " + ipDestino + "...");

        this.socketClient = new SocketClient(ipDestino);
        this.socketClient.start();
        this.socketClient.addListener(Mediador.getInstance());

        Invitacion nuevaInvitacion = new Invitacion(MI_ID, "Profe");
        this.socketClient.send(nuevaInvitacion);
        System.out.println("✅ Invitación enviada. Esperando a que el usuario acepte...");
    }


    private void iniciarSalaDeChat() {
        if (contactoActual == null) {
            System.out.println("\n❌ Aún no estás conectado con nadie. Usa la opción 2 para enviar una invitación primero.");
            return;
        }

        enSalaDeChat = true;
        System.out.println("\n=====================================");
        System.out.println("    SALA DE CHAT CON: " + contactoActual.getName());
        System.out.println("  (Escribe '/salir' para volver al menú)");
        System.out.println("=====================================\n");

        historialChat.forEach(System.out::println);

        while (enSalaDeChat) {
            System.out.print("> ");
            String textoMensaje = scanner.nextLine();

            if (textoMensaje.trim().equalsIgnoreCase("/salir")) {
                enSalaDeChat = false;
                break;
            }

            if (!textoMensaje.trim().isEmpty()) {
                TextMessage message = new TextMessage(MI_ID, textoMensaje);
                historialChat.add(message);
                try {
                    socketClient.send(message);
                }catch (IOException e) {
                    System.out.println("❌ Error al enviar mensaje. Causa: "+e.getMessage());
                }
            }
        }
    }

    @Override
    public void onLoadData(List<Contact> contacts) {
        System.out.println("\n--- CONTACTOS ---");
        if (contacts.isEmpty()) {
            System.out.println("No tienes contactos agregados.");
        } else {
            contacts.forEach(System.out::println);
        }
        System.out.println("-----------------");
    }

    @Override
    public void onMessage(AbstractMessage mensajeRecibido)  {
        if (mensajeRecibido instanceof Aceptar aceptar) {
            System.out.println("\n\n🔔 ¡INVITACIÓN ACEPTADA! Ingresa a la sala\n> ");
            contactoActual = new Contact(aceptar.getIdUsuario(), aceptar.getNombre(), aceptar.getIp(), true);
            return;
        }

        if(mensajeRecibido instanceof TextMessage message){
            historialChat.add(message);

            if (enSalaDeChat) {
                System.out.println("\n" + message);
                System.out.print("> ");
            } else {
                System.out.println("\n\n💬 Tienes un nuevo mensaje en la Sala de Chat.\n> ");
            }
        }
    }
}
