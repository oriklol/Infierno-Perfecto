package com.dojan.infiernoperfecto.enciclopedia;

import com.badlogic.gdx.graphics.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EntradaEnciclopedia {

    // ========== ATAQUES DEL PELEADOR ==========
    ESPADAZO(
        "Espadazo",
        "Un golpe básico pero confiable. Tu espada nunca te fallará.",
        "Daño: 40\n" +
            "Precisión: 100%\n" +
            "Usos: Infinitos\n" +
            "Costo Fe: 0",
        "💡 Tip: Perfecto para enemigos con alta evasión",
        CategoriaEnciclopedia.ATAQUE_FISICO,
        ClaseEnciclopedia.PELEADOR,
        1 // rareza
    ),

    GRAN_GOLPE(
        "Gran Golpe",
        "Un ataque devastador que concentra toda tu fuerza en un solo golpe.",
        "Daño: 505\n" +
            "Precisión: 95%\n" +
            "Usos: 100",
        "⚠️ Nota: El daño parece excesivo, puede ser un bug.\n" +
            "💡 Tip: Guarda algunos usos para jefes finales",
        CategoriaEnciclopedia.ATAQUE_FISICO,
        ClaseEnciclopedia.PELEADOR,
        3
    ),

    AFILAMIENTO(
        "Afilamiento",
        "Afilas tu espada, aumentando el daño de tus próximos ataques.",
        "Daño: 0\n" +
            "Precisión: 100%\n" +
            "Usos: 5\n" +
            "Efecto: Aumenta daño temporalmente",
        "💡 Tip: Úsalo antes de combates difíciles",
        CategoriaEnciclopedia.ATAQUE_BUFF,
        ClaseEnciclopedia.PELEADOR,
        2
    ),

    ULTIMO_ALIENTO(
        "Último Aliento",
        "Un ataque desesperado que consume Fe. Solo úsalo cuando todo esté perdido.",
        "Daño: 50\n" +
            "Precisión: 100%\n" +
            "Usos: 10\n" +
            "Costo Fe: 30",
        "⚠️ Cuidado: Consume mucha Fe\n" +
            "💡 Tip: Ideal para rematar enemigos peligrosos",
        CategoriaEnciclopedia.ATAQUE_ESPECIAL,
        ClaseEnciclopedia.PELEADOR,
        4
    ),

    // ========== ATAQUES DEL MÁGICO ==========
    HECHIZO_BASICO(
        "Hechizo Básico",
        "Un hechizo elemental fundamental. Confiable y con muchos usos.",
        "Daño: 60\n" +
            "Precisión: 90%\n" +
            "Usos: 50",
        "💡 Tip: Tu pan de cada día en combates largos",
        CategoriaEnciclopedia.ATAQUE_MAGICO,
        ClaseEnciclopedia.MAGICO,
        1
    ),

    BOLA_DE_FUEGO(
        "Bola de Fuego",
        "Una esfera ardiente que causa gran daño. Menos precisa pero devastadora.",
        "Daño: 90\n" +
            "Precisión: 70%\n" +
            "Usos: 10",
        "🔥 Efecto: Puede quemar al enemigo\n" +
            "💡 Tip: Mejor contra enemigos lentos",
        CategoriaEnciclopedia.ATAQUE_MAGICO,
        ClaseEnciclopedia.MAGICO,
        3
    ),

    BANIO_TOTAL(
        "Baño Total",
        "Un hechizo de agua que golpea con fuerza moderada pero nunca falla.",
        "Daño: 50\n" +
            "Precisión: 100%\n" +
            "Usos: 10",
        "💧 Efecto: Siempre impacta\n" +
            "💡 Tip: Útil contra enemigos evasivos",
        CategoriaEnciclopedia.ATAQUE_MAGICO,
        ClaseEnciclopedia.MAGICO,
        2
    ),

    CONCENTRACION(
        "Concentración",
        "Te concentras para aumentar tu poder mágico temporalmente.",
        "Daño: 0\n" +
            "Precisión: 100%\n" +
            "Usos: 10\n" +
            "Efecto: Aumenta daño mágico",
        "💡 Tip: Combínalo con Bola de Fuego",
        CategoriaEnciclopedia.ATAQUE_BUFF,
        ClaseEnciclopedia.MAGICO,
        2
    ),

    // ========== ATAQUES DEL SOPORTE ==========
    BESO_DE_ANGEL(
        "Beso de Angel",
        "Una bendición divina que restaura tu salud. Usos ilimitados.",
        "Curación: Variable\n" +
            "Precisión: 100%\n" +
            "Usos: Infinitos",
        "😇 Efecto: Cura HP\n" +
            "💡 Tip: Tu salvavidas en combates largos",
        CategoriaEnciclopedia.ATAQUE_CURACION,
        ClaseEnciclopedia.SOPORTE,
        2
    ),

    EN_EL_NOMBRE_DEL_PADRE(
        "En el nombre del padre del hijo y del espíritu santo",
        "Una oración sagrada que causa daño divino a los enemigos.",
        "Daño: 77\n" +
            "Precisión: 100%\n" +
            "Usos: 2",
        "✝️ Efecto: Daño sagrado\n" +
            "⚠️ Nota: Pocos usos, úsalo sabiamente",
        CategoriaEnciclopedia.ATAQUE_SAGRADO,
        ClaseEnciclopedia.SOPORTE,
        5
    ),

    // ========== ITEMS ==========
    VINO_MEDIO_VACIO(
        "Vino Medio Vacío",
        "Un trago de vino que restaura un poco de salud. ¿O está medio lleno?",
        "Curación: 20 HP\n" +
            "Precio: 30 monedas",
        "🍷 Filosofía: La perspectiva lo es todo\n" +
            "💡 Tip: Barato y efectivo para emergencias",
        CategoriaEnciclopedia.ITEM_CONSUMIBLE,
        null,
        1
    ),

    ANGEL_BOSTEZANDO(
        "Pequeño Ángel Bostezando",
        "Una estatuilla sagrada que cura heridas moderadas.",
        "Curación: 30 HP\n" +
            "Precio: 40 monedas",
        "😇 El ángel está cansado pero sigue ayudando\n" +
            "💡 Tip: Mejor relación calidad-precio",
        CategoriaEnciclopedia.ITEM_CONSUMIBLE,
        null,
        2
    ),

    ZANAHORIA_INTERDIMENSIONAL(
        "Zanahoria Interdimensional",
        "Una zanahoria que atravesó el espacio-tiempo. Cura mucho.",
        "Curación: 50 HP\n" +
            "Precio: 70 monedas",
        "🥕 Viene de otra dimensión\n" +
            "💡 Tip: Cara pero poderosa",
        CategoriaEnciclopedia.ITEM_CONSUMIBLE,
        null,
        3
    ),

    // ========== ENEMIGOS ==========
    MINI_ON(
        "Mini-on",
        "Una criatura pequeña del Limbo. No subestimes su mordisco.",
        "HP: 70\n" +
            "Defensa: 50\n" +
            "Daño: 45\n" +
            "Ataques: Mordisco, Rasguño, Amenaza, Agrandamiento",
        "📍 Ubicación: Piso 1 - Limbo\n" +
            "💡 Estrategia: Ataca primero, son frágiles",
        CategoriaEnciclopedia.ENEMIGO_NORMAL,
        null,
        1
    ),

    ESBIRRO(
        "Esbirro",
        "Un sirviente del infierno con mucha resistencia.",
        "HP: 200\n" +
            "Defensa: 30\n" +
            "Daño: 40\n" +
            "Ataques: Mordisco, Rasguño, Amenaza, Agrandamiento",
        "📍 Ubicación: Piso 1 - Limbo\n" +
            "💡 Estrategia: Mucho HP, prepárate para combate largo",
        CategoriaEnciclopedia.ENEMIGO_NORMAL,
        null,
        2
    ),

    MINIBOSS_LIMBO(
        "Sabueso del Limbo",
        "El guardián del primer círculo. Un enemigo formidable.",
        "HP: 300\n" +
            "Defensa: 60\n" +
            "Daño: 65\n" +
            "Ataques: Doble Golpe, Mordisco, Amenaza, Agrandamiento",
        "📍 Ubicación: Piso 1 - Limbo (Nivel 4)\n" +
            "⚠️ BOSS: Requiere estrategia\n" +
            "💡 Tip: Llega con HP y items completos",
        CategoriaEnciclopedia.ENEMIGO_JEFE,
        null,
        4
    ),

    BOSS_FINAL(
        "Caos",
        "La personificación del caos absoluto. El enemigo más poderoso.",
        "HP: 500\n" +
            "Defensa: 70\n" +
            "Daño: 80\n" +
            "Ataques: ???",
        "📍 Ubicación: Piso 5 - Traición\n" +
            "💀 BOSS FINAL\n" +
            "⚠️ No hay vuelta atrás\n" +
            "💡 Estrategia: Usa todo lo que tienes",
        CategoriaEnciclopedia.ENEMIGO_JEFE,
        null,
        5
    );

    // ========== ATRIBUTOS ==========
    private final String nombre;
    private final String descripcion;
    private final String estadisticas;
    private final String notas;
    private final CategoriaEnciclopedia categoria;
    private final ClaseEnciclopedia claseAsociada;
    private final int rareza; // 1-5 estrellas

    // ========== CONSTRUCTOR ==========
    EntradaEnciclopedia(String nombre, String descripcion, String estadisticas,
                        String notas, CategoriaEnciclopedia categoria,
                        ClaseEnciclopedia claseAsociada,int rareza) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estadisticas = estadisticas;
        this.notas = notas;
        this.categoria = categoria;
        this.claseAsociada = claseAsociada;
        this.rareza = rareza;
    }

    // ========== GETTERS ==========
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getEstadisticas() { return estadisticas; }
    public String getNotas() { return notas; }
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

        sb.append("DESCRIPCIÓN:\n");
        sb.append(descripcion).append("\n\n");

        sb.append("ESTADÍSTICAS:\n");
        sb.append(estadisticas).append("\n\n");

        if (notas != null && !notas.isEmpty()) {
            sb.append("NOTAS:\n");
            sb.append(notas).append("\n\n");
        }

        sb.append("Categoría: ").append(categoria.getNombre()).append("\n");
        if (claseAsociada != null) {
            sb.append("Clase: ").append(claseAsociada.getNombre()).append("\n");
        }

        return sb.toString();
    }
}
