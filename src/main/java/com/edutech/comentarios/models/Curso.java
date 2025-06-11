package com.edutech.comentarios.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "curso")
@Data
public class Curso {

    @Id
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "nombre_curso")
    private String nombreCurso;
}
