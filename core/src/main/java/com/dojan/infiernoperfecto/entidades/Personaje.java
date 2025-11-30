package com.dojan.infiernoperfecto.entidades;

import java.util.List;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.EfectoSecundario;
import com.dojan.infiernoperfecto.ataques.efectos.ModificacionEstadistica;
import com.dojan.infiernoperfecto.ataques.efectos.TipoEfecto;
import com.dojan.infiernoperfecto.entidades.clases.Clase;
import com.dojan.infiernoperfecto.entidades.estados.EstadoAlterado;
import com.dojan.infiernoperfecto.entidades.estados.EstadoEstadisticaModificada;
import com.dojan.infiernoperfecto.items.ItemCura;
import com.dojan.infiernoperfecto.utiles.Random;

public abstract class Personaje {

    protected String nombre;
    protected Clase clase;
    protected float vidaBase;
    protected float vidaActual;
    protected int danioBase;
    protected int defensaBase;
    protected List<Ataque> ataques;
    protected int feBase;
    protected int feActual;
    protected int monedasBase;
    protected int monedasActual;
    private EstadoAlterado estadoAlterado;


    public Personaje(String nombre, int vida, int danio, int defensa, int monedas,List<Ataque> ataques) {
        this.nombre = nombre;
        this.vidaBase = vida;
        this.vidaActual = vida;
        this.danioBase = danio;
        this.defensaBase = defensa;
        this.monedasBase = monedas;
        this.monedasActual = monedas;
        this.ataques = ataques;
        // si la clase está asignada más tarde (constructor de Jugador), la inicialización de fe
        // se hará en el constructor correspondiente. Aquí dejamos fe en 0 por defecto.
        this.feBase = 0;
        this.feActual = 0;
    }

    public Personaje(String nombre, int vida, int danio, int defensa, List<Ataque> ataques) {
        this.nombre = nombre;
        this.vidaBase = vida;
        this.vidaActual = vida;
        this.danioBase = danio;
        this.defensaBase = defensa;
        this.ataques = ataques;
        // si la clase está asignada más tarde (constructor de Jugador), la inicialización de fe
        // se hará en el constructor correspondiente. Aquí dejamos fe en 0 por defecto.
        this.feBase = 0;
        this.feActual = 0;
    }

    public ResultadoAtaque atacar(Personaje objetivo, int ataqueElegido){
        int porcentajeReduccionAtaque = 0;
        int porcentajeReduccionDefensa = 0;
        int porcentajeReduccionPrecision = 0;

        if(this.estadoAlterado != null && this.estadoAlterado instanceof EstadoEstadisticaModificada){
            EstadoEstadisticaModificada estado = (EstadoEstadisticaModificada)this.estadoAlterado;
            switch (estado.getTipoEstadistica()) {
                case DANIO:
                    porcentajeReduccionAtaque = estado.getPorcentaje();
                    break;
                case DEFENSA:
                    porcentajeReduccionDefensa = estado.getPorcentaje();
                    break;
                case PRECISION:
                    porcentajeReduccionPrecision = estado.getPorcentaje();
                    break;
            }
            estado.mostrarInformacion();
        }

        int precisionOriginal = ataques.get(ataqueElegido).getProbabilidadAcierto();
        int precisionModificada = precisionOriginal - porcentajeReduccionPrecision;
        precisionModificada = Math.max(0, Math.min(100, precisionModificada));

        boolean acierta = Random.verificarAcierto(precisionModificada);
        if (acierta) {
            System.out.println(this.nombre + " ataca a " + objetivo.getNombre() + " con " + ataques.get(ataqueElegido).getNombre());
        } else {
            System.out.println(this.nombre + " falla el ataque a " + objetivo.getNombre());
        }

        if(ataques.get(ataqueElegido).getCantUsos()<=0){
            System.out.println("Te quedaste sin usos");
        }else{
            // Verificar costo de Fe antes de ejecutar el ataque
            Ataque ataque = ataques.get(ataqueElegido);
            int costoFe = ataque.getCostoFe();
            if (costoFe > 0) {
                if (this.feActual < costoFe) {
                    System.out.println("No tienes suficiente Fe para usar " + ataque.getNombre());
                    return ResultadoAtaque.empty(); // ataque no realizado
                } else {
                    // Restar la Fe correspondiente al atacante
                    this.feActual -= costoFe;
                    if (this.feActual < 0) this.feActual = 0;
                }
            }

            this.ataques.get(ataqueElegido).restarUso();
            if (acierta){
                float danioBaseAtaque = ataques.get(ataqueElegido).getDanio();

                // ============================================================
                // ← AQUÍ EMPIEZA EL CÓDIGO NUEVO (REEMPLAZA LA SECCIÓN VIEJA)
                // ============================================================

                EfectoSecundario efecto = ataques.get(ataqueElegido).getEfectoSecundario();
                String mensajeEfecto = null;

                if(efecto != null && Random.verificarAcierto(efecto.getProbabilidad())){
                    TipoEfecto tipoEfecto = efecto.getTipoEfecto();

                    // Si es un buff/debuff (MODIFICACION_STAT)
                    if (tipoEfecto == TipoEfecto.MODIFICACION_STAT) {
                        // Determinar si es buff (se aplica al atacante) o debuff (al objetivo)
                        if (efecto instanceof ModificacionEstadistica) {
                            ModificacionEstadistica modEfecto = (ModificacionEstadistica) efecto;
                            // Si el porcentaje es NEGATIVO, es un BUFF que se aplica al atacante
                            if (modEfecto.getPorcentajeMin() < 0) {
                                mensajeEfecto = efecto.aplicar(this); // Aplicar al atacante
                            } else {
                                // Si es POSITIVO, es un DEBUFF que se aplica al objetivo
                                mensajeEfecto = efecto.aplicar(objetivo);
                            }
                        }
                    }
                    // Curaciones, recuperación de Fe, etc. siempre al atacante
                    else if (tipoEfecto == TipoEfecto.CURACION ||
                        tipoEfecto == TipoEfecto.RECUPERAR_FE ||
                        tipoEfecto == TipoEfecto.LIMPIAR_ESTADO) {
                        mensajeEfecto = efecto.aplicar(this);
                    }
                    // Por defecto, aplicar al objetivo
                    else {
                        mensajeEfecto = efecto.aplicar(objetivo);
                    }
                }

                // ============================================================
                // ← AQUÍ TERMINA EL CÓDIGO NUEVO
                // ============================================================

                // Calcular daño si el ataque lo tiene
                float danioFinal = 0;
                if (danioBaseAtaque > 0) {
                    float danioReducido = danioBaseAtaque * (1 - porcentajeReduccionAtaque / 100.0f);
                    float danioHecho = this.danioBase + danioReducido;
                    danioFinal = Math.max(0, danioHecho - objetivo.getDefensaBase());
                    objetivo.recibirDanio(danioFinal);
                    System.out.println("daño final: "+danioFinal);
                    System.out.println("El danio a "+objetivo.getNombre()+" es de "+danioFinal);
                }

                return new ResultadoAtaque(danioFinal, mensajeEfecto);
            }else{
                System.out.println("Fallaste el ataque");
            }

        }

        if (this.estadoAlterado != null) {
            this.estadoAlterado.reducirTurno();
            if (this.estadoAlterado.getTurnos() == 0) {
                System.out.println(this.nombre + " ha recuperado su estado normal.");
                this.estadoAlterado = null;
            }
        }

        return ResultadoAtaque.empty();
    }

