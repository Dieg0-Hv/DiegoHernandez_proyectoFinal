/**
 *  Día 1 de la Feria con los juegos de Salvado y Cuadrado Mágico
 * @author Diego Hernandez Vazquez
 * @version 2.0  
 * @date 27 nov 2024
 */


import java.io.*;
import java.util.*;

public class Feria {
    static Map<String, Jugador> jugadores = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static final int CREDITO_INICIAL = 100;
    static final int COSTO_JUEGO = 15;
    static final String ARCHIVO_JUGADORES = "jugadores.dat";
    static String diaActual = "Día 1";
    static final int N = 4; // Tamaño del tablero para el Cuadrado Mágico
    
    public static void main(String[] args) {
        cargarDatos();
        
        while (true) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    registrarJugador();
                    break;
                case 2:
                    iniciarJuego();
                    break;
                case 3:
                    verMejoresJugadores();
                    break;
                case 4:
                    verPuntosJugador();
                    break;
                case 5:
                    guardarYSalir();
                    return;
                default:
                    System.out.println("Opción no válida. Intenta nuevamente.");
            }
        }
    }

    // Menú de opciones
    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Registrar jugador");
        System.out.println("2. Iniciar nuevo juego");
        System.out.println("3. Ver mejores 3 jugadores");
        System.out.println("4. Ver puntos acumulados por el jugador actual");
        System.out.println("5. Guardar y salir");
        System.out.print("Selecciona una opción: ");
    }

    // Registrar un nuevo jugador
    private static void registrarJugador() {
        System.out.print("Ingresa tu nombre: ");
        String nombre = scanner.nextLine();
        
        if (jugadores.containsKey(nombre)) {
            System.out.println("¡Bienvenido de nuevo, " + nombre + "!");
            System.out.println("Tienes " + jugadores.get(nombre).getCreditos() + " créditos disponibles");
        } else {
            Jugador nuevoJugador = new Jugador(nombre, CREDITO_INICIAL);
            jugadores.put(nombre, nuevoJugador);
            System.out.println("¡Registro exitoso! Has sido registrado con " + CREDITO_INICIAL + " créditos");
        }
    }

    // Iniciar un nuevo juego
    private static void iniciarJuego() {
        System.out.println("\nSelecciona un juego para jugar:");
        if (diaActual.equals("Día 1")) {
            System.out.println("1. Cuadrado Mágico (Costo: 15 créditos)");
            System.out.println("2. Salvado (Costo: 15 créditos)");
        } else if (diaActual.equals("Día 2")) {
            System.out.println("1. Cuadrado Mágico (Costo: 15 créditos)");
        }
        
        int opcionJuego = scanner.nextInt();
        scanner.nextLine(); 
        
        System.out.print("Ingresa tu nombre para continuar: ");
        String nombreJugador = scanner.nextLine();
        
        if (!jugadores.containsKey(nombreJugador)) {
            System.out.println("Jugador no registrado. Primero debes registrarte.");
            return;
        }
        
        Jugador jugador = jugadores.get(nombreJugador);
        
        if (jugador.getCreditos() < COSTO_JUEGO) {
            System.out.println("¡No tienes suficientes créditos para jugar! Tienes " + jugador.getCreditos() + " créditos.");
            return;
        }
        
        jugador.reducirCreditos(COSTO_JUEGO);
        System.out.println("¡Bienvenido al juego! Estás jugando el juego del " + (diaActual.equals("Día 1") ? "Cuadrado Mágico" : "Salvado"));
        
        // Aquí se llama a los métodos de los juegos 
        if (opcionJuego == 1) {
            jugarCuadradoMagico();
        } else if (opcionJuego == 2 && diaActual.equals("Día 1")) {
            jugarSalvado();
        } else {
            System.out.println("Opción de juego no válida.");
        }
    }

    // Juego Cuadrado Mágico

    public static boolean esCuadradoMagico(int[][] tablero) {
        int N = 4;  // Definir tamaño del tablero (4x4)
        int sumaMagica = N * (N * N + 1) / 2;

        // Verificar filas
        for (int i = 0; i < N; i++) {
            int sumaFila = 0;
            for (int j = 0; j < N; j++) {
                sumaFila += tablero[i][j];
            }
            if (sumaFila != sumaMagica) {
                return false;
            }
        }

        // Verificar columnas
        for (int j = 0; j < N; j++) {
            int sumaColumna = 0;
            for (int i = 0; i < N; i++) {
                sumaColumna += tablero[i][j];
            }
            if (sumaColumna != sumaMagica) {
                return false;
            }
        }

        // Verificar diagonales
        int sumaDiagonal1 = 0, sumaDiagonal2 = 0;
        for (int i = 0; i < N; i++) {
            sumaDiagonal1 += tablero[i][i];
            sumaDiagonal2 += tablero[i][N - 1 - i];
        }
        
        return sumaDiagonal1 == sumaMagica && sumaDiagonal2 == sumaMagica;
    }

    private static void jugarCuadradoMagico() {
        int puntuacionJugador = 0;
        Scanner scanner = new Scanner(System.in);
        
        int[][] tablero = inicializarTablero();
        mostrarTablero(tablero);
        
        System.out.println("Completa el tablero para que sea un cuadrado mágico.");
        int numerosUsados = 0;
        
        while (numerosUsados < 16) {
            System.out.print("Ingresa la fila (0-3): ");
            int fila = scanner.nextInt();
    
            System.out.print("Ingresa la columna (0-3): ");
            int columna = scanner.nextInt();
    
            if (tablero[fila][columna] != 0) {
                System.out.println("Esta celda ya tiene un valor fijo o ya has colocado un número aquí.");
                continue;
            }
    
            System.out.print("Ingresa el número que deseas colocar (1-16): ");
            int numero = scanner.nextInt();
    
            if (numero < 1 || numero > 16 || contiene(tablero, numero)) {
                System.out.println("Número inválido o ya utilizado. Intenta de nuevo.");
                continue;
            }
    
            tablero[fila][columna] = numero;
            numerosUsados++;
            mostrarTablero(tablero);
    
            if (!esPosibleCuadradoMagico(tablero)) {
                System.out.println("No es posible completar el cuadrado mágico. ¡Has perdido!");
                return;
            }
        }
    
        if (esCuadradoMagico(tablero)) {
            System.out.println("¡Felicitaciones! Has completado el cuadrado mágico.");
            puntuacionJugador += 10;
        } else {
            System.out.println("El tablero no es un cuadrado mágico. Mejor suerte la próxima vez.");
        }
    
        System.out.println("Tu puntuación en este juego es: " + puntuacionJugador);
    }

    // Métodos auxiliares para "Cuadrado Mágico"
    private static int[][] inicializarTablero() {
        int[][] tablero = new int[N][N];
        tablero[0][0] = 1;
        tablero[1][1] = 6;
        tablero[2][2] = 11;
        tablero[3][3] = 16;
        return tablero;
    }

    private static void mostrarTablero(int[][] tablero) {
        for (int[] fila : tablero) {
            for (int celda : fila) {
                System.out.print((celda == 0 ? "_" : celda) + "\t");
            }
            System.out.println();
        }
    }

    private static boolean contiene(int[][] tablero, int numero) {
        for (int[] fila : tablero) {
            for (int celda : fila) {
                if (celda == numero) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean esPosibleCuadradoMagico(int[][] tablero) {
        int sumaMagica = N * (N * N + 1) / 2;
        int[] filas = new int[N];
        int[] columnas = new int[N];
        int diagonal1 = 0, diagonal2 = 0;
    
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tablero[i][j] != 0) {
                    filas[i] += tablero[i][j];
                    columnas[j] += tablero[i][j];
                    if (i == j) diagonal1 += tablero[i][j];
                    if (i + j == N - 1) diagonal2 += tablero[i][j];
                }
            }
        }
    
        for (int k = 0; k < N; k++) {
            if (filas[k] > sumaMagica || columnas[k] > sumaMagica) {
                return false;
            }
        }
        return diagonal1 <= sumaMagica && diagonal2 <= sumaMagica;
    }

    // Método para jugar Salvado (lo implementas de acuerdo a lo que tenías antes)
    private static void jugarSalvado() {
        System.out.println("Jugando Salvado...");
        // Aquí puedes implementar la lógica del juego "Salvado" como lo tenías antes.
    }

    // Mostrar los mejores 3 jugadores
    private static void verMejoresJugadores() {
        System.out.println("\nTop 3 Jugadores:");
        List<Jugador> listaJugadores = new ArrayList<>(jugadores.values());
        listaJugadores.sort(Comparator.comparingInt(Jugador::getPuntos).reversed());
        
        for (int i = 0; i < Math.min(3, listaJugadores.size()); i++) {
            Jugador jugador = listaJugadores.get(i);
            System.out.println((i + 1) + ". " + jugador.getNombre() + " - " + jugador.getPuntos() + " puntos");
        }
    }

    // Ver los puntos acumulados por el jugador actual
    private static void verPuntosJugador() {
        System.out.print("Ingresa tu nombre: ");
        String nombreJugador = scanner.nextLine();
        
        if (jugadores.containsKey(nombreJugador)) {
            Jugador jugador = jugadores.get(nombreJugador);
            System.out.println("Tienes " + jugador.getPuntos() + " puntos acumulados.");
        } else {
            System.out.println("Jugador no registrado.");
        }
    }

    // Guardar los datos y salir
    private static void guardarYSalir() {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_JUGADORES))) {
            salida.writeObject(jugadores);
            System.out.println("¡Datos guardados exitosamente!");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos.");
        }
    }

