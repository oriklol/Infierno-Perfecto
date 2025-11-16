package com.dojan.infiernoperfecto.pantallas.enciclopedia;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EntradaEnciclopedia {

    // ========== ATAQUES DEL PELEADOR ==========
    ESPADAZO(
        "Espadazo",
        "Daño: 40\n" +
            "Precisión: 100%\n" +
            "Usos: Infinitos\n" +
            "Costo Fe: 0",
        CategoriaEnciclopedia.ATAQUE_FISICO,
        ClaseEnciclopedia.PELEADOR,
        1 // rareza
    ),

    GRAN_GOLPE(
        "Gran Golpe",
        "Daño: 505\n" +
            "Precisión: 95%\n" +
            "Usos: 100",
        CategoriaEnciclopedia.ATAQUE_FISICO,
        ClaseEnciclopedia.PELEADOR,
        3
    ),

    AFILAMIENTO(
        "Afilamiento",
        "Daño: 0\n" +
            "Precisión: 100%\n" +
            "Usos: 5\n" +
            "Efecto: Aumenta daño temporalmente",
        CategoriaEnciclopedia.ATAQUE_BUFF,
        ClaseEnciclopedia.PELEADOR,
        2
    ),

    ULTIMO_ALIENTO(
        "Último Aliento",
        "Daño: 50\n" +
            "Precisión: 100%\n" +
            "Usos: 10\n" +
            "Costo Fe: 30",
        CategoriaEnciclopedia.ATAQUE_ESPECIAL,
        ClaseEnciclopedia.PELEADOR,
        4
    ),

    // ========== ATAQUES DEL MÁGICO ==========
    HECHIZO_BASICO(
        "Hechizo Básico",
        "Daño: 60\n" +
            "Precisión: 90%\n" +
            "Usos: 50",
        CategoriaEnciclopedia.ATAQUE_MAGICO,
        ClaseEnciclopedia.MAGICO,
        1
    ),

    BOLA_DE_FUEGO(
        "Bola de Fuego",
        "Daño: 90\n" +
            "Precisión: 70%\n" +
            "Usos: 10",
        CategoriaEnciclopedia.ATAQUE_MAGICO,
        ClaseEnciclopedia.MAGICO,
        3
    ),

    BANIO_TOTAL(
        "Baño Total",
        "Daño: 50\n" +
            "Precisión: 100%\n" +
            "Usos: 10",
        CategoriaEnciclopedia.ATAQUE_MAGICO,
        ClaseEnciclopedia.MAGICO,
        2
    ),

    CONCENTRACION(
        "Concentración",
        "Daño: 0\n" +
            "Precisión: 100%\n" +
            "Usos: 10\n" +
            "Efecto: Aumenta daño mágico",
        CategoriaEnciclopedia.ATAQUE_BUFF,
        ClaseEnciclopedia.MAGICO,
        2
    ),

    // ========== ATAQUES DEL SOPORTE ==========
    BESO_DE_ANGEL(
        "Beso de Angel",
        "Curación: Variable\n" +
            "Precisión: 100%\n" +
            "Usos: Infinitos",
        CategoriaEnciclopedia.ATAQUE_CURACION,
        ClaseEnciclopedia.SOPORTE,
        2
    ),

    EN_EL_NOMBRE_DEL_PADRE(
        "En el nombre del padre del hijo y del espíritu santo",
        "Daño: 77\n" +
            "Precisión: 100%\n" +
            "Usos: 2",
        CategoriaEnciclopedia.ATAQUE_SAGRADO,
        ClaseEnciclopedia.SOPORTE,
        5
    ),

    // ========== ITEMS ==========
    VINO_MEDIO_VACIO(
        "Vino Medio Vacío",
        "Curación: 20 HP\n" +
            "Precio: 30 monedas",
        CategoriaEnciclopedia.ITEM_CONSUMIBLE,
        null,
        1
    ),

    ANGEL_BOSTEZANDO(
        "Pequeño Ángel Bostezando",
        "Curación: 30 HP\n" +
            "Precio: 40 monedas",
        CategoriaEnciclopedia.ITEM_CONSUMIBLE,
        null,
        2
    ),

    ZANAHORIA_INTERDIMENSIONAL(
        "Zanahoria Interdimensional",
        "Curación: 50 HP\n" +
            "Precio: 70 monedas",
        CategoriaEnciclopedia.ITEM_CONSUMIBLE,
        null,
        3
    ),

    // ========== ENEMIGOS ==========
    MINI_ON(
        "Mini-on",
        "HP: 70\n" +
            "Defensa: 50\n" +
            "Daño: 45\n" +
            "Ataques: Mordisco, Rasguño, Amenaza, Agrandamiento",
        CategoriaEnciclopedia.ENEMIGO_NORMAL,
        null,
        1
    ),

    ESBIRRO(
        "Esbirro",
        "HP: 200\n" +
            "Defensa: 30\n" +
            "Daño: 40\n" +
            "Ataques: Mordisco, Rasguño, Amenaza, Agrandamiento",
        CategoriaEnciclopedia.ENEMIGO_NORMAL,
        null,
        2
    ),

    MINIBOSS_LIMBO(
        "Sabueso del Limbo",
        "HP: 300\n" +
            "Defensa: 60\n" +
            "Daño: 65\n" +
            "Ataques: Doble Golpe, Mordisco, Amenaza, Agrandamiento",
        CategoriaEnciclopedia.ENEMIGO_JEFE,
        null,
        4
    ),

    BOSS_FINAL(
        "Caos",
        "HP: 500\n" +
            "Defensa: 70\n" +
            "Daño: 80\n" +
            "Ataques: ???",
        CategoriaEnciclopedia.ENEMIGO_JEFE,
        null,
        5
    );

    // ========== ATRIBUTOS ==========
    private final String nombre;
    private final String estadisticas;
    private final CategoriaEnciclopedia categoria;
    private final ClaseEnciclopedia claseAsociada;
    private final int rareza; // 1-5 estrellas

    // ========== CONSTRUCTOR ==========
    EntradaEnciclopedia(String nombre, String estadisticas,
                        CategoriaEnciclopedia categoria,
                        ClaseEnciclopedia claseAsociada,int rareza) {
        this.nombre = nombre;
        this.estadisticas = estadisticas;
        this.categoria = categoria;
        this.claseAsociada = claseAsociada;
        this.rareza = rareza;
    }

    // ========== GETTERS ==========
    public String getNombre() { return nombre; }
    public String getEstadisticas() { return estadisticas; }
    public CategoriaEnciclopedia getCategoria() { return categoria; }
    public ClaseEnciclopedia getClaseAsociada() { return claseAsociada; }
    public int getRareza() { return rareza; }

    // ========== MÉTODOS DE FILTRADO ==========

    public static List<EntradaEnciclopedia> obtenerTodos() {
        return Arrays.asList(values());
    }

    public static List<EntradaEnciclopedia> filtrarPorCategoria(CategoriaEnciclopedia cat) {
        return Arrays.stream(values())
            .filter(e -> e.categoria == cat)
            .collect(Collectors.toList());
    }

    public static List<EntradaEnciclopedia> filtrarPorClase(ClaseEnciclopedia clase) {
        return Arrays.stream(values())
            .filter(e -> e.claseAsociada == clase)
            .collect(Collectors.toList());
    }

    public static List<EntradaEnciclopedia> filtrarPorRareza(int rarezaMinima) {
        return Arrays.stream(values())
            .filter(e -> e.rareza >= rarezaMinima)
            .sorted((a, b) -> Integer.compare(b.rareza, a.rareza))
            .collect(Collectors.toList());
    }

    public static List<EntradaEnciclopedia> buscarPorNombre(String query) {
        String queryLower = query.toLowerCase();
        return Arrays.stream(values())
            .filter(e -> e.nombre.toLowerCase().contains(queryLower))
            .collect(Collectors.toList());
    }

    public static List<CategoriaEnciclopedia> obtenerCategoriasUnicas() {
        return Arrays.stream(values())
            .map(e -> e.categoria)
            .distinct()
            .collect(Collectors.toList());
    }

    // ========== MÉTODO PARA TEXTO COMPLETO ==========
    public String getTextoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════\n");
        sb.append(nombre.toUpperCase()).append("\n");
        sb.append("═══════════════════════════════\n\n");

        // Rareza en estrellas
        sb.append("Rareza: ");
        for (int i = 0; i < rareza; i++) sb.append("★");
        for (int i = rareza; i < 5; i++) sb.append("☆");
        sb.append("\n\n");

        sb.append("ESTADÍSTICAS:\n");
        sb.append(estadisticas).append("\n\n");



        sb.append("Categoría: ").append(categoria.getNombre()).append("\n");
        if (claseAsociada != null) {
            sb.append("Clase: ").append(claseAsociada.getNombre()).append("\n");
        }

        return sb.toString();
    }
}
