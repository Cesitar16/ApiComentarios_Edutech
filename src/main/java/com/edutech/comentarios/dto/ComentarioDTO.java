package com.edutech.comentarios.dto;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ComentarioDTO extends RepresentationModel<ComentarioDTO> {

    private Integer idComentario;
    private int calificacion;
    private String comentario;
    private LocalDate fecha;

    // Para saber quién escribió el comentario
    private UsuarioInfoDTO usuario; 

    // Para saber a qué curso pertenece el comentario
    private CursoInfoDTO curso;
}