// Cargar los datos desde un archivo
private static void cargarDatos() {
    try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_JUGADORES))) {
        // Deserializamos el objeto
        Object obj = entrada.readObject();
        
        // Verificamos que el objeto sea una instancia de Map
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            
            // Verificamos que las claves sean de tipo String y los valores de tipo Jugador
            if (!map.isEmpty() && map.keySet().iterator().next() instanceof String && map.values().iterator().next() instanceof Jugador) {
                // Hacemos el cast después de la validación
                jugadores = new HashMap<>();
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    // Solo hacer el cast a String y Jugador de manera segura
                    String key = (String) entry.getKey();
                    Jugador value = (Jugador) entry.getValue();
                    jugadores.put(key, value);
                }
            }
        }
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("No se encontraron datos previos o hubo un error al cargarlos.");
    }
}

    // Clase Jugador
    static class Jugador implements Serializable {
        private String nombre;
        private int puntos;
        private int creditos;

        public Jugador(String nombre, int creditos) {
            this.nombre = nombre;
            this.creditos = creditos;
            this.puntos = 0; // Inicialmente sin puntos
        }

        public String getNombre() {
            return nombre;
        }

        public int getPuntos() {
            return puntos;
        }

        public void agregarPuntos(int puntos) {
            this.puntos += puntos;
        }

        public int getCreditos() {
            return creditos;
        }

        public void reducirCreditos(int cantidad) {
            this.creditos -= cantidad;
        }
    }
}
