package com.edutech.comentarios.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ComentarioDTO {

    private Integer idComentario;
    private int calificacion;
    private String comentario;
    private LocalDate fecha;

    // Para saber quién escribió el comentario
    private UsuarioInfoDTO usuario; 

    // Para saber a qué curso pertenece el comentario
    private CursoInfoDTO curso;
}