    public void recibirDanio(float cantidad){
        this.vidaActual -= cantidad;
    }

    // curacion de vida
    public void curar(int cantidad) {
        this.vidaActual += cantidad;
        if (this.vidaActual > this.vidaBase) {
            this.vidaActual = this.vidaBase;
        }
    }

    // recuperacion de fe
    public void recuperarFe(int cantidad) {
        this.feActual += cantidad;
        if (this.feActual > this.feBase) {
            this.feActual = this.feBase;
        }
    }

    public boolean tieneEstadoAlterado() {
        return this.estadoAlterado != null;
    }

    // limpiar estado alterado
    public void limpiarEstadosAlterados() {
        if (this.estadoAlterado != null) {
            this.estadoAlterado = null;
        }
    }


    public void aplicarEstadoAlterado(EstadoAlterado estadoAlterado) {
        this.estadoAlterado = estadoAlterado;
    }


    public boolean sigueVivo(){
        if (this.vidaActual <= 0){
            return false;
        }else {
            return true;
        }
    }

    public boolean comprar(ItemCura itemSeleccionado){
        if(this.monedasActual < itemSeleccionado.getPrecio()){
            System.out.println("No tienes suficiente dinero");
            return false;
        }else if(this.monedasActual >= itemSeleccionado.getPrecio()){
            System.out.println("Compraste "+itemSeleccionado.getNombre());
            this.monedasActual -= itemSeleccionado.getPrecio();
            if (this.monedasActual < 0){
                this.monedasActual = 0;
            }
            this.vidaActual += itemSeleccionado.getCantidadCura();
            if (this.vidaActual > this.vidaBase) {
                this.vidaActual = this.vidaBase;
            }
            return true;
        }
        return false;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public List<Ataque> getAtaques() {
        return ataques;
    }

    public Clase getClase() {
        return clase;
    }

    public float getVidaBase() {
        return vidaBase;
    }

    public float getVidaActual() {
        return vidaActual;
    }

    public int getFeActual() {
        return feActual;
    }

    public void setFeBase(int feBase) {
        this.feBase = feBase;
        this.feActual = feBase;
    }

    public int getMonedasBase(){
        return monedasBase;
    }

    public int getMonedasActual(){
        return monedasActual;
    }

    public void setMonedasActuales(int monedas) {
        this.monedasActual += monedas;
    }
}
